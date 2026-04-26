import pygame
import pymunk
import math
from rozamiento_aire import get_Cd,mach_correction

# === Configuración ===
WIDTH, HEIGHT = 1000, 600
FPS = 60
GRAVITY = 9.8 
SCALE = 1 / 7
ORIGIN_X, ORIGIN_Y = 50, HEIGHT - 10 

# --- PARÁMETRO DE TIEMPO ---
TIME_SCALE = 15.0  

# --- Parámetros Físicos ---
M_KG = 24 * 0.453592
DENSITY_IRON = 7874 
DENSITY_BRONZE = 8800
DENSITY_GRANITE = 2500

DENSITY=DENSITY_BRONZE
vol_m3 = M_KG / DENSITY
R_PHYSICAL_M = ((3 * vol_m3) / (4 * math.pi))**(1/3) 
DIAMETER_M = 2 * R_PHYSICAL_M

#### Los de cadiz
#M_KG = 190* 0.453592  #190 LIBRAS
#R_PHYSICAL_M=2.54*13/2/100  #13 PULGADAS DIAMETRO
#DIAMETER_M = 2 * R_PHYSICAL_M
### densidad media de unos 4500 kg/m3


V0_M_S = 600 

ANGLE_RAD = math.radians(40)

# --- Aire ---
CD_0 = 0.44
RHO_AIR_0 = 1.2 
RHO_AIR=RHO_AIR_0
A_M2 = math.pi * (R_PHYSICAL_M**2)
#DRAG_CONSTANT = 0.5 * CD * RHO_AIR * A_M2
MU_AIR = 1.789e-5 

# --- Colores ---
WHITE, BLACK = (255, 255, 255), (0, 0, 0)
RED, BLUE = (220, 20, 60), (30, 144, 255)
GRAY, GREEN = (100, 100, 100), (34, 139, 34)

def to_pygame(p):
	return int(p.x * SCALE + ORIGIN_X), int(ORIGIN_Y - p.y * SCALE)


#########################################################
def apply_drag(body,A_M2=A_M2,CD=CD_0,RHO_AIR=RHO_AIR_0):
	DRAG_CONSTANT = 0.5 * CD * RHO_AIR * A_M2
	speed = body.velocity.length
	if speed > 0.01:
		force = - DRAG_CONSTANT * speed * body.velocity
		body.apply_force_at_local_point(force, (0,0))
###########################################################

