import os

from contactos import contactos_bp
from dotenv import load_dotenv
from eventos import eventos_bp
from flask import Flask, request
from usuarios import usuarios_bp

app = Flask(__name__)
# Define blueprints:
app.register_blueprint(usuarios_bp, url_prefix='/usuarios')
app.register_blueprint(contactos_bp, url_prefix='/usuarios/<email>/contactos')
app.register_blueprint(eventos_bp, url_prefix='/eventos')

@app.route('/')
def hello_world():
    return "Hello, World!"

if __name__ == '__main__':
    load_dotenv()
    app.run(host="0.0.0.0", port=os.getenv("SERVICE_PORT_MAIN"))
