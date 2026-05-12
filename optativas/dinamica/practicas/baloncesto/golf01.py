import pygame
import pymunk
from pymunk import Vec2d
import pymunk.pygame_util
import numpy as np



# Configuración de la ventana
WIDTH, HEIGHT = 1000, 600
FPS = 60
NIVEL_DEL_SUELO=HEIGHT-62 #PIXELES 




#escala
PX_M=5 #5px por metro
M_PX=1.0/PX_M

##################################
def xy2pixel(p):
	return [p[0]*PX_M,NIVEL_DEL_SUELO-p[1]*PX_M]

def pixel2xy(p):
	return [p[0]*M_PX,(NIVEL_DEL_SUELO-p[1])*M_PX]
##################################



#------------------------------------
RADIO_m=0.0213  #2.13 cm
RADIO_PX=RADIO_m*PX_M  # en pixeles
RADIO_HOYO_m=0.054 #5.4 cm
HOYO_m=181.6    #coordenada X del hoyo
MASA_BOLA=0.045
#####################################

# Masas convertidas a kg (Masa / 1000)
masas_palos = [
	0.200, 0.205, 0.210, 0.215, 0.220,  # Maderas
	0.240, 0.247, 0.254, 0.261, 0.268,  # Hierros 3-7
	0.275, 0.282, 0.290, 0.300          # Hierros 8-SW
]

# Ángulos de Loft en grados
lofts = [
	11, 13, 15, 17, 19,  # Maderas
	21, 24, 27, 31, 35,  # Hierros 3-7
	39, 43, 48, 55       # Hierros 8-SW
]

# Nombres de los palos por si necesitas una etiqueta visual
nombres_palos = [
	"Driver", "2-wood", "3-wood", "4-wood", "5-wood",
	"3-iron", "4-iron", "5-iron", "6-iron", "7-iron",
	"8-iron", "9-iron", "PW", "SW"
]



#####################################

def run():
	# 1. Inicializar Pygame
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()
	draw_options = pymunk.pygame_util.DrawOptions(screen)

	# 2. Cargar y ajustar imagen de fondo
	# Cargamos la imagen y la escalamos para que cubra los 1000px de ancho
	try:
		bg_image = pygame.image.load("calle_golf.png")
		bg_width, bg_height = bg_image.get_size()
		bg_width*=0.7
		bg_height*=0.6
		# Ajuste proporcional: el ancho es 1000, calculamos el alto correspondiente
		aspect_ratio = bg_height / bg_width
		new_height = int(WIDTH * aspect_ratio)
		bg_image = pygame.transform.scale(bg_image, (WIDTH, new_height))
		# Posición: pegado a la parte inferior
		bg_pos = (0, HEIGHT - new_height)
	except pygame.error:
		print("No se pudo cargar la imagen calle_golf.png. Se usará fondo negro.")
		bg_image = None

	# 3. Inicializar Pymunk (Espacio físico)
	space = pymunk.Space()
	space.gravity = (0, 9.81*PX_M)  # Gravedad hacia abajo (ajusta el valor según tu escala)

###################################################
#A) ponemos el suelo con un agujero (dos trozos de suelo)
	# Coordenadas del hoyo en el eje X
	suelo_body=space.static_body
	# Segmento del green ANTES del hoyo
	suelo1 = pymunk.Segment(suelo_body,xy2pixel([0,0]) , xy2pixel([HOYO_m-RADIO_m,0]), 5)
	# Segmento del green DESPUÉS del hoyo
	suelo2 = pymunk.Segment(suelo_body,xy2pixel([HOYO_m+RADIO_m,0]) , xy2pixel([WIDTH,0]), 5)
	suelo1.elasticity = 0.25  
	suelo2.elasticity = 0.25  
	suelo1.friction = 0.7     
	suelo2.friction = 0.7     
	space.add(suelo1,suelo2)
###################################################
#B) Creamos la bola
	momento=2*(MASA_BOLA*RADIO_PX**2)/5  #una esfera no un circulo
	RADIO_visual=RADIO_PX*30 #30 veces más grande de la realidad, para que se vea
	bola=pymunk.Body(MASA_BOLA,momento,body_type=pymunk.Body.DYNAMIC)
	bola_shape=pymunk.Circle(bola,RADIO_PX)
	bola_shape.elasticity=0.8
	bola_shape.friction=0.3
	space.add(bola,bola_shape)
		
	bola.position=(xy2pixel((5,RADIO_m)))
	
######################################################
#C) Creamos la cabeza del palo
	alpha0=55*np.pi/180  #el maximo loft
	alpha=lofts[13]*np.pi/180
	h=0.06*PX_M*30
	w=0.02*PX_M*30
	p1=(0,NIVEL_DEL_SUELO-h)
	p2=p1+Vec2d(h*np.tan(alpha0),h)
	p3=p2+Vec2d(w,0)
	p4=p3+Vec2d(-h*np.tan(alpha),-h)
	
	vertices=[p1,p2,p3,p4]
	
	#Crear el cuerpo como KINEMATIC
	palo = pymunk.Body(body_type=pymunk.Body.KINEMATIC)
	palo_shape = pymunk.Poly(palo, vertices)
	palo_shape.elasticity = 0.6  # Rebote
	palo_shape.friction = 0.5    # Agarre con la bola
	# 4. Añadir al espacio (solo se añade el cuerpo y la forma)
	# No lo voy a dibujar
	space.add(palo, palo_shape)
	palo.position = xy2pixel([0,0.005]) #a la izquierda de la bola y un poquito por encima del suelo
	palo.velocity=Vec2d(5*PX_M,0)
	palo.angular_velocity=0


	
	# 4. Bucle principal de simulación
	substep=100
	dt=1.0/(FPS*substep)
	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# Limpiar pantalla
		screen.fill((135, 206, 235))  # Color cielo por defecto


		# Dibujar fondo si existe
		if bg_image:
			screen.blit(bg_image, bg_pos)

		# Paso de tiempo de la física (fijo para estabilidad)
		for _ in range(substep):
			palo.position+=dt*palo.velocity
			space.step(dt)

		# Dibujar debug de Pymunk (para ver los cuerpos físicos sobre el dibujo)
		# space.debug_draw(draw_options)


				
		pygame.draw.circle(screen, (255,255,255), bola.position, RADIO_visual)
		
		#print(palo.position)
		vertices_mod=[]
		for v in vertices:
			vertices_mod.append(v+Vec2d(palo.position.x,0))
		pygame.draw.polygon(screen, (100, 100, 100), vertices_mod)
		
		print(vertices_mod[0])

		pygame.display.flip()
		clock.tick(FPS)
		
		

	pygame.quit()

if __name__ == "__main__":
	run()
