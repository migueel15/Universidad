import pygame
import pymunk
import pymunk.pygame_util
import math
import os
from pymunk import Vec2d

from rozamiento_aire_basket import (
	RADIO_BALON_BASKET_M,
	AREA_BALON_BASKET_M2,
	RHO_AIRE,
	MU_AIRE,
	aplicar_arrastre_newton_basket,
	aplicar_magnus_basket,
	aplicar_frenado_aire_rotacional_basket
)


BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ASSETS_DIR = os.path.join(BASE_DIR, "assets")


def asset_path(nombre_archivo):
	return os.path.join(ASSETS_DIR, nombre_archivo)


class Tsim:
	def __init__(self, width=1000, height=600, suelo=600, PX_M=1, gravedad=(0, -9.81), fondo=None):
		self.width = width
		self.height = height
		self.PX_M = PX_M
		self.M_PX = 1.0 / PX_M
		self.suelo = suelo

		pygame.init()
		self.screen = pygame.display.set_mode((self.width, self.height))
		pygame.display.set_caption("Tarea baloncesto - Arrastre, Magnus, Reynolds y red física")
		self.clock = pygame.time.Clock()
		self.font = pygame.font.SysFont("Arial", 16)

		if fondo is not None:
			self.fondo = self.pone_fondo(fondo)
		else:
			self.fondo = None

		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		self.space = pymunk.Space()
		self.space.gravity = Vec2d(gravedad[0], -gravedad[1]) * PX_M
		self.space.iterations = 35

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

		self._eventos_teclado = {}
		self.running = True

	def pone_fondo(self, imagen):
		try:
			ruta = asset_path(imagen)
			fondo = pygame.image.load(ruta).convert()
			fondo = pygame.transform.smoothscale(fondo, (self.width, self.height))
		except Exception as e:
			print(f"No se pudo cargar el fondo {imagen}: {e}")
			fondo = None

		self.fondo = fondo
		return fondo

	def draw(self):
		if self.fondo:
			self.screen.blit(self.fondo, (0, 0))
		else:
			self.screen.fill((240, 240, 240))

	def add_evento_tecla(self, tecla, funcion, activo=True):
		self._eventos_teclado[tecla] = {
			"func": funcion,
			"activo": activo
		}

	def set_estado_evento(self, tecla, estado):
		if tecla in self._eventos_teclado:
			self._eventos_teclado[tecla]["activo"] = estado

	def actualizar_eventos(self):
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


class Tobjeto:
	def __init__(self, sim):
		self.sim = sim

	def _m_a_px(self, pos_m):
		x_px = pos_m[0] * self.sim.PX_M
		y_px = self.sim.suelo - (pos_m[1] * self.sim.PX_M)

		return Vec2d(x_px, y_px)

	def _px_a_m(self, pos_px):
		x_m = pos_px[0] / self.sim.PX_M
		y_m = (self.sim.suelo - pos_px[1]) / self.sim.PX_M

		return Vec2d(x_m, y_m)

	def _long_m_a_px(self, longitud_m):
		return longitud_m * self.sim.PX_M

	def _long_px_a_m(self, longitud_px):
		return longitud_px / self.sim.PX_M

	def _vector_m_a_px(self, vector_m):
		return Vec2d(vector_m[0], -vector_m[1]) * self.sim.PX_M

	def _vector_px_a_m(self, vector_px):
		v = Vec2d(vector_px[0], vector_px[1]) / self.sim.PX_M

		return Vec2d(v.x, -v.y)

	@property
	def posicion(self):
		return self._px_a_m(self.body.position)

	@posicion.setter
	def posicion(self, pos_m):
		self.body.position = self._m_a_px(pos_m)

	@property
	def velocidad(self):
		return self._vector_px_a_m(self.body.velocity)

	@velocidad.setter
	def velocidad(self, vel_m):
		self.body.velocity = self._vector_m_a_px(vel_m)

	def aplicar_impulso(self, imp_m):
		imp_px = self._vector_m_a_px(imp_m)
		self.body.apply_impulse_at_local_point(imp_px)

	def aplicar_fuerza(self, fuerza_m):
		fuerza_px = self._vector_m_a_px(fuerza_m)
		self.body.apply_force_at_world_point(fuerza_px, self.body.position)

	def draw(self):
		pass


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

		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		self.shape = pymunk.Segment(self.body, self.p1, self.p2, 0)
		self.shape.elasticity = 0.8
		self.shape.friction = 0.6
		self.sim.space.add(self.shape)

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

	@Tobjeto.posicion.setter
	def posicion(self, valor):
		print("Advertencia: no se puede mover el suelo porque es estático.")

	@Tobjeto.velocidad.setter
	def velocidad(self, valor):
		print("Advertencia: no se puede asignar velocidad al suelo porque es estático.")

	def draw(self):
		pygame.draw.line(
			self.sim.screen,
			self.color,
			self.p1,
			self.p2,
			2
		)


