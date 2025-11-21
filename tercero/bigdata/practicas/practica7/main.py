from pymongo import MongoClient
import json
import re
from datetime import datetime

client = MongoClient("mongodb://admin:admin@localhost:27017/?authSource=admin")
db = client["Biblioteca"]

# limpio las 4 colecciones
for col in ["libros", "estudiantes", "categorias", "prestamos"]:
    db[col].delete_many({})

categorias = [
    {
        "_id": 1,
        "nombre": "Ciencia Ficción",
        "descripcion": "Novelas de ciencia ficción y fantasía",
        "libros_count": 0,
        "ubicacion": "Ala A",
    },
    {
        "_id": 2,
        "nombre": "Literatura Clásica",
        "descripcion": "Obras clásicas de la literatura universal",
        "libros_count": 0,
        "ubicacion": "Ala B",
    },
    {
        "_id": 3,
        "nombre": "Tecnología",
        "descripcion": "Libros de programación y tecnología",
        "libros_count": 0,
        "ubicacion": "Ala C",
    },
    {
        "_id": 4,
        "nombre": "Ciencias Sociales",
        "descripcion": "Sociología, psicología y antropología",
        "libros_count": 0,
        "ubicacion": "Ala D",
    },
]

libros = [
    {
        "titulo": "1984",
        "autor": "George Orwell",
        "genero": "Ciencia Ficción",
        "año_publicacion": 1949,
        "isbn": "978-0451524935",
        "editorial": "Signet Classics",
        "paginas": 328,
        "precio": 12.99,
        "stock": 5,
        "disponible": True,
        "tags": ["distopía", "política", "clásico"],
        "fecha_ingreso": datetime(2023, 1, 15),
        "categoria_id": 1,
    },
    {
        "titulo": "Cien años de soledad",
        "autor": "Gabriel García Márquez",
        "genero": "Realismo Mágico",
        "año_publicacion": 1967,
        "isbn": "978-0307474728",
        "editorial": "Penguin Random House",
        "paginas": 417,
        "precio": 15.50,
        "stock": 3,
        "disponible": True,
        "tags": ["realismo mágico", "latinoamericano", "clásico"],
        "fecha_ingreso": datetime(2023, 2, 20),
        "categoria_id": 2,
    },
    {
        "titulo": "Python Crash Course",
        "autor": "Eric Matthes",
        "genero": "Programación",
        "año_publicacion": 2019,
        "isbn": "978-1593279288",
        "editorial": "No Starch Press",
        "paginas": 544,
        "precio": 39.99,
        "stock": 8,
        "disponible": True,
        "tags": ["python", "programación", "aprendizaje"],
        "fecha_ingreso": datetime(2023, 3, 10),
        "categoria_id": 3,
    },
    {
        "titulo": "El Principito",
        "autor": "Antoine de Saint-Exupéry",
        "genero": "Literatura Infantil",
        "año_publicacion": 1943,
        "isbn": "978-0156012195",
        "editorial": "Harcourt Brace",
        "paginas": 96,
        "precio": 9.99,
        "stock": 0,
        "disponible": False,
        "tags": ["infantil", "filosofía", "clásico"],
        "fecha_ingreso": datetime(2023, 1, 5),
        "categoria_id": 2,
    },
    {
        "titulo": "Clean Code",
        "autor": "Robert C. Martin",
        "genero": "Programación",
        "año_publicacion": 2008,
        "isbn": "978-0132350884",
        "editorial": "Prentice Hall",
        "paginas": 464,
        "precio": 47.99,
        "stock": 6,
        "disponible": True,
        "tags": ["programación", "calidad", "best practices"],
        "fecha_ingreso": datetime(2023, 4, 15),
        "categoria_id": 3,
    },
]


estudiantes = [
    {
        "nombre": "Ana García López",
        "email": "ana.garcia@universidad.edu",
        "carrera": "Ingeniería Informática",
        "semestre": 5,
        "edad": 21,
        "ciudad": "Madrid",
        "fecha_registro": datetime(2022, 9, 1),
        "activo": True,
    },
    {
        "nombre": "Carlos Rodríguez Martín",
        "email": "carlos.rodriguez@universidad.edu",
        "carrera": "Literatura",
        "semestre": 3,
        "edad": 20,
        "ciudad": "Barcelona",
        "fecha_registro": datetime(2023, 1, 15),
        "activo": True,
    },
    {
        "nombre": "María Chen Wang",
        "email": "maria.chen@universidad.edu",
        "carrera": "Psicología",
        "semestre": 7,
        "edad": 23,
        "ciudad": "Valencia",
        "fecha_registro": datetime(2021, 9, 1),
        "activo": True,
    },
    {
        "nombre": "David Martínez Ruiz",
        "email": "david.martinez@universidad.edu",
        "carrera": "Ingeniería Informática",
        "semestre": 6,
        "edad": 22,
        "ciudad": "Madrid",
        "fecha_registro": datetime(2022, 2, 1),
        "activo": False,
    },
]

db.categorias.insert_many(categorias)
res_lib = db.libros.insert_many(libros)
res_est = db.estudiantes.insert_many(estudiantes)

id_1984 = db.libros.find_one({"titulo": "1984"})["_id"]
id_python = db.libros.find_one({"titulo": "Python Crash Course"})["_id"]

