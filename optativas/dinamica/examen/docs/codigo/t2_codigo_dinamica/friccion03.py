import pygame
import pymunk
import pymunk.pygame_util
from pymunk import Vec2d
import math
import tkinter as tk
from tkinter import messagebox

# Valores iniciales incluyendo la nueva variable d_cima
config = {
	"masa_cuna": 10.0,
	"mu_caja": 0.2,
	"mu_suelo": 0.3,
	"alpha_deg": 30.0,
	"d_cima": 100.0  # Nueva variable solicitada
}

def simular_accion_reaccion(masa_cuna, mu_caja, mu_suelo, alpha_deg, d_cima):
	pygame.init()
	ancho, alto = 1000, 700
	screen = pygame.display.set_mode((ancho, alto))
	clock = pygame.time.Clock()
	draw_options = pymunk.pygame_util.DrawOptions(screen)

	space = pymunk.Space()
	space.gravity = (0, 900)

	# --- GEOMETRÍA ---
	alpha_rad = math.radians(alpha_deg)
	base_cuna = 400.0
	altura_cuna = base_cuna * math.tan(alpha_rad)
	
	# --- CUÑA DINÁMICA ---
	vertices_cuna = [(0, 0), (base_cuna, 0), (0, -altura_cuna)]
	momento_cuna = pymunk.moment_for_poly(masa_cuna, vertices_cuna)
	cuna_body = pymunk.Body(masa_cuna, momento_cuna)
	
	cuna_body.position = Vec2d(300, 600)
	cuna_shape = pymunk.Poly(cuna_body, vertices_cuna)
	cuna_shape.friction = 1.0 
	space.add(cuna_body, cuna_shape)

	# --- CAJA DINÁMICA ---
	# Aquí podrías usar d_cima para posicionar la caja a lo largo de la hipotenusa
	masa_caja = 1.0 
	dim_caja = 40.0
	caja_body = pymunk.Body(masa_caja, pymunk.moment_for_box(masa_caja, (dim_caja, dim_caja)))
	
	# Ejemplo de uso de d_cima: desplazar la caja desde el vértice superior (0, -altura_cuna)
	# hacia abajo por la hipotenusa.
	d_cima=d_cima+dim_caja/2
	pos_caja_local = Vec2d(0, -altura_cuna) + Vec2d(d_cima * math.cos(alpha_rad), d_cima * math.sin(alpha_rad)) + Vec2d(-1,1)
	
	caja_body.angle = alpha_rad
	caja_body.position = cuna_body.position + pos_caja_local
	caja_shape = pymunk.Poly.create_box(caja_body, (dim_caja, dim_caja))
	caja_shape.friction = mu_caja 
	space.add(caja_body, caja_shape)

	# --- SUELO ESTÁTICO ---
	suelo_shape = pymunk.Segment(space.static_body, (0, 600), (ancho, 600), 5)
	suelo_shape.friction = mu_suelo 
	space.add(suelo_shape)

	# Definir un color (R, G, B, Alpha)
	cuna_shape.color = (200,200,200,255)  
	caja_shape.color = (0, 50, 190, 255)  


	ntic = 60
	Dt = 1.0 / ntic
	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False

		space.step(Dt)
		screen.fill((255, 255, 255))
		space.debug_draw(draw_options)
		pygame.display.flip()
		clock.tick(ntic)

	pygame.quit()

def iniciar_gui():
	root = tk.Tk()
	root.title("Configuración de la simulación")
	root.geometry("380x350")

	tk.Label(root, text="Parámetros de Simulación", font=("Arial", 10, "bold")).pack(pady=10)

	campos = [
		("Masa cuña (kg):", "masa_cuna"),
		("mu caja/cuña:", "mu_caja"),
		("mu cuña/suelo:", "mu_suelo"),
		("Ángulo (grados):", "alpha_deg"),
		("Dist. a la cima:", "d_cima") # Añadido a la GUI
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
			config["d_cima"] = float(entries["d_cima"].get()) # Captura del valor
			
			root.withdraw()
			simular_accion_reaccion(
				config["masa_cuna"], 
				config["mu_caja"], 
				config["mu_suelo"], 
				config["alpha_deg"],
				config["d_cima"]
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
	simular_accion_reaccion(
		config["masa_cuna"], 
		config["mu_caja"], 
		config["mu_suelo"], 
		config["alpha_deg"],
		config["d_cima"]
	)
	iniciar_gui()
