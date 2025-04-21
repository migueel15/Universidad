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

def cargarBytesDeFichero(path):
    fileByte = open(path, "rb").read()
    return fileByte

def descifrarTexto(cifrado, key):
    engineRSADescifrado = PKCS1_OAEP.new(key)
    datos = engineRSADescifrado.decrypt(cifrado)
    cadena = datos.decode("utf-8")
    return cadena

def comprobarCifrado(texto, firma, key_public):
    h = SHA256.new(texto.encode("utf-8"))
    print(h.hexdigest())
    verifier = pss.new(key_public)
    try:
        verifier.verify(h, firma)
        return True
    except (ValueError, TypeError):
        return False

clavePrivadaBob = cargarClavePrivada("./ficheros/privBob.key", "bob")
clavePublicaAlice = cargarClavePublica("./ficheros/pubAlice.key")

textoCifrado = cargarBytesDeFichero("./ficheros/textoCifrado")
firmaDifital = cargarBytesDeFichero("./ficheros/firmaDigital")

textoPlano = descifrarTexto(textoCifrado, clavePrivadaBob)
print(textoPlano)
print(comprobarCifrado(textoPlano, firmaDifital, clavePublicaAlice))
