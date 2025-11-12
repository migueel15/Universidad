import redis


class RedisManager:

    def __init__(self, host="localhost", port=6379):

        try:
            client = redis.Redis(host=host, port=port, decode_responses=True)
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
