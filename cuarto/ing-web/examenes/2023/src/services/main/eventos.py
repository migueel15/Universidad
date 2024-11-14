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
eventos_bp = Blueprint("eventos", __name__)

client = pymongo.MongoClient(MONGODB_URL)
db = client.examen2023
usuarios = db.usuarios
eventos = db.eventos
contactos = db.contactos

# Eventos. Representa los eventos de agenda, con los siguientes atributos:
# o identificador. Clave o identificador del evento, propia de la base de datos elegida.
# o anfitrión. Dirección de email del usuario anfitrión del evento (el que crea el evento).
# o descripción. Título o descripción breve (hasta 50 caracteres) del evento.
# o inicio. Timestamp con la fecha y hora de inicio del evento (en tramos de 15 minutos).
# o duración. Duración del evento (en tramos de 15 minutos).
# o invitados. Lista de invitados al evento, representado cada uno por su email y el estado de la invitación
# (aceptada/pendiente).

# • Invitar a un evento a un contacto de un usuario, identificado por su email. El contacto invitado se incluirá en la
# lista de invitados del evento, con la invitación en estado pendiente.
# • Aceptar una invitación a un evento. El estado de la invitación pasará a aceptada.
# • Reprogramar un evento ya pasado, indicando cuánto tiempo se desplaza (un número de días determinado, una
# semana, un mes, o un año). Se creará un nuevo evento, con la nueva fecha y el resto de valores iguales a los del
# evento reprogramado.
# • Obtener la agenda de un usuario, representada por una lista de eventos, tanto propios como invitados, por orden
# ascendente de inicio
# Se valorará que en todas estas operaciones se realicen los controles de errores necesarios para evitar corromper la
# información de la base de datos (fechas/horas inexistentes o incorrectas, invitar a un evento a alguien que no sea
# contacto de su anfitrión, aceptar un evento al que no se ha sido invitado, etc.)

