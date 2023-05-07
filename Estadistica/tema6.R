##############################################################
# M�todos estad�sticos para la computaci�n
# Escuela T�cnica Superior de Ingenier�a Inform�tica.
# Universidad de M�laga. Curso 2020 / 21
# Tema 6. Inferencia estad�stica
##############################################################
library(tidyverse)

##############################################################
# C�lculo de varios valores de las distribuciones de referencia
##############################################################
# nivel de confianza 1 - alpha = 95%
# tama�o de muestra n

n <- 1000

# Normal
qnorm(.975)
qnorm(.025)

# t de student
qt(.975, n - 1)
qt(.025, n - 1)


# Chi square
qchisq(.025, n - 1)
qchisq(.975, n - 1)


##############################################################
# Intervalos de confianza de una normal
##############################################################
m <- 175
s <- 10
n <- 1000

# Intervalos de la media con la t de student y con la normal al 95%
# Vemos que son casi iguales.
# El correcto es el de la t de student. 
# Usar la normal es una aproximaci�n siempre
e1 <- qt(.975, n - 1) * s / sqrt(n)
e2 <- qnorm(.975) * s / sqrt(n)
m + c(-e1, e1)
m + c(-e2, e2)

# intervalo al 99%
e <- qt(.995, n - 1) * s / sqrt(n)
m + c(-e, e)


# Intervalo de confianza de la varianza al 95%
(n-1)*s^2/qchisq(.025, n-1)
(n-1)*s^2/qchisq(.975, n-1)

# Intervalo de la desviaci�n t�pica
sqrt((n-1)*s^2/qchisq(c(.975, .025), n-1))

# TODO Cae!
# Si nos dan la desviaci�n t�pica y hay que calcular la cuasi-desviaci�n t�pica
m <- 170
sn <- 11
n <- 1000
s <- sqrt( n / (n - 1)) * sn


##############################################################
# Intervalos de confianza de la media
##############################################################
# Medimos 100 espa�oles y miden de media 175, con quasidesv tipica 10
m1 <- 175
s1 <- 10
n1 <- 100

# Medimos 100 suecos y miden de media 177, con quasidesv tipica 9
m2 <- 177
s2 <- 9
n2 <- 100

# Suponemos nivel de confianza 95%
# 1 - alpha = 0.95

# Intervalo de confianza para la media de altura de los espa�oles
e1 <- qt(.975, n1 - 1) * s1 / sqrt(n1)
e1
c(m1 - e1, m1 + e1)

# Intervalo de confianza para la media de altura de los suecos
e2 <- qt(.975, n2 - 1) * s2 / sqrt(n2)
e2
c(m2 - e2, m2 + e2)


##############################################################
# Intervalos de la diferencia de medias
##############################################################

# Funciones para facilitar el c�lcul

sp <- function(nA, sA, nB, sB) sqrt(((nA-1) * sA^2 + (nB-1) * sB^2) / (nA + nB - 2)) 
ff <- function(n1, s1, n2, s2) round( (s1^2 / n1 + s2^2 / n2) ^ 2 / 
                   
                                                             ((s1^2 / n1) ^ 2 / (n1 + 1) + (s2^2 / n2) ^ 2 / (n2 + 1)) - 2 )
ef1 <- function(nivSign, n1, s1, n2, s2) {
  qt(1 - nivSign / 2, n1 + n2 - 2) * sp(n1, s1, n2, s2) * sqrt(1 / n1 + 1 / n2) }
ef2 <- function(nivSign, n1, s1, n2, s2) {
  qt(1 - nivSign / 2, ff(n1, s1, n2, s2)) * sqrt(s1 ^ 2 / n1 + s2 ^ 2 / n2) }



# Intervalo de confianza de la diferencia de medias
# nivel de confianza de un 95%
f <- ff(n1, s1, n2, s2)
e95 <- ef2(.05, n1, s1, n2, s2)
m2 - m1 + c(-e95, e95)

e95n <- qnorm(.975) * sqrt(s1^2 / n1 + s2^2 / n2)

# nivel de confianza de un 90%
e90 <- ef2(.1, n1, s1, n2, s2)
m2 - m1 + c(-e90, e90)
# nivel de confianza de un 80%
e80 <- ef2(.2, n1, s1, n2, s2)
m2 - m1 + c(-e80, e80)


# Vemos que al 80% de confianza podemos afirmar que la media de altura de los suecos es superior a la de los espa�oles.
#   (porque el intervalo entero es superior a 0)
# Pero al 90% no podemos afirmarlo

# Hacemos una simulaci�n
# OJO: Aqu� estamos fijando los par�metros de la poblaci�n y las medias muestrales ser�n diferentes de las de nuestro ejemplo
# As� que obtendremos resultados parecidos a los anteriores, pero no id�nticos
muest1 <- rnorm(100, 175, 10)
muest2 <- rnorm(100, 177, 9)

t.test(muest1)
t.test(muest2)

t.test(muest2, muest1)
t.test(muest2, muest1, conf.level=.8)



##############################################################
# Ejemplo de c�lculo de la quasi desviacion tipica a partir de la desv tipica
# Y creaci�n de una muestra con una media y desv para usar t.test
##############################################################
# problema
# media_muestral=175
# desv=10
# tama�o = 100
# nivel de confianza 1 - alpha=0.95
xm <- 175
n  <- 100
sn <- 10
s  <- sn * sqrt(n / (n-1))
e  <- qt(.975, n - 1) * s / sqrt(n)
c(xm - e, xm + e)

# Simula una muestra con esas caracter�sticas y usa t.test
xx <- c(rep(xm - sn, n / 2), rep(xm + sn, n / 2))
t.test(xx)

