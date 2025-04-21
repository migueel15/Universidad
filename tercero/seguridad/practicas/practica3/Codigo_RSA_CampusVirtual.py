"""
Codigo APENDICE A
"""
from Crypto.Cipher import PKCS1_OAEP
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Signature import pss


def crear_RSAKey():
    key = RSA.generate(2048)

    return key

def guardar_RSAKey_Privada(fichero, key, password):
    key_cifrada = key.export_key(passphrase=password, pkcs=8, protection="scryptAndAES128-CBC")
    file_out = open(fichero, "wb")
    file_out.write(key_cifrada)
    file_out.close()

def cargar_RSAKey_Privada(fichero, password):
    key_cifrada = open(fichero, "rb").read()
    key = RSA.import_key(key_cifrada, passphrase=password)

    return key

def guardar_RSAKey_Publica(fichero, key):
    key_pub = key.publickey().export_key()
    file_out = open(fichero, "wb")
    file_out.write(key_pub)
    file_out.close()

def cargar_RSAKey_Publica(fichero):
    keyFile = open(fichero, "rb").read()
    key_pub = RSA.import_key(keyFile)

    return key_pub

def cifrarRSA_OAEP(cadena, key):
    datos = cadena.encode("utf-8")
    engineRSACifrado = PKCS1_OAEP.new(key)
    cifrado = engineRSACifrado.encrypt(datos)

    return cifrado

def descifrarRSA_OAEP(cifrado, key):
    engineRSADescifrado = PKCS1_OAEP.new(key)
    datos = engineRSADescifrado.decrypt(cifrado)
    cadena = datos.decode("utf-8")

    return cadena

def firmarRSA_PSS(texto, key_private):
    h = SHA256.new(texto.encode("utf-8")) # Crea un nuevo objeto SHA 256, pasándole el texto
    print(h.hexdigest()) # Muestra el hash del texto en hexadecimal (NOTA: prueba a poner print(h.digest()) en la siguiente línea...)
    signature = pss.new(key_private).sign(h)

    return signature

def comprobarRSA_PSS(texto, firma, key_public):
    h = SHA256.new(texto.encode("utf-8")) # Crea un nuevo objeto SHA 256, pasándole el texto
    print(h.hexdigest()) # Muestra el hash del texto en hexadecimal (NOTA: prueba a poner print(h.digest()) en la siguiente línea...)
    verifier = pss.new(key_public)
    try:
        verifier.verify(h, firma)
        return True
    except (ValueError, TypeError):
        return False

"""
Codigo APENDICE C
"""

from Crypto.Hash import SHA256
from Crypto.PublicKey import ECC
from Crypto.Signature import DSS

# Ver https://pycryptodome.readthedocs.io/en/latest/src/public_key/ecc.html
# Ver https://pycryptodome.readthedocs.io/en/latest/src/signature/dsa.html 

def crear_ECCKey():
    # Use 'NIST P-256'
    return key

def guardar_ECCKey_Privada(fichero, key, password):
    # ...

def cargar_ECCKey_Privada(fichero, password):
    # ...
    return key

def guardar_ECCKey_Publica(fichero, key):
    # ...

def cargar_ECCKey_Publica(fichero):
    # ...
    return key_pub

# def cifrarECC_OAEP(cadena, key):
# El cifrado con ECC (ECIES) aun no está implementado
# Por lo tanto, no se puede implementar este método aun en la versión 3.9.7 
#    return cifrado

# def descifrarECC_OAEP(cifrado, key):
# El cifrado con ECC (ECIES) aun no está implementado
# Por lo tanto, no se puede implementar este método aun en la versión 3.9.7 
#    return cadena

def firmarECC_PSS(texto, key_private):
    # ...
    return signature

def comprobarECC_PSS(texto, firma, key_public):
    # ...
    try:
        # ...
        return True
    except (ValueError, TypeError):
        return False
