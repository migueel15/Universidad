import socket
from fastapi import FastAPI, HTTPException
from keras.models import load_model
import joblib


from api.bd import RedisManager

app = FastAPI()

try:
    redis = RedisManager()
except Exception as e:
    print(f"No se pudo inicializar RedisManager: {e}")
    redis = None

try:
    model = load_model("./models/model.keras")
    scaler = joblib.load("./models/scaler.pkl")
except Exception as e:
    print(f"No se pudo cargar el modelo o el scaler: {e}")
    model = None
    scaler = None

threshold = 0.98


@app.get("/nuevo")
def home(dato: float):
    if not redis:
        raise HTTPException(status_code=503, detail="Redis no está disponible")
    try:
        redis.addValue(dato)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"No se pudo insertar el dato: {e}")
    return {"mensaje": f"Dato insertado: {dato}"}


@app.get("/listar")
def getLista():
    if not redis:
        raise HTTPException(status_code=503, detail="Redis no está disponible")
    try:
        data = redis.showValues()
    except Exception as e:
        raise HTTPException(
            status_code=500, detail=f"No se pudo recuperar la lista: {e}"
        )
    return {"HOSTNAME": socket.gethostname(), "data": data}


@app.get("/detectar")
def detectar(dato: float):
    if not redis:
        raise HTTPException(status_code=503, detail="Redis no está disponible")
    if not model or not scaler:
        raise HTTPException(status_code=503, detail="Modelo no disponible")
    try:
        res = redis.detectar(dato, model, scaler, threshold)
    except Exception as e:
        raise HTTPException(
            status_code=500, detail=f"No se pudo evaluar la anomalia: {e}"
        )
    return res
