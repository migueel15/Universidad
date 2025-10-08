import redis

r = redis.Redis(host="localhost",port=6379, decode_responses=True)
r.flushall()

def crear_usuario(id, nombre, email, edad):
    userId = f'usuario:{id}'
    r.hset(userId, mapping={
        "nombre":nombre,
        "email":email,
        "edad":edad
        })

def obtener_perfil(id):
    userKey = f'usuario:{id}'
    user = r.hgetall(userKey)
    return user

def crear_tarea(userId, title):
    taskListKey = f'tareas:{userId}'
    r.rpush(taskListKey, title)

def obtener_tareas(userId):
    taskListKey = f'tareas:{userId}'
    return r.lrange(taskListKey,0,-1)

def marcar_completada(userId,title):
    taskListKey = f'tareas:{userId}'
    r.lrem(taskListKey,0,title)

def obtener_informacion_usuario(userId):
    perfil = obtener_perfil(userId)
    tareas = obtener_tareas(userId)

    return {
            "perfil": perfil,
            "tareas": tareas
    }

r.close()

if __name__ == "__main__":
    crear_usuario(1,"miguel","miguel@gmail.com",22)
    user = obtener_perfil(1)
    print(user)
    crear_tarea(1,"hacer practica1")
    crear_tarea(1,"hacer electronica")
    tareas = obtener_tareas(1)
    print(tareas)
    marcar_completada(1,"hacer electronica")
    tareas = obtener_tareas(1)
    print(tareas)

    info = obtener_informacion_usuario(1)
    print(info)

    
    r.close()
