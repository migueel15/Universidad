from python.whilelanguage import *
from python.whilelanguage.encoding import *

Q = """(3,
while X1 != 0 do
    X1 := X1 - 1;
    X4 := X4 + 1
od;
while X2 != 0 do
    X2 := X2 - 1;
    X4 := X4 + 1
od;
while X3 != 0 do
    X3 := X3 - 1;
    X4 := X4 + 1
od;
X1 := X4)"""

print("TQ",t_steps(Q, [3, 5, 2]))
print("Res:",f_function(Q, [3, 5, 2]))


D = "X1:=X1+1; while X1!=0 do X1:=X1+1 od"

print(while2n(0, D))

def enumera_vectores(k):
    vectores = []

    for n in range(k):
        vectores.append(godeldecoding(n))

    return vectores

print(enumera_vectores(10))

def enumera_programas_while(k):
    programas = []

    for n in range(k):
        programas.append(n2while(n))

    return programas

programas = enumera_programas_while(10)

for i, programa in enumerate(programas):
    print(i, programa)
