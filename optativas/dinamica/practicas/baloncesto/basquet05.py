import pygame
import pymunk
import pymunk.pygame_util
import math
from pymunk import Vec2d
import numpy as np

# --- NUEVA CONFIGURACIÓN DE ESCALA ---
PX_M = 150  # 1 metro = 150 píxeles
WIDTH, HEIGHT = 1600, 800
FPS = 60

def crear_balon(space, mass, radius, y_suelo):
	moment = pymunk.moment_for_circle(mass, 0, radius)
	body = pymunk.Body(mass, moment)
	
	# Posicionamos el balón a 2.5m sobre el suelo y a 2m de X
	pos_x = 2.0 * PX_M
	pos_y = y_suelo - (2.0 * PX_M)
	body.position = (pos_x, pos_y) 
	
	shape = pymunk.Circle(body, radius)
	shape.elasticity = 0.85 
	shape.friction = 0.5
	space.add(body, shape)
	
	# Cuerpo estático invisible para que el balón repose encima al inicio
	plataforma_body = space.static_body
	# Segmento invisible justo debajo del balón
	plataforma_shape = pymunk.Segment(plataforma_body, (pos_x - 50, pos_y + radius), (pos_x + 50, pos_y + radius), 1)
	plataforma_shape.elasticity = 0.85
	plataforma_shape.friction = 0.5
	# No se añade a ninguna lista de dibujado, por lo que no se ve
	space.add(plataforma_shape)
	
	return body, shape

