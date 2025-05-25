

import base64
import json
import sys
from os import error

import funciones_aes
from Crypto.Hash import HMAC, SHA256
from Crypto.Random import get_random_bytes
from socket_class import SOCKET_SIMPLE_TCP

# Paso 0: Inicializacion
########################

# Lee clave KBT
KBT = open("KBT.bin", "rb").read()

# Paso 1) B->T: KBT(Bob, Nb) en AES-GCM
#######################################

# Crear el socket de conexion con T (5551)
print("Creando conexion con T...")
socket = SOCKET_SIMPLE_TCP('127.0.0.1', 5551)
socket.conectar()

# Crea los campos del mensaje
t_n_origen = get_random_bytes(16)

# Codifica el contenido (los campos binarios en una cadena) y contruyo el mensaje JSON
msg_TE = []
msg_TE.append("Bob")
msg_TE.append(t_n_origen.hex())
json_ET = json.dumps(msg_TE)
print("B -> T (descifrado): " + json_ET)

# Cifra los datos con AES GCM
aes_engine = funciones_aes.iniciarAES_GCM(KBT)
cifrado, cifrado_mac, cifrado_nonce = funciones_aes.cifrarAES_GCM(aes_engine,json_ET.encode("utf-8"))

# Envia los datos
socket.enviar(cifrado)
socket.enviar(cifrado_mac)
socket.enviar(cifrado_nonce)

# Paso 2) T->B: KBT(K1, K2, Nb) en AES-GCM
##########################################
cifrado = socket.recibir()
mac = socket.recibir()
nonce = socket.recibir()

datosDescifrados = funciones_aes.descifrarAES_GCM(KBT, nonce, cifrado, mac)
jsonString = datosDescifrados.decode("utf-8")
print("T->B datos descifrados: ", jsonString)
msgDescifradoJson = json.loads(jsonString)

K1, K2, bNounce = msgDescifradoJson
K1 = bytes.fromhex(K1)
K2 = bytes.fromhex(K2)
bNounce = bytes.fromhex(bNounce)

# Cerramos el socket entre B y T, no lo utilizaremos mas
socket.cerrar() 

if (t_n_origen == bNounce):
    print("Equal nonce. Correct keys recieved from TTP")
else:
    # throw error if nonce is not equal
    print("Error: Nonce mismatch. Aborting.")
    sys.exit(1)

# Nonce correcto establecemos conexion con Alice

print("Creando conexion con Alice...")
socket_Alice = SOCKET_SIMPLE_TCP('127.0.0.1', 5553)
socket_Alice.escuchar()

# Paso 5) A->B: KAB(Nombre) en AES-CTR con HMAC
###############################################

print("A->B: Recibimos nombre y hmac")
mensajeCifrado = socket_Alice.recibir()
nonce = socket_Alice.recibir()
hmac = socket_Alice.recibir()

aes_ctr_engine = funciones_aes.iniciarAES_CTR_descifrado(K1,nonce)
mensajeDescifrado = funciones_aes.descifrarAES_CTR(aes_ctr_engine, mensajeCifrado)
jsonObject = json.loads(mensajeDescifrado.decode("utf-8"))
nombre = jsonObject[0]
print("Nombre recibido: ", nombre)
if(nombre != "Miguel"):
    print("Nombre recibido no valido.")


hmacNuevo = HMAC.new(K2, mensajeDescifrado)
hmacNuevo.verify(hmac)
print("HMAC correcto. Verificacion valida")

# (A realizar por el alumno/a...)

# Paso 6) B->A: KAB(Apellido) en AES-CTR con HMAC
#################################################

print("B->A: Mandamos apellido y hmac")
APELLIDO= "Dorado"
data = []
data.append(APELLIDO)
jsonString = json.dumps(data)
aes_ctr_engine, nonce = funciones_aes.iniciarAES_CTR_cifrado(K1)
mensajeCifrado = funciones_aes.cifrarAES_CTR(aes_ctr_engine, jsonString.encode("utf-8"))

# generamos hmac con texto plano. Podriamos hacerlo con texto cifrado (Seria mas seguro)
hmac = HMAC.new(K2, jsonString.encode("utf-8"))

socket_Alice.enviar(mensajeCifrado)
socket_Alice.enviar(nonce)
socket_Alice.enviar(hmac.digest())

# Paso 7) A->B: KAB(END) en AES-CTR con HMAC
############################################

print("A->B: Recibimos mensaje de cierre")
mensajeCifrado = socket_Alice.recibir()
nonce = socket_Alice.recibir()
hmac = socket_Alice.recibir()

aes_ctr_engine = funciones_aes.iniciarAES_CTR_descifrado(K1,nonce)
mensajeDescifrado = funciones_aes.descifrarAES_CTR(aes_ctr_engine, mensajeCifrado)
jsonObject = json.loads(mensajeDescifrado.decode("utf-8"))
mensaje = jsonObject[0]
print("Mensaje recibido: ", mensaje)
if(mensaje != "END"):
    print("Mensaje recibido no valido.")


hmacNuevo = HMAC.new(K2, mensajeDescifrado)
hmacNuevo.verify(hmac)
print("HMAC correcto. Verificacion valida")
socket_Alice.cerrar()
# (A realizar por el alumno/a...)

