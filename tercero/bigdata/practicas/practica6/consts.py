from datetime import datetime

clientes = [
    {
        "nombre": "Ana García",
        "email": "ana@techworld.com",
        "ciudad": "Madrid",
        "premium": True,
        "fecha_registro": datetime(2023, 12, 1),
    },
    {
        "nombre": "Carlos López",
        "email": "carlos@techworld.com",
        "ciudad": "Barcelona",
        "premium": False,
        "fecha_registro": datetime(2024, 1, 15),
    },
    {
        "nombre": "María Rodríguez",
        "email": "maria@techworld.com",
        "ciudad": "Madrid",
        "premium": True,
        "fecha_registro": datetime(2023, 11, 20),
    },
]

productos = [
    {
        "_id": 1,
        "nombre": "Laptop Gaming Pro",
        "categoria": "computadoras",
        "precio": 1500,
        "stock": 8,
        "marca": "ASUS",
        "tags": ["gaming", "portatil", "rendimiento"],
        "fecha_ingreso": datetime(2024, 1, 10),
        "activo": True,
    },
    {
        "_id": 2,
        "nombre": "Smartphone Galaxy",
        "categoria": "moviles",
        "precio": 799,
        "stock": 25,
        "marca": "Samsung",
        "tags": ["android", "5G", "camara"],
        "fecha_ingreso": datetime(2024, 2, 15),
        "activo": True,
    },
    {
        "_id": 3,
        "nombre": "Tablet iPad",
        "categoria": "tablets",
        "precio": 599,
        "stock": 15,
        "marca": "Apple",
        "tags": ["apple", "creatividad", "portatil"],
        "fecha_ingreso": datetime(2024, 1, 25),
        "activo": True,
    },
    {
        "_id": 4,
        "nombre": "Auriculares Bluetooth",
        "categoria": "audio",
        "precio": 299,
        "stock": 0,
        "marca": "Sony",
        "tags": ["audio", "inalambrico", "calidad"],
        "fecha_ingreso": datetime(2024, 3, 1),
        "activo": False,
    },
]

ventas = [
    {
        "producto_id": 1,
        "cliente_email": "ana@techworld.com",
        "cantidad": 1,
        "total": 1500,
        "fecha": datetime(2024, 3, 15),
        "ciudad": "Madrid",
    },
    {
        "producto_id": 2,
        "cliente_email": "carlos@techworld.com",
        "cantidad": 2,
        "total": 1598,
        "fecha": datetime(2024, 3, 16),
        "ciudad": "Barcelona",
    },
    {
        "producto_id": 3,
        "cliente_email": "maria@techworld.com",
        "cantidad": 1,
        "total": 599,
        "fecha": datetime(2024, 3, 17),
        "ciudad": "Madrid",
    },
]
