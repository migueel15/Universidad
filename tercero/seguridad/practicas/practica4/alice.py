import json

import funciones_aes
import funciones_rsa
from Crypto.Hash import HMAC
from socket_class import SOCKET_SIMPLE_TCP

socket = SOCKET_SIMPLE_TCP("localhost", 3333)
socket.conectar()

# generar claves k1 y k2
k1 = funciones_aes.crear_AESKey()
k2 = funciones_aes.crear_AESKey()

print("k1:", k1)
print("k2:", k2)

#cifrar las claves con la clave publica de Bob
kPubBob = funciones_rsa.cargar_RSAKey_Publica("rsa_bob.pub")
k1Cipher = funciones_rsa.cifrarRSA_OAEP_BIN(k1,kPubBob)
k2Cipher = funciones_rsa.cifrarRSA_OAEP_BIN(k2,kPubBob)

# firma k1||k2 con la clave privada de Alice
kPrivAlice = funciones_rsa.cargar_RSAKey_Privada("rsa_alice.pem", "alice")

firmaClaves = funciones_rsa.firmarRSA_PSS(k1+k2,kPrivAlice)

#enviar las claves
socket.enviar(k1Cipher)
socket.enviar(k2Cipher)
socket.enviar(firmaClaves)

# mandar mensaje cifrado con Alice nounce
cipher_engine, nounce = funciones_aes.iniciarAES_CTR_cifrado(k1)
textoPlano = "Alice"
cipherText = cipher_engine.encrypt(textoPlano.encode("utf-8"))

# generamos HMAC
mensajeFirmado = cipherText + nounce
h = HMAC.new(k2,mensajeFirmado).digest()

datos = []
datos.append(cipherText.hex())
datos.append(nounce.hex())
datos.append(h.hex())
jStr = json.dumps(datos)

socket.enviar(jStr.encode("utf-8"))

socket.cerrar()
