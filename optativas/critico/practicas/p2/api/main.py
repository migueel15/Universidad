from fastapi import FastAPI
from datetime import datetime
import os
import time

from api.bd import RedisManager

app = FastAPI()
redis = RedisManager()


@app.get("/nuevo")
def home(dato: float):
    redis.addValue(dato)
    return f"{dato}"


@app.get("/listar")
def getLista():
    redis.showValues()
    return "Lista completa"
