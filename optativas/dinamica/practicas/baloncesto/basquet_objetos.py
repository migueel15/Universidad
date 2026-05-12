import pygame
import pymunk
import pymunk.pygame_util
import math
from pymunk import Vec2d
import numpy as np


################################# SIMULACIÓN GENERICA ##############################################
####################################################################################################
class Tsim:
    def __init__(
        self, width=1000, height=600, suelo=600, PX_M=1, gravedad=(0, -9.81), fondo=None
    ):
        self.width = width
        self.height = height
        self.PX_M = PX_M
        self.M_PX = 1.0 / PX_M
        self.suelo = suelo
        # inicia pygame
        pygame.init()
        self.screen = pygame.display.set_mode((self.width, self.height))
        self.clock = pygame.time.Clock()
        # intenta poner el fondo
        if fondo != None:
            self.fondo = self.pone_fondo(fondo)
        else:
            self.fondo = None
        # crea el espacio
        self.space = pymunk.Space()
        self.space.gravity = Vec2d(gravedad[0], -gravedad[1]) * PX_M
        self.space.iterations = 35  # Aumentamos iteraciones para mayor estabilidad

        # Diccionario: {tecla: {'func': funcion, 'activo': bool}}
        self._eventos_teclado = {}
        self.running = True

    # -------- pone imagen de fondo ----------------------------------
    def pone_fondo(self, imagen):
        try:
            fondo = pygame.image.load(imagen).convert()
            fondo = pygame.transform.smoothscale(fondo, (self.width, self.height))
        except:
            fondo = None
        self.fondo = fondo
        return fondo

    # ----------------------------------------------------------------------
    def draw(self):
        if self.fondo:
            self.screen.blit(self.fondo, (0, 0))
        else:
            self.screen.fill((240, 240, 240))

    # ----------------------------------------------------------------------

    ############### eventos ############################################
    def add_evento_tecla(self, tecla, funcion, activo=True):
        """Registra una tecla con su función y estado inicial."""
        self._eventos_teclado[tecla] = {"func": funcion, "activo": activo}

    # -----------------------  on  / off------------------------------
    def set_estado_evento(self, tecla, estado):
        """Activa o desactiva un evento específico."""
        if tecla in self._eventos_teclado:
            self._eventos_teclado[tecla]["activo"] = estado

    # ------------------- manejador de eventos ----------------------
    def actualizar_eventos(self):
        """Procesa eventos y ejecuta solo los que están activos."""
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self.running = False
                return False

            if event.type == pygame.KEYDOWN:
                if event.key in self._eventos_teclado:
                    evento = self._eventos_teclado[event.key]
                    if evento["activo"]:
                        evento["func"]()
        return True

    # ----------------------------------------------------------------


##############################################################################################
##############################################################################################


###############################################################################################
# ----------- Un objeto genérico al que se le puede mandar y extraer la velocidad y ------------
# ----------- posición en metros, y ya se encarga de hacer todos los cambios dentro ------------
class Tobjeto:
    def __init__(self, sim):
        self.sim = sim
        # Se asume que las clases hijas crearán self.body

    # --- Métodos internos de conversión (privados) ---
    def _m_a_px(self, pos_m):
        x_px = pos_m[0] * self.sim.PX_M
        y_px = self.sim.suelo - (pos_m[1] * self.sim.PX_M)
        return Vec2d(x_px, y_px)

    def _px_a_m(self, pos_px):
        x_m = pos_px[0] / self.sim.PX_M
        y_m = (self.sim.suelo - pos_px[1]) / self.sim.PX_M
        return Vec2d(x_m, y_m)

    def _distancia_m_a_px(self, distancia_m):
        return distancia_m * self.sim.PX_M

    def _distancia_px_a_m(self, distancia_px):
        return distancia_px * self.sim.M_PX

    # --- Interfaz externa (Propiedades) ---
    @property
    def posicion(self):
        """Devuelve la posición en metros (x, y) desde el suelo"""
        return self._px_a_m(self.body.position)

    @posicion.setter
    def posicion(self, pos_m):
        """Define la posición pasando metros (x, y)"""
        self.body.position = self._m_a_px(pos_m)

    @property
    def velocidad(self):
        """Devuelve la velocidad en m/s (corrigiendo el eje Y)"""
        v = self.body.velocity / self.sim.PX_M
        return Vec2d(v.x, -v.y)

    @velocidad.setter
    def velocidad(self, vel_m):
        """Define la velocidad pasando metros/segundo (x, y)"""
        vx = vel_m[0] * self.sim.PX_M
        vy = -vel_m[1] * self.sim.PX_M
        self.body.velocity = (vx, vy)

    # Dentro de Tobjeto usando tabuladores
    def aplicar_impulso(self, imp_m):
        """
        Aplica un impulso instantáneo (fuerza * tiempo).
        imp_m: tupla (x, y) en unidades de masa * m/s
        """
        # Convertimos el impulso de metros a la escala de la simulación
        # Invertimos Y porque en tu lógica "metros" arriba es positivo
        imp_px = Vec2d(imp_m[0], -imp_m[1]) * self.sim.PX_M
        # Aplicamos el impulso en el centro de masa del objeto
        self.body.apply_impulse_at_local_point(imp_px)