# Lo mismo para un tama�o de muestra impar
xm <- 175
n  <- 101
sn <- 10
s  <- sn * sqrt(n / (n-1))
e  <- qt(.975, n - 1) * s / sqrt(n)
c(xm - e, xm + e)
# Simula una muestra con esas caracter�sticas y usa t.test
xx <- c(rep(xm - s, (n - 1) / 2), xm, rep(xm + s, (n - 1) / 2))
t.test(xx)



##############################################################
# Ejemplo diapositiva 16 intervalo de proporciones
##############################################################
p <- 27 / 150
e <- qnorm(.975) * sqrt(p * (1-p) / 150)
c(p - e, p + e)



##############################################################
# Contrastes no param�tricos
##############################################################
# Ejemplo tirada de dado
xx <- sample(1:6, 600, replace=T)
table(xx)


# frecuencia
oo <- as.vector(table(xx))
ee <- rep(100, 6)

# Estad�stico de contraste: (algo as� como tama�o del error)
eC <- sum ((oo - ee) ^ 2 / ee)
# Forma alternativa de calcularlo
sum(oo^2 / ee) - sum(oo)

# Como hay 5 grados de libertad, hay que comparar con esto:
qchisq(.95, 5)

# p-value del test no param�trico
# Nos dice la probabilidad de que ocurra lo que ha ocurrido si nuestra hip�tesis es cierta
#  (en este caso, que sea una distribuci�n uniforme)
# Si es un valor muy bajo, deber�amos descartar nuestra hip�tesis.
1 - pchisq(eC, 5)



oo <- c(125, 75, 125, 75, 125, 75)
ee <- rep(100, 6)
# Estad�stico de contraste: (algo as� como tama�o del error)
eC <- sum ((oo - ee) ^ 2 / ee)
eC

# p-value
1 - pchisq(eC, 5)




##############################################################
# Ejemplo diapositiva 34 (Contraste Poisson)
##############################################################
xx <- 0:6
oo <- c(200, 220, 150, 68, 25, 10, 4)

lambda <- sum(xx * oo) / sum(oo)
ee <- c(dpois(0:5, lambda), 1 - ppois(5, lambda)) * sum(oo)

ooA <- c(oo[1:5], oo[6] + oo[7])
eeA <- c(ee[1:5], ee[6] + ee[7])

est <- sum ((ooA - eeA) ^ 2 / eeA)
qchisq(.95, 4)

# df = 4 = numero de modalides (6) - numero par�metros estimados (1) - 1

# p-value
1 - pchisq(est, 4)


##############################################################
# Ejemplo Contraste normal
##############################################################
xx <- round(rnorm(1000, 175, 10), -1)
oo <- as.vector(table(xx))
oo <- c(oo[1]+oo[2], oo[3:6], oo[7]+oo[8])

m <- mean(xx)
s <- sd(xx)
n <- length(xx)
int <- c(0, pnorm(seq(155, 195, by=10), m, s), 1)
ee <- n * (int[-1] - int[-7])
eC <- sum ((oo - ee) ^ 2 / ee)

# Grados de libertad = n�mero de clase - par�metros calculados - 1
df <- length(oo) - 2 - 1

# p-value
1 - pchisq(eC, df)

# Lo estropeamos...
oo2 <- oo + c(0, 0, 0, 0, 30, -30)
eC2 <- sum ((oo2 - ee) ^ 2 / ee)
# Nuevo p-value
1 - pchisq(eC2, df)



##############################################################
# Ejemplo diapositiva 40
##############################################################
oo <- matrix(c(198, 28, 62, 39, 6, 12, 105, 15, 35), byrow=T, ncol=3)
rf <- rowSums(oo) / sum(oo)
cf <- colSums(oo) / sum(oo)

ee <- matrix(rep(cf, 3) * rep(rf, rep(3, 3)), byrow=T, ncol=3) * sum(oo)
eC <- sum ((oo - ee) ^ 2 / ee)
eC
qchisq(.95, 4)
qchisq(.05, 4)

# p-value
1 - pchisq(eC, 4)




##############################################################
# Problema 6.4
##############################################################
m1 <- 8.7
m2 <- 10.9
s1 <- sqrt(1.02)
s2 <- sqrt(1.73)
n1 <- 33
n2 <- 27

ed <- qnorm(.975) * sqrt(s1^2 / n1 + s2^2 / n2)
c(m1-m2 - ed, m1-m2 + ed)



##############################################################
# Problema 6.6
##############################################################
e <- qt(.975, 17) * 7 / sqrt(18)
c(19 - e, 19 + e)



##############################################################
# Problema 6.8
##############################################################
m1 <- 4.3
m2 <- 3.6
s1 <- 0.9
s2 <- 1.9
n1 <- 12
n2 <- 8
f <- round((s1^2/n1 + s2^2/n2)^2/((s1^2/n1)^2 / (n1+1) + (s2^2/n2)^2 / (n2+1)) - 2)
e <- qt(.975, f) * sqrt(s1^2/n1 + s2^2/n2)
d <- m1 - m2
c(d - e, d + e)


##########################
# Unir dos muestras
##########################
mB1 <- 15
sB1 <- 2.7
nB1 <- 7

mB2 <- 17
sB2 <- 2
nB2 <- 8

nB   <- nB1 + nB2
mB   <- (mB1 * nB1 + mB2 * nB2) / (nB1 + nB2)
vB1  <- (nB1 - 1) / nB1 * sB1 ^ 2
vB2  <- (nB2 - 1) / nB2 * sB2 ^ 2
s2B1 <- (vB1 + mB1 ^ 2) * nB1
s2B2 <- (vB2 + mB2 ^ 2) * nB2
vB   <- (s2B1 + s2B2) / nB - mB ^ 2
sB   <- sqrt(vB * nB / (nB - 1))

