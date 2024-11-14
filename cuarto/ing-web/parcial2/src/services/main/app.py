import os

from dotenv import load_dotenv
from flask import Flask, request
# import blueprints
from service import servicio_bp

app = Flask(__name__)
# Define blueprints:
app.register_blueprint(servicio_bp, url_prefix='/prefix')

@app.route('/')
def hello_world():
    return 'Hello, World!'

if __name__ == '__main__':
    load_dotenv()
    app.run(host="0.0.0.0", port=os.getenv("SERVICE_PORT_MAIN"))
