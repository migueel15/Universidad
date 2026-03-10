"""El muelle se puede amortiguar con su propio amortiguamiento o
con el genérico del espacio:
IMPORTANTE: el damping del espacio 1 -> no hay amortiguamiento
en el damping del muelle 0 -> no hay amortiguamiento"""

import math

import pygame
import pymunk


def draw_zigzag_spring(screen, start, end, num_steps=25, width=15):
    """Dibuja un muelle en zigzag entre dos puntos."""
    start_x, start_y = start
    end_x, end_y = end

    dx, dy = end_x - start_x, end_y - start_y
    length = math.sqrt(dx**2 + dy**2)

    if length == 0:
        return

    ux, uy = dx / length, dy / length
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
    space.damping = 1.0  # Se puede usar el amortiguamiento del espacio
    # o el propio del muelle (ver abajo)

    ceiling_pos = (300, 50)

    # --- CONFIGURACIÓN DE GRAN AMPLITUD ---
    mass = 8.0
    radius = 40
    # tiempo característico 2*mass/bmuelle
    kmuelle = 150
    bmuelle = math.sqrt(4 * mass * kmuelle) * 0.5
    body = pymunk.Body(mass, pymunk.moment_for_circle(mass, 0, radius))

    # EQUILIBRIO: Techo(50) + rest_length(350) = 400
    # POSICIÓN INICIAL: 650 (Diferencia de 250px = Gran Amplitud)
    body.position = (300, 350)  # 400 es la posición de equilibrio)
    body.velocity = (0, -2000)

    shape = pymunk.Circle(body, radius)
    space.add(body, shape)

    rest_length = 350
    # Aumentamos stiffness (k) a 150 para que el tirón sea más fuerte
    spring = pymunk.DampedSpring(
        space.static_body,
        body,
        ceiling_pos,
        (0, 0),
        rest_length=rest_length,
        stiffness=kmuelle,
        damping=bmuelle,
    )
    space.add(spring)
    # --------------------------------------

    camara_lenta = 1  # factor de  reducciónd de velocidad
    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        space.step(1.0 / (60 * camara_lenta))
        screen.fill((255, 255, 255))

        # 1. Dibujar Techo
        pygame.draw.line(screen, (0, 0, 0), (150, 50), (450, 50), 5)

        # 2. Dibujar Muelle (con más pasos para el zigzag)
        draw_zigzag_spring(screen, ceiling_pos, body.position, num_steps=30)

        # 3. Dibujar Esfera
        # print(body.position.y)
        pos = (int(body.position.x), int(body.position.y))
        pygame.draw.circle(screen, (50, 100, 255), pos, radius)
        pygame.draw.circle(screen, (0, 0, 0), pos, radius, 2)
        y_equ = 400 + mass * space.gravity[1] / kmuelle
        print(y_equ)
        pygame.draw.line(screen, "gray", (0, y_equ), (800, y_equ), 1)

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    run_simulation()
