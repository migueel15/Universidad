import pygame
import pymunk
import pymunk.pygame_util
import math
from pymunk import Vec2d
import numpy as np


from codigo.rozamiento_aire import *

# Configuración inicial
WIDTH, HEIGHT = 1600, 800
FPS = 60

def run():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()

	# Configuración del espacio físico
	space = pymunk.Space()
	space.gravity = (0, 900)

	# Parámetros de la esfera física
	mass = 1
	radius = 35
	moment = pymunk.moment_for_circle(mass, 0, radius)

	# Carga y escalado de la imagen
	try:
		diametro = int(radius * 2)
		img_original = pygame.image.load("balon_basket.png").convert_alpha()
		ball_img_base = pygame.transform.smoothscale(img_original, (diametro, diametro))
	except:
		ball_img_base = pygame.Surface((radius * 2, radius * 2), pygame.SRCALPHA)
		pygame.draw.circle(ball_img_base, (200, 50, 50), (radius, radius), radius)

	# Creación del balón
	body = pymunk.Body(mass, moment)
	body.position = (600, 450)
	
	shape = pymunk.Circle(body, radius)
	shape.elasticity = 0.9
	shape.friction = 0.5
	space.add(body, shape)

	# Velocidad inicial del lanzamiento
	alpha = 42 * np.pi / 180
	body.velocity = 943 * Vec2d(np.cos(alpha), -np.sin(alpha))
	body.angular_velocity = 0

	# --- ELEMENTOS ESTÁTICOS ---
	static_body = space.static_body

	# Suelo
	floor = pymunk.Segment(static_body, (0, HEIGHT - 50), (WIDTH, HEIGHT - 50), 5)
	floor.elasticity = 0.9
	floor.friction = 0.5
	space.add(floor)

	# Tablero
	tablero = pymunk.Segment(static_body, (1550, 200), (1550, 450), 10)
	tablero.elasticity = 0.7
	tablero.friction = 0.5
	space.add(tablero)

	# Aro Físico (Tus posiciones originales)
	aro_y = 380
	x_aro_trasero = 1515
	x_aro_delantero = 1400
	x_aro_sop = x_aro_trasero + 7.5

	# Creación de cuerpos estáticos independientes respetando tus tamaños (15x10 y 30x10)
	
	# Aro Trasero
	body_trasero = pymunk.Body(body_type=pymunk.Body.STATIC)
	body_trasero.position = (x_aro_trasero, aro_y)
	aro_trasero_shape = pymunk.Poly.create_box(body_trasero, (15, 10))
	
	# Aro Delantero
	body_delantero = pymunk.Body(body_type=pymunk.Body.STATIC)
	body_delantero.position = (x_aro_delantero, aro_y)
	aro_delantero_shape = pymunk.Poly.create_box(body_delantero, (15, 10))

	# Soporte aro
	body_sop = pymunk.Body(body_type=pymunk.Body.STATIC)
	body_sop.position = (x_aro_sop, aro_y)
	aro_sop_shape = pymunk.Poly.create_box(body_sop, (30, 10))
	
	for a in [aro_trasero_shape, aro_delantero_shape, aro_sop_shape]:
		a.elasticity = 0.5
		a.friction = 0.5
		space.add(a.body, a)

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		screen.fill((240, 240, 240))
		dt = 1.0 / FPS
		if body.position.y>710: aplicar_rodadura(body,Crr=0.05,dt=dt)
		
		
		
		space.step(dt)

		# --- DIBUJADO (Tus rectángulos originales) ---
		pygame.draw.line(screen, (50, 50, 50), (0, HEIGHT - 50), (WIDTH, HEIGHT - 50), 5)
		pygame.draw.line(screen, (100, 100, 100), (1550, 200), (1550, 450), 10)
		
		color_aro_suave = (255, 150, 150)
		pygame.draw.line(screen, color_aro_suave, (x_aro_delantero, aro_y), (x_aro_trasero, aro_y), 3)

		# Dibujo de rectángulos tal cual los tenías
		pygame.draw.rect(screen, (200, 0, 0), (x_aro_trasero - 7, aro_y - 5, 15, 10))
		pygame.draw.rect(screen, (200, 0, 0), (x_aro_delantero - 7, aro_y - 5, 15, 10))
		pygame.draw.rect(screen, (200, 0, 0), (x_aro_sop, aro_y - 5, 30, 10))

		# Balón
		pos = body.position
		angle_degrees = math.degrees(-body.angle)
		img_rotada = pygame.transform.rotate(ball_img_base, angle_degrees)
		img_rect = img_rotada.get_rect(center=(int(pos.x), int(pos.y)))
		screen.blit(img_rotada, img_rect)

		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	run()
