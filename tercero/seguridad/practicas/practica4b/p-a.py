
import base64
import json
import sys

import funciones_aes
from Crypto.Hash import HMAC, SHA256
from Crypto.Random import get_random_bytes
from socket_class import SOCKET_SIMPLE_TCP

# Paso 0: Inicializacion
########################

# Lee clave KBT
KAT = open("KAT.bin", "rb").read()

# Paso 3) A->T: KAT(Alice, Na) en AES-GCM
#########################################

print("Creando conexion con T...")
socket = SOCKET_SIMPLE_TCP('127.0.0.1', 5552)
socket.conectar()

t_n_origen = get_random_bytes(16)

msg_TE = []
msg_TE.append("Alice")
msg_TE.append(t_n_origen.hex())
json_ET = json.dumps(msg_TE)
print("A -> T (descifrado): " + json_ET)

# Cifra los datos con AES GCM
aes_engine = funciones_aes.iniciarAES_GCM(KAT)
cifrado, cifrado_mac, cifrado_nonce = funciones_aes.cifrarAES_GCM(aes_engine,json_ET.encode("utf-8"))

# Envia los datos
socket.enviar(cifrado)
socket.enviar(cifrado_mac)
socket.enviar(cifrado_nonce)

# Paso 4) T->A: KAT(K1, K2, Na) en AES-GCM
##########################################

cifrado = socket.recibir()
mac = socket.recibir()
nonce = socket.recibir()

datosDescifrados = funciones_aes.descifrarAES_GCM(KAT, nonce, cifrado, mac)
jsonString = datosDescifrados.decode("utf-8")
print("T->A datos descifrados: ", jsonString)
msgDescifradoJson = json.loads(jsonString)

K1, K2, aNounce = msgDescifradoJson
K1 = bytes.fromhex(K1)
K2 = bytes.fromhex(K2)
aNounce = bytes.fromhex(aNounce)

# Cerramos el socket entre B y T, no lo utilizaremos mas
socket.cerrar() 

if (t_n_origen == aNounce):
    print("Equal nonce. Correct keys recieved from TTP")
else:
    # throw error if nonce is not equal
    print("Error: Nonce mismatch. Aborting.")
    sys.exit(1)

# Nonce correcto establecemos conexion con Bob

socket = SOCKET_SIMPLE_TCP('127.0.0.1', 5553)
socket.conectar()

# Paso 5) A->B: KAB(Nombre) en AES-CTR con HMAC
###############################################

print("A->B: Mensaje con nombre cifrado y HMAC")
NOMBRE = "Miguel"
data = []
data.append(NOMBRE)
jsonString = json.dumps(data)
aes_ctr_engine, nonce = funciones_aes.iniciarAES_CTR_cifrado(K1)
mensajeCifrado = funciones_aes.cifrarAES_CTR(aes_ctr_engine, jsonString.encode("utf-8"))
# el hmac contiene el json descifrado en bytes
hmac = HMAC.new(K2, jsonString.encode("utf-8"))

socket.enviar(mensajeCifrado)
socket.enviar(nonce)
socket.enviar(hmac.digest())

# Paso 6) B->A: KAB(Apellido) en AES-CTR con HMAC
#################################################

print("B->A: Recibimos apellido y hmac")
mensajeCifrado = socket.recibir()
nonce = socket.recibir()
hmac = socket.recibir()

aes_ctr_engine = funciones_aes.iniciarAES_CTR_descifrado(K1, nonce)
msgDescifrado = funciones_aes.descifrarAES_CTR(aes_ctr_engine, mensajeCifrado)
jsonObject = json.loads(msgDescifrado.decode("utf-8"))
apellido = jsonObject[0]

print("Apellido recibido: ", apellido)

if(apellido != "Dorado"):
    print("Apellido recibido no valido")

hmacNuevo = HMAC.new(K2, msgDescifrado)
hmacNuevo.verify(hmac)
print("HMAC correcto. Verificacion valida")

# Paso 7) A->B: KAB(END) en AES-CTR con HMAC
############################################

print("A->B: Mensaje de cierre")
MENSAJE = "END"
data = []
data.append(MENSAJE)
jsonString = json.dumps(data)
aes_ctr_engine, nonce = funciones_aes.iniciarAES_CTR_cifrado(K1)
mensajeCifrado = funciones_aes.cifrarAES_CTR(aes_ctr_engine, jsonString.encode("utf-8"))
# el hmac contiene el json descifrado en bytes
hmac = HMAC.new(K2, jsonString.encode("utf-8"))

socket.enviar(mensajeCifrado)
socket.enviar(nonce)
socket.enviar(hmac.digest())
socket.cerrar()