@eventos_bp.route("/", methods=["GET"])
def get_eventos():
    try:
        query = {}
        if request.args:
            if "anfitrion" in request.args:
                query["anfitrion"] = request.args["anfitrion"]
            if "descripcion" in request.args:
                query["descripcion"] = request.args["descripcion"]

        eventos_list = eventos.find(query)
        return jsonify(json.loads(json_util.dumps(eventos_list))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@eventos_bp.route("/<id>", methods=["GET"])
def get_evento(id):
    try:
        evento = eventos.find({"_id": ObjectId(id)})
        return jsonify(json.loads(json_util.dumps(evento))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@eventos_bp.route("/", methods=["POST"]) # lista de invitados [email, estado]
def create_evento():
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400
        if "anfitrion" not in data:
            return jsonify({"error": "El anfitrión es requerido"}), 400
        if "descripcion" not in data:
            return jsonify({"error": "La descripción es requerida"}), 400
        if "inicio" not in data:
            return jsonify({"error": "El inicio es requerido"}), 400
        if "duracion" not in data:
            return jsonify({"error": "La duración es requerida"}), 400

        if len(data["descripcion"]) > 50:
            return jsonify({"error": "La descripción debe tener menos de 50 caracteres"}), 400

        if not data["invitados"]:
            return jsonify({"error": "La lista de invitados es requerida"}), 400

        for invitado in data["invitados"]:
            if "email" not in invitado:
                return jsonify({"error": "El email del invitado es requerido"}), 400
            if "estado" not in invitado:
                return jsonify({"error": "El estado del invitado es requerido"}), 400
            if not contactos.find_one({"email": data["anfitrion"], "emailContacto": invitado["email"]}):
                return jsonify({"error": "El invitado no es un contacto del anfitrión"}), 400
            if invitado["estado"] not in ["aceptada", "pendiente"]:
                return jsonify({"error": "El estado del invitado debe ser aceptada o pendiente"}), 400

        # comprobar tramos
        inicio = data["inicio"]
        inicioDatetime = datetime.datetime.strptime(inicio, "%Y-%m-%dT%H:%M:%S.%fZ")
        duracion = int(data["duracion"])
        #inicio es un timestamp en string formato ISO "2023-06-01T00:00:00.000Z"
        if inicioDatetime.minute % 15 != 0:
            return jsonify({"error": "El inicio debe ser un tramo de 15 minutos"}), 400
        if duracion % 15 != 0:
            return jsonify({"error": "La duración debe ser un tramo de 15 minutos"}), 400

        if not usuarios.find_one({"email": data["anfitrion"]}):
            return jsonify({"error": "El anfitrión no existe"}), 400

        evento = eventos.insert_one(data)
        return jsonify({"mensaje": "Evento creado exitosamente", "id": str(evento.inserted_id)}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@eventos_bp.route("/<id>", methods=["PUT"])
def update_evento(id):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400

        if data["descripcion"]:
            if len(data["descripcion"]) > 50:
                return jsonify({"error": "La descripción debe tener menos de 50 caracteres"}), 400

        if data["invitados"]:
            for invitado in data["invitados"]:
                if "email" not in invitado:
                    return jsonify({"error": "El email del invitado es requerido"}), 400
                if "estado" not in invitado:
                    return jsonify({"error": "El estado del invitado es requerido"}), 400
                if not contactos.find_one({"email": data["anfitrion"], "emailContacto": invitado["email"]}):
                    return jsonify({"error": "El invitado no es un contacto del anfitrión"}), 400
                if invitado["estado"] not in ["aceptada", "pendiente"]:
                    return jsonify({"error": "El estado del invitado debe ser aceptada o pendiente"}), 400

        # comprobar tramos
        if data["inicio"]:
            inicio = data["inicio"]
            inicioDatetime = datetime.datetime.strptime(inicio, "%Y-%m-%dT%H:%M:%S.%fZ")
            if inicioDatetime.minute % 15 != 0:
                return jsonify({"error": "El inicio debe ser un tramo de 15 minutos"}), 400
        if data["duracion"]:
            duracion = int(data["duracion"])
            if duracion % 15 != 0:
                return jsonify({"error": "La duración debe ser un tramo de 15 minutos"}), 400

        if data["anfitrion"]:
            if not usuarios.find_one({"email": data["anfitrion"]}):
                return jsonify({"error": "El anfitrión no existe"}), 400

        if eventos.find_one({"_id": ObjectId(id)}):
            eventos.update_one({"_id": ObjectId(id)}, {"$set": data})
            return jsonify({"mensaje": "Evento actualizado exitosamente"}), 200
        else:
            return jsonify({"error": "El evento no existe"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@eventos_bp.route("/<id>", methods=["DELETE"])
def delete_evento(id):
    try:
        res = eventos.find_one_and_delete({"_id": ObjectId(id)})
        if not res:
            return jsonify({"error": "El evento no existe"}), 400
        return jsonify({"mensaje": "Evento eliminado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@eventos_bp.route("/<id>/invitar", methods=["PUT"])
def invitar_evento(id):
    try:
        evento = eventos.find_one({"_id": ObjectId(id)})
        if not evento:
            return jsonify({"error": "El evento no existe"}), 404
        anfitrion = evento["anfitrion"]
        invitados = []
        data = request.json # data tiene que tener formato [{},{},...]
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400
        if not data["invitados"]:
            return jsonify({"error": "La lista de invitados es requerida"}), 400

        for invitado in data["invitados"]:
            if "email" not in invitado:
                return jsonify({"error": "El email del invitado es requerido"}), 400

            if not contactos.find_one({"email": anfitrion, "emailContacto": invitado["email"]}):
                return jsonify({"error": "El invitado no es un contacto del anfitrión"}), 400
            if not eventos.find_one({"_id": ObjectId(id), "invitados.email": invitado["email"]}):
                invitados.append({"email": invitado["email"], "estado": "pendiente"})


        if not eventos.update_one({"_id": ObjectId(id)}, {"$push": {"invitados": {"$each": invitados}}}):
            return jsonify({"error": "El evento no existe"}), 404

        return jsonify({"mensaje": "Invitación enviada exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@eventos_bp.route("/<id>/aceptar", methods=["PUT"])
def aceptar_evento(id):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400
        if "email" not in data:
            return jsonify({"error": "El email es requerido"}), 400

        evento = eventos.find_one({"_id": ObjectId(id)})
        if not evento:
            return jsonify({"error": "El evento no existe"}), 404

        if not eventos.find_one({"_id": ObjectId(id), "invitados.email": data["email"]}):
            return jsonify({"error": "No se ha sido invitado a este evento"}), 400

        if not eventos.update_one({"_id": ObjectId(id), "invitados.email": data["email"]}, {"$set": {"invitados.$.estado": "aceptada"}}):
            return jsonify({"error": "El evento no existe"}), 404

        return jsonify({"mensaje": "Invitación aceptada exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@eventos_bp.route("/<id>/reprogramar", methods=["POST"]) # crea una copia del evento con la nueva fecha
def reprogramar_evento(id):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se envió la información requerida"}), 400
        if "dias" not in data:
            return jsonify({"error": "Los días son requeridos"}), 400

        evento = eventos.find_one({"_id": ObjectId(id)})
        if not evento:
            return jsonify({"error": "El evento no existe"}), 404
        del evento["_id"]
        if not evento:
            return jsonify({"error": "El evento no existe"}), 404

        inicio = evento["inicio"]
        inicioDatetime = datetime.datetime.strptime(inicio, "%Y-%m-%dT%H:%M:%S.%fZ")
        dias = int(data["dias"])
        inicioDatetime = inicioDatetime + datetime.timedelta(days=int(dias))
        inicio = inicioDatetime.strftime("%Y-%m-%dT%H:%M:%S.%fZ")
        evento["inicio"] = inicio

        if not eventos.insert_one(evento):
            return jsonify({"error": "Error al reprogramar el evento"}), 400

        return jsonify({"mensaje": "Evento reprogramado exitosamente"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400
