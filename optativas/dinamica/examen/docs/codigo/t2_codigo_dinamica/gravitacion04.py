
''' Este incluye una pequeña corrección para desviar la trayectoria un poco
cuando se acerca mucho a alguno de los planetas'''


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
	draw_options.shape_outline_color = (255,0,0)

	space = pymunk.Space()
	space.gravity = (0, 0)

	# 1) Atractores estáticos
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

	# 2) Objeto móvil
	masa = 1
	radio_peq = 5
	momento = pymunk.moment_for_circle(masa, 0, radio_peq)
	body = pymunk.Body(masa, momento)
	
	body.position = (200, 200)
	alpha = -30 * np.pi / 180.0
	V0 = 250
	body.velocity = (V0 * np.cos(alpha), V0 * np.sin(alpha)) 
	
	shape = pymunk.Circle(body, radio_peq)
	shape.color = pygame.Color("blue")
	space.add(body, shape)

	K1, K2 = 5000000, 2500000
	
	# Parámetros de la fuerza de corrección
	Fcorrect_mag = 100  # Magnitud de la fuerza deflectora
	Ncorrect = 500         # Duración en pasos de física
	contador_correccion = 0

	trayectoria = []
	pasos_fisica = 200
	dt_paso = (1/60.0) / pasos_fisica

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT: running = False

		for _ in range(pasos_fisica):
			fx_total, fy_total = 0, 0

			# Distancias a los centros
			v_dist1 = static_body_1.position - body.position
			dist1 = v_dist1.length
			v_dist2 = static_body_2.position - body.position
			dist2 = v_dist2.length

			# 1. Fuerza Gravitatoria Estándar
			if dist1 > 1:
				mag1 = K1 / (dist1**2)
				fx_total += mag1 * (v_dist1.x / dist1)
				fy_total += mag1 * (v_dist1.y / dist1)
			
			if dist2 > 1:
				mag2 = K2 / (dist2**2)
				fx_total += mag2 * (v_dist2.x / dist2)
				fy_total += mag2 * (v_dist2.y / dist2)

			# 2. Lógica de Activación de Fcorrect
			if (dist1 < 50 or dist2 < 30) and contador_correccion <= 0:
				contador_correccion = Ncorrect

			# 3. Aplicación de Fcorrect (Normal a la trayectoria)
			if contador_correccion > 0:
				# Vector velocidad actual
				vx, vy = body.velocity
				v_mag = body.velocity.length
				if v_mag > 0:
					# Normal perpendicular (hacia afuera de la masa)
					# Si (vx, vy) es la dirección, (-vy, vx) es una normal.
					# Evaluamos el signo para que empuje LEJOS del atractor
					v_radial = v_dist1 if dist1 < 50 else v_dist2
					# Producto escalar para ver si la normal apunta hacia el atractor o fuera
					nx, ny = -vy / v_mag, vx / v_mag
					if (nx * v_radial.x + ny * v_radial.y) > 0:
						nx, ny = -nx, -ny # Invertimos para que sea repulsiva
					
					fx_total += nx * Fcorrect_mag
					fy_total += ny * Fcorrect_mag
				
				contador_correccion -= 1

			body.apply_force_at_local_point((fx_total, fy_total))
			space.step(dt_paso)

		trayectoria.append((int(body.position.x), int(body.position.y)))
		if len(trayectoria) > 3000: trayectoria.pop(0)

		screen.fill((255, 255, 255))
		if len(trayectoria) > 2:
			pygame.draw.lines(screen, (150, 150, 150), False, trayectoria, 1)
		
		space.debug_draw(draw_options)
		pygame.display.flip()
		clock.tick(60)

	pygame.quit()

if __name__ == "__main__":
	simular()
