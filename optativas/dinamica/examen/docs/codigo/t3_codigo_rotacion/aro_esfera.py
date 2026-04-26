"""En la caida, rodando sin deslizar, la componente tangencial del peso
es la responsable de la aceleración, como ruedan sin deslizar, a la vez
que se genera velocidad lineal se genera angular y el momento de inercia
del aro es mayor por lo que le cuesta más ganar velocidad angular (que
limita la lineal por la condición de rodadura pura)"""

import pymunk
import pymunk.pygame_util
import pygame
import math

# --- PARÁMETROS ---
MASA = 10.0
RADIO = 30.0
MU = 0.35
CRR = 0.05
ANGULO_GRADOS = 40.0
GRAVEDAD = 981.0

ANCHO = 1000
ALTO = 900


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

    # --- ESFERA MACIZA ---
    p_a1, u1, n1 = crear_pista(100, 1)

    i_esfera = (2 / 5) * MASA * (RADIO**2)

    body_s = pymunk.Body(MASA, i_esfera)
    body_s.position = (p_a1[0] + n1[0] * RADIO, p_a1[1] + n1[1] * RADIO)

    shape_s = pymunk.Circle(body_s, RADIO)
    shape_s.friction = MU

    space.add(body_s, shape_s)

    # --- ARO ---
    p_a2, u2, n2 = crear_pista(300, 2)

    i_aro = MASA * (RADIO**2)

    body_r = pymunk.Body(MASA, i_aro)
    body_r.position = (p_a2[0] + n2[0] * RADIO, p_a2[1] + n2[1] * RADIO)

    shape_r = pymunk.Circle(body_r, RADIO)
    shape_r.friction = MU

    space.add(body_r, shape_r)

    return space, body_s, body_r


def draw_ring(screen, body, radius):
    x, y = int(body.position.x), int(body.position.y)

    pygame.draw.circle(screen, (0, 0, 200), (x, y), radius, 4)
    pygame.draw.circle(screen, (255, 255, 255), (x, y), radius - 8)

    angle = body.angle
    end = (x + radius * math.cos(angle), y + radius * math.sin(angle))

    pygame.draw.line(screen, (0, 0, 200), (x, y), end, 3)


def estado_rodadura(body, radio):
    v_lineal = body.velocity.length
    v_tangencial = abs(body.angular_velocity) * radio

    if v_lineal < 1.0:
        return "REPOSO"
    elif abs(v_lineal - v_tangencial) < 1.5:
        return "RODADURA PURA"
    else:
        return "DESLIZANDO"


def main():
    pygame.init()
    screen = pygame.display.set_mode((ANCHO, ALTO))
    pygame.display.set_caption("Esfera vs Aro")
    clock = pygame.time.Clock()

    font = pygame.font.SysFont("Arial", 20, bold=True)

    draw_options = pymunk.pygame_util.DrawOptions(screen)

    space, sphere, ring = setup_simulation()

    tiempo_total = 0.0

    res_s = {"t": 0, "done": False}
    res_r = {"t": 0, "done": False}

    while True:

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                return

        dt = 1 / 60.0
        tiempo_total += dt

        # resistencia a la rodadura
        fn = MASA * GRAVEDAD * math.cos(math.radians(ANGULO_GRADOS))
        torque_rod = CRR * fn * RADIO

        for body in [sphere, ring]:
            if abs(body.angular_velocity) > 0.1:
                body.torque = -math.copysign(torque_rod, body.angular_velocity)

        # metas
        if not res_s["done"] and sphere.position.x <= 200:
            res_s["t"] = tiempo_total
            res_s["done"] = True

        if not res_r["done"] and ring.position.x <= 200:
            res_r["t"] = tiempo_total
            res_r["done"] = True

        # --- DIBUJO ---
        screen.fill((255, 255, 255))

        pygame.draw.line(screen, (255, 0, 0), (200, 0), (200, ALTO), 2)

        space.debug_draw(draw_options)

        estado_esfera = estado_rodadura(sphere, RADIO)
        estado_aro = estado_rodadura(ring, RADIO)

        # dibujar aro manualmente
        draw_ring(screen, ring, RADIO)

        t_s = res_s["t"] if res_s["done"] else tiempo_total
        t_r = res_r["t"] if res_r["done"] else tiempo_total

        # screen.blit(font.render(f"ESFERA  T: {t_s:.3f}s",True,(200,0,0)),(650,80))
        # screen.blit(font.render(f"ARO     T: {t_r:.3f}s",True,(0,0,200)),(650,250))
        screen.blit(font.render(f"ESFERA  T: {t_s:.3f}s", True, (200, 0, 0)), (650, 80))
        screen.blit(
            font.render(f"ESTADO: {estado_esfera}", True, (0, 120, 0)), (650, 110)
        )

        screen.blit(
            font.render(f"ARO     T: {t_r:.3f}s", True, (0, 0, 200)), (650, 250)
        )
        screen.blit(font.render(f"ESTADO: {estado_aro}", True, (0, 120, 0)), (650, 280))

        space.step(dt)

        pygame.display.flip()
        clock.tick(60)


if __name__ == "__main__":
    main()
