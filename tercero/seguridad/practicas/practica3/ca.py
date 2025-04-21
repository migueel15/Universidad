from Crypto.Cipher import PKCS1_OAEP
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Signature import pss


def crear_RSAKey():
    key = RSA.generate(2048)
    return key

def guardarClavePrivada(fichero, key, passwd):
    keyCifrada = key.export_key(passphrase=passwd, pkcs=8, protection="scryptAndAES128-CBC")
    file_out = open(fichero, "wb")
    file_out.write(keyCifrada)
    file_out.close()

def guardarClavePublica(fichero, key):
    keyPublic = key.publickey().export_key()
    file_out = open(fichero, "wb")
    file_out.write(keyPublic)
    file_out.close()

# Generar clave publica
key = crear_RSAKey()

# Generar claves de Alice
guardarClavePublica("./ficheros/pubAlice.key", key)
guardarClavePrivada("./ficheros/privAlice.key", key, "alice")

# Generar claves de Bob
guardarClavePublica("./ficheros/pubBob.key", key)
guardarClavePrivada("./ficheros/privBob.key", key, "bob")
