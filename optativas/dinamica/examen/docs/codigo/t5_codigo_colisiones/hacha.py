'''Lanza un hacha con velocidad horizontal (no hay gravedad) y se le imprime también
una velocidad angular tal que el extremo del mango parece estar parado al iniciar el
movimiento (simulando un lanzamiento real)
Se puede pintar la trayectoria del extremo del mango y del CM
Si se quiere, tiene slowmotion'''

import pygame
import pymunk
from pymunk import Vec2d
import math

# --- CONFIGURACIÓN ---
WIDTH, HEIGHT = 1000, 600
TRAYECTORIA = False
TRAYECTORIA_CM = False
PARED_X = 900

# --- PARÁMETROS DEL HACHA ---
FPS=60.0
slowmotion=1
escala=0.43
L_MANGO = 350*escala
F_CM = 0.2	 # CM cerca de la cabeza
MASS = 2.0
VEL_ANGULAR = 8.3 # Subida para que rote más antes de chocar

def solve():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()

	space = pymunk.Space()
	space.gravity = (0, 0)

	# 1. CÁLCULO DE INERCIA
	d_centro_al_cm = abs(L_MANGO/2 - (F_CM * L_MANGO))
	moment = (1/12) * MASS * L_MANGO**2 + MASS * d_centro_al_cm**2
	
	body = pymunk.Body(MASS, moment)
	body.position = (100, HEIGHT // 2)
	
	p_cabeza = Vec2d(0, -F_CM * L_MANGO)
	p_mango_fin = Vec2d(0, (1 - F_CM) * L_MANGO)
	
	v1 = p_cabeza + Vec2d(0, -30)*escala
	v2 = p_cabeza + Vec2d(90, 60)*escala
	v3 = p_cabeza + Vec2d(0, 120)*escala
	
	mango_shape = pymunk.Segment(body, p_cabeza, p_mango_fin, 4)
	cabeza_shape = pymunk.Poly(body, [v1, v2, v3])
	space.add(body, mango_shape, cabeza_shape)

	#body.velocity = (450, 0)
	v_base = (900, 0)
	body.angular_velocity = VEL_ANGULAR
	
	# --- EL TRUCO PARA CAMBIAR EL CENTRO DE GIRO ---
	r=p_mango_fin #vector de posición del punto que está fijo al salir
	body.velocity = Vec2d(body.angular_velocity * r.y,-body.angular_velocity * r.x) #-omega x r
	
	

	cm_path = []
	extremo_path = []
	congelada = False

	running = True
	dt=1/60.0*slowmotion
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# --- LÓGICA DE LA PARED ---
		# Comprobamos si la punta de la cabeza (v2) ha tocado la pared
		p_punta_world = body.local_to_world(v2)
		
		if not congelada and p_punta_world.x >= PARED_X:
			body.velocity = (0, 0)
			body.angular_velocity = 0
			congelada = True

		if not congelada:
			cm_path.append((int(body.position.x), int(body.position.y)))
			p_ext_world = body.local_to_world(p_mango_fin)
			#p_ext_world = body.local_to_world(p_cabeza)
			extremo_path.append((int(p_ext_world.x), int(p_ext_world.y)))

		# --- DIBUJO ---
		screen.fill((255, 255, 255))
		
		# Dibujar Pared
		pygame.draw.line(screen, (200, 0, 0), (PARED_X, 0), (PARED_X, HEIGHT), 5)
		
		# Trayectorias
		if TRAYECTORIA and len(extremo_path) > 2:
			pygame.draw.lines(screen, (0, 150, 255), False, extremo_path, 2)
		if TRAYECTORIA_CM and len(cm_path) > 2:
			pygame.draw.lines(screen, (255, 50, 50), False, cm_path, 2)

		# Dibujo Hacha
		p_inicio = body.local_to_world(p_cabeza)
		p_fin = body.local_to_world(p_mango_fin)
		pygame.draw.line(screen, (101, 67, 33), p_inicio, p_fin, 8)
		
		cabeza_puntos_world = [body.local_to_world(v) for v in [v1, v2, v3]]
		pygame.draw.polygon(screen, (150, 150, 150), cabeza_puntos_world)
		pygame.draw.polygon(screen, (0, 0, 0), cabeza_puntos_world, 2)

		# Punto del CM
		if TRAYECTORIA_CM:
			pygame.draw.circle(screen, (255, 0, 0), (int(body.position.x), int(body.position.y)), 4)

		space.step(dt)
		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	solve()
