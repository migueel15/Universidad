'''En este script se ve cómo el intercambio de momento se lleva a cabo sólo
en la dirección de la línea de acción. La bola blanca está inicialmente
parada y después de la colisión sólo tiene velocidad en la dirección de
la linea de acción.

SIN EMBARGO, esto cambia si hay fricción.

'''

import pygame
import pymunk
from pymunk import Vec2d



#-------------------------------------------------
class Bola(pymunk.Body):
	def __init__(self, space, pos, r, mass, friccion=0.1, elasticidad=0.7, color=(255, 255, 255)):
		# 1. Inicializar el Body (herencia)
		# Calculamos el momento de inercia para una esfera (disco en 2D)
		moment = pymunk.moment_for_circle(mass, 0, r)
		super().__init__(mass, moment)
		
		self.position = pos
		self.radius = r
		self.color = color
		
		# 2. Crear y configurar el Shape
		self.shape = pymunk.Circle(self, r)
		self.shape.friction = friccion
		self.shape.elasticity = elasticidad
		self.shape.collision_type = 1
		
		self.shape.parent=self
		
		# Guardamos una referencia al objeto Bola dentro del shape 
		# para recuperarlo en los collision handlers
		self.shape.parent = self
		
		# 3. Añadir al espacio
		space.add(self, self.shape)

	def asignar_velocidad(self, v_lineal, v_angular):
		"""
		v_lineal: puede ser una tupla (x, y) o un Vec2d
		v_angular: float (radianes/s)
		"""
		self.velocity = v_lineal
		self.angular_velocity = v_angular

	def dibujar(self, screen,debug=True):
		"""
		px_m: factor de conversión metros a píxeles (si se usa)
		"""
		# Convertimos posición a píxeles
		pos_px = (int(self.position.x * px_m), int(self.position.y * px_m))
		rad_px = int(self.radius * px_m)
		
		# Dibujar el círculo principal
		pygame.draw.circle(screen, self.color, pos_px, rad_px)
		# Dibujar el contorno
		pygame.draw.circle(screen, (0, 0, 0), pos_px, rad_px, 2)
		
		# Dibujar una línea para visualizar la rotación, si debug=True
		# Usamos el ángulo actual del Body
		if debug:
			start_line = self.position
			# Vector que apunta hacia el borde según el ángulo
			end_line = self.position + pymunk.Vec2d(self.radius, 0).rotated(self.angle)
		
			pygame.draw.line(screen, (0, 0, 0), 
						 (int(start_line.x * px_m), int(start_line.y * px_m)), 
						 (int(end_line.x * px_m), int(end_line.y * px_m)), 2)
		#----------------------------------------------				 

#-----------------------------------------------------
def dibuja_linea_de_accion(screen,p1,p2):
	pygame.draw.line(screen,(180,180,180),p1*px_m,p2*px_m,1)
		
#--------------------------
def escribir(screen, texto, x, y, color=(255, 255, 255)):
	# Crea la fuente y el dibujo en un solo paso
	f = pygame.font.SysFont(None, 25).render(str(texto), True, color)
	screen.blit(f, (x, y))
		