def run():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()

	# Carga de imagen de fondo
	try:
		background_img = pygame.image.load("grada_baloncesto03.jpg").convert()
		background_img = pygame.transform.smoothscale(background_img, (WIDTH, HEIGHT))
	except:
		background_img = None

	# Carga de imágenes del jugador
	player_imgs = {}
	altura_jugador=[2.0,2.2]
	for name,h in zip(["jugador01.png", "jugador02.png"],altura_jugador):
		try:
			img = pygame.image.load(name).convert_alpha()
			ratio = img.get_width() / img.get_height()
			p_height = int(h * PX_M)
			player_imgs[name] = pygame.transform.smoothscale(img, (int(p_height * ratio), p_height))
		except:
			player_imgs[name] = None

	# Configuración del espacio físico
	space = pymunk.Space()
	space.gravity = (0, 9.81 * PX_M) # Gravedad real en m/s^2

	# Parámetros reales (FIBA/NBA)
	mass = 0.625 # kg
	radius = 0.119 * PX_M # Radio real ~12cm convertido a píxeles
	
	# Estado del lanzamiento
	lanzado = False

	# Carga y escalado de la imagen del balón
	try:
		diametro = int(radius * 2)
		img_original = pygame.image.load("balon_basket.png").convert_alpha()
		ball_img_base = pygame.transform.smoothscale(img_original, (diametro, diametro))
	except:
		ball_img_base = pygame.Surface((radius * 2, radius * 2), pygame.SRCALPHA)
		pygame.draw.circle(ball_img_base, (200, 50, 50), (int(radius), int(radius)), int(radius))

	# Elementos estáticos para el cálculo de posición
	y_suelo = HEIGHT - (0.1 * PX_M)

	# Creación del balón inicial
	body, shape = crear_balon(space, mass, radius, y_suelo)

	# --- ELEMENTOS ESTÁTICOS ---
	static_body = space.static_body

	# Suelo
	floor = pymunk.Segment(static_body, (0, y_suelo), (WIDTH, y_suelo), 5)
	floor.elasticity = 0.9
	floor.friction = 0.5
	space.add(floor)

	# Tablero
	x_tablero = 9.0 * PX_M 
	y_tablero_top = y_suelo - (3.95 * PX_M)
	y_tablero_bot = y_suelo - (2.90 * PX_M)
	tablero = pymunk.Segment(static_body, (x_tablero, y_tablero_top), (x_tablero, y_tablero_bot), 10)
	tablero.elasticity = 0.7
	tablero.friction = 0.5
	space.add(tablero)

	# Aro Físico
	aro_y = y_suelo - (3.05 * PX_M)
	diametro_aro = 0.45 * PX_M
	x_aro_trasero = x_tablero - (0.15 * PX_M)
	x_aro_delantero = x_aro_trasero - diametro_aro
	x_aro_sop = x_aro_trasero + (0.075 * PX_M)

	aro_part_w = 0.04 * PX_M 
	aro_part_h = 0.02 * PX_M

	# Aro Trasero
	body_trasero = pymunk.Body(body_type=pymunk.Body.STATIC)
	body_trasero.position = (x_aro_trasero, aro_y)
	aro_trasero_shape = pymunk.Poly.create_box(body_trasero, (aro_part_w, aro_part_h))
	
	# Aro Delantero
	body_delantero = pymunk.Body(body_type=pymunk.Body.STATIC)
	body_delantero.position = (x_aro_delantero, aro_y)
	aro_delantero_shape = pymunk.Poly.create_box(body_delantero, (aro_part_w, aro_part_h))
	
	# --- CADENA DE BOLITAS COLGANTES ---
	cadenas_bolitas = [] # Lista de listas para guardar las 2 cadenas
	radio_bolita = 0.025 * PX_M
	masa_bolita = 0.02
	
	for body_anclaje_inicial in [body_delantero, body_trasero]:
		cadena_actual = []
		cuerpo_padre = body_anclaje_inicial
		distancias = [0.12* PX_M, 0.12* PX_M, 0.12* PX_M] # 3 niveles
		
		for dist in distancias:
			momento = pymunk.moment_for_circle(masa_bolita, 0, radio_bolita)
			b_hijo = pymunk.Body(masa_bolita, momento)
			# Posicionamos cada bolita debajo de su padre
			b_hijo.position = (cuerpo_padre.position.x, cuerpo_padre.position.y + dist)
			
			s_hijo = pymunk.Circle(b_hijo, radio_bolita)
			s_hijo.elasticity = 0.5
			s_hijo.friction = 0.5
			
			# Unión PinJoint entre el padre y el hijo actual
			union = pymunk.PinJoint(cuerpo_padre, b_hijo, (0,0), (0,0))
			
			space.add(b_hijo, s_hijo, union)
			cadena_actual.append(b_hijo)
			cuerpo_padre = b_hijo # El hijo actual será el padre del siguiente
			
		cadenas_bolitas.append({'anclaje': body_anclaje_inicial, 'bolitas': cadena_actual})
		
	# --- MUELLES ENTRE CADENAS (SIMULACIÓN DE RED) ---
	muelles = []
	stiffness = 50.0  # Rigidez del muelle
	damping = 2.    # Amortiguación
	# Longitudes naturales que se estrechan al bajar (en metros -> píxeles)
	longitudes_muelles = [0.35 * PX_M, 0.25 * PX_M, 0.15 * PX_M]

	# Unimos la bolita i de la cadena izquierda con la bolita i de la derecha
	for i in range(3):
		b_izq = cadenas_bolitas[0]['bolitas'][i]
		b_der = cadenas_bolitas[1]['bolitas'][i]
		
		muelle = pymunk.DampedSpring(
			b_izq, b_der, 
			(0,0), (0,0), 
			longitudes_muelles[i], 
			stiffness, 
			damping
		)
		space.add(muelle)
		muelles.append(muelle)	

	#-------------------------------------------------
	# Soporte aro
	body_sop = pymunk.Body(body_type=pymunk.Body.STATIC)
	body_sop.position = (x_aro_sop, aro_y)
	aro_sop_shape = pymunk.Poly.create_box(body_sop, (aro_part_w * 2, aro_part_h))
	
	for a in [aro_trasero_shape, aro_delantero_shape, aro_sop_shape]:
		a.elasticity = 0.5
		a.friction = 0.5
		space.add(a.body, a)

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False
			
			if event.type == pygame.KEYDOWN and event.key == pygame.K_SPACE:
				if not lanzado:
					v_inicial_ms = 8.99
					alpha = 60 * np.pi / 180
					body.velocity = v_inicial_ms * PX_M * Vec2d(np.cos(alpha), -np.sin(alpha))
					body.angular_velocity = -20
					lanzado = True

		if lanzado:
			if body.position.x < 0 or body.position.x > WIDTH or body.position.y > HEIGHT:
				space.remove(body, shape)
				body, shape = crear_balon(space, mass, radius, y_suelo)
				lanzado = False

		# Dibujado del fondo
		if background_img:
			screen.blit(background_img, (0, 0))
		else:
			screen.fill((240, 240, 240))

		dt = 1.0 / FPS
		space.step(dt)

		# --- DIBUJADO DE ELEMENTOS ---

		pygame.draw.line(screen, (50, 50, 50), (0, int(y_suelo)), (WIDTH, int(y_suelo)), 5)
		pygame.draw.line(screen, (100, 100, 100), (int(x_tablero), int(y_tablero_top)), (int(x_tablero), int(y_tablero_bot)), 10)
		
		# --- DIBUJO DE LAS CADENAS DE BOLITAS ---
		for cadena in cadenas_bolitas:
			padre_pos = cadena['anclaje'].position
			for b_hijo in cadena['bolitas']:
				hijo_pos = b_hijo.position
				# Dibujar hilo entre padre e hijo
				pygame.draw.line(screen, (255, 255, 255), (int(padre_pos.x), int(padre_pos.y)), (int(hijo_pos.x), int(hijo_pos.y)), 1)
				# Dibujar bolita
				pygame.draw.circle(screen, (255, 255, 255), (int(hijo_pos.x), int(hijo_pos.y)), int(radio_bolita))
				padre_pos = hijo_pos # El siguiente hilo sale de esta bolita
				
		# --- DIBUJO DE LA RED (ENTRELAZADO) ---
		bolas_izq = cadenas_bolitas[0]['bolitas']
		bolas_der = cadenas_bolitas[1]['bolitas']
		
		for b in bolas_izq + bolas_der:
			b.velocity*=0.9
		
		
		
		# 1. Unir primera bola de la izquierda con aro derecho y primera derecha con aro izquierdo
		pygame.draw.line(screen, (255, 255, 255), (int(bolas_izq[0].position.x), int(bolas_izq[0].position.y)), (int(x_aro_trasero), int(aro_y)), 1)
		pygame.draw.line(screen, (255, 255, 255), (int(bolas_der[0].position.x), int(bolas_der[0].position.y)), (int(x_aro_delantero), int(aro_y)), 1)
		
		# 2. Unir segunda bola derecha con primera izquierda, y segunda izquierda con primera derecha
		pygame.draw.line(screen, (255, 255, 255), (int(bolas_der[1].position.x), int(bolas_der[1].position.y)), (int(bolas_izq[0].position.x), int(bolas_izq[0].position.y)), 1)
		pygame.draw.line(screen, (255, 255, 255), (int(bolas_izq[1].position.x), int(bolas_izq[1].position.y)), (int(bolas_der[0].position.x), int(bolas_der[0].position.y)), 1)
		
		# 3. Unir tercera derecha con segunda derecha, y tercera izquierda con segunda izquierda
		pygame.draw.line(screen, (255, 255, 255), (int(bolas_der[2].position.x), int(bolas_der[2].position.y)), (int(bolas_izq[1].position.x), int(bolas_izq[1].position.y)), 1)
		pygame.draw.line(screen, (255, 255, 255), (int(bolas_izq[2].position.x), int(bolas_izq[2].position.y)), (int(bolas_der[1].position.x), int(bolas_der[1].position.y)), 1)
		
	
		
		# 4. Unir las dos últimas bolas entre ellas (las terceras)
		pygame.draw.line(screen, (255, 255, 255), (int(bolas_izq[2].position.x), int(bolas_izq[2].position.y)), (int(bolas_der[2].position.x), int(bolas_der[2].position.y)), 1)

		# Dibujar las bolitas blancas encima para que se vean limpias
		for b in bolas_izq + bolas_der:
			pygame.draw.circle(screen, (255, 255, 255), (int(b.position.x), int(b.position.y)), int(radio_bolita))		
				
		
		color_aro_suave = (255, 150, 150)
		pygame.draw.line(screen, color_aro_suave, (int(x_aro_delantero), int(aro_y)), (int(x_aro_trasero), int(aro_y)), 3)

		for b in [body_trasero, body_delantero]:
			pygame.draw.rect(screen, (200, 0, 0), (int(b.position.x - aro_part_w/2), int(b.position.y - aro_part_h/2), int(aro_part_w), int(aro_part_h)))
		
		pygame.draw.rect(screen, (200, 0, 0), (int(x_aro_sop - aro_part_w), int(aro_y - aro_part_h/2), int(aro_part_w * 2), int(aro_part_h)))

		# 1. Dibujado del balón
		pos = body.position
		angle_degrees = math.degrees(-body.angle)
		img_rotada = pygame.transform.rotate(ball_img_base, angle_degrees)
		img_rect = img_rotada.get_rect(center=(int(pos.x), int(pos.y)))
		screen.blit(img_rotada, img_rect)

		# 2. Dibujado del jugador
		img_key = "jugador02.png" if lanzado else "jugador01.png"
		if player_imgs[img_key]:
			player_rect = player_imgs[img_key].get_rect(midbottom=(int(2.0 * PX_M), int(y_suelo)))
			screen.blit(player_imgs[img_key], player_rect)

		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	run()