#############################################################################################


# ---------------- suelo, como es estático no se puede modificar su velocidad o ----------
# ---------------- o posición                                                   ----------
class Tsuelo(Tobjeto):
    def __init__(self, sim, punto_a_m=None, punto_b_m=None, color=(0, 0, 0)):
        super().__init__(sim)
        self.color = color
        self.body = self.sim.space.static_body

        if punto_a_m is None:
            self.p1 = Vec2d(0, self.sim.suelo)
        else:
            self.p1 = self._m_a_px(punto_a_m)

        if punto_b_m is None:
            self.p2 = Vec2d(self.sim.width, self.sim.suelo)
        else:
            self.p2 = self._m_a_px(punto_b_m)

        self.shape = pymunk.Segment(self.body, self.p1, self.p2, 0)
        self.shape.elasticity = 0.8
        self.shape.friction = 0.6
        self.sim.space.add(self.shape)

    # --- Sobrescribimos los Setters para "bloquearlos" ---
    @Tobjeto.posicion.setter
    def posicion(self, valor):
        """El suelo es estático. No se puede cambiar su posición."""
        print("Advertencia: No se puede mover el suelo, es un objeto estático.")

    @Tobjeto.velocidad.setter
    def velocidad(self, valor):
        """El suelo es estático. No tiene velocidad."""
        print("Advertencia: No se puede asignar velocidad al suelo.")

    def draw(self):
        """Dibuja la línea del suelo en la pantalla"""
        # Dibujamos la línea usando las coordenadas de píxeles ya calculadas
        pygame.draw.line(self.sim.screen, self.color, self.p1, self.p2, 1)


