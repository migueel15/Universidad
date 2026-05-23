import pygame
import pymunk
import pymunk.pygame_util
import math
from pymunk import Vec2d
import numpy as np
import os
from rozamiento_aire import aplicar_newton, aplicar_magnus, get_Cd
import random

################################# SIMULACIÓN GENERICA ##############################################
####################################################################################################
class Tsim:
	def __init__(self,width=1000,height=600,suelo=600,PX_M=1,gravedad=(0,-9.81),fondo=None):
		self.width=width
		self.height=height
		self.PX_M=PX_M
		self.M_PX=1.0/PX_M
		self.suelo=	suelo
		#inicia pygame
		pygame.init()
		self.screen = pygame.display.set_mode((self.width, self.height))
		self.clock = pygame.time.Clock()
		#intenta poner el fondo
		if fondo!=None: 
			self.fondo=self.pone_fondo(fondo)
		else:
			self.fondo=None	
		#crea el espacio	
		self.space = pymunk.Space()
		self.space.gravity = Vec2d(gravedad[0],-gravedad[1])* PX_M
		self.space.iterations = 35 # Aumentamos iteraciones para mayor estabilidad	
		
		# Diccionario: {tecla: {'func': funcion, 'activo': bool}}
		self._eventos_teclado = {}
		self.running = True
		
		
	#-------- pone imagen de fondo ----------------------------------	
	def pone_fondo(self, imagen):
		if imagen is None:
			self.fondo = None
			return None
		
		try:
			# Intentar cargar desde la ruta dada (que puede ser absoluta)
			fondo = pygame.image.load(imagen).convert()
			fondo = pygame.transform.smoothscale(fondo, (self.width, self.height))
		except:
			# Si falla, intentar construir la ruta basándose en este archivo
			try:
				script_dir = os.path.dirname(os.path.abspath(__file__))
				full_path = os.path.join(script_dir, imagen)
				fondo = pygame.image.load(full_path).convert()
				fondo = pygame.transform.smoothscale(fondo, (self.width, self.height))
			except:
				fondo = None
		
		self.fondo = fondo
		return fondo	
		
	#----------------------------------------------------------------------	
	def draw(self):
		if self.fondo: self.screen.blit(self.fondo, (0, 0))
		else: self.screen.fill((192, 192, 192))	
	#----------------------------------------------------------------------	
		
	############### eventos ############################################	
	def add_evento_tecla(self, tecla, funcion, activo=True):
		"""Registra una tecla con su función y estado inicial."""
		self._eventos_teclado[tecla] = {
			'func': funcion,
			'activo': activo}
	#-----------------------  on  / off------------------------------
	def set_estado_evento(self, tecla, estado):
		"""Activa o desactiva un evento específico."""
		if tecla in self._eventos_teclado:
			self._eventos_teclado[tecla]['activo'] = estado
	#------------------- manejador de eventos ----------------------
	def actualizar_eventos(self):
		"""Procesa eventos y ejecuta solo los que están activos."""
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				self.running = False
				return False
			
			if event.type == pygame.KEYDOWN:
				if event.key in self._eventos_teclado:
					evento = self._eventos_teclado[event.key]
					if evento['activo']:
						evento['func']()							
		return True
	#----------------------------------------------------------------	
	
##############################################################################################
##############################################################################################	



###############################################################################################
#----------- Un objeto genérico al que se le puede mandar y extraer la velocidad y ------------
#----------- posición en metros, y ya se encarga de hacer todos los cambios dentro ------------
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

	# Getters y Setters
	# El getter devuelve de píxeles a metros(internamente está en píxeles pero nosostros entendemos los metros)
	# El setter establece en pixeles los metros que tu le pases
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

#---------------- suelo, como es estático no se puede modificar su velocidad o ----------
#---------------- o posición                                                   ----------  
class Tsuelo(Tobjeto):
	def __init__(self, sim, punto_a_m=None, punto_b_m=None, color=(0,0,0)):
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

		self.shape = pymunk.Segment(self.body, self.p1, self.p2,0)
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
		pygame.draw.line(
			self.sim.screen, 
			self.color, 
			self.p1, 
			self.p2, 
			1)

