# Un dato atómico es lo mismo que un vector de longitud 1
x <- 5

# Definición de vectores y composición
x <- c(1, 2)
y <- c(3, 6, 7, 11)
z <- c(x, y)

# Vectores lógicos y de cadenas
a <- c(T, F, T)
b <- c("dsd", "fdf")

# Secuencias
1:10
-10:-1
seq(1, 10, 2)

# Subsetting en sus dos formas
z <- 11:20
z[c(3, 4, 6)]
z[c(T, T, F, F, F, F, T, T, T, T)]

# Cuando pido índices que no existen recibo NA
x[2:5]


# Definición de listas
ll <- list(2, 3, "vc", "dfs", T, F)

# Esto es una sublista
ll[1]

# Esto extrae un valor de una lista
ll[[1]]

####################################################

x <- c(1, 2, 5)

# Definiendo un vector con nombres para sus elementos
x <- c(a=1, b=2, c=5)

# Accediendo a los elementos de un vector por su nombre
x[c("c", "b")]

# Recuperando los nombres del vector
names(x)

# Modificando los nombres del vector
names(x)<-c("n1", "n2", "n3")

# Definiendo una lista con nombres para sus elementos
resultado <- list(datos=c(1, 2, 3, 6), total=12, nombre="hijos")

# Accediendo a los elementos de una lista por índice y por nombre
resultado[[1]]
resultado[1]
resultado[["datos"]]
resultado["datos"]
resultado$datos

# Recuperando los nombres de la lista
names(resultado)

####################################################

# Definición de una función en R y su asignación a una variable para poder usarla
ff <- function(x, y){
  x + 2 * y
}

# Lo podemos llamar con números o con vectores
ff(2,3)
ff(1:5, 11:15)

# Definición de función con valor por defecto en uno de sus argumentos
ff2 <- function(x, y=10){
  x + 2 * y
}

ff2(2,3)
ff2(2)

# Diversas formas de llamar a la función
ff(2, 100)
ff(100, 2)
ff(x=2, y=100)
ff(y=100, x=2)
ff(y=100, 2)

# sum admite un número indefinido de argumentos y se puede utilizar con un vector o con múltiples argumentos
sum(1, 3, 4, 23)
sum(c(1, 3, 4, 23))

# Pero ojo, mean no funciona igual. Hay que llamarlo con un vector
mean(1, 3, 4, 23)
mean(c(1, 3, 4, 23))

####################################################

# Definición de matrices
A <- matrix(c(1, 2, 5, -1, 2, 11, 2, 3, 0), ncol=3)
B <- matrix(c(1, 2, 5, -1, 2, 11, 2, 3, 0), ncol=3, byrow=T)


# acceso a un elemento
A[2,3]

# Ojo: acceso como vector
A[4]

#producto como vectores
A * B

# Producto matricial
A %*% B

# Solución de sistema de ecuaciones lineal
b <- c(1, -1, 2)
solve(A, b)

# Resolver una ecuación matricial
M <- matrix(c(1, 12, 5, -21, 21, 1, -2, 3, 15), ncol=3)
solve(A, M)

# Inversa de una matriz
solve(A)

# Mala idea: resolver un sistema, utilizando la matriz inversa
solve(A)%*%b

####################################################

data("AirPassengers")
ap <- as.vector(AirPassengers)

# operaciones básicas
length(ap)
sum(ap)
mean(ap)
sort(ap)

# Mediana
median(ap)
mean(sort(ap)[72:73])

# Moemento ordinario de orden 2
mean(ap^2)

# Momento central de orden 1 = 0
mean(ap-mean(ap))

# Varianza
mean((ap - mean(ap))^2)
mean(ap^2) - mean(ap)^2
sum((ap - mean(ap))^2)/length(ap)
var(ap) * (length(ap)-1) / length(ap)

# cuasivarianza
sum((ap - mean(ap))^2) / (length(ap)-1)
var(ap)

# Desviación típica
sqrt(mean(ap^2) - mean(ap)^2)
sd(ap) * sqrt((length(ap)-1) / length(ap))

# Funciones para calcular momentos
Mord   <- function(data, n) mean(data^n)
Mcentr <- function(data, n) mean((data - mean(data))^n)

# Histograma
hist(ap)

# Coeficiente de asimetría
Mcentr(ap, 3) / Mcentr(ap, 2) ^ (3/2)

# Coeficiente de APuntamiento
Mcentr(ap, 4) / Mcentr(ap, 2) ^ 2 - 3

# Covarianza
x <- ap
y <- 1:144
mean(x * y) - mean(x) * mean(y)
cov(x, y) * (length(ap)-1) / length(ap)

####################################################

# Cuantiles en R
quantile(ap)
quantile(ap, .1)

# Vemos que la interpolación de cuantiles es ligeramente diferente de unos lenguajes de progamación a otros
quantile(1:100, .1)

# Funciones sample y table
xx <- sample(1:10, 100, replace=T)
table(xx)

# Funciones min y max
min(ap)
max(ap)

# Función findInterval para agrupar en intervalos
seq(0, 700, 50)
findInterval(ap, seq(0, 700, 50))

# Tabla de frecuencias de intervalos
50*(findInterval(ap, seq(0, 700, 50))-1)
table(50*(findInterval(ap, seq(0, 700, 50))-1))

# Uso de NA
sum(xx)
sum(xx, na.rm=T)
mean(xx, na.rm=T)

# Indentificación y eliminación de NAs
is.na(xx)
xx[!is.na(xx)]

####################################################

x <- 1:10
y <- 2 * x + 3 + runif(10, -1, 1)

# función gráfica plot
plot(x, y, xlim=c(0, 11), ylim=c(0, 25), type="p", xlab="puntos x", ylab="puntos y")


# funciones básicas gráficas
abline(3, 2, col="red")
abline(h=5)
abline(v=4, col="blue")
points(c(6,7), c(10,12), col="green")

#representación de funciones
x <- seq(0, 4*pi, .1)
y <- sin(x)
plot(x,y, type="l")

# Generación directa de un gráfico en disco
jpeg("c:/data/ejemplo.jpg")
plot(x, y, xlim=c(0, 11), ylim=c(0, 25), type="p", xlab="puntos x", ylab="puntos y")
abline(3, 2, col="red")
abline(h=5)
abline(v=4, col="blue")
points(c(6,7), c(10,12), col="green")
dev.off()

####################################################

# Creación de un data frame y visualización
df <- data.frame(x=c(1,2,3), y=c("a", "xx", "jo"))
df
View(df)

# Descarga de nuestro data frame de referencia
dfMec <- read.csv("c:/data/dfMec.csv")
View(dfMec)

# Acceso a filas
dfMec[1,]
dfMec[10:15,]

# Acceso a una columna
dfMec[[1]]
dfMec[,1]
dfMec$notaFinal

# Acceso a varias columnas
dfMec[,3:4]
dfMec[c("notaFinal", "sexo")]

# Nombres de columnas
names(dfMec)

# Filtrado de filas 
dfMec[dfMec$sexo=="V",]
dfMec[dfMec$sexo=="V" & dfMec$edad>21,]

####################################################

# Sólo una vez:
install.packages("tidyverse")

# Cargamos tidyverse
library(tidyverse)

# Seleccionar columnas
select(dfMec, notaFinal, sexo, edad)

# Filtrar filas en estilo básico y con Dplyr
dfMec[dfMec$edad>20,]
filter(dfMec, edad>20)

# NO HACER: Primera forma de encadenar funciones dplyr
df1 <- filter(dfMec, edad>20)
select(df1, notaFinal, sexo, edad)

# NO HACER: Segunda forma de encadenar funciones dplyr
select(filter(dfMec, edad>20), notaFinal, sexo, edad)

# Funcionamiento del operador Pipe (shift + ctrl + M)
# x %>% f(y) %>% g(z)
# g(f(x, y), z)

# filter, mutate, rename, select
dfMec %>% 
  filter(sexo == "M") %>% 
  mutate(mayorEdad = (edad >= 21) ) %>% 
  rename(ed=edad) %>% 
  select(notaFinal, s=sexo, ed, mayorEdad)

