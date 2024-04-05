cadena1 = "este es el contenido de la cadena uno"
cadena2 = "aquí pongo la segunda"

conjunto1 = set()
conjunto2 = set()

for el in cadena1:
    conjunto1.add(el)
for el in cadena2:
    conjunto2.add(el)

salida = conjunto1.difference(conjunto2)

print(salida)
