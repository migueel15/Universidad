entrada = [(1,3),(4,2),(7,12),(54,13)]
salida = []
for tupla in entrada:
    salida.append(sum(tupla))

salida.sort(reverse=True)
print(salida)
