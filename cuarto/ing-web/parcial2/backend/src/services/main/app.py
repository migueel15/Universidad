import os

from colaboradores import colaboradores_bp
from dotenv import load_dotenv
from flask import Flask, request
# import blueprints
from tareas import tareas_bp

app = Flask(__name__)
# Define blueprints:
app.register_blueprint(tareas_bp, url_prefix='/tareas')
app.register_blueprint(colaboradores_bp, url_prefix='/colaboradores')

@app.route('/')
def hello_world():
    return 'Hello, World!'

if __name__ == '__main__':
    load_dotenv()
    app.run(host="0.0.0.0", port=os.getenv("SERVICE_PORT_MAIN"))
