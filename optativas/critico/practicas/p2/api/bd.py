import socket
import redis
from redis import sentinel
from redis.sentinel import Sentinel
import time
import os
import numpy as np

REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
SENTINEL_HOST = os.getenv("SENTINEL_HOST")
SENTINEL_HOST2 = os.getenv("SENTINEL_HOST2")
SENTINEL_HOST3 = os.getenv("SENTINEL_HOST3")
SENTINEL_PORT = os.getenv("SENTINEL_PORT")
SENTINEL_PORT2 = os.getenv("SENTINEL_PORT2")
SENTINEL_PORT3 = os.getenv("SENTINEL_PORT3")


class RedisManager:

    def __init__(self, host=REDIS_HOST, port=6379, retries=10):

        self.host = host
        self.port = port
        self.retries = retries
        self.tsId = "ts:1"

        if SENTINEL_HOST:
            self._connect_with_sentinel()
        else:
            self._connect_with_retry()
        self._ensure_ts_exists()

    def _connect_with_sentinel(self):
        sentinel = Sentinel(
            [
                (SENTINEL_HOST, SENTINEL_PORT),
                (SENTINEL_HOST2, SENTINEL_PORT2),
                (SENTINEL_HOST3, SENTINEL_PORT3),
            ],
            socket_timeout=0.1,
        )
        self.master_info = sentinel.discover_master("mymaster")
        self.client = sentinel.master_for("mymaster", socket_timeout=0.1)
        return True

    def _connect_with_retry(self):
        intento = 0
        delay = 1

        while intento < self.retries:
            try:
                client = redis.Redis(
                    host=self.host,
                    port=self.port,
                    decode_responses=True,
                    socket_connect_timeout=2,
                    socket_timeout=2,
                )
                client.ping()
                self.client = client
                print("Conectado correctamente a Redis.")
                return

            except Exception as e:
                intento += 1
                print(f"[{intento}/{self.retries}] Error de conexión: {e}")
                print(f"Reintentando en {delay} segundos...")
                time.sleep(delay)
                delay = min(delay * 2, 10)

        print("No se pudo conectar a Redis tras múltiples intentos.")

    def _ensure_ts_exists(self):
        ts = self.client.ts()
        try:
            ts.info(self.tsId)
        except Exception:
            ts.create(self.tsId)
            print(f"Serie TS creada: {self.tsId}")

    def clearData(self):
        self.client.flushall()

    def addValue(self, nuevoValor: float, timestamp="*"):
        try:
            ts = self.client.ts()
            ts.add(key=self.tsId, timestamp=timestamp, value=nuevoValor)
            print(f"VALOR: {nuevoValor}")
        except Exception as e:
            print("Error al añadir el valor")

    def showValues(self):
        try:
            ts = self.client.ts()
            return ts.range(self.tsId, "-", "+")
        except Exception as e:
            print("Error al mostrar la tabla")

    def detectar(self, nuevoValor: float, model, scaler, threshold, WINDOW=24):
        try:
            ts = self.client.ts()

            series = ts.range(self.tsId, "-", "+")
            print(series)

            valores = [float(v[1]) for v in series]

            self.addValue(nuevoValor)

            if len(valores) < WINDOW:
                return {
                    "anomalia": "no evaluada",
                    "motivo": f"No hay suficientes valores. Se necesitan {WINDOW}.",
                    "actuales": len(valores),
                }

            ventana = valores[-WINDOW:]

            ventana_np = np.array(ventana).reshape(-1, 1)
            ventana_scaled_individual = scaler.transform(ventana_np)
            ventana_scaled = ventana_scaled_individual.reshape(1, WINDOW, 1)

            pred = model.predict(ventana_scaled)[0][0]
            real = float(nuevoValor)

            denom = max(abs(real), abs(pred), 1e-6)
            error = abs(real - pred) / denom

            anomalía = "si" if error > threshold else "no"

            mediciones_json = [
                {"time": int(t), "valor": float(v)} for (t, v) in series[-WINDOW:]
            ]

            return {"mediciones": mediciones_json, "anomalia": anomalía}

        except Exception as e:
            print(f"Error al detectar anomalia: {e}")

    def close(self):
        self.client.close()
        print("Conexion cerrada con redis")