# transmute = mutate + select
dfMec %>% 
  filter(sexo == "M") %>% 
  transmute(notaFinal, s=sexo, ed=edad, mayorEdad = (edad >= 21) )

# Extraer una columna
dfMec$edad
dfMec %>% pull(edad)

####################################################

# Número de filas de un Data Frame
nrow(dfMec)

# Número de columnas de un Data frame
length(dfMec)

#Ordenar un vector
sort(dfMec$notaFinal)

# Ordenar un data frame
dfMec %>% 
  arrange(desc(notaFinal))

# Añadir una nueva columna
dfMec %>% 
  mutate(masEdad=edad+3)

# Sumarizar todo el data frame
dfMec %>% summarise(mediaEdad=mean(edad), mediaNota=mean(notaFinal))

# Agrupar y sumarizar. El uso habitual.
dfMec %>% 
  group_by(edad) %>% 
  summarise(n=n(), mediaNota=mean(notaFinal))

# Agrupar y modificar. Uso avanzado
dfMec %>% 
  group_by(edad) %>% 
  mutate(nota2=notaFinal / mean(notaFinal)) %>% 
  select(edad, notaFinal, nota2) %>% 
  ungroup

# Agrupar por dos variables. No olvidar ungroup!  
dfMec %>% 
  group_by(edad, sexo) %>% 
  summarise(n=n(), mediaNota=mean(notaFinal)) %>% 
  ungroup

####################################################

library(tidyverse)

# Leemos en formato data frame
dfMec <- read.csv('c:/data/dfMec.csv')

# Usamos la alternativa mejorada de la libreria readr (tidyverse) que lee en formato tibble
dfMec2 <- read_csv('c:/data/dfMec.csv')

# Transformando un Data frame en Tibble
tibble(dfMec)
as.tibble(dfMec)

# Transformando un Tibble en Data frame
data.frame(dfMec2)
as.data.frame(dfMec2)

# Funciones dplyr que si entra un data frame devuelven un Data frame
dfMec %>% select(edad, sexo)

# Funciones dplyr que devuelven un Tibble, aunque la entrada sea un Data Frame
dfMec %>% group_by(edad)


# definiendo un Data frame o Tibble sobre la marcha
data.frame(x=1:5, y=11:15)
tibble(x=1:5, y=11:15)


# Definiendo tibbles de una manera más cool con tribble
tibble(x=c("a", "b"), y=c(3, 6))

tribble(
  ~x,  ~y,
  "a", 3,
  "b", 6
)


####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv de Tidyverse
dfMec <- read_csv('c:/data/dfMec.csv')

# Extraemos un nuevo tibble con las variables que vamos a trabajar. 
# NO es necesario y normalmente no se haría, pero aquí lo hacemos por claridad.
df1 <- dfMec %>% select(notaFinal, notaBach)

# Calculamos covarianza y varianzas manualmente
covXY <- mean(df1$notaFinal * df1$notaBach) - mean(df1$notaFinal) * mean(df1$notaBach)
varX  <- mean(df1$notaBach ^ 2) - mean(df1$notaBach) ^ 2
varY  <- mean(df1$notaFinal ^ 2) - mean(df1$notaFinal) ^ 2

# Esta es la pendiente de la recta calculada directamente
covXY / varX

# Y este es el centro de gravedad de la distribución, por la que pasa la recta
mean(df1$notaFinal)
mean(df1$notaBach)

# Por tanto, ésta es la recta:
# y - 4.30 = 0.21 * (x - 7.08)

# Aquí calculamos el termino independiente o Intercept
mean(df1$notaFinal) - covXY / varX * mean(df1$notaBach)


# coef corr lineal de Pearson r
covXY / sqrt(varX * varY)

# coef corr lineal de Pearson r^2
covXY ^ 2 / varX / varY

# Ahora lo calculamos con lm
mod <- lm(notaFinal ~ notaBach, dfMec)

# Extraemos los coeficientes y el coeficiente de determinación
mod$coefficients
summary(mod)$r.squared

# Vemos que efectivamente los datos se parecen poco a una recta
plot(df1$notaBach, df1$notaFinal)
abline(mod)

# Aquí podemos ver como nuestro modelo lineal predice los datos
df1$notaPredicha <- predict.lm(mod)

# Esto es un tibble con los datos predichos y el error cometido
df1 %>% 
  mutate(notaPredicha=predict.lm(mod), err=notaFinal - notaPredicha)

# Aquí sumarizamos el MSE, la varianza de la variable predicha y el coeficiente de determinación R2:
df1 %>% 
  mutate(notaPredicha=predict.lm(mod), err=notaFinal - notaPredicha) %>% 
  summarise(MSE=mean(err ^ 2), varY=mean(notaFinal ^ 2) - mean(notaFinal) ^ 2, R2=1 - MSE / varY )

# Vemos que el ajuste ordinario por minimos cuadrados el coeficiente de determinación (R^2) coincide con 
#   el coeficiente de regresión lineal de Pearson al cuadrado (r^2)

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv de Tidyverse
dfMec <- read_csv('c:/data/dfMec.csv')

# Creamos las matrices para las ecuaciones normales
M <- dfMec %>% transmute(x0=1, x1=notaBach, x2=notaSel) %>% as.matrix
Y <- matrix(dfMec$notaFinal)
A <- t(M) %*% M
B <- t(M) %*% Y

# Resolvemos el sistema para obtener los coeficientes de la regesión lineal
solve(A, B)

# Hacemos lo mismo con la función lm
mod <- lm(notaFinal ~ notaBach + notaSel, dfMec)
summary(mod)

# Estas son las predicciones del modelo
predict.lm(mod)

# Cálculo del coeficiente de determinación
dfMec %>% 
  summarise(MSE = mean((notaFinal - predict.lm(mod)) ^ 2), varY = mean(notaFinal ^ 2) - mean(notaFinal) ^ 2, R2 = 1 - MSE / varY)

# Un modelo con más variables predictoras
mod2 <- lm(notaFinal ~ notaBach + notaSel + minutTeor + minutPrac, dfMec)
summary(mod2)

# Un modelo sin Intercept o término independiente
mod0 <- lm(notaFinal ~ 0 + notaBach + notaSel, dfMec)
mod0

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv y nos fijaremos en el reporte de tipos que nos proporciona,
#   para así poder modificarlo
dfMec <- read_csv('c:/data/dfMec.csv')

# Cargamos nuestro Tibble utilizando read_csv y especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor()
                  ))

# NO UTILIZAMOS ESTO: Esta sería una manera alternativa de especificar los tipos de las columnas.
dfMec <- read_csv('c:/data/dfMec.csv', col_types="dfiddiiiiiiiiiifiiiffii")

# NO UTILIZAMOS ESTO: Esta sería una manera de transformar una columna en un factor, 
#  pero en lugar de eso preferimos leerla directamente como factor.
dfMec %>% mutate(sexo=factor(sexo))

# Aquí podemos ver los nombres de las columnas y sus tipos
names(dfMec)
spec(dfMec)

# Si queremos un uso avanzado de factores podemos utilizar forcats, librería de Tidyverse.
# La librería se instala cuando instalamos Tidyverse, pero necesitamos cargarla en memoria separadamente.
library(forcats)

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor()
                  ))

# Una regresión lineal de una variable cualitativa dicotómica
mod <- lm(notaFinal ~ sexo, dfMec)
# Vemos que la predicción que obtenemos es asignar un valor constante a cada uno de los individuos del mismo sexo
mod
# Vemos que obtenemos un R2 muy bajo
dfMec %>% summarise(MSE  = mean((notaFinal - predict.lm(mod)) ^ 2), 
                    varY = mean(notaFinal ^ 2) - mean(notaFinal) ^ 2,
                    R2   = 1 - MSE / varY)

# Una regresión lineal de una variable cualitativa que toma 5 valores
mod <- lm(notaFinal~nivEstPad, dfMec)
dfMec$nivEstPad

# Vemos que se han creado 4 variables dummy
mod

dfMec %>% summarise(MSE=mean((notaFinal - predict.lm(mod))^2), 
                    varY=mean(notaFinal ^ 2)- mean(notaFinal) ^ 2,
                    R2=1 - MSE / varY)