id_ana = db.estudiantes.find_one({"nombre": "Ana García López"})["_id"]
id_carlos = db.estudiantes.find_one({"nombre": "Carlos Rodríguez Martín"})["_id"]

db.prestamos.insert_many(
    [
        {
            "libro_id": id_1984,
            "estudiante_id": id_ana,
            "fecha_prestamo": datetime(2024, 1, 10),
            "fecha_devolucion": datetime(2024, 1, 24),
            "fecha_devolucion_real": None,
            "estado": "activo",
            "multa": 0,
        },
        {
            "libro_id": id_python,
            "estudiante_id": id_carlos,
            "fecha_prestamo": datetime(2024, 1, 5),
            "fecha_devolucion": datetime(2024, 1, 19),
            "fecha_devolucion_real": datetime(2024, 1, 18),
            "estado": "devuelto",
            "multa": 0,
        },
    ]
)

# EJERCICIO 1
print("Ejercicio 1")
db.libros.update_one({"titulo": "1984"}, {"$set": {"stock": 3}})
print(list(db.libros.find({"titulo": "1984"})))
print("\n")

# EJERCICIO 2
print("Ejercicio 2")
db.libros.update_many({"año_publicacion": {"$lt": 2000}}, {"$set": {"descuento": 0.15}})
print(list(db.libros.find({"año_publicacion": {"$lt": 2000}})))
print("\n")

# EJERCICIO 3
print("Ejercicio 3")
db.categorias.update_one(
    {"nombre": "Matemáticas"},
    {
        "$set": {
            "descripcion": "Libros de matemáticas y cálculo",
            "ubicacion": "Ala E",
            "libros_count": 0,
        }
    },
    upsert=True,
)
print(list(db.categorias.find({"nombre": "Matemáticas"})))
print("\n")

# EJERCICIO 4
print("Ejercicio 4")
print(list(db.libros.find({"stock": {"$gt": 0}})))
print("\n")

# EJERCICIO 5
print("Ejercicio 5")
print(list(db.estudiantes.find({"carrera": "Ingeniería Informática"})))
print("\n")

# EJERCICIO 6
print("Ejercicio 6")
print(list(db.libros.find({"precio": {"$gt": 20}})))
print("\n")

# EJERCICIO 7
print("Ejercicio 7")
print(
    list(
        db.libros.aggregate(
            [
                {"$match": {"stock": {"$gt": 0}}},
                {
                    "$group": {
                        "_id": "$genero",
                        "total_libros": {"$sum": 1},
                        "stock_total": {"$sum": "$stock"},
                        "precio_promedio": {"$avg": "$precio"},
                        "libros": {"$push": "$titulo"},
                    }
                },
                {"$sort": {"total_libros": -1}},
            ]
        )
    )
)

# EJERCICIO 8
print("Ejercicio 8")
print(
    list(
        db.libros.aggregate(
            [
                {
                    "$bucket": {
                        "groupBy": "$precio",
                        "boundaries": [0, 10, 20, 30, 50],
                        "default": "50+",
                        "output": {
                            "total": {"$sum": 1},
                            "precio_promedio": {"$avg": "$precio"},
                        },
                    }
                }
            ]
        )
    )
)
print("\n")

# EJERCICIO 9
print("Ejercicio 9")
print(
    list(
        db.prestamos.aggregate(
            [
                {
                    "$lookup": {
                        "from": "libros",
                        "localField": "libro_id",
                        "foreignField": "_id",
                        "as": "libro",
                    }
                },
                {"$unwind": "$libro"},
                {
                    "$lookup": {
                        "from": "estudiantes",
                        "localField": "estudiante_id",
                        "foreignField": "_id",
                        "as": "estudiante",
                    }
                },
                {"$unwind": "$estudiante"},
                {
                    "$project": {
                        "_id": 0,
                        "estudiante": "$estudiante.nombre",
                        "carrera": "$estudiante.carrera",
                        "libro": "$libro.titulo",
                        "fecha_prestamo": 1,
                        "fecha_devolucion": 1,
                        "estado": 1,
                    }
                },
            ]
        )
    )
)
print("\n")

# EJERCICIO 10
print("Ejercicio 10")
print(
    list(
        db.estudiantes.aggregate(
            [
                {"$match": {"activo": True}},
                {
                    "$group": {
                        "_id": "$ciudad",
                        "total_estudiantes": {"$sum": 1},
                        "promedio_edad": {"$avg": "$edad"},
                        "carreras_unicas": {"$addToSet": "$carrera"},
                        "nombres": {"$push": "$nombre"},
                    }
                },
                {
                    "$project": {
                        "_id": 0,
                        "ciudad": "$_id",
                        "total_estudiantes": 1,
                        "promedio_edad": {"$round": ["$promedio_edad", 0]},
                        "carreras_unicas": 1,
                        "nombres": 1,
                    }
                },
            ]
        )
    )
)
print("\n")

# EJERCICIO 11
print("Ejercicio 11")
temp = db.estudiantes.insert_one({"nombre": "Temporal", "activo": False})
db.estudiantes.delete_one({"_id": temp.inserted_id})
print("Estudiante temporal creado y eliminado")
print("\n")

# EJERCICIO 12
print("Ejercicio 12")
db.libros.update_many(
    {"genero": "Programación"},
    {"$set": {"ubicacion": "Sala de ordenadores", "etiqueta_especial": "Tecnología"}},
)
print(list(db.libros.find({"genero": "Programación"})))
print("\n")
