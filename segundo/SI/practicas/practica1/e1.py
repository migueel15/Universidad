correo = input("Ingresa un correo: ")
nombre = correo.split("@")[0]
dominio = correo.split("@")[1]

print(nombre)
print(dominio)
