''' Aplicamos las fuerzas manualmente para provocar la explosión y tenemos
que comprobar que se cumple la Tercera Ley de Newton'''

import pygame
import pymunk
from pymunk import Vec2d

# Configuración inicial
WIDTH, HEIGHT = 1000, 600
FPS = 60
PX_M = 100	# Píxeles por metro
G_GRAVITY = 0.0

def to_pygame(pos):
	return int(pos.x * PX_M), int(HEIGHT - pos.y * PX_M)

class Simulation:
	def __init__(self):
		pygame.init()
		self.screen = pygame.display.set_mode((WIDTH, HEIGHT))
		pygame.display.set_caption("Conservación del Momento Lineal - CM")
		self.clock = pygame.time.Clock()
		self.running = True
		
		# Inicializar fuente para la etiqueta CM
		self.font = pygame.font.SysFont("Arial", 18, bold=True)
		
		# Espacio físico
		self.space = pymunk.Space()
		self.space.gravity = (0, G_GRAVITY)
		
		# Posición inicial y velocidad común
		x0, y0 = 1.0, 1.0
		v0 = Vec2d(1.2, 0.4)
		#v0 =(0,0)
		
		self.balls = []
		mass = 1.0
		# CAMBIO: El doble del tamaño anterior (0.05 -> 0.1)
		self.radius = 0.1 
		moment = pymunk.moment_for_circle(mass, 0, self.radius)
		
		# Crear 3 bolas
		offsets = [Vec2d(0, 0), Vec2d(0.01, 0.01), Vec2d(-0.01, -0.01)]
		for i in range(3):
			body = pymunk.Body(mass, moment)
			body.position = Vec2d(x0, y0) + offsets[i]
			body.velocity = v0
			shape = pymunk.Circle(body, self.radius)
			shape.elasticity = 0.9
			shape.friction = 0.5
			self.space.add(body, shape)
			self.balls.append(body)
			
		self.cm_history = []

	def apply_internal_impulses(self):
		p01 = Vec2d(3.0, 6.0)  #fuerza de 0 sobre 1
		p12 = Vec2d(-5.0, 3.0) #fuerza de 1 sobre 2
		p02 = Vec2d(4.0, -2.0) #fuerza de 0 sobre 2
		
		############### APLICAMOS LAS FUERZAS COMPROBANDO #########
		############ QUE SE CUMPLE LA TERCERA LEY DE NEWTON #######
		self.balls[0].apply_impulse_at_local_point(-p01 - p02)
		self.balls[1].apply_impulse_at_local_point(p01 - p12)
		self.balls[2].apply_impulse_at_local_point(p12 + p02)

	def get_cm(self):
		total_mass = sum(b.mass for b in self.balls)
		weighted_pos = sum((b.position * b.mass for b in self.balls), Vec2d(0, 0))
		return weighted_pos / total_mass

	def run(self):
		while self.running:
			for event in pygame.event.get():
				if event.type == pygame.QUIT:
					self.running = False
				if event.type == pygame.KEYDOWN:
					if event.key == pygame.K_SPACE:
						self.apply_internal_impulses()
			
			dt = 1.0 / FPS
			self.space.step(dt)
			
			current_cm = self.get_cm()
			cm_screen_pos = to_pygame(current_cm)
			self.cm_history.append(cm_screen_pos)
			
			self.screen.fill((255, 255, 255))
			
			# Rastro del CM
			if len(self.cm_history) > 1:
				pygame.draw.lines(self.screen, (200, 200, 200), False, self.cm_history, 2)
			
			# Dibujar bolas
			colors = [(220, 20, 60), (34, 139, 34), (0, 0, 205)]
			for i, body in enumerate(self.balls):
				pos = to_pygame(body.position)
				# Usamos self.radius para que el dibujo sea exacto a la física
				r_px = int(self.radius * PX_M)
				pygame.draw.circle(self.screen, colors[i], pos, r_px)
				pygame.draw.circle(self.screen, (0, 0, 0), pos, r_px, 1)
				
			# Dibujar punto del CM
			pygame.draw.circle(self.screen, (0, 0, 0), cm_screen_pos, 2)
			
			# Renderizar y posicionar etiqueta "CM"
			text_img = self.font.render("CM", True, (0, 0, 0))
			self.screen.blit(text_img, (cm_screen_pos[0] + 12, cm_screen_pos[1] + 6))
			
			pygame.display.flip()
			self.clock.tick(FPS)
		pygame.quit()

if __name__ == "__main__":
	Simulation().run()
