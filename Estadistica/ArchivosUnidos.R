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