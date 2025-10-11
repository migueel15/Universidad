import redis

class TaskManager:

    def __init__(self, host="localhost", port=6379, decode_responses=True):
        try:
            self.r = redis.Redis(
                    host=host,
                    port=port,
                    decode_responses=decode_responses
            )
            self.r.ping()
        except redis.ConnectionError as e:
            print(f"Fallo al conectarse a redis. {e}")
            raise


    def clearDatabase(self):
        self.r.flushdb()

    def closeConnection(self):
        self.r.close()

    def crear_usuario(self, id, nombre, email, edad):
        userId = f'usuario:{id}'
        self.r.hset(userId, mapping={
            "nombre":nombre,
            "email":email,
            "edad":edad
            })

    def obtener_perfil(self, id):
        userKey = f'usuario:{id}'
        user = self.r.hgetall(userKey)
        return user

    def crear_tarea(self, userId, title):
        taskListKey = f'tareas:{userId}'
        self.r.rpush(taskListKey, title)

    def obtener_tareas(self, userId):
        taskListKey = f'tareas:{userId}'
        return self.r.lrange(taskListKey,0,-1)

    def marcar_completada(self, userId,title):
        taskListKey = f'tareas:{userId}'
        self.r.lrem(taskListKey,0,title)

    def obtener_informacion_usuario(self, userId):
        perfil = self.obtener_perfil(userId)
        tareas = self.obtener_tareas(userId)

        return {
                "perfil": perfil,
                "tareas": tareas
        }


if __name__ == "__main__":
    try:
        manager = TaskManager()
        manager.clearDatabase()
        manager.crear_usuario(1,"miguel","miguel@gmail.com",22)
        user = manager.obtener_perfil(1)
        print(user)
        manager.crear_tarea(1,"hacer practica1")
        manager.crear_tarea(1,"hacer electronica")
        tareas = manager.obtener_tareas(1)
        print(tareas)
        manager.marcar_completada(1,"hacer electronica")
        tareas = manager.obtener_tareas(1)
        print(tareas)

        info = manager.obtener_informacion_usuario(1)
        print(info)
        manager.closeConnection()
    except redis.ConnectionError as e:
        print("Asegurate de que redis está corriendo.")
    except Exception as e:
        print("Error inesperado")
        
