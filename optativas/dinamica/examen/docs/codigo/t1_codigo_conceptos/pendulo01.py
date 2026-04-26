import pymunk
import pygame
import math

def run_simulation():
    pygame.init()
    width, height = 800, 600
    screen = pygame.display.set_mode((width, height)) 
    pygame.display.set_caption("Simulación de un muelle amortiguado")
    clock = pygame.time.Clock()

    space = pymunk.Space()
    space.gravity = (0, 900)
    
    ceiling_pos = (400, 100)

    # --- CONFIGURACIÓN DEL PÉNDULO ---
    mass = 1.0
    radius = 40
    # Importante: darle un momento de inercia para estabilidad
    moment = pymunk.moment_for_circle(mass, 0, radius)
    body = pymunk.Body(mass, moment)
    
    # Ángulo inicial de 70 grados (mucha energía)
    longitud_hilo = 350
    angulo_inicial = math.radians(70)
    
    body.position = (
        ceiling_pos[0] + longitud_hilo * math.sin(angulo_inicial),
        ceiling_pos[1] + longitud_hilo * math.cos(angulo_inicial)
    )
    
    shape = pymunk.Circle(body, radius)
    space.add(body, shape)

    # El Hilo
    joint = pymunk.PinJoint(space.static_body, body, ceiling_pos, (0, 0))
    space.add(joint)

    # --- PARÁMETRO DE AMORTIGUACIÓN ---
    b = 0.3  # Coeficiente de amortiguamiento (ajústalo para frenar más o menos)

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT: running = False

        # APLICAR AMORTIGUACIÓN MANUAL (F = -b * v)
        # Esto garantiza que siempre haya una fuerza opuesta al movimiento
        v = body.velocity
        f_amortiguacion = -b * v
        body.apply_force_at_world_point(f_amortiguacion, body.position)

        space.step(1.0 / 60.0)
        
        screen.fill((255, 255, 255))

        # Dibujar Soporte
        pygame.draw.line(screen, (0, 0, 0), (350, 100), (450, 100), 5)
        
        # Dibujar Hilo
        pos = (int(body.position.x), int(body.position.y))
        pygame.draw.line(screen, (150, 150, 150), ceiling_pos, pos, 2)

        # Dibujar Esfera
        pygame.draw.circle(screen, (50, 100, 255), pos, radius)
        pygame.draw.circle(screen, (0, 0, 0), pos, radius, 2)

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()

if __name__ == "__main__":
    run_simulation()