###############################################################################################
class Tbalon(Tobjeto):
	def __init__(self, sim, pos_m, masa_kg=0.625, radio_m=0.119, img_path=None):
		#sim es la simulación a la que pertenece, puede ser un Tsim o Tbasket
		#self.sim = sim
		super().__init__(sim)
		self.radio_m = radio_m
		self.radio_px = radio_m * self.sim.PX_M
		self.masa_kg = masa_kg
		
		# 1. Física
		moment = pymunk.moment_for_circle(masa_kg, 0, self.radio_px)
		self.body = pymunk.Body(masa_kg, moment)
		self.posicion = pos_m
		
		# Flag para indicar si la pelota está en manos del jugador
		self.en_manos_jugador = True

		self.shape = pymunk.Circle(self.body, self.radio_px)
		self.shape.elasticity = 0.85
		self.shape.friction = 0.5
		self.sim.space.add(self.body, self.shape)
		
		# 2. Parámetros para el arrastre y Magnus (baloncesto)
		self.diametro_m = 2 * radio_m  # ~0.238 m
		self.area_m2 = np.pi * (radio_m ** 2)  # Área de la sección transversal
		self.Cd = get_Cd(10, self.diametro_m)  # Cd inicial (sera actualizado cada frame)
		self.k_magnus = 0.7  # Coeficiente de Magnus para baloncesto
		self.aplicar_fuerzas = True  # Flag para activar/desactivar fuerzas
		
		# 3. Imagen base (escalada al diámetro en píxeles)
		self.img_base = self._preparar_imagen(img_path)
	#-----------------------------------------------------------
	def _preparar_imagen(self, path):
		if path is None:
			# Si no hay imagen, creamos un círculo rojo con una línea para ver la rotación
			diametro = int(self.radio_px * 2)
			surf = pygame.Surface((diametro, diametro), pygame.SRCALPHA)
			pygame.draw.circle(surf, (200, 50, 50), (int(self.radio_px), int(self.radio_px)), int(self.radio_px))
			pygame.draw.line(surf, (255, 255, 255), (int(self.radio_px), int(self.radio_px)), (diametro, int(self.radio_px)), 2)
			return surf
		
		diametro = int(self.radio_px * 2)
		# Construir ruta absoluta basada en la ubicación del script actual
		script_dir = os.path.dirname(os.path.abspath(__file__))
		full_path = os.path.join(script_dir, path)
		
		try:
			# Cargamos y escalamos una sola vez para ahorrar CPU
			img = pygame.image.load(full_path).convert_alpha()  #convert_alpha para los png transparentes
			return pygame.transform.smoothscale(img, (diametro, diametro))  #suaviza los bordes
		except:
			# Si no hay imagen, creamos un círculo rojo con una línea para ver la rotación
			surf = pygame.Surface((diametro, diametro), pygame.SRCALPHA)
			pygame.draw.circle(surf, (200, 50, 50), (int(self.radio_px), int(self.radio_px)), int(self.radio_px))
			pygame.draw.line(surf, (255, 255, 255), (int(self.radio_px), int(self.radio_px)), (diametro, int(self.radio_px)), 2)
			return surf
	#---------------------------------------------------------------
	def actualizar_fuerzas(self):
		"""
		PUNTO 1: Aplicar rozamiento del aire y efecto Magnus
		Se llama en cada frame para actualizar las fuerzas aerodinámicas
		"""
		# Si está en manos del jugador, no aplicar fuerzas
		if self.en_manos_jugador:
			self.velocidad = (0, 0)  # Mantenerla estática
			return
		
		# Sólo se le aplicará la fuerza cuando esté en el aire
		if not self.aplicar_fuerzas:
			return
		
		# Velocidad actual en m/s
		v_actual_ms = self.velocidad.length
		
		# Aunque no es necesario, podriamos usar uno básico como 0.47
		# Actualizar Cd según velocidad actual (para más realismo)
		if v_actual_ms > 0.1:
			self.Cd = get_Cd(v_actual_ms, self.diametro_m)
		
		# Aplicar fuerza de arrastre (drag) - resistencia del aire
		aplicar_newton(
			self.body,
			AREA_M2=self.area_m2,
			M_PX=self.sim.M_PX,
			Cd=self.Cd,
			alt_m=0,
			v_viento=[0, 0],
			CORRECT_RHO=False,
			MACH=False,
			offset=(0, 0)
		)
		
		# Aplicar efecto Magnus (desviación causada por la rotación)
		aplicar_magnus(
			self.body,
			AREA_M2=self.area_m2,
			M_PX=self.sim.M_PX,
			k=self.k_magnus,
			v_viento=[0, 0],
			offset=(0, 0)
		)
	#---------------------------------------------------------------
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
	#---------------------------------------------------------------
	def lanzar(self, v_ms, angulo_deg, omega=0):  
		"""Método auxiliar para aplicar el impulso inicial.
		se le pasa velocidad, angulo con la horizontal y
		velocidad angular en rad/s (negativo es backspin"""
		rad = math.radians(angulo_deg)

		# Aplicando velocidad.setter (aprovechando Tobjeto)
		vx = v_ms * math.cos(rad)
		vy = v_ms * math.sin(rad)
		self.velocidad = (vx, vy)
		self.body.angular_velocity = omega # efecto de rotacion
		self.en_manos_jugador = False  # La pelota sale de las manos

