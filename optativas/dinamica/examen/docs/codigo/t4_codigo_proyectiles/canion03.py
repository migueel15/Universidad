import tkinter as tk
from tkinter import ttk
import math
import pygame
import pymunk

# --- Constantes Físicas ---
GRAVITY = 9.81
RHO_AIR = 1.225
MU_AIR = 1.789e-5
LB_TO_KG = 0.453592
FPS = 60

try:
	from rozamiento_aire import get_Cd, mach_correction
except ImportError:
	def get_Cd(re): return 0.44
	def mach_correction(v): return 1.0

class BallisticsUI:
	def __init__(self, root):
		self.root = root
		self.root.title("Control de Artillería - Cádiz 1812")
		self.root.geometry("480x600") # Aumentado ligeramente para los checkbox

		# Variables de entrada
		self.v0 = tk.DoubleVar(value=500)
		self.angle = tk.DoubleVar(value=40)
		self.calibre_pulg = tk.DoubleVar(value=8.0)
		self.material_var = tk.StringVar(value="Bronce")
		self.densidad_custom = tk.DoubleVar(value=8800)
		self.time_scale_var = tk.DoubleVar(value=15.0)
		
		# --- NUEVAS VARIABLES BOOLEANAS ---
		self.correccion_mach = tk.BooleanVar(value=True)
		self.crisis_arrastre = tk.BooleanVar(value=False)
		
		self.masa_libras_str = tk.StringVar()

		# Resultados
		self.res_th = {"r": tk.StringVar(value="---"), "h": tk.StringVar(value="---"), "t": tk.StringVar(value="---")}
		self.res_sim = {"r": tk.StringVar(value="---"), "h": tk.StringVar(value="---"), "t": tk.StringVar(value="---")}

		self.setup_ui()
		
		for var in [self.v0, self.angle, self.calibre_pulg, self.densidad_custom]:
			var.trace_add("write", self.auto_update)
		
		self.auto_update()

	def setup_ui(self):
		main_frame = ttk.Frame(self.root, padding="20")
		main_frame.pack(fill="both", expand=True)

		# Entradas
		ttk.Label(main_frame, text="Velocidad Inicial (m/s):").grid(row=0, column=0, sticky="w")
		ttk.Entry(main_frame, textvariable=self.v0).grid(row=0, column=1, pady=5, sticky="ew")

		ttk.Label(main_frame, text="Ángulo (grados):").grid(row=1, column=0, sticky="w")
		ttk.Entry(main_frame, textvariable=self.angle).grid(row=1, column=1, pady=5, sticky="ew")

		ttk.Label(main_frame, text="Calibre (pulgadas):").grid(row=2, column=0, sticky="w")
		ttk.Entry(main_frame, textvariable=self.calibre_pulg).grid(row=2, column=1, pady=5, sticky="ew")

		ttk.Label(main_frame, text="Material:").grid(row=3, column=0, sticky="w")
		materiales = {"Hierro": 7874, "Bronce": 8800, "Plomo": 11340, "Granito": 2500, "Personalizado": 0}
		combo = ttk.Combobox(main_frame, textvariable=self.material_var, values=list(materiales.keys()), state="readonly")
		combo.grid(row=3, column=1, pady=5, sticky="ew")
		combo.bind("<<ComboboxSelected>>", lambda e: self.update_density(materiales))

		ttk.Label(main_frame, text="Densidad (kg/m³):").grid(row=4, column=0, sticky="w")
		ttk.Entry(main_frame, textvariable=self.densidad_custom).grid(row=4, column=1, pady=5, sticky="ew")

		# Panel de Masa
		info_frame = ttk.LabelFrame(main_frame, text=" Masa del proyectil ", padding="10")
		info_frame.grid(row=5, column=0, columnspan=2, pady=10, sticky="ew")
		#ttk.Label(info_frame, text="Masa:").pack(side="left")
		ttk.Label(info_frame, textvariable=self.masa_libras_str, font=("Helvetica", 10, "bold"), foreground="#2e7d32").pack(side="left", padx=5)

		ttk.Separator(main_frame, orient='horizontal').grid(row=6, column=0, columnspan=2, sticky="ew", pady=10)

		# Tabla
		h_font = ('Helvetica', 10, 'bold')
		ttk.Label(main_frame, text="Métrica", font=h_font).grid(row=7, column=0)
		ttk.Label(main_frame, text="Teórico (Vacío)", font=h_font).grid(row=7, column=1)
		ttk.Label(main_frame, text="Simulación (Aire)", font=h_font).grid(row=7, column=2)

		metrics = [("Alcance (m)", "r"), ("H. Máx (m)", "h"), ("Tiempo (s)", "t")]
		for i, (label, key) in enumerate(metrics):
			ttk.Label(main_frame, text=label).grid(row=8+i, column=0, sticky="w", pady=2)
			ttk.Label(main_frame, textvariable=self.res_th[key], foreground="#1565c0").grid(row=8+i, column=1)
			ttk.Label(main_frame, textvariable=self.res_sim[key], foreground="#c62828", font=("Helvetica", 10, "bold")).grid(row=8+i, column=2)

		# --- Configuración de Simulación (Con Checkboxes) ---
		sim_config_frame = ttk.LabelFrame(main_frame, text=" Configuración de Simulación ", padding="10")
		sim_config_frame.grid(row=11, column=0, columnspan=2, pady=15, sticky="ew")
		
		ttk.Label(sim_config_frame, text="Escala Temporal (x):").grid(row=0, column=0, sticky="w")
		ttk.Entry(sim_config_frame, textvariable=self.time_scale_var, width=10).grid(row=0, column=1, padx=10, sticky="w", pady=5)

		ttk.Checkbutton(sim_config_frame, text="Corrección de Mach", variable=self.correccion_mach).grid(row=1, column=0, columnspan=2, sticky="w")
		ttk.Checkbutton(sim_config_frame, text="Crisis de Arrastre", variable=self.crisis_arrastre).grid(row=2, column=0, columnspan=2, sticky="w")

		ttk.Button(main_frame, text="LANZAR SIMULACIÓN", command=self.run_pygame).grid(row=12, column=0, columnspan=3, pady=10)

	def update_density(self, tabla):
		d = tabla.get(self.material_var.get(), 0)
		if d > 0: self.densidad_custom.set(d)

	def auto_update(self, *args):
		try:
			r_m = (self.calibre_pulg.get() * 0.0254) / 2
			vol = (4/3) * math.pi * r_m**3
			masa_kg = vol * self.densidad_custom.get()
			self.masa_libras_str.set(f"{masa_kg/LB_TO_KG:.2f} lb ({masa_kg:.2f} kg)")
			v0, a_rad = self.v0.get(), math.radians(self.angle.get())
			r_th = (v0**2 * math.sin(2 * a_rad)) / GRAVITY
			h_th = (v0**2 * (math.sin(a_rad)**2)) / (2 * GRAVITY)
			t_th = (2 * v0 * math.sin(a_rad)) / GRAVITY
			self.res_th["r"].set(f"{r_th:.2f}")
			self.res_th["h"].set(f"{h_th:.2f}")
			self.res_th["t"].set(f"{t_th:.2f}")
		except: pass

	def run_pygame(self):
		r_m = (self.calibre_pulg.get() * 0.0254) / 2
		vol = (4/3) * math.pi * r_m**3
		params = {
			"v0": self.v0.get(), "angle": self.angle.get(),
			"mass": vol * self.densidad_custom.get(), "radius": r_m,
			"r_th": float(self.res_th["r"].get()), "h_th": float(self.res_th["h"].get()),
			"time_scale": self.time_scale_var.get(),
			"use_mach": self.correccion_mach.get(),  # Acceso a CORRECCION_MACH
			"use_crisis": self.crisis_arrastre.get(), # Acceso a CRISIS_ARRASTRE
			"tk_root": self.root
		}
		start_simulation(params, self)

