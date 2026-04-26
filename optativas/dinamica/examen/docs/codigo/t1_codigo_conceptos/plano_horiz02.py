import pymunk
import pygame
import pymunk.pygame_util
import numpy as np
import tkinter as tk


######################## parte de TKINTER #############################
######################## parte de TKINTER #############################
def pedir_angulo_gui():
	"""
	Crea una ventana de Tkinter con dos botones.
	Devuelve el valor (float) si acepta, o -1 si sale o cancela.
	"""
	resultado = {"angulo": -1} # Valor por defecto -1

	root = tk.Tk()
	root.title("Entrada de Parámetros")
	root.geometry("300x180")
	root.attributes("-topmost", True)

	tk.Label(root, text="Introduce el ángulo de la fuerza:", 
			 font=("Arial", 10)).pack(pady=10)
	
	entry = tk.Entry(root)
	entry.insert(0, "30.0")
	entry.pack(pady=5)
	entry.focus_set()

	def confirmar():
		try:
			valor = float(entry.get())
			resultado["angulo"] = valor
			root.destroy()
		except ValueError:
			messagebox.showerror("Error", "Introduce un número válido")

	def salir():
		resultado["angulo"] = -1
		root.destroy()

	# Contenedor para los botones (uno al lado del otro)
	frame_botones = tk.Frame(root)
	frame_botones.pack(pady=15)

	btn_aceptar = tk.Button(frame_botones, text="Aceptar", command=confirmar, width=10)
	btn_aceptar.pack(side=tk.LEFT, padx=5)

	btn_salir = tk.Button(frame_botones, text="Salir", command=salir, width=10)
	btn_salir.pack(side=tk.LEFT, padx=5)

	# 'Enter' confirma, 'Escape' sale
	root.bind('<Return>', lambda event: confirmar())
	root.bind('<Escape>', lambda event: salir())

	root.mainloop()
	return resultado["angulo"]
#--------------------------------------
def ventana_final():
	root = tk.Tk()
	root.title("")
	root.geometry("200x100")
	root.attributes("-topmost", True)
	
	tk.Label(root, text="Simulación terminada").pack(pady=10)
	
	# El botón destruye la ventana y permite que el código siga
	btn = tk.Button(root, text="OK", command=root.destroy, width=10)
	btn.pack(pady=5)
	
	root.mainloop()

#####################################################################


def run_simulation(angulo_grados):
	pygame.init()
	screen = pygame.display.set_mode((800, 400))
	clock = pygame.time.Clock()
	font = pygame.font.SysFont("Arial", 18) # Fuente para el texto
	draw_options = pymunk.pygame_util.DrawOptions(screen)

	# 1. Configuración del Espacio
	space = pymunk.Space()
	space.gravity = (0, 981)

	# 2. Creación del Suelo (Plano horizontal)
	# Segmento de (50, 350) a (750, 350) con grosor 5
	ground = pymunk.Segment(space.static_body, (50, 350), (750, 350), 5)
	ground.friction = 0.5
	space.add(ground)

	# 3. Creación de la Caja
	mass = 1
	size = (50, 50)
	moment = pymunk.moment_for_box(mass, size)
	body = pymunk.Body(mass, moment)
	body.position = (100, 320)
	
	shape = pymunk.Poly.create_box(body, size)
	shape.friction = 0.5
	space.add(body, shape)

	# Parámetros de la fuerza
	#angulo_grados = 20 se pide con tkinter
	alpha = angulo_grados * np.pi / 180.0
	fuerza_magnitud = 400
	
	# Vector de fuerza: Fx es horizontal, Fy es hacia arriba (negativo en Pygame)
	force_vector = (fuerza_magnitud * np.cos(alpha), -fuerza_magnitud * np.sin(alpha))

	running = True
	v0=0
	
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		# Aplicar la fuerza de tracción en el centro de la caja
		body.apply_force_at_local_point(force_vector, (0, 0))

		# Paso de física
		dt = 1.0 / 60.0
		space.step(dt)
		
		##########################################
	
		
#-----------------------------------------
		

		# Dibujado
		screen.fill((255, 255, 255))
		space.debug_draw(draw_options)

		# Dibujar la flecha de la fuerza
		start_p = body.position
		end_p = start_p + pymunk.Vec2d(80 * np.cos(alpha), -80 * np.sin(alpha))
		pygame.draw.line(screen, (255, 0, 0), start_p, end_p, 3)
		
		# Punta de la flecha dinámica
		angle_arrow = -alpha
		p1 = end_p
		p2 = end_p + pymunk.Vec2d(10, 0).rotated(angle_arrow + 2.6)
		p3 = end_p + pymunk.Vec2d(10, 0).rotated(angle_arrow - 2.6)
		pygame.draw.polygon(screen, (255, 0, 0), [p1, p2, p3])

		aceleracion=(body.velocity.x-v0)/dt
		v0=body.velocity.x
		screen.blit(font.render(f'Velocidad   = {body.velocity.x/10:0.2f} m/s',True,(0,0,0)), (20,20))
		screen.blit(font.render(f'Aceleración = {aceleracion/10:0.2f} m/s²',True,(0,0,0)), (20,45))
		screen.blit(font.render(f'Fx = {force_vector[0]:0.2f} m/s²',True,(0,0,0)), (20,70))

		pygame.display.flip()
		clock.tick(60)
		if body.position.x> 800 -100:
			ventana_final()
			pygame.quit()
			running=False
			

	pygame.quit()

if __name__ == "__main__":
	
	while 1:
		angulo_grados=pedir_angulo_gui()
		if angulo_grados<0: exit()
		run_simulation(angulo_grados)
