'''Dibuja la trayectoria de un "satélite" alrededor de un planeta aplicando
la fuerza gravitatoria -K/r^2 como fuerza externa. Las unidades son arbitrarias
y están elegidas para que cuadre en pantalla.

La trayectoria debería ser una elipse pero es ESPIRAL a causa de los errores
acumulados por el solver que utiliza Euler'''

import pymunk
import pymunk.pygame_util
import pygame
import math

def simular():
	# Configuración de la ventana
	pygame.init()
	screen = pygame.display.set_mode((800, 600))
	clock = pygame.time.Clock()
	# Vamos a usar space.debug_draw() en lugar de pintar
	# cada cosa por separado
	draw_options = pymunk.pygame_util.DrawOptions(screen)
	draw_options.shape_outline_color = (255,0,0)

	# Espacio sin gravedad
	# LA GRAVEDAD LA IMPLMENTAMOS COMO UNA FUERZA CENTRAL
	space = pymunk.Space()
	space.gravity = (0, 0)

	# 1) Círculo estático en el centro
	# ES EL CUERPO QUE CREA LA GRAVEDAD (EJ. TIERRA)
	centro_pos = (500, 300)
	static_body = space.static_body
	static_body.position = centro_pos
	static_shape = pymunk.Circle(static_body, 40)
	static_shape.color = pygame.Color("red")
	space.add(static_shape)

	# 2) Objeto móvil (círculo pequeño)
	# SATELITE
	masa = 1
	radio_peq = 5
	momento = pymunk.moment_for_circle(masa, 0, radio_peq)
	body = pymunk.Body(masa, momento)
	
	# Posición y velocidad inicial
	# La velocidad no es tangencial: en este caso Vy!=0
	# por lo que la trayectoria es elíptica, no circular
	body.position = (500, 100)
	body.velocity = (150, 80) 
	
	shape = pymunk.Circle(body, radio_peq)
	shape.color = pygame.Color("blue")
	space.add(body, shape)

	# Constante de la fuerza K
	K = 5000000 

	# --- NUEVO: Lista para el trazo ---
	trayectoria = []

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# Cálculo de la fuerza K/r^2
		# Vector desde el cuerpo al centro estático
		# Si queremos que sea repulsiva, ej. cargas, la resta se hace al revés
		dx = static_body.position.x - body.position.x
		dy = static_body.position.y - body.position.y
		distancia_sq = dx**2 + dy**2
		distancia = math.sqrt(distancia_sq)

		if distancia > 1: # Evitar división por cero o fuerzas infinitas
			fuerza_mag = K / distancia_sq
			# Normalizar vector y aplicar magnitud
			f_x = fuerza_mag * (dx / distancia)
			f_y = fuerza_mag * (dy / distancia)
			
			# Aplicar la fuerza en el centro del cuerpo
			body.apply_force_at_local_point((f_x, f_y))

		# --- NUEVO: Guardar posición actual ---
		trayectoria.append((int(body.position.x), int(body.position.y)))
		if len(trayectoria) > 2000: # Límite para no saturar memoria
			trayectoria.pop(0)

		# Limpiar pantalla y dibujar
		screen.fill((255, 255, 255))
		
		# --- NUEVO: Dibujar trazo ---
		# para que se vea la órbita
		if len(trayectoria) > 2:
			pygame.draw.lines(screen, (100, 100, 100), False, trayectoria, 1)
			
		space.debug_draw(draw_options)
		
		space.step(1/60.0)
		pygame.display.flip()
		clock.tick(60)

	pygame.quit()

if __name__ == "__main__":
	simular()
