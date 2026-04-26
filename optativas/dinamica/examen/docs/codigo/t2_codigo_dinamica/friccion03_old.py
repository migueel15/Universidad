import pygame
import pymunk
from pymunk import Vec2d
import math
import tkinter as tk
from tkinter import messagebox

# Valores iniciales para la persistencia y la primera ejecución
config = {
	"masa_cuna": 10.0,
	"mu_caja": 0.2,
	"mu_suelo": 0.3,
	"alpha_deg": 30.0
}

def simular_accion_reaccion(masa_cuna, mu_caja, mu_suelo, alpha_deg):
	pygame.init()
	ancho, alto = 1000, 700
	screen = pygame.display.set_mode((ancho, alto))
	clock = pygame.time.Clock()

	space = pymunk.Space()
	space.gravity = (0, 900)

	# --- GEOMETRÍA ---
	alpha_rad = math.radians(alpha_deg)
	base_cuna = 400.0
	altura_cuna = base_cuna * math.tan(alpha_rad)
	
	# --- CUÑA DINÁMICA ---
	v_raw = [(0, 0), (base_cuna, 0), (0, -altura_cuna)]
	centroide = Vec2d(base_cuna/3.0, -altura_cuna/3.0) #centro de gravedad de la cuña
	vertices_cuna = [Vec2d(*v) - centroide for v in v_raw]
	
	momento_cuna = pymunk.moment_for_poly(masa_cuna, vertices_cuna)
	cuna_body = pymunk.Body(masa_cuna, momento_cuna)
	
	
	cuna_body.position = Vec2d(500, 600)
	#cuna_body.position = Vec2d(500, 600 - 2*(altura_cuna/3.0)) 
	
	cuna_shape = pymunk.Poly(cuna_body, vertices_cuna)
	cuna_shape.friction = 1.0 
	space.add(cuna_body, cuna_shape)

	# --- CAJA DINÁMICA ---
	masa_caja = 1.0 
	dim_caja = 40.0
	caja_body = pymunk.Body(masa_caja, pymunk.moment_for_box(masa_caja, (dim_caja, dim_caja)))
	caja_body.angle = -alpha_rad
	
	u_hipo = Vec2d(math.cos(alpha_rad), math.sin(alpha_rad))
	n_hipo = Vec2d(-math.sin(alpha_rad), -math.cos(alpha_rad))
	punto_cima_local = Vec2d(0, -altura_cuna) - centroide
	pos_caja_local = punto_cima_local + u_hipo * dim_caja + n_hipo * (dim_caja / 2.0)
	
	caja_body.position = cuna_body.position + pos_caja_local
	caja_shape = pymunk.Poly.create_box(caja_body, (dim_caja, dim_caja))
	caja_shape.friction = mu_caja 
	space.add(caja_body, caja_shape)

	# --- SUELO ESTÁTICO ---
	suelo_shape = pymunk.Segment(space.static_body, (0, 600), (ancho, 600), 5)
	suelo_shape.friction = mu_suelo 
	space.add(suelo_shape)

	


	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		space.step(1/60.0)
		
		screen.fill((255, 255, 255))
		pygame.draw.line(screen, (150, 150, 150), (0, 600), (ancho, 600), 2)

		# Render Cuña
		pts_cuna = [v.rotated(cuna_body.angle) + cuna_body.position for v in cuna_shape.get_vertices()]
		pygame.draw.polygon(screen, (200, 200, 200), pts_cuna)
		pygame.draw.polygon(screen, (0, 0, 0), pts_cuna, 2)

		# Render Caja
		pts_caja = [v.rotated(caja_body.angle) + caja_body.position for v in caja_shape.get_vertices()]
		pygame.draw.polygon(screen, (100, 150, 200), pts_caja)
		pygame.draw.polygon(screen, (0, 0, 0), pts_caja, 2)

		pygame.display.flip()
		clock.tick(60)

	pygame.quit()

def iniciar_gui():
	root = tk.Tk()
	root.title("Configuración de la simulación")
	root.geometry("350x300")

	tk.Label(root, text="Masa de la caja: Siempre 1 kg", font=("Arial", 10, "bold")).pack(pady=10)

	campos = [
		("Masa cuña (kg):", "masa_cuna"),
		("mu caja/cuña:", "mu_caja"),
		("mu cuña/suelo:", "mu_suelo"),
		("Ángulo (grados):", "alpha_deg")
	]

	entries = {}
	for label_text, key in campos:
		frame = tk.Frame(root)
		frame.pack(fill="x", padx=20, pady=5)
		tk.Label(frame, text=label_text, width=15, anchor="w").pack(side="left")
		ent = tk.Entry(frame)
		ent.insert(0, str(config[key]))
		ent.pack(side="right", expand=True)
		entries[key] = ent

	def aceptar():
		try:
			config["masa_cuna"] = float(entries["masa_cuna"].get())
			config["mu_caja"] = float(entries["mu_caja"].get())
			config["mu_suelo"] = float(entries["mu_suelo"].get())
			config["alpha_deg"] = float(entries["alpha_deg"].get())
			
			root.withdraw()
			simular_accion_reaccion(
				config["masa_cuna"], 
				config["mu_caja"], 
				config["mu_suelo"], 
				config["alpha_deg"]
			)
			root.deiconify()
		except ValueError:
			messagebox.showerror("Error de entrada", "Introduce valores numéricos válidos.")

	btn_frame = tk.Frame(root)
	btn_frame.pack(pady=20)

	tk.Button(btn_frame, text="Aceptar", command=aceptar, width=10).pack(side="left", padx=10)
	tk.Button(btn_frame, text="Salir", command=root.destroy, width=10).pack(side="left", padx=10)

	root.mainloop()

if __name__ == "__main__":
	# 1. Ejecución por primera vez con datos por defecto
	simular_accion_reaccion(
		config["masa_cuna"], 
		config["mu_caja"], 
		config["mu_suelo"], 
		config["alpha_deg"]
	)
	
	# 2. Tras cerrar la primera simulación, aparece la ventana de entrada de datos
	iniciar_gui()
