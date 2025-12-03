import pandas as pd
import numpy as np

# EJERCICIO 1
datos = {
    "Producto": ["Laptop", "Mouse", "Teclado", "Monitor", "Tablet"],
    "Precio": [1200, 25, 80, 300, 450],
    "Stock": [15, 100, 50, 30, 25],
    "Categoria": [
        "Electrónica",
        "Accesorio",
        "Accesorio",
        "Electrónica",
        "Electrónica",
    ],
}
df1 = pd.DataFrame(datos)

# EJERCICIO 2
datos = [
    [1, "Laptop", 1200, 15],
    [2, "Mouse", 25, 100],
    [3, "Teclado", 80, 50],
    [4, "Monitor", 300, 30],
    [5, "Tablet", 450, 25],
]
df2 = pd.DataFrame(datos)

# EJERCICIO 3
print(df1.head(3))
print(df2.head(3))

# EJERCICIO 4
print(df1.shape)
print(df1.columns)
print(df1.dtypes)
print(df1.describe())
print(df1.sample(2))

# EJERCICIO 5
print(df1["Producto"])
print(df1[["Producto", "Precio"]])
print(df1.iloc[0])
print(df1.loc[0:1])

# EJERCICIO 6
np.random.seed(42)
datos_ventas = {
    "Fecha": pd.date_range("2024-01-01", periods=100, freq="D"),
    "Producto": np.random.choice(
        ["Laptop", "Mouse", "Teclado", "Monitor", "Tablet"], 100
    ),
    "Cantidad": np.random.randint(1, 10, 100),
    "Precio_Unitario": np.random.choice([1200, 25, 80, 300, 450], 100),
    "Region": np.random.choice(["Norte", "Sur", "Este", "Oeste"], 100),
    "Vendedor": np.random.choice(["Ana", "Carlos", "Maria", "Pedro"], 100),
}
df_ventas = pd.DataFrame(datos_ventas)
df_ventas["Venta_Total"] = df_ventas["Cantidad"] * df_ventas["Precio_Unitario"]

print(df_ventas[df_ventas["Producto"] == "Laptop"])
print(df_ventas[df_ventas["Venta_Total"] > 1000])
print(df_ventas[(df_ventas["Vendedor"] == "Maria") & (df_ventas["Region"] == "Norte")])

# EJERCICIO 7
np.random.seed(42)
datos_ventas = {
    "Fecha": pd.date_range("2024-01-01", periods=100, freq="D"),
    "Producto": np.random.choice(["Laptop", "Mouse", "Teclado", np.nan, "Tablet"], 100),
    "Cantidad": np.random.randint(1, 10, 100),
    "Precio_Unitario": np.random.choice([1200, 25, np.nan, 300, 450], 100),
    "Region": np.random.choice(["Norte", np.nan, "Este", "Oeste"], 100),
    "Vendedor": np.random.choice(["Ana", "Carlos", "Maria", "Pedro"], 100),
}
df_ventas_copy = pd.DataFrame(datos_ventas)
df_ventas_copy = df_ventas_copy.replace("nan", np.nan)
df_ventas_copy["Venta_Total"] = (
    df_ventas_copy["Cantidad"] * df_ventas_copy["Precio_Unitario"]
)

print(df_ventas_copy.isnull().sum())

df_limpio_ventas = df_ventas_copy.dropna()
print(df_ventas_copy.shape[0])
print(df_limpio_ventas.shape[0])

defaultRegion = "Desconocido"
defaultProduct = "Desconocido"
mediaPrecio = df_limpio_ventas["Precio_Unitario"].mean()
df_ventas_copy["Precio_Unitario"] = df_ventas_copy["Precio_Unitario"].fillna(
    mediaPrecio
)
df_ventas_copy["Region"] = df_ventas_copy["Region"].fillna(defaultRegion)
df_ventas_copy["Producto"] = df_ventas_copy["Producto"].fillna(defaultProduct)
df_ventas_copy["Venta_Total"] = (
    df_ventas_copy["Cantidad"] * df_ventas_copy["Precio_Unitario"]
)

print(df_ventas_copy.info())

df_ordenado = df_ventas_copy.sort_values(by="Venta_Total", ascending=False)
print(df_ordenado)

# EJERCICIO 8
df = df_ventas.copy()
print(df["Venta_Total"].agg(["sum", "mean", "max", "count"]))
print(df.groupby("Producto")["Venta_Total"].sum())
print(df.groupby("Region")["Venta_Total"].agg(["sum", "mean", "max", "count"]))
print(df.groupby("Vendedor")["Venta_Total"].sum().sort_values(ascending=False).head(3))

# EJERCICIO 9
df = df_ventas.copy()
df["Venta_Con_IVA"] = df["Venta_Total"] * 1.21
df["Categoria_Precio"] = np.where(df["Precio_Unitario"] > 500, "Alto", "Bajo")

print(
    df[
        [
            "Producto",
            "Precio_Unitario",
            "Categoria_Precio",
            "Venta_Total",
            "Venta_Con_IVA",
        ]
    ].head(10)
)


def clasificar_venta(valor):
    if valor < 500:
        return "Pequeña"
    elif valor < 2000:
        return "Mediana"
    else:
        return "Grande"


df["Tipo_Venta"] = df["Venta_Total"].apply(clasificar_venta)
print(df)

# EJERCICIO 10
info_producto = {
    "Producto": ["Laptop", "Mouse", "Teclado", "Monitor", "Tablet", "Auriculares"],
    "Categoria": [
        "Electrónica",
        "Accesorio",
        "Accesorio",
        "Electrónica",
        "Electrónica",
        "Accesorio",
    ],
    "Costo": [800, 15, 40, 200, 300, 20],
}

info_vendedores = {
    "Vendedor": ["Ana", "Carlos", "Maria", "Pedro", "Laura"],
    "Departamento": ["Ventas", "Tecnología", "Ventas", "Marketing", "Ventas"],
    "Salario_Base": [30000, 35000, 32000, 28000, 31000],
}

df_info_productos = pd.DataFrame(info_producto)
df_info_vendedores = pd.DataFrame(info_vendedores)

df_inner = pd.merge(df_ventas, df_info_productos, on="Producto", how="inner")
print(df_inner[["Producto", "Venta_Total", "Categoria", "Costo"]].head())
df_left = pd.merge(df_ventas, df_info_vendedores, on="Vendedor", how="left")
print(df_left[["Vendedor", "Venta_Total", "Departamento", "Salario_Base"]].head())
nueva_venta = {
    "Fecha": pd.to_datetime(["2024-04-01"]),
    "Producto": ["Auriculares"],
    "Cantidad": [2],
    "Precio_Unitario": [50],
    "Region": ["Norte"],
    "Vendedor": ["Laura"],
    "Venta_Total": [100],
}
df_nueva = pd.DataFrame(nueva_venta)

df_ventas_final = pd.concat([df_ventas, df_nueva], ignore_index=True)

print(f"Filas antes: {len(df_ventas)}, Filas después: {len(df_ventas_final)}")
print(df_ventas_final.tail(3))

# EJERCICIO 11
df_ventas_final["Fecha"] = pd.to_datetime(df_ventas_final["Fecha"])
fecha_inicio = "2024-01-15"
fecha_fin = "2024-01-31"
mask_fechas = (df_ventas_final["Fecha"] >= fecha_inicio) & (
    df_ventas_final["Fecha"] <= fecha_fin
)
ventas_enero = df_ventas_final.loc[mask_fechas]
print(ventas_enero)