##############################################################################

#----------- PUNTO 3: CLASE PARA LA RED ----------

class Tred(Tobjeto):
	"""
	Red colgante debajo del aro, con cadenas de bolitas.
	Las bolitas están conectadas con PinJoints y los dos lados
	están conectados con muelles (DampedSpring) para simular la red.
	
	MEJORADO: Las longitudes de muelles se generan dinámicamente.
	"""
	def __init__(self, sim, x_aro_delantero_px, x_aro_trasero_px, y_aro_px, 
	             radio_bolita_m=0.015, masa_bolita_kg=0.01, num_nodos=8,
	             stiffness=65.0, damping=3.5):
		super().__init__(sim)
		
		self.x_aro_delantero = x_aro_delantero_px
		self.x_aro_trasero = x_aro_trasero_px
		self.y_aro = y_aro_px
		self.radio_bolita_m = radio_bolita_m
		self.radio_bolita_px = radio_bolita_m * sim.PX_M
		self.masa_bolita = masa_bolita_kg
		self.num_nodos = num_nodos
		self.stiffness = stiffness
		self.damping = damping
		
		# Se asume que self.body no es crítico en este contexto (es más visual que físico)
		self.body = self.sim.space.static_body
		
		# Crear cadenas de bolitas
		self.cadenas_bolitas = []
		
		for x_aro_anclaje in [self.x_aro_delantero, self.x_aro_trasero]:
			cadena_actual = []
			cuerpo_padre_posicion = (x_aro_anclaje, self.y_aro)
			
			for i in range(num_nodos):
				# Crear cuerpo y shape para cada bolita
				momento = pymunk.moment_for_circle(masa_bolita_kg, 0, self.radio_bolita_px)
				b_hijo = pymunk.Body(masa_bolita_kg, momento)
				
				# Posicionar debajo del anterior
				distancia = 0.10 * sim.PX_M  # Separación vertical entre nodos
				b_hijo.position = (cuerpo_padre_posicion[0], cuerpo_padre_posicion[1] + distancia)
				
				s_hijo = pymunk.Circle(b_hijo, self.radio_bolita_px)
				s_hijo.elasticity = 0.65
				s_hijo.friction = 0.75
				
				# Conectar con el anterior usando PinJoint
				if i == 0:
					# El primer nodo se conecta al aro (body estático)
					anclaje_body = self.sim.space.static_body
					anclaje_pos = cuerpo_padre_posicion
					pin = pymunk.PinJoint(anclaje_body, b_hijo, anclaje_pos, (0, 0))
				else:
					pin = pymunk.PinJoint(cadena_actual[-1], b_hijo, (0, 0), (0, 0))
				
				self.sim.space.add(b_hijo, s_hijo, pin)
				cadena_actual.append(b_hijo)
				cuerpo_padre_posicion = b_hijo.position
			
			self.cadenas_bolitas.append(cadena_actual)
		
		# ===== MEJORA: Generar longitudes de muelles dinámicamente =====
		# Interpolación lineal: primer nodo más separado, último más cerca
		longitud_max = 0.40 * sim.PX_M  # Longitud máxima (nodo 0)
		longitud_min = 0.15 * sim.PX_M  # Longitud mínima (último nodo)
		
		# Generar longitudes interpoladas para cada nodo
		longitudes_muelles = []
		for i in range(num_nodos):
			# Interpolación lineal: de longitud_max a longitud_min
			ratio = i / (num_nodos - 1) if num_nodos > 1 else 0
			longitud = longitud_max + (longitud_min - longitud_max) * ratio
			longitudes_muelles.append(longitud)
		
		print(f"[RED] {num_nodos} nodos | Longitudes muelles: {[f'{l/sim.PX_M:.3f}m' for l in longitudes_muelles]}")
		
		# Conectar las dos cadenas con muelles para formar la red
		for i in range(num_nodos):
			b_izq = self.cadenas_bolitas[0][i]
			b_der = self.cadenas_bolitas[1][i]
			
			muelle = pymunk.DampedSpring(
				b_izq, b_der,
				(0, 0), (0, 0),
				longitudes_muelles[i],  # AHORA ESCALABLE
				self.stiffness,
				self.damping
			)
			self.sim.space.add(muelle)
	
	def actualizar(self):
		"""Aplicar amortiguamiento a las bolitas"""
		factor_amort = 0.94  # Factor de amortiguamiento por frame
		for cadena in self.cadenas_bolitas:
			for bolita in cadena:
				bolita.velocity *= factor_amort
	
	def draw(self):
		"""Dibujar la red: líneas entre nodos y bolitas"""
		for idx_cadena, cadena in enumerate(self.cadenas_bolitas):
			# Determinar el anclaje (aro)
			x_anclaje = self.x_aro_trasero if idx_cadena == 0 else self.x_aro_delantero
			
			# Primera conexión: aro a primer nodo
			if len(cadena) > 0:
				pygame.draw.line(
					self.sim.screen,
					(255, 255, 255),
					(int(x_anclaje), int(self.y_aro)),
					(int(cadena[0].position.x), int(cadena[0].position.y)),
					1
				)
			
			# Líneas entre nodos de la misma cadena
			for i in range(len(cadena) - 1):
				pygame.draw.line(
					self.sim.screen,
					(255, 255, 255),
					(int(cadena[i].position.x), int(cadena[i].position.y)),
					(int(cadena[i+1].position.x), int(cadena[i+1].position.y)),
					1
				)
		
		# Líneas transversales (conectando cadena izq con derecha) para formar la red
		bolas_izq = self.cadenas_bolitas[0]
		bolas_der = self.cadenas_bolitas[1]
		
		for i in range(len(bolas_izq)):
			pygame.draw.line(
				self.sim.screen,
				(200, 200, 200),
				(int(bolas_izq[i].position.x), int(bolas_izq[i].position.y)),
				(int(bolas_der[i].position.x), int(bolas_der[i].position.y)),
				1
			)
		
		# Dibujar las bolitas
		for cadena in self.cadenas_bolitas:
			for bolita in cadena:
				pygame.draw.circle(
					self.sim.screen,
					(255, 255, 255),
					(int(bolita.position.x), int(bolita.position.y)),
					int(self.radio_bolita_px)
				)

