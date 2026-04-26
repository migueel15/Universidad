'''Dos bolas se lanzan unidas en un tiro parabólico. En un momento se separan y vemos que
el CM sigue una trayectoria parólica ya que la única fuerza que actua es la graveda.

En en blucle están comentadas dos líneas que paran las bolas cuando llegan al suelo. 
La aparición de esta fuerza extra hace que la trayectoria del CM deje de ser una
parábola'''

import pygame
import pymunk
import pymunk.pygame_util
import math

# --- CONFIGURACIÓN ---
WIDTH, HEIGHT = 1000, 500
FPS = 60
# Factor de cámara lenta (4 veces más lento)
SLOW_MO = 0.25 

def solve():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()
	draw_options = pymunk.pygame_util.DrawOptions(screen)

	space = pymunk.Space()
	space.gravity = (0, 500) # Gravedad un poco más suave para ver mejor el efecto

	# Parámetros físicos
	radius = 12
	mass = 1
	moment = pymunk.moment_for_circle(mass, 0, radius)
	
	# Velocidad inicial (Ángulo de salida 60º)
	v0_mag = 550 
	angle_out = math.radians(-60) 
	vx = v0_mag * math.cos(angle_out)
	vy = v0_mag * math.sin(angle_out)

	def crear_bola(pos_x):
		body = pymunk.Body(mass, moment)
		body.position = (pos_x, HEIGHT - 20)
		shape = pymunk.Circle(body, radius)
		shape.elasticity = 0.9
		shape.friction = 0.5
		body.velocity = (vx, vy)
		space.add(body, shape)
		return body

	bola1 = crear_bola(50)
	bola2 = crear_bola(50)

	cm_path = []
	impulso_aplicado = False

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# --- LÓGICA DE EXPLOSIÓN EN EL APOGEO ---
		if not impulso_aplicado and bola1.velocity.y >= 0:
			# Ángulo de la fuerza: 45 grados
			f_mag = 300
			ang_f = math.radians(45)
			# Componentes del impulso
			imp_x = f_mag * math.cos(ang_f)
			imp_y = f_mag * math.sin(ang_f)

			# Aplicamos fuerzas opuestas en 45º
			bola1.apply_impulse_at_local_point((-imp_x, -imp_y))
			bola2.apply_impulse_at_local_point((imp_x, imp_y))
			impulso_aplicado = True

		# Centro de Masas
		cm_x = (bola1.position.x + bola2.position.x) / 2
		cm_y = (bola1.position.y + bola2.position.y) / 2
		cm_path.append((int(cm_x), int(cm_y)))

		# Dibujado
		screen.fill((255, 255, 255))
		
		# Dibujar rastro del CM
		if len(cm_path) > 2:
			pygame.draw.lines(screen, (220, 20, 60), False, cm_path, 3)

		space.debug_draw(draw_options)

		pygame.draw.line(screen, (55, 55, 55), bola1.position, bola2.position, 1)
		
		# El paso de simulación es más pequeño para el slow motion
		
		''' si se descomenta aquí, las bolas paran al tocar el suelo'''
		#for b in (bola1,bola2):
		#	if b.position[1]>HEIGHT-5: b.velocity=(0,0)

		
		dt = (1.0 / FPS) * SLOW_MO
		space.step(dt)
		
		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	solve()
