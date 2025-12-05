import numpy as np
import pandas as pd


def main():
    clientes = pd.read_csv(
        "datos/clientes.csv",
        parse_dates=["fecha_registro"],
    )
    pedidos = pd.read_csv(
        "datos/pedidos.csv",
        parse_dates=["fecha_pedido"],
    )

    # EJERCICIO 1
    print(clientes.head())
    print(pedidos.head())
    print(clientes.dtypes)
    print(clientes.shape)
    print(pedidos.dtypes)
    print(pedidos.shape)
    print(clientes.describe(include="all"))
    print(pedidos.describe(include="all"))

    # EJERCICIO 2
    print(clientes[(clientes["ciudad"] == "Madrid") & (clientes["edad"] > 30)])
    print(
        clientes[
            (clientes["categoria"] == "Premium") | (clientes["puntos_fidelidad"] > 3000)
        ]
    )
    print(pedidos[pedidos["total"] > 500])
    print(
        pedidos[
            (pedidos["estado"] == "Pendiente")
            & (pedidos["fecha_pedido"].dt.year == 2023)
        ]
    )

    # EJERCICIO 3
    print(clientes.groupby("ciudad").size())
    print(pedidos.groupby("estado")["total"].sum())
    print(clientes.groupby("categoria")["edad"].mean())
    pedidos_ciudad = pedidos.merge(
        clientes[["cliente_id", "ciudad"]], on="cliente_id", how="left"
    )
    pedidos_ciudad = pedidos_ciudad.dropna(subset=["ciudad"])
    top_ciudades = (
        pedidos_ciudad.groupby("ciudad")["pedido_id"]
        .count()
        .sort_values(ascending=False)
        .head(3)
    )
    print(top_ciudades)

    # EJERCICIO 4
    pedidos_con_nombre = pedidos.merge(
        clientes[["cliente_id", "nombre"]], on="cliente_id", how="left"
    )
    print(pedidos_con_nombre[["nombre", "pedido_id", "producto", "cantidad", "total"]])
    print(
        clientes.merge(
            pedidos,
            on="cliente_id",
            how="left",
        )
    )
    pedidos_sin_cliente = pedidos.merge(
        clientes[["cliente_id"]],
        on="cliente_id",
        how="left",
        indicator=True,
    )
    print(pedidos_sin_cliente[pedidos_sin_cliente["_merge"] == "left_only"])
    clientes_sin_pedido = clientes.merge(
        pedidos[["cliente_id"]],
        on="cliente_id",
        how="left",
        indicator=True,
    )
    print(clientes_sin_pedido[clientes_sin_pedido["_merge"] == "left_only"])

    # EJERCICIO 5
    print(clientes.isnull().sum()[clientes.isnull().sum() > 0])
    print(pedidos.isnull().sum()[pedidos.isnull().sum() > 0])
    clientes["edad"] = clientes["edad"].fillna(clientes["edad"].mean())
    print(clientes["edad"])
    clientes["puntos_fidelidad"] = clientes["puntos_fidelidad"].fillna(0)
    print(clientes["puntos_fidelidad"])
    pedidos = pedidos[pedidos["cantidad"].notna()]
    print(pedidos)

    # EJERCICIO 6
    clientes["rango_edad"] = np.select(
        [
            clientes["edad"] < 30,
            (clientes["edad"] >= 30) & (clientes["edad"] <= 60),
            clientes["edad"] > 60,
        ],
        ["Joven", "Adulto", "Senior"],
        default="Desconocido",
    )
    print(clientes[["cliente_id", "edad", "rango_edad"]])
    pedidos["año_mes_pedido"] = pedidos["fecha_pedido"].dt.to_period("M").astype(str)
    print(pedidos[["pedido_id", "fecha_pedido", "año_mes_pedido"]])

    def clasificar_producto(nombre):
        nombre_min = str(nombre).lower()
        if any(
            k in nombre_min for k in ["laptop", "smartphone", "tablet", "smartwatch"]
        ):
            return "Dispositivo"
        if any(
            k in nombre_min for k in ["monitor", "teclado", "ratón", "raton", "web"]
        ):
            return "Periféricos"
        if any(k in nombre_min for k in ["auriculares", "altavoz"]):
            return "Audio"
        return "Otros"

    pedidos["tipo_producto"] = pedidos["producto"].apply(clasificar_producto)
    print(pedidos[["pedido_id", "producto", "tipo_producto"]])
    clientes["cliente_activo"] = np.where(
        clientes["cliente_id"].isin(pedidos["cliente_id"].dropna().unique()),
        "sí",
        "no",
    )
    print(clientes[["cliente_id", "cliente_activo"]])

    # EJERCICIO 7
    pedidos_clientes = pedidos.merge(
        clientes[["cliente_id", "ciudad", "nombre", "categoria"]],
        on="cliente_id",
        how="left",
    )
    print(
        pedidos_clientes[
            (pedidos_clientes["ciudad"] == "Barcelona")
            & (pedidos_clientes["total"] > 1000)
        ]
    )
    ventas_por_ciudad = (
        pedidos_clientes.dropna(subset=["ciudad"])
        .groupby(["ciudad", "producto"])["cantidad"]
        .sum()
        .reset_index()
    )
    top_productos_ciudad = ventas_por_ciudad.sort_values(
        ["ciudad", "cantidad"], ascending=[True, False]
    ).drop_duplicates(subset=["ciudad"], keep="first")
    print(top_productos_ciudad)
    premium_gasto = (
        pedidos_clientes[pedidos_clientes["categoria"] == "Premium"]
        .groupby(["cliente_id", "nombre"])["total"]
        .sum()
        .reset_index()
        .sort_values("total", ascending=False)
    )
    print(premium_gasto)
    ventas_2023 = (
        pedidos[pedidos["fecha_pedido"].dt.year == 2023]
        .groupby(pd.Grouper(key="fecha_pedido", freq="ME"))["total"]
        .sum()
    )
    print(ventas_2023)
    print(pedidos.groupby("cliente_id")["total"].agg(["mean", "count"]))
    rendimiento = (
        pedidos_clientes.dropna(subset=["categoria"])
        .groupby(["categoria", "producto"])
        .agg(total_ventas=("total", "sum"), cantidad_ventas=("cantidad", "sum"))
        .reset_index()
    )
    top_rendimiento = (
        rendimiento.sort_values(
            ["categoria", "total_ventas", "cantidad_ventas"],
            ascending=[True, False, False],
        )
        .groupby("categoria")
        .head(2)
    )
    print(top_rendimiento)


if __name__ == "__main__":
    main()
