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
from pyspark.sql.functions import (
    col,
    concat_ws,
    upper,
    substring,
    avg,
    max as spark_max,
    count,
    datediff,
    current_date,
)

# ejercicio 1
spark = SparkSession.builder.appName("Practica10Spark").getOrCreate()

schema = StructType(
    [
        StructField("id_empleado", IntegerType(), True),
        StructField("nombre", StringType(), True),
        StructField("apellido", StringType(), True),
        StructField("edad", IntegerType(), True),
        StructField("departamento", StringType(), True),
        StructField("puesto", StringType(), True),
        StructField("salario", DoubleType(), True),
        StructField("fecha_contratacion", DateType(), True),
        StructField("ciudad", StringType(), True),
        StructField("activo", BooleanType(), True),
    ]
)

df = spark.read.csv("empleados.csv", header=True, schema=schema)

# ejercicio 2
df.printSchema()
df.show(10)
df.describe().show()

# ejercicio 3
df.select("nombre", "apellido", "departamento").show()
df.filter(col("departamento") == "Ventas").select(
    "nombre", "apellido", "departamento"
).show()

# ejercicio 4
df = df.withColumn("nombre_completo", concat_ws(" ", col("nombre"), col("apellido")))
df = df.withColumn("bono", col("salario") * 0.05)
df = df.withColumnRenamed("puesto", "cargo")

# ejercicio 5
df.filter(col("salario") > 45000).select(
    "nombre", "apellido", "departamento", "salario"
).show()
df.filter(col("ciudad") == "Madrid").select(
    "nombre", "apellido", "ciudad", "cargo"
).show()
df.filter(col("activo") == True).select("nombre", "apellido", "activo").show()

# ejercicio 6
df.groupBy("departamento").agg(
    count("*").alias("num_empleados"),
    avg("salario").alias("salario_medio"),
    spark_max("salario").alias("salario_maximo"),
).show()

# ejercicio 7
df.orderBy(col("salario").desc(), col("nombre").asc()).show(10)

# ejercicio 8
df = df.withColumn("nombre_mayusculas", upper(col("nombre")))
df = df.withColumn(
    "email",
    concat_ws("@", concat_ws(".", col("nombre"), col("apellido")), col("departamento")),
)
df = df.withColumn("apellido_3_letras", substring(col("apellido"), 1, 3))

# ejercicio 9
df = df.withColumn(
    "antiguedad_dias", datediff(current_date(), col("fecha_contratacion"))
)

# ejercicio 10
df.createOrReplaceTempView("empleados")
spark.sql(
    """
    SELECT nombre, departamento, salario
    FROM empleados
    WHERE salario > 40000
    ORDER BY salario DESC
"""
).show()

# ejercicio 11
df.write.mode("overwrite").option("header", True).csv("empleados_ejercicio_8")
