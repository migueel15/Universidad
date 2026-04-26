import pygame
import pymunk
import pymunk.pygame_util

# --- CONFIGURACIÓN ---
WIDTH, HEIGHT = 1000, 600
FPS = 60

def main():
	pygame.init()
	screen = pygame.display.set_mode((WIDTH, HEIGHT))
	clock = pygame.time.Clock()
	draw_options = pymunk.pygame_util.DrawOptions(screen)

	space = pymunk.Space()
	space.gravity = (0, 900)
	space.damping = 0.98 

	# --- SUELO ---
	static_body = space.static_body
	floor = pymunk.Segment(static_body, (0, 550), (1000, 550), 5)
	floor.friction = 0.0 # 0.5
	floor.elasticity = 0.5
	space.add(floor)

	# --- 1. BLOQUE BASE (Grande + Fijo) ---
	mass_base = 15
	w_g, h_g = 400, 100
	moment_base = pymunk.moment_for_box(mass_base, (w_g, h_g))
	cuerpo_base = pymunk.Body(mass_base, moment_base)
	cuerpo_base.position = (600, 500)
	
	shape_g = pymunk.Poly.create_box(cuerpo_base, (w_g, h_g))
	shape_g.friction = 0.4
	
	off_x, off_y = 175, -75
	d = 25
	v_fijo = [(off_x-d, off_y-d), (off_x+d, off_y-d), (off_x+d, off_y+d), (off_x-d, off_y+d)]
	shape_f = pymunk.Poly(cuerpo_base, v_fijo)
	
	space.add(cuerpo_base, shape_g, shape_f)

	# --- 2. CAJA CULATA (Roja) ---
	mass_c = 2
	moment_c = pymunk.moment_for_box(mass_c, (50, 50))
	cuerpo_culata = pymunk.Body(mass_c, moment_c)
	cuerpo_culata.position = (cuerpo_base.position.x + 50, cuerpo_base.position.y - 75)
	shape_culata = pymunk.Poly.create_box(cuerpo_culata, (50, 50))
	shape_culata.friction = 0.1
	shape_culata.color = (255, 0, 0, 255)
	space.add(cuerpo_culata, shape_culata)

	# --- 3. CAJA PROYECTIL (Azul) ---
	mass_p = 1
	moment_p = pymunk.moment_for_box(mass_p, (50, 50))
	cuerpo_proj = pymunk.Body(mass_p, moment_p)
	# Separamos 1 pixel extra para que no nazcan solapadas
	cuerpo_proj.position = (cuerpo_culata.position.x - 81, cuerpo_culata.position.y)
	shape_proj = pymunk.Poly.create_box(cuerpo_proj, (50, 50))
	shape_proj.friction = 0.05
	shape_proj.color = (0, 0, 255, 255)
	space.add(cuerpo_proj, shape_proj)

	# --- EL MUELLE ---
	stiffness = 5000.0
	damping = 30.0
	muelle = pymunk.DampedSpring(cuerpo_base, cuerpo_culata, (off_x, off_y), (0, 0), 300, stiffness, damping)
	space.add(muelle)

	# --- EL SEGURO (CORREGIDO) ---
	seguro = pymunk.PinJoint(cuerpo_base, cuerpo_culata, (off_x, off_y), (0, 0))
	# ESTA ES LA LÍNEA CLAVE: permite que los cuerpos sigan chocando aunque estén unidos
	seguro.collide_bodies = True 
	space.add(seguro)

	disparado = False

	# --- BUCLE PRINCIPAL ---
	running = True
	SUBSTEPS=50
	while running:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False
			if event.type == pygame.KEYDOWN:
				if event.key == pygame.K_ESCAPE:
					running = False
				elif not disparado:
					space.remove(seguro)
					disparado = True

		screen.fill((240, 240, 240))
		
		#-----------------------------------
		for _ in range(SUBSTEPS):
			space.step(1/(FPS*SUBSTEPS))
		#-----------------------------------	
			
		space.debug_draw(draw_options)
		
		p1 = cuerpo_base.local_to_world((off_x, off_y))
		p2 = cuerpo_culata.position
		pygame.draw.line(screen, (200, 0, 0), p1, p2, 3)

		pygame.display.flip()
		clock.tick(FPS)

	pygame.quit()

if __name__ == "__main__":
	main()
