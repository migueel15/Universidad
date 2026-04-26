import pygame
import pymunk
import pymunk.pygame_util
import math

# --- CONFIGURACIÓN ---
WIDTH, HEIGHT = 1000, 600
FPS = 60
SLOW_MO = 0.25 

def solve():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()
	draw_options = pymunk.pygame_util.DrawOptions(screen)

	space = pymunk.Space()
	space.gravity = (0, 600)

	radius = 12
	mass = 1
	moment = pymunk.moment_for_circle(mass, 0, radius)
	
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

	# Tres bolas que salen del mismo punto
	b1 = crear_bola(50)
	b2 = crear_bola(50)
	b3 = crear_bola(50)

	cm_path = []
	impulso_aplicado = False

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# --- LÓGICA DE EXPLOSIÓN TRIPLE EN EL APOGEO ---
		if not impulso_aplicado and b1.velocity.y >= 0:
			# Definimos los impulsos entre pares (magnitud, ángulo_radianes)
			# Pareja 1-2
			j12_mag, j12_ang = 300, math.radians(45)
			j12 = pymunk.Vec2d(j12_mag * math.cos(j12_ang), j12_mag * math.sin(j12_ang))
			
			# Pareja 1-3
			j13_mag, j13_ang = 150, math.radians(30)
			j13 = pymunk.Vec2d(j13_mag * math.cos(j13_ang), j13_mag * math.sin(j13_ang))
			
			# Pareja 2-3
			j23_mag, j23_ang = 200, math.radians(60)
			j23 = pymunk.Vec2d(j23_mag * math.cos(j23_ang), j23_mag * math.sin(j23_ang))

			# Aplicación de la 3ª Ley de Newton para cada par:
			# Bola 1 recibe de 2 y 3
			b1.apply_impulse_at_local_point(-j12 - j13)
			# Bola 2 recibe de 1 y 3
			b2.apply_impulse_at_local_point(j12 - j23)
			# Bola 3 recibe de 1 y 2
			b3.apply_impulse_at_local_point(j13 + j23)

			impulso_aplicado = True

		# Centro de Masas de las 3 bolas
		cm_x = (b1.position.x + b2.position.x + b3.position.x) / 3
		cm_y = (b1.position.y + b2.position.y + b3.position.y) / 3
		cm_path.append((int(cm_x), int(cm_y)))

		screen.fill((255, 255, 255))
		
		if len(cm_path) > 2:
			pygame.draw.lines(screen, (220, 20, 60), False, cm_path, 3)

		space.debug_draw(draw_options)
		
		dt = (1.0 / FPS) * SLOW_MO
		space.step(dt)
		
		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	solve()