##############################################################################

class Ttablero(Tobjeto):
	def __init__(self, sim, x_tablero_m=7, color=(100, 100, 100)):
		super().__init__(sim)
		self.color = color
		
		# PUNTO 2: Usando _m_a_px de Tobjeto para conversión de unidades
		self.espesor_m = 0.05
		self.alto_m = 1.05
		self.espesor = self.espesor_m * sim.PX_M
		self.alto = self.alto_m * sim.PX_M
		
		self.pos = self._m_a_px(Vec2d(x_tablero_m, 2.90))
		
		p0 = Vec2d(self.pos.x,             self.pos.y - self.alto)  #arriba izq
		p1 = Vec2d(self.pos.x + self.espesor, self.pos.y - self.alto)  #arriba der
		p2 = Vec2d(self.pos.x + self.espesor, self.pos.y)  #abajo der
		p3 = Vec2d(self.pos.x,             self.pos.y)  #arriba der
		
		self.body = self.sim.space.static_body
		self.shape = pymunk.Poly(self.body, [p0, p1, p2, p3])
		self.shape.elasticity = 0.8
		self.shape.friction = 0.6
		self.sim.space.add(self.shape)
		
		#------
		# Aro Físico (también usando _m_a_px)
		_, self.aro_y = self._m_a_px(Vec2d(0, 3.05))
		diametro_aro = 0.45 * sim.PX_M
		self.x_aro_trasero = self.pos.x - (0.15 * sim.PX_M)
		self.x_aro_delantero = self.x_aro_trasero - diametro_aro
		
		self.aro_part_w = 0.04 * sim.PX_M 
		self.aro_part_h = 0.02 * sim.PX_M
		self.x_aro_sop = self.x_aro_trasero + self.aro_part_w

		# Aro Trasero
		self.body_trasero = pymunk.Body(body_type=pymunk.Body.STATIC)
		self.body_trasero.position = (self.x_aro_trasero, self.aro_y)
		self.aro_trasero_shape = pymunk.Poly.create_box(self.body_trasero, (self.aro_part_w, self.aro_part_h))
		
		# Aro Delantero
		self.body_delantero = pymunk.Body(body_type=pymunk.Body.STATIC)
		self.body_delantero.position = (self.x_aro_delantero, self.aro_y)
		self.aro_delantero_shape = pymunk.Poly.create_box(self.body_delantero, (self.aro_part_w, self.aro_part_h))
		
		# Soporte aro
		self.body_sop = pymunk.Body(body_type=pymunk.Body.STATIC)
		self.body_sop.position = (self.x_aro_sop, self.aro_y)
		self.aro_sop_shape = pymunk.Poly.create_box(self.body_sop, (self.aro_part_w * 2, self.aro_part_h))
		
		self.sim.space.add(self.body_trasero, self.aro_trasero_shape)
		self.sim.space.add(self.body_delantero, self.aro_delantero_shape)
		self.sim.space.add(self.body_sop, self.aro_sop_shape)

	def draw(self):
		pygame.draw.rect(self.sim.screen, self.color, (self.pos.x, self.pos.y - self.alto, self.espesor, self.alto))	
		color_sop = (255, 150, 150)
		sopx = 0.10 * self.sim.PX_M
		sopy = 0.40 * self.sim.PX_M
		barrax = 0.05 * self.sim.PX_M
		barray = self.sim.height
		
		pygame.draw.rect(self.sim.screen, color_sop, (
			self.pos.x + self.espesor,
			self.pos.y - 0.6 * self.alto,
			sopx,
			sopy))	
		
		pygame.draw.rect(self.sim.screen, color_sop, (
			self.pos.x + self.espesor + sopx,
			0,
			barrax,
			self.pos.y - 0.4 * self.alto))	
		
		#ARO
		color_aro_suave = (255, 150, 150)
		pygame.draw.line(self.sim.screen, color_aro_suave, (int(self.x_aro_delantero), int(self.aro_y)), (int(self.x_aro_trasero), int(self.aro_y)), 3)

		for b in [self.body_trasero, self.body_delantero]:
			pygame.draw.rect(self.sim.screen, (200, 0, 0), (int(b.position.x - self.aro_part_w/2), int(b.position.y - self.aro_part_h/2), int(self.aro_part_w), int(self.aro_part_h)))
		
		pygame.draw.rect(self.sim.screen, (200, 0, 0), (self.x_aro_sop, 
		                                                int(self.aro_y - self.aro_part_h/2), 
		                                                self.pos.x - self.x_aro_sop,
		                                                int(self.aro_part_h)))

