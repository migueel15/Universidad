from Crypto.PublicKey import ECC
from Crypto.Hash import SHA256
from Crypto.Signature import DSS

def crear_ECCKey():
# Use 'NIST P‐256'
    key = ECC.generate(curve="p256")
    return key

def guardar_ECCKey_Privada(fichero, key:ECC.EccKey, password):
    fs = open(fichero, "wt")
    data = key.export_key(format="PEM", 
                          passphrase=password, 
                          protection="PBKDF2WithHMAC-SHA512AndAES256-CBC",
                          prot_params={"iteration_count":131072})
    fs.write(data)
    fs.close()

def cargar_ECCKey_Privada(fichero, password):
    fs = open(fichero, "rt")
    data = fs.read()
    key = ECC.import_key(data,password)
    return key

def guardar_ECCKey_Publica(fichero, key:ECC.EccKey):
    fs = open(fichero, "wt")
    data = key.public_key().export_key(format="PEM")
    fs.write(data)
    fs.close()

def cargar_ECCKey_Publica(fichero):
    fs = open(fichero, "rt")
    dataStr = fs.read()
    key_pub = ECC.import_key(dataStr)
    return key_pub

def firmarECC_PSS(texto, key_private):
    h = SHA256.new(texto)
    signer = DSS.new(key_private, "fips-186-3")
    firma = signer.sign(h)
    return firma

def comprobarECC_PSS(texto, firma, key_public):
    h = SHA256.new(texto)
    verifier = DSS.new(key_public, "fips-186-3")
    try:
        verifier.verify(h,firma)
        return True
    except (ValueError, TypeError):
        return False
