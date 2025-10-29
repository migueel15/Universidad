import redis
from datetime import datetime

class SistemaClasificacion:

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
        print("Base de datos limpia")

    def closeConnection(self):
        self.r.close()
        print("Conexion cerrada")

    def registrar_equipo(self, id_equipo, nombre, ciudad, entrenador, año_fundacion):
        equipo_key = f"equipo:{id_equipo}"
        self.r.hset(equipo_key, mapping={
            "nombre": nombre,
            "ciudad": ciudad,
            "entrenador": entrenador,
            "año_fundacion": año_fundacion
        })
        print(f"Equipo '{nombre}' registrado con ID {id_equipo}")

    def obtener_equipo(self, id_equipo):
        equipo_key = f"equipo:{id_equipo}"
        return self.r.hgetall(equipo_key)

    def inicializar_clasificacion(self):
        equipos_keys = self.r.keys("equipo:*")
        equipos_dict = {}
        for key in equipos_keys:
            id_equipo = key.split(":")[1]
            equipos_dict[id_equipo] = 0
        if equipos_dict:
            self.r.zadd("clasificacion_equipos", equipos_dict)

    def registrar_jugador(self, id_jugador, nombre, edad, posicion, id_equipo):
        jugador_key = f"jugador:{id_jugador}"
        self.r.hset(jugador_key, mapping={
            "nombre": nombre,
            "edad": edad,
            "posicion": posicion,
            "equipo": id_equipo
        })
        print(f"Jugador '{nombre}' registrado con ID {id_jugador}")

    def inicializar_goleadores(self):
        jugadores_keys = self.r.keys("jugador:*")
        jugadores_dict = {}
        for key in jugadores_keys:
            id_jugador = key.split(":")[1]
            jugadores_dict[id_jugador] = 0
        if jugadores_dict:
            self.r.zadd("goleadores", jugadores_dict)



    def registrar_partido(self, id_partido, local, visitante, resultado, fecha):
        partido_key = f"partido:{id_partido}"
        self.r.hset(partido_key, mapping={
            "local": local,
            "visitante": visitante,
            "resultado": resultado,
            "fecha": fecha
        })
        timestamp = datetime.fromisoformat(fecha).timestamp()
        self.r.zadd("partidos_jugados", {id_partido: timestamp})
        print(f"Partido registrado: {local} {resultado} {visitante}")

    def actualizar_clasificacion(self, id_equipo, puntos):
        self.r.zincrby("clasificacion_equipos", puntos, id_equipo)
        print(f"Equipo {id_equipo} actualizado con +{puntos} puntos")

    def actualizar_goleador(self, id_jugador, goles):
        self.r.zincrby("goleadores", goles, id_jugador)
        print(f"Jugador {id_jugador} ha marcado +{goles} goles")

    def programar_partido(self, id_partido, local, visitante, fecha):
        partido_key = f"partido:{id_partido}"
        self.r.hset(partido_key, mapping={
            "local": local,
            "visitante": visitante,
            "resultado": "pendiente",
            "fecha": fecha
        })
        timestamp = datetime.fromisoformat(fecha).timestamp()
        self.r.zadd("calendario", {id_partido: timestamp})
        print(f"Partido programado: {local} vs {visitante}")

    def obtener_clasificacion(self):
        clasificacion = self.r.zrevrange("clasificacion_equipos", 0, -1, withscores=True)
        resultado = []
        for id_equipo, puntos in clasificacion:
            equipo = self.obtener_equipo(id_equipo)
            resultado.append(f"{equipo['nombre']} ({int(puntos)}pts)")
        return resultado

    def obtener_goleadores(self):
        goleadores = self.r.zrevrange("goleadores", 0, -1, withscores=True)
        resultado = []
        for id_jugador, goles in goleadores:
            jugador = self.obtener_jugador(id_jugador)
            resultado.append(f"{jugador['nombre']} ({int(goles)} goles)")
        return resultado

    def obtener_jugador(self, id_jugador):
        jugador_key = f"jugador:{id_jugador}"
        return self.r.hgetall(jugador_key)

    def obtener_historial_partidos(self):
        partidos_ids = self.r.zrange("partidos_jugados", 0, -1)
        resultado = []
        for id_partido in partidos_ids:
            partido_key = f"partido:{id_partido}"
            partido = self.r.hgetall(partido_key)
            resultado.append(f"{partido['local']} {partido['resultado']} {partido['visitante']}")
        return resultado

    def registrar_traspaso(self, id_jugador, id_equipo_origen, id_equipo_destino):
        traspaso = f"{id_jugador}-{id_equipo_origen}-{id_equipo_destino}"
        self.r.lpush("historial", traspaso)
        jugador_key = f"jugador:{id_jugador}"
        self.r.hset(jugador_key, "equipo", id_equipo_destino)
        print(f"Traspaso registrado: Jugador {id_jugador} de equipo {id_equipo_origen} a {id_equipo_destino}")


if __name__ == "__main__":
    try:
        manager = SistemaClasificacion()
        manager.clearDatabase()

        # Ejercicio 1
        manager.registrar_equipo("1", "Leones FC", "Madrid", "Carlos Ruiz", "1920")
        manager.registrar_equipo("2", "Águilas Deportivas", "Barcelona", "Ana Martínez", "1935")
        manager.registrar_equipo("3", "Tiburones FC", "Valencia", "David González", "1948")

        # Ejercicio 2
        manager.inicializar_clasificacion()

        # Ejercicio 3
        manager.registrar_jugador("1", "Luis Torres", "25", "delantero", "1")
        manager.registrar_jugador("2", "María Rodríguez", "28", "centrocampista", "2")
        manager.registrar_jugador("3", "Javier López", "22", "defensa", "3")
        manager.registrar_jugador("4", "Sofía García", "26", "delantero", "1")

        # Ejercicio 4
        manager.inicializar_goleadores()

        # Ejercicio 5
        manager.registrar_partido("1", "Leones FC", "Águilas Deportivas", "2-1", "2025-10-01")
        manager.registrar_partido("2", "Tiburones FC", "Leones FC", "0-0", "2025-10-08")
        manager.registrar_partido("3", "Águilas Deportivas", "Tiburones FC", "3-2", "2025-10-15")

        # Ejercicio 6
        manager.actualizar_clasificacion("1", 3)
        manager.actualizar_clasificacion("1", 1)
        manager.actualizar_clasificacion("2", 3)
        manager.actualizar_clasificacion("3", 1)

        # Ejercicio 7
        manager.actualizar_goleador("1", 2)
        manager.actualizar_goleador("2", 1)
        manager.actualizar_goleador("2", 2)
        manager.actualizar_goleador("4", 1)

        # Ejercicio 8
        manager.programar_partido("4", "Leones FC", "Tiburones FC", "2025-10-22")
        manager.programar_partido("5", "Águilas Deportivas", "Leones FC", "2025-10-29")

        # Ejercicio 9
        print("\nClasificación:")
        for equipo in manager.obtener_clasificacion():
            print(f"  {equipo}")
        
        print("\nGoleadores:")
        for goleador in manager.obtener_goleadores():
            print(f"  {goleador}")
        
        print("\nHistorial de partidos:")
        for partido in manager.obtener_historial_partidos():
            print(f"  {partido}")

        # Ejercicio 10
        manager.registrar_traspaso("4", "1", "2")

        manager.closeConnection()

    except redis.ConnectionError:
        print("Asegurate de que redis está corriendo.")
    except Exception as e:
        print(f"Error inesperado: {e}")