##############################################################################

#----------- PUNTO 5: CLASE PARA EL JUGADOR ----------
class Tjugador(Tobjeto):
	"""
	Representación visual del jugador en la cancha.
	"""
	def __init__(self, sim, pos_m=(2, 0), img_paths=None, altura_m=2.0):
		super().__init__(sim)
		
		self.pos_m = pos_m
		self.altura_m = altura_m
		self.altura_px = altura_m * sim.PX_M
		
		# Imágenes del jugador (antes y después del lanzamiento)
		self.img_reposo = None
		self.img_lanzando = None
		
		if img_paths:
			self.img_reposo = self._cargar_imagen(img_paths[0])
			self.img_lanzando = self._cargar_imagen(img_paths[1])
		
		# Estado
		self.lanzando = False
		self.body = self.sim.space.static_body  # El jugador no tiene física
	
	def _cargar_imagen(self, path):
		if path is None:
			return None
		
		# Construir ruta absoluta basada en la ubicación del script actual
		script_dir = os.path.dirname(os.path.abspath(__file__))
		full_path = os.path.join(script_dir, path)
		
		try:
			img = pygame.image.load(full_path).convert_alpha()
			ratio = img.get_width() / img.get_height()
			img_escalada = pygame.transform.smoothscale(img, (int(self.altura_px * ratio), int(self.altura_px)))
			return img_escalada
		except:
			return None
	
	def set_estado(self, lanzando=False):
		self.lanzando = lanzando
	
	def draw(self):
		pos_px = self._m_a_px(self.pos_m)
		
		# Elegir imagen según estado
		img = self.img_lanzando if self.lanzando else self.img_reposo
		
		if img:
			rect = img.get_rect(midbottom=(int(pos_px.x), int(pos_px.y)))
			self.sim.screen.blit(img, rect)
		else:
			# Dibujar figura simple si no hay imagen
			pygame.draw.circle(self.sim.screen, (100, 100, 200), (int(pos_px.x), int(pos_px.y - self.altura_px/2)), int(self.altura_px/4))
			pygame.draw.line(self.sim.screen, (100, 100, 200), (int(pos_px.x), int(pos_px.y - self.altura_px/2)), (int(pos_px.x), int(pos_px.y)), 2)