#------------------------------------------------
#iniciamos el espacio y creamos los handlers de colisión
def inicia_espacio(WIDTH=1000,HEIGHT=600):
	pygame.init()
	screen = pygame.display.set_mode((WIDTH,HEIGHT))
	clock = pygame.time.Clock()
	
	# Espacio de Pymunk
	space = pymunk.Space()
	# Sin gravedad (vista cenital)
	space.gravity = (0, 0)
	space.damping = 1 #0.99 #Frena un 1% la velocidad lineal cada segundo
	
	#función que actúa la inicio de la colisión
	def begin(arbiter, space, data):
		s1, s2 = arbiter.shapes
		b1,b2 =s1.parent,s2.parent
		print(f'Antes del choque:\nBola 1: ({b1.velocity.x*100:.2f},{b1.velocity.y*100:.2f}) cm/s Bola 2: ({b2.velocity.x*100:.2f},{b2.velocity.y*100:.2f}) cm/s')
		return True
		
	#función que actúa después de la colisión	
	def separate(arbiter, space, data):
		global p1,p2,linea_de_accion
		
		linea_de_accion=True
		s1, s2 = arbiter.shapes
		b1,b2 =s1.parent,s2.parent
		print(f'Después del choque:\nBola 1: ({b1.velocity.x*100:.2f},{b1.velocity.y*100:.2f}) cm/s Bola 2: ({b2.velocity.x*100:.2f},{b2.velocity.y*100:.2f}) cm/s')
		v=b2.position-b1.position			
		p1=b1.position-3*v
		p2=b2.position+3*v
		linea_de_accion=True
		dibuja_linea_de_accion(screen,p1,p2)
		pygame.display.flip()
		salir=False
		while not(salir):
			for event in pygame.event.get():
				if event.type == pygame.KEYDOWN:
					if event.key == pygame.K_SPACE:
						salir=True
		return True	
	# Ambos tipos iguales
	space.on_collision(1, 1, begin=begin,separate=separate)
	
	
	#esto es equivalente a:
	# Es equivalente al método tradicional:
	#handler = space.add_collision_handler(1, 1)
	#handler.begin = begin
	#handler.post_solve = post_solve
	return space,screen,clock
#-------------------------------------------------------------	
	
################################################################
################################################################

#iniciamos el espacio y creamos los handlers de colisión
WIDTH=1000
HEIGHT=600
px_m=2000
space,screen,clock=inicia_espacio(WIDTH,HEIGHT)
# variables globales para el dibujo de la linea de accion
p1=Vec2d(0,0)
p2=Vec2d(0,0)
#creamos las bolas
##################################################
########## aquí se puede cambiar el comportamiento
fr=0.0
elas=1.0
##################################################

mass=0.2
bola1=Bola(space,pos=(350/px_m,300/px_m),r=0.03,mass=mass,
			friccion=fr,elasticidad=elas)
bola2=Bola(space,pos=(800/px_m,200/px_m),r=0.03,mass=mass,
			friccion=fr,elasticidad=elas,color=(135, 206, 235))
bola2.velocity=(-0.20,0)

FPS=60
dt=1.0/FPS
substep=100
dt_sub=dt/substep
linea_de_accion=False
running=True
while running:
	for event in pygame.event.get():
		if event.type == pygame.QUIT: running = False
	screen.fill((34, 139, 34))
	bola1.dibujar(screen,debug=True)
	bola2.dibujar(screen,debug=True)
	x0=650
	y0=400
	dy=22
	escribir(screen,f'Bola 1: v = {bola1.velocity.length*100:.2f} cm/s',650,y0)
	escribir(screen,f'Bola 2: v = {bola2.velocity.length*100:.2f} cm/s',650,y0+dy)
	P=mass*(bola1.velocity+bola2.velocity)*100
	escribir(screen,f'M. Lineal = ({P.x:.2f},{P.y:.2f}) kg·cm/s',650,y0+2*dy)
	Ect=0.5*bola1.mass*(bola1.velocity.length**2+bola2.velocity.length**2)*1000
	Ecr=0.5*bola1.moment*(bola1.angular_velocity**2+bola2.angular_velocity**2)*1000
	Ectot=Ect+Ecr
	escribir(screen,f'Ec Trasl. = {Ect:.2f} mJ',650,y0+3*dy)
	escribir(screen,f'Ec Rot.   = {Ecr:.2f} mJ',650,y0+4*dy)
	escribir(screen,f'Ec TOT.   = {Ectot:.2f} mJ',650,y0+5*dy)
	
	if linea_de_accion:
		dibuja_linea_de_accion(screen,p1,p2)
	for _ in range(substep):	
		space.step(dt_sub)
	pygame.display.flip()
	clock.tick(FPS)
	

pygame.quit()
	


		
