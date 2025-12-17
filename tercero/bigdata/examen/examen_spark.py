from pyspark.sql import SparkSession
from pyspark.sql.types import *
from pyspark.sql.functions import col, month, year, round, sum, avg, count
from pyspark.sql import SparkSession
from pyspark.sql.types import (
    StructType,
    StructField,
    IntegerType,
    StringType,
    DoubleType,
    DateType,
)

spark = SparkSession.builder.appName("ExamenSpark").getOrCreate()

# ejercicio 1
schema_peliculas = StructType([
    StructField("id", IntegerType(), True),
    StructField("titulo", StringType(), True),
    StructField("genero", StringType(), True),
    StructField("anyo", IntegerType(), True),
    StructField("duracion_min", DoubleType(), True),
    StructField("precio", DoubleType(), True)
])

schema_ventas = StructType([
    StructField("id_venta", IntegerType(), True),
    StructField("id_pelicula", IntegerType(), True),
    StructField("fecha", DateType(), True),
    StructField("cantidad", IntegerType(), True),
    StructField("metodo_pago", StringType(), True)
])

peliculas = spark.read.csv(
    "peliculas.csv",
    header=True,
    schema=schema_peliculas
)

ventas = spark.read.csv(
    "ventas.csv",
    header=True,
    schema=schema_ventas
)

ventas = ventas.fillna({
    "cantidad": 2,
    "metodo_pago": "Tarjeta"
})

peliculas = peliculas.fillna({
    "duracion_min": 120.0,
    "precio": 15.95
})

peliculas_drama = peliculas.filter(
    (col("genero") == "Drama") & (col("anyo") > 2000)
).select(
    "id", "titulo", "genero", "anyo"
).orderBy("anyo")

peliculas_drama.show()

ventas_mes = ventas.select(
    "id_venta", "id_pelicula", "fecha", "cantidad"
).withColumn(
    "mes", month(col("fecha"))
)

ventas_mes.show(10)


# ejercicio 2
peliculas_caras = peliculas.select(
    "titulo", "genero", "precio"
).orderBy(
    col("precio").desc()
).limit(5)

peliculas_caras.show()

peliculas_por_genero = peliculas.groupBy("genero").agg(
    count("*").alias("total_peliculas")
).orderBy(
    col("total_peliculas").desc()
)

peliculas_por_genero.show()


# ejercicio 3
ventas_enero_2025 = ventas.filter(
    (year(col("fecha")) == 2025) & (month(col("fecha")) == 1)
)

ventas_peliculas = ventas_enero_2025.join(
    peliculas,
    ventas_enero_2025["id_pelicula"] == peliculas["id"],
    "inner"
).select(
    "id_venta", "fecha", "cantidad", "titulo", "genero", "precio"
).orderBy(
    "id_venta"
)

ventas_peliculas.show(15)

total_unidades = ventas.groupBy("id_pelicula").agg(
    sum("cantidad").alias("total_unidades_vendidas")
).orderBy(
    col("total_unidades_vendidas").desc()
).limit(5)

total_unidades.show()


# ejercicio 4
estadisticas_genero = peliculas.groupBy("genero").agg(
    count("*").alias("total_peliculas"),
    round(avg("duracion_min"), 1).alias("duracion_promedio"),
    round(avg("precio"), 2).alias("precio_promedio")
).orderBy(
    col("total_peliculas").desc()
)

estadisticas_genero.show()

ventas_ingresos = ventas.join(
    peliculas,
    ventas["id_pelicula"] == peliculas["id"],
    "inner"
).withColumn(
    "ingreso", col("cantidad") * col("precio")
)

analisis_financiero = ventas_ingresos.groupBy("genero").agg(
    count("id_venta").alias("cantidad_ventas"),
    sum("cantidad").alias("unidades_vendidas"),
    round(sum("ingreso"), 2).alias("ingresos_totales"),
    round(avg("ingreso"), 2).alias("ingresos_promedio_por_venta")
).orderBy(
    col("ingresos_totales").desc()
)

analisis_financiero.show()
