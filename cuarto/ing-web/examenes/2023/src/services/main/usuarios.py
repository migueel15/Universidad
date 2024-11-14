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
db = client.examen2023
usuarios = db.usuarios
eventos = db.eventos

@usuarios_bp.route("/", methods=["GET"])
def get_usuarios():
    try:
        query = {}
        if request.args:
            if "email" in request.args:
                query["email"] = request.args["email"]
            if "nombre" in request.args:
                query["nombre"] = request.args["nombre"]

        usuarios_list = usuarios.find(query)
        return jsonify(json.loads(json_util.dumps(usuarios_list))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@usuarios_bp.route("/<email>", methods=["GET"])
def get_usuario(email):
    try:
        usuario = usuarios.find({"email": email})
        return jsonify(json.loads(json_util.dumps(usuario))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@usuarios_bp.route("/", methods=["POST"])
def create_usuario():
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400
        if "email" not in data:
            return jsonify({"error": "El email es requerido"}), 400
        if "nombre" not in data:
            return jsonify({"error": "El nombre es requerido"}), 400

        if usuarios.find_one({"email": data["email"]}):
            return jsonify({"error": "El email ya está registrado"}), 400

        usuario = usuarios.insert_one(data)
        return jsonify({"mensaje": "Usuario creado exitosamente", "id": str(usuario.inserted_id)}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@usuarios_bp.route("/<email>", methods=["PUT"])
def update_usuario(email):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400
        query = {}
        if "nombre" in data:
            query["nombre"] = data["nombre"]
        if "email" in data:
            query["email"] = data["email"]

        if usuarios.find_one({"email": email}):
            usuarios.update_one({"email": email}, {"$set": query})
            return jsonify({"mensaje": "Usuario actualizado exitosamente"}), 200
        else:
            return jsonify({"error": "El usuario no existe"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@usuarios_bp.route("/<email>", methods=["DELETE"])
def delete_usuario(email):
    try:
        if usuarios.find_one_and_delete({"email": email}):
            return jsonify({"mensaje": "Usuario eliminado exitosamente"}), 200
        else:
            return jsonify({"error": "El usuario no existe"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@usuarios_bp.route("/<email>/eventos", methods=["GET"])
def get_eventos(email):
    try:
        if not usuarios.find({"email": email}):
            return jsonify({"error": "El email del usuario no existe"}), 400

        eventos_list = eventos.find({"$or": [{"anfitrion": email}, {"invitados.email": email}]}).sort("inicio", pymongo.ASCENDING)
        return jsonify(json.loads(json_util.dumps(eventos_list))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
