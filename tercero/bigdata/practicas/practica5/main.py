from pymongo import MongoClient, ASCENDING, DESCENDING
from datetime import datetime

client = MongoClient('localhost', 27017)

db = client['academia']

db.estudiantes.drop()
db.cursos.drop()
db.calificaciones.drop()

print("INSERTANDO DATOS EN MONGODB")
estudiantes = [
    {
        "_id": 1,
        "nombre": "Laura Martínez",
        "edad": 22,
        "email": "laura@email.com",
        "ciudad": "Madrid",
        "curso_id": 1
    },
    {
        "_id": 2,
        "nombre": "David Chen",
        "edad": 25,
        "email": "david@email.com",
        "ciudad": "Barcelona",
        "curso_id": 2
    },
    {
        "_id": 3,
        "nombre": "Sofía Pérez",
        "edad": 20,
        "email": "sofia@email.com",
        "ciudad": "Madrid",
        "curso_id": 1
    },
    {
        "_id": 4,
        "nombre": "Javier Ruiz",
        "edad": 28,
        "email": "javier@email.com",
        "ciudad": "Valencia",
        "curso_id": 3
    }
]

resultado_estudiantes = db.estudiantes.insert_many(estudiantes)
print(f"\nInsertados {len(resultado_estudiantes.inserted_ids)} estudiantes")
print(f"IDs insertados: {resultado_estudiantes.inserted_ids}")

cursos = [
    {
        "_id": 1,
        "nombre": "Python Básico",
        "profesor": "Ana García",
        "duracion_horas": 40,
        "nivel": "Principiante"
    },
    {
        "_id": 2,
        "nombre": "Web Development",
        "profesor": "Carlos López",
        "duracion_horas": 60,
        "nivel": "Intermedio"
    },
    {
        "_id": 3,
        "nombre": "Data Science",
        "profesor": "María Rodríguez",
        "duracion_horas": 80,
        "nivel": "Avanzado"
    }
]

resultado_cursos = db.cursos.insert_many(cursos)
print(f"\nInsertados {len(resultado_cursos.inserted_ids)} cursos")
print(f"IDs insertados: {resultado_cursos.inserted_ids}")

calificaciones = [
    {"estudiante_id": 1, "curso_id": 1, "calificacion": 8.5},
    {"estudiante_id": 2, "curso_id": 2, "calificacion": 9.0},
    {"estudiante_id": 3, "curso_id": 1, "calificacion": 7.5},
    {"estudiante_id": 4, "curso_id": 3, "calificacion": 8.0}
]

resultado_calificaciones = db.calificaciones.insert_many(calificaciones)
print(f"\n Insertadas {len(resultado_calificaciones.inserted_ids)} calificaciones")
print(f"IDs insertados: {resultado_calificaciones.inserted_ids}")

print("RESUMEN DE DATOS INSERTADOS")
print(f"Estudiantes: {db.estudiantes.count_documents({})}")
print(f"Cursos: {db.cursos.count_documents({})}")
print(f"Calificaciones: {db.calificaciones.count_documents({})}")

print("MUESTRA DE DATOS INSERTADOS")

print(db.estudiantes.find_one())

print(db.cursos.find_one())

print(db.calificaciones.find_one())

for estudiante in db.estudiantes.find():
    print(f"- {estudiante['nombre']} ({estudiante['edad']} años)")

estudiante = db.estudiantes.find_one({"edad": 25})
print(f"- {estudiante['nombre']}")

for est in db.estudiantes.find({"edad": {"$gt": 23}}):
    print(f"- {est['nombre']}: {est['edad']} años")

for est in db.estudiantes.find({"edad": {"$gte": 20, "$lte": 25}}):
    print(f"- {est['nombre']}: {est['edad']} años")

for est in db.estudiantes.find({"ciudad": {"$in": ["Madrid", "Barcelona"]}}):
    print(f"- {est['nombre']} de {est['ciudad']}")

for est in db.estudiantes.find({"ciudad": {"$ne": "Madrid"}}):
    print(f"- {est['nombre']} de {est['ciudad']}")

for est in db.estudiantes.find({}, {"nombre": 1, "email": 1, "_id": 0}):
    print(f"- {est['nombre']}: {est['email']}")

for est in db.estudiantes.find().sort("edad", DESCENDING):
    print(f"- {est['nombre']}: {est['edad']} años")

for est in db.estudiantes.find({"$and": [{"ciudad": "Madrid"}, {"edad": {"$gt": 21}}]}):
    print(f"- {est['nombre']}")

# $or
for est in db.estudiantes.find({"$or": [{"ciudad": "Madrid"}, {"edad": {"$gt": 26}}]}):
    print(f"- {est['nombre']}")

# $not
for est in db.estudiantes.find({"edad": {"$not": {"$lt": 23}}}):
    print(f"- {est['nombre']}: {est['edad']} años")

# $exists
for est in db.estudiantes.find({"email": {"$exists": True}}):
    print(f"- {est['nombre']}: {est['email']}")

# $type
total_numeros = db.estudiantes.count_documents({"edad": {"$type": "number"}})
print(f"- {total_numeros} estudiantes tienen edad como número")

# $regex
for est in db.estudiantes.find({"nombre": {"$regex": "^L"}}):
    print(f"- {est['nombre']}")

client.close()
