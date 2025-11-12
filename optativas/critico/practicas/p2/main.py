from fastapi import FastAPI
from datetime import datetime
import time

from bd import RedisManager


app = FastAPI()


@app.get("/nuevo")
def home(dato: float):
    return f"Este es el dato {dato}"


@app.get("/listar")
def getLista():
    return "Lista completa"


if __name__ == "__main__":
    dbManager = RedisManager()
    dbManager.clearData()
    dbManager.addValue(10.2)
    dbManager.addValue(23.2)
    dbManager.addValue(102.2)

    dbManager.showValues()
