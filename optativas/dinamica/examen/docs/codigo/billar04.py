import pymunk
import pygame
from pymunk import Vec2d
import math
import numpy as np

# --- Configuración de Dimensiones Reales (metros) ---
MESA_W_M = 2.115
MESA_H_M = 1.205
SCREEN_WIDTH = 1400
PX_M = SCREEN_WIDTH / MESA_W_M
M_PX = 1 / PX_M
SCREEN_HEIGHT = int(MESA_H_M * PX_M)

G_GRAVITY = 9.8      
ROZ_BOLA_BOLA = 0.055  
ROZ_BOLA_BAND_ = 0.40   
FRIC_BOLA = ROZ_BOLA_BOLA

if FRIC_BOLA > 0:
	FRIC_BANDA = (ROZ_BOLA_BAND_ ** 2) / FRIC_BOLA
else:
	FRIC_BANDA = 0.0

OFFSET_Y = 75  # Espacio para el marcador arriba
SCREEN_HEIGHT = int(MESA_H_M * PX_M) + OFFSET_Y
def to_pygame(point_m):
	# Sumamos el offset solo al dibujar en pantalla
	return int(point_m[0] * PX_M), int(point_m[1] * PX_M) + OFFSET_Y
	

#######################################################################################
############################ clase para la mesa de billar #############################
#######################################################################################	
class BilliardTable:
	def __init__(self, space, width, height, margin=0.05):
		self.space = space
		self.width = width
		self.height = height
		self.margin = margin
		self.mu_sliding = 0.25
		self.crr_rolling = 0.01
		self.pocket_radius = 0.06 
		self.wall_elasticity = 0.5
		self.wall_friction = FRIC_BANDA
		
		self.pockets = [
			((self.margin, self.margin), 0.0585),
			((self.width / 2, self.margin), 0.065),
			((self.width - self.margin, self.margin), 0.0585),
			((self.margin, self.height - self.margin), 0.0585),
			((self.width / 2, self.height - self.margin), 0.065),
			((self.width - self.margin, self.height - self.margin), 0.0585)]
		
		self._create_walls()

	#------------------------------------------------------------
	def _create_walls(self):
		static_body = self.space.static_body
		m = self.margin
		w = self.width
		h = self.height
		rc = 0.0585
		rs = 0.065
		
		walls_data = [
			((m + rc, m), (w / 2 - rs, m)),
			((w / 2 + rs, m), (w - m - rc, m)),
			((m + rc, h - m), (w / 2 - rs, h - m)),
			((w / 2 + rs, h - m), (w - m - rc, h - m)),
			((m, m + rc), (m, h - m - rc)),
			((w - m, m + rc), (w - m, h - m - rc))
		]
		
		self.walls = []
		for start, end in walls_data:
			wall = pymunk.Segment(static_body, start, end, 0.01)
			wall.elasticity = self.wall_elasticity
			wall.friction = self.wall_friction
			self.space.add(wall)
			self.walls.append(wall)
	#---------------------------------------------------------------------		
		 

	#--------------------------------------------------------------------
	def draw(self, screen):
		color_madera = (100, 50, 20)
		color_tapete = (34, 139, 34)
		# Dibujar fondo del marcador (negro o gris oscuro)
		pygame.draw.rect(screen, (20, 20, 20), (0, 0, SCREEN_WIDTH, OFFSET_Y-15))
		# Dibujar estructura de madera
		pygame.draw.rect(screen, color_madera, (0, OFFSET_Y-15, SCREEN_WIDTH, SCREEN_HEIGHT - OFFSET_Y+15))
		# Dibujar el tapete (superficie de juego)
		top_left = to_pygame((self.margin, self.margin))
		tapete_w_m = self.width - 2 * self.margin
		tapete_h_m = self.height - 2 * self.margin
		tapete_w_px = int(tapete_w_m * PX_M)
		tapete_h_px = int(tapete_h_m * PX_M)
		pygame.draw.rect(screen, color_tapete, (*top_left, tapete_w_px, tapete_h_px))
		# --- Dibujar Puntos de Señalización (Oficiales) ---
		# Los puntos se sitúan a 1/4 y 3/4 de la longitud del área de juego
		spot_radius = max(2, int(0.01 * PX_M))
		y_center = self.height / 2
		
		# Punto de salida (Head Spot) - donde se coloca la blanca
		head_spot_x = self.margin + (tapete_w_m / 4)
		pygame.draw.circle(screen, (0, 0, 0), to_pygame((head_spot_x, y_center)), spot_radius)
		
		# Punto de pie (Foot Spot) - centro del triángulo
		foot_spot_x = self.margin + (3 * tapete_w_m / 4)
		pygame.draw.circle(screen, (0, 0, 0), to_pygame((foot_spot_x, y_center)), spot_radius)

		# Dibujar troneras
		for pos, radius in self.pockets:
			pygame.draw.circle(screen, (0, 0, 0), to_pygame(pos), int(radius * PX_M))
			
		# Dibujar bandas
		for wall in self.walls:
			p1 = to_pygame(wall.a)
			p2 = to_pygame(wall.b)
			pygame.draw.line(screen, (139, 69, 19), p1, p2, max(2, int(0.015 * PX_M)))
