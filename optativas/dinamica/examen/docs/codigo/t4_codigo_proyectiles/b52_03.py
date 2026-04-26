''' Esta versión de B52 hace uso de todas las funciones de la librería rozamiento_aire
incluida aplicar_newton para calcular la fuerza de arrastre con todas las opciones '''

import pygame
import pymunk
import math
import tkinter as tk
import os
####################################
### ESTA LIBRERÍA ES NUESTRA
from rozamiento_aire import  aplicar_newton



# --- PERSISTENCIA DE DATOS ---
root_main = tk.Tk()
root_main.withdraw() # Ocultamos la ventana principal de control

v_v = tk.DoubleVar(value=650)
v_m = tk.DoubleVar(value=1000)
v_d = tk.DoubleVar(value=0.4)
v_n = tk.BooleanVar(value=True)
v_cd = tk.DoubleVar(value=0.47)
v_ma = tk.BooleanVar(value=False)
v_de = tk.BooleanVar(value=False)

def mostrar_dialogo():
	config = {}
	dialog = tk.Toplevel(root_main)
	dialog.title("Configuración de Misión B-52")
	
	fields = [("Velocidad Avión (km/h):", v_v), ("Masa Bomba (kg):", v_m), 
			  ("Diámetro Bomba (m):", v_d), ("Coeficiente Cd:", v_cd)]
	
	for i, (txt, var) in enumerate(fields):
		tk.Label(dialog, text=txt).grid(row=i, column=0, sticky="w", padx=10, pady=2)
		tk.Entry(dialog, textvariable=var).grid(row=i, column=1, padx=10)

	f_adv = tk.LabelFrame(dialog, text="Física de Newton")
	f_adv.grid(row=4, column=0, columnspan=2, pady=10, padx=10, sticky="we")

	tk.Checkbutton(f_adv, text="Activar Arrastre (Newton)", variable=v_n).pack(anchor="w")
	tk.Checkbutton(f_adv, text="Corrección Mach", variable=v_ma).pack(anchor="w")
	tk.Checkbutton(f_adv, text="Variación Densidad", variable=v_de).pack(anchor="w")

	def iniciar():
		config.update({
			"v": v_v.get(), "m": v_m.get(), "d": v_d.get(),
			"arrastre": v_n.get(), "cd": v_cd.get(), "mach": v_ma.get(), "densidad": v_de.get()
		})
		dialog.destroy()

	tk.Button(dialog, text="LANZAR SIMULACIÓN", command=iniciar, bg="green", fg="white").grid(row=5, column=0, columnspan=2, pady=10)
	
	dialog.grab_set() # Bloquea interacción con otras ventanas
	root_main.wait_window(dialog)
	return config

def dibujar_bomba(screen, x, y):
	col_cuerpo = (40, 40, 40)
	for i in range(3):
		ox = (i - 1) * 4
		pygame.draw.rect(screen, col_cuerpo, (int(x + ox - 2), int(y - 2.5), 5, 5), border_radius=2)
	pygame.draw.polygon(screen, (20, 20, 20), [(x-6, y), (x-2, y-3.5), (x-2, y+3.5)])

################################################################################