# Una regresión con una variable cuantitativa y una cualitativa
mod <- lm(notaFinal~notaBach + nivEstPad, dfMec)
# Algo mejor
dfMec %>% summarise(MSE=mean((notaFinal - predict.lm(mod))^2), 
                    varY=mean(notaFinal ^ 2)- mean(notaFinal) ^ 2,
                    R2=1 - MSE / varY)

# Podemos ver la nube de puntos con la variable cuantitativa
plot(dfMec$notaBach, dfMec$notaFinal)

# Y ahora vemos la nube de puntos con la variable cuantitativa y la variable cualitativa con color
plot(dfMec$notaBach, dfMec$notaFinal, col=(dfMec$nivEstPad))
coefs <- mod$coefficients
walk2(coefs[1] + c(0, coefs[3:6]), 1:5, ~abline(.x, coefs[2], col=.y))

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor() ) )

# Creamos una nueva columna
df1 <- dfMec %>% transmute(notaFinal, notaBach, logNotaBach=log(notaBach))

# Modelo de la variable original
mod1 <- lm(notaFinal ~ notaBach,    df1)
summary(mod1)$r.squared

# Modelo de la variable transformada
mod2 <- lm(notaFinal ~ logNotaBach, df1)
summary(mod2)$r.squared

# R hace lo mismo por nosotros sin necesidad de que creemos la nueva columna
mod3 <- lm(notaFinal ~ log(notaBach), dfMec)
summary(mod3)$r.squared

# Podemos tb hacer una transformación en la variable explicada
mod4 <- lm(sqrt(notaFinal) ~ log(notaBach), dfMec)
summary(mod4)$r.squared

# Aquí vemos que esto es equivalente a hacer un ajuste polinómico de segundo grado
#sqrt(Y) = a + bX
#Y = (a + bx)^2
#Y = a1 + b1x + b2x^2

# Podemos definir nuevas variables a partir de varias variables originales
mod5 <- lm(notaFinal ~ log(notaBach + notaSel), dfMec)
summary(mod5)$r.squared

# x1:x2 significa la interacción entre dos variables y en el caso de variables cuantitativas significa 
#   usar una variable predictora que sea su producto
mod6 <- lm(notaFinal ~ notaBach:notaSel, dfMec)
mod6
summary(mod6)$r.squared

# x1 * x2 es equivalente a x1 + x2 + x1:x2
mod7 <- lm(notaFinal ~ notaBach * notaSel, dfMec)
summary(mod7)$r.squared

# Como + y * tienen significados especiales, si queremos construir variables con estos operadores, 
#   tenemos que utilizar la función I() para indicar expresiones literales.
mod8 <- lm(notaFinal ~ I(notaBach + notaSel), dfMec)
mod8
summary(mod8)$r.squared

# Esto es equivalente a mod6
mod9 <- lm(notaFinal ~ I(notaBach * notaSel), dfMec)
mod9
summary(mod8)$r.squared

# Aquí vemos la interacción entre una variable cualitativa y una cuantitativa
# De esta manera le asignamos una pendiente diferente a cada subconjunto de individuos con un nivEstPad diferente
# Es decir, 5 rectas con pendientes diferentes, aunque con el mismo Intercept.
mod10 <- lm(notaFinal ~ notaBach:nivEstPad, dfMec)
mod10

# Esto otro, sin embargo, creaba 5 rectas paralelas. 
# Es decir, con la misma pendiente, pero con intercepts diferentes.
mod11 <- lm(notaFinal ~ notaBach + nivEstPad, dfMec)
mod11

# Si hacemos esto otro, tendremos 5 rectas totalmente diferentes. 
# Cada una con su propio intercept y pendiente.
lm(notaFinal ~ notaBach * nivEstPad, dfMec)

# Intentamos ahora el modelo polinómico de segundo grado.
# Es fundamental el uso de I(), sino lo interpretaría como las interacciones entre una variable y ella misma
mod12 <- lm(notaFinal ~ I(notaBach ^ 2) + notaBach, dfMec)
summary(mod12)$r.squared

# Vemos que el R2 sale diferente al de mod4.
# Esto es algo importante a tener en cuenta cuando hagamos transformaciones en la variable explicada. 
# En este caso, cambia la escala en la que se miden los errores, por tanto no podemos comparar directamente
#  los modelos con el R2 que nos proporciona lm.
# Tendríamos que hacerlo con un R2 calculado manualmente midiendo directamente el error con la variable y original, 
#   no la transformada.

####################################################

# Una de las reglas de oro más básicas en programación es:
#      NO REPETIR NUNCA CÓDIGO

# Si tengo un código como éste:
a <- x * 2 + 5
b <- y * 2 + 5

# Puedo crear una función y llamarla cada vez con un argumento diferente:
ff <- function(x) {
  x * 2 + 5
}

a <- ff(x)
b <- ff(y)


# Todo en R es una función
# incluidos los operadores:
2 + 3
`+`(2, 3)


# y los comandos también:
if (2 == 2) print("hola")
`if`(2 == 2, print("hola"))

for (i in 1:4) print (i)
`for`(i, 1:4, print(i))


# Ejemplo clásico de programación recursiva:
gcd <- function(a, b) {
  if (b) gcd(b, a %% b) else a
}

gcd(30, 66)


# A veces tengo código similar, pero no exactamente igual. 
# En este caso, tampoco quiero repetir el código.
# Esto se puede solucionar con programación orientada a objetos (OOP) o con programación funcional (FP)
x <- 1:10

y <- x + 2
m <- mean(y)
median(x[x > m])

y <- x + 2
m <- exp(mean(log(y)))
median(x[x > m])

# La solución con programación funcional es pasar como argumento la función que varia:
oper <- function(x, f) {
  y <- x + 2
  m <- f(y)
  median(x[x > m])
}

oper(x, mean)
oper(x, function(x) exp(mean(log(x))))

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor() ) )

# Aplicaciones básicas de map, pero que realmente no son necesarias
map(1:10, cos)
cos(1:10)

map_dbl(1:10, `+`, 23)
(1:10) + 23

# Aplicamos map a una lista de vectores. Aquí sí que no podemos hacerlo de otra manera
list(1:10, 11:20, 21:30) %>% map(mean)

# Nos aprovechamos de que un data frame es una lista de vectores columna
# Aplicamos mean, pero en las columnas no numéricas me da warnings
dfMec %>% map(mean)

# Función is.numeric: me dice si un vector es numérico o no
is.numeric(dfMec$notaFinal)
is.numeric(dfMec$sexo)

# Lo aprovecho con la función keep, para quedarme sólo con las columnas que son numéricas
dfMec %>% keep(is.numeric) %>% map_dbl(mean)


# Defino una función que me hace un ajuste lineal
linearAdj <- function(df, y, x) {
  lm(str_c(y, "~", x), df)
}

# Cuatro maneras equivalentes de llamar a map para que me prueba a hacer ajustes lineales simples con todas las 
#  variables del Data Frame
# Lo que estoy haciendo es generar una lista de 22 modelos de regresión lineal simple
mods <- names(dfMec)[-1] %>% map(linearAdj, df=dfMec, y="notaFinal")
mods <- map(names(dfMec)[-1], linearAdj, df=dfMec, y="notaFinal")
mods <- names(dfMec)[-1] %>% map( ~linearAdj(df=dfMec, y="notaFinal", .) )
mods <- names(dfMec)[-1] %>% map( function(x) linearAdj(df=dfMec, y="notaFinal", x) )

# Y otra manera equivalente que no usa la definición previa de función, sino que directamente la transcribe
mods <- names(dfMec)[-1] %>% map( ~lm(str_c("notaFinal", "~", .), dfMec) )

# lm permite recibir la expresión de regresión como formula o como string
lm(notaFinal ~ notaBach, dfMec)
lm("notaFinal ~ notaBach", dfMec)

# Pero si queremos, lo podemos pasar a fórmula con as.formula
lm(as.formula("notaFinal ~ notaBach"), dfMec)


