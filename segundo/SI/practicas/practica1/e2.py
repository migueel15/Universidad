nombre = input("Nombre de producto: ")
precio = float(input("Precio de producto: "))
unidades = int(input("Numero de unidades: "))
total = precio*unidades

print('{}:{:5d} unidades x {:6.2f}€ = {:8.2f}€'.format(nombre,unidades,precio,precio*unidades))