##############################################################################

########## ESTA ES LA CLASE QUE LO CONTIENE TODO ############################################
############################ específica para baloncesto ######################################
class Tbasket(Tsim):
	def __init__(self, x_tablero_m=7, pos_balon_m=(1, 2), **kwargs):
		super().__init__(**kwargs)
		self.pos_inicio_balon = pos_balon_m
	
		# Parámetros de lanzamiento por defecto
		self.v_lanzamiento = 9.0    # m/s
		self.ang_lanzamiento = 55.0  # grados
		self.w_lanzamiento = -15.0   # rad/s (backspin)
		
		# NUEVO
		# PUNTO 4: Diccionario de objetos para dibujado automático
		self.objetos = {}
		
		# Crear objetos
		self.objetos['suelo'] = Tsuelo(self)
		self.objetos['balon'] = Tbalon(self, self.pos_inicio_balon, img_path='balon_basket.png')
		self.objetos['tablero'] = Ttablero(self, x_tablero_m)
		
		# PUNTO 3: Crear la red CON PARÁMETROS ESCALABLES
		self.objetos['red'] = Tred(
			self,
			self.objetos['tablero'].x_aro_delantero,
			self.objetos['tablero'].x_aro_trasero,
			self.objetos['tablero'].aro_y,
			radio_bolita_m=0.015,      # Cuerdas más finas
			masa_bolita_kg=0.01,       # Masa reducida
			num_nodos=6,               # MÁS NODOS = MÁS DENSO
			stiffness=65.0,            # Rigidez moderada
			damping=3.5                # Amortiguamiento moderado-alto
		)
		
		# PUNTO 5: Crear jugador
		self.objetos['jugador'] = Tjugador(self, pos_m=(2, 0), img_paths=['jugador01.png', 'jugador02.png'], altura_m=2.0)
		
		#teclas
		self.add_evento_tecla(pygame.K_SPACE, self.lanzar_triple)
		self.add_evento_tecla(pygame.K_ESCAPE, self.resetear_posicion)

	def lanzar_triple(self):
		# Ahora usamos las variables de la instancia
		print(f"Lanzando a {self.v_lanzamiento} m/s con {self.ang_lanzamiento}º")
		self.objetos['balon'].lanzar(self.v_lanzamiento, self.ang_lanzamiento, self.w_lanzamiento)
		self.objetos['balon'].aplicar_fuerzas = True  # Activar fuerzas aerodinámicas
		self.objetos['jugador'].set_estado(lanzando=True)  # Cambiar estado del jugador
		self.set_estado_evento(pygame.K_SPACE, False)

	def configurar_tiro(self, v=None, ang=None, w=None):
		"""Método para cambiar los parámetros desde fuera"""
		if v is not None: self.v_lanzamiento = v
		if ang is not None: self.ang_lanzamiento = ang
		if w is not None: self.w_lanzamiento = w

	def resetear_posicion(self):
		self.objetos['balon'].posicion = self.pos_inicio_balon
		self.objetos['balon'].velocidad = (0, 0)
		self.objetos['balon'].body.angular_velocity = 0
		self.objetos['balon'].aplicar_fuerzas = False  # Desactivar fuerzas
		self.objetos['balon'].en_manos_jugador = True  # Pelota en manos del jugador
		self.objetos['jugador'].set_estado(lanzando=False)  # Volver al reposo
		self.set_estado_evento(pygame.K_SPACE, True)

	# PUNTO 4: Rediseñar dibujar_todo como draw() con herencia y diccionario
	def draw(self):
		"""
		Método draw heredado y extendido.
		Primero llama al draw de la clase padre (para el fondo)
		Luego dibuja todos los objetos del diccionario de forma automática
		"""
		super().draw()  # Dibuja el fondo
		
		# Recorrer el diccionario de objetos y llamar al draw de cada uno
		for nombre, objeto in self.objetos.items():
			if hasattr(objeto, 'draw'):
				objeto.draw()
	
	def actualizar_fisicas(self):
		"""
		Actualizar fuerzas y física de los objetos
		Se llama en cada frame del loop principal
		"""
		# Actualizar fuerzas aerodinámicas del balón
		self.objetos['balon'].actualizar_fuerzas()
		
		# Si la pelota está en manos del jugador, actualizamos su posición
		# para que siga al jugador (un poco arriba)
		if self.objetos['balon'].en_manos_jugador:
			# Posición del jugador más un offset vertical
			pos_jugador = self.objetos['jugador'].pos_m
			offset_vertical = 2  # Medio metro por encima de la posición del jugador
			self.objetos['balon'].posicion = (pos_jugador[0], pos_jugador[1] + offset_vertical)
			self.objetos['balon'].velocidad = (0, 0)  # Asegurar velocidad nula
		
		# Actualizar amortiguamiento de la red
		self.objetos['red'].actualizar()

