import redis
import os

REDIS_HOST = os.getenv("REDIS_HOST", "localhost")


class RedisManager:

    def __init__(self, host=REDIS_HOST, port=6379):

        try:
            client = redis.Redis(
                host=host,
                port=port,
                decode_responses=True,
                socket_connect_timeout=2,
                socket_timeout=2,
            )
            client.ping()
            self.client = client

        except Exception as e:
            print(f"Error al establecer la conexion {e}")

    def clearData(self):
        self.client.flushall()

    def addValue(self, nuevoValor: float, timestamp="*", timeSerieId="ts:1"):
        try:
            ts = self.client.ts()
            ts.add(key=timeSerieId, timestamp=timestamp, value=nuevoValor)
        except Exception as e:
            print("Error al añadir el valor")

    def showValues(self, timeSerieId="ts:1"):
        ts = self.client.ts()
        data = ts.range(timeSerieId, "-", "+")
        print(data)

    def close(self):
        self.client.close()
        print("Conexcion cerrada con redis")
