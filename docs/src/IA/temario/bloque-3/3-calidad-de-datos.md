# 3. Data quality — preparar, comprobar y medir los datos de un modelo

Este bloque recorre el trabajo con datos antes y durante la evaluación de un sistema de IA: adquisición y unión de fuentes, validación, extracción de características, análisis estadístico, compromiso entre cantidad y calidad, problemas frecuentes, métricas ligadas a decisiones y reproducibilidad de los datos.

El ejemplo conductor es un servicio de bicicletas compartidas de Washington, D. C. Las diapositivas describen observaciones horarias de 2011 y 2012 y un total de **17.379 filas**. Cada fila representa una hora. El modelo usa el historial para predecir `next_hour`, los alquileres de la hora siguiente. Las cifras que siguen corresponden a ese conjunto y a los experimentos mostrados; no son umbrales universales.

## 3.1. Complejidad de los datos: adquisición, limpieza y extracción de características

### Adquirir y unir las fuentes

La adquisición recoge datos sin tratar y los combina en una tabla. En el ejemplo se integran:

- Registros de viajes, inicialmente una fila por alquiler, que se agregan por hora.
- Meteorología, ya disponible por hora.
- Calendario de festivos, con una fila por día, cuyo indicador se replica en las 24 horas del día.

La **clave de unión** es la combinación de fecha y hora. Una fila describe una observación y cada columna una variable. La tabla horaria final tiene 17.379 filas; una hora sin viajes no tiene fila en el origen y debe tratarse de forma deliberada. La preparación pasa por tres estados:

```text
RAW (tal como llega) → VALID (tras las reglas de calidad) → FEATURE (formato de entrada del modelo)
```

### Identificador, input y target

Cada columna debe tener un papel explícito:

- **Identificador:** permite ubicar la fila, por ejemplo fecha y hora; no se entrega necesariamente como entrada.
- **Input / feature:** valor que el modelo puede consultar.
- **Target:** valor que el modelo debe predecir.

La tabla de ejemplo contiene fecha, hora, día laborable, estado meteorológico, temperatura, alquileres ocasionales (`casual`), registrados (`registered`), total (`cnt`) y objetivo (`next_hour`). En todas las 17.379 filas se cumple `casual + registered = cnt`; las dos columnas componentes son redundantes para este modelo y no se usan. El target es el total de alquileres de la hora siguiente.

### Reglas de limpieza: comprobar, contar y decidir

No basta con detectar una anomalía: por cada regla se cuenta cuántas filas la incumplen y se decide una acción específica. En las diapositivas se evalúan estas reglas:

| Comprobación | Hallazgo del conjunto | Acción indicada en el ejemplo |
| --- | ---: | --- |
| ¿Hay 24 horas por día? | Faltan 165 horas | Añadir horas nocturnas ausentes con 0 alquileres cuando esa sea la causa |
| ¿La humedad es mayor que 0? | 22 horas con humedad 0, todas el 10 de marzo de 2011 | Tratar esos ceros como ausentes; investigar el sensor |
| ¿El viento es mayor que 0? | 2.180 horas con valor 0 (12,5 %) | Conservar el valor y añadir un indicador 0/1 de viento ausente, según la interpretación del ejemplo |
| ¿La siguiente hora sigue en el tiempo? | 75 filas no van seguidas por la hora siguiente | Construir el objetivo por fecha y hora, no copiando la fila siguiente |

Una **missing value** es una medida esperada que falta; un **impossible value** contradice las reglas del dominio (como humedad cero en este conjunto). El cero puede ser una medida válida, un valor por defecto o un código de ausencia, según la variable. Se debe conservar esa distinción en el contrato y en los datos.

La relación entre la ausencia y otros datos orienta el tratamiento:

- **MCAR (Missing Completely At Random):** la ausencia no se explica por otras variables observadas; el fallo de un sensor es el ejemplo conceptual.
- **MAR (Missing At Random):** la ausencia se explica por otra información registrada, como un cierre del servicio.
- **MNAR (Missing Not At Random):** la ausencia depende del propio valor ausente, por ejemplo una hora sin alquileres que no generó registro.

Las diapositivas ilustran decisiones diferentes: completar con cero cuando una hora nocturna no tuvo alquileres; marcar como cierre el 29 de octubre de 2012 y excluirlo del entrenamiento; revisar primero los registros del servicio para otras ausencias; y completar las 22 humedades anómalas con la mediana de entrenamiento (0,62), manteniendo una columna indicadora de ausencia. La acción debe corresponder a la causa, no aplicarse indiscriminadamente.