###############################################################################################
class Tbalon(Tobjeto):
    def __init__(
        self,
        sim,
        pos_m,
        masa_kg=0.625,
        radio_m=0.119,
        img_path="balon_basket.png",
        Cd=0.54,
        k_magnus=0.7,
    ):
        # sim es la simulación a la que pertenece, puede ser un Tsim o Tbasket
        # self.sim = sim
        super().__init__(sim)
        self.radio_m = radio_m
        self.radio_px = self._distancia_m_a_px(radio_m)
        self.area_m2 = math.pi * self.radio_m**2
        self.rho_aire = 1.225
        self.Cd = Cd
        self.k_magnus = k_magnus
        self.Cm_rotacional = 0.02
        self.sujeto = False
        self.pos_mano = pos_m
        # 1. Física
        moment = pymunk.moment_for_circle(masa_kg, 0, self.radio_px)
        self.body = pymunk.Body(masa_kg, moment)
        self.posicion = pos_m
        self.shape = pymunk.Circle(self.body, self.radio_px)
        self.shape.elasticity = 0.85
        self.shape.friction = 0.5
        self.sim.space.add(self.body, self.shape)
        # 2. Imagen base (escalada al diámetro en píxeles)
        self.img_base = self._preparar_imagen(img_path)

    # -----------------------------------------------------------
    def _preparar_imagen(self, path):
        diametro = int(self.radio_px * 2)
        try:
            # Cargamos y escalamos una sola vez para ahorrar CPU
            img = pygame.image.load(
                path
            ).convert_alpha()  # convert_alpha para los png transparentes
            return pygame.transform.smoothscale(
                img, (diametro, diametro)
            )  # suaviza los bordes
        except:
            # Si no hay imagen, creamos un círculo rojo con una línea para ver la rotación
            surf = pygame.Surface((diametro, diametro), pygame.SRCALPHA)
            pygame.draw.circle(
                surf,
                (200, 50, 50),
                (int(self.radio_px), int(self.radio_px)),
                int(self.radio_px),
            )
            pygame.draw.line(
                surf,
                (255, 255, 255),
                (int(self.radio_px), int(self.radio_px)),
                (diametro, int(self.radio_px)),
                2,
            )
            return surf

    # ---------------------------------------------------------------
    def draw(self):
        # Pymunk usa radianes (horario positivo), Pygame usa grados (anti-horario positivo)
        # Por eso multiplicamos por -1 al convertir a grados
        angulo_deg = math.degrees(-self.body.angle)

        # Rotamos la imagen que escalamos en el __init__
        img_rotada = pygame.transform.rotate(self.img_base, angulo_deg)

        # Al rotar, el rectángulo de la imagen cambia de tamaño (se hace más grande)
        # Es vital centrar el nuevo rectángulo en la posición del cuerpo
        pos = self.body.position
        rect = img_rotada.get_rect(center=(int(pos.x), int(pos.y)))

        self.sim.screen.blit(img_rotada, rect)

    # ---------------------------------------------------------------
    def lanzar(self, v_ms, angulo_deg, omega=0):
        """Método auxiliar para aplicar el impulso inicial.
        se le pasa velocidad, angulo con la horizontal y
        velocidad angular en rad/s (negativo es backspin"""
        self.sujeto = False
        rad = math.radians(angulo_deg)
        self.velocidad = (v_ms * math.cos(rad), v_ms * math.sin(rad))
        self.body.angular_velocity = omega  # efecto de rotacion

    def sujetar_en_mano(self, pos_m):
        self.sujeto = True
        self.pos_mano = pos_m
        self.posicion = pos_m
        self.velocidad = (0, 0)
        self.body.angle = 0
        self.body.angular_velocity = 0
        self.body.force = (0, 0)
        self.body.torque = 0

    def aplicar_rozamiento_aire(self, v_viento=(0, 0)):
        v_viento = Vec2d(v_viento[0], -v_viento[1])
        v_rel = self.body.velocity * self.sim.M_PX - v_viento
        v_rel_mag = v_rel.length
        if v_rel_mag < 0.1:
            return
        f_drag = (
            -0.5
            * self.rho_aire
            * self.Cd
            * self.area_m2
            * v_rel_mag
            * v_rel
            / self.sim.M_PX
        )
        self.body.apply_force_at_world_point(f_drag, self.body.position)

    def aplicar_magnus(self, v_viento=(0, 0)):
        v_viento = Vec2d(v_viento[0], -v_viento[1])
        v_rel = self.body.velocity * self.sim.M_PX - v_viento
        v_rel_mag = v_rel.length
        if v_rel_mag < 0.1 or abs(self.body.angular_velocity) < 0.01:
            return
        S = self.radio_m * self.body.angular_velocity / v_rel_mag
        Cm = self.k_magnus * S / (2 + abs(S))
        u_magnus = Vec2d(-v_rel.y, v_rel.x).normalized()
        f_magnus = (
            0.5
            * self.rho_aire
            * Cm
            * self.area_m2
            * v_rel_mag**2
            * u_magnus
            / self.sim.M_PX
        )
        self.body.apply_force_at_world_point(f_magnus, self.body.position)

    def aplicar_frenado_rotacional(self):
        w = self.body.angular_velocity
        if abs(w) < 0.01:
            return
        torque_m = (
            -0.5 * self.rho_aire * self.Cm_rotacional * abs(w) * w * self.radio_m**5
        )
        self.body.torque += torque_m * self.sim.PX_M**2

    def update(self, dt):
        if self.sujeto:
            self.posicion = self.pos_mano
            self.velocidad = (0, 0)
            self.body.angle = 0
            self.body.angular_velocity = 0
            self.body.force = (0, 0)
            self.body.torque = 0
            return
        if not math.isfinite(self.body.angular_velocity):
            self.body.angular_velocity = 0
        self.body.angular_velocity = max(-80, min(80, self.body.angular_velocity))
        self.aplicar_rozamiento_aire()
        self.aplicar_magnus()
        self.aplicar_frenado_rotacional()


##############################################################################


