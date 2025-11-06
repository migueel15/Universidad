import os
from typing import List, Literal, Tuple, Union
import pymongo
from datetime import datetime
from dotenv import load_dotenv

from DatabaseConnection import DatabaseConnection

load_dotenv()

# para no tener que usar .env he puesto los valores por defecto
DB_URI = os.getenv("DB_URI", "mongodb://admin:admin@localhost:27017")
DB_NAME = os.getenv("DB_NAME", "techworld")


class ProductManager:
    def __init__(self):

        db = DatabaseConnection().connect(DB_URI, DB_NAME)
        if db is not None:
            self.db = db
            print("ProductManager inicializado")
        else:
            print("No se pudo conectar a la base de datos")
            raise Exception("Error de conexión a la base de datos")

    def flushDatabase(self):
        try:
            print("Base de datos limpia")
            collections = self.db.list_collection_names()
            for collection in collections:
                self.db[collection].drop()
        except Exception as e:
            print(f"✗ Error al vaciar la BD: {e}")

    def loadData(self, collection_name, json_data):
        try:
            collection = self.db[collection_name]

            if isinstance(json_data, list):
                result = collection.insert_many(json_data)
                print(
                    f"Insertados {len(result.inserted_ids)} datos a coleccion {collection_name}"
                )

            if isinstance(json_data, dict):
                result = collection.insert_one(json_data)
                print(
                    f"Documento {result.inserted_id} insertado a coleccion {collection_name}"
                )
        except Exception as e:
            print(f"Error al insertar datos: {e}")

    def find(
        self,
        collection: Literal["clientes", "productos", "ventas"],
        filter={},
        returnProps={},
    ):
        return list(self.db[collection].find(filter, returnProps))

    def update(
        self,
        collection_name,
        filter,
        update,
        type: Literal["set", "unset", "inc", "push", "pull"] = "set",
    ):
        try:
            collection = self.db[collection_name]
            result = collection.update_many(filter, {f"${type}": update})
            return result.modified_count
        except Exception as e:
            print(f"Error al actualizar: {e}")

    def createIndex(
        self,
        collection_name,
        fields: Union[str, List[Tuple[str, int]]],
        unique: bool = False,
        name: str = None,
    ):
        try:
            if name is None:
                return None

            result = self.db[collection_name].create_index(
                fields, unique=unique, name=name
            )

            print(f"Indice {result} creado en {collection_name}")

        except Exception as e:
            print("No es posible crear el indice")

    def pipeline(self, collection_name, pipeline):
        try:
            result = self.db[collection_name].aggregate(pipeline)
            return list(result)

        except Exception as e:
            print("Error al ejecutar el pipeline")
