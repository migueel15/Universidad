import pymunk
import pygame
import pymunk.pygame_util
import numpy as np
import tkinter as tk
from tkinter import messagebox

######################## parte de TKINTER #############################

def pedir_parametros_gui(fza_previa="3000", fric_previa="0.02"):
	res = {"fuerza": -1, "friccion": 0.02}
	confirmado = {"estado": False}
	
	root = tk.Tk()
	root.title("Configuración de Curling")
	root.geometry("300x200")
	root.attributes("-topmost", True)

	tk.Label(root, text="Magnitud de la fuerza de lanzamiento:", font=("Arial", 10)).pack(pady=5)
	entry_fza = tk.Entry(root)
	entry_fza.insert(0, fza_previa)
	entry_fza.pack()

	tk.Label(root, text="Coeficiente de rozamiento (mu):", font=("Arial", 10)).pack(pady=5)
	entry_fric = tk.Entry(root)
	entry_fric.insert(0, fric_previa)
	entry_fric.pack()

	def confirmar():
		try:
			res["fuerza"] = float(entry_fza.get())
			res["friccion"] = float(entry_fric.get())
			confirmado["estado"] = True
			root.destroy()
		except ValueError:
			messagebox.showerror("Error", "Introduce números válidos")

	def salir():
		root.destroy()

	frame_botones = tk.Frame(root)
	frame_botones.pack(pady=20)

	btn_aceptar = tk.Button(frame_botones, text="Lanzar", command=confirmar, width=10)
	btn_aceptar.pack(side=tk.LEFT, padx=5)

	btn_salir = tk.Button(frame_botones, text="Salir", command=salir, width=10)
	btn_salir.pack(side=tk.LEFT, padx=5)

	root.mainloop()
	return res if confirmado["estado"] else None

def ventana_final():
	root = tk.Tk()
	root.title("")
	root.geometry("200x100")
	root.attributes("-topmost", True)
	tk.Label(root, text="Simulación terminada").pack(pady=10)
	tk.Button(root, text="OK", command=root.destroy, width=10).pack(pady=5)
	root.mainloop()

######################## PARTE DE SIMULACIÓN #############################

def run_simulation(params):
	pygame.init()
	WIDTH, HEIGHT = 1200, 400
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	pygame.display.set_caption("Curling")
	clock = pygame.time.Clock()
	font = pygame.font.SysFont("Arial", 18)
	
	draw_options = pymunk.pygame_util.DrawOptions(screen)

	space = pymunk.Space()
	space.gravity = (0, 981)

	# Suelo
	ground_y = 350
	ground = pymunk.Segment(space.static_body, (0, ground_y), (WIDTH, ground_y), 5)
	ground.friction = 1.0 
	space.add(ground)

	
# Piedra Cápsula
	mass = 20
	w, h = 40, 24 
	radius = h / 2 
	moment = pymunk.moment_for_box(mass, (w + 2*radius, h))
	body = pymunk.Body(mass, moment)
	body.position = (60, ground_y - h/2)
	
	rect_shape = pymunk.Poly.create_box(body, (w, h))
	rect_shape.friction = params["friccion"]
	circle_left = pymunk.Circle(body, radius, offset=(-w/2, 0))
	circle_right = pymunk.Circle(body, radius, offset=(w/2, 0))
	circle_left.friction = params["friccion"]
	circle_right.friction = params["friccion"]
	
	space.add(body, rect_shape, circle_left, circle_right)
	
	

	# Fuerza horizontal
	fuerza_magnitud = params["fuerza"]
	force_vector = (fuerza_magnitud, 0)

	running = True
	v0 = 0.0
	
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		fuerza_activa = body.position.x < 200
		if fuerza_activa:
			body.apply_force_at_local_point(force_vector, (0, 0))

		dt = 1.0 / 60.0
		space.step(dt)
		
		screen.fill((230, 240, 250)) 
		
		
		# Línea de impulso (Hog Line)
		pygame.draw.line(screen, (255, 100, 100), (200, 0), (200, 350), 2)
		
		# Suelo
		space.debug_draw(draw_options)

		# Punto rojo a 800 px (Diana)
		pygame.draw.circle(screen, (255, 0, 0), (900, ground_y), 8)
		

		# Piedra manual
		pos = body.position
		color_piedra = (100, 100, 100) # Gris oscuro
		color_asa = (180, 0, 0)      # Rojo granate
		
		# Base de la piedra (Cápsula)
		pygame.draw.rect(screen, color_piedra, (pos.x - w/2, pos.y - h/2, w, h))
		pygame.draw.circle(screen, color_piedra, (int(pos.x - w/2), int(pos.y)), int(radius))
		pygame.draw.circle(screen, color_piedra, (int(pos.x + w/2), int(pos.y)), int(radius))
		
		# Dibujado del Asa
		# Soporte vertical (el "cuello" del asa)
		pygame.draw.line(screen, color_asa, (int(pos.x), int(pos.y - h/2)), (int(pos.x), int(pos.y - h/2 - 6)), 4)
		# Parte horizontal (donde se agarra)
		pygame.draw.rect(screen, color_asa, (int(pos.x - 12), int(pos.y - h/2 - 10), 24, 4))
		
		if fuerza_activa:
			start_p = body.position
			end_p = start_p + pymunk.Vec2d(100, 0)
			pygame.draw.line(screen, (255, 0, 0), start_p, end_p, 3)
			p1 = end_p
			p2 = end_p + pymunk.Vec2d(-10, -5)
			p3 = end_p + pymunk.Vec2d(-10, 5)
			pygame.draw.polygon(screen, (255, 0, 0), [p1, p2, p3])

		v_actual = body.velocity.x
		aceleracion = (v_actual - v0) / dt
		v0 = v_actual
		if (v0<=0): aceleracion=0.0
		
		
		screen.blit(font.render(f'Fuerza       = {fuerza_magnitud:0.2f} N', True, (0,0,0)), (20,20))
		screen.blit(font.render(f'Velocidad    = {v_actual/10:0.2f} m/s', True, (0,0,0)), (20,45))
		screen.blit(font.render(f'Aceleración  = {aceleracion/10:0.2f} m/s²', True, (0,0,0)), (20,70))
		screen.blit(font.render(f'Fricción mu  = {params["friccion"]}', True, (0,0,0)), (20,95))

		pygame.display.flip()
		clock.tick(60)

		if body.position.x > WIDTH + (w/2 + radius):
			ventana_final()
			running = False
		
		if body.position.x > 200 and abs(v_actual) < 0.01:
			ventana_final()
			running = False

	pygame.quit()

if __name__ == "__main__":
	# Valores iniciales
	ultima_fza = "3000"
	ultima_fric = "0.02"
	
	while True:
		parametros = pedir_parametros_gui(ultima_fza, ultima_fric)
		if parametros is None: 
			break
		
		# Actualizamos los valores "de memoria" para la siguiente vuelta
		ultima_fza = str(parametros["fuerza"])
		ultima_fric = str(parametros["friccion"])
		
		run_simulation(parametros)
