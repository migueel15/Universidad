import pymunk
import pygame
import math

def draw_zigzag_spring(screen, start, end, num_steps=25, width=15):
    """Dibuja un muelle en zigzag entre dos puntos."""
    start_x, start_y = start
    end_x, end_y = end
    
    dx, dy = end_x - start_x, end_y - start_y
    length = math.sqrt(dx**2 + dy**2)
    
    if length == 0: return

    ux, uy = dx/length, dy/length
    px, py = -uy, ux 

    points = [start]
    for i in range(1, num_steps):
        tx = start_x + ux * (length * i / num_steps)
        ty = start_y + uy * (length * i / num_steps)
        offset = width if i % 2 == 0 else -width
        points.append((tx + px * offset, ty + py * offset))
    
    points.append(end)
    pygame.draw.lines(screen, (80, 80, 80), False, points, 2)

def run_simulation():
    pygame.init()
    # Ventana más alta (800) para ver la gran amplitud
    screen = pygame.display.set_mode((600, 800)) 
    pygame.display.set_caption("Simulación de un muelle amortiguado")
    clock = pygame.time.Clock()

    space = pymunk.Space()
    space.gravity = (0, 900)
    space.damping = 0.96 # Menos amortiguamiento para que dure más la oscilación

    ceiling_pos = (300, 50)

    # --- CONFIGURACIÓN DE GRAN AMPLITUD ---
    mass = 1.0
    radius = 40
    body = pymunk.Body(mass, pymunk.moment_for_circle(mass, 0, radius))
    
    # EQUILIBRIO: Techo(50) + rest_length(350) = 400
    # POSICIÓN INICIAL: 650 (Diferencia de 250px = Gran Amplitud)
    body.position = (300, 650) 
    
    shape = pymunk.Circle(body, radius)
    space.add(body, shape)

    rest_length = 350 
    # Aumentamos stiffness (k) a 150 para que el tirón sea más fuerte
    spring = pymunk.DampedSpring(
        space.static_body, body, ceiling_pos, (0, 0), 
        rest_length=rest_length, stiffness=150, damping=1.2
    )
    space.add(spring)
    # --------------------------------------

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT: running = False

        space.step(1.0 / 60.0)
        screen.fill((255, 255, 255))

        # 1. Dibujar Techo
        pygame.draw.line(screen, (0, 0, 0), (150, 50), (450, 50), 5)

        # 2. Dibujar Muelle (con más pasos para el zigzag)
        draw_zigzag_spring(screen, ceiling_pos, body.position, num_steps=30)

        # 3. Dibujar Esfera
        pos = (int(body.position.x), int(body.position.y))
        pygame.draw.circle(screen, (50, 100, 255), pos, radius)
        pygame.draw.circle(screen, (0, 0, 0), pos, radius, 2)

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()

if __name__ == "__main__":
    run_simulation()
