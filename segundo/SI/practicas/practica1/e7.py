dic = {'a':1,'b':2,'c':12,'d':4, 'e':2}
out = {}
print(dic)
filtro = int(input("Introduce un valor a eliminar: "))

for k,v in dic.items():
    if(v != filtro):
        out[k] = v

print(out)
