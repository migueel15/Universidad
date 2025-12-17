import pandas as pd

# ejercicio 1
peliculas = pd.read_csv("peliculas.csv")
ventas = pd.read_csv("ventas.csv")

peliculas["duracion_min"] = peliculas["duracion_min"].fillna(
    peliculas["duracion_min"].mean()
)
peliculas["precio"] = peliculas["precio"].fillna(
    peliculas["precio"].mean()
)

ventas["metodo_pago"] = ventas["metodo_pago"].fillna("Tarjeta")
ventas["cantidad"] = ventas["cantidad"].fillna(
    ventas["cantidad"].mean()
)

print(peliculas)
print(ventas)


# ejercicio 2
accion_drama = peliculas[
    (peliculas["genero"].isin(["Acción", "Drama"])) &
    (peliculas["anyo"] > 2010)
][["titulo", "genero", "anyo"]]

print(accion_drama)

precio_duracion = peliculas[
    (peliculas["precio"] < 15) &
    (peliculas["duracion_min"] > 100)
][["titulo", "precio", "duracion_min"]]

print(precio_duracion)


# ejercicio 3
peliculas_ventas = peliculas.merge(
    ventas,
    left_on="id",
    right_on="id_pelicula",
    how="left"
)[["titulo", "genero", "id_venta", "cantidad", "fecha"]]

print(peliculas_ventas)

resumen_pelicula = peliculas_ventas.groupby("titulo").agg(
    total_unidades_vendidas=("cantidad", "sum"),
    numero_ventas=("id_venta", "count"),
    promedio_cantidad=("cantidad", "mean")
).reset_index()

print(resumen_pelicula)


# ejercicio 4
ventas_peliculas = peliculas.merge(
    ventas,
    left_on="id",
    right_on="id_pelicula",
    how="inner"
)

ventas_peliculas["ingreso"] = ventas_peliculas["cantidad"] * ventas_peliculas["precio"]

ingresos_genero = ventas_peliculas.groupby("genero").agg(
    total_ingresos=("ingreso", "sum"),
    numero_ventas=("id_venta", "count")
).reset_index()

idx = ventas_peliculas.groupby("genero")["precio"].idxmax()

pelicula_mas_cara = ventas_peliculas.loc[
    idx, ["genero", "titulo", "precio"]
].rename(columns={
    "titulo": "pelicula_mas_cara_vendida",
    "precio": "precio_pelicula_mas_cara"
})

ingresos_genero = ingresos_genero.merge(
    pelicula_mas_cara,
    on="genero",
    how="left"
).sort_values("total_ingresos", ascending=False)

print(ingresos_genero)
