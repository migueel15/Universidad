import os

from contactos import contactos_bp
from dotenv import load_dotenv
from flask import Flask, request
from mensajes import mensajes_bp
from usuarios import usuarios_bp

app = Flask(__name__)
# Define blueprints:
app.register_blueprint(usuarios_bp, url_prefix='/usuarios')
app.register_blueprint(mensajes_bp, url_prefix='/mensajes')
app.register_blueprint(contactos_bp, url_prefix='/usuarios/<telefono>/contactos')

@app.route('/')
def hello_world():
    return 'Hello, World!'

if __name__ == '__main__':
    load_dotenv()
    app.run(host="0.0.0.0", port=os.getenv("SERVICE_PORT_MAIN"))

# Rutas que se deben implementar:
