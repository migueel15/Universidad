# Importamos la libreria que utilizaremos para crear un tibble. Este es similar
# a read.csv pero añade nuevas funcionalidades.

library(tidyverse)
library(readr)
library(purrr)

# Añado mi directorio como ruta del trabajo
setwd("~/Universidad/Estadistica/Trabajo R")

# Para cargar el fichero usamos read_csv dando como parametros el nombre del archivo y col_types. Este último nos permite definir el tipo
# de dato que representa cada columno. En este caso hay algunas columnas que no representan datos numéricos sino que son datos
# cualitativos. A todos estos les pongo la propiedad col_factor(). Por ejemplo sexo representa un factor de dos niveles.

data <- read_csv("21425.csv", col_types=cols(.default=col_double(), sexo=col_factor(),
                                             dietaEsp=col_factor(), nivEstPad=col_factor(),
                                             nivEstudios=col_factor(), nivIngresos=col_factor()))

# Añadimos la columna IMC la cual representa el indice de masa corporal de cada individuo.

data$IMC <- data$peso/data$altura^2


# Eliminamos las filas que contengan algún NA en sus columnas. Este método omite todas las filas con algún NA.

data <- na.omit(data)

# Calculamos la media de todas las columnas numéricas.
# Para ello creamos un nuevo dataframe que filtre aquellas columnas que solo sean numéricas haciendo uso de la funcion keep
# importada de purrr

dfNumerico <- keep(data,is.numeric)
medias <- map_dbl(dfNumerico, mean)
medias
# Calculamos la desviación tipica de cada columna numérica. Para ello partimos del df procesado con datos numéricos.
# "%>%" Nos permite pasar el resultado del calculo anterior a el como parametro al cálculo siguiente.
# De esta forma con el método summarise_all calcula lo definido en la función para todas las columnas del dataframe.
# Esto devuelve un dataframe que convertimos en un vector de resultados utilizando map_dbl y esta funcion específica.

desvTipicas <- dfNumerico %>% 
  summarise_all(function(x) sqrt(mean(x^2) - mean(x)^2)) %>%
  map_dbl(function(x) x)

# Calculamos los coeficientes de regresion y el coeficiente de determinación
# para las 12 regresiones lineales unidimensionales.

varPredictorias <- names(data[3:14])
varPredictorias
# Creo una funcion para calcular los ceficientes de regresión haciendo uso de la función lm y summary para obtener un resumen de
# las principales propiedades.

coefRegresion <- function(df,y,x){
  modelo <- lm(y ~ x, df)
  summary(modelo)$coefficients[2]
}

# Creo una función para obtener el R2 del modelo.

calcR2 <- function(df,y,x){
  modelo <- lm(y ~ x, df)
  summary(modelo)$r.squared
}

# Haciendo uso de map_dbl puedo calcular una funcion para cada  elemento de un vector
# siendo este el dado como parametro .x
# Es este caso calculo el coeficiente de regresión para todos los valores del vector
# varPredictorias y los guardo en un vector llamado coeficientes.

coeficientes <- map_dbl(varPredictorias, ~ coefRegresion(data,data$IMC, data[[.]]))
coeficientes
# De igual forma calculamos el R2 de cada valor del vector varPredictorias.

valoresR2 <- map_dbl(varPredictorias, ~ calcR2(data,data$IMC, data[[.]]))
valoresR2
# Creamos una función que nos devuelva una lista con el nombre de las variables
# "x", "y" y el modelo. esta lista la utilizremos para crear los plots con mayor facilidad

linearAdjust <- function(df, y, x) {
  list(x=x, y=y, mod=lm(str_c(y, "~", x), df))
}

# Creamos una función para crear el plot o el boxplot correspondiente y guardarlo
# en la ubicación definida.
# Si el valor del elemento es numérico se realizará un plot. En otro caso un boxplot.
# abline crea una recta en el plot que representa la recta de regresión.

dibujarModelos <- function(mod) {
  jpeg(str_c("./Imagenes/", mod$x, ".jpeg"))
  
  if (is.numeric(data[[mod$x]])) {
    plot(data[[mod$x]], data[[mod$y]], xlab=mod$x, ylab=mod$y)
    abline(mod$mod, col="red")  
  }else{
    boxplot(formula=data[[mod$y]] ~ data[[mod$x]], xlab=mod$x, ylab=mod$y)
  }
  
  dev.off()
}