class Ttablero(Tobjeto):
    def __init__(self, sim, x_tablero_m=7, color=(100, 100, 100)):
        super().__init__(sim)
        self.color = color
        self.espesor = self._distancia_m_a_px(0.05)
        self.alto = self._distancia_m_a_px(1.05)

        self.pos = self._m_a_px((x_tablero_m, 2.90))
        p0 = Vec2d(self.pos.x, self.pos.y - self.alto)  # arriba izq
        p1 = Vec2d(self.pos.x + self.espesor, self.pos.y - self.alto)  # arriba der
        p2 = Vec2d(self.pos.x + self.espesor, self.pos.y)  # abajo der
        p3 = Vec2d(self.pos.x, self.pos.y)  # arriba der
        self.body = self.sim.space.static_body
        self.shape = pymunk.Poly(self.body, [p0, p1, p2, p3])
        self.shape.elasticity = 0.8
        self.shape.friction = 0.6
        self.sim.space.add(self.shape)
        # ------
        # Aro Físico
        self.aro_y = self._m_a_px((0, 3.05)).y
        diametro_aro = self._distancia_m_a_px(0.45)
        self.x_aro_trasero = self.pos.x - self._distancia_m_a_px(0.15)
        self.x_aro_delantero = self.x_aro_trasero - diametro_aro

        self.aro_part_w = self._distancia_m_a_px(0.04)
        self.aro_part_h = self._distancia_m_a_px(0.02)
        self.x_aro_sop = self.x_aro_trasero + self.aro_part_w

        # Aro Trasero
        self.body_trasero = pymunk.Body(body_type=pymunk.Body.STATIC)
        self.body_trasero.position = (self.x_aro_trasero, self.aro_y)
        self.aro_trasero_shape = pymunk.Poly.create_box(
            self.body_trasero, (self.aro_part_w, self.aro_part_h)
        )
        # Aro Delantero
        self.body_delantero = pymunk.Body(body_type=pymunk.Body.STATIC)
        self.body_delantero.position = (self.x_aro_delantero, self.aro_y)
        self.aro_delantero_shape = pymunk.Poly.create_box(
            self.body_delantero, (self.aro_part_w, self.aro_part_h)
        )
        # Soporte aro
        self.body_sop = pymunk.Body(body_type=pymunk.Body.STATIC)
        self.body_sop.position = (self.x_aro_sop, self.aro_y)
        self.aro_sop_shape = pymunk.Poly.create_box(
            self.body_sop, (self.aro_part_w * 2, self.aro_part_h)
        )

        for aro_shape in [
            self.aro_trasero_shape,
            self.aro_delantero_shape,
            self.aro_sop_shape,
        ]:
            aro_shape.elasticity = 0.5
            aro_shape.friction = 0.5
            self.sim.space.add(aro_shape.body, aro_shape)

        self.cadenas_red = []
        self.nodos_red = []
        self.muelles_red = []
        self._crear_red()

    def _crear_red(self):
        radio_nodo = self._distancia_m_a_px(0.025)
        masa_nodo = 0.02
        distancias = [self._distancia_m_a_px(0.12)] * 4
        grupo_red = 1

        for anclaje in [self.body_delantero, self.body_trasero]:
            cadena = []
            cuerpo_padre = anclaje
            for dist in distancias:
                momento = pymunk.moment_for_circle(masa_nodo, 0, radio_nodo)
                nodo = pymunk.Body(masa_nodo, momento)
                nodo.position = (
                    cuerpo_padre.position.x,
                    cuerpo_padre.position.y + dist,
                )
                shape = pymunk.Circle(nodo, radio_nodo)
                shape.elasticity = 0.2
                shape.friction = 0.6
                shape.filter = pymunk.ShapeFilter(group=grupo_red)
                union = pymunk.PinJoint(cuerpo_padre, nodo, (0, 0), (0, 0))
                union.collide_bodies = False
                self.sim.space.add(nodo, shape, union)
                cadena.append(nodo)
                self.nodos_red.append(nodo)
                cuerpo_padre = nodo
            self.cadenas_red.append({"anclaje": anclaje, "nodos": cadena})

        longitudes_muelles = [
            self._distancia_m_a_px(0.36),
            self._distancia_m_a_px(0.30),
            self._distancia_m_a_px(0.22),
            self._distancia_m_a_px(0.14),
        ]
        stiffness = 50.0
        damping = 2.0

        for i in range(len(longitudes_muelles)):
            b_izq = self.cadenas_red[0]["nodos"][i]
            b_der = self.cadenas_red[1]["nodos"][i]
            muelle = pymunk.DampedSpring(
                b_izq, b_der, (0, 0), (0, 0), longitudes_muelles[i], stiffness, damping
            )
            muelle.collide_bodies = False
            self.sim.space.add(muelle)
            self.muelles_red.append(muelle)

    def update(self, dt):
        factor = max(0.0, 1.0 - 3.0 * dt)
        for nodo in self.nodos_red:
            nodo.velocity *= factor
            nodo.angular_velocity *= factor

    def _dibujar_red(self):
        color_red = (255, 255, 255)
        for cadena in self.cadenas_red:
            padre_pos = cadena["anclaje"].position
            for nodo in cadena["nodos"]:
                pos = nodo.position
                pygame.draw.line(
                    self.sim.screen,
                    color_red,
                    (int(padre_pos.x), int(padre_pos.y)),
                    (int(pos.x), int(pos.y)),
                    1,
                )
                padre_pos = pos

        if len(self.cadenas_red) != 2:
            return
        bolas_izq = self.cadenas_red[0]["nodos"]
        bolas_der = self.cadenas_red[1]["nodos"]
        for i in range(len(bolas_izq)):
            pygame.draw.line(
                self.sim.screen,
                color_red,
                (int(bolas_izq[i].position.x), int(bolas_izq[i].position.y)),
                (int(bolas_der[i].position.x), int(bolas_der[i].position.y)),
                1,
            )
            if i > 0:
                pygame.draw.line(
                    self.sim.screen,
                    color_red,
                    (
                        int(bolas_izq[i - 1].position.x),
                        int(bolas_izq[i - 1].position.y),
                    ),
                    (int(bolas_der[i].position.x), int(bolas_der[i].position.y)),
                    1,
                )
                pygame.draw.line(
                    self.sim.screen,
                    color_red,
                    (
                        int(bolas_der[i - 1].position.x),
                        int(bolas_der[i - 1].position.y),
                    ),
                    (int(bolas_izq[i].position.x), int(bolas_izq[i].position.y)),
                    1,
                )

        for nodo in bolas_izq + bolas_der:
            pygame.draw.circle(
                self.sim.screen,
                color_red,
                (int(nodo.position.x), int(nodo.position.y)),
                max(1, int(self._distancia_m_a_px(0.012))),
            )

    def draw(self):
        pygame.draw.rect(
            self.sim.screen,
            self.color,
            (self.pos.x, self.pos.y - self.alto, self.espesor, self.alto),
        )  # self.espesor,self.alto))
        color_sop = (255, 150, 150)
        sopx = self._distancia_m_a_px(0.10)
        sopy = self._distancia_m_a_px(0.40)
        barrax = self._distancia_m_a_px(0.05)
        pygame.draw.rect(
            self.sim.screen,
            color_sop,
            (self.pos.x + self.espesor, self.pos.y - 0.6 * self.alto, sopx, sopy),
        )
        pygame.draw.rect(
            self.sim.screen,
            color_sop,
            (self.pos.x + self.espesor + sopx, 0, barrax, self.pos.y - 0.4 * self.alto),
        )
        self._dibujar_red()
        # ARO
        color_aro_suave = (255, 150, 150)
        pygame.draw.line(
            self.sim.screen,
            color_aro_suave,
            (int(self.x_aro_delantero), int(self.aro_y)),
            (int(self.x_aro_trasero), int(self.aro_y)),
            3,
        )

        for b in [self.body_trasero, self.body_delantero]:
            pygame.draw.rect(
                self.sim.screen,
                (200, 0, 0),
                (
                    int(b.position.x - self.aro_part_w / 2),
                    int(b.position.y - self.aro_part_h / 2),
                    int(self.aro_part_w),
                    int(self.aro_part_h),
                ),
            )

        pygame.draw.rect(
            self.sim.screen,
            (200, 0, 0),
            (
                self.x_aro_sop,
                int(self.aro_y - self.aro_part_h / 2),
                self.pos.x - self.x_aro_sop,
                int(self.aro_part_h),
            ),
        )


