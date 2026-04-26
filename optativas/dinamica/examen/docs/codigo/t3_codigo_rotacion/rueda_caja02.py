import pymunk
import pymunk.pygame_util
import pygame
import math

# --- PARÁMETROS AJUSTADOS PARA DESLIZAMIENTO ---
MASA = 10.0
RADIO = 30.0
LADO = RADIO * 2
MU = 0.3
ANGULO_GRADOS = 40.0
GRAVEDAD = 981.0
VEL_INICIAL = 0.0

ANCHO = 1000
ALTO = 800


def setup_simulation():
    space = pymunk.Space()
    space.gravity = (0, GRAVEDAD)

    angulo_rad = math.radians(ANGULO_GRADOS)

    def crear_pista(y_base, col_group):
        p_alta = (900, y_base)
        p_baja = (100, y_base + 800 * math.sin(angulo_rad))

        seg = pymunk.Segment(space.static_body, p_alta, p_baja, 5)
        seg.friction = 1.0
        seg.filter = pymunk.ShapeFilter(group=col_group)
        space.add(seg)

        muro = pymunk.Segment(
            space.static_body, p_baja, (p_baja[0], p_baja[1] - 100), 5
        )
        space.add(muro)

        dx, dy = p_baja[0] - p_alta[0], p_baja[1] - p_alta[1]
        dist = math.sqrt(dx**2 + dy**2)
        u = (dx / dist, dy / dist)
        n = (-u[1], u[0])
        return p_alta, u, n

    p_a1, u1, n1 = crear_pista(100, 1)
    # Momento de inercia de un aro (peor para rodar que una esfera)
    i_esf = MASA * (RADIO**2)
    body_e = pymunk.Body(MASA, i_esf)
    body_e.position = (p_a1[0] + n1[0] * RADIO, p_a1[1] + n1[1] * RADIO)
    body_e.velocity = (u1[0] * abs(VEL_INICIAL), u1[1] * abs(VEL_INICIAL))
    shape_e = pymunk.Circle(body_e, RADIO)
    shape_e.friction = MU
    space.add(body_e, shape_e)

    p_a2, u2, n2 = crear_pista(300, 2)
    i_caj = (1 / 6) * MASA * (LADO**2)
    body_c = pymunk.Body(MASA, i_caj)
    body_c.position = (p_a2[0] + n2[0] * (LADO / 2), p_a2[1] + n2[1] * (LADO / 2))
    body_c.angle = math.atan2(u2[1], u2[0])
    body_c.velocity = (u2[0] * abs(VEL_INICIAL), u2[1] * abs(VEL_INICIAL))
    shape_c = pymunk.Poly.create_box(body_c, (LADO, LADO))
    shape_c.friction = MU
    space.add(body_c, shape_c)

    return space, body_e, body_c


def main():
    pygame.init()
    screen = pygame.display.set_mode((ANCHO, ALTO))
    pygame.display.set_caption("Estado de Rodadura vs Deslizamiento")
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("Arial", 22, bold=True)
    font_status = pygame.font.SysFont("Arial", 24, bold=True)
    draw_options = pymunk.pygame_util.DrawOptions(screen)

    space, ball, box = setup_simulation()

    tiempo_total = 0.0
    tiempo_bola, tiempo_caja = 0.0, 0.0
    ek_bola_final, ek_caja_final = 0.0, 0.0
    bola_finalizada, caja_finalizada = False, False

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                return

        dt = 1 / 60.0
        if not (bola_finalizada and caja_finalizada):
            tiempo_total += dt

        # Detección de Rodadura Pura para la Bola
        v_lineal = ball.velocity.length
        v_tangencial = abs(ball.angular_velocity) * RADIO

        # Usamos un margen de tolerancia (0.5) para el motor físico
        if v_lineal < 1.0:  # En reposo
            estado_txt = "REPOSO"
        elif abs(v_lineal - v_tangencial) < 1.0:
            estado_txt = "RODADURA PURA"
        else:
            estado_txt = "DESLIZAMIENTO"

        if not bola_finalizada:
            ke_ball_trans = 0.5 * ball.mass * ball.velocity.length**2
            ke_ball_rot = 0.5 * ball.moment * ball.angular_velocity**2
            ek_bola_actual = ke_ball_trans + ke_ball_rot

            if ball.position.x <= 200:
                tiempo_bola = tiempo_total
                ek_bola_final = ek_bola_actual
                bola_finalizada = True

        if not caja_finalizada:
            ek_caja_actual = 0.5 * box.mass * box.velocity.length**2

            if box.position.x <= 200:
                tiempo_caja = tiempo_total
                ek_caja_final = ek_caja_actual
                caja_finalizada = True

        screen.fill((255, 255, 255))
        pygame.draw.line(screen, (255, 0, 0), (200, 0), (200, ALTO), 2)
        space.debug_draw(draw_options)

        t_b = tiempo_bola if bola_finalizada else tiempo_total
        e_b = ek_bola_final if bola_finalizada else ek_bola_actual
        t_c = tiempo_caja if caja_finalizada else tiempo_total
        e_c = ek_caja_final if caja_finalizada else ek_caja_actual

        surf_bola = font.render(
            f"BOLA | Tiempo: {t_b:.3f}s | Ek Meta: {e_b/1000:.1f}k", True, (200, 0, 0)
        )
        surf_caja = font.render(
            f"CAJA | Tiempo: {t_c:.3f}s | Ek Meta: {e_c/1000:.1f}k", True, (0, 0, 200)
        )

        # Mostrar estado de rodadura en verde
        surf_estado = font_status.render(
            f"ESTADO BOLA: {estado_txt}", True, (34, 139, 34)
        )

        screen.blit(surf_bola, (600, 40))
        screen.blit(surf_estado, (600, 75))
        screen.blit(surf_caja, (600, 240))

        space.step(dt)
        pygame.display.flip()
        clock.tick(60)


if __name__ == "__main__":
    main()
