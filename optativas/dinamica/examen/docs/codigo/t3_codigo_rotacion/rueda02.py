import pygame
import pymunk
import pymunk.pygame_util


def run_simulation():
    pygame.init()
    width, height = 1000, 600
    screen = pygame.display.set_mode((width, height))
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("Arial", 18)

    space = pymunk.Space()
    space.gravity = (0, 900)
    draw_options = pymunk.pygame_util.DrawOptions(screen)

    # Parámetros iniciales
    R = 40
    m = 1.0
    start_pos = (R * 2, height - 100 - R - 5)
    inertia = pymunk.moment_for_circle(m, 0, R)

    body = pymunk.Body(m, inertia)
    body.position = start_pos
    shape = pymunk.Circle(body, R)
    shape.friction = 0.2
    space.add(body, shape)

    ground = pymunk.Segment(
        space.static_body, (0, height - 100), (width, height - 100), 5
    )
    ground.friction = 1.0
    space.add(ground)

    applied_torque = 0.0
    torque_step = 10000.0

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

            if event.type == pygame.KEYDOWN:
                # LÓGICA DE CONTROL TRANSICIONAL
                if event.key == pygame.K_UP:
                    if applied_torque < 0:
                        applied_torque = 0.0  # Primer toque arriba limpia el freno
                    else:
                        applied_torque += torque_step

                if event.key == pygame.K_DOWN:
                    if applied_torque > 0:
                        applied_torque = 0.0  # Primer toque abajo suelta acelerador
                    else:
                        applied_torque -= torque_step

                if event.key == pygame.K_ESCAPE:
                    body.position = start_pos
                    body.velocity = (0, 0)
                    body.angular_velocity = 0
                    body.angle = 0
                    applied_torque = 0.0

        keys = pygame.key.get_pressed()
        frenando_espacio = keys[pygame.K_SPACE]

        if frenando_espacio:
            body.torque = 0
            body.angular_velocity *= 0.1
            if abs(body.angular_velocity) < 0.1:
                body.angular_velocity = 0
                applied_torque = 0.0
        else:
            # Impedir que el torque negativo cree rotación perpetua hacia atrás
            # si lo que queremos es solo frenar hasta detenernos.
            if applied_torque < 0 and body.angular_velocity <= 0.05:
                body.torque = 0
                body.angular_velocity = 0
                applied_torque = 0.0
            else:
                body.torque = applied_torque

        space.step(1 / 60.0)

        # Cinemática para UI
        v_cm = body.velocity.x
        wR = body.angular_velocity * R
        diff = v_cm - wR
        tol = 1.0

        if frenando_espacio and abs(v_cm) > tol:
            estado = "BLOQUEO (Freno de mano)"
            color_estado = (255, 0, 0)
        elif abs(v_cm) < tol and abs(wR) < tol:
            estado = "Parado"
            color_estado = (100, 100, 100)
        elif abs(diff) < tol:
            estado = "Rodadura Pura"
            color_estado = (0, 180, 0)
        else:
            estado = "Deslizamiento / Tracción"
            color_estado = (0, 0, 200)

        screen.fill((255, 255, 255))
        space.debug_draw(draw_options)

        # UI de estados
        info_texts = [
            f"Torque: {applied_torque/1000:.1f} kNm",
            f"v: {v_cm:.2f} | wR: {wR:.2f}",
            f"Estado: {estado}",
            "FLECHAS: Control Transicional | ESPACIO: Bloqueo | ESC: Reset",
        ]

        for i, text in enumerate(info_texts):
            color = color_estado if "Estado" in text else (40, 40, 40)
            img = font.render(text, True, color)
            screen.blit(img, (20, 20 + i * 25))

        # Indicador visual de rotación
        dir_vec = pygame.math.Vector2(R, 0).rotate_rad(body.angle)
        pygame.draw.line(screen, (0, 0, 0), body.position, body.position + dir_vec, 3)

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    run_simulation()