### Duplicados e identidad lógica de una fila

La identidad lógica de una observación horaria es la pareja fecha-hora. Dos filas con esa identidad son duplicadas aunque otras columnas difieran. Se agrupan filas por la clave y se inspeccionan los grupos con más de una fila: las copias idénticas se pueden deduplicar; las distintas se reportan para resolver el conflicto.

El conjunto de ejemplo no contiene duplicados. La diapositiva muestra que duplicar la hora de las 17:00, con 782 alquileres, elevaría la media diaria de 263,0 a 283,76. Una sola fila repetida puede alterar estadísticas y entrenamiento.

### Extracción de features y variables cíclicas

Una **feature** es un número que se entrega al modelo; un **feature vector** reúne las features de una fila. Las categorías deben convertirse a representación numérica. Para una variable periódica como la hora, la codificación ordinal 0–23 crea una discontinuidad artificial entre 23 y 0. La codificación circular usa seno y coseno:

\[
hr_{sin}=\sin\left(\frac{2\pi\,hr}{24}\right),\qquad
hr_{cos}=\cos\left(\frac{2\pi\,hr}{24}\right)
\]

Las horas 23 y 0 quedan vecinas en el círculo. Para la fila del 12 de septiembre de 2012 a las 07:00, la diapositiva da `hr_sin = 0,9659`, `hr_cos = −0,2588`, temperatura normalizada `0,52`, alquileres de la hora `cnt = 557`, media móvil de tres horas `mean_3h = 276,0` y target `next_hour = 770`.

La ventana `mean_3h` es *trailing*: solo usa la hora actual y las dos horas pasadas (05:00, 06:00 y 07:00). El target apunta a las 08:00. Esta dirección temporal evita que las features utilicen datos que todavía no existirían al servir una predicción.

## 3.2. Análisis estadístico exploratorio

### Centro, dispersión y forma

El histograma de alquileres por hora tiene asimetría positiva (*right-skew*): predominan los valores pequeños y hay pocas horas con recuentos muy altos. En las diapositivas, la media es **189,46**, la mediana **142**, la desviación típica **181,39** y el máximo **977**. Unos pocos picos desplazan la media por encima de la mediana.

La media y desviación típica resumen centro y dispersión; el histograma muestra la forma y la cola. La distribución no se debe describir como normal solo porque se calculen estas estadísticas.

### Percentiles y umbral operativo

El percentil p es el valor por debajo del que cae aproximadamente p % de las filas. Para calcularlo se ordenan los n valores y se toma la posición \(h=(n-1)p/100\), interpolando entre vecinos si h no es entero.

| Estadístico | Alquileres por hora |
| --- | ---: |
| P25 | 40 |
| P50 (mediana) | 142 |
| P75 | 281 |
| P90 | 451,2 |
| P95 | 563,1 |
| P99 | 782,2 |
| Máximo | 977 |

La diapositiva define como hora *busy* una predicción mayor que 400 alquileres. Ese umbral queda entre P75 y P90. Se remarca que el umbral responde a la política operativa: por encima de 400 se avisa a una persona que decide cuántas bicicletas mover y si la hora es anómala para el servicio.

### Outliers: señalar, explicar y decidir

Un *outlier* está lejos del resto, pero puede ser un error o un evento real. Las diapositivas comparan dos reglas para los alquileres horarios:

1. **Valla superior de Tukey:** \(Q_3+1,5\,IQR=281+1,5\cdot241=642,5\). La regla marca 505 horas.
2. **Z-score:** \(z=(x-\bar{x})/s\); se marca si \(|z|>3\). El límite superior es \(189,46+3\cdot181,39=733,6\), con 244 horas marcadas.

El 81 % de las 505 horas señaladas por Tukey ocurre a las 08:00, 17:00 o 18:00. Son picos reales de desplazamiento y se conservan. En cambio, la humedad cero del 10 de marzo de 2011 se marca mediante Tukey y se corrige por fallo del sensor. Por eso el procedimiento es **flag → explain → decide**: marcar, explicar grupos de valores marcados y decidir si conservar, corregir o descartar.

El huracán Sandy muestra un límite de ambas reglas. El 29 de octubre de 2012 el servicio estuvo cerrado; se observan 22 alquileres y solo 23 de las 24 horas del día. Para el total diario, el z-score es −2,31, que no cruza −3; la valla inferior de Tukey es −1.054, imposible para un recuento no negativo. Ninguna regla lo señala, pero la comprobación de 24 horas detecta la anomalía. La explicación del evento permite marcar el cierre y no entrenar con ese día.

