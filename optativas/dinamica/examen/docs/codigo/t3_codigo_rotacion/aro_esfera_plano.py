import pygame
import pymunk
import pymunk.pygame_util
import math

# --- PARÁMETROS ---
MASA = 10.0
RADIO = 30.0
DAMPING = 0.2
V_INICIAL = -500.0

ANCHO = 1000
ALTO = 600


def setup_simulation():
    space = pymunk.Space()
    space.gravity = (0, 0)
    space.damping = DAMPING

    # Suelos horizontales (Estáticos)
    suelo_disco = pymunk.Segment(space.static_body, (0, 250), (ANCHO, 250), 5)
    suelo_disco.friction = 1.0
    space.add(suelo_disco)

    suelo_aro = pymunk.Segment(space.static_body, (0, 500), (ANCHO, 500), 5)
    suelo_aro.friction = 1.0
    space.add(suelo_aro)

    # --- DISCO ---
    i_disco = 2 / 5 * MASA * (RADIO**2)  # he puesto el de una esfera, disco es 1/2
    body_d = pymunk.Body(MASA, i_disco)
    body_d.position = (ANCHO - 100, 250 - RADIO)
    body_d.velocity = (V_INICIAL, 0)
    body_d.angular_velocity = V_INICIAL / RADIO
    shape_d = pymunk.Circle(body_d, RADIO)
    shape_d.friction = 1.0
    space.add(body_d, shape_d)

    # --- ARO ---
    i_aro = MASA * (RADIO**2)
    body_r = pymunk.Body(MASA, i_aro)
    body_r.position = (ANCHO - 100, 500 - RADIO)
    body_r.velocity = (V_INICIAL, 0)
    body_r.angular_velocity = V_INICIAL / RADIO
    shape_r = pymunk.Circle(body_r, RADIO)
    shape_r.friction = 1.0
    space.add(body_r, shape_r)

    return space, body_d, body_r


def draw_ring_original_style(screen, body, radius):
    """El dibujo personalizado del aro que tenías en tu código original"""
    x, y = int(body.position.x), int(body.position.y)
    pygame.draw.circle(screen, (0, 0, 200), (x, y), radius, 4)
    pygame.draw.circle(screen, (255, 255, 255), (x, y), radius - 8)
    angle = body.angle
    end = (x + radius * math.cos(angle), y + radius * math.sin(angle))
    pygame.draw.line(screen, (0, 0, 200), (x, y), end, 3)


def main():
    pygame.init()
    screen = pygame.display.set_mode((ANCHO, ALTO))
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("Arial", 20, bold=True)

    # ESTO ES LO QUE USABAS PARA EL DIBUJO ORIGINAL
    draw_options = pymunk.pygame_util.DrawOptions(screen)

    space, disco, aro = setup_simulation()

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                return

        space.step(1 / 60.0)

        screen.fill((255, 255, 255))

        # DIBUJO ORIGINAL DE PYMUNK (Debug Draw)
        # Esto dibujará los suelos y el disco automáticamente con el estilo que viste antes
        space.debug_draw(draw_options)

        # DIBUJO MANUAL DEL ARO (Tal cual estaba en tu función draw_ring original)
        draw_ring_original_style(screen, aro, RADIO)

        # Textos informativos
        txt_disco = font.render(
            f"DISCO (1/2 MR²) V: {abs(disco.velocity.x):.1f}", True, (50, 50, 50)
        )
        txt_aro = font.render(
            f"ARO (MR²) V: {abs(aro.velocity.x):.1f}", True, (0, 0, 200)
        )
        screen.blit(txt_disco, (20, 210))
        screen.blit(txt_aro, (20, 460))

        pygame.display.flip()
        clock.tick(60)


if __name__ == "__main__":
    main()
