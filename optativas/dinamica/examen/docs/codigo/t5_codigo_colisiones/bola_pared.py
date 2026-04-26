import pygame
import pymunk
import pymunk.pygame_util

# Colores
COLOR_BOLA = (200, 50, 50)
COLOR_PARED = (100, 100, 100)
COLOR_SUELO = (50, 50, 50)

def run_simulation():
	pygame.init()
	# Dimensiones solicitadas: 1000x600 (asumo 1000 por el contexto de las posiciones x=700)
	width, height = 1000, 600 
	screen = pygame.display.set_mode((width, height))
	clock = pygame.time.Clock()
	font = pygame.font.SysFont("Arial", 18, bold=True)

	space = pymunk.Space()
	space.gravity = (0, 900)
	#space.gravity = (0, 0)
	
	# --- CONFIGURACIÓN DE FRICCIONES ---
	FRICCION_SUELO = 0.5
	FRICCION_PARED = 0.5
	FRICCION_BOLA = 0.5
	# -----------------------------------

	# 1. Crear Pared (Rectángulo de 10kg, h=400, w=40)
	m_pared = 20.0
	p_w, p_h = 40, 400
	moment_p = pymunk.moment_for_box(m_pared, (p_w, p_h))
	body_p = pymunk.Body(m_pared, moment_p)
	# Posicionado en x=700, apoyado en el suelo (y = 600 - 80 - p_h/2)
	body_p.position = (700, height - 80 - p_h/2)
	shape_p = pymunk.Poly.create_box(body_p, (p_w, p_h))
	shape_p.friction = FRICCION_PARED
	space.add(body_p, shape_p)

	# 2. Crear Bola (1kg, lanzada desde x=100, y=300 a 100px/s)
	m_bola = 1.0
	r_bola = 15
	moment_b = pymunk.moment_for_circle(m_bola, 0, r_bola)
	body_b = pymunk.Body(m_bola, moment_b)
	body_b.position = (100, 200)
	shape_b = pymunk.Circle(body_b, r_bola)
	shape_b.friction = FRICCION_BOLA
	space.add(body_b, shape_b)
	
	################### FISICA ######################
	body_b.velocity = (500, -500) 
	#body_b.velocity = (500,0)
	shape_b.elasticity = 1.0
	shape_p.elasticity = 0
	print(shape_p.elasticity)
	######################################

	# 3. Suelo estático
	ground_y = height - 80
	ground = pymunk.Segment(space.static_body, (0, ground_y), (width, ground_y), 5)
	ground.friction = FRICCION_SUELO
	space.add(ground)

	#momento lineal inicial
	P0=body_b.mass*body_b.velocity

	FPS=60
	substep=100
	dt=1.0/FPS
	dt_sub=dt/substep
	while True:
		for event in pygame.event.get():
			if event.type == pygame.QUIT: return
			if event.type == pygame.KEYDOWN:
				if event.key == pygame.K_ESCAPE:
					# Reiniciar posiciones
					body_b.position = (100, 300)
					body_b.velocity = (100, 0)
					body_b.angle = 0
					body_p.position = (700, height - 80 - p_h/2)
					body_p.velocity = (0, 0)
					body_p.angle = 0
					body_p.angular_velocity = 0


		for _ in range(substep):
			space.step(dt_sub)
			
		screen.fill((245, 245, 245))

		# Dibujo del Suelo
		pygame.draw.line(screen, COLOR_SUELO, (0, ground_y), (width, ground_y), 5)

		# Dibujo de la Pared (Rectángulo)
		p_pts = []
		for v in shape_p.get_vertices():
			pos = body_p.position + v.rotated(body_p.angle)
			p_pts.append((pos.x, pos.y))
		pygame.draw.polygon(screen, COLOR_PARED, p_pts)
		pygame.draw.polygon(screen, (0, 0, 0), p_pts, 2)

		# Dibujo de la Bola
		pos_b = (int(body_b.position.x), int(body_b.position.y))
		pygame.draw.circle(screen, COLOR_BOLA, pos_b, r_bola)
		# Línea para ver la rotación de la bola
		vec_rot = pygame.math.Vector2(r_bola, 0).rotate_rad(body_b.angle)
		pygame.draw.line(screen, (255, 255, 255), pos_b, (pos_b[0] + vec_rot.x, pos_b[1] + vec_rot.y), 2)

		#momento lineal
		Pb=body_b.mass*body_b.velocity
		Pp=body_p.mass*body_p.velocity
		P=Pb+Pp
		# UI - Instrucciones
		x0=20
		y0=20
		dy=25
		screen.blit(font.render(f"Velocidad Bola: {body_b.velocity.length:.1f} px/s", True, (0,0,0)), (x0, y0))
		screen.blit(font.render(f"Momento lineal inicial: ({P0.x:.2f},{P0.y:.2f}) kg·px/s ", True, (50, 50, 50)), (x0, y0+1*dy))
		screen.blit(font.render(f"Momento lineal bola:   ({Pb.x:.2f},{Pb.y:.2f}) kg·px/s ", True, (50, 50, 50)), (x0, y0+2*dy))
		screen.blit(font.render(f"Momento lineal pared:   ({Pp.x:.2f},{Pp.y:.2f}) kg·px/s ", True, (50, 50, 50)), (x0, y0+3*dy))
		screen.blit(font.render(f"Momento lineal TOTAL:   ({P.x:.2f},{P.y:.2f}) kg·px/s ", True, (50, 50, 50)), (x0, y0+4*dy))
		screen.blit(font.render("ESC: Reiniciar lanzamiento", True, (50, 50, 50)), (x0, y0+6*dy))


		pygame.display.flip()
		clock.tick(FPS)

if __name__ == "__main__":
	run_simulation()
