import socket
from fastapi import FastAPI
from datetime import datetime
import os
import time
from keras.models import load_model
import joblib


from api.bd import RedisManager

app = FastAPI()
redis = RedisManager()

model = load_model("./models/model.keras")
scaler = joblib.load("./models/scaler.pkl")
threshold = 0.98


@app.get("/nuevo")
def home(dato: float):
    redis.addValue(dato)
    return f"Dato insertado: {dato}"


@app.get("/listar")
def getLista():
    data = redis.showValues()
    return {"HOSTNAME": socket.gethostname(), "data": data}


@app.get("/detectar")
def detectar(dato: float):
    res = redis.detectar(dato, model, scaler, threshold)
    return res