class Tjugador(Tobjeto):
    def __init__(
        self,
        sim,
        pos_m=(2, 0),
        imagenes=("jugador01.png", "jugador02.png"),
        alturas_m=(2.0, 2.2),
    ):
        super().__init__(sim)
        self.pos_m = pos_m
        self.lanzando = False
        self.imagenes = []
        for path, altura_m in zip(imagenes, alturas_m):
            self.imagenes.append(self._preparar_imagen(path, altura_m))

    def _preparar_imagen(self, path, altura_m):
        p_height = max(1, int(self._distancia_m_a_px(altura_m)))
        try:
            img = pygame.image.load(path).convert_alpha()
            ratio = img.get_width() / img.get_height()
            return pygame.transform.smoothscale(img, (int(p_height * ratio), p_height))
        except:
            p_width = max(1, int(0.35 * p_height))
            surf = pygame.Surface((p_width, p_height), pygame.SRCALPHA)
            pygame.draw.circle(
                surf,
                (230, 190, 130),
                (p_width // 2, int(0.12 * p_height)),
                int(0.08 * p_height),
            )
            pygame.draw.rect(
                surf,
                (50, 90, 180),
                (
                    int(0.30 * p_width),
                    int(0.20 * p_height),
                    int(0.40 * p_width),
                    int(0.45 * p_height),
                ),
            )
            pygame.draw.line(
                surf,
                (30, 30, 30),
                (p_width // 2, int(0.65 * p_height)),
                (int(0.20 * p_width), p_height),
                3,
            )
            pygame.draw.line(
                surf,
                (30, 30, 30),
                (p_width // 2, int(0.65 * p_height)),
                (int(0.80 * p_width), p_height),
                3,
            )
            return surf

    def draw(self):
        idx = 1 if self.lanzando and len(self.imagenes) > 1 else 0
        img = self.imagenes[idx]
        pos = self._m_a_px(self.pos_m)
        rect = img.get_rect(midbottom=(int(pos.x), int(pos.y)))
        self.sim.screen.blit(img, rect)


########## ESTA ES LA CLASE QUE LO CONTIENE TODO ############################################
############################ específica para baloncesto ######################################
class Tbasket(Tsim):
    def __init__(self, x_tablero_m=7, pos_balon_m=(1, 2), **kwargs):
        super().__init__(**kwargs)
        self.pos_inicio_balon = pos_balon_m

        # Parámetros de lanzamiento por defecto
        self.v_lanzamiento = 9.0  # m/s
        self.ang_lanzamiento = 55.0  # grados
        self.w_lanzamiento = -15.0  # rad/s (backspin)

        # balon
        self.balon = Tbalon(self, self.pos_inicio_balon)
        self.balon.sujetar_en_mano(self.pos_inicio_balon)
        # suelo
        self.suelo_fisico = Tsuelo(self)

        # canasta
        self.tablero = Ttablero(self, x_tablero_m)
        self.jugador = Tjugador(self, (self.pos_inicio_balon[0], 0))

        self.objetos = {
            "suelo": self.suelo_fisico,
            "jugador": self.jugador,
            "balon": self.balon,
            "tablero": self.tablero,
        }

        # teclas
        self.add_evento_tecla(pygame.K_SPACE, self.lanzar_triple)
        self.add_evento_tecla(pygame.K_ESCAPE, self.resetear_posicion)

    def lanzar_triple(self):
        # Ahora usamos las variables de la instancia
        print(f"Lanzando a {self.v_lanzamiento} m/s con {self.ang_lanzamiento}º")
        self.balon.lanzar(self.v_lanzamiento, self.ang_lanzamiento, self.w_lanzamiento)
        self.jugador.lanzando = True
        self.set_estado_evento(pygame.K_SPACE, False)

    def configurar_tiro(self, v=None, ang=None, w=None):
        """Método para cambiar los parámetros desde fuera"""
        if v is not None:
            self.v_lanzamiento = v
        if ang is not None:
            self.ang_lanzamiento = ang
        if w is not None:
            self.w_lanzamiento = w

    def resetear_posicion(self):
        self.balon.sujetar_en_mano(self.pos_inicio_balon)
        self.jugador.lanzando = False
        self.set_estado_evento(pygame.K_SPACE, True)

    def update(self, dt):
        for obj in self.objetos.values():
            actualizar = getattr(obj, "update", None)
            if actualizar:
                actualizar(dt)

    def draw(self):
        super().draw()
        for obj in self.objetos.values():
            dibujar = getattr(obj, "draw", None)
            if dibujar:
                dibujar()


# -------------------------------------------------------------------------------------------


###############################################################################################
if __name__ == "__main__":
    bk = Tbasket(
        x_tablero_m=7,
        pos_balon_m=(2, 2),
        PX_M=120,
        width=1000,
        height=600,
        suelo=600,
        gravedad=(0, -9.81),
        fondo="grada_baloncesto03.jpg",
    )
    bk.configurar_tiro(8.5, 65, -10)

    #########################################################################
    #########################################################################

    FPS = 60
    substeps = 30
    dt = 1.0 / FPS / substeps
    while bk.actualizar_eventos():

        for _ in range(substeps):
            bk.update(dt)
            bk.space.step(dt)

        bk.draw()
        # print(bk.balon.velocidad)

        pygame.display.flip()
        bk.clock.tick(FPS)

    pygame.quit()