def start_simulation(p, ui_app):
	pygame.init()
	WIDTH, HEIGHT = 1100, 600
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	font = pygame.font.SysFont("Consolas", 15, bold=True)
	clock = pygame.time.Clock()
	space = pymunk.Space()
	space.gravity = (0, -GRAVITY)
	
	try:
		fondo_original = pygame.image.load("fondo_cadiz.png")
		fondo = pygame.transform.scale(fondo_original, (WIDTH, HEIGHT))
	except:
		fondo = None

	mass, radius = p["mass"], p["radius"]
	body = pymunk.Body(mass, pymunk.moment_for_circle(mass, 0, radius))
	body.position = (0, 0)
	a_rad = math.radians(p["angle"])
	body.velocity = (p["v0"] * math.cos(a_rad), p["v0"] * math.sin(a_rad))
	space.add(body, pymunk.Circle(body, radius))

	DRAW_SCALE, ORIGIN = 0.15, (50, HEIGHT - 10)
	def to_pyg(pos): return int(pos.x * DRAW_SCALE + ORIGIN[0]), int(ORIGIN[1] - pos.y * DRAW_SCALE)

	running, sim_frozen, results_updated = True, False, False
	path_real, path_th = [], []
	t_sim, h_max, reynolds, cd_calc = 0.0, 0.0, 0.0, 0.0
	sub_steps = 10
	dt = (1.0 / FPS * p["time_scale"]) / sub_steps

	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT: running = False

		if not sim_frozen:
			for _ in range(sub_steps):
				#####################################################################
				#### AQUÍ ESTÁ LA FISICA RELEVANTE DEL PROBLEMA
				#####################################################################
				speed = body.velocity.length
				reynolds = (RHO_AIR * speed * (radius*2)) / MU_AIR
				# Aquí el bucle tiene acceso a p["use_mach"] y p["use_crisis"]
				#Si no está chequeado se pasa False y no se aplica
				cd_base = get_Cd(reynolds,p["use_crisis"])
				#Si no está chequeado el factor de corrección es 1.0 (no hay)
				mach_corr = mach_correction(speed) if p["use_mach"] else 1.0
				cd_calc = cd_base * mach_corr 
				
				force = -0.5 * cd_calc * RHO_AIR * (math.pi * radius**2) * speed * body.velocity
				body.apply_force_at_local_point(force, (0,0))
				space.step(dt)
				#######################################################################
				
				t_sim += dt
				if body.position.y > h_max: h_max = body.position.y
				path_real.append(to_pyg(body.position))
				x_th = (p["v0"] * math.cos(a_rad)) * t_sim
				y_th = (p["v0"] * math.sin(a_rad) * t_sim) - (0.5 * GRAVITY * t_sim**2)
				if y_th >= 0: path_th.append(to_pyg(pymunk.Vec2d(x_th, y_th)))
				if body.position.y <= 0:
					sim_frozen = True
					break

		if sim_frozen and not results_updated:
			ui_app.res_sim["r"].set(f"{body.position.x:.2f}")
			ui_app.res_sim["h"].set(f"{h_max:.2f}")
			ui_app.res_sim["t"].set(f"{t_sim:.2f}")
			results_updated = True

		screen.fill((255, 255, 255))
		if fondo: screen.blit(fondo, (0,0))
		if len(path_th) > 1: pygame.draw.lines(screen, (30, 144, 255), False, path_th, 1)
		if len(path_real) > 1: pygame.draw.lines(screen, (220, 20, 60), False, path_real, 3)
		
		# HUD
		txt_teorico = f"TEÓRICO: Alcance {p['r_th']:.1f}m | Hmax {p['h_th']:.1f}m"
		screen.blit(font.render(txt_teorico, True, (30, 144, 255)), (20, 20))
		txt_dinamico = f"V: {body.velocity.length:.1f} m/s | T: {t_sim:.2f} s"
		screen.blit(font.render(txt_dinamico, True, (0, 0, 0)), (20, 40))
		if sim_frozen:
			txt_real = f"REAL: Alcance {body.position.x:.1f}m | Hmax {h_max:.1f}m"
			screen.blit(font.render(txt_real, True, (220, 20, 60)), (20, 65))
		
		re_txt = f"Re: {reynolds:,.0f} | Cd: {cd_calc:.3f}"
		surf_re = font.render(re_txt, True, (50, 50, 50))
		screen.blit(surf_re, (WIDTH - surf_re.get_width() - 20, 20))

		pygame.display.flip()
		p["tk_root"].update()
		clock.tick(FPS)
	pygame.quit()

if __name__ == "__main__":
	root = tk.Tk()
	app = BallisticsUI(root)
	root.mainloop()
