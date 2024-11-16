import json
import os

import pymongo
import requests
from bson import json_util
from bson.objectid import ObjectId
from dotenv import load_dotenv
from flask import Blueprint, current_app, jsonify, request

# ● Tareas. Las tareas en las que se puede colaborar, descritas por las siguientes características:
# o responsable: dirección de email del usuario responsable de la tarea (el que crea la tarea).
# o descripción: título o descripción breve de la tarea (hasta 50 caracteres).
# o habilidades: una serie de habilidades (términos) adecuadas para participar en la tarea.
# o segmentos: duración estimada de la tarea (en segmentos de 1 hora de trabajo).

load_dotenv()
MONGODB_URL = os.getenv("MONGODB_URL")
tareas_bp = Blueprint("tareas", __name__)

client = pymongo.MongoClient(MONGODB_URL)
db = client.examen
tareas = db.tareas
participantes = db.participantes

@tareas_bp.route("/", methods=["GET"])
def get_tareas():
    try:
        resTareas = tareas.find()
        filtro = {}
        params = request.args

        if "habilidad" in params:
            filtro["habilidades"] = {"$in": [params["habilidad"]]}

        resTareas = tareas.find(filtro)

        # busca las tareas que tengan el mismo numero de segmentos que participantes asociados
        if "completa" in params:
            if params["completa"] == "true":
                resTareas = [tarea for tarea in resTareas if participantes.count_documents({"idTarea": tarea["_id"]}) == tarea["segmentos"]]
            else:
                resTareas = [tarea for tarea in resTareas if participantes.count_documents({"idTarea": tarea["_id"]}) < int(tarea["segmentos"])]
        if resTareas:
            return jsonify(json.loads(json_util.dumps(resTareas))), 200
        else:
            return jsonify({"error": "No se encontraron tareas"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@tareas_bp.route("/<id>", methods=["GET"])
def get_tarea(id):
    try:
        resTarea = tareas.find_one({"_id": ObjectId(id)})
        if resTarea:
            return jsonify(json.loads(json_util.dumps(resTarea))), 200
        else:
            return jsonify({"error": "No se encontró la tarea"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@tareas_bp.route("/", methods=["POST"])
def create_tarea():
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400
        if "responsable" not in data:
            return jsonify({"error": "Falta el responsable"}), 400
        if "descripcion" not in data:
            return jsonify({"error": "Falta la descripción"}), 400
        if "habilidades" not in data:
            return jsonify({"error": "Faltan las habilidades"}), 400
        if "segmentos" not in data:
            return jsonify({"error": "Faltan los segmentos"}), 400

        tarea = {
            "responsable": data["responsable"],
            "descripcion": data["descripcion"],
            "habilidades": data["habilidades"],
            "segmentos": int(data["segmentos"])
        }
        resTarea = tareas.insert_one(tarea)
        if resTarea:
            return jsonify({"message": "Tarea creada exitosamente"}), 201
        else:
            return jsonify({"error": "No se pudo crear la tarea"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@tareas_bp.route("/<id>", methods=["PUT"])
def update_tarea(id):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400

        newData = {}

        if "responsable" in data:
            newData["responsable"] = data["responsable"]
        if "descripcion" in data:
            newData["descripcion"] = data["descripcion"]
        if "habilidades" in data:
            newData["habilidades"] = data["habilidades"]
        if "segmentos" in data:
            newData["segmentos"] = int(data["segmentos"])


        resTarea = tareas.update_one({"_id": ObjectId(id)}, {"$set": newData})
        if resTarea:
            return jsonify({"message": "Tarea actualizada exitosamente"}), 200
        else:
            return jsonify({"error": "No se pudo actualizar la tarea"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@tareas_bp.route("/<id>", methods=["DELETE"])
def delete_tarea(id):
    try:
        resTarea = tareas.delete_one({"_id": ObjectId(id)})
        if resTarea:
            return jsonify({"message": "Tarea eliminada exitosamente"}), 200
        else:
            return jsonify({"error": "No se pudo eliminar la tarea"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# tendremos una coleccion participantes en la que se guardaran los colaboradores que se han unido a una tarea
participantes = db.participantes
colaboradores = db.colaboradores

@tareas_bp.route("/<idTarea>/participantes", methods=["GET"])
def get_participantes(idTarea):
    try:
        resParticipantes = participantes.find({"idTarea": ObjectId(idTarea)})
        if resParticipantes:
            return jsonify(json.loads(json_util.dumps(resParticipantes))), 200
        else:
            return jsonify({"error": "No se encontraron participantes"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@tareas_bp.route("/<idTarea>/participantes", methods=["POST"])
def create_participante(idTarea):
    try:
        data = request.json
        if not data:
            return jsonify({"error": "No se proporcionaron datos"}), 400
        if "idColaborador" not in data:
            return jsonify({"error": "Falta el idColaborador"}), 400

        colabor = colaboradores.find_one({"_id": ObjectId(data["idColaborador"])})
        if not colabor:
            return jsonify({"error": "No se encontró el colaborador"}), 404
        nombreColaborador = colabor["nombre"]


        participante = {
            "idTarea": ObjectId(idTarea),
            "idColaborador": ObjectId(data["idColaborador"]),
            "nombreColaborador": nombreColaborador
        }
        if participantes.find_one(participante):
            return jsonify({"error": "El participante ya existe"}), 400

        resParticipante = participantes.insert_one(participante)
        if resParticipante:
            return jsonify({"message": "Participante creado exitosamente"}), 201
        else:
            return jsonify({"error": "No se pudo crear el participante"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@tareas_bp.route("/<idTarea>/participantes/<idColaborador>", methods=["DELETE"])
def delete_participante(idTarea, idColaborador):
    try:
        resParticipante = participantes.delete_one({"idTarea": ObjectId(idTarea), "idColaborador": ObjectId(idColaborador)})
        if resParticipante:
            return jsonify({"message": "Participante eliminado exitosamente"}), 200
        else:
            return jsonify({"error": "No se pudo eliminar el participante"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# buscar candidatos para una tarea
# devolver lista de emails de colaboradores que no estan en la tarea y cumplan con una habilidad al menos
@tareas_bp.route("/<idTarea>/candidatos", methods=["GET"])
def get_candidatos(idTarea):
    try:
        tarea = tareas.find_one({"_id": ObjectId(idTarea)})
        if not tarea:
            return jsonify({"error": "No se encontró la tarea"}), 404

        habilidadesTarea = tarea["habilidades"]
        resColaboradores = colaboradores.find({"habilidades": {"$in": habilidadesTarea}})
        listaColaboradores = []
        for colaborador in resColaboradores:
            if not participantes.find_one({"idTarea": ObjectId(idTarea), "idColaborador": colaborador["_id"]}):
                listaColaboradores.append(colaborador["email"])
        return jsonify(listaColaboradores), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
