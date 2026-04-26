"""
SIMULACIÓN FÍSICA DE TIRO PARABÓLICO E IMPACTO

El programa simula el lanzamiento de un proyectil desde el suelo hacia un objetivo 
suspendido. Incluye las siguientes funcionalidades:
- Control de ángulo inicial (Izquierda/Derecha) y velocidad inicial (Arriba/Abajo).
- Línea de mira predictiva basada en el vector de velocidad inicial.
- Cuenta atrás de 5 segundos: al finalizar, el objetivo cae por gravedad y el 
  proyectil se dispara automáticamente si no ha sido lanzado antes.
- Disparo manual mediante la tecla ESPACIO.
- Detección de colisión y contacto con el suelo sin interrumpir la física.
- Gestión de reinicio: tras el evento, se puede repetir la simulación pulsando 
  ENTER o salir con ESC.
"""

import pymunk
import pygame
import math

def simular():
	pygame.init()
	screen = pygame.display.set_mode((800, 600))
	clock = pygame.time.Clock()
	fuente = pygame.font.SysFont("Arial", 30)

	def inicializar_espacio():
		sp = pymunk.Space()
		sp.gravity = (0, 900)
		
		# Suelo
		y_piso = 580
		suelo = pymunk.Segment(sp.static_body, (0, y_piso), (800, y_piso), 5)
		suelo.elasticity = 0.5
		suelo.friction = 0.5
		sp.add(suelo)
		
		# Proyectil
		m = 1
		r = 10
		b_p = pymunk.Body(m, pymunk.moment_for_circle(m, 0, r))
		b_p.position = (20, y_piso - r)
		s_p = pymunk.Circle(b_p, r)
		s_p.elasticity = 0.5
		
		# Objetivo
		b_o = pymunk.Body(1, pymunk.moment_for_circle(1, 0, r))
		b_o.position = (400, 200)
		s_o = pymunk.Circle(b_o, r)
		s_o.elasticity = 0.5
		
		return sp, b_p, s_p, b_o, s_o, y_piso

	# Variables que se mantienen entre reinicios
	angulo = 30.0
	velocidad = 1000

	# Estado inicial
	space, proyectil, shape_p, objetivo, shape_o, suelo_y = inicializar_espacio()
	cuenta_atras = 5.0
	objetivo_liberado = False
	proyectil_disparado = False
	finalizado = False

	#--------------------------------
	def dispara(proyectil,v_mag,angulo):
	# Disparar proyectil
		rad = math.radians(-angulo) # Negativo porque Y crece hacia abajo
		proyectil.velocity = (v_mag * math.cos(rad), v_mag * math.sin(rad))
		space.add(proyectil, shape_p)
		return True
	#----------------------------------

	running = True
	while running:
		dt = clock.tick(60) / 1000.0
		
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				running = False
			
			if event.type == pygame.KEYDOWN:
				# Si la simulación ha terminado (colisión o suelo), Enter reinicia
				if finalizado and event.key == pygame.K_RETURN:
					space, proyectil, shape_p, objetivo, shape_o, suelo_y = inicializar_espacio()
					cuenta_atras = 5.0
					objetivo_liberado = False
					proyectil_disparado = False
					finalizado = False
				
				# Escape para salir siempre disponible
				if event.key == pygame.K_ESCAPE:
					running = False
				
				# DISPARO CON ESPACIO (Evita el conflicto con Enter de reinicio)
				if event.key == pygame.K_SPACE and not proyectil_disparado and not finalizado:
					proyectil_disparado=dispara(proyectil,velocidad,angulo)

		# Controles de puntería (solo si no se ha disparado)
		if not proyectil_disparado:
			keys = pygame.key.get_pressed()
			if keys[pygame.K_LEFT]:  angulo += 0.25
			if keys[pygame.K_RIGHT]: angulo -= 0.25
			if keys[pygame.K_UP]:    velocidad += 10
			if keys[pygame.K_DOWN]:  velocidad -= 10
			angulo = max(0, min(90, angulo))
			velocidad = max(100, min(3000, velocidad))

		# Cronómetro y liberación
		if not objetivo_liberado:
			cuenta_atras -= dt
			if cuenta_atras <= 0:
				space.add(objetivo, shape_o)
				objetivo_liberado = True
				cuenta_atras = 0
				# Disparo automático si no se disparó antes
				if not proyectil_disparado:
					proyectil_disparado=dispara(proyectil,velocidad,angulo)

		# Evolución de la física (siempre activa)
		space.step(1/60.0)

		# Comprobación de estado finalizado
		dist = (proyectil.position - objetivo.position).length
		if dist < 20 or (objetivo_liberado and objetivo.position.y > suelo_y - 12):
			finalizado = True

		# --- RENDERIZADO ---
		screen.fill((255, 255, 255))
		pygame.draw.line(screen, (0, 0, 0), (0, suelo_y), (800, suelo_y), 5)

		# Guía visual
		if not proyectil_disparado:
			r_m = math.radians(-angulo)
			for i in range(1, 30):
				px = proyectil.position.x + i * 20 * math.cos(r_m)
				py = proyectil.position.y + i * 20 * math.sin(r_m)
				if px > 800 or py < 0: break
				pygame.draw.circle(screen, (150, 150, 150), (int(px), int(py)), 2)

		# Proyectil y Objetivo
		pygame.draw.circle(screen, (0, 0, 255), (int(proyectil.position.x), int(proyectil.position.y)), 10)
		color_o = (255, 0, 0) if objetivo_liberado else (200, 100, 100)
		pygame.draw.circle(screen, color_o, (int(objetivo.position.x), int(objetivo.position.y)), 10)

		# UI superior
		txt_status = f"Objetivo cae en: {max(0, cuenta_atras):.1f}s" if not objetivo_liberado else "¡LIBRE!"
		screen.blit(fuente.render(txt_status, True, (0, 0, 0)), (20, 20))
		screen.blit(fuente.render(f"Ángulo: {angulo:.1f}°", True, (0, 0, 0)), (20, 60))
		screen.blit(fuente.render(f"Velocidad: {velocidad} px/s", True, (0, 0, 0)), (20, 100))

		# UI inferior
		if not proyectil_disparado and not finalizado:
			screen.blit(fuente.render("[ESPACIO] Disparar", True, (0, 100, 0)), (300, 520))
		
		if finalizado:
			screen.blit(fuente.render("[ENTER] Nueva simulación | [ESC] Salir", True, (200, 0, 0)), (210, 550))

		pygame.display.flip()

	pygame.quit()

if __name__ == "__main__":
	simular()
