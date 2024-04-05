def findSecondMin(lista):
    menor = segundo = float("inf")
    for el in lista:
        if(el < menor):
            segundo = menor
            menor = el
        elif(el < segundo and el != menor):
            segundo = el

    return segundo
