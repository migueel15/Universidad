def calcConversion(numero, base, desp):
    return (((numero - base) + desp) % 26) + base

# 1. Implementar la función de descifrado Cesar para alfabeto inglés en mayúsculas, la cual
# descifre los textos cifrados creados por el código anterior.

def cifradoCesarAlfabetoInglesMAY(cadena):
    """Devuelve un cifrado Cesar tradicional (+3)"""
    # Definir la nueva cadena resultado
    resultado = ''
    # Realizar el "cifrado", sabiendo que A = 65, Z = 90, a = 97, z = 122
    i = 0
    while i < len(cadena):
        # Recoge el caracter a cifrar
        ordenClaro = ord(cadena[i])
        ordenCifrado = 0
        # Cambia el caracter a cifrar
        if (ordenClaro >= 65 and ordenClaro <= 90):
            ordenCifrado = calcConversion(ordenClaro, 65, 3)
        # Añade el caracter cifrado al resultado
        resultado = resultado + chr(ordenCifrado)
        i = i + 1
    # devuelve el resultado
    return resultado

def descifrarCesarAlfabetoInglesMAY(cifrado):
    resultado = ""
    i = 0
    while i < len(cifrado):
        ordenLetra = (ord(cifrado[i]))
        ordenDescifrado = 0

        if(ordenLetra >= 65 and ordenLetra <= 90):
            ordenDescifrado = calcConversion(ordenLetra, 65, -3)
        resultado = resultado + chr(ordenDescifrado)
        i = i + 1 
    return resultado

# 2. Modificar las funciones de cifrado y descifrado, para que soporten tanto letras en
# mayúsculas (A..Z) como letras en minúsculas (a..z) en el alfabeto Inglés.

def cifradoCesarAlfabetoIngles(cadena):
    """Devuelve un cifrado Cesar tradicional (+3)"""
    # Definir la nueva cadena resultado
    resultado = ''
    # Realizar el "cifrado", sabiendo que A = 65, Z = 90, a = 97, z = 122
    i = 0
    while i < len(cadena):
        # Recoge el caracter a cifrar
        ordenClaro = ord(cadena[i])
        ordenCifrado = 0
        # Cambia el caracter a cifrar
        if (ordenClaro >= 65 and ordenClaro <= 90):
            ordenCifrado = calcConversion(ordenClaro, 65, 3)
        elif (ordenClaro >= 97 and ordenClaro <= 122):
            ordenCifrado = calcConversion(ordenClaro, 97, 3)
        # Añade el caracter cifrado al resultado
        resultado = resultado + chr(ordenCifrado)
        i = i + 1
    # devuelve el resultado
    return resultado

def descifrarCesarAlfabetoIngles(cifrado):
    resultado = ""
    i = 0
    while i < len(cifrado):
        ordenLetra = (ord(cifrado[i]))
        ordenDescifrado = 0

        if(ordenLetra >= 65 and ordenLetra <= 90):
            ordenDescifrado = calcConversion(ordenLetra, 65, -3)
        elif(ordenLetra >= 97 and ordenLetra <= 122):
            ordenDescifrado = calcConversion(ordenLetra, 97, -3)
        resultado = resultado + chr(ordenDescifrado)
        i = i + 1 
    return resultado

# 3.Modificar las funciones de cifrado y descifrado, para que soporten el cifrado Cesar
# generalizado (C: M → M + i (mod. 26))

def cifradoCesarAlfabetoInglesDesp(cadena, desp):
    """Devuelve un cifrado Cesar tradicional (+3)"""
    # Definir la nueva cadena resultado
    resultado = ''
    # Realizar el "cifrado", sabiendo que A = 65, Z = 90, a = 97, z = 122
    i = 0
    while i < len(cadena):
        # Recoge el caracter a cifrar
        ordenClaro = ord(cadena[i])
        ordenCifrado = 0
        # Cambia el caracter a cifrar
        if (ordenClaro >= 65 and ordenClaro <= 90):
            ordenCifrado = calcConversion(ordenClaro, 65, desp)
        elif (ordenClaro >= 97 and ordenClaro <= 122):
            ordenCifrado = calcConversion(ordenClaro, 97, desp)
        # Añade el caracter cifrado al resultado
        resultado = resultado + chr(ordenCifrado)
        i = i + 1
    # devuelve el resultado
    return resultado

def descifrarCesarAlfabetoInglesDesp(cifrado, desp):
    resultado = ""
    i = 0
    while i < len(cifrado):
        ordenLetra = (ord(cifrado[i]))
        ordenDescifrado = 0

        if(ordenLetra >= 65 and ordenLetra <= 90):
            ordenDescifrado = calcConversion(ordenLetra, 65, -desp)
        elif(ordenLetra >= 97 and ordenLetra <= 122):
            ordenDescifrado = calcConversion(ordenLetra, 97, -desp)
        resultado = resultado + chr(ordenDescifrado)
        i = i + 1 
    return resultado

print("\nPrimer Ejercicio\n")
claroCESARMAY = 'VENI VIDI VINCI AURIA'
print(claroCESARMAY)
cifradoCESARMAY = cifradoCesarAlfabetoInglesMAY(claroCESARMAY) 
descifradoCESARMAY = descifrarCesarAlfabetoInglesMAY(cifradoCESARMAY)
print(cifradoCESARMAY)
print(descifradoCESARMAY)


print("\nSegundo Ejercicio\n")
claroMinuscula = "Esto es UNA prueba con MAYUSCULAS y minisculas"
print(claroMinuscula)
cifrado = cifradoCesarAlfabetoIngles(claroMinuscula)
descifrado = descifrarCesarAlfabetoIngles(cifrado)
print(cifrado)
print(descifrado)

print("\nTercer Ejercicio\n")
desplazamiento = int(input("Ingrese un desplazamiento: "))
claroMinuscula = "Esto es UNA prueba con MAYUSCULAS y minisculas y desplazamiento por input"
print(claroMinuscula)
cifrado = cifradoCesarAlfabetoInglesDesp(claroMinuscula, desplazamiento)
descifrado = descifrarCesarAlfabetoInglesDesp(cifrado, desplazamiento)
print(cifrado)
print(descifrado)
