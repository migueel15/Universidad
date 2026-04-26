import pygame
import pymunk
import pymunk.pygame_util

# Colores
COLOR_CARROCERIA = (173, 216, 230)  # Azul Claro Pastel
COLOR_VENTANA = (255, 255, 255)     # Blanco
COLOR_CROMO = (150, 150, 150)       # Gris metálico
COLOR_RUEDA = (40, 40, 40)          # Negro neumático
COLOR_PLATAFORMA = (100, 100, 100)  # Gris oscuro

def draw_sedan_blue(screen, body, w, h):
	# 1. Base del chasis
	pts_base_local = [(-w/2, -h/2), (w/2, -h/2), (w/2, h/2), (-w/2, h/2)]
	
	# 2. Cabina
	cabina_h = h * 0.8
	base_grande = w * 0.8
	base_pequeña = w * 0.5
	pts_cabina_local = [
		(-base_grande/2, -h/2),
		(base_grande/2, -h/2),
		(base_pequeña/2, -h/2 - cabina_h),
		(-base_pequeña/2, -h/2 - cabina_h)
	]

	# 3. Ventanas
	v_margin = 6
	pts_v1 = [
		(-base_grande/2 + 10, -h/2 - 5), 
		(-2, -h/2 - 5), 
		(-2, -h/2 - cabina_h + v_margin), 
		(-base_pequeña/2 + 8, -h/2 - cabina_h + v_margin)
	]
	pts_v2 = [
		(2, -h/2 - 5), 
		(base_grande/2 - 10, -h/2 - 5), 
		(base_pequeña/2 - 8, -h/2 - cabina_h + v_margin), 
		(2, -h/2 - cabina_h + v_margin)
	]

	def transform(pts):
		out = []
		for p in pts:
			v = pygame.math.Vector2(p).rotate_rad(body.angle)
			out.append((body.position.x + v.x, body.position.y + v.y))
		return out

	pygame.draw.polygon(screen, COLOR_CARROCERIA, transform(pts_cabina_local))
	pygame.draw.polygon(screen, COLOR_VENTANA, transform(pts_v1))
	pygame.draw.polygon(screen, COLOR_VENTANA, transform(pts_v2))
	pygame.draw.polygon(screen, COLOR_CARROCERIA, transform(pts_base_local))
	pygame.draw.polygon(screen, (0, 0, 0), transform(pts_base_local), 2)

