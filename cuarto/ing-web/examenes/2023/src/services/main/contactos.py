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
contactos_bp = Blueprint("contactos", __name__)

client = pymongo.MongoClient(MONGODB_URL)
db = client.examen2023
usuarios = db.usuarios
contactos = db.contactos

@contactos_bp.route("/", methods=["GET"])
def get_contactos(email):
    try:
        query = {"email": email}
        if request.args:
            if "nombreContacto" in request.args:
                query["nombreContacto"] = {"$regex": request.args["nombreContacto"], "$options": "i"}

        contactos_list = contactos.find(query)
        return jsonify(json.loads(json_util.dumps(contactos_list))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@contactos_bp.route("/<emailContacto>", methods=["GET"])
def get_contacto(email, emailContacto):
    try:
        if not usuarios.find_one({"email": email}):
            return jsonify({"error": "El email del usuario no existe"}), 400
        if not usuarios.find_one({"email": emailContacto}):
            return jsonify({"error": "El email del contacto no existe"}), 400

        contacto = contactos.find({"email": email, "emailContacto": emailContacto})
        return jsonify(json.loads(json_util.dumps(contacto))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@contactos_bp.route("/", methods=["POST"])
def create_contacto(email):
    try:
        query = {"email": email}
        data = request.json
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400
        if "emailContacto" not in data:
            return jsonify({"error": "El email del contacto es requerido"}), 400
        if "nombreContacto" not in data:
            return jsonify({"error": "El nombre del contacto es requerido"}), 400

        if not usuarios.find_one({"email": email}):
            return jsonify({"error": "El email del usuario no existe"}), 400
        if not usuarios.find_one({"email": data["emailContacto"]}):
            return jsonify({"error": "El email del contacto no existe"}), 400

        query["emailContacto"] = data["emailContacto"]
        query["nombreContacto"] = data["nombreContacto"]

        if contactos.find_one({"email": email, "emailContacto": query["emailContacto"]}):
            return jsonify({"error": "El contacto ya está registrado"}), 400

        contacto = contactos.insert_one(query)
        return jsonify({"mensaje": "Contacto creado exitosamente", "id": str(contacto.inserted_id)}), 201
    except Exception as e:
        return jsonify({"Los datos no son correctos": str(e)}), 400

@contactos_bp.route("/<emailContacto>", methods=["DELETE"])
def delete_contacto(email, emailContacto):
    try:
        query = {"email": email, "emailContacto": emailContacto}
        res = contactos.find_one_and_delete(query)
        if not res:
            return jsonify({"error": "El contacto no existe"}), 400
        return jsonify({"mensaje": "Contacto eliminado exitosamente"}), 200
    except Exception as e:
        return jsonify({"Los datos no son correctos": str(e)}), 400
