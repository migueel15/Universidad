import pymunk
import pygame
import math

def draw_zigzag_spring(screen, start, end, num_steps=15, width=12):
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
    pygame.draw.lines(screen, (100, 100, 100), False, points, 2)

def run_simulation():
    pygame.init()
    width, height = 600, 800
    screen = pygame.display.set_mode((width, height)) 
    pygame.display.set_caption("Sistema Doble Masa-Muelle Amortiguado")
    clock = pygame.time.Clock()

    space = pymunk.Space()
    space.gravity = (0, 900)
    space.damping = 0.98 # Amortiguación global

    ceiling_pos = (300, 50)
    radius = 30
    mass = 1.0

    # --- MASA 1 ---
    moment1 = pymunk.moment_for_circle(mass, 0, radius)
    body1 = pymunk.Body(mass, moment1)
    body1.position = (300, 250) # Posición inicial
    shape1 = pymunk.Circle(body1, radius)
    space.add(body1, shape1)

    # --- MASA 2 ---
    moment2 = pymunk.moment_for_circle(mass, 0, radius)
    body2 = pymunk.Body(mass, moment2)
    body2.position = (300, 500) # Posición inicial
    shape2 = pymunk.Circle(body2, radius)
    space.add(body2, shape2)

    # --- MUELLE 1 (Techo a Masa 1) ---
    spring1 = pymunk.DampedSpring(
        space.static_body, body1, ceiling_pos, (0, 0), 
        rest_length=150, stiffness=150, damping=0.5
    )
    space.add(spring1)

    # --- MUELLE 2 (Masa 1 a Masa 2) ---
    spring2 = pymunk.DampedSpring(
        body1, body2, (0, 0), (0, 0), 
        rest_length=150, stiffness=100, damping=0.3
    )
    space.add(spring2)

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT: running = False

        space.step(1.0 / 60.0)
        screen.fill((255, 255, 255))

        # 1. Dibujar Techo
        pygame.draw.line(screen, (0, 0, 0), (200, 50), (400, 50), 5)

        # 2. Dibujar Muelles
        draw_zigzag_spring(screen, ceiling_pos, body1.position, num_steps=15)
        draw_zigzag_spring(screen, body1.position, body2.position, num_steps=15)

        # 3. Dibujar Masas
        for body in [body1, body2]:
            pos = (int(body.position.x), int(body.position.y))
            pygame.draw.circle(screen, (50, 100, 255), pos, radius)
            pygame.draw.circle(screen, (0, 0, 0), pos, radius, 2)

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()

if __name__ == "__main__":
    run_simulation()