# Otro uso de map, cuando le paso una lista de listas.
# Le digo que me extraiga un elemento de cada elemento de la lista, especificando su índice o su nombre
list(1:10, 11:20, 21:30) %>% map(2)


# Extraigo de los 22 modelos de regresión cuál es el coeficiente de determinación de cada uno
mods %>% map(summary) %>% map_dbl("r.squared")

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor() ) )

# Uso simple de reduce: Sumar todos los elementos de un vector. 
# Aquí reduce no es necesario, podríamos hacerlo con sum
reduce(1:10, `+`)
sum(1:10)


# Recordamos que tenemos dos maneras de calcular la varianza de un vector
x <- 100:140
# Como el momento central del orden 2:
mean((x - mean(x)) ^ 2)
# LA BUENA: Usando momentos ordinarios
mean(x ^ 2) - mean(x) ^ 2


# Vamos a suponer que tenemos cuatro ordenadores con datos en cada uno de ellos y queremos calcular la varianza
#   de manera distribuida
datos <- list(1:20, 21:50, 41:80, 61:120)


# Esto es una función que en cada uno de los ordenadores va a calcular estos acumulados 
#   que son los momentos ordinarios de orden 0, 1 y 2 multiplicados por N
sumas <- function(x) {
  c(length(x), sum(x), sum(x^2))
}

# Esto es una función que a partir de los tres acumulados calcula la varianza
sums2var <- function(sums) {
  sums[3] / sums[1] - (sums[2] / sums[1]) ^ 2
}

# Y esto es un ejemplo del paradigma MAP-REDUCE que se utiliza en el mundo Big Data para el procesado masivo de datos.
# Usamos MAP para calcular en paralelo cosas sobre cada uno de los conjuntos de datos.
# La operación MAP es una operación naturalmente paralelizable.
# Si la función llamada está libre de efectos colaterales, se puede ejecutar simultáneamente para cada elemento.
# Nos traemos el resultado a un ordenador central y hacemos REDUCE para "mezclar" los datos y obtener el resultado final.
# REDUCE no es paralelizable, pero es algo que hacemos al final con una cantidad muy reducida de información.
map(datos, sumas) %>% reduce(`+`) %>% sums2var

# Esto es lo mismo, pero implementando sums2var directamente en la línea
map(datos, sumas) %>% reduce(`+`) %>% { .[3] / .[1] - (.[2] / .[1]) ^ 2 }


# Por supuesto, como en este caso los datos están todos en nuestro ordenador, podríamos haber hecho esto otro.
# Aquí vemos otro ejemplo de uso de reduce.
x <- reduce(datos, c)
mean(x ^ 2) - mean(x) ^ 2

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor() ) )

# recordamos el uso básico de map
map_dbl(1:10, cos)

# map2 es similar, pero con dos listas de la misma longitud y una función binaria
x <- runif(10, 0, 10)
y <- runif(10, 0, 10)
map2_dbl(y, x, atan2)
map2_dbl(x, y, max)

# Se puede hacer lo mismo con pmax. Ojo: max es diferente.
pmax(x, y)
max(x,y)

# pmap es similar, pero se recibe una lista de listas o un data frame.
# se ejecuta con una función que tenga tantos argumentos como columnas el data frame
# Para el ejemplo, primero seleccionamos las columnas numéricas utilizando select_if
df1 <- dfMec %>% select_if(is.numeric)
pmap(df1, sum)

# Vamos a utilizar str_c de la librería stringr
str_c("ho", "la")
# Opción con separador
str_c("ho", "la", "xx", "kssl", sep="+")
# Opción paralela con varios vectores de cadenas
str_c(c("hjoh", "fedf", "jkjllk"), c("aaa", "bbb", "cccc"), sep="+")
# Lo mismo, pero colapsando al final todos los vectores en uno usando el separador especificado en collapse
str_c(c("hjoh", "fedf", "jkjllk"), c("aaa", "bbb", "cccc"), sep="+", collapse='*' )


# Hacemos el producto cartesiano de varias opciones con crossing, tambien de tidyverse (tidyr)
crossing(x=1:5, y=11:13, z=21:22)

# Vamos a generar todas las regresiones lineales simples con interaccion de variables
# Para dos variables numéricas son su producto:
lm(notaFinal~notaBach:notaSel, dfMec)

# Si las dos variables son la misma, es lo mismo que usar esa variable de manera simple
lm(notaFinal~notaBach:notaBach, dfMec)

# Para variables cualitativas es el producto cartesiano de sus posibilidades
lm(notaFinal~sexo:nivEstPad, dfMec)

# Generamos en varPred un vector con todas las variables predictoras
varPred <- names(dfMec[-1])
# Y ahora generamos todas las combinaciones de interacciones de variables, usando pmap
varPredInter <- crossing(x=varPred, y=varPred) %>% pmap_chr(str_c, sep=':')

# Hacemos una función que haga un ajuste lineal
linearAdjust <- function(df, y, x) {
  lm(str_c(y, "~", x), df)
}

# Y ahora generamos los 22 modelos de una variable
mods1 <- varPred %>% map(~linearAdjust(dfMec, "notaFinal", .))
# Y los 484 modelos de la interacción de dos variables
mods2 <- varPredInter %>% map(~linearAdjust(dfMec, "notaFinal", .)) 

# Y ahora con map vemos sus coeficientes de determinación
mods1 %>% map(summary) %>% map_dbl("r.squared")
mods2 %>% map(summary) %>% map_dbl("r.squared")

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor() ) )

# Esto sería un ejemplo de usar map para leer una serie de data frames de disco y meterlos todos en una lista
dfs <- list("data1.csv", "data2.csv") %>% map(read_csv)

# Y esto sería un uso típico de walk para grabar unos data frames a disco
# Aquí usamos walk en lugar de map, porque estamos haciendo un proceso de salida a disco, sin devolver ningún resultado
list(list(df1, "data1.csv"), list(df2, "data2.csv")) %>% walk2(write_csv)


# Hacemos una función que haga un ajuste lineal
# La hemos preparado para que devuelva los dos nombres de variable y el modelo
linearAdjust <- function(df, y, x) {
  list(x=x, y=y, mod=lm(str_c(y, "~", x), df))
}

# Y esto es una función que recibe una lista con tres argumentos:
#   Nombre de las dos variables y modelo lineal
# Y graba en disco un gráfico de dispersión con la recta de regresión sobreimpresa
dibujarModelos <- function(mod) {
  jpeg(str_c("c:/data/plots/grafico_", mod$x, ".jpeg"))
  plot(dfMec[[mod$x]], dfMec[[mod$y]])
  abline(mod$mod)
  dev.off()
}


# Generamos los 22 modelos de una variable
varPred <- names(dfMec[-1])
mods <- varPred %>% map(~linearAdjust(dfMec, "notaFinal", .))
# Y generamos los 22 gráficos
mods %>% walk(dibujarModelos)

# Recibimos un warning para la variable nivEstPad, ya que es una variable cualitativa de 5 niveles, y por tanto lo
#   que se genera no es una recta.
# Lo que vemos dibujado por abline en ese caso no es correcto.
# En los demás casos sí vemos un gráfico correcto.
# Dejamos como ejercicio modificar la llamada a la función plot para que indique correctamente los nombres de las 
#  variables en los ejes x e y.

####################################################

library(tidyverse)

# Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
dfMec <- read_csv('c:/data/dfMec.csv',
                  col_types=cols(
                    .default = col_double(),
                    sexo = col_factor(),
                    emailMovil = col_factor(),
                    academia = col_factor(),
                    nivEstPad = col_factor() ) )


# Generamos en varPred un vector con todas las variables predictoras
varPred <- names(dfMec[-1])

# NO LO VAMOS A HACER ASÍ: Podríamos generar así los conjunto de train y set, 
#  pero si el data frame es muy grande sería computacionalmente muy costoso.
df1 <- sample_n(dfMec, 150)
df2 <- setdiff(dfMec, df1)

# Esto es una función para separar nuestro data frame original en dos: uno de entrenamiento y otro de testeo,
#   en la proporción que nos indica p
separarSets <- function(df, p) {
  rDf    <- 1:nrow(df)
  rTrain <- sample(rDf, p * length(rDf))
  rTest  <- setdiff(rDf, rTrain)
  
  list(train=df[rTrain,], test=df[rTest,])  
}


# Y esto es una función que nos devuelve el coeficiente de determinación calculado sobre un data frame, 
#  que no tiene porque ser el mismo sobre el que se ha entrenado el modelo que contiene mod
calcR2 <- function(df, mod, y) {
  MSE  <- mean((df[[y]] - predict.lm(mod, df)) ^ 2)
  varY <- mean(df[[y]] ^ 2) - mean(df[[y]]) ^ 2
  R2   <- 1 - MSE / varY
  
  R2
}


# Nuestra función de ajuste lineal, pero devolviendo el coeficiente de determinación directamente, no el modelo.
linearAdjust <- function(dfTrain, dfTest, y, x) {
  mod <- lm(str_c(y, "~", x), dfTrain)
  calcR2(dfTest, mod, "notaFinal")
}

# Separamos el conjunto de datos en train y test
dfs <- separarSets(dfMec, .7)

# Un ejemplo de cálculo de modelo sobre el conjunto train y de cálculo de R2 sobe el conjunto test
mod <- lm("notaFinal~notaBach", dfs$train)
calcR2(dfs$test, mod, "notaFinal")


# Y ahora calculamos los 22 coeficientes de determinación de los 22 modelos lineales unidimensionales
# Esto nos permitiría escoger el mejor modelo lineal unidimensional eliminando fluctuaciones estadísticas
varPred %>% map_dbl(linearAdjust, dfTrain=dfs$train, dfTest=dfs$test, y="notaFinal")

####################################################

  library(tidyverse)
  
  ###########################
  # Funciones
  ###########################
  
  # Esto es una función para separar nuestro data frame original en dos: uno de entrenamiento y otro de testeo,
  #   en la proporción que nos indica p
  separarSets <- function(df, p1, p2) {
    rDf    <- 1:nrow(df)
    rTrain <- sample(rDf, p1 * length(rDf))
    rTemp  <- setdiff(rDf, rTrain)
    rTest  <- sample(rTemp, p2 * length(rTemp))
    rValid <- setdiff(rTemp, rTest)
    
    list(train=df[rTrain,], test=df[rTest,], valid=df[rValid,])  
  }
  
  
  # Nuestra función básica de ajuste lineal.
  linearAdjust <- function(df, y, x) {
    lm(str_c(y, "~", x), df)
  }
  
  # Y esto es una función que nos devuelve el coeficiente de determinación calculado sobre un data frame, 
  #  que no tiene porque ser el mismo sobre el que se ha entrenado el modelo que contiene mod
  calcR2 <- function(df, mod, y) {
    MSE  <- mean((df[[y]] - predict.lm(mod, df)) ^ 2)
    varY <- mean(df[[y]] ^ 2) - mean(df[[y]]) ^ 2
    R2   <- 1 - MSE / varY
    aR2  <- 1 - (1- R2) * (nrow(df) - 1) / (nrow(df) - mod$rank)
    
    tibble(MSE=MSE, varY=varY, R2=R2, aR2=aR2)
  }
  
  
  # Nuestra función de ajuste lineal, pero devolviendo el coeficiente de determinación directamente, no el modelo.
  calcModR2 <- function(dfTrain, dfTest, y, x) {
    mod <- linearAdjust(dfTrain, y, x)
    calcR2(dfTest, mod, y)$aR2
  }
  
  
  ###########################
  # Código principal
  ###########################
  
  # Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
  dfMec <- read_csv('c:/data/dfMec.csv',
                    col_types=cols(
                      .default = col_double(),
                      sexo = col_factor(),
                      emailMovil = col_factor(),
                      academia = col_factor(),
                      nivEstPad = col_factor() ) )
  
  # Generamos en varPred un vector con todas las variables predictoras
  varPred <- names(dfMec[-1])
  
  # Separamos el conjunto de datos en train, test y validación
  dat <- separarSets(dfMec, .6, .5)
  
  # Un ejemplo de cálculo de modelo sobre el conjunto train y de cálculo de R2 sobe el conjunto test
  mod <- linearAdjust(dat$train, "notaFinal", "notaBach")
  calcR2(dat$test, mod, "notaFinal")
  # Lo mismo, en un solo paso
  calcModR2(dat$train, dat$test, "notaFinal", "notaBach")
  # Vemos el rango del modelo (número de parámetros del modelo)
  mod$rank
  
  
  # Y ahora calculamos los 22 coeficientes de determinación de los 22 modelos lineales unidimensionales
  # Esto nos permitiría escoger el mejor modelo lineal unidimensional eliminando fluctuaciones estadísticas
  ar2 <- varPred %>% map_dbl(calcModR2, dfTrain=dat$train, dfTest=dat$test, y="notaFinal")
  
  # Vemos cuál es la mejor variable para una predicción unidimensional
  bestVar <- varPred[which.max(ar2)]
  # Este es el mejor modelo y su R2 sobre el conjunto de test
  bestMod <- linearAdjust(dat$train, "notaFinal", bestVar)
  calcR2(dat$test, bestMod, "notaFinal")
  
  # Calculamos su R2 sobre el conjunto de validación
  calcR2(dat$valid, bestMod, "notaFinal")
  
####################################################

  library(tidyverse)
  
  ###########################
  # Funciones
  ###########################
  
  # Esto es una función para separar nuestro data frame original en dos: uno de entrenamiento y otro de testeo,
  #   en la proporción que nos indica p
  separarSets <- function(df, p1, p2) {
    rDf    <- 1:nrow(df)
    rTrain <- sample(rDf, p1 * length(rDf))
    rTemp  <- setdiff(rDf, rTrain)
    rTest  <- sample(rTemp, p2 * length(rTemp))
    rValid <- setdiff(rTemp, rTest)
    
    list(train=df[rTrain,], test=df[rTest,], valid=df[rValid,])  
  }
  
  # Nuestra función básica de ajuste lineal para múltiples x
  linearAdjust <- function(df, y, x) {
    lm(str_c(y, "~", str_c(x, collapse="+")), df)
  }
  
  # Y esto es una función que nos devuelve el coeficiente de determinación calculado sobre un data frame, 
  #  que no tiene porque ser el mismo sobre el que se ha entrenado el modelo que contiene mod
  calcR2 <- function(df, mod, y) {
    MSE  <- mean((df[[y]] - predict.lm(mod, df)) ^ 2)
    varY <- mean(df[[y]] ^ 2) - mean(df[[y]]) ^ 2
    R2   <- 1 - MSE / varY
    aR2  <- 1 - (1- R2) * (nrow(df) - 1) / (nrow(df) - mod$rank)
    
    tibble(MSE=MSE, varY=varY, R2=R2, aR2=aR2)
  }
  
  # Nuestra función de ajuste lineal, pero devolviendo el coeficiente de determinación directamente, no el modelo.
  calcModR2 <- function(dfTrain, dfTest, y, x) {
    mod <- linearAdjust(dfTrain, y, x)
    calcR2(dfTest, mod, y)$aR2
  }
  
  # Nuestra función estrella que calcula el mejor ajuste lineal
  encontrarMejorAjuste <- function(dfTrain, dfTest, varPos) {
    bestVars <- character(0)
    aR2      <- 0
    
    repeat {
      aR2v <- map_dbl(varPos, ~calcModR2(dfTrain, dfTest, "notaFinal", c(bestVars, .)))
      i    <- which.max(aR2v)
      aR2M <- aR2v[i]
      if (aR2M <= aR2) break
      
      cat(sprintf("%1.4f %s\n", aR2M, varPos[i]))
      aR2 <- aR2M
      bestVars <- c(bestVars, varPos[i])
      varPos   <- varPos[-i]
    }
    
    mod <- linearAdjust(dfTrain, "notaFinal", bestVars)
    
    list(vars=bestVars, mod=mod)
  }
  
  
  
  ###########################
  # Código principal
  ###########################
  
  # Cargamos nuestro Tibble utilizando read_csv, especificando los tipos de las columnas
  dfMec <- read_csv('c:/data/dfMec.csv',
                    col_types=cols(
                      .default = col_double(),
                      sexo = col_factor(),
                      emailMovil = col_factor(),
                      academia = col_factor(),
                      nivEstPad = col_factor() ) )
  
  # Separamos el conjunto de datos en train, test y validación
  dat <- separarSets(dfMec, .6, .5)
  
  # Generamos en varPred un vector con todas las variables predictoras
  varPos1 <- names(dfMec[-1])
  varPos2 <- crossing(var1=varPos1, var2=varPos1) %>% pmap_chr(str_c, sep=":")
  
  
  # EJEMPLO: Hacemos dos pasos de como sería el proceso de calcular nuestro modelo incremental
  # Vemos cuál es la mejor variable para una predicción unidimensional
  ar2v <- varPos1 %>% map_dbl(calcModR2, dfTrain=dat$train, dfTest=dat$test, y="notaFinal")
  i1 <- which.max(ar2v)
  bestVar  <- varPos1[i1]
  varPos1b <- varPos1[-i1]
  ar2v[i1]
  
  ar2b <- varPos1b %>% map_dbl(~calcModR2(dfTrain=dat$train, dfTest=dat$test, y="notaFinal", x=c(bestVar, .)))
  i2 <- which.max(ar2b)
  bestVar  <- c(bestVar, varPos1b[i2])
  varPos1c <- varPos1b[-i2]
  ar2b[i2]
  
  
  # Calculamos el mejor ajuste para variables simples
  bestMod1 <- encontrarMejorAjuste(dat$train, dat$test, varPos1)
  calcR2(dat$valid, bestMod1$mod, "notaFinal")
  
  # Calculamos el mejor ajuste para pares de interacciones de variables
  bestMod2 <- encontrarMejorAjuste(dat$train, dat$test, varPos2)
  calcR2(dat$valid, bestMod2$mod, "notaFinal")
  
