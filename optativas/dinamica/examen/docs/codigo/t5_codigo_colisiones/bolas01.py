import pygame
import pymunk
import pymunk.pygame_util
import sys

# Configuración
WIDTH, HEIGHT = 1000, 600
GREEN_TABLE = (34, 139, 34)
WHITE = (255, 255, 255)
RED = (200, 0, 0)
FPS = 60
V0 = 250.0
RADIO = 25

class BolaPymunk:
	def __init__(self, space, x, y, color, e, masa=1.0):
		# 1. Crear el cuerpo (Body)
		moment = pymunk.moment_for_circle(masa, 0, RADIO)
		self.body = pymunk.Body(masa, moment)
		self.body.position = (x, y)
		
		# 2. Crear la forma (Shape)
		self.shape = pymunk.Circle(self.body, RADIO)
		self.shape.elasticity = e
		self.shape.friction = 0.0  # Para simular colisión pura en 1D
		self.color = color
		
		# 3. Añadir al espacio
		space.add(self.body, self.shape)

	def dibujar(self, pantalla):
		pos = self.body.position
		pygame.draw.circle(pantalla, self.color, (int(pos.x), int(pos.y)), RADIO)

def main():
	pygame.init()
	pantalla = pygame.display.set_mode((WIDTH, HEIGHT))
	pygame.display.set_caption("Pymunk: Colisiones con diferentes coeficientes")
	reloj = pygame.time.Clock()
	fuente = pygame.font.SysFont("Arial", 18)

	# Inicializar Espacio de Pymunk
	space = pymunk.Space()
	space.gravity = (0, 0)

	# Creación de pares de bolas
	# Nota: Como e_final = e1 * e2, ponemos e=1.0 a las blancas 
	# y el coeficiente deseado a las rojas.
	
	# Fila 1: Elástica (1.0 * 1.0 = 1.0)
	b1_blanca = BolaPymunk(space, 50, 150, WHITE, 1.0)
	b1_roja = BolaPymunk(space, 500, 150, RED, 1.0)
	
	# Fila 2: Inelástica (1.0 * 0.0 = 0.0)
	b2_blanca = BolaPymunk(space, 50, 300, WHITE, 1.0)
	b2_roja = BolaPymunk(space, 500, 300, RED, 0.0)
	
	# Fila 3: Parcial (1.0 * 0.5 = 0.5)
	b3_blanca = BolaPymunk(space, 50, 450, WHITE, 1.0)
	b3_roja = BolaPymunk(space, 500, 450, RED, 0.5)

	bolas = [b1_blanca, b1_roja, b2_blanca, b2_roja, b3_blanca, b3_roja]
	bolas_lanzadas = 0

	while True:
		for evento in pygame.event.get():
			if evento.type == pygame.QUIT:
				pygame.quit()
				sys.exit()
			
			if evento.type == pygame.KEYDOWN:
				if evento.key == pygame.K_SPACE:
					if bolas_lanzadas == 0:
						b1_blanca.body.velocity = (V0, 0)
						bolas_lanzadas += 1
					elif bolas_lanzadas == 1:
						b2_blanca.body.velocity = (V0, 0)
						bolas_lanzadas += 1
					elif bolas_lanzadas == 2:
						b3_blanca.body.velocity = (V0, 0)
						bolas_lanzadas += 1

		# Actualizar el motor físico
		# Usamos un paso fijo para mayor estabilidad
		dt = 1.0 / FPS
		space.step(dt)

		# Dibujo
		pantalla.fill(GREEN_TABLE)
		
		pantalla.blit(fuente.render("Pulsa ESPACIO para lanzar", True, WHITE), (20, 20))
		pantalla.blit(fuente.render("Elástica (e=1.0)", True, WHITE), (550, 90))
		pantalla.blit(fuente.render("Inelástica (e=0.0)", True, WHITE), (550, 240))
		pantalla.blit(fuente.render("Parcial (e=0.5)", True, WHITE), (550, 390))

		for b in bolas:
			b.dibujar(pantalla)

		pygame.display.flip()
		reloj.tick(FPS)

if __name__ == "__main__":
	main()
