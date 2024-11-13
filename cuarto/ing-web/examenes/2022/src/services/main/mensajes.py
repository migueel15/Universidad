
import datetime
import json
import os

import pymongo
import requests
from bson import json_util
from bson.objectid import ObjectId
from dotenv import load_dotenv
from flask import Blueprint, current_app, jsonify, request

load_dotenv()
MONGODB_URL = os.getenv("MONGODB_URL")
mensajes_bp = Blueprint("mensajes", __name__)

client = pymongo.MongoClient(MONGODB_URL)
db = client.examen2022
mensajes = db.mensajes
contactos = db.contactos

#CRUD de mensajes (GET ALL, GET, POST, PUT, DELETE).
# Atributos de un mensaje: id, timestmap, origen, destino, texto (max 400 chars).
@mensajes_bp.route("/", methods=["GET"])
def get_mensajes():
    try:
        params = {}
        if "origen" in request.args:
            params["origen"] = request.args["origen"]
        if "destino" in request.args:
            params["destino"] = request.args["destino"]
        msgs = mensajes.find(params)
        if msgs is None:
            return jsonify({"error": "No se encontraron mensajes"}), 400
        return jsonify(json.loads(json_util.dumps(msgs))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@mensajes_bp.route("/<telefono>", methods=["GET"])
def get_mensaje(telefono):
    try:
        params = {}
        if "texto" in request.args:
            params["texto"] = {"$regex": request.args["texto"], "$options": "i"}
        msg = mensajes.find({"$or": [{"origen": telefono}, {"destino": telefono}], **params})
        if msg is None:
            return jsonify({"error": "Mensaje no encontrado"}), 404
        return jsonify(json.loads(json_util.dumps(msg))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@mensajes_bp.route("/", methods=["POST"])
def post_mensaje():
    try:
        data = request.json
        requestData = {}
        if data is None:
            return jsonify({"error": "No se encontró el body"}), 400
        if "origen" not in data:
            return jsonify({"error": "Origen es requerido"}), 400
        if "destino" not in data:
            return jsonify({"error": "Destino es requerido"}), 400
        if "texto" not in data:
            return jsonify({"error": "Texto es requerido"}), 400
        if len(data["texto"]) > 400:
            return jsonify({"error": "Texto no puede ser mayor a 400 caracteres"}), 400

        requestData["origen"] = str(data["origen"])
        requestData["destino"] = str(data["destino"])
        requestData["texto"] = str(data["texto"])
        requestData["timestamp"] = str(datetime.datetime.now())

        mensajes.insert_one(requestData)
        return jsonify({"success": "Mensaje enviado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

# enviar mensaje a un contacto a partir de su alias.
@mensajes_bp.route("<telefono>/enviar/<alias>", methods=["POST"])
def post_mensaje_alias(telefono, alias):
    try:
        contacto = contactos.find_one({"telefono": telefono, "aliasContacto": alias})
        if contacto is None:
            return jsonify({"error": "Usuario no encontrado"}), 404
        data = request.json
        requestData = {}
        if data is None:
            return jsonify({"error": "No se encontró el body"}), 400
        if "texto" not in data:
            return jsonify({"error": "Texto es requerido"}), 400
        if len(data["texto"]) > 400:
            return jsonify({"error": "Texto no puede ser mayor a 400 caracteres"}), 400

        requestData["origen"] = str(telefono)
        requestData["destino"] = str(contacto["telefonoContacto"])
        requestData["texto"] = str(data["texto"])
        requestData["timestamp"] = str(datetime.datetime.now())

        mensajes.insert_one(requestData)
        return jsonify({"success": "Mensaje enviado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@mensajes_bp.route("/<id>", methods=["PUT"])
def put_mensaje(id):
    try:
        data = request.json
        requestData = {}
        if data is None:
            return jsonify({"error": "No se encontró el body"}), 400
        if "texto" not in data:
            return jsonify({"error": "Texto es requerido"}), 400
        if len(data["texto"]) > 400:
            return jsonify({"error": "Texto no puede ser mayor a 400 caracteres"}), 400

        requestData["texto"] = str(data["texto"])
        requestData["timestamp"] = str(datetime.datetime.now())

        mensajes.update_one({"_id": ObjectId(id)}, {"$set": requestData})
        return jsonify({"success": "Mensaje actualizado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@mensajes_bp.route("/<id>", methods=["DELETE"])
def delete_mensaje(id):
    try:
        res = mensajes.find_one_and_delete({"_id": ObjectId(id)})
        if res is None:
            return jsonify({"error": "Mensaje no encontrado"}), 404
        return jsonify({"success": "Mensaje eliminado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

#Obtener las conversaciones de un usuario, representadas como una lista de alias de contactos ordenada
#(descendente) por el timestamp del último mensaje enviado o recibido, de forma similar a como se muestra en la
#interfaz de WhatsApp o Telegram
@mensajes_bp.route("/<telefono>/conversaciones", methods=["GET"])
def get_conversaciones(telefono):
    try:
        messages = mensajes.find({"$or": [{"origen": telefono}, {"destino": telefono}]}).sort("timestamp", pymongo.DESCENDING)
        return jsonify(json.loads(json_util.dumps(messages))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

#Obtener los mensajes de una conversación con un contacto, ordenados por timestamp.
@mensajes_bp.route("/<telefono>/conversaciones/<contacto>", methods=["GET"])
def get_conversacion(telefono, contacto):
    try:
        messages = mensajes.find({"$or": [{"origen": telefono, "destino": contacto}, {"origen": contacto, "destino": telefono}]}).sort("timestamp", pymongo.DESCENDING)
        return jsonify(json.loads(json_util.dumps(messages))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400