def run_simulation():
	CONF = mostrar_dialogo()
	if not CONF: return False # Si se cierra la ventana sin lanzar

	pygame.init()
	WIDTH, HEIGHT = 1000, 600
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	pygame.display.set_caption("Simulador Balístico B-52 (SPACE lanza bomba, ESC para volver)")
	clock = pygame.time.Clock()
	font = pygame.font.SysFont("monospace", 14, bold=True)
	
	M_PX = 18.05 #metros por pixel
	VX_AVION_PX = (CONF["v"] / 3.6) / M_PX
	GRAV_PX = 9.8 / M_PX
	AREA_M2 = math.pi * ((CONF["d"]/2)**2)
	SUELO_Y = 550
	ESCALA_T = 15.0

	b52_img = None
	if os.path.exists("B52.png"):
		try:
			b52_img = pygame.image.load("B52.png").convert_alpha()
			b52_img = pygame.transform.scale(b52_img, (150, 50))
		except: pass
	
	if b52_img is None:
		b52_img = pygame.Surface((150, 50), pygame.SRCALPHA)
		pygame.draw.rect(b52_img, (100, 100, 100), (40, 15, 100, 20))
	
	plane_rect = b52_img.get_rect()
	space = pymunk.Space()
	space.gravity = (0, GRAV_PX)
	space.add(pymunk.Segment(space.static_body, (0, SUELO_Y), (WIDTH, SUELO_Y), 5))

	plane_x, plane_y = 950.0, 75.0
	bombas_aire = []
	bombas_suelo = []
	t_sim = 0.0

	running = True
	regresar = False

	while running:
		dt = (1.0 / 60) * ESCALA_T
		for e in pygame.event.get():
			if e.type == pygame.QUIT:
				running = False
				pygame.quit()
				return False # Cerrar todo
			if e.type == pygame.KEYDOWN:
				if e.key == pygame.K_SPACE:
					b_body = pymunk.Body(CONF["m"], pymunk.moment_for_circle(CONF["m"], 0, 5))
					b_body.position = (plane_x, plane_y + 15)
					b_body.velocity = (-VX_AVION_PX, 0)
					b_shape = pymunk.Circle(b_body, 5)
					space.add(b_body, b_shape)
					bombas_aire.append({"body": b_body, "shape": b_shape, "t_ini": t_sim})
				if e.key == pygame.K_ESCAPE:
					running = False
					regresar = True
		############################################################################			
		#---------------------------------------------------------------------------			
		# Aquí está la Física, para cada bomba activa, si está activado el drag 
		# llamamos aplicar_newton del módulo rozamiento_aire.py
		if CONF["arrastre"]:
			for b in bombas_aire: 
				alt_m = (550 - b["body"].position.y) * M_PX	
				#Le pasamos a aplicar_newton:
				# El body, su superficie en m2 (AREA_M2), el factor de conversión m/pixel (M_PX
				# Del Tkinter sacamos el arrastre Cd=CONF["cd"], altitud y los booleanos para ver si
				# Si no vamos a poner corrección por altitud, alt_m no es necesaria
				# aplicamos la corrección de densidad por la altura y la corrección de mach
				# No ponemos velocidad del viento ni offset en la aplicación de la fuerza de drag
				aplicar_newton(b["body"],AREA_M2, M_PX, Cd=CONF["cd"],alt_m=alt_m,v_viento=[0,0],
							CORRECT_RHO=CONF["densidad"],MACH=CONF["mach"],offset=(0,0))
		#---------------------------------------------------------------------------
		############################################################################	
		space.step(dt)
		t_sim += dt
		plane_x -= VX_AVION_PX * dt
		if plane_x < -150: plane_x = WIDTH + 150

		for b in bombas_aire[:]:
			if b["body"].position.y >= SUELO_Y - 25:
				x_f = float(b["body"].position.x)
				bombas_suelo.append({
					"pos": (x_f, float(SUELO_Y - 5)),
					"vx": float(b["body"].velocity.x),
					"vy": float(b["body"].velocity.y),
					"t_v": t_sim - b["t_ini"],
					"dx": (x_f - plane_x) * M_PX
				})
				space.remove(b["shape"], b["body"])
				bombas_aire.remove(b)

		screen.fill((135, 206, 235))
		pygame.draw.line(screen, (34, 139, 34), (0, SUELO_Y), (WIDTH, SUELO_Y), 5)

		target = bombas_aire[-1] if bombas_aire else (bombas_suelo[-1] if bombas_suelo else None)
		if target:
			is_aire = "body" in target
			v_x = target["body"].velocity.x if is_aire else target["vx"]
			v_y = target["body"].velocity.y if is_aire else target["vy"]
			alt = (SUELO_Y - target["body"].position.y) * M_PX if is_aire else 0.0
			dx = (target["body"].position.x - plane_x) * M_PX if is_aire else target["dx"]
			tiempo = (t_sim - target["t_ini"]) if is_aire else target["t_v"]

			txts = [f"ALTITUD: {max(0, alt):.1f} m", f"VEL X: {abs(v_x * M_PX):.1f} m/s", 
					f"VEL Y: {v_y * M_PX:.1f} m/s", f"DELTA X: {dx:.1f} m", f"TIEMPO: {tiempo:.2f} s"]
			for i, t in enumerate(txts):
				screen.blit(font.render(t, True, (0, 0, 0)), (15, 15 + i * 18))

		for b in bombas_suelo: dibujar_bomba(screen, b["pos"][0], b["pos"][1])
		for b in bombas_aire: dibujar_bomba(screen, b["body"].position.x, b["body"].position.y)
		
		plane_rect.center = (int(plane_x), int(plane_y))
		screen.blit(b52_img, plane_rect)

		pygame.display.flip()
		clock.tick(60)
	
	pygame.quit()
	return regresar

if __name__ == "__main__":
	while run_simulation():
		pass
	root_main.destroy()
