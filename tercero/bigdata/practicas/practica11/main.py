from pyspark.sql import SparkSession
from pyspark.sql.types import (
    StructType,
    StructField,
    IntegerType,
    StringType,
    DoubleType,
    BooleanType,
    DateType,
)
from pyspark.sql.functions import col, when, count, sum as spark_sum, avg

# ejercicio 1
spark = SparkSession.builder.appName("Practica11Spark").getOrCreate()

schema_clientes = StructType(
    [
        StructField("cliente_id", IntegerType(), True),
        StructField("nombre", StringType(), True),
        StructField("ciudad", StringType(), True),
        StructField("pais", StringType(), True),
        StructField("fecha_registro", DateType(), True),
        StructField("vip", BooleanType(), True),
        StructField("categoria_cliente", StringType(), True),
    ]
)

schema_ventas = StructType(
    [
        StructField("venta_id", IntegerType(), True),
        StructField("fecha", DateType(), True),
        StructField("cliente_id", IntegerType(), True),
        StructField("producto", StringType(), True),
        StructField("categoria", StringType(), True),
        StructField("cantidad", IntegerType(), True),
        StructField("precio", DoubleType(), True),
        StructField("region", StringType(), True),
        StructField("descuento", DoubleType(), True),
        StructField("vendedor_id", IntegerType(), True),
        StructField("total", DoubleType(), True),
    ]
)

clientes = spark.read.csv("clientes.csv", header=True, schema=schema_clientes)
ventas = spark.read.csv("ventas.csv", header=True, schema=schema_ventas)

clientes.printSchema()
ventas.printSchema()

clientes.show(5)
ventas.show(5)

clientes.count()
ventas.count()

# ejercicio 2
ventas_sel = ventas.select("venta_id", "producto", "cantidad", "precio", "total")
ventas_iva = ventas_sel.withColumn("precio_con_iva", col("total") * 1.21)
ventas_ren = ventas_iva.withColumnRenamed(
    "precio", "precio_unitario"
).withColumnRenamed("total", "precio_final")
ventas_clas = ventas_ren.withColumn(
    "clasificacion",
    when(col("precio_final") > 800, "Grande")
    .when((col("precio_final") >= 300) & (col("precio_final") <= 800), "Mediana")
    .otherwise("Pequeña"),
)

# ejercicio 3
filtro1 = ventas.filter(col("total") > 500)
filtro1.show()
filtro1.count()

filtro2 = ventas.filter(
    (col("categoria") == "Electrónicos") | (col("categoria") == "Computación")
)
filtro2.show()
filtro2.count()

filtro3 = ventas.filter((col("cantidad") > 2) & (col("descuento") > 0.1))
filtro3.show()
filtro3.count()

# ejercicio 4
ventas.groupBy("categoria").agg(
    count("*").alias("num_ventas"),
    spark_sum("total").alias("total_vendido"),
    avg("total").alias("promedio_venta"),
).show()

ventas.groupBy("region").agg(
    count("*").alias("num_ventas"),
    spark_sum("total").alias("total_vendido"),
    avg("descuento").alias("descuento_medio"),
).show()

ventas.groupBy("producto").agg(
    spark_sum("cantidad").alias("unidades_vendidas"),
    spark_sum("total").alias("total_vendido"),
).orderBy(col("unidades_vendidas").desc()).show(3)

# ejercicio 5
ventas.select(
    count(col("precio").isNull().cast("int")).alias("precio_na"),
    count(col("descuento").isNull().cast("int")).alias("descuento_na"),
    count(col("cantidad").isNull().cast("int")).alias("cantidad_na"),
    count(col("region").isNull().cast("int")).alias("region_na"),
).show()

ventas_limpio = ventas.fillna(
    {"precio": 500.0, "descuento": 0.0, "cantidad": 2, "region": "Desconocida"}
)

# ejercicio 6
join_df = ventas_limpio.join(clientes, "cliente_id", "left").select(
    "venta_id", "producto", "total", "nombre", "ciudad"
)

join_df.show()

join_df.groupBy("ciudad").agg(spark_sum("total").alias("total_vendido")).orderBy(
    col("total_vendido").desc()
).show()

# ejercicio 7
ventas_limpio.createOrReplaceTempView("ventas")
clientes.createOrReplaceTempView("clientes")

spark.sql("SELECT * FROM ventas LIMIT 10").show()

spark.sql(
    """
    SELECT categoria, COUNT(*) AS num_ventas, SUM(total) AS total_vendido
    FROM ventas
    GROUP BY categoria
"""
).show()

spark.sql(
    """
    SELECT v.venta_id, v.producto, v.total, c.nombre, c.ciudad
    FROM ventas v
    JOIN clientes c ON v.cliente_id = c.cliente_id
"""
).show()

spark.sql(
    """
    SELECT
        COUNT(*) AS total_ventas,
        SUM(total) AS ingresos,
        AVG(total) AS promedio,
        SUM(cantidad) AS unidades_vendidas
    FROM ventas
"""
).show()
