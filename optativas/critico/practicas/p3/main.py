import sys
import os
import time
import signal
import threading

from kazoo.client import KazooClient
from kazoo.recipe.election import Election
from kazoo.recipe.watchers import ChildrenWatch


if len(sys.argv) != 2:
    sys.exit(1)

DEVICE_ID = sys.argv[1]

ZOOKEEPER_HOSTS = os.getenv("ZOOKEEPER_HOSTS", "127.0.0.1:2181")

ELECTION_PATH = "/election"
MEASUREMENTS_PATH = "/mediciones"
CONFIG_PATH = "/config"
CONFIG_SAMPLING_PATH = f"{CONFIG_PATH}/sampling_period"
CONFIG_API_PATH = f"{CONFIG_PATH}/api_url"
api_url = "http://localhost:4000"

running = True


def shutdown_handler(sig, frame):
    global running
    print(f"[{DEVICE_ID}] Señal recibida, cerrando...")
    running = False


def on_device_change(children):
    print(f"[Lider] Dispositivos activos: {children}")
    return True


def leader_loop():
    print(f"[{DEVICE_ID}] SOY EL LÍDER")
    ChildrenWatch(zk, MEASUREMENTS_PATH, on_device_change)

    while running:
        # Aquí irá más adelante:
        # - leer mediciones
        # - calcular media
        # - POST a la API
        time.sleep(5)
        print(f"[{DEVICE_ID}] Líder activo")


def run_election():
    election.run(leader_loop)


signal.signal(signal.SIGINT, shutdown_handler)
signal.signal(signal.SIGTERM, shutdown_handler)

zk = KazooClient(hosts=ZOOKEEPER_HOSTS)

zk.start()

print(f"[{DEVICE_ID}] Aplicación arrancada")
print(f"[{DEVICE_ID}] ZooKeeper = {ZOOKEEPER_HOSTS}")

zk.ensure_path(ELECTION_PATH)
zk.ensure_path(MEASUREMENTS_PATH)
zk.ensure_path(CONFIG_PATH)
zk.ensure_path(CONFIG_SAMPLING_PATH)
zk.ensure_path(CONFIG_API_PATH)

zk.set(CONFIG_SAMPLING_PATH, b"5")
zk.set(CONFIG_API_PATH, api_url.encode("utf-8"))

election = Election(zk, ELECTION_PATH, DEVICE_ID)

zk.create(f"{MEASUREMENTS_PATH}/{DEVICE_ID}", b"online", ephemeral=True)


threading.Thread(target=run_election, daemon=True).start()

while running:
    time.sleep(1)


print(f"[{DEVICE_ID}] Cerrando conexión ZooKeeper")
zk.stop()