class Tbalon(Tobjeto):
	def __init__(self, sim, pos_m, masa_kg=0.625, radio_m=RADIO_BALON_BASKET_M, img_path="balon_basket.png"):
		super().__init__(sim)

		self.masa_kg = masa_kg
		self.radio_m = radio_m
		self.radio_px = self._long_m_a_px(radio_m)

		self.area_m2 = AREA_BALON_BASKET_M2
		self.rho_aire = RHO_AIRE
		self.mu_aire = MU_AIRE

		self.arrastre_activo = True
		self.crisis_activa = True
		self.magnus_activo = True
		self.frenado_rotacional_activo = True

		self.ultimo_reynolds = 0
		self.ultimo_Cd = 0
		self.ultimo_Cd_sin_crisis = 0
		self.ultimo_Cd_con_crisis = 0
		self.ultimo_Cm = 0
		self.ultimo_S = 0
		self.ultimo_tau_rot = 0

		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		moment = pymunk.moment_for_circle(masa_kg, 0, self.radio_px)

		self.body = pymunk.Body(masa_kg, moment)
		self.posicion = pos_m

		self.shape = pymunk.Circle(self.body, self.radio_px)
		self.shape.elasticity = 0.85
		self.shape.friction = 0.5

		self.sim.space.add(self.body, self.shape)

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

		self.img_base = self._preparar_imagen(img_path)

	def _preparar_imagen(self, path):
		diametro = int(self.radio_px * 2)

		try:
			ruta = asset_path(path)
			img = pygame.image.load(ruta).convert_alpha()

			return pygame.transform.smoothscale(img, (diametro, diametro))

		except Exception as e:
			print(f"No se pudo cargar la imagen del balón {path}: {e}")

			surf = pygame.Surface((diametro, diametro), pygame.SRCALPHA)
			pygame.draw.circle(
				surf,
				(200, 50, 50),
				(int(self.radio_px), int(self.radio_px)),
				int(self.radio_px)
			)
			pygame.draw.line(
				surf,
				(255, 255, 255),
				(int(self.radio_px), int(self.radio_px)),
				(diametro, int(self.radio_px)),
				2
			)

			return surf

	def aplicar_aerodinamica(self):
		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		if self.arrastre_activo:
			info_drag = aplicar_arrastre_newton_basket(
				self.body,
				AREA_M2=self.area_m2,
				radio_m=self.radio_m,
				M_PX=self.sim.M_PX,
				rho=self.rho_aire,
				mu=self.mu_aire,
				crisis=self.crisis_activa
			)

			self.ultimo_reynolds = info_drag["Re"]
			self.ultimo_Cd = info_drag["Cd"]
			self.ultimo_Cd_sin_crisis = info_drag["Cd_sin_crisis"]
			self.ultimo_Cd_con_crisis = info_drag["Cd_con_crisis"]

		else:
			v_ms = self.body.velocity * self.sim.M_PX
			v_mag = v_ms.length

			if v_mag < 0.1:
				self.ultimo_reynolds = 0
				self.ultimo_Cd = 0
				self.ultimo_Cd_sin_crisis = 0
				self.ultimo_Cd_con_crisis = 0

		if self.magnus_activo:
			info_magnus = aplicar_magnus_basket(
				self.body,
				AREA_M2=self.area_m2,
				radio_m=self.radio_m,
				M_PX=self.sim.M_PX,
				rho=self.rho_aire,
				k=0.7
			)

			self.ultimo_Cm = info_magnus["Cm"]
			self.ultimo_S = info_magnus["S"]

		else:
			self.ultimo_Cm = 0
			self.ultimo_S = 0

		if self.frenado_rotacional_activo:
			info_rot = aplicar_frenado_aire_rotacional_basket(
				self.body,
				radio_m=self.radio_m,
				M_PX=self.sim.M_PX,
				rho=self.rho_aire,
				Cm_rot=0.02
			)

			self.ultimo_tau_rot = info_rot["tau"]

		else:
			self.ultimo_tau_rot = 0

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

	def draw(self):
		angulo_deg = math.degrees(-self.body.angle)
		img_rotada = pygame.transform.rotate(self.img_base, angulo_deg)

		pos = self.body.position
		rect = img_rotada.get_rect(center=(int(pos.x), int(pos.y)))

		self.sim.screen.blit(img_rotada, rect)

	def lanzar(self, v_ms, angulo_deg, omega=0):
		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		rad = math.radians(angulo_deg)

		vx = v_ms * math.cos(rad)
		vy = v_ms * math.sin(rad)

		self.velocidad = (vx, vy)
		self.body.angular_velocity = omega

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================