####################################################

  ##############################################################
  # M�todos estad�sticos para la computaci�n
  # Escuela T�cnica Superior de Ingenier�a Inform�tica.
  # Universidad de M�laga. Curso 2020 / 21
  # Tema 3. Series temporales
  ##############################################################
  library(tidyverse)
  
  # La carpeta d�nde tengas los datos
  localFolder='c:/data/'
  
  # leemos los datos
  # Le indicamos la frecuencia de la componente estacional
  xx <- scan(str_c(localFolder, 'ts01.dat'))
  uk <- ts(xx, start=c(1969, 1), frequency=4)
  
  # Ejemplo del primer valor de la media movil de orden 3.
  (uk[1] * .5 + uk[2] + uk[3] + uk[4] + uk[5] * .5) / 4
  
  # Componente tendencia, mediante una media movil de orden 3
  tend <- stats::filter(uk, c(.5, 1, 1, 1, .5) / 4)
  # Gr�fico de la serie con la tendencia en rojo
  plot(uk)
  lines(tend, col=2)
  
  
  ##############################################################
  # Descomposici�n multiplicativa
  ##############################################################
  # Componente estacional * aleatoria
  est_aleM <- uk / tend
  # Componente estacional sin normalizar
  est1M <- colMeans(matrix(est_aleM, ncol=4, byrow=T), na.rm=T)
  # Normalizo la componente estacional
  estM  <- est1M / mean(est1M)
  # Componente estacional como serie temporal
  estMC <- ts(rep(estM, 20), start=c(1969, 1), frequency=4)
  # Componente aleatoria
  aleM  <- est_aleM / estM
  # Serie desestacionalizada
  desestM <- uk / estM
  # Gr�fico de la serie desestacionalizada
  plot(uk)
  lines(desestM, col=4)
  
  
  ##############################################################
  # Descomposici�n aditiva
  ##############################################################
  # Componente estacional * aleatoria
  est_aleA <- uk - tend
  # Componente estacional sin normalizar
  est1A <- colMeans(matrix(est_aleA, ncol=4, byrow=T), na.rm=T)
  # Normalizo la componente estacional
  estA  <- est1A - mean(est1A)
  # Componente estacional como serie temporal
  estAC <- ts(rep(estA, 20), start=c(1969, 1), frequency=4)
  # Componente aleatoria
  aleA  <- est_aleA - estA
  # Serie desestacionalizada
  desestA <- uk - estA
  # Gr�fico de la serie desestacionalizada
  plot(uk)
  lines(desestA, col=4)
  
  
  ##############################################################
  # Calculamos una regresi�n lineal de la serie desestacionalizada
  ##############################################################
  # Esto mete en xx un vector 1:n, donde n es el tama�o de uk
  xx <- seq_along(uk)
  # regresi�n lineal
  model <- lm(desestM ~ xx)
  # Calculamos los valores predichos por la recta
  regL <- ts(predict.lm(model), start=c(1969, 1), frequency=4)
  # Hacemos un gr�fico de la serie desestacionalizada con la recta
  plot(desestM)
  lines(regL, col=2)
  
  
  ##############################################################
  # Uso del comando decompose. Debemos obtener los mismos resultados.
  ##############################################################
  descM <- decompose(uk, type="mul")
  descA <- decompose(uk, type="add")
  plot(descM)
  
  
  ##############################################################
  # Autocorrelaci�n
  ##############################################################
  # Funci�n que calcula la autocorrelaci�n para una frecuencia n
  autoCorrel <- function(data, n) {
    # Quita los n �ltimos datos
    xx1 <- head(data, -n)   
    # Quita los n primeros datos
    xx2 <- tail(data, -n)
    # correlaci�n
    summary(lm(xx1 ~ xx2))$r.squared
  }
  
  # Calculamos la autocorrelaci�n para frecuencia 4
  autoCorrel(uk, 4)
  
  # Calculamos las autocorrelaciones para frecuencias 1 a 12
  acValues <- map_dbl(1:12, autoCorrel, data=uk)
  # Y vemos que el m�ximo se alcanza con autocorrelaci�n 4, as� que es la frecuencia correcta.
  acValues
  which.max(acValues)
  
####################################################

# TEMA 4

##############################################################
# M�todos estad�sticos para la computaci�n
# Escuela T�cnica Superior de Ingenier�a Inform�tica.
# Universidad de M�laga. Curso 2020 / 21
# Tema 4. Probabilidad
##############################################################
library(tidyverse)


##############################################################
# Funciones para calcular conjuntos combinatorios
# En pocas l�neas definimos 5 funciones combinatorias
# variaciones, Combinaciones, Variaciones con repetici�n, 
#   Combinaciones con repetici�n y Permutaciones
##############################################################
combU <- function(f, elems, n){
  if (!n) list(integer(0)) else
  if (!length(elems)) list() else
    elems %>% imap(~map(combU(f, f(elems, .y), n - 1), c, .x)) %>% reduce(c) 
}

vari  <- partial(combU, function(e, i) e[-i])
comb  <- partial(combU, function(e, i) e[-1:-i])
variR <- partial(combU, function(e, i) e)
combR <- partial(combU, function(e, i) e[i:length(e)])
permu <- function(e) vari(e, length(e))

##############################################################
# Si quieres entender como funciona combU, aqu� tienes como ser�a 
#   definir dos de las funciones directamente
##############################################################
# variR <- function(elems, n){
#   if (!n) list(integer(0)) else
#   if (!length(elems)) list() else
#     elems %>% map(~map(variR(elems, n - 1), c, .x)) %>% reduce(c) 
# }
# comb <- function(elems, n){
#   if (!n) list(integer(0)) else
#   if (!length(elems)) list() else
#     elems %>% imap(~map(comb(elems[-1:-.y], n - 1), c, .x)) %>% reduce(c) 
# }


##############################################################
# Ejemplos de manejar conjuntos (sucesos) en R
##############################################################
A <- vari(letters[1:5], 2)
B <- vari(letters[3:7], 2)

union(A, B)
intersect(A, B)
setdiff(A, B)
setequal(A, B)


##############################################################
# Ejemplos de c�lculo de cantidades combinatorias
##############################################################
# Combinaciones de 10 elementos tomados de 3 en 3
choose(10, 3)

# Combinaciones de 10 elementos tomados de 3 en 3 con Repetici�n
choose(10 + 3 - 1, 3)