### Correlación de Pearson

El coeficiente de Pearson \(r\in[-1,1]\) describe cuánto se aproximan dos columnas a una relación lineal. Cerca de +1 hay asociación lineal positiva; cerca de −1, negativa; cerca de 0, poca relación lineal. La diapositiva lo define mediante la covariación centrada y normalizada por las desviaciones típicas.

| Par de variables | r | Lectura del ejemplo |
| --- | ---: | --- |
| Temperatura y temperatura aparente | 0,988 | Casi repiten la misma información; se elimina una |
| `cnt` y temperatura | 0,405 | Asociación parcial, insuficiente para retirar una automáticamente |
| `cnt` y humedad | −0,323 | Asociación negativa moderada; no implica causalidad |
| `cnt` y hora | 0,394 | La media horaria tiene dos picos y no sigue una recta |

La correlación ayuda a localizar redundancia, pero no prueba que una variable cause la otra ni detecta relaciones no lineales complejas. El gráfico horario ilustra precisamente dos picos que una sola relación lineal no representa.

## 3.3. Compromiso entre cantidad y calidad

### Curva de aprendizaje y selección del número de filas

Las **training rows** se usan para aprender y las **test rows** se reservan para medir el resultado. La *learning curve* representa el MAE de prueba frente al número de filas de entrenamiento. En el ejemplo las filas de entrenamiento son temporalmente contiguas y preceden a las de prueba.

| Horas de entrenamiento | MAE de prueba (bicicletas) |
| ---: | ---: |
| 500 | 50,99 |
| 1.000 | 46,78 |
| 2.000 | 41,92 |
| 4.000 | 35,08 |
| 8.000 | 35,10 |
| 13.902 | 32,69 |

El modelo del gráfico es un árbol de decisión de profundidad 10. Añadir 1.000 filas a partir de 4.000 mejora menos de 0,5 bicicletas en ese experimento; con más filas el MAE continúa bajando, pero con retornos decrecientes. La decisión no es simplemente «más siempre es mejor»: se valora el coste de recolectar, almacenar y procesar los datos, y el beneficio medido.

### Ruido en etiquetas

**Label noise** son valores incorrectos en la columna target. El experimento reemplaza aleatoriamente una fracción de los targets de entrenamiento; el conjunto de prueba permanece limpio.

| Datos de entrenamiento | MAE de prueba |
| --- | ---: |
| 13.902 horas limpias | 32,69 |
| 13.902 horas, 10 % de targets incorrectos | 40,78 |
| 13.902 horas, 40 % de targets incorrectos | 86,68 |
| 4.000 horas limpias | 35,08 |

El 10 % de targets equivocados añade 8,09 bicicletas de error al entrenamiento completo. Usar solo 4.000 filas limpias añade 2,39 frente al conjunto completo limpio: en este ejemplo, reducir la cantidad pero mantener etiquetas limpias resulta más útil que incorporar etiquetas erróneas.

### Cobertura de situaciones de uso

La **coverage** pregunta si cada situación que encontrará el servicio está suficientemente representada. El ejemplo cuenta observaciones por categoría meteorológica:

| Estado del tiempo | Horas |
| --- | ---: |
| Despejado | 11.413 |
| Bruma | 4.544 |
| Lluvia ligera o nieve | 1.419 |
| Lluvia fuerte o nieve | 3 |

Las diapositivas proponen reunir más ejemplos o derivar la decisión a una persona si una categoría tiene menos de 30 filas. El caso de lluvia/nieve fuerte tiene solo tres horas entre 17.379; el modelo no debe decidir automáticamente en ese escenario poco cubierto. La secuencia de trabajo es **cover → clean → grow**: medir cobertura, corregir objetivos defectuosos y aumentar datos mientras la mejora justifique su coste.

## 3.4. Problemas frecuentes: missing, duplicates, leakage y bias

### Information leakage

Hay *leakage* si durante el desarrollo se usa información que el modelo no tendría al predecir. El error medido entonces parece menor que el error real en uso.

| Variante probada | MAE con leakage | MAE sin leakage |
| --- | ---: | ---: |
| Barajar filas antes de separar train/test | 23,92 | 32,69 |
| Usar una media centrada de tres horas (incluye la siguiente hora) | 25,19 | 32,94 |
| Predecir `cnt` con `casual` y `registered` de esa misma hora | 5,12 | 85,26 |

Cada caso reduce artificialmente el error. La evaluación debe repetir el cálculo con un split temporal y features calculadas solo con el pasado. Si el primer error es sospechosamente menor, hay que buscar leakage.

