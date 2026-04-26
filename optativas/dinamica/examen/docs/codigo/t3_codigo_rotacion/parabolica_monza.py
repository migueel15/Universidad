import pygame
import pymunk
import math


def main():
    # Configuración de Pygame
    pygame.init()
    width, height = 1000, 750
    screen = pygame.display.set_mode((width, height))
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("Arial", 20)

    # Espacio de Pymunk
    space = pymunk.Space()
    space.gravity = (0, 0)

    # Parámetros físicos
    mass = 1.0
    size = (30, 30)
    v0 = 180.0

    # Parábola lateral: x = a * y^2
    # Aumentamos 'a' para cerrar la curva (antes 0.0015, ahora 0.004)
    a = 0.004

    # Punto del vértice (desplazado a la izquierda)
    vertice = pymunk.Vec2d(150, height // 2)

    # Posición inicial: más alejada en Y para ver más tramo
    y_start_rel = -320
    start_pos = pymunk.Vec2d(vertice.x + a * (y_start_rel**2), vertice.y - y_start_rel)

    body = pymunk.Body(mass, pymunk.moment_for_box(mass, size))
    body.position = start_pos

    # Velocidad inicial tangente
    dx_dy = 2 * a * (-y_start_rel)
    angle_init = math.atan2(1, dx_dy) + math.pi
    body.velocity = pymunk.Vec2d(v0, 0).rotated(angle_init)

    shape = pymunk.Poly.create_box(body, size)
    space.add(body, shape)

    # --- GENERACIÓN DE LA CARRETERA ---
    puntos_carretera = []
    # Rango amplio para cubrir toda la pantalla
    for py_rel in range(-350, 351, 2):
        px = vertice.x + a * (py_rel**2)
        py = vertice.y + py_rel
        if px < width + 100:  # Solo añadir si está cerca de la pantalla
            puntos_carretera.append((px, py))

    running = True
    while running:
        if body.position.x > start_pos.x and body.position.y < 0:
            body.position = start_pos
            body.velocity = pymunk.Vec2d(v0, 0).rotated(angle_init)

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        # --- FÍSICA DINÁMICA ---
        v = body.velocity
        y_rel = body.position.y - vertice.y

        # x = a*y^2 -> dx/dy = 2ay, d2x/dy2 = 2a
        dx_dy = 2 * a * y_rel
        d2x_dy2 = 2 * a

        # Radio de curvatura: R = [1 + (dx/dy)^2]^(3/2) / |d2x/dy2|
        R_curvatura = ((1 + dx_dy**2) ** 1.5) / abs(d2x_dy2)

        # Fuerza centrípeta: F = m * v^2 / R
        force_magnitude = (mass * v.length**2) / R_curvatura

        # Dirección normal
        v_unit = v.normalized()
        f_direction = pymunk.Vec2d(-v_unit.y, v_unit.x)
        # print(body.position.x, body.position.y)

        force = f_direction * force_magnitude
        body.apply_force_at_local_point(force, (0, 0))

        space.step(1.0 / 60.0)

        # --- DIBUJO ---
        screen.fill((30, 30, 30))

        # Carretera
        if len(puntos_carretera) > 1:
            pygame.draw.lines(screen, (80, 80, 80), False, puntos_carretera, 60)
            pygame.draw.lines(screen, (255, 255, 255), False, puntos_carretera, 1)

        # Caja
        pos = body.position
        angle = math.degrees(body.angle)
        box_surface = pygame.Surface(size, pygame.SRCALPHA)
        box_surface.fill((50, 150, 250))
        pygame.draw.rect(box_surface, (255, 255, 255), (0, 0, size[0], size[1]), 2)

        rotated_box = pygame.transform.rotate(box_surface, -angle)
        rect = rotated_box.get_rect(center=(pos.x, pos.y))
        screen.blit(rotated_box, rect)

        # Vector Fuerza (Rojo)
        if v.length > 0:
            pygame.draw.line(screen, (255, 50, 50), pos, pos + (force * 1.5), 3)

        # --- INFORMACIÓN ---
        text_v = font.render(f"Velocidad: {v.length:.2f} px/s", True, (255, 255, 255))
        text_f = font.render(f"Fuerza: {force_magnitude:.2f} N", True, (255, 100, 100))
        text_r = font.render(f"Radio R: {R_curvatura:.2f} px", True, (200, 200, 200))

        screen.blit(text_v, (20, 20))
        screen.blit(text_f, (20, 50))
        screen.blit(text_r, (20, 80))

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    main()
