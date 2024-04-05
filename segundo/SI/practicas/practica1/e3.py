a = int(input("Inicio: "))
b = int(input("Final: "))

for el in range(a,b):
    if el > 1:
        primo = True
        for div in range(2,el):
            if(el % div == 0):
                primo = False
                break
        if primo:
            print(el)