################################################################################################
################################################################################################


################################################################################################
################################# clase generica para las bolas ################################
################################################################################################
class Ball:
	def __init__(self, space, pos, color, number=None, is_cue=False, punto=False):
		self.space = space
		self.radius = 0.0285 
		self.color = color
		self.number = number
		self.is_cue = is_cue
		self.in_pocket = False
		self.punto=punto
		
		mass = 0.17 
		moment = pymunk.moment_for_circle(mass, 0, self.radius)
		self.body = pymunk.Body(mass, moment)
		self.body.position = pos
		
		self.shape = pymunk.Circle(self.body, self.radius)
		self.shape.elasticity = 0.8
		self.shape.friction = FRIC_BOLA
		self.shape.collision_type = 1
		self.shape.parent = self
		
		self.mu_sliding = 0.015
		self.crr_rolling = 0.01
		self.is_rolling = False
		self.v_check = 0.0 
		
		self.space.add(self.body, self.shape)
		
		# Fuente para el número
		self.font = pygame.font.SysFont("Arial", int(self.radius * PX_M * 0.8), bold=True)
	#-------------------------------------------------
	def update(self, pockets):
		if self.in_pocket: return
		for p_pos, p_radius in pockets:
			dist = self.body.position.get_distance(p_pos)
			# La bola cae solo si está totalmente contenida en el radio de la tronera
			if dist  < p_radius:
				self.fall_into_pocket()
				break

	#---------------------------------------------------
	def fall_into_pocket(self):
		self.in_pocket = True
		self.space.remove(self.body, self.shape)
	#--------------------------------------------------	


	#-------------------- FISICA ----------------------------
	def apply_impulse(self, velocity_vector):
		impulse_vector = self.body.mass * Vec2d(*velocity_vector)
		self.is_rolling = False
		self.body.apply_impulse_at_world_point(impulse_vector,self.body.position)
		self.v_check = self.body.velocity.length
	#-------------------------------------------------------	

    #-------------------- FISICA --------------------------
	def apply_advanced_friction(self, dt):
		v = self.body.velocity
		speed = v.length
		
		if abs(self.body.angular_velocity) > 0.1:
			self.body.angular_velocity *= math.pow(0.6, dt)
		else:    
			self.body.angular_velocity = 0
		
		if speed < 0.005:
			self.body.velocity = (0, 0)
			return

		if not self.is_rolling and speed <= (5/7) * self.v_check:
			self.is_rolling = True

		fn = self.body.mass * G_GRAVITY
		mu = self.crr_rolling if self.is_rolling else self.mu_sliding
		f_mag = mu * fn
		f_stop = (speed * self.body.mass) / dt
		f_final = min(f_mag, f_stop)
		self.body.apply_force_at_world_point(-v.normalized() * f_final, self.body.position)
		
	#---------------------------------------------------------------------------------	

	def _draw_number(self, screen, pos_px, rad_px):
		"""Dibuja el número rotando con la bola."""
		if self.number is None: return
		
		# Ángulo de la física (Pymunk)
		angle = self.body.angle
		
		# Círculo blanco central
		white_rad = rad_px // 2
		pygame.draw.circle(screen, (255, 255, 255), pos_px, white_rad)
		
		# Texto del número rotado
		# Usamos -math.degrees porque Pygame rota en sentido horario y Pymunk antihorario
		text_surf = self.font.render(str(self.number), True, (0, 0, 0))
		rotated_text = pygame.transform.rotate(text_surf, -math.degrees(angle))
		text_rect = rotated_text.get_rect(center=pos_px)
		screen.blit(rotated_text, text_rect)
	

	def draw(self, screen):
		if self.in_pocket: return
		pos_px = to_pygame(self.body.position)
		rad_px = int(self.radius * PX_M)
		
		# Dibujo base de la bola (Cuerpo y brillo)
		pygame.draw.circle(screen, self.color, pos_px, rad_px)
		self._draw_number(screen, pos_px, rad_px)
		if self.punto: pygame.draw.circle(screen, (5, 5, 5), (pos_px[0]-rad_px//3, pos_px[1]-rad_px//3), rad_px//5, width=0)

class Ball_solid(Ball):
	"""Bola de color liso con círculo blanco y número."""
	def __init__(self, space, pos, color, number):
		super().__init__(space, pos, color, number=number, is_cue=False)

class Ball_striped(Ball):
	"""Bola rayada: base blanca con una franja ancha de color."""
	def __init__(self, space, pos, color, number):
		super().__init__(space, pos, (255, 255, 255), number=number, is_cue=False)
		self.stripe_color = color

	def draw(self, screen):
		if self.in_pocket: return
		pos_px = to_pygame(self.body.position)
		rad_px = int(self.radius * PX_M)
		angle = self.body.angle
		
		# 1. Crear una superficie temporal para la bola con transparencia
		# El tamaño es el diámetro de la bola
		temp_surf = pygame.Surface((rad_px * 2, rad_px * 2), pygame.SRCALPHA)
		center_surf = (rad_px, rad_px)
		
		# 2. Dibujar la base blanca de la bola en la superficie temporal
		pygame.draw.circle(temp_surf, (255, 255, 255), center_surf, rad_px)
		
		# 3. Dibujar la franja de color rotada
		# Calculamos los puntos finales de la franja basados en el ángulo
		# para que la franja gire con la bola
		p1_x = rad_px + rad_px * math.cos(angle)
		p1_y = rad_px + rad_px * math.sin(angle)
		p2_x = rad_px - rad_px * math.cos(angle)
		p2_y = rad_px - rad_px * math.sin(angle)
		
		# Dibujamos la franja. El grosor suele ser un 60-70% del diámetro
		pygame.draw.line(temp_surf, self.stripe_color, (p1_x, p1_y), (p2_x, p2_y), int(rad_px * 1.1))
		
		# 4. "Recortar" la superficie para que sea circular
		# Creamos una máscara circular: lo que no sea el círculo será transparente
		mask_surf = pygame.Surface((rad_px * 2, rad_px * 2), pygame.SRCALPHA)
		pygame.draw.circle(mask_surf, (255, 255, 255, 255), center_surf, rad_px)
		temp_surf.blit(mask_surf, (0, 0), special_flags=pygame.BLEND_RGBA_MIN)
		
		# 5. Dibujar la superficie temporal en la pantalla principal
		screen.blit(temp_surf, (pos_px[0] - rad_px, pos_px[1] - rad_px))
		
		# 6. Dibujar el número y el brillo encima (el número ya gestiona su propia rotación)
		self._draw_number(screen, pos_px, rad_px)
		
		# Brillo estático para realismo
		pygame.draw.circle(screen, (255, 255, 255), (pos_px[0]-rad_px//3, pos_px[1]-rad_px//3), rad_px//6)
		
		
def setup_game(space, mesa):
	bolas = []
	rad = 0.0285
	margin_ball = 0.0001 # Pequeño margen para evitar solapamiento inicial
	
	# 1. Colocar la bola blanca en el Head Spot
	head_spot_x = mesa.margin + ((mesa.width - 2 * mesa.margin) / 4)
	y_center = mesa.height / 2
	#Esta es la blanca
	bolas.append(Ball(space, (head_spot_x, y_center), (255, 255, 255), is_cue=True,punto=True))
	
	# 2. Configuración del Triángulo (Foot Spot)
	foot_spot_x = mesa.margin + (3 * (mesa.width - 2 * mesa.margin) / 4)
	
	# Colores y números típicos (lisa < 9, rayada >= 9)
	# Definimos el orden para que la 8 esté en el centro y las esquinas cumplan la norma
	# Lista de datos: (Número, Color RGB, Tipo)
	# Configurada para que la 8 sea el centro y las esquinas tengan una lisa y una rayada
	pool_data = [
		(1, (255, 215, 0), "solid"),    # 1 Amarilla
		(9, (255, 215, 0), "striped"),  # 9 Amarilla
		
		(2, (0, 0, 255), "solid"),      # 2 Azul
		(10, (0, 0, 255), "striped"),   # 10 Azul
		
		(8, (0, 0, 0), "solid"),        # 8 Negra
		(3, (255, 0, 0), "solid"),      # 3 Roja
		
		(11, (255, 0, 0), "striped"),   # 11 Roja
		(7, (128, 0, 0), "solid"),      # 7 Granate
		
		(15, (128, 0, 0), "striped"),   # 15 Granate
		(4, (128, 0, 128), "solid"),    # 4 Morada
		
		(5, (255, 165, 0), "solid"),    # 5 Naranja
		(13, (255, 165, 0), "striped"), # 13 Naranja
		
		(14, (0, 100, 0), "striped"),   # 14 Verde Oscuro
		(6, (0, 100, 0), "solid"),      # 6 Verde Oscuro
		
		(12, (128, 0, 128), "striped")  # 12 Morada
	]
	
	idx = 0
	dx = (rad * 2 + margin_ball) * math.cos(math.radians(30))
	dy = rad + (margin_ball / 2)
	
	for fila in range(5):
		for col in range(fila + 1):
			num, color, tipo = pool_data[idx]
			
			# Posicionamiento relativo al vértice del triángulo
			pos_x = foot_spot_x + (fila * dx)
			pos_y = y_center + (col * 2 * dy) - (fila * dy)
			
			if tipo == "solid":
				bolas.append(Ball_solid(space, (pos_x, pos_y), color, num))
			else:
				bolas.append(Ball_striped(space, (pos_x, pos_y), color, num))
			idx += 1
			
	return bolas
#---------------------------	
def reset_cue_ball(ball, mesa):
	# 1. Calculamos el Head Spot (donde empezó la partida)
	head_spot_x = mesa.margin + ((mesa.width - 2 * mesa.margin) / 4)
	y_center = mesa.height / 2
	
	# 2. Resetear propiedades físicas
	ball.in_pocket = False
	ball.body.position = (head_spot_x, y_center)
	ball.body.velocity = (0, 0)
	ball.body.angular_velocity = 0
	
	# 3. Volver a añadirla al espacio físico de Pymunk
	# Es vital porque fall_into_pocket() la eliminó del espacio
	ball.space.add(ball.body, ball.shape)	

#-----------------------------------------------------------
def draw_aiming_help(screen, ball, cue_angle):
	"""Dibuja dos líneas rojas paralelas a la trayectoria a un radio de distancia."""
	# 1. Obtener dirección actual y posición en píxeles
	direction = Vec2d(1, 0).rotated(cue_angle)
	pos_px = to_pygame(ball.body.position)
	rad_px = int(ball.radius * PX_M)
	
	# 2. Calcular el vector normal (perpendicular) a la dirección
	# Simplemente giramos el vector de dirección 90 grados
	normal = Vec2d(-direction.y, direction.x)
	
	# 3. Calcular los puntos de inicio y fin de las líneas de ayuda
	# Longitud de la línea en píxeles
	line_len_px = 1000 
	
	# Desplazamos los puntos de inicio de las líneas un radio en la dirección normal
	start_p1 = (pos_px[0] + normal.x * rad_px, pos_px[1] + normal.y * rad_px)
	start_p2 = (pos_px[0] - normal.x * rad_px, pos_px[1] - normal.y * rad_px)
	
	# Calculamos los puntos finales proyectando desde el inicio
	end_p1 = (start_p1[0] + direction.x * line_len_px, start_p1[1] + direction.y * line_len_px)
	end_p2 = (start_p2[0] + direction.x * line_len_px, start_p2[1] + direction.y * line_len_px)
	
	# 4. Dibujar las líneas (Rojo claro/transparente si usas SRCALPHA, o sólido)
	# Usamos un color rojo claro sólido (255, 150, 150)
	color_help = (255, 150, 150)
	
	pygame.draw.line(screen, color_help, start_p1, end_p1, 1)
	pygame.draw.line(screen, color_help, start_p2, end_p2, 1)


#--------------------------------------
def main():
	pygame.init()
	screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
	pygame.display.set_caption("Billar - Control de Tiro")
	clock = pygame.time.Clock()
	
	space = pymunk.Space()
	space.gravity = (0, 0)
	space.damping = 0.99 
	
	mesa = BilliardTable(space, MESA_W_M, MESA_H_M)
	bolas = setup_game(space, mesa)
	
	# Variables de control
	cue_angle = 0.0					
	cue_power = 3.0
	MAX_POWER = 10.0		# Valor límite reducido a 10
	MIN_POWER = 0.1			# Mínimo más bajo para tiros muy suaves
	angle_speed = 0.01		
	power_step = 0.05		# Incremento mucho más pequeño (antes 0.2)
	#le pongo un poco de aleatoriedad a la salida para evitar que
	#siempre sea la misma salida
	cue_angle+=(np.random.rand()-0.5)*angle_speed*0.3
	

	lisas_coladas = []
	rayadas_coladas = []

	
	substepping = 20
	dt = 1/60.0
	dt_sub = dt / substepping
	
	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False
			
			if event.type == pygame.KEYDOWN:
				if event.key == pygame.K_SPACE:
					# Solo disparamos si la blanca está quieta y en la mesa
					blanca = bolas[0]
					if blanca.body.velocity.length < 0.02 and not blanca.in_pocket:
						# Calculamos el vector de dirección según el ángulo de la línea
						# cos para X, sin para Y
						impulse_x = math.cos(cue_angle) * cue_power
						impulse_y = math.sin(cue_angle) * cue_power
						blanca.apply_impulse((impulse_x, impulse_y))

		# Entrada continua de teclado
		keys = pygame.key.get_pressed()
		
		# Determinar la velocidad de giro actual (ajuste fino con CTRL)
		current_angle_speed = angle_speed
		if keys[pygame.K_LCTRL] or keys[pygame.K_RCTRL]:
			current_angle_speed = angle_speed / 10

		# Controles de ángulo
		if keys[pygame.K_LEFT]:
			cue_angle -= current_angle_speed
		if keys[pygame.K_RIGHT]:
			cue_angle += current_angle_speed
			
		# Controles de potencia (se mantienen igual)
		if keys[pygame.K_UP]:
			cue_power = min(MAX_POWER, cue_power + power_step)
		if keys[pygame.K_DOWN]:
			cue_power = max(MIN_POWER, cue_power - power_step)

		screen.fill((34, 139, 34))
		mesa.draw(screen)
		
		#Actualiza lista de bolas
		# Lógica de actualización y clasificación
		for b in bolas:
			if not b.in_pocket:
				b.update(mesa.pockets)
				if b.in_pocket:
					if b.is_cue:
						# REGLA: Si es la blanca, la reseteamos en lugar de eliminarla
						reset_cue_ball(b, mesa)
					else:
						# Clasificación normal para el marcador
						if isinstance(b, Ball_striped):
							rayadas_coladas.append(b)
						else:
							lisas_coladas.append(b)
			
			b.draw(screen)
			
		#Marcador
		rad_ui = 15  # Tamaño visual en el marcador
		
		# Dibujar lisas a la izquierda
		for i, b in enumerate(lisas_coladas):
			pos_ui = (200 + i * 40, 50) # 50 es la mitad del OFFSET_Y
			pygame.draw.circle(screen, b.color, pos_ui, rad_ui)
			# Un pequeño brillo para que no parezcan planos
			pygame.draw.circle(screen, (255, 255, 255), (pos_ui[0]-5, pos_ui[1]-5), 4)

		# Dibujar rayadas a la derecha
		for i, b in enumerate(rayadas_coladas):
			pos_ui = (SCREEN_WIDTH - 40 - i * 40, 50)
			# Base blanca
			pygame.draw.circle(screen, (255, 255, 255), pos_ui, rad_ui)
			# Franja de color
			pygame.draw.circle(screen, b.stripe_color, pos_ui, rad_ui - 6)	
			
		
		
		
		# Referencia a la bola blanca para la interfaz y la guía
		blanca = bolas[0]
		
		# Solo dibujamos la guía si la bola está parada
		if not blanca.in_pocket and blanca.body.velocity.length < 0.02:
			pos_px = to_pygame(blanca.body.position)
			
			# DIBUJAR LÍNEAS ROJAS DE AYUDA 
			draw_aiming_help(screen, blanca, cue_angle)
			
			# Dibujar línea gris (dirección del tiro)
			line_len = 1500
			target_x = pos_px[0] + math.cos(cue_angle) * line_len
			target_y = pos_px[1] + math.sin(cue_angle) * line_len
			pygame.draw.line(screen, (200, 200, 200), pos_px, (target_x, target_y), 1)
			
			# UI de potencia con una pequeña barra visual opcional
			font_ui = pygame.font.SysFont("Arial", 22, bold=True)
			power_text = font_ui.render(f"IMPULSO: {cue_power:.1f}", True, (255, 255, 255))
			screen.blit(power_text, (30, 30))
			
			# Dibujar una barra de fuerza debajo del texto
			bar_width = 300
			pygame.draw.rect(screen, (100, 100, 100), (30, 60, bar_width, 10))
			pygame.draw.rect(screen, (255, 0, 0), (30, 60, (cue_power / MAX_POWER) * bar_width, 10))

		# Lógica de actualización y dibujo de todas las bolas
		for b in bolas:
			b.update(mesa.pockets)
			b.draw(screen)
		
		# Paso de física
		for _ in range(substepping):
			for b in bolas:
				if not b.in_pocket:
					b.apply_advanced_friction(dt_sub)
			space.step(dt_sub)
		
		pygame.display.flip()
		clock.tick(60)

	pygame.quit()

if __name__ == "__main__":
	main()
