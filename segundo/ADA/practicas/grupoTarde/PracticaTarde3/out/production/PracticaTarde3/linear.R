datos <- read.csv("/home/miguel/valores.txt")
# Supongamos que ya tienes un dataframe llamado 'datos' con las variables SCORE, DAMAGE, COOLDOWN, DISPERSION y RANGE.

# Ajustar el modelo lineal
modelo <- lm(cbind(DAMAGE, COOLDOWN, DISPERSION, RANGE) ~ SCORE, data = datos)

# Nuevo valor de SCORE para el que deseas predecir las variables
nuevo_score <- 25  # Cambia esto con el valor de SCORE que desees

# Crear un dataframe con el nuevo valor de SCORE
nuevo_datos <- data.frame(SCORE = nuevo_score)

# Predecir los valores de DAMAGE, COOLDOWN, DISPERSION y RANGE para el nuevo SCORE
prediccion <- predict(modelo, nuevo_datos)

# Mostrar las predicciones
print(prediccion)
