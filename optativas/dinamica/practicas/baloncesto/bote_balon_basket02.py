import pygame
import pymunk

# Configuración del entorno
WIDTH, HEIGHT = 1000, 600
PX_M = 300
FPS = 60
SUBSTEPS = 100  # Número de pasos físicos por frame de Pygame
BALL_RADIUS = 0.119 * PX_M
Y_REPOSO = HEIGHT - (0.5 * PX_M) # 1 metro desde el suelo
X_REPOSO = WIDTH/2

def main():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()
	space = pymunk.Space()
	space.gravity = (0, 9.81 * PX_M)

	# 1. Suelo
	ground = pymunk.Segment(space.static_body, (0, HEIGHT), (WIDTH, HEIGHT), 5)
	ground.elasticity = 0.8
	space.add(ground)

	# 2. Balón (en el suelo)
	mass_ball = 0.625
	moment_ball = pymunk.moment_for_circle(mass_ball, 0, BALL_RADIUS)
	ball_body = pymunk.Body(mass_ball, moment_ball)
	# Posicionado justo encima del suelo
	ball_body.position = (WIDTH / 2, HEIGHT - BALL_RADIUS - 10)
	ball_shape = pymunk.Circle(ball_body, BALL_RADIUS)
	ball_shape.elasticity = 0.8
	ball_shape.collision_type = 1
	space.add(ball_body, ball_shape)
	
	# Velocidad hacia ARRIBA (negativa en el eje Y de Pygame)
	ball_body.velocity = (0, -7.0 * PX_M)

	# 3. Rectángulo (Mano) - Dinámico para que el muelle le afecte
	mano_w, mano_h = 0.2 * PX_M, 0.03 * PX_M
	mass_mano = 0.1
	moment_mano = pymunk.moment_for_box(mass_mano, (mano_w, mano_h))
	mano_body = pymunk.Body(mass_mano, moment_mano)
	mano_body.position = (X_REPOSO, Y_REPOSO)
	
	mano_shape = pymunk.Poly.create_box(mano_body, (mano_w, mano_h))
	mano_shape.collision_type = 3
	mano_shape.elasticity = 0.0
	space.add(mano_body, mano_shape)

	# 4. El Muelle (Invisible)
	anchor = pymunk.Body(body_type=pymunk.Body.STATIC)
	anchor.position = (WIDTH / 2, Y_REPOSO)
	
	# Ajustamos stiffness y damping para que sea más firme con el substepping
	stiffness=1
	spring = pymunk.DampedSpring(anchor, mano_body, (0, 0), (0, 0), 0, 40, 0)
	space.add(spring)

	# Restricción para que solo se mueva verticalmente
	groove = pymunk.GrooveJoint(anchor, mano_body, (0, -HEIGHT), (0, HEIGHT), (0, 0))
	space.add(groove)

	def begin(arbiter, space, data):
		s1, s2 = arbiter.shapes # s1 es la bola, s2 es la mano
		
		# IGUALAR VELOCIDADES: La mano adopta la velocidad de la bola
		# Esto elimina el choque seco y permite que el muelle tome el control
		s2.body.velocity = s1.body.velocity
		
		# Relajamos el muelle para que la compresión sea natural
		spring.stiffness = 5
		spring.damping = 2
		
		print("Contacto iniciado - Velocidades sincronizadas")
		return True

	def post_solve(arbiter, space, data):
		global Y_REPOSO
		# Se ejecuta después de calcular los impulsos
		pass

	def separate(arbiter, space, data):
		s1, s2 = arbiter.shapes
		
		# Al separarse, la bola mantiene su impulso de salida
		vx, vy = s1.body.velocity
		s1.body.velocity = (vx, vy+ 5 * PX_M)
		
		# FRENADO SECO: La mano se congela y vuelve al equilibrio
		s2.body.velocity = (0, 0)
		spring.stiffness = 100
		spring.damping = 100
		
		print("Contacto terminado - Retorno seco")

	# Registro directo con tu sintaxis
	space.on_collision(1, 3, begin=begin, post_solve=post_solve, separate=separate)

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# --- SUBSTEPPING ---
		# Dividimos el dt del frame entre el número de substeps
		dt = 1.0 / FPS / SUBSTEPS
		for _ in range(SUBSTEPS):
			space.step(dt)

		# --- RENDERIZADO ---
		screen.fill((255, 255, 255))

		# Dibujar Balón
		p = ball_body.position
		pygame.draw.circle(screen, (255, 100, 0), (int(p.x), int(p.y)), int(BALL_RADIUS))

		# Dibujar Rectángulo (Mano)
		vertices = []
		for v in mano_shape.get_vertices():
			# Aplicar rotación y posición manualmente para el dibujo
			x, y = v.rotated(mano_body.angle) + mano_body.position
			vertices.append((x, y))
		pygame.draw.polygon(screen, (100, 100, 100), vertices)

		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	main()
