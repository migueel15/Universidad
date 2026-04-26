'''Lanza un bastón al aire (bola+varilla). Como el CM no está en el centro, los extremos hacen
trayectorias complejas (se puede cambiar en el bucle para que se pinte un extremo u otro.
Si se pinta la trayectoria del CM es una parábola
TRAYECTORIA y TRAYECTORIA_CM son boolenaos que controlan si se pintan la trayectoria
del extremo y/o del CM'''



import pygame
import pymunk
import math

# --- CONFIGURACIÓN ---
WIDTH, HEIGHT = 1000, 600
FPS = 60
SLOW_MO = 1
TRAYECTORIA = True
TRAYECTORIA_CM = True

# --- PARÁMETROS DEL BASTÓN ---
L = 120			# Longitud total
F_CM = 0.125	# Posición del CM (0.125 es 1/8). 0.5 sería el centro exacto.
MASS = 1.0
RADIUS_BOLA = 12

def solve():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()

	space = pymunk.Space()
	space.gravity = (0, 600)

	# 1. CÁLCULOS SEGÚN LA POSICIÓN DEL CM
	# Distancia del CM al centro geométrico (para el Teorema de Steiner)
	d_centro_al_cm = abs(L/2 - (F_CM * L))
	
	# Momento de inercia: I = I_centro + M * d^2
	moment = (1/12) * MASS * L**2 + MASS * d_centro_al_cm**2
	
	body = pymunk.Body(MASS, moment)
	body.position = (50, HEIGHT - 50)
	
	# Coordenadas de los extremos RELATIVAS al CM
	# El extremo A (donde está la bola) está a -F_CM * L
	# El extremo B está a (1 - F_CM) * L
	extremo_a = pymunk.Vec2d(-F_CM * L, 0)
	extremo_b = pymunk.Vec2d((1 - F_CM) * L, 0)
	
	shape = pymunk.Segment(body, extremo_a, extremo_b, 4)
	shape.elasticity = 0.5
	shape.friction = 0.5
	space.add(body, shape)

	# Lanzamiento
	v0_mag = 850
	angle_out = math.radians(-65)
	body.velocity = (v0_mag * math.cos(angle_out), v0_mag * math.sin(angle_out))
	body.angular_velocity = 10 

	cm_path = []
	extremo_path = []

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False


		'''Aquí se puede cambiar para que pinte uno u otro extremo en 
		la trayectoria'''
		# Guardar posiciones, pinta al trayectoria del extremo_a o el extremo_b
		cm_path.append((int(body.position.x), int(body.position.y)))
		#p_extremo = body.local_to_world(extremo_a)
		p_extremo = body.local_to_world(extremo_b)
		extremo_path.append((int(p_extremo.x), int(p_extremo.y)))

		screen.fill((255, 255, 255))
		
		# Trayectorias
		if TRAYECTORIA and len(extremo_path) > 2:
			pygame.draw.lines(screen, (0, 100, 255), False, extremo_path, 2)
		if TRAYECTORIA_CM and len(cm_path) > 2:
			pygame.draw.lines(screen, (220, 20, 60), False, cm_path, 3)

		# Dibujo manual del bastón
		pa_world = body.local_to_world(extremo_a)
		pb_world = body.local_to_world(extremo_b)
		
		# Cuerpo del bastón
		pygame.draw.line(screen, (50, 50, 50), pa_world, pb_world, 6)
		
		# Bola (siempre en el extremo A)
		pygame.draw.circle(screen, (100, 100, 100), (int(pa_world.x), int(pa_world.y)), RADIUS_BOLA)
		pygame.draw.circle(screen, (0, 0, 0), (int(pa_world.x), int(pa_world.y)), RADIUS_BOLA, 2)

		# Punto del CM (la posición física del body)
		if TRAYECTORIA_CM:
			pygame.draw.circle(screen, (220, 20, 60), (int(body.position.x), int(body.position.y)), 4)

		dt = (1.0 / FPS) * SLOW_MO
		space.step(dt)
		
		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	solve()
