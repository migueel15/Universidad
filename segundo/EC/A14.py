print("Ejercicio 1")
cadd = input("ciclos de la instruccion add: ")
cxor = input("ciclos de la instruccion xor: ")
clw = input("ciclos de la instruccion lw: ")
csw = input("ciclos de la instruccion sw: ")
cmulti = input("ciclos de la instruccion mult: ")
cliclostotales = int(cadd)*3 + int(cxor) + int(clw)*3 + int(csw)*2 + int(cmulti)
print(cadd)
print(cliclostotales)
print("El CPI es: ", int(cadd)*3/int(cliclostotales))

print("")
print("Ejercicio 2")
cadd = input("ciclos de la instruccion add: ")
cxor = input("ciclos de la instruccion xor: ")
clw = input("ciclos de la instruccion lw: ")
csw = input("ciclos de la instruccion sw: ")
cmulti = input("ciclos de la instruccion mult: ")

rep = input("veces que se ejecuta la instruccion: ")

cpidespuesadd = input("ciclos despues de la mejora instruccion add: ")
cliclostotales = int(cadd)*3 + int(cxor) + int(clw)*3 + int(csw)*2 + int(cmulti)

fm = (int(rep)*int(cadd))/int(cliclostotales)
sm = int(cadd)/int(cpidespuesadd)

ley = 1/(1-fm+(fm/sm))
print("El CPI es: ", ley)

print("")
print("Ejercicio 3")
tiempo = int(input("tiempo de ejecucion(microseg): "))
cpi = float(input("CPI: "))
frecuencia = int(input("frecuencia del procesador(MHz): "))

ni = ((tiempo*(10**-6))*(frecuencia*(10**6)))/cpi
print("Numero de instrucciones: ", ni)

print("")
print("Ejercicio 4")

frecuencia = float(input("frecuencia del procesador(GHz): "))
frecuencia = frecuencia*(10**9)

perlni = float(input("numero instrucciones perl: "))
bzip2ni = float(input("numero instrucciones bzip2: "))
gccni = float(input("numero instrucciones gcc: "))
mcfni = float(input("numero instrucciones mcf: "))
goni = float(input("numero instrucciones go: "))
hmmerni = float(input("numero instrucciones hmmer: "))
sjengni = float(input("numero instrucciones sjeng: "))
libquantumni = float(input("numero instrucciones libquantum: "))
h264avcni = float(input("numero instrucciones h264avc: "))
omnetppni = float(input("numero instrucciones omnetpp: "))
astarni = float(input("numero instrucciones astar: "))
xalancbmkni = float(input("numero instrucciones xalancbmk: "))

perlcpi = float(input("cpi perl: "))
bzip2cpi = float(input("cpi bzip2: "))
gcccpi = float(input("cpi gcc: "))
mcfcpi = float(input("cpi mcf: "))
gocpi = float(input("cpi go: "))
hmmercpi = float(input("cpi hmmer: "))
sjengcpi = float(input("cpi sjeng: "))
libquantumcpi = float(input("cpi libquantum: "))
h264avccpi = float(input("cpi h264avc: "))
omnetppcpi = float(input("cpi omnetpp: "))
astarcpi = float(input("cpi astar: "))
xalancbmkcpi = float(input("cpi xalancbmk: "))

perltime = ((perlni*(10**11)) * perlcpi) / frecuencia
bzip2time = ((bzip2ni*(10**11)) * bzip2cpi) / frecuencia
gcctime = ((gccni*(10**11)) * gcccpi) / frecuencia
mcftime = ((mcfni*(10**11)) * mcfcpi) / frecuencia
gotime = ((goni*(10**11)) * gocpi) / frecuencia
hmmertime = ((hmmerni*(10**11)) * hmmercpi) / frecuencia
sjengtime = ((sjengni*(10**11)) * sjengcpi) / frecuencia
libquantumtime = ((libquantumni*(10**11)) * libquantumcpi) / frecuencia
h264avctime = ((h264avcni*(10**11)) * h264avccpi) / frecuencia
omnetpptime = ((omnetppni*(10**11)) * omnetppcpi) / frecuencia
astartime = ((astarni*(10**11)) * astarcpi) / frecuencia
xalancbmktime = ((xalancbmkni*(10**11)) * xalancbmkcpi / frecuencia)

tnuevototal = (perltime + bzip2time + gcctime + mcftime + gotime + hmmertime + sjengtime + libquantumtime + h264avctime + omnetpptime + astartime + xalancbmktime)/frecuencia

perlexectime = 9770
bzip2exectime = 9650
gccexectime = 8050
mcfexectime = 9120
goexectime = 10490
hmmerexectime = 9330
sjengexectime = 12100
libquantumexectime = 20720
h264avcexectime = 22130
omnetppexectime = 6250
astarexectime = 7020
xalancbmkexectime = 6900


res = ((perlexectime/perltime) * (bzip2exectime/bzip2time) * (gccexectime/gcctime) * (mcfexectime/mcftime) * (goexectime/gotime) * (hmmerexectime/hmmertime) * (sjengexectime/sjengtime) * (libquantumexectime/libquantumtime) * (h264avcexectime/h264avctime) * (omnetppexectime/omnetpptime) * (astarexectime/astartime) * (xalancbmkexectime/xalancbmktime)) ** (1.0/12.0)

print("El tiempo total de ejecucion es: ",res)