# Variaciones de 10 elementos tomados de 3 en 3
factorial(3) * choose(10, 3)

# Variaciones de 10 elementos tomados de 3 en 3 con Repetici�n
10 ^ 3

# Para n�meros muy grandes, son interesantes lchoose y lfactorial
# Calcula logaritmos de las cantidades
lfactorial(1000)
lchoose(10000, 5000)


##############################################################
# Ejemplo: Generar una baraja espa�ola y extraer dos cartas ordenadas
##############################################################
cartas <- map(c("O", "C", "B", "E"),str_c, 1:10) %>% reduce(c)
vari(cartas, 2)


##############################################################
# Ejemplo: Una urna con 7 bolas blancas y 5 rojas
##############################################################
urna <- map2(c("B", "R"), list(1:7, 1:5), str_c) %>% reduce(c)
# Extraemos dos bolas al mismo tiempo
extr <- comb(urna, 2)
# Nos quedamos con todas las extraciones d�nde las dos bolas son blancas
extB <- extr %>% keep(~all(startsWith(., "B")))
# Nos quedamos con todas las extraciones d�nde las dos bolas son rojas
extR <- extr %>% keep(~all(startsWith(., "R")))
# Extracciones con las dos bolas son iguales
extEq <- union(extB, extR)
# Como las extracciones son equiprobables, podemos calcular la probabilidad de extraer dos bolas iguales
#  dividiendo casos favorables por vasos posibles.
length(extEq) / length(extr)

# TEMA 5 a

##############################################################
# M�todos estad�sticos para la computaci�n
# Escuela T�cnica Superior de Ingenier�a Inform�tica.
# Universidad de M�laga. Curso 2020 / 21
# Tema 5a. Variable aleatoria
##############################################################
library(tidyverse)


##############################################################
# Funci�n densidad de ejemplo
##############################################################
# Esto es la funci�n de densidad del ejemplo de las diapositivas
# Esta funci�n no es vectorial. No se le puede pasar un vector y que lo evalue de golpe.
# Como vamos a utilizar integrate necesitamos que la funci�n densidad que usemos sea vectorial.
# OJO: Tal cual est� NO nos vale.
densV0 <- function(x) {
  if (x < 1)      0
  else if (x < 2) x -1
  else if (x < 3) 3 - x
  else 0
}

# Con map podemos convertir cualquier funci�n de n�meros at�micos en una funci�n vectorial.
# Esta funci�n ya s� nos valdr�a.
densVv <- function(x) map_dbl(x, densV0)

# Pero es m�s eficiente hacerlo directamente utilizando la funci�n vectorial ifelse
# As� que utilizaremos esta versi�n.
# Es equivalente a la anterior densVv, pero usamos esta porque es m�s eficiente.
densV1 <- function(x) {
  ifelse(x < 1, 0, 
  ifelse(x < 2, x -1, 
  ifelse(x < 3, 3 - x, 
                0)))
}



##############################################################
# Comprobaci�n de que su integral vale 1
##############################################################
# Esto tiene que dar 1
integrate(densV1, -Inf, Inf)



##############################################################
# Obtenci�n num�rica de la funci�n de distribuci�n
##############################################################
# OJO: Esto es s�lo una estimaci�n num�rica.
# OJO: Hay que saber calcular la funci�n de distribuci�n de manera algebr�ica.
# Esta funci�n recibe como argumento la funci�n de densidad y un valor x
# y devuelve el valor de la funci�n de distribuci�n F(x)
distV <- function(densV, x) {
  integrate(densV, -Inf, x)$value
}

# Esto es m�s "estilo funcional"
# Recibe una funci�n densidad y devuelve una funci�n de distribuci�n.
# Ojo: as� definida no es una funci�n vectorial
distF <- function(densV) {
  function(x) integrate(densV, -Inf, x)$value
}

# Ejemplo de uso.
distV(densV1, 2)
distV(densV1, 2.5)

# Metemos en distF1 la nueva funci�n
distF1 <- distF(densV1)
# La usamos todas las veces que queramos
distF1(2)
distF1(2.5)



##############################################################
# C�lculo de las probabilidades que se piden en el ejemplo
##############################################################
# OJO: Es una estimaci�n num�rica. Hay peque�as variaciones de precisi�n
# Directamente con la funci�n de densidad
integrate(densV1, -Inf, 1.5)
integrate(densV1, 2.3, Inf)
integrate(densV1, 1.1, 1.7)
integrate(densV1, 1.5, 2.5)

# Lo mismo con la funci�n de distribuci�n
distF1(1.5)
1 - distF1(2.3)
distF1(1.7) - distF1(1.1)
distF1(2.5) - distF1(1.5)



##############################################################
# Esperanza matem�tica
##############################################################
# OJO: Es una estimaci�n num�rica. Hay peque�as variaciones de precisi�n
# Funciones de esperanza matem�tica discreta y continua
espMD <- function(x, p)  sum(x * p)
espMC <- function(densV) integrate(function(x) x * densV(x), -Inf, Inf)$value
 
# Ejemplo: lanzamiento de un dado
x <- 1:6
p <- rep(1/6, 6)
espMD(x, p)

# Esperanza del ejemplo densV1
espMC(densV1)



##############################################################
# Momentos ordinarios y centrales. Caso discreto y continuo
##############################################################
# OJO: Es una estimaci�n num�rica. Hay peque�as variaciones de precisi�n
# Variables discretas
mOrdD  <- function(x, p, n)  sum(x ^ n * p)
mCentD <- function(x, p, n)  sum((x - espMD(x, p)) ^ n * p)

# Variables continuas
mOrdC  <- function(densV, n) integrate(function(x) x ^ n * densV(x), -Inf, Inf)$value
mCentC <- function(densV, n) integrate(function(x) (x - espMC(densV)) ^ n * densV(x), -Inf, Inf)$value

# Ejemplo: C�lculo de la varianza discreta
# De las dos manera habituales, para ver que coinciden
mCentD(x, p, 2)
mOrdD(x, p, 2) - espMD(x, p) ^ 2

# Ejemplo: C�lculo de la varianza continua
# De las dos manera habituales, para ver que coinciden
mCentC(densV1, 2)
mOrdC(densV1, 2) - espMC(densV1) ^ 2



##############################################################
# C�lculo de cuantiles
##############################################################
# OJO: Es una estimaci�n num�rica. Hay peque�as variaciones de precisi�n
# Funci�n para calcular el cuantil de una variable continua
# Hay que pasarle un intervalo d�nde se estima que est� el resultado.
cuantilC <- function(densV, c, interv) {
  uniroot(function (x) distV(densV, x) - c, interv)$root
}

# Mediana
cuantilC(densV1, .5, c(0, 5))

# Percentil 10
cuantilC(densV1, .1, c(0, 5))




##############################################################
# Ejercicio 5.1
##############################################################
# Apartado a
# Primero hay que calcular manualmente que a vale 1/3
# Alternativamente, podr�amos hacerlo en R, definiendo una funci�n que dependa del par�metro:
densParam <- function(a, x) {
  ifelse(x < -1, 0, 
  ifelse(x <  0, 2 * a * (x+1), 
  ifelse(x <  2, a * (2 - x), 
                 0)))
}
# Y otra que diga para cada par�metro cuanto se desvia la integral de 1
funcionObj <- function(a) integrate(function(x) densParam(a, x), -Inf, Inf)$value - 1
# Y ahora buscamos para qu� par�metro esa desviaci�n es 0
uniroot(funcionObj, c(-10, 10))

# Una vez que tenemos claro que el par�metro es 1/3:
densV51 <- function(x) {
  ifelse(x < -1, 0, 
  ifelse(x <  0, 2/3 * (x+1), 
  ifelse(x <  2, 1/3 * (2 - x), 
                 0)))
}

# Apartado b
# La funci�n de distribuci�n habr�a que calcularla anal�ticamente.
# Funci�n de distribuci�n num�rica:
distF51 <- distF(densV51)
# Representaci�n gr�fica
x <- seq(-2, 3, length.out = 101)
# Ojo, distf51 no es una funci�n vectorial, as� que necesitamos map para aplicarla a un vector
y <- map_dbl(x, distF51)
plot(x, y, type="l")

# Apartado c
# Esperanza matem�tica:
espMc(densV51)
# Mediana:
cuantilC(densV51, .5, c(-1, 2))
# Moda
x[which.max(densV51(x))]

# Apartado d
# Varianza:
var51 <- mCentC(densV51, 2)
var51

# Apartado e
# Coeficiente de asimetr�a
mCentC(densV51, 3) / var51^(3/2)
# Curtosis
mCentC(densV51, 4) / var51^2

# Apartado g
# P(X < 1)
distF51(1)
# P(X > -0.5)
1 - distF51(-.5)
# P(|X| < 0.3)
distF51(.3) - distF51(-.3)


# TEMA 5 b

##############################################################
# M�todos estad�sticos para la computaci�n
# Escuela T�cnica Superior de Ingenier�a Inform�tica.
# Universidad de M�laga. Curso 2020 / 21
# Tema 5b. Distribuciones est�ndar
##############################################################
library(tidyverse)

##############################################################
# Distribuciones discretas
##############################################################
##############################################################
# Distribuci�n Uniforme discreta
##############################################################
# Generaci�n n�meros aleatorios uniforme discreta
# 600 tiradas de dados
xx <- sample(1:6, 600, replace=T)
# frecuencia
table(xx)
hist(xx)


##############################################################
# Distribuci�n Binomial
##############################################################
# Distribuci�n binomial
# Simulaci�n de 10000 tiradas de 10 monedas
# Cada elemento ser�a el n�mero de caras que ha salido en cada tirada
xx <- rbinom(10000, 10, .5)
# �cuantas veces han salido 10 caras?
sum(xx == 10)

table(xx)
hist(xx)

# Funci�n de probabilidad
# Probabilidad de sacar 1 cara en 10 tiradas de monedas
dbinom(1, 10, .5)
# Probabilidad de sacar 5 caras en 10 tiradas de monedas
dbinom(5, 10, .5)

# Funci�n de distribuci�n
# Probabilidad de sacar 1 cara o menos
pbinom(1, 10, .5)
# es lo mismo que:
dbinom(0, 10, .5) + dbinom(1, 10, .5)
# Probabilidad de sacar 10 caras o menos. Tiene que valer 1.
pbinom(10, 10, .5)

# Funci�n cuantil:
# Mediana
qbinom(.5, 10, .5)
# Percentil 10
qbinom(.1, 10, .5)

# Ejemplo de dualidad
# Es lo mismo 3 �xitos de 10 con prop = 0.7
dbinom(3, 10, .7)
# que 7 �xitos de 10 con prop = 0.3
dbinom(7, 10, .3)

#Ejemplo diapositiva 11
# Apartado 1
dbinom(2, 15, .02)
# Apartado 2
1 - pbinom(1, 15, .02)
1 - (dbinom(0, 15, .02) + dbinom(1, 15, .02))
pbinom(15, 15, .02) - pbinom(1, 15, .02)
# Apartado 3
dbinom(1, 4, .02)


##############################################################
# Distribuci�n Geom�trica
##############################################################
# OJO: En las diapositivas definimos la dist geom�trica como el punto donde aparece el primer �xito
# En R es el n�mero de intentos PREVIOS al �xito
dgeom(0, .98)
dgeom(1, .98)

# Ejemplo Diapositiva 13
1 - pgeom(2, .98)

# Funci�n de probabilidad equivalente a dgeom
dgeom1 <- function(x, p) p * (1 - p) ^ x
# Funci�n de probabilidad como la definimos en las diapositivas
dgeom2 <- function(x, p) p * (1 - p) ^ (x - 1)

dgeom1(0, .98)


##############################################################
# Distribuci�n de Poisson
##############################################################
# Probabilidad de 10 sucesos si la media es 10
dpois(10, 10)
# Probabilidad de 10 sucesos o menos si la media es 10
ppois(10, 10)
# Aqu� vemos que el 30% de las veces ocurren 8 sucesos o menos (si la media es 10)
qpois(.3, 10)



##############################################################
# Distribuciones continuas
##############################################################
##############################################################
# Distribuci�n Uniforme continua
##############################################################
# Simulamos 1000 valores uniformes entre 10 y 20
runif(1000, 10, 20) %>% floor %>% table
# Representamos la funci�n densidad uniforme U[10, 20]
x <- seq(0, 30, length.out = 101)
y <- dunif(x, 10, 20)

plot(x, y, type="l")
# Probabilidad del intervalo (12, 15)
punif(15, 10, 20) - punif(12, 10, 20)
# Percentil 10
qunif(.1, 10, 20)


##############################################################
# Distribuci�n normal
##############################################################
# Simulamos 1000 sujetos de una poblaci�n de altura media 175 y desv 10
xx <- rnorm(1000, 175, 10)
min(xx)
max(xx)
xx %>% floor %>% table
# Representamos la densidad
x <- seq(140, 210, length.out=200)
y <- dnorm(x, 175, 10)
plot(x, y, type="l")

# �Qu� proporci�n mide m�s de 185
1 - pnorm(185, 175, 10)

# el 10% m�s alto a partir de qu� altura est�?
qnorm(.9, 175, 10)

# Simetr�a: Hay los mismo sujetos por debajo de 165, que por encima de 185
pnorm(165, 175, 10)
1 - pnorm(185, 175, 10)

# Individuos entre mu-sigma y mu+sigma
# Siempre hay 0.6827, independientemente de los valores de mu y sigma
pnorm(185, 175, 10) - pnorm(165, 175, 10)
pnorm(1) - pnorm(-1)
pnorm(1, 0, 1) - pnorm(-1, 0, 1)


# Simulaci�n para ver que sale algo parecido
xx <- rnorm(1000, 175, 10)
sum(between(xx, 165, 185)) / 1000

# Individuos entre mu-2*sigma y mu+2*sigma
pnorm(195, 175, 10) - pnorm(155, 175, 10)
sum(between(xx, 155, 195)) / 1000

# 3 sigma
pnorm(205, 175, 10) - pnorm(145, 175, 10)

# 4 sigma
pnorm(215, 175, 10) - pnorm(135, 175, 10)



##############################################################
# Aproximaci�n de una binomial
##############################################################
# Probabilidad de ganar una  primitiva
1 / choose(49, 6)
# N�mero semanal medio de jugadores es 20e6
# As� que el n�mero medio de acertantes es 
20e6 / choose(49, 6)

# Probabilidades de 1ue haya entre 0 y 15 acertantes una semana
dbinom(0:15, 20e6, 1/choose(49, 6))

n <- 20e6
p <- 1/choose(49, 6)
q <- 1 - p

# Aproximaci�n de una binomial con una dist de Poisson
dbinom(0:15, n, p)
dpois(0:15, n * p)

# Aproximaci�n de una binomial con una dist normal
dbinom(0:15, n, p)
pnorm(0:15+.5, n * p, sqrt(n * p * q)) - pnorm(0:15-.5, n * p, sqrt(n * p * q))

pnorm(1.5, n * p, sqrt(n * p * q)) - pnorm(0.5, n * p, sqrt(n * p * q))
dbinom(1, n, p)


##############################################################
# Aproximaciones de la funci�n densidad
##############################################################
# Histograma de la altura de 10000 sujetos con precisi�n de 1 cm
xx1 <- round(rnorm(10000, 175, 10), 0)
table(xx1)
hist(xx1, seq(125, 225, by=1))

# Histograma de la altura de 10000 sujetos con precisi�n de 0.1 cm
xx2 <- round(rnorm(10000, 175, 10), 1)
table(xx2)
hist(xx2, seq(125, 225, by=.1))

# Histograma de la altura de 10000 sujetos con precisi�n de 0.01 cm
xx3 <- round(rnorm(10000, 175, 10), 2)
table(xx3)
hist(xx3, seq(125, 225, by=.01))

# Esto converge a una normal N(175, 10)
# Este valor = 0.04, me dice que hay aproximadamente un 4% de sujetos entre 174.5 y 175.5
# o un 0.4% entre 174.95 y 175.05
dnorm(175, 175, 10)

# TEMA 6

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


