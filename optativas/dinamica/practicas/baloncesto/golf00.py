import pygame
import pymunk
import pymunk.pygame_util



# Configuración de la ventana
WIDTH, HEIGHT = 1000, 600
FPS = 60
NIVEL_DEL_SUELO=HEIGHT-60 #PIXELES 


#escala
PX_M=5 #5px por metro
M_PX=1.0/PX_M

#------------------------------------
RADIO_M=0.0213  #2.13 cm
HOYO_M=181.6    #coordenada X del hoyo


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

	# 4. Bucle principal de simulación
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
		dt = 1.0 / FPS
		space.step(dt)

		# Dibujar debug de Pymunk (para ver los cuerpos físicos sobre el dibujo)
		# space.debug_draw(draw_options)


		radio=RADIO_M*PX_M*30
		pygame.draw.circle(screen, (255,255,255), (100,NIVEL_DEL_SUELO-radio), radio)
		
		pygame.draw.circle(screen, (255,255,255), (HOYO_M*PX_M,NIVEL_DEL_SUELO-radio), radio)
		

		pygame.display.flip()
		clock.tick(FPS)
		
		
		

	pygame.quit()

if __name__ == "__main__":
	run()