# A continuación utilizamos un map para realizar la funcion linearAdjust a todos
# las columnas del dataframe.

mods <- names(data) %>% map(~linearAdjust(data, "IMC", .))

# Utilizando el metodo walk para generar los graficos. Creo la carpeta imagenes
# en caso de no estarlo.
dir.create("Imagenes")
mods %>% walk(dibujarModelos)

# Separamos el dataframe en 3 sets distintos. Entrenamiento, test y validacion.
# Para ello creo una función que tome como parametros el dataframe y los porcentajes
# Haciendo uso de sample y setdiff creo muestras del dataframe con la longitud indicada.
# Por ultimo guardo los 3 subsets en una lista.

separarSets <- function(df, p1, p2) {
  rDf <- 1:nrow(df)
  rTrain <- sample(rDf, p1 * length(rDf))
  rResto  <- setdiff(rDf, rTrain)
  rTest <- sample(rResto, p2*length(rDf))
  rValid <- setdiff(rResto, rTest)
  
  list(train=df[rTrain,], test=df[rTest,], valid=df[rValid,])  
}

setsSeparados <- separarSets(data,.6,.2)

# Buscamos cual de las 12 variables es la que mejor explica la variable IMC. Para ello
# creamos una funcion que calcule el el R2 ajustado

calcR2ajst <- function(df, mod, y) {
  MSE <- mean((df[[y]] - predict.lm(mod, df)) ^ 2)
  varY <- mean(df[[y]] ^ 2) - mean(df[[y]]) ^ 2
  R2 <- 1 - MSE / varY
  ajR2 <- 1 - (1- R2) * (nrow(df) - 1) / (nrow(df) - mod$rank)
  ajR2
}

# Calculamos el coeficiente de determinación 

calcModR2 <- function(dfTrain, dfTest, y, x) {
  mod <- linearAdjust(dfTrain, y, x)
  calcR2ajst(dfTest, mod$mod, y)
}

# Calculamos los R2 ajustados de cada una de las variables. Estás determinarán como de
# dependientes son las variables. Aquella con el valor más alto, será la más relaciada.

AjstR2 <- varPredictorias %>%
  map_dbl(calcModR2,dfTrain=setsSeparados$train,dfTest=setsSeparados$test,y="IMC")

#Aquí calculamos la mejor variable que explique el IMC

x <- which.max(AjstR2)
mejorVar <- varPredictorias[x]

# La mejor variable y su valor:

mejorVar
AjstR2[x]

# Creamos una función para calcular el mejor ajuste

encontrarMejorAjuste <- function(dfTrain, dfTest, varPos) {
  bestVars <- character(0)
  aR2      <- 0
  
  repeat {
    aR2v <- map_dbl(varPos, ~calcModR2(dfTrain, dfTest, "IMC", c(bestVars, .)))
    i    <- which.max(aR2v)
    aR2M <- aR2v[i]
    if (aR2M <= aR2) break
    
    cat(sprintf("%1.4f %s\n", aR2M, varPos[i]))
    aR2 <- aR2M
    bestVars <- c(bestVars, varPos[i])
    varPos   <- varPos[-i]
  }
  
  mod <- linearAdjust(dfTrain, "IMC", bestVars)
  
  list(vars=bestVars, mod=mod)
}

mejorAjuste <- encontrarMejorAjuste(setsSeparados$train, setsSeparados$test, varPredictorias)
mejorMod <- mejorAjuste$mod$mod
calcR2ajst(setsSeparados$valid, mejorMod, "IMC")

# Leemos el dataframe eval

dfEval <- read_csv("eval.csv")

# Para predecir las nueva variable IMC utilizaremos predict.lm
# Creamos la nueva columna con los valores.
# Calculamos el peso en función a la altura y al nuevo IMC. 

dfEval["IMC"] <- predict.lm(mejorMod, dfEval)
dfEval["Peso"] <- dfEval$IMC*dfEval$altura^2

# Guardamos en el archivo evalX el dataframe dfEval

write.csv(dfEval, "evalX.csv", row.names = FALSE)