import pymongo
from pymongo import MongoClient
from pymongo.errors import ServerSelectionTimeoutError


def singleton(cls):
    instances = {}

    def get_instance():
        if cls not in instances:
            instances[cls] = cls()
        return instances[cls]

    return get_instance


@singleton
class DatabaseConnection:
    def __init__(self):
        self.client = None

    def connect(self, uri_string, db_name):
        try:
            print("Conectando a la base de datos...")
            self.client = MongoClient(uri_string, serverSelectionTimeoutMs=5000)
            self.client.admin.command("ping")
            print("Conexion con la base de datos establecida.")
            return self.client[db_name]
        except Exception as e:
            print(
                f"Error en la conexion: Compruebe que la base de datos esté disponible"
            )
            self.client = None
            return None

    def disconnect(self):
        if self.client:
            self.client.close()
            self.client = None
            print("Conexion con la base de datos cerrada.")
