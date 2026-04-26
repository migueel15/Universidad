import pygame
import pymunk
import pymunk.pygame_util

# Colores
COLOR_CARROCERIA = (173, 216, 230)  # Azul Claro Pastel
COLOR_VENTANA = (255, 255, 255)  # Blanco
COLOR_CROMO = (150, 150, 150)  # Gris metálico
COLOR_RUEDA = (40, 40, 40)  # Negro neumático


def draw_sedan_blue(screen, body, w, h):
    # 1. Base del chasis (Rectángulo principal)
    pts_base_local = [
        (-w / 2, -h / 2),
        (w / 2, -h / 2),
        (w / 2, h / 2),
        (-w / 2, h / 2),
    ]

    # 2. Cabina (Trapecio Isósceles: Base grande ABAJO)
    # La base grande coincide con el ancho del chasis (o casi)
    # La base pequeña es el techo
    cabina_h = h * 0.8
    base_grande = w * 0.8
    base_pequeña = w * 0.5

    pts_cabina_local = [
        (-base_grande / 2, -h / 2),  # Esquina inferior izquierda (en la base grande)
        (base_grande / 2, -h / 2),  # Esquina inferior derecha (en la base grande)
        (base_pequeña / 2, -h / 2 - cabina_h),  # Esquina superior derecha (techo)
        (-base_pequeña / 2, -h / 2 - cabina_h),  # Esquina superior izquierda (techo)
    ]

    # 3. Ventanas (Adaptadas al trapecio isósceles)
    # Ventana delantera y trasera separadas por un pilar central
    v_margin = 6
    pts_v1 = [
        (-base_grande / 2 + 10, -h / 2 - 5),
        (-2, -h / 2 - 5),
        (-2, -h / 2 - cabina_h + v_margin),
        (-base_pequeña / 2 + 8, -h / 2 - cabina_h + v_margin),
    ]
    pts_v2 = [
        (2, -h / 2 - 5),
        (base_grande / 2 - 10, -h / 2 - 5),
        (base_pequeña / 2 - 8, -h / 2 - cabina_h + v_margin),
        (2, -h / 2 - cabina_h + v_margin),
    ]

    def transform(pts):
        out = []
        for p in pts:
            v = pygame.math.Vector2(p).rotate_rad(body.angle)
            out.append((body.position.x + v.x, body.position.y + v.y))
        return out

    # Dibujo de las capas
    pygame.draw.polygon(
        screen, COLOR_CARROCERIA, transform(pts_cabina_local)
    )  # Estructura cabina
    pygame.draw.polygon(screen, COLOR_VENTANA, transform(pts_v1))  # Ventana frontal
    pygame.draw.polygon(screen, COLOR_VENTANA, transform(pts_v2))  # Ventana trasera
    pygame.draw.polygon(
        screen, COLOR_CARROCERIA, transform(pts_base_local)
    )  # Cuerpo principal
    pygame.draw.polygon(screen, (0, 0, 0), transform(pts_base_local), 2)  # Contorno


def run_simulation():
    pygame.init()
    width, height = 1200, 600
    screen = pygame.display.set_mode((width, height))
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("Arial", 18, bold=True)

    space = pymunk.Space()
    space.gravity = (0, 900)

    # Parámetros
    pos_i = (200, height - 150)
    chasis_w, chasis_h = 170, 45
    R = 30
    m_chasis, m_rueda = 25.0, 1.2

    # Crear Chasis
    body_c = pymunk.Body(
        m_chasis, pymunk.moment_for_box(m_chasis, (chasis_w, chasis_h))
    )
    body_c.position = pos_i
    shape_c = pymunk.Poly.create_box(body_c, (chasis_w, chasis_h))
    shape_c.filter = pymunk.ShapeFilter(group=1)
    space.add(body_c, shape_c)

    # Crear Ruedas (Tracción trasera)
    def add_w(pos, f):
        b = pymunk.Body(m_rueda, pymunk.moment_for_circle(m_rueda, 0, R))
        b.position = pos
        s = pymunk.Circle(b, R)
        s.friction = f
        s.elasticity = 0.9  # 80% de recuperación de energía
        s.filter = pymunk.ShapeFilter(group=1)
        space.add(b, s)
        return b

    # Fricción baja atrás para ver el patinado
    Rozamiento = 0.3
    body_rt = add_w((pos_i[0] - 60, pos_i[1] + 30), Rozamiento)
    body_rd = add_w((pos_i[0] + 60, pos_i[1] + 30), Rozamiento)

    # Uniones
    space.add(pymunk.PivotJoint(body_c, body_rt, body_rt.position))
    space.add(pymunk.PivotJoint(body_c, body_rd, body_rd.position))

    # Suelo
    ground = pymunk.Segment(
        space.static_body, (0, height - 80), (10000, height - 80), 5
    )
    ground.friction = 1.0
    ground.elasticity = 0.9  # 80% de recuperación de energía
    space.add(ground)

    applied_torque = 0.0
    torque_step = 15000.0

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                return
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_UP:
                    applied_torque = (
                        0.0 if applied_torque < 0 else applied_torque + torque_step
                    )
                if event.key == pygame.K_DOWN:
                    applied_torque = (
                        0.0 if applied_torque > 0 else applied_torque - torque_step
                    )
                if event.key == pygame.K_ESCAPE:
                    body_c.position = pos_i
                    body_c.velocity = (0, 0)
                    body_c.angle = 0
                    body_rt.position = (pos_i[0] - 60, pos_i[1] + 30)
                    body_rt.velocity = (0, 0)
                    body_rt.angular_velocity = 0
                    body_rd.position = (pos_i[0] + 60, pos_i[1] + 30)
                    body_rd.velocity = (0, 0)
                    body_rd.angular_velocity = 0
                    applied_torque = 0.0

        frenando = pygame.key.get_pressed()[pygame.K_SPACE]
        if frenando:
            for b in [body_rt, body_rd]:
                b.angular_velocity *= 0.1
            applied_torque = 0.0
        else:
            if applied_torque < 0 and body_rt.angular_velocity <= 0.05:
                body_rt.torque = 0
                applied_torque = 0.0
            else:
                body_rt.torque = applied_torque

        space.step(1 / 60.0)
        screen.fill((245, 245, 245))

        # Dibujo del coche
        draw_sedan_blue(screen, body_c, chasis_w, chasis_h)

        # Dibujo de ruedas
        for b in [body_rt, body_rd]:
            pygame.draw.circle(
                screen, COLOR_RUEDA, (int(b.position.x), int(b.position.y)), R
            )
            pygame.draw.circle(
                screen,
                COLOR_CROMO,
                (int(b.position.x), int(b.position.y)),
                int(R * 0.5),
            )
            v = pygame.math.Vector2(R, 0).rotate_rad(b.angle)
            pygame.draw.line(screen, (255, 255, 255), b.position, b.position + v, 2)

        # Suelo y UI
        pygame.draw.line(
            screen, (50, 50, 50), (0, height - 80), (width, height - 80), 5
        )
        screen.blit(
            font.render(f"Torque: {applied_torque/1000:.1f} kNm", True, (0, 0, 0)),
            (20, 20),
        )
        screen.blit(
            font.render(
                f"Velocidad: {abs(body_c.velocity.x)/10:.1f} km/h", True, (0, 0, 0)
            ),
            (20, 45),
        )

        pygame.display.flip()
        clock.tick(60)


run_simulation()
