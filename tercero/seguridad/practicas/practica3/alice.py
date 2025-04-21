from Crypto.Cipher import PKCS1_OAEP
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Signature import pss


def cargarClavePrivada(fichero, passwd):
    keyCifrada = open(fichero, "rb").read()
    key = RSA.import_key(keyCifrada, passphrase=passwd)
    return key

def cargarClavePublica(fichero):
    keyFile = open(fichero, "rb").read()
    keyPublica = RSA.import_key(keyFile)
    return keyPublica

def guardarEnArchivo(fichero, mensaje):
    filePtr = open(fichero, "wb")
    filePtr.write(mensaje)
    filePtr.close()

def cifrar(cadena:str, key):
    data = cadena.encode("utf-8")
    cifradoRSA = PKCS1_OAEP.new(key)
    cadenaCifrada = cifradoRSA.encrypt(data)
    return cadenaCifrada

def firmarMensaje(texto:str, keyPriv):
    hashedText = SHA256.new(texto.encode("utf-8"))
    signature = pss.new(keyPriv).sign(hashedText)
    return signature


textoPlano = "Hola amigos de la seguridad"

clavePrivadaAlice = cargarClavePrivada("./ficheros/privAlice.key", "alice")
clavePublicaBob = cargarClavePublica("./ficheros/pubBob.key")

# Cifrar el mensaje
mensajeCifrado = cifrar(textoPlano, clavePublicaBob)
mensajeFirmado = firmarMensaje(textoPlano, clavePrivadaAlice)

guardarEnArchivo("./ficheros/textoCifrado", mensajeCifrado)
guardarEnArchivo("./ficheros/firmaDigital", mensajeFirmado)
