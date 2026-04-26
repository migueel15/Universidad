"""
SIMULACIÓN DINÁMICA: CÁLCULO DE ROZAMIENTO
- Cálculo de aceleración derivando la velocidad.
- Obtención de Fuerza Neta y Fuerza de Rozamiento.
- Visualización de datos en tiempo real.
- TIENE en cuenta la diferencia entre rozamiento estático y dinámico
  cuando se inicia el rozamiento cambia el estático por el dinámico
"""
import pymunk
import pygame
import math

def simular():
	pygame.init()
	screen = pygame.display.set_mode((800, 600))
	clock = pygame.time.Clock()
	fuente = pygame.font.SysFont("Arial", 22)

	space = pymunk.Space()
	space.gravity = (0, 981)

	# 1. Suelo
	suelo_body = space.static_body
	suelo_shape = pymunk.Segment(suelo_body, (0, 500), (800, 500), 5)
	suelo_shape.friction = 0.6
	space.add(suelo_shape)

	# 2. Caja (10 kg)
	rozamiento_estatico=0.6
	rozamiento_dinamico=0.4
	masa = 10
	dim = 60
	momento = pymunk.moment_for_box(masa, (dim, dim))
	caja_body = pymunk.Body(masa, momento)
	caja_body.position = (100, 475)
	caja_shape = pymunk.Poly.create_box(caja_body, (dim, dim))
	caja_shape.friction = rozamiento_estatico
	space.add(caja_body, caja_shape)

	# Parámetros de control
	fuerza_aplicada = 10.0 
	incremento = 0.2       
	escala_flecha = 100 / 40 
	movimiento_iniciado = False
	
	# Variables para cálculo de aceleración
	vel_anterior = 0.0
	dt = 1/60.0
	fuerza_rozamiento = 0.0

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# Lógica de crecimiento de fuerza
		if not movimiento_iniciado and caja_body.velocity.length > 0.1:
			caja_shape.friction = rozamiento_dinamico
			movimiento_iniciado = True
		
		
		
		
		if not movimiento_iniciado:
			fuerza_aplicada += incremento

		# Aplicar la fuerza (la multiplicamos por 100 para que sea visible en el motor)
		f_ext_motor = fuerza_aplicada * 100
		caja_body.apply_force_at_local_point((f_ext_motor, 0), (0, 0))
		
		space.step(dt)

		# CÁLCULO DINÁMICO
		vel_actual = caja_body.velocity.x
		aceleracion = (vel_actual - vel_anterior) / dt
		fuerza_neta = masa * aceleracion
		# F_neta = F_aplicada + F_rozo -> F_rozo = F_neta - F_aplicada
		fuerza_rozamiento = (fuerza_neta - f_ext_motor) / 100 # Volvemos a escala N
		vel_anterior = vel_actual

		# --- RENDER ---
		screen.fill((255, 255, 255))
		pygame.draw.line(screen, (0, 0, 0), (0, 500), (800, 500), 5)
		
		pos = caja_body.position
		rect = pygame.Rect(pos.x - dim/2, pos.y - dim/2, dim, dim)
		pygame.draw.rect(screen, (100, 100, 100), rect)

		# Vector Fuerza Aplicada
		largo_flecha = fuerza_aplicada * escala_flecha
		fin_flecha_x = pos.x + largo_flecha
		if fin_flecha_x >= 800: running = False

		pygame.draw.line(screen, (255, 0, 0), (pos.x, pos.y), (fin_flecha_x, pos.y), 4)
		pygame.draw.polygon(screen, (255, 0, 0), [(fin_flecha_x, pos.y), (fin_flecha_x-10, pos.y-5), (fin_flecha_x-10, pos.y+5)])

		# Info en pantalla (Esquina superior izquierda)
		txt_f = fuente.render(f"F. Aplicada: {fuerza_aplicada:.1f} N", True, (200, 0, 0))
		txt_v = fuente.render(f"Velocidad: {vel_actual/10:.2f} m/s", True, (0, 0, 0))
		txt_a = fuente.render(f"Aceleración: {aceleracion/10:.2f} m/s²", True, (0, 150, 0))
		txt_r = fuente.render(f"F. Rozamiento: {fuerza_rozamiento:.1f} N", True, (0, 0, 255))
		
		screen.blit(txt_f, (20, 20))
		screen.blit(txt_v, (20, 50))
		screen.blit(txt_a, (20, 80))
		screen.blit(txt_r, (20, 110))

		pygame.display.flip()
		clock.tick(60)

	pygame.quit()

if __name__ == "__main__":
	simular()