def run_simulation():
	pygame.init()
	width, height = 1200, 600
	screen = pygame.display.set_mode((width, height))
	clock = pygame.time.Clock()
	font = pygame.font.SysFont("Arial", 18, bold=True)

	space = pymunk.Space()
	space.gravity = (0, 900)
	
	# --- CONFIGURACIÓN DE FRICCIONES ---
	FRICCION_SUELO = 0.2
	FRICCION_PLATAFORMA = 0.5  # Fricción entre plataforma y suelo
	FRICCION_RUEDAS = 0.8      # Fricción entre ruedas y plataforma
	# -----------------------------------

	# Parámetros Coche
	pos_i = (100, height - 250)
	chasis_w, chasis_h = 170, 45
	R = 30
	m_chasis, m_rueda = 25.0, 1.2

	# Crear Plataforma Deslizable
	plat_w, plat_h = 900, 20
	m_plat = 10.0
	body_p = pymunk.Body(m_plat, pymunk.moment_for_box(m_plat, (plat_w, plat_h)))
	body_p.position = (350+pos_i[0], height - 100)
	shape_p = pymunk.Poly.create_box(body_p, (plat_w, plat_h))
	shape_p.friction = FRICCION_PLATAFORMA
	space.add(body_p, shape_p)

	# Crear Chasis
	body_c = pymunk.Body(m_chasis, pymunk.moment_for_box(m_chasis, (chasis_w, chasis_h)))
	body_c.position = pos_i
	shape_c = pymunk.Poly.create_box(body_c, (chasis_w, chasis_h))
	shape_c.filter = pymunk.ShapeFilter(group=1)
	space.add(body_c, shape_c)

	# Crear Ruedas
	def add_w(pos, f):
		b = pymunk.Body(m_rueda, pymunk.moment_for_circle(m_rueda, 0, R))
		b.position = pos
		s = pymunk.Circle(b, R)
		s.friction = f
		s.elasticity = 0.9
		s.filter = pymunk.ShapeFilter(group=1)
		space.add(b, s)
		return b

	body_rt = add_w((pos_i[0]-60, pos_i[1]+30), FRICCION_RUEDAS)
	body_rd = add_w((pos_i[0]+60, pos_i[1]+30), FRICCION_RUEDAS)

	space.add(pymunk.PivotJoint(body_c, body_rt, body_rt.position))
	space.add(pymunk.PivotJoint(body_c, body_rd, body_rd.position))

	# Suelo estático
	ground = pymunk.Segment(space.static_body, (0, height - 80), (10000, height - 80), 5)
	ground.friction = FRICCION_SUELO
	ground.elasticity = 0.9
	space.add(ground)

	applied_torque = 0.0
	torque_step = 15000.0

	while True:
		for event in pygame.event.get():
			if event.type == pygame.QUIT: return
			if event.type == pygame.KEYDOWN:
				if event.key == pygame.K_UP:
					applied_torque = 0.0 if applied_torque < 0 else applied_torque + torque_step
				if event.key == pygame.K_DOWN:
					applied_torque = 0.0 if applied_torque > 0 else applied_torque - torque_step
				if event.key == pygame.K_ESCAPE:
					body_c.position = pos_i; body_c.velocity = (0,0); body_c.angle = 0
					body_rt.position = (pos_i[0]-60, pos_i[1]+30); body_rt.velocity = (0,0); body_rt.angular_velocity = 0
					body_rd.position = (pos_i[0]+60, pos_i[1]+30); body_rd.velocity = (0,0); body_rd.angular_velocity = 0
					body_p.position = (350+pos_i[0], height - 100); body_p.velocity = (0,0); body_p.angle = 0
					applied_torque = 0.0

		frenando = pygame.key.get_pressed()[pygame.K_SPACE]
		if frenando:
			for b in [body_rt, body_rd]: b.angular_velocity *= 0.1
			applied_torque = 0.0
		else:
			if applied_torque < 0 and body_rt.angular_velocity <= 0.05:
				body_rt.torque = 0; applied_torque = 0.0
			else:
				body_rt.torque = applied_torque

		space.step(1/60.0)
		screen.fill((245, 245, 245))

		# Dibujo de la plataforma
		p_pts = []
		for v in shape_p.get_vertices():
			pos = body_p.position + v.rotated(body_p.angle)
			p_pts.append((pos.x, pos.y))
		pygame.draw.polygon(screen, COLOR_PLATAFORMA, p_pts)
		pygame.draw.polygon(screen, (0, 0, 0), p_pts, 2)

		# Dibujo del coche
		draw_sedan_blue(screen, body_c, chasis_w, chasis_h)
		
		# Dibujo de ruedas
		for b in [body_rt, body_rd]:
			pygame.draw.circle(screen, COLOR_RUEDA, (int(b.position.x), int(b.position.y)), R)
			pygame.draw.circle(screen, COLOR_CROMO, (int(b.position.x), int(b.position.y)), int(R*0.5))
			v = pygame.math.Vector2(R, 0).rotate_rad(b.angle)
			pygame.draw.line(screen, (255, 255, 255), b.position, b.position + v, 2)

		# Suelo y UI
		pygame.draw.line(screen, (50, 50, 50), (0, height-80), (width, height-80), 5)
		
		# Textos
		screen.blit(font.render(f"Torque: {applied_torque/1000:.1f} kNm", True, (0,0,0)), (20, 20))
		screen.blit(font.render(f"Velocidad Coche: {abs(body_c.velocity.x)/10:.1f} km/h", True, (0,0,0)), (20, 45))
		
		# Instrucciones
		instr_color = (100, 50, 50)
		x0=width-350
		y0=20
		dy=35
		screen.blit(font.render("CONTROLES:", True, instr_color), (x0,y0))
		screen.blit(font.render("FLECHA ARRIBA: Aumentar Torque", True, (50, 50, 50)), (x0,y0+dy))
		screen.blit(font.render("FLECHA ABAJO: Disminuir Torque", True, (50, 50, 50)), (x0,y0+2*dy))
		screen.blit(font.render("ESPACIO: Frenar Ruedas", True, (50, 50, 50)), (x0,y0+3*dy))
		screen.blit(font.render("ESC: Reiniciar Posición", True, (50, 50, 50)), (x0,y0+4*dy))

		pygame.display.flip()
		clock.tick(60)

run_simulation()
