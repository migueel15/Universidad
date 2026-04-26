''' Se se añade fricción hay cambio de velocidad angular'''

import pymunk
import pymunk.pygame_util
import pygame
import sys
import numpy as np

def setup_simulation():
	width, height = 1000, 600
	pantalla = pygame.display.set_mode((width, height))
	pygame.display.set_caption("Modo Slow Motion | Flechas: +-1º")
	
	space = pymunk.Space()
	space.gravity = (0, 0)
	draw_options = pymunk.pygame_util.DrawOptions(pantalla)
	
	mass, radius = 1, 25
	moment = pymunk.moment_for_circle(mass, 0, radius)
	
	# Bola 1 - Azul
	b1 = pymunk.Body(mass, moment)
	b1.position = (250, 200)
	s1 = pymunk.Circle(b1, radius)
	####################### FISICA ############################
	s1.elasticity, s1.friction = 1.0, 0.0
	############################################################
	s1.color = (50, 150, 255, 255) 
	space.add(b1, s1)
	
	# Bola 2 - Roja
	b2 = pymunk.Body(mass, moment)
	b2.position = (750, 400)
	s2 = pymunk.Circle(b2, radius)
	####################### FISICA ############################
	s2.elasticity, s2.friction = 1.0, 0.0
	####################### FISICA ############################
	s2.color = (255, 80, 80, 255) 
	space.add(b2, s2)
	
	return space, b1, b2, pantalla, draw_options

def draw_info(pantalla, b1, b2, v1, v2, font, activa, slow):
	vel1 = pygame.Vector2(b1.velocity) if activa else v1
	vel2 = pygame.Vector2(b2.velocity) if activa else v2
	
	def get_physics(body, v):
		v_mag = v.length()
		# Energía Traslacional
		ke_trans = 0.5 * body.mass * (v_mag**2)
		# Energía Rotacional
		ke_rot = 0.5 * body.moment * (body.angular_velocity**2)
		p = body.mass * v
		return v_mag, body.angular_velocity, ke_trans, ke_rot, p

	v1m, w1, e1t, e1r, p1 = get_physics(b1, vel1)
	v2m, w2, e2t, e2r, p2 = get_physics(b2, vel2)
	
	total_et = e1t + e2t
	total_er = e1r + e2r

	lines = [
		f"BOLA 1: v={v1m:.1f} | w={w1:.2f} | Ek_trans={e1t/1000:.3f} | Ek_rot={e1r/1000:.3f}",
		f"BOLA 2: v={v2m:.1f} | w={w2:.2f} | Ek_trans={e2t/1000:.3f} | Ek_rot={e2r/1000:.3f}",
		f"TOTALES: Ek_T={total_et/1000:.3f} | Ek_R={total_er/1000:.3f} | Ek_(T+R)={(total_et+total_er)/1000:.3f} | p_tot=({(p1+p2).x:.0f},{(p1+p2).y:.0f})",
		f"(Energía en kJ)"
		
	]

	for i, text in enumerate(lines):
		surf = font.render(text, True, (50, 50, 50))
		pantalla.blit(surf, (10, 10 + i * 20))

def run_loop():
	space, b1, b2, pantalla, draw_options = setup_simulation()
	font = pygame.font.SysFont("monospace", 15)
	
	slowmotion = 0.5 
	
	v1_target = pygame.Vector2(400, 200)
	v2_base = pygame.Vector2(-400, -200)
	
	reloj = pygame.time.Clock()
	activa = False
	angulo_v2 = 0.0 
	delta_ang = 1.0 # Usamos grados directamente para pygame.rotate()
	
	while True:
		v2_current = v2_base.rotate(angulo_v2)

		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				pygame.quit()
				sys.exit()
			if event.type == pygame.KEYDOWN:
				if event.key == pygame.K_SPACE and not activa:
					b1.velocity = (v1_target.x, v1_target.y)
					b2.velocity = (v2_current.x, v2_current.y)
					activa = True
				if event.key == pygame.K_ESCAPE:
					return

		if not activa:
			keys = pygame.key.get_pressed()
			if keys[pygame.K_LEFT]: angulo_v2 -= delta_ang
			if keys[pygame.K_RIGHT]: angulo_v2 += delta_ang

		if activa:
			dt = (1/60.0) * slowmotion
			space.step(dt)

		pantalla.fill((255, 255, 255)) 
		space.debug_draw(draw_options)

		draw_info(pantalla, b1, b2, v1_target, v2_current, font, activa, slowmotion)

		if not activa:
			p1, p2 = b1.position, b2.position
			pygame.draw.line(pantalla, (200, 200, 200), p1, (p1.x + v1_target.x, p1.y + v1_target.y), 2)
			pygame.draw.line(pantalla, (200, 200, 200), p2, (p2.x + v2_current.x, p2.y + v2_current.y), 2)

		pygame.display.flip()
		reloj.tick(60)

if __name__ == "__main__":
	pygame.init()
	while True:
		run_loop()
