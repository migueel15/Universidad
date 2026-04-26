import pygame
import pymunk
from pymunk import Vec2d

# --- 1. CONFIGURACIÓN DE ESCALA ---
# Con px_m = 400, una mesa de 2.1m mide 840px. El taco (1.45m) mide 580px.
px_m = 1000 

# --- 2. CLASE BOLA (Dimensiones y Masa Reales) ---
class Bola(pymunk.Body):
	def __init__(self, space, pos, r=0.0285, mass=0.170, friccion=0.1, elasticidad=0.8, color=(255, 255, 255)):
		# Inercia de una esfera
		moment = pymunk.moment_for_circle(mass, 0, r)
		super().__init__(mass, moment)
		
		self.position = pos
		self.radius = r
		self.color = color
		
		self.shape = pymunk.Circle(self, r)
		self.shape.friction = friccion
		self.shape.elasticity = elasticidad
		self.shape.collision_type = 1
		self.shape.parent = self # Referencia para handlers
		
		space.add(self, self.shape)

	def dibujar(self, screen, debug=True):
		pos_px = (int(self.position.x * px_m), int(self.position.y * px_m))
		rad_px = int(self.radius * px_m)
		
		pygame.draw.circle(screen, self.color, pos_px, rad_px)
		pygame.draw.circle(screen, (0, 0, 0), pos_px, rad_px, 2)
		
		if debug:
			# Línea de rotación
			end_line = self.position + Vec2d(self.radius, 0).rotated(self.angle)
			pygame.draw.line(screen, (0, 0, 0), pos_px, 
							 (int(end_line.x * px_m), int(end_line.y * px_m)), 2)

# --- 3. CLASE TACO (Trapecio con Dimensiones Reales) ---
class Taco(pymunk.Body):
	def __init__(self, space, pos, angulo_deg=0):
		self.L = 1.45       # 145 cm
		self.base_w = 0.030 # 30 mm
		self.punta_w = 0.012# 12 mm
		mass = 0.54         # 19 oz aprox
		#mass = 2 #si consideramos la masa del brazo
		
		# Vértices del trapecio (la punta está en el eje X positivo)
		vertices = [
			(-self.L, -self.base_w/2), (-self.L, self.base_w/2),
			(0, self.punta_w/2), (0, -self.punta_w/2)
		]
		
		moment = pymunk.moment_for_poly(mass, vertices)
		super().__init__(mass, moment)
		
		self.position = pos
		self.angle = -angulo_deg * (3.14159 / 180)
		
		self.shape = pymunk.Poly(self, vertices)
		self.shape.friction = 0.7
		self.shape.elasticity = 0.4 
		self.shape.collision_type = 2 # Tipo Taco
		self.shape.parent = self # Referencia para handlers
		
		space.add(self, self.shape)

	def dibujar(self, screen):
		# 1. Obtener todos los vértices transformados al mundo (píxeles)
		puntos_mundo = []
		vertices_locales = self.shape.get_vertices()
		for v in vertices_locales:
			p = self.position + v.rotated(self.angle)
			puntos_mundo.append((int(p.x * px_m), int(p.y * px_m)))
		
		# 2. Dibujar el cuerpo principal (Madera)
		pygame.draw.polygon(screen, (100, 50, 20), puntos_mundo)
		
		# 3. Dibujar el casquillo (extremo blanco de 2 cm)
		# Calculamos los puntos de la punta basándonos en que el origen (0,0) es el extremo
		L_casquillo = 0.02  # 2 cm
		
		# Definimos el trapecio pequeño del casquillo en coordenadas locales
		# (Es el trozo que va de x=-0.02 a x=0)
		v_punta_sup = Vec2d(0, self.punta_w / 2)
		v_punta_inf = Vec2d(0, -self.punta_w / 2)
		# Estimamos un ancho ligeramente mayor hacia atrás para que siga la forma del taco
		v_atras_sup = Vec2d(-L_casquillo, self.punta_w / 1.9) 
		v_atras_inf = Vec2d(-L_casquillo, -self.punta_w / 1.9)
		
		# Transformar estos puntos a la posición y ángulo actual
		puntos_casquillo = []
		for v in [v_punta_sup, v_atras_sup, v_atras_inf, v_punta_inf]:
			p = self.position + v.rotated(self.angle)
			puntos_casquillo.append((int(p.x * px_m), int(p.y * px_m)))
			
		# Pintar el casquillo de blanco (o un blanco roto/crema)
		pygame.draw.polygon(screen, (240, 240, 230), puntos_casquillo)
		
		# 4. Contorno final para que todo quede bien definido
		pygame.draw.polygon(screen, (0, 0, 0), puntos_mundo, 2)

# --- 4. FUNCIONES AUXILIARES ---
def escribir(screen, texto, x, y, color=(255, 255, 255)):
	f = pygame.font.SysFont(None, 24).render(str(texto), True, color)
	screen.blit(f, (x, y))

def inicia_juego():
	pygame.init()
	screen = pygame.display.set_mode((1000, 600))
	clock = pygame.time.Clock()
	space = pymunk.Space()
	space.gravity = (0, 0)
	space.damping = 0.99
	
	#función que actúa la inicio de la colisión
	def antes(arbiter, space, data):
		staco, sbola = arbiter.shapes
		taco,bola =staco.parent,sbola.parent
		P=bola.mass*bola.velocity
		print(f'M.Lineal antes del choque: ({P.x*100:.2f},{P.y*100:.2f}) kg·cm/s')
		return True
		
	#función que actúa después de la colisión	
	def despues(arbiter, space, data):
		staco, sbola = arbiter.shapes
		taco,bola =staco.parent,sbola.parent
		P=bola.mass*bola.velocity
		print(f'M.Lineal despué del choque: ({P.x*100:.2f},{P.y*100:.2f}) kg·cm/s')
		taco.velocity=(0,0)
		return True	
	# Las usamos para la colisión entre taco y bola
	space.on_collision(2, 1, begin=antes,post_solve=despues)
	
	
	
	return space, screen, clock

# --- 5. MAIN ---
space, screen, clock = inicia_juego()

# Instancias
bola1 = Bola(space, pos=(0.3, 0.300),friccion=0.7)
bola2 = Bola(space, pos=(0.7, 0.280),friccion=0.7, color=(135, 206, 235))

# Taco colocado detrás de bola1, apuntando hacia ella
taco = Taco(space, pos=(0.20, 0.310), angulo_deg=0)


running = True
substep = 50
dt = (1/60) / substep

iniciado=False
while running:
	for event in pygame.event.get():
		if event.type == pygame.QUIT: running = False
		if event.type == pygame.KEYDOWN:
			if event.key == pygame.K_SPACE:
				if not(iniciado):
					taco.velocity = (0.5, 0) # Empuje inicial del taco	
				iniciado=True

	screen.fill((34, 139, 34)) # Tapete
	
	# Dibujar
	bola1.dibujar(screen)
	bola2.dibujar(screen)
	taco.dibujar(screen)
	
	# Info
	escribir(screen, f"V_bola1: {bola1.velocity.length:.2f} m/s", 10, 10)
	escribir(screen, f"V_taco: {taco.velocity.length:.2f} m/s", 10, 35)

	# Física
	for _ in range(substep):
		space.step(dt)
		
	pygame.display.flip()
	clock.tick(60)

pygame.quit()