class Ttablero(Tobjeto):
	def __init__(self, sim, x_tablero_m=7, color=(100, 100, 100)):
		super().__init__(sim)

		self.color = color
		self.body = self.sim.space.static_body

		self.x_tablero_m = x_tablero_m
		self.espesor_m = 0.05
		self.alto_m = 1.05

		self.espesor = self._long_m_a_px(self.espesor_m)
		self.alto = self._long_m_a_px(self.alto_m)

		self.pos = self._m_a_px(Vec2d(x_tablero_m, 2.90))

		p0 = Vec2d(self.pos.x, self.pos.y - self.alto)
		p1 = Vec2d(self.pos.x + self.espesor, self.pos.y - self.alto)
		p2 = Vec2d(self.pos.x + self.espesor, self.pos.y)
		p3 = Vec2d(self.pos.x, self.pos.y)

		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		self.shape = pymunk.Poly(self.body, [p0, p1, p2, p3])
		self.shape.elasticity = 0.8
		self.shape.friction = 0.6
		self.sim.space.add(self.shape)

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

		self._crear_aro()
		self._crear_red()

	def _crear_aro(self):
		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		_, self.aro_y = self._m_a_px(Vec2d(0, 3.05))

		diametro_aro = self._long_m_a_px(0.45)

		self.x_aro_trasero = self.pos.x - self._long_m_a_px(0.15)
		self.x_aro_delantero = self.x_aro_trasero - diametro_aro

		self.aro_part_w = self._long_m_a_px(0.04)
		self.aro_part_h = self._long_m_a_px(0.02)

		self.x_aro_sop = self.x_aro_trasero + self.aro_part_w

		self.body_trasero = pymunk.Body(body_type=pymunk.Body.STATIC)
		self.body_trasero.position = (self.x_aro_trasero, self.aro_y)
		self.aro_trasero_shape = pymunk.Poly.create_box(
			self.body_trasero,
			(self.aro_part_w, self.aro_part_h)
		)

		self.body_delantero = pymunk.Body(body_type=pymunk.Body.STATIC)
		self.body_delantero.position = (self.x_aro_delantero, self.aro_y)
		self.aro_delantero_shape = pymunk.Poly.create_box(
			self.body_delantero,
			(self.aro_part_w, self.aro_part_h)
		)

		self.body_sop = pymunk.Body(body_type=pymunk.Body.STATIC)
		self.body_sop.position = (self.x_aro_sop, self.aro_y)
		self.aro_sop_shape = pymunk.Poly.create_box(
			self.body_sop,
			(self.aro_part_w * 2, self.aro_part_h)
		)

		for shape in [self.aro_trasero_shape, self.aro_delantero_shape, self.aro_sop_shape]:
			shape.elasticity = 0.5
			shape.friction = 0.5
			self.sim.space.add(shape.body, shape)

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

	def _crear_red(self):
		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		self.cadenas_red = []
		self.muelles_red = []

		self.radio_bolita = self._long_m_a_px(0.025)

		masa_bolita = 0.02
		niveles = 5
		dist_vertical = self._long_m_a_px(0.10)

		for body_anclaje in [self.body_delantero, self.body_trasero]:
			cadena = []
			cuerpo_padre = body_anclaje

			for _ in range(niveles):
				momento = pymunk.moment_for_circle(masa_bolita, 0, self.radio_bolita)

				b_hijo = pymunk.Body(masa_bolita, momento)
				b_hijo.position = (
					cuerpo_padre.position.x,
					cuerpo_padre.position.y + dist_vertical
				)

				s_hijo = pymunk.Circle(b_hijo, self.radio_bolita)
				s_hijo.elasticity = 0.25
				s_hijo.friction = 0.8

				union = pymunk.PinJoint(cuerpo_padre, b_hijo, (0, 0), (0, 0))
				union.collide_bodies = False

				self.sim.space.add(b_hijo, s_hijo, union)

				cadena.append(b_hijo)
				cuerpo_padre = b_hijo

			self.cadenas_red.append({
				"anclaje": body_anclaje,
				"bolitas": cadena
			})

		bolas_izq = self.cadenas_red[0]["bolitas"]
		bolas_der = self.cadenas_red[1]["bolitas"]

		stiffness = 45.0
		damping = 2.5

		for i in range(niveles):
			longitud = self._long_m_a_px(0.36 - i * 0.045)

			muelle = pymunk.DampedSpring(
				bolas_izq[i],
				bolas_der[i],
				(0, 0),
				(0, 0),
				longitud,
				stiffness,
				damping
			)

			self.sim.space.add(muelle)
			self.muelles_red.append(muelle)

		for i in range(niveles - 1):
			muelle1 = pymunk.DampedSpring(
				bolas_izq[i],
				bolas_der[i + 1],
				(0, 0),
				(0, 0),
				self._long_m_a_px(0.22),
				stiffness * 0.5,
				damping
			)

			muelle2 = pymunk.DampedSpring(
				bolas_der[i],
				bolas_izq[i + 1],
				(0, 0),
				(0, 0),
				self._long_m_a_px(0.22),
				stiffness * 0.5,
				damping
			)

			self.sim.space.add(muelle1, muelle2)
			self.muelles_red.append(muelle1)
			self.muelles_red.append(muelle2)

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

	def actualizar_red(self):
		# ============================================================
		# ========================= FÍSICA ============================
		# ============================================================

		for cadena in self.cadenas_red:
			for b in cadena["bolitas"]:
				b.velocity *= 0.985
				b.angular_velocity *= 0.985

		# ============================================================
		# ======================= FIN FÍSICA =========================
		# ============================================================

	def draw(self):
		pygame.draw.rect(
			self.sim.screen,
			self.color,
			(self.pos.x, self.pos.y - self.alto, self.espesor, self.alto)
		)

		color_sop = (255, 150, 150)

		sopx = self._long_m_a_px(0.10)
		sopy = self._long_m_a_px(0.40)
		barrax = self._long_m_a_px(0.05)

		pygame.draw.rect(
			self.sim.screen,
			color_sop,
			(
				self.pos.x + self.espesor,
				self.pos.y - 0.6 * self.alto,
				sopx,
				sopy
			)
		)

		pygame.draw.rect(
			self.sim.screen,
			color_sop,
			(
				self.pos.x + self.espesor + sopx,
				0,
				barrax,
				self.pos.y - 0.4 * self.alto
			)
		)

		self._draw_red()
		self._draw_aro()

	def _draw_red(self):
		color_red = (255, 255, 255)

		bolas_izq = self.cadenas_red[0]["bolitas"]
		bolas_der = self.cadenas_red[1]["bolitas"]

		for cadena in self.cadenas_red:
			padre_pos = cadena["anclaje"].position

			for b_hijo in cadena["bolitas"]:
				hijo_pos = b_hijo.position

				pygame.draw.line(
					self.sim.screen,
					color_red,
					(int(padre_pos.x), int(padre_pos.y)),
					(int(hijo_pos.x), int(hijo_pos.y)),
					1
				)

				padre_pos = hijo_pos

		for b_izq, b_der in zip(bolas_izq, bolas_der):
			pygame.draw.line(
				self.sim.screen,
				color_red,
				(int(b_izq.position.x), int(b_izq.position.y)),
				(int(b_der.position.x), int(b_der.position.y)),
				1
			)

		for i in range(len(bolas_izq) - 1):
			pygame.draw.line(
				self.sim.screen,
				color_red,
				(int(bolas_izq[i].position.x), int(bolas_izq[i].position.y)),
				(int(bolas_der[i + 1].position.x), int(bolas_der[i + 1].position.y)),
				1
			)

			pygame.draw.line(
				self.sim.screen,
				color_red,
				(int(bolas_der[i].position.x), int(bolas_der[i].position.y)),
				(int(bolas_izq[i + 1].position.x), int(bolas_izq[i + 1].position.y)),
				1
			)

		for b in bolas_izq + bolas_der:
			pygame.draw.circle(
				self.sim.screen,
				color_red,
				(int(b.position.x), int(b.position.y)),
				int(self.radio_bolita)
			)

	def _draw_aro(self):
		color_aro_suave = (255, 150, 150)

		pygame.draw.line(
			self.sim.screen,
			color_aro_suave,
			(int(self.x_aro_delantero), int(self.aro_y)),
			(int(self.x_aro_trasero), int(self.aro_y)),
			3
		)

		for b in [self.body_trasero, self.body_delantero]:
			pygame.draw.rect(
				self.sim.screen,
				(200, 0, 0),
				(
					int(b.position.x - self.aro_part_w / 2),
					int(b.position.y - self.aro_part_h / 2),
					int(self.aro_part_w),
					int(self.aro_part_h)
				)
			)

		pygame.draw.rect(
			self.sim.screen,
			(200, 0, 0),
			(
				int(self.x_aro_sop - self.aro_part_w),
				int(self.aro_y - self.aro_part_h / 2),
				int(self.aro_part_w * 2),
				int(self.aro_part_h)
			)
		)