def main():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	pygame.display.set_caption(f"Balística x{TIME_SCALE} - Escala 1px:4m")
	clock = pygame.time.Clock()
	font = pygame.font.SysFont("Consolas", 16, bold=True)

	space = pymunk.Space()
	fondo_bitmap = pygame.transform.scale(pygame.image.load("fondo_cadiz.png"),(WIDTH,HEIGHT))
	
	space.gravity = (0.0, -GRAVITY)
	
	body = pymunk.Body(M_KG, pymunk.moment_for_circle(M_KG, 0, R_PHYSICAL_M))
	body.position = (0, 0)
	body.velocity = (V0_M_S * math.cos(ANGLE_RAD), V0_M_S * math.sin(ANGLE_RAD))
	shape = pymunk.Circle(body, 8) 
	space.add(body, shape)
	
	path_real, path_th = [], []
	time_sim, sim_frozen = 0.0, False
	reynolds, h_max_real = 0.0, 0.0
	
	res_real = {"range": 0, "v_final": 0, "t_flight": 0, "h_max": 0}

	# Datos Teóricos
	t_th = (2 * V0_M_S * math.sin(ANGLE_RAD)) / GRAVITY
	r_th = (V0_M_S**2 * math.sin(2 * ANGLE_RAD)) / GRAVITY
	h_th = (V0_M_S**2 * (math.sin(ANGLE_RAD)**2)) / (2 * GRAVITY)

	sub_steps = 10
	dt_fisico = ((1.0 / FPS) * TIME_SCALE) / sub_steps

	running = True
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT: running = False
			if event.type == pygame.KEYDOWN and event.key == pygame.K_r: main(); return

		screen.fill(WHITE)
		screen.blit(fondo_bitmap, (0, 0))
		#pygame.draw.line(screen, GREEN, (0, ORIGIN_Y), (WIDTH, ORIGIN_Y), 3)

		if not sim_frozen:
			for _ in range(sub_steps):
				##############################
				speed = body.velocity.length
				reynolds = (RHO_AIR * speed * DIAMETER_M) / MU_AIR
				Cd_calc=get_Cd(reynolds)
				apply_drag(body,CD=Cd_calc*mach_correction(speed))
				#apply_drag(body,CD=Cd_calc)
				
				##############################
				space.step(dt_fisico)
				time_sim += dt_fisico
				
				curr = body.position
				if curr.y > h_max_real: h_max_real = curr.y
				
				if int(time_sim * 100) % 2 == 0:
					path_real.append(to_pygame(curr))
					x_th = (V0_M_S * math.cos(ANGLE_RAD)) * time_sim
					y_th = (V0_M_S * math.sin(ANGLE_RAD) * time_sim) - (0.5 * GRAVITY * time_sim**2)
					if y_th >= -10: path_th.append(to_pygame(pymunk.Vec2d(x_th, y_th)))

				if curr.y <= 0:
					sim_frozen = True
					res_real = {"range": curr.x, "v_final": body.velocity.length, "t_flight": time_sim, "h_max": h_max_real}
					break
			
			# Reynolds calculado en cada frame visual
			speed = body.velocity.length
			reynolds = (RHO_AIR * speed * DIAMETER_M) / MU_AIR
			Cd_calc=get_Cd(reynolds)
			

		# Dibujo
		if len(path_th) > 2: pygame.draw.lines(screen, BLUE, False, path_th, 2)
		if len(path_real) > 2: pygame.draw.lines(screen, RED, False, path_real, 3)
		if not sim_frozen: pygame.draw.circle(screen, GRAY, to_pygame(body.position), 8)

		# --- UI IZQUIERDA (Resultados) ---
		left_panel = [
			(f"MODO: x{TIME_SCALE} Velocidad", BLACK),
			(f"--- TEÓRICO (Sin aire) ---", BLUE),
			(f"Alcance: {r_th:.1f} m | H.Máx: {h_th:.1f} m", BLUE),
			(f"Tiempo: {t_th:.2f} s | V.Final: {V0_M_S:.1f} m/s", BLUE),
		]
		if sim_frozen:
			left_panel.extend([
				(f"--- REAL (Con arrastre) ---", RED),
				(f"Alcance: {res_real['range']:.1f} m | H.Máx: {res_real['h_max']:.1f} m", RED),
				(f"Tiempo: {res_real['t_flight']:.2f} s | V.Final: {res_real['v_final']:.1f} m/s", RED),
				(f"PULSA 'R' PARA REINICIAR", BLACK)
			])
		else:
			left_panel.append((f"Simulando vuelo...", BLACK))

		for i, (txt, col) in enumerate(left_panel):
			screen.blit(font.render(txt, True, col), (20, 20 + i*22))

		# --- UI DERECHA (Reynolds) ---
		re_label = f"Re: {reynolds:,.0f}"
		regimen = "Régimen: Turbulento" if reynolds > 4000 else "Régimen: Laminar"
		Cd_label = f"Cd: {Cd_calc:,.3f}"
		
		surf_re = font.render(re_label, True, BLACK)
		surf_reg = font.render(regimen, True, (100, 100, 100))
		surf_Cd = font.render(Cd_label, True, BLACK)
		
		screen.blit(surf_re, (WIDTH - surf_re.get_width() - 20, 20))
		screen.blit(surf_reg, (WIDTH - surf_reg.get_width() - 20, 42))
		screen.blit(surf_Cd, (WIDTH - surf_Cd.get_width() - 20, 64))
		

		pygame.display.flip()
		clock.tick(FPS)
	pygame.quit()

if __name__ == "__main__": main()
