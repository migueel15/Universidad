import pymunk
import pygame
import math

def run_simulation():
    pygame.init()
    width, height = 800, 800
    screen = pygame.display.set_mode((width, height)) 
    pygame.display.set_caption("Péndulo Doble")
    clock = pygame.time.Clock()

    # Superficie especial para la estela (para no redibujar miles de líneas cada frame)
    trail_surface = pygame.Surface((width, height))
    trail_surface.fill((255, 255, 255))
    last_pos = None

    space = pymunk.Space()
    space.gravity = (0, 900)
    
    ceiling_pos = (400, 350)
    radius = 15 # Masas un poco más pequeñas para ver mejor el rastro
    mass = 1.0
    longitud_hilo = 180

    # CONFIGURACIÓN CAÓTICA
    body1 = pymunk.Body(mass, pymunk.moment_for_circle(mass, 0, radius))
    angle1 = math.radians(-175) 
    body1.position = (ceiling_pos[0] + longitud_hilo * math.sin(angle1), 
                      ceiling_pos[1] + longitud_hilo * math.cos(angle1))
    space.add(body1, pymunk.Circle(body1, radius))

    body2 = pymunk.Body(mass, pymunk.moment_for_circle(mass, 0, radius))
    angle2 = math.radians(-165)
    body2.position = (body1.position.x + longitud_hilo * math.sin(angle2), 
                      body1.position.y + longitud_hilo * math.cos(angle2))
    space.add(body2, pymunk.Circle(body2, radius))

    space.add(pymunk.PinJoint(space.static_body, body1, ceiling_pos, (0, 0)))
    space.add(pymunk.PinJoint(body1, body2, (0, 0), (0, 0)))

    b = 0.005 

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT: running = False

        for bdy in [body1, body2]:
            bdy.apply_force_at_world_point(-b * bdy.velocity, bdy.position)

        space.step(1.0 / 60.0)
        
        # Dibujar en la superficie de la estela (esto no se borra)
        curr_pos = (int(body2.position.x), int(body2.position.y))
        if last_pos:
            # Dibujamos una línea del punto anterior al actual en la superficie persistente
            pygame.draw.line(trail_surface, (220, 220, 220), last_pos, curr_pos, 1)
        last_pos = curr_pos

        # Renderizado
        screen.blit(trail_surface, (0, 0)) # Dibujamos el fondo con todo el rastro acumulado

        # Dibujar Hilos y Masas actuales
        p1 = (int(body1.position.x), int(body1.position.y))
        p2 = (int(body2.position.x), int(body2.position.y))
        
        pygame.draw.line(screen, (0, 0, 0), ceiling_pos, p1, 2)
        pygame.draw.line(screen, (0, 0, 0), p1, p2, 2)
        pygame.draw.circle(screen, (50, 100, 255), p1, radius)
        pygame.draw.circle(screen, (255, 100, 50), p2, radius)
        
        pygame.display.flip()
        clock.tick(60)

    pygame.quit()

if __name__ == "__main__":
    run_simulation()
