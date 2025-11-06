from ProductManager import ProductManager
from DatabaseConnection import DatabaseConnection
from datetime import datetime
from consts import clientes, productos, ventas


if __name__ == "__main__":
    pm = ProductManager()
    pm.flushDatabase()  # Limpia toda la base de datos para evitar duplicados

    # tarea 1
    pm.loadData("clientes", clientes)
    pm.loadData("productos", productos)
    pm.loadData("ventas", ventas)

    # ejercicio 1
    print("\nTodos los productos")
    allProducts = pm.find("productos")
    print(allProducts)

    # ejercicio 2
    print("\nProductos activos")
    productosActivos = pm.find("productos", {"activo": True})
    print(productosActivos)

    # ejercicio 3
    print("\nProductos categoria computadora")
    computadoras = pm.find("productos", {"categoria": "computadoras"})
    print(computadoras)

    # ejercicio 4
    print("\nProductos premium")
    productosPremium = pm.find("productos", {"precio": {"$gt": 500}})
    print(productosPremium)

    # ejercicio 5
    print("\nClientes premium")
    clientesPremium = pm.find("clientes", {"premium": True})
    print(clientesPremium)

    # ejercicio 6
    print("\nActualizar precio Auriculares")
    numActualizados = pm.update(
        "productos", {"nombre": "Auriculares Bluetooth"}, {"precio": 249}
    )
    print(numActualizados)

    # ejercicio 7
    print("\nActivar inactivos")
    numActualizados = pm.update(
        "productos", {"activo": False}, {"activo": True, "stock": 10}
    )
    print(numActualizados)

    # ejercicio 8
    print("\nActivar descuento Apple")
    numActualizados = pm.update("productos", {"marca": "Apple"}, {"descuento": 10})
    print(numActualizados)

    # ejercicio 9
    print("\nAumentar stock en 5")
    numActualizados = pm.update(
        "productos", {"stock": {"$lt": 10}}, {"stock": 5}, type="inc"
    )
    print(numActualizados)

    # ejercicio 10
    pm.createIndex("productos", "nombre", name="nombre")

    # ejercicio 11
    pm.createIndex("productos", [("categoria", 1), ("precio", -1)], name="compuesto")

    # ejercicio 12
    pm.createIndex("clientes", "email", unique=True, name="email")

    # ejercicio 13
    print(pm.db["productos"].index_information())

    # ejercicio 14
    print("\nAnalisis distribucion productos")
    pipeline = [
        {
            "$group": {
                "_id": "$categoria",
                "cantidad_total": {"$sum": 1},
                "precio_promedio": {"$avg": "$precio"},
                "stock_total": {"$sum": "$stock"},
                "precio_maximo": {"$max": "$precio"},
                "productos": {"$push": "$$ROOT"},
            }
        },
        {"$sort": {"caontidad_productos": -1}},
    ]
    datos = pm.pipeline("productos", pipeline)
    for d in datos:
        print(d)

    # ejercicio 15
    print("\nAnalisis ventas productos")
    pipeline = [
        {
            "$lookup": {
                "from": "productos",
                "localField": "producto_id",
                "foreignField": "_id",
                "as": "producto_info",
            }
        },
        {"$unwind": "$producto_info"},
        {
            "$project": {
                "_id": 0,
                "cliente": "$cliente_email",
                "producto": "$producto_info.nombre",
                "categoria": "$producto_info.categoria",
                "marca": "$producto_info.marca",
                "cantidad": 1,
                "total": 1,
                "ciudad": 1,
            }
        },
    ]
    datos = pm.pipeline("ventas", pipeline)
    for d in datos:
        print(d)

    # ejercicio 16
    print("\nAnalisis desempeño comercial por ubicacion")
    pipeline = [
        {
            "$group": {
                "_id": "$ciudad",
                "total_ventas": {
                    "$sum": "$total",
                },
                "cantidad_total_transacciones": {"$sum": "$cantidad"},
                "promedio_venta": {"$avg": "$venta"},
            }
        },
        {"$sort": {"total_ventas": -1}},
    ]
    datos = pm.pipeline("ventas", pipeline)
    for d in datos:
        print(d)

    # ejercicio 17
    newProduct = {
        "_id": 10,
        "nombre": "Mi producto",
        "categoria": "universidad",
        "precio": 23.59,
        "stock": 10,
        "marca": "Sony",
        "tags": ["tecnologia"],
        "fecha_ingreso": datetime(2024, 3, 1),
        "activo": True,
    }
    pm.db["productos"].insert_one(newProduct)
    print(f"Mi producto: {pm.find("productos",{"_id":10})}")
    pm.db["productos"].delete_one({"nombre": "Mi producto"})
    print(f"Mi producto: {pm.find("productos",{"_id":10})}")

    DatabaseConnection().disconnect()
