import socket
import redis
import time
import os

REDIS_HOST = os.getenv("REDIS_HOST", "localhost")


class RedisManager:

    def __init__(self, host=REDIS_HOST, port=6379, retries=10):

        self.host = host
        self.port = port
        self.retries = retries

        self._connect_with_retry()

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

    def clearData(self):
        self.client.flushall()

    def addValue(self, nuevoValor: float, timestamp="*", timeSerieId="ts:1"):
        try:
            ts = self.client.ts()
            ts.add(key=timeSerieId, timestamp=timestamp, value=nuevoValor)
        except Exception as e:
            print("Error al añadir el valor")

    def showValues(self, timeSerieId="ts:1"):
        try:
            ts = self.client.ts()
            return ts.range(timeSerieId, "-", "+")
        except Exception as e:
            print("Error al mostrar la tabla")

    def close(self):
        self.client.close()
        print("Conexion cerrada con redis")
