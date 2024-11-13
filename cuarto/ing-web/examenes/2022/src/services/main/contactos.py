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
db = client.examen2022
contactos = db.contactos
usuarios = db.usuarios

#CRUD de contactos (GET ALL, GET, POST, PUT, DELETE).

@contactos_bp.route("/", methods=["GET"])
def get_contactos(telefono):
    try:
        if not usuarios.find_one({"telefono": telefono}):
            return jsonify({"error": "Usuario no existe"}), 400

        params = {"telefono": telefono}
        if "alias" in request.args:
            params["aliasContacto"] = {"$regex": request.args["alias"], "$options": "i"}

        contacts = contactos.find(params)
        if contacts is None:
            return jsonify({"error": "No se encontraron contactos"}), 400
        return jsonify(json.loads(json_util.dumps(contacts))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@contactos_bp.route("/<contacto>", methods=["GET"])
def get_contacto(telefono, contacto):
    try:
        if not usuarios.find_one({"telefono": telefono}):
            return jsonify({"error": "Usuario no existe"}), 400
        contact = contactos.find_one({"telefono": telefono, "telefonoContacto": contacto})
        if contact is None:
            return jsonify({"error": "No se encontró el contacto"}), 400
        return jsonify(json.loads(json_util.dumps(contact))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@contactos_bp.route("/", methods=["POST"])
def post_contacto(telefono):
    try:
        if not usuarios.find_one({"telefono": telefono}):
            return jsonify({"error": "Usuario no existe"}), 400
        data = request.json
        requestData = {}
        if data is None:
            return jsonify({"error": "No se encontró el body"}), 400
        if "telefonoContacto" not in data:
            return jsonify({"error": "Telefono de contacto es requerido"}), 400
        if "aliasContacto" not in data:
            return jsonify({"error": "Alias de contacto es requerido"}), 400

        requestData["telefono"] = telefono
        requestData["telefonoContacto"] = str(data["telefonoContacto"])
        requestData["aliasContacto"] = str(data["aliasContacto"])

        if contactos.find_one({"telefono": requestData["telefono"], "telefonoContacto": requestData["telefonoContacto"]}):
            return jsonify({"error": "Contacto ya existe"}), 400

        if not usuarios.find_one({"telefono": requestData["telefonoContacto"], "alias": requestData["aliasContacto"]}):
            return jsonify({"error": "El contacto no existe en la lista de usuarios"}), 400

        contactos.insert_one(requestData)
        return jsonify({"message": "Contacto creado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@contactos_bp.route("/<contacto>", methods=["PUT"])
def put_contacto(telefono, contacto):
    try:
        if not usuarios.find_one({"telefono": telefono}):
            return jsonify({"error": "Usuario no existe"}), 400
        data = request.json
        newData = {}
        if data is None:
            return jsonify({"error": "No se encontró el body"}), 400
        for key in data:
            if key not in ["aliasContacto", "telefonoContacto"]:
                return jsonify({"error": "Campo no permitido"}), 400
            newData[key] = str(data[key])

        contact = contactos.find_one_and_update({"telefono": telefono, "telefonoContacto": contacto}, {"$set": newData})
        if contact is None:
            return jsonify({"error": "No se encontró el contacto"}), 400
        return jsonify({"message": "Contacto actualizado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@contactos_bp.route("/<contacto>", methods=["DELETE"])
def delete_contacto(telefono, contacto):
    try:
        if not usuarios.find_one({"telefono": telefono}):
            return jsonify({"error": "Usuario no existe"}), 400
        res = contactos.find_one_and_delete({"telefono": telefono, "telefonoContacto": contacto})
        if res is None:
            return jsonify({"error": "Contacto no encontrado"}), 404
        return jsonify({"message": "Contacto eliminado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400