class Tjugador(Tobjeto):
	def __init__(self, sim, x_m=2.0, altura_m=2.05, img_preparado="jugador01.png", img_lanzando="jugador02.png"):
		super().__init__(sim)

		self.x_m = x_m
		self.altura_m = altura_m
		self.lanzando = False

		self.img_preparado = self._cargar_imagen(img_preparado)
		self.img_lanzando = self._cargar_imagen(img_lanzando)

	def _cargar_imagen(self, nombre_archivo):
		try:
			ruta = asset_path(nombre_archivo)
			img = pygame.image.load(ruta).convert_alpha()

			ratio = img.get_width() / img.get_height()
			alto_px = int(self._long_m_a_px(self.altura_m))
			ancho_px = int(alto_px * ratio)

			return pygame.transform.smoothscale(img, (ancho_px, alto_px))

		except Exception as e:
			print(f"No se pudo cargar la imagen del jugador {nombre_archivo}: {e}")
			return None

	def posicion_balon_en_manos(self):
		if self.lanzando:
			return Vec2d(self.x_m + 0.35, 2.10)

		return Vec2d(self.x_m + 0.13, 2.08)

	def set_lanzando(self, lanzando):
		self.lanzando = lanzando

	def draw(self):
		img = self.img_lanzando if self.lanzando else self.img_preparado

		if img is None:
			return

		x_px = int(self._long_m_a_px(self.x_m))
		y_suelo_px = int(self.sim.suelo)

		rect = img.get_rect(midbottom=(x_px, y_suelo_px))

		self.sim.screen.blit(img, rect)