#-------------------------------------------------------------------------------------------




###############################################################################################
# PROGRAMA PRINCIPAL
###############################################################################################

# Obtener el directorio del script para rutas relativas de imágenes
script_dir = os.path.dirname(os.path.abspath(__file__))

bk = Tbasket(
	x_tablero_m=7,
	pos_balon_m=(2, 2),
	PX_M=120,
	width=1000,
	height=600,
	suelo=600,
	gravedad=(0, -9.81),
	fondo=os.path.join(script_dir, 'grada_baloncesto03.jpg')
)
bk.configurar_tiro(8, 58, -10)

#########################################################################
# PRUEBAS: Cambiar num_nodos aquí para ver diferentes densidades
# num_nodos=3  → Red básica (original)
# num_nodos=5  → Red más densa
# num_nodos=8  → Red muy densa (muy realista)
#########################################################################

FPS = 60
substeps = 30
dt = 1.0 / FPS / substeps

while bk.actualizar_eventos():
	# Actualizar física
	bk.actualizar_fisicas()
	
	# Pasos de simulación de pymunk
	for _ in range(substeps):
		bk.space.step(dt)
	
	# Dibujar todo (llamando al draw rediseñado)
	bk.draw()
	
	pygame.display.flip()
	bk.clock.tick(FPS)

pygame.quit()