### Sampling bias

Existe **sampling bias** cuando las filas de entrenamiento no representan las situaciones en las que se usará el modelo. El ejemplo entrena solo con 2011 y prueba en horas de 2012: la media real del conjunto de prueba es 248,8, pero el modelo predice 195,9; el MAE es 66,44. Al incluir 2012 en el entrenamiento, la diapositiva muestra un MAE de 32,69.

La comprobación compara, por grupos como año, estación, tiempo y día laborable, la proporción y la media de las filas de entrenamiento con las horas de uso. Si difieren, se añaden observaciones representativas o se declara la limitación.

### Reparar en orden

El orden de reparación importa porque cada paso depende de los anteriores:

1. Dejar una fila por fecha y hora.
2. Construir `next_hour` usando fecha y hora.
3. Calcular features solo con horas pasadas.
4. Hacer el split temporal.
5. Calcular medianas y medias usando solo filas de entrenamiento.

Después de esta reparación, el ejemplo obtiene 17.544 filas; 74 horas nocturnas se completan con cero y 91 horas desconocidas quedan fuera del entrenamiento. La validación de datos se vuelve a ejecutar en cada lote nuevo y bloquea la preparación si falla alguna regla.

## 3.5. Validación cruzada y métricas según la decisión

### Elegir una métrica según el uso

La predicción numérica de alquileres se mide con **MAE**, que en el ejemplo es 32,69 bicicletas (13,1 % de la media horaria de 248,8). Para la decisión de enviar o no una alerta de hora ocupada, importan los costes distintos de falsos negativos y falsos positivos.

La matriz de confusión de la decisión «enviar la hora a una persona» es:

|  | Alerta enviada | Sin alerta |
| --- | ---: | ---: |
| Hora realmente ocupada | TP = 624 | FN = 139 |
| Hora realmente normal | FP = 43 | TN = 2.670 |

De ahí se calculan:

\[
\text{precision}=\frac{TP}{TP+FP}=93,55\%,\qquad
\text{recall}=\frac{TP}{TP+FN}=81,78\%
\]

\[
F1=2\frac{\text{precision}\cdot\text{recall}}{\text{precision}+\text{recall}}=87,27\%,\qquad
\text{accuracy}=\frac{TP+TN}{TP+TN+FP+FN}=94,76\%
\]

*Precision* mide qué proporción de las alertas correspondía a horas ocupadas; *recall*, qué proporción de las horas ocupadas se detectó. Accuracy puede ocultar fallos si las clases están desequilibradas. Si perder una hora ocupada es más costoso que emitir una alerta falsa, el recall tiene prioridad.

### Threshold, curva ROC y AUC

El **threshold** es el valor predicho a partir del que se envía una alerta. Al bajarlo, normalmente suben recall y falsos positivos. Para cada threshold se calcula recall y la tasa de falsos positivos; la curva ROC grafica una frente a otra. El **AUC** es el área bajo esa curva: 0,5 corresponde al azar y 1 a una separación perfecta.

| Threshold | Recall | Tasa de FP | F1 | Horas enviadas |
| ---: | ---: | ---: | ---: | ---: |
| 300 | 0,991 | 0,121 | 0,819 | 1.084 |
| 350 | 0,945 | 0,055 | 0,883 | 870 |
| 400 | 0,818 | 0,016 | 0,873 | 667 |
| 450 | 0,699 | 0,005 | 0,814 | 547 |

El AUC mostrado es 0,9895. Threshold 350 alcanza el F1 más alto de la tabla, detecta 94,5 % de las horas ocupadas y envía 870 horas a revisión. El threshold final depende de la capacidad humana disponible y del coste relativo de cada tipo de error.

### Cross-validation respetando el tiempo y el despliegue

La validación cruzada repite entrenamiento y validación para estimar la variabilidad. En una serie temporal, cada *forward fold* entrena con horas anteriores y valida con las posteriores:

| Fold | MAE de validación |
| ---: | ---: |
| 1 | 55,74 |
| 2 | 27,06 |
| 3 | 25,82 |
| 4 | 44,90 |
| 5 | 44,73 |

La media es **39,65**, con desviación **12,87**. El *shuffled cross-validation* da 24,15 ± 0,35, pero resulta demasiado optimista: las horas de validación quedan rodeadas por horas de entrenamiento. El split debe reflejar cuándo y cómo se desplegará el modelo.

