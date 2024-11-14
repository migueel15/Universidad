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
servicio_bp = Blueprint("nombre_servicio", __name__)

client = pymongo.MongoClient(MONGODB_URL)
db = client.

@servicio_bp.route("/users", methods=["GET"])
def get_users():
    return jsonify({"nombre":"Miguel"})
