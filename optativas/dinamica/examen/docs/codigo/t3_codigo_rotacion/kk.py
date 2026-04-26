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

    # Espacio de Pymunk (Sin gravedad)
    space = pymunk.Space()
    space.gravity = (0, 0)

    # Parámetros físicos
    mass = 1.0
    size = (40, 40)
    v0 = 100.0
    R = 700
    ancho_carretera = 80
    force_magnitude = 13

    # Punto de aplicación local
    punto_local = (10, 10)

    # Crear la caja
    inertia = pymunk.moment_for_box(mass, size)
    body = pymunk.Body(mass, inertia)
    start_pos = pymunk.Vec2d(100, height - 150)
    body.position = start_pos
    body.velocity = (v0, 0)

    shape = pymunk.Poly.create_box(body, size)
    space.add(body, shape)

    # Lógica de la carretera
    centro_x = start_pos.x
    centro_y = start_pos.y - R
    R_ajustado = R + (ancho_carretera / 2)
    rect_carretera = pygame.Rect(
        centro_x - R_ajustado, centro_y - R_ajustado, 2 * R_ajustado, 2 * R_ajustado
    )

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        # --- FÍSICA ---
        v = body.velocity
        force = pymunk.Vec2d(0, 0)

        if v.length > 0:
            v_unit = v.normalized()
            f_direction = pymunk.Vec2d(v_unit.y, -v_unit.x)
            force = f_direction * force_magnitude
            body.apply_force_at_local_point(force, punto_local)

        space.step(1.0 / 60.0)

        # --- DIBUJO ---
        screen.fill((30, 30, 30))

        # Carretera
        pygame.draw.arc(
            screen,
            (200, 200, 200),
            rect_carretera,
            math.radians(-90),
            math.radians(90),
            ancho_carretera,
        )

        # Caja
        pos = body.position
        angle_deg = math.degrees(body.angle)

        box_surface = pygame.Surface(size, pygame.SRCALPHA)
        box_surface.fill((50, 150, 250))
        pygame.draw.rect(box_surface, (255, 255, 255), (0, 0, size[0], size[1]), 2)

        # Dibujamos el punto de aplicación sobre la superficie del cuadrado (local)
        # Nota: (0,0) local en Pymunk es el centro, en Pygame es la esquina superior izquierda.
        pygame.draw.circle(
            box_surface,
            (255, 50, 50),
            (punto_local[0] + size[0] // 2, punto_local[1] + size[1] // 2),
            4,
        )

        rotated_box = pygame.transform.rotate(box_surface, -angle_deg)
        rect = rotated_box.get_rect(center=(pos.x, pos.y))
        screen.blit(rotated_box, rect)

        # --- VECTOR FUERZA EN EL PUNTO ---
        if force.length > 0:
            # Convertimos el punto local (10, 10) a coordenadas de mundo (globales)
            # Esto tiene en cuenta la posición y la rotación actual del cuerpo
            punto_mundo = body.local_to_world(punto_local)

            # Dibujamos la línea de la fuerza partiendo exactamente desde el punto de aplicación
            pygame.draw.line(
                screen, (255, 50, 50), punto_mundo, punto_mundo + (force * 4), 3
            )

        # --- INFORMACIÓN ---
        text_v = font.render(f"Velocidad: {v.length:.2f} px/s", True, (255, 255, 255))
        text_w = font.render(
            f"Vel. Angular: {body.angular_velocity:.2f} rad/s", True, (255, 255, 255)
        )
        screen.blit(text_v, (20, 20))
        screen.blit(text_w, (20, 50))

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    main()
