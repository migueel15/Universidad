import json
import os

import pymongo
import requests
from bson import json_util
from bson.objectid import ObjectId
from dotenv import load_dotenv
from flask import Blueprint, current_app, jsonify, request

# Colaboradores. Representa a los usuarios de la aplicación, con las siguientes características:
# o email: dirección de email del colaborador.
# o nombre: nombre del usuario.
# o habilidades: una serie de habilidades (términos) que posee el colaborador

load_dotenv()
MONGODB_URL = os.getenv("MONGODB_URL")
colaboradores_bp = Blueprint("colaboradores", __name__)

client = pymongo.MongoClient(MONGODB_URL)
db = client.examen
colaboradores = db.colaboradores
tareas = db.tareas
participantes = db.participantes

@colaboradores_bp.route("/", methods=["GET"])
def get_colaboradores():
    try:
        resColaboradores = colaboradores.find()
        if resColaboradores:
            return jsonify(json.loads(json_util.dumps(resColaboradores))), 200
        else:
            return jsonify({"error": "No se encontraron colaboradores"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<id>", methods=["GET"])
def get_colaborador(id):
    try:
        resColaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if resColaborador:
            return jsonify(json.loads(json_util.dumps(resColaborador))), 200
        else:
            return jsonify({"error": "No se encontró el colaborador"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/", methods=["POST"])
def create_colaborador():
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400
        if "email" not in data:
            return jsonify({"error": "Falta el email"}), 400
        if "nombre" not in data:
            return jsonify({"error": "Falta el nombre"}), 400
        if "habilidades" not in data:
            return jsonify({"error": "Faltan las habilidades"}), 400

        if colaboradores.find_one({"email": data["email"]}):
            return jsonify({"error": "El colaborador ya existe"}), 400
        colaboradores.insert_one(data)
        return jsonify({"message": "Colaborador creado"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<id>", methods=["PUT"])
def update_colaborador(id):
    try:
        data = request.json
        newData = {}

        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400
        if "email" in data:
            newData["email"] = data["email"]
        if "nombre" in data:
            newData["nombre"] = data["nombre"]
        if "habilidades" in data:
            newData["habilidades"] = data["habilidades"]

        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        colaboradores.update_one({"_id": ObjectId(id)}, {"$set": newData})
        return jsonify({"message": "Colaborador actualizado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<id>", methods=["DELETE"])
def delete_colaborador(id):
    try:
        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        colaboradores.delete_one({"_id": ObjectId(id)})
        return jsonify({"message": "Colaborador eliminado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


# crud de las habilidades de los colaboradores

@colaboradores_bp.route("/<id>/habilidades", methods=["GET"])
def get_habilidades_colaborador(id):
    try:
        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        return jsonify({"habilidades": colaborador["habilidades"]}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<id>/habilidades", methods=["POST"])
def add_habilidad_colaborador(id):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400
        if "habilidad" not in data:
            return jsonify({"error": "Falta la habilidad"}), 400

        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        if data["habilidad"] in colaborador["habilidades"]:
            return jsonify({"error": "La habilidad ya existe"}), 400

        colaboradores.update_one({"_id": ObjectId(id)}, {"$push": {"habilidades": data["habilidad"]}})
        return jsonify({"message": "Habilidad agregada"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<id>/habilidades", methods=["DELETE"])
def delete_habilidad_colaborador(id):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400
        if "habilidad" not in data:
            return jsonify({"error": "Falta la habilidad"}), 400

        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        res = colaboradores.update_one({"_id": ObjectId(id)}, {"$pull": {"habilidades": data["habilidad"]}})
        if res.modified_count == 0:
            return jsonify({"error": "No se encontró la habilidad"}), 404
        return jsonify({"message": "Habilidad eliminada"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

    # tareas asignadas a un colaborador

@colaboradores_bp.route("/<id>/tareas", methods=["GET"])
def get_tareas_colaborador(id):
    try:
        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        tareasParticipante = participantes.find({"idColaborador": ObjectId(id)})
        if not tareasParticipante:
            return jsonify({"error": "No se encontraron tareas"}), 404

        listaTareas = []
        for tarea in tareasParticipante:
            dataTarea = tareas.find_one({"_id": tarea["idTarea"]})
            listaTareas.append(dataTarea)
        return jsonify(json.loads(json_util.dumps(listaTareas))), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<id>/tareas", methods=["POST"])
def add_tarea_colaborador(id):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400
        if "idTarea" not in data:
            return jsonify({"error": "Falta el idTarea"}), 400

        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        tarea = tareas.find_one({"_id": ObjectId(data["idTarea"])})
        if not tarea:
            return jsonify({"error": "No se encontró la tarea"}), 404

        # al menos una habilidad del colaborador debe coincidir con las habilidades de la tarea
        habilidadesColaborador = colaborador["habilidades"]
        habilidadesTarea = tarea["habilidades"]
        if not any(habilidad in habilidadesColaborador for habilidad in habilidadesTarea):
            return jsonify({"error": "El colaborador no tiene las habilidades necesarias para la tarea"}), 400

        participante = {
            "idTarea": ObjectId(data["idTarea"]),
            "idColaborador": ObjectId(id),
            "nombreColaborador": colaborador["nombre"]
        }
        if participantes.find_one(participante):
            return jsonify({"error": "El participante ya existe"}), 400

        participantes.insert_one(participante)
        return jsonify({"message": "Participante agregado"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<id>/tareas/<idTarea>", methods=["DELETE"])
def delete_tarea_colaborador(id, idTarea):
    try:
        colaborador = colaboradores.find_one({"_id": ObjectId(id)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404

        tarea = tareas.find_one({"_id": ObjectId(idTarea)})
        if not tarea:
            return jsonify({"error": "No se encontró la tarea"}), 404

        res = participantes.delete_one({"idTarea": ObjectId(idTarea), "idColaborador": ObjectId(id)})
        if res.deleted_count == 0:
            return jsonify({"error": "No se encontró la tarea asignada"}), 404
        return jsonify({"message": "Colaborador desasignado de la tarea"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500



# @colaboradores_bp.route("/<idResponsable>/relaciones", methods=["GET"])
# def get_relaciones_colaborador(idResponsable):
#     try:
#         colaborador = colaboradores.find_one({"_id": ObjectId(idResponsable)})
#         if not colaborador:
#             return jsonify({"error": "No se encontró el colaborador"}), 404
#         correoResponsable = colaborador["email"]
#         tareasResponsable = tareas.find({"responsable": correoResponsable})
#         usuariosVinculadosAResponsable = []
#
#         for tarea in tareasResponsable:
#             participantesTarea = participantes.find({"idTarea": tarea["_id"]})
#             if not participantesTarea:
#                 return jsonify({"error": "No se encontraron participantes"}), 404
#             for participante in participantesTarea:
#                 colaboradorParticipante = colaboradores.find_one({"_id": participante["idColaborador"]})
#                 if not colaboradorParticipante:
#                     return jsonify({"error": "No se encontró el colaborador participante"}), 404
#                 if colaboradorParticipante["email"] not in usuariosVinculadosAResponsable and colaboradorParticipante["email"] != correoResponsable:
#                     usuariosVinculadosAResponsable.append(colaboradorParticipante["email"])
#         return jsonify(usuariosVinculadosAResponsable),
#     except Exception as e:
#         return jsonify({"error": str(e)}), 500

@colaboradores_bp.route("/<idResponsable>/relaciones", methods=["GET"])
def get_relaciones_colaborador(idResponsable):
    try:
        colaborador = colaboradores.find_one({"_id": ObjectId(idResponsable)})
        if not colaborador:
            return jsonify({"error": "No se encontró el colaborador"}), 404
        correoResponsable = colaborador["email"]
        tareasResponsable = tareas.find({"responsable": correoResponsable})
        usuariosVinculadosAResponsable = []

        for tarea in tareasResponsable:
            participantesTarea = participantes.find({"idTarea": tarea["_id"]})
            if participantesTarea:
                for participante in participantesTarea:
                    colaboradorParticipante = colaboradores.find_one({"_id": participante["idColaborador"]})
                    if colaboradorParticipante:
                        if colaboradorParticipante["email"] not in usuariosVinculadosAResponsable and colaboradorParticipante["email"] != correoResponsable:
                            usuariosVinculadosAResponsable.append(colaboradorParticipante["email"])
        return jsonify(usuariosVinculadosAResponsable), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
