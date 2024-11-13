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
usuarios_bp = Blueprint("usuarios", __name__)

client = pymongo.MongoClient(MONGODB_URL)
db = client.examen2022
usuarios = db.usuarios

#CRUD de usuarios (GET ALL, GET, POST, PUT, DELETE).
# Buscar un usuario de la red social a partir de su alias.


@usuarios_bp.route("/", methods=["GET"])
def get_usuarios():
    try:
        params = {}
        if "alias" in request.args:
            params["alias"] = {"$regex": request.args["alias"], "$options": "i"}
        users = usuarios.find(params)
        return jsonify(json.loads(json_util.dumps(users))), 200
        
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@usuarios_bp.route("/<telefono>", methods=["GET"])
def get_usuario(telefono):
    try:
        user = usuarios.find_one({"telefono": telefono})
        return jsonify(json.loads(json_util.dumps(user))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@usuarios_bp.route("/", methods=["POST"])
def post_usuario():
    try:
        data = request.json
        requestData = {}
        if data is None:
            return jsonify({"error": "No se encontró el body"}), 400
        if "alias" not in data:
            return jsonify({"error": "Alias es requerido"}), 400
        if "telefono" not in data:
            return jsonify({"error": "Telefono es requerido"}), 400

        requestData["alias"] = str(data["alias"])
        requestData["telefono"] = str(data["telefono"])

        if usuarios.find_one({"telefono": requestData["telefono"]}):
            return jsonify({"error": "Usuario ya existe"}), 400

        usuarios.insert_one(requestData)
        return jsonify({"message": "Usuario creado"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@usuarios_bp.route("/<telefono>", methods=["PUT"])
def put_usuario(telefono):
    try:
        data = request.json
        newData = {}
        if data is None:
            return jsonify({"error": "No se encontró el body"}), 400

        if "alias" in data:
            newData["alias"] = str(data["alias"])
        if "telefono" in data:
            newData["telefono"] = str(data["telefono"])

        res = usuarios.find_one_and_update({"telefono": telefono}, {"$set": newData})
        if res is None:
            return jsonify({"error": "Usuario no encontrado"}), 404
        return jsonify({"message": "Usuario actualizado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@usuarios_bp.route("/<telefono>", methods=["DELETE"])
def delete_usuario(telefono):
    try:
        res = usuarios.find_one_and_delete({"telefono": telefono})
        if res is None:
            return jsonify({"error": "Usuario no encontrado"}), 404
        return jsonify({"message": "Usuario eliminado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400
