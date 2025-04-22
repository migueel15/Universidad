import json

import funciones_aes
import funciones_rsa
from Crypto.Hash import HMAC
from socket_class import SOCKET_SIMPLE_TCP

server = SOCKET_SIMPLE_TCP("localhost", 3333)

kPrivBob = funciones_rsa.cargar_RSAKey_Privada("rsa_bob.pem","bob")
kPubAlice = funciones_rsa.cargar_RSAKey_Publica("rsa_alice.pub")

server.escuchar()

k1Cipher = server.recibir()
k2Cipher = server.recibir()
firmaClaves = server.recibir()

k1 = funciones_rsa.descifrarRSA_OAEP_BIN(k1Cipher,kPrivBob)
k2 = funciones_rsa.descifrarRSA_OAEP_BIN(k2Cipher,kPrivBob)
valido = funciones_rsa.comprobarRSA_PSS(k1+k2,firmaClaves, kPubAlice)

print(valido)
print(k1)
print(k2)

cipherMsg = server.recibir()
if cipherMsg:
    mensaje = json.loads(cipherMsg.decode("utf-8"))
    print(mensaje)
    cipherText, nounce, h = mensaje

    # convertimos a bytes
    cipherText = bytearray.fromhex(cipherText)
    nounce = bytearray.fromhex(nounce)
    h = bytearray.fromhex(h)

    # comprobamos integridad
    mensajeAComprobar = cipherText + nounce
    hNew = HMAC.new(k2,mensajeAComprobar)
    try:
        hNew.verify(h)
        print("El mensaje es autentico")
    except ValueError:
        print("El mensaje o clave es incorrecto")

    decipher_engine = funciones_aes.iniciarAES_CTR_descifrado(k1,nounce)
    mensajePlano = decipher_engine.decrypt(cipherText)
    print(mensajePlano.decode("utf-8"))


server.cerrar()
