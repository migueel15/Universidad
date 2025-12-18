import sys
import os
import time
import signal
import threading
import random
from kazoo.protocol.states import KazooState
from kazoo.recipe.counter import Counter
import requests

from kazoo.client import KazooClient
from kazoo.recipe.election import Election
from kazoo.recipe.barrier import Barrier
from kazoo.recipe.watchers import ChildrenWatch, DataWatch

DEFAULT_SAMPLING_PERIOD = 5
DEFAULT_API_URL = "http://api:80"

ELECTION_PATH = "/election"
MEASUREMENTS_PATH = "/mediciones"
BARRIER_PATH = "/barrier"
COUNTER_PATH = "/counter"
CONFIG_PATH = "/config"
CONFIG_SAMPLING_PATH = f"{CONFIG_PATH}/sampling_period"
CONFIG_API_PATH = f"{CONFIG_PATH}/api_url"


class MedicionesApp:
    def __init__(self, device_id: str):
        self.device_id = device_id
        self.zk_hosts = os.getenv("ZOOKEEPER_HOSTS", "127.0.0.1:2181")

        self.api_url = DEFAULT_API_URL
        self.sampling_period = DEFAULT_SAMPLING_PERIOD

        self.zk_ready = False
        self.running = True

        self.zk = KazooClient(hosts=self.zk_hosts)
        self.zk.add_listener(self.zk_state_listener)
        self.election: Election | None = None
        self.barrier = None
        self.counter = None

    def shutdown_handler(self, sig, frame):
        print(f"[{self.device_id}] Señal recibida, terminando sincronizacion...")
        self.running = False

    def zk_state_listener(self, state):
        if state == KazooState.SUSPENDED:
            print(f"[{self.device_id}] ZooKeeper SUSPENDIDO")
            self.zk_ready = False

        elif state == KazooState.CONNECTED:
            print(f"[{self.device_id}] ZooKeeper CONECTADO")
            self.zk_ready = True
            self.on_reconnected()

        elif state == KazooState.LOST:
            print(f"[{self.device_id}] ZooKeeper SESIÓN PERDIDA")
            self.zk_ready = False

    def on_reconnected(self):
        print(f"[{self.device_id}] Reconfigurando recetas")

        self.barrier = Barrier(self.zk, BARRIER_PATH)
        self.counter = Counter(self.zk, COUNTER_PATH)

        self.election = Election(self.zk, ELECTION_PATH, self.device_id)
        threading.Thread(target=self.run_election, daemon=True).start()

    def generate_random_number(
        self, min_val: float = 0.0, max_val: float = 100.0
    ) -> float:
        return random.uniform(min_val, max_val)

    def save_new_value(self, value: float):
        path = f"{MEASUREMENTS_PATH}/{self.device_id}"
        data = str(value).encode("utf-8")
        if self.zk.exists(path):
            self.zk.set(path, data)
        else:
            self.zk.create(path, data, ephemeral=True)

    def calc_mean(self) -> float | None:
        print("Calculando mediciones...")
        try:
            children = self.zk.get_children(MEASUREMENTS_PATH)
            values: list[float] = []
            for child in children:
                data, _ = self.zk.get(f"{MEASUREMENTS_PATH}/{child}")
                values.append(float(data.decode("utf-8")))
            if values:
                return sum(values) / len(values)
        except Exception as e:
            print(f"Error calculando la media: {e}")
        return None

    def send_mediciones(self, value: float):
        print(f"Mandando mediciones: {value}")
        params = {"dato": value}
        try:
            response = requests.get(f"{self.api_url}/nuevo", params=params, timeout=2)
        except Exception as e:
            print(f"Error enviando mediciones: {e}")

    def on_device_change(self, children):
        print(f"[Líder] Dispositivos activos: {children}")
        return True

    def on_sampling_period_change(self, data, stat):
        if data:
            new_period = int(data.decode("utf-8"))
            print(f"[{self.device_id}] Nuevo período de muestreo: {new_period}")
            self.sampling_period = new_period
        else:
            print(f"[{self.device_id}] Nodo de período de muestreo eliminado")
        return True

    def on_api_url_change(self, data, stat):
        if data:
            new_url = data.decode("utf-8")
            print(f"[{self.device_id}] Nueva URL de API: {new_url}")
            self.api_url = new_url
        else:
            print(f"[{self.device_id}] Nodo de URL de API eliminado")
        return True

    def leader_loop(self):
        print(f"[{self.device_id}] SOY EL LÍDER")
        ChildrenWatch(self.zk, MEASUREMENTS_PATH, self.on_device_change)

        while self.running:
            if not self.zk_ready:
                time.sleep(1)
                continue

            try:
                self.barrier.create()
                time.sleep(self.sampling_period)

                mean = self.calc_mean()
                if mean is not None:
                    self.send_mediciones(mean)

                self.barrier.remove()

            except Exception as e:
                print(f"[{self.device_id}] Error líder, esperando reconexión: {e}")
                time.sleep(1)

    def run_election(self):
        if self.election is None:
            return
        self.election.run(self.leader_loop)

    def setup(self):
        signal.signal(signal.SIGINT, self.shutdown_handler)
        signal.signal(signal.SIGTERM, self.shutdown_handler)

        self.zk.start()
        print(f"[{self.device_id}] Aplicación arrancada")
        print(f"[{self.device_id}] ZooKeeper = {self.zk_hosts}")

        self.zk.ensure_path(ELECTION_PATH)
        self.zk.ensure_path(MEASUREMENTS_PATH)
        self.zk.ensure_path(BARRIER_PATH)
        self.zk.ensure_path(CONFIG_PATH)

        if self.zk.exists(CONFIG_SAMPLING_PATH):
            data, _ = self.zk.get(CONFIG_SAMPLING_PATH)
            self.sampling_period = int(data.decode())
        if self.zk.exists(CONFIG_API_PATH):
            data, _ = self.zk.get(CONFIG_API_PATH)
            self.api_url = data.decode()

        DataWatch(self.zk, CONFIG_SAMPLING_PATH, self.on_sampling_period_change)
        DataWatch(self.zk, CONFIG_API_PATH, self.on_api_url_change)

        self.election = Election(self.zk, ELECTION_PATH, self.device_id)
        self.barrier = Barrier(self.zk, BARRIER_PATH)
        self.barrier.create()
        self.counter = Counter(self.zk, COUNTER_PATH)
        threading.Thread(target=self.run_election, daemon=True).start()

    def run(self):
        self.setup()
        while self.running:
            if not self.zk_ready:
                time.sleep(1)
                continue
            try:
                self.barrier.create()
                value = self.generate_random_number()
                self.save_new_value(value)
                self.counter += 1
                print(f"[{self.device_id}] Iteración global: {self.counter.value}")
                self.barrier.wait()
            except Exception as e:
                print(f"[{self.device_id}] Error ZooKeeper, reintentando: {e}")
                time.sleep(1)

        self.zk.stop()


if __name__ == "__main__":

    if len(sys.argv) != 2:
        sys.exit(1)

    DEVICE_ID = sys.argv[1]
    app = MedicionesApp(DEVICE_ID)
    app.run()