Las diapositivas formulan cuatro preguntas para elegir el split: ¿se predice el futuro con el pasado? Usar *forward folds*. ¿Hay varias muestras por unidad (por ejemplo, estación)? Mantener cada unidad en un solo lado. ¿Hay eventos raros? Conservar su proporción en todos los folds cuando la evaluación lo requiera. Si ninguna condición especial aplica, usar folds aleatorios.

## 3.6. Calidad de infraestructura y versionado de datos

### Hashes para identificar los datos exactos

Un **hash** es un código calculado sobre los bytes de un archivo. Con SHA-256, cambiar un byte cambia el hash. Guardar el hash junto al resultado permite comprobar que se evaluó el mismo archivo; si el hash no coincide, los datos cambiaron y debe detenerse la comparación.

### DVC y almacenamiento de archivos de datos

Git guarda código y archivos pequeños como punteros; **DVC** conserva archivos de datos grandes en almacenamiento de datos aparte y versiona en Git sus punteros. El flujo mostrado es `dvc add hour.csv`, commit del puntero junto al código y `dvc push` para enviar los datos al almacenamiento. Para recuperar una versión, se vuelve al commit y se ejecuta `dvc pull` para traer exactamente los datos correspondientes.

### CSV y Parquet

CSV almacena texto con una fila por línea y valores separados por comas. Parquet almacena la tabla en formato binario columnar, de modo que puede leer solo las columnas solicitadas.

| Medida del benchmark mostrado | CSV | Parquet |
| --- | ---: | ---: |
| Tamaño | 1.156.736 bytes | 247.945 bytes |
| Leer todas las columnas | 29,2 ms | 6,3 ms |
| Leer solo `cnt` | 17,3 ms | 2,6 ms |

En esa máquina y tabla, Parquet ocupa 4,7 veces menos y es 4,6 veces más rápido de leer. Los tiempos dependen del equipo, el almacenamiento y la consulta; el resultado ilustra la ventaja potencial de un formato columnar, no un benchmark universal.

### Reproducir un resultado

La identidad reproducible de un resultado se compone de seis partes:

1. **Code:** commit del script de entrenamiento.
2. **Data:** archivo y hash exactos.
3. **Split:** qué filas se usaron para train y test.
4. **Configuration:** tipo de modelo e hiperparámetros.
5. **Environment:** versiones de Python y librerías.
6. **Weights:** artefacto del modelo entrenado y su hash.

El ejemplo registra un MAE de 32,69 con el commit del código; `hour.csv` y su SHA-256; las primeras 13.902 filas para entrenamiento y las últimas 3.476 para prueba; árbol de decisión de profundidad 10 y semilla 42; Python 3.12, pandas 3.0, scikit-learn 1.9; y el archivo de pesos junto con su hash. Estas versiones son los valores del ejemplo del material, no requisitos actuales de instalación. La semilla fija las elecciones aleatorias reproducibles del proceso.

## Resumen de procedimientos

| Procedimiento | Pregunta práctica |
| --- | --- |
| `RAW → VALID → FEATURE` | ¿En qué estado está cada fila? |
| `CHECK → COUNT → DECIDE` | ¿Qué regla falla, cuántas filas afecta y qué hacer? |
| `CENTRE · SPREAD · TAIL · RELATION` | ¿Cómo se comporta cada columna? |
| `FLAG · EXPLAIN · DECIDE` | ¿Un valor extremo es error o evento real? |
| `COVER · CLEAN · GROW` | ¿Hay cobertura y calidad suficiente a un coste razonable? |
| `MISSING · DUPLICATE · LEAK · BIAS` | ¿Qué problemas pueden invalidar la evaluación? |
| `THE METRIC FOLLOWS THE DECISION` | ¿Qué error importa más en el uso real? |
| `CODE · DATA · SPLIT · CONFIG · ENVIRONMENT · WEIGHTS` | ¿Se puede reconstruir el resultado exacto? |

## Referencias relacionadas

- [Conceptos básicos de estadística](../bloque-1/1-5-conceptos-basicos-de-estadistica.md)
- [Retos en el desarrollo de aplicaciones basadas en IA](../bloque-1/1-2-retos-en-el-desarrollo-de-aplicaciones-basadas-en-ia.md)
- [Apuntes de clase — Miércoles 14 de octubre de 2026](../../notas-clase/clase-2026-10-14.md)
- [Arquitectura y diseño — Bloque 2](../bloque-2/2-arquitectura-y-diseno.md)

## Material de referencia

Diapositivas suministradas para el Bloque 3, *Data quality* (32 diapositivas). La copia adjunta no se duplica dentro del repositorio.
