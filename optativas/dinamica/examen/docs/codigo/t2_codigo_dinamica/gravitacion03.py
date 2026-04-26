import pymunk
import pymunk.pygame_util
import pygame
import math
import numpy as np

def simular():
	# Configuración de la ventana
	pygame.init()
	screen = pygame.display.set_mode((800, 600))
	clock = pygame.time.Clock()
	draw_options = pymunk.pygame_util.DrawOptions(screen)
	# Esto es para que no salga una linea en la esfera
	draw_options.shape_outline_color = (255,0,0)

	# Espacio sin gravedad
	space = pymunk.Space()
	space.gravity = (0, 0)

	
	# 1) Atractores estáticos (Masas fijas)
	# Atractor Grande (Izquierda)
	# Atractor Pequeño (Derecha)
	pos_grande = (200, 300)
	pos_pequeno = (600, 300)
	static_body_1 = pymunk.Body(body_type=pymunk.Body.STATIC)
	static_body_1.position = pos_grande
	shape_1 = pymunk.Circle(static_body_1, 20)
	shape_1.color = pygame.Color("red")
	
	static_body_2 = pymunk.Body(body_type=pymunk.Body.STATIC)
	static_body_2.position = pos_pequeno
	shape_2 = pymunk.Circle(static_body_2, 10)
	shape_2.color = pygame.Color("red")
	
	space.add(static_body_1, shape_1, static_body_2, shape_2)

	# 2) Objeto móvil (Satélite)
	masa = 1
	radio_peq = 5
	momento = pymunk.moment_for_circle(masa, 0, radio_peq)
	body = pymunk.Body(masa, momento)
	
	body.position = (200, 200)
	alpha=-30*np.pi/180.0
	F=250
	body.velocity = (F*np.cos(alpha), F*np.sin(alpha)) 
	
	shape = pymunk.Circle(body, radio_peq)
	shape.color = pygame.Color("blue")
	space.add(body, shape)

	# Constantes de fuerza (proporcionales al "tamaño" o masa)
	K1 = 5000000 # Para la grande
	K2 = 2500000 # Para la pequeña (puedes ajustar esta proporción)

	trayectoria = []
	pasos_fisica = 200 # Con 200 es suficiente y va más fluido
	dt_total = 1/60.0
	dt_paso = dt_total / pasos_fisica

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		for _ in range(pasos_fisica):
			# Fuerza neta inicial
			fx_total = 0
			fy_total = 0

			# --- Fuerza Atractor 1 (Grande) ---
			dx1 = static_body_1.position.x - body.position.x
			dy1 = static_body_1.position.y - body.position.y
			dist_sq1 = dx1**2 + dy1**2
			dist1 = math.sqrt(dist_sq1)
			if dist1 > 1:
				mag1 = K1 / dist_sq1
				fx_total += mag1 * (dx1 / dist1)
				fy_total += mag1 * (dy1 / dist1)
			
			# --- Fuerza Atractor 2 (Pequeña) ---
			dx2 = static_body_2.position.x - body.position.x
			dy2 = static_body_2.position.y - body.position.y
			dist_sq2 = dx2**2 + dy2**2
			dist2 = math.sqrt(dist_sq2)
			if dist2 > 1:
				mag2 = K2 / dist_sq2
				fx_total += mag2 * (dx2 / dist2)
				fy_total += mag2 * (dy2 / dist2)

			body.apply_force_at_local_point((fx_total, fy_total))
			space.step(dt_paso)
	
		# Guardar posición
		trayectoria.append((int(body.position.x), int(body.position.y)))
		if len(trayectoria) > 3000:
			trayectoria.pop(0)

		# Dibujo
		screen.fill((255, 255, 255))
		if len(trayectoria) > 2:
			pygame.draw.lines(screen, (150, 150, 150), False, trayectoria, 1)
			
		space.debug_draw(draw_options)
		pygame.display.flip()
		clock.tick(60)

	pygame.quit()

if __name__ == "__main__":
	simular()