class Tbasket(Tsim):
	def __init__(self, x_tablero_m=None, mostrar_jugador=True, **kwargs):
		super().__init__(**kwargs)

		self.objetos = []

		if x_tablero_m is None:
			x_tablero_m = (self.width / self.PX_M) - 0.75

		self.v_lanzamiento = 8.5
		self.ang_lanzamiento = 65.0
		self.w_lanzamiento = -10.0

		self.suelo_fisico = Tsuelo(self)
		self.tablero = Ttablero(self, x_tablero_m)

		self.jugador = Tjugador(self, x_m=2.0) if mostrar_jugador else None

		if self.jugador is not None:
			self.pos_inicio_balon = self.jugador.posicion_balon_en_manos()
		else:
			self.pos_inicio_balon = Vec2d(2, 2)

		self.balon = Tbalon(self, self.pos_inicio_balon)

		self.balon_en_manos = True
		self.mantener_balon_en_manos()

		self.add_objeto("suelo", self.suelo_fisico)

		if self.jugador is not None:
			self.add_objeto("jugador", self.jugador)

		self.add_objeto("tablero", self.tablero)
		self.add_objeto("balon", self.balon)

		self.add_evento_tecla(pygame.K_SPACE, self.lanzar_triple)
		self.add_evento_tecla(pygame.K_ESCAPE, self.resetear_posicion)

		self.add_evento_tecla(pygame.K_1, self.toggle_arrastre)
		self.add_evento_tecla(pygame.K_2, self.toggle_crisis)
		self.add_evento_tecla(pygame.K_3, self.toggle_magnus)
		self.add_evento_tecla(pygame.K_4, self.toggle_frenado_rotacional)

	def add_objeto(self, nombre, objeto):
		self.objetos.append({
			"nombre": nombre,
			"objeto": objeto
		})

	def mantener_balon_en_manos(self):
		if self.jugador is None:
			return

		self.balon.posicion = self.jugador.posicion_balon_en_manos()
		self.balon.body.velocity = (0, 0)
		self.balon.body.force = (0, 0)
		self.balon.body.angular_velocity = 0
		self.balon.body.angle = 0

	def lanzar_triple(self):
		print(f"Lanzando a {self.v_lanzamiento} m/s con {self.ang_lanzamiento}º")

		self.balon_en_manos = False

		if self.jugador is not None:
			self.jugador.set_lanzando(True)

		self.balon.lanzar(
			self.v_lanzamiento,
			self.ang_lanzamiento,
			self.w_lanzamiento
		)

		self.set_estado_evento(pygame.K_SPACE, False)

	def configurar_tiro(self, v=None, ang=None, w=None):
		if v is not None:
			self.v_lanzamiento = v

		if ang is not None:
			self.ang_lanzamiento = ang

		if w is not None:
			self.w_lanzamiento = w

	def resetear_posicion(self):
		self.balon_en_manos = True

		if self.jugador is not None:
			self.jugador.set_lanzando(False)

		self.mantener_balon_en_manos()

		self.set_estado_evento(pygame.K_SPACE, True)

	def toggle_arrastre(self):
		self.balon.arrastre_activo = not self.balon.arrastre_activo
		print(f"Arrastre activo: {self.balon.arrastre_activo}")

	def toggle_crisis(self):
		self.balon.crisis_activa = not self.balon.crisis_activa
		print(f"Crisis de arrastre activa: {self.balon.crisis_activa}")

	def toggle_magnus(self):
		self.balon.magnus_activo = not self.balon.magnus_activo
		print(f"Magnus activo: {self.balon.magnus_activo}")

	def toggle_frenado_rotacional(self):
		self.balon.frenado_rotacional_activo = not self.balon.frenado_rotacional_activo
		print(f"Frenado rotacional activo: {self.balon.frenado_rotacional_activo}")

	def actualizar_fisica_extra(self):
		if self.balon_en_manos:
			self.mantener_balon_en_manos()
		else:
			self.balon.aplicar_aerodinamica()

		self.tablero.actualizar_red()

	def draw_hud(self):
		lineas = [
			"ESPACIO: lanzar | ESC: reset",
			"1: arrastre | 2: crisis | 3: Magnus | 4: frenado rotacional",
			f"Arrastre: {self.balon.arrastre_activo}",
			f"Crisis arrastre: {self.balon.crisis_activa}",
			f"Magnus: {self.balon.magnus_activo}",
			f"Frenado rotacional: {self.balon.frenado_rotacional_activo}",
			f"v tiro: {self.v_lanzamiento:.2f} m/s",
			f"angulo: {self.ang_lanzamiento:.2f} grados",
			f"omega: {self.w_lanzamiento:.2f} rad/s",
			f"Re: {self.balon.ultimo_reynolds:.0f}",
			f"Cd usado: {self.balon.ultimo_Cd:.3f}",
			f"Cd sin crisis: {self.balon.ultimo_Cd_sin_crisis:.3f}",
			f"Cd con crisis: {self.balon.ultimo_Cd_con_crisis:.3f}",
			f"Cm Magnus: {self.balon.ultimo_Cm:.3f}",
			f"S spin: {self.balon.ultimo_S:.3f}",
			f"tau rot: {self.balon.ultimo_tau_rot:.6f}",
		]

		x = 10
		y = 10

		for linea in lineas:
			txt = self.font.render(linea, True, (0, 0, 0))
			self.screen.blit(txt, (x, y))
			y += 18

	def draw(self):
		if self.balon_en_manos:
			self.mantener_balon_en_manos()

		super().draw()

		for item in self.objetos:
			objeto = item["objeto"]

			if hasattr(objeto, "draw"):
				objeto.draw()

		self.draw_hud()


if __name__ == "__main__":
	bk = Tbasket(
		x_tablero_m=None,
		PX_M=120,
		width=1000,
		height=600,
		suelo=600,
		gravedad=(0, -9.81),
		fondo="grada_baloncesto03.jpg"
	)

	bk.configurar_tiro(8.5, 65, -10)

	FPS = 60
	substeps = 30
	dt = 1.0 / FPS / substeps

	while bk.actualizar_eventos():
		for _ in range(substeps):
			bk.actualizar_fisica_extra()
			bk.space.step(dt)

		bk.draw()
		pygame.display.flip()
		bk.clock.tick(FPS)

	pygame.quit()