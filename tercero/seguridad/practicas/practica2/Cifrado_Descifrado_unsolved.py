from Crypto.Cipher import AES, DES
from Crypto.Random import get_random_bytes
from Crypto.Util import Counter
from Crypto.Util.Padding import pad, unpad

# Datos necesarios
key = get_random_bytes(8) # Clave aleatoria de 64 bits
IV = get_random_bytes(8)  # IV aleatorio de 64 bits para CBC
BLOCK_SIZE_DES = 8 # Bloque de 64 bits
data = "Hola amigos de la seguridad".encode("utf-8") # Datos a cifrar
data2 = "Hola amigas de la seguridad".encode("utf-8") # Datos a cifrar

# Creamos DES cipher y decipher
cipher = DES.new(key, DES.MODE_CBC, IV)
decipher_des = DES.new(key, DES.MODE_CBC, IV)

# CIFRADO #######################################################################
ciphertext = cipher.encrypt(pad(data,BLOCK_SIZE_DES))
print(data)
print(ciphertext)
ciphertext2 = cipher.encrypt(pad(data2,BLOCK_SIZE_DES))
print(data2)
print(ciphertext2)

# DESCIFRADO #######################################################################
new_data = unpad(decipher_des.decrypt(ciphertext), BLOCK_SIZE_DES).decode("utf-8", "ignore")
print(new_data)
new_data2 = unpad(decipher_des.decrypt(ciphertext2), BLOCK_SIZE_DES).decode("utf-8", "ignore")
print(new_data2)

# Si se observa los textos cifrados, es posible ver que ese cambio de una “o” por una “a” (amigos → amigas) impacta en ambos textos, ¿a qué se debe ese cambio?
# Al tratarse de un cifrado en bloque de tipo CBC (cipher block chaining), en cada bloque se utiliza el cifrado del bloque anterior como IV lo que hace que un pequeño cambio afecte al resto de la cadena de cifrado. Es por esto que a partir de la letra a de amigas el resto del texto cifrado no coincide con el anterior


class AES_CIPHER_CBC:

    BLOCK_SIZE_AES = 16 # AES: Bloque de 128 bits

    def __init__(self, key):
        """Inicializa las variables locales"""
        if len(key) is not 16:
            raise ValueError("La clave debe ser de 16 bytes")
        self.key = key

    def cifrar(self, cadena: str, IV):
        """Cifra el parámetro cadena (de tipo String) con una IV específica, y 
           devuelve el texto cifrado binario"""
        if len(IV) != self.BLOCK_SIZE_AES:
            raise ValueError("El IV deber tener", str(BLOCK_SIZE_DES))
        cipher = AES.new(self.key, AES.MODE_CBC, IV)
        textoPad = pad(cadena.encode("utf-8"), self.BLOCK_SIZE_AES)
        textoCifrado = cipher.encrypt(textoPad)
        return textoCifrado

    def descifrar(self, cifrado, IV):
        """Descifra el parámetro cifrado (de tipo binario) con una IV específica, y 
           devuelve la cadena en claro de tipo String"""
        if len(IV) != self.BLOCK_SIZE_AES:
            raise ValueError("El IV deber tener", str(BLOCK_SIZE_DES))
        decipher = AES.new(self.key, AES.MODE_CBC, IV)
        textoPad = decipher.decrypt(cifrado)
        textoClaro = unpad(textoPad, self.BLOCK_SIZE_AES).decode("utf-8")
        return textoClaro

class AES_CIPHER_ECB:

    BLOCK_SIZE_AES = 16 # AES: Bloque de 128 bits

    def __init__(self, key):
        if len(key) is not 16:
            raise ValueError("La clave debe ser de 16 bytes")
        self.key = key

    def cifrar(self, cadena: str):
        cipher = AES.new(self.key, AES.MODE_ECB)
        textoPad = pad(cadena.encode("utf-8"), self.BLOCK_SIZE_AES)
        textoCifrado = cipher.encrypt(textoPad)
        return textoCifrado

    def descifrar(self, cifrado):
        decipher = AES.new(self.key, AES.MODE_ECB)
        textoPad = decipher.decrypt(cifrado)
        textoClaro = unpad(textoPad, self.BLOCK_SIZE_AES).decode("utf-8")
        return textoClaro

class AES_CIPHER_CTR:

    BLOCK_SIZE_AES = 16 # AES: Bloque de 128 bits

    def __init__(self, key):
        if len(key) is not BLOCK_SIZE_DES:
            raise ValueError("La clave debe ser de 16 bytes")
        self.key = key

    def cifrar(self, cadena: str, nonce: bytes):
        ctr = Counter.new(128,initial_value=int.from_bytes(nonce, byteorder="big"))
        cipher = AES.new(self.key, AES.MODE_CTR, counter=ctr)
        return cipher.encrypt(cadena.encode())

    def descifrar(self, cifrado, nonce: bytes):
        ctr = Counter.new(128,initial_value=int.from_bytes(nonce, byteorder="big"))
        cipher = AES.new(self.key, AES.MODE_CTR, counter=ctr)
        return cipher.decrypt(cifrado).decode()

class AES_CIPHER_OFB:
    def __init__(self, key: bytes):
        if len(key) not in (16, 24, 32):
            raise ValueError("Clave inválida.")
        self.key = key

    def cifrar(self, texto_claro: str, iv: bytes):
        cipher = AES.new(self.key, AES.MODE_OFB, iv=iv)
        return cipher.encrypt(texto_claro.encode())

    def descifrar(self, cifrado: bytes, iv: bytes) -> str:
        cipher = AES.new(self.key, AES.MODE_OFB, iv=iv)
        return cipher.decrypt(cifrado).decode()

class AES_CIPHER_CFB:
    def __init__(self, key: bytes):
        if len(key) is not 16:
            raise ValueError("Clave inválida.")
        self.key = key

    def cifrar(self, texto_claro: str, iv: bytes):
        cipher = AES.new(self.key, AES.MODE_CFB, iv=iv)
        return cipher.encrypt(texto_claro.encode())

    def descifrar(self, cifrado: bytes, iv: bytes) -> str:
        cipher = AES.new(self.key, AES.MODE_CFB, iv=iv)
        return cipher.decrypt(cifrado).decode()

class AES_CIPHER_GCM:
    def __init__(self, key: bytes):
        if len(key) is not 16:
            raise ValueError("Clave inválida.")
        self.key = key

    def cifrar(self, texto_claro: str, nonce: bytes):
        cipher = AES.new(self.key, AES.MODE_GCM, nonce=nonce)
        cifrado, tag = cipher.encrypt_and_digest(texto_claro.encode())
        return cifrado, tag

    def descifrar(self, cifrado: bytes, nonce: bytes, tag: bytes):
        cipher = AES.new(self.key, AES.MODE_GCM, nonce=nonce)
        return cipher.decrypt_and_verify(cifrado, tag).decode()


key = get_random_bytes(16) # Clave aleatoria de 128 bits
IV = get_random_bytes(16)  # IV aleatorio de 128 bits
datos = "Hola Mundo con AES en modo CBC"
d = AES_CIPHER_CBC(key)
cifrado = d.cifrar(datos, IV)
descifrado = d.descifrar(cifrado, IV)

print(datos)
print(cifrado)
print(descifrado)

############################
############################
############################


