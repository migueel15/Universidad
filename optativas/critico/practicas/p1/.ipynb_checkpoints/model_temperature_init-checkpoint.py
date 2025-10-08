# Muestra los valores más anómalos del fichero csv suministrado

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import tensorflow as tf
from keras.models import Sequential
from keras.layers import LSTM, Dense
import joblib

# Cargar los datos
df = pd.read_csv("datos.csv",index_col=0,parse_dates=True)

print(df)
print(df.shape) # (7267,1) (según el fichero cambiará el número de filas)

df.plot()
plt.show()

# Crear las ventanas temporales
# Lo que se predice (y) es el "siguiente" valor de la secuencia
# pasando la ventana actual que tenemos.

# Dividir los datos en entrenamiento y prueba es lo habitual
# Aunque en este caso, vamos a querer luego detectar anomalías en todos los datos

# Redimensionar los datos para la RNN
# LSTM espera 3 dimensiones: número muestras, pasos temporales, número features
# P.ej: (5805,10,1)

# Crear la RNN

# Entrenar la RNN

# Un posible criterio de anomalía
# Calcular el error absoluto medio (MAE) de los "siguientes" valores de cada secuencia y los valores predichos
# Calcular el promedio del MAE
# mae = np.mean(mae)

# Otro posible criterio: uso de percentiles. Valores que superen un percentil determinado

# Mostrar las fechas de las anomalías
# las anomalias se refieren a las ventanas, no a filas específicas dentro de la ventana

# Mostrar la gráfica con las anomalías
windows_size=10

fechas_test=df[windows_size:].index.to_numpy() # aquí tengo array con fechas de los datos de test

# Anomalías de ejemplo
anomalies = np.array([True if i % 100 == 0 else False for i in range(len(fechas_test))], dtype=bool)

# Opcion 1
y_test=df["value"][windows_size:]
plt.plot(fechas_test,y_test,color='blue',label='y_test')
# los valores de anomalias en el array anomalies se refieren a las ventanas, no a "y"
# aunque hay el mismo numero de ventanas que de valores
plt.scatter(x=fechas_test, y=y_test, c='red', alpha=anomalies.astype(int),s=50)
plt.legend()
plt.show()

# Opcion 2
# fechas_test = df.drop(df.index[:windows_size]) # Eliminar las primeras filas
#                                     # para que coincida con numero de ventanas y poder mostrar grafica
# plt.plot(fechas_test.index, fechas_test["value"], label='Valores', color='blue')  # Columna de tiempo
# plt.scatter(fechas_test.index[anomalies == True], fechas_test["value"][anomalies == True], c='r', label='Anomalía')

# plt.legend()
# plt.show()
