lista = [1,3,5,17,8,2,45,86,23,12]
salida = []
final = False
contador = 0
while not final:
    if lista[contador] % 2 != 0:
        salida.append(lista[contador])
        contador += 1
    else:
        final = True

print(lista)
print(salida)
