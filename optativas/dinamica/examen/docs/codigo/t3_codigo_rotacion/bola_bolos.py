"""Una bola de bolos empieza deslizando hasta que, por rozamiento, su
velocidad disminuye y alcanza la rodadura pura.
Se puede cambiar el rozamiento y la velocidad inicial para ver cómo afecta"""

import pymunk
import pymunk.pygame_util
import pygame
import math

# --- PARÁMETROS EDITABLES ---
MASA = 10.0
RADIO = 45.0
LADO = 2 * RADIO
FRICCION_SUELO = 0.1
FRICCION_BOLA = 0.5
FRICCION_CAJA = FRICCION_BOLA

muk = FRICCION_SUELO * FRICCION_BOLA  # coef. rozamiento

VELOCIDAD_MAGNITUD = 600.0  # Rapidez inicial a lo largo del plano
GRAVEDAD = 981.0
ANGULO_GRADOS = 0.0  # Inclinación de la rampa

# --- CONFIGURACIÓN DE PANTALLA ---
ANCHO = 1500
ALTO = 400


def setup_simulation(objeto):
    space = pymunk.Space()
    space.gravity = (0, GRAVEDAD)

    # --- EL PLANO INCLINADO ---
    static_body = space.static_body
    angulo_rad = math.radians(ANGULO_GRADOS)

    # Puntos de la rampa (Descendente de derecha a izquierda)
    p1 = (ANCHO, ALTO - 50 - ALTO * math.sin(angulo_rad))  # Punto alto
    p2 = (0, ALTO - 50)  # Punto bajo

    floor = pymunk.Segment(static_body, p1, p2, 5)
    floor.friction = FRICCION_SUELO
    space.add(floor)

    # --- CÁLCULO DE POSICIÓN Y VELOCIDAD INICIAL ---
    # Vector dirección del plano (de p1 a p2)
    dx = p2[0] - p1[0]
    dy = p2[1] - p1[1]
    dist = math.sqrt(dx**2 + dy**2)
    ux, uy = dx / dist, dy / dist  # Vector unitario dirección plano

    # Vector normal al plano (hacia arriba/izquierda)
    nx, ny = -uy, ux

    # Posición: Punto de inicio p1 + desplazar hacia abajo el radio en la normal
    # y un poco hacia el centro para que no empiece en el borde exacto
    pos_x = p1[0] + ux * 100 + nx * RADIO
    pos_y = p1[1] + uy * 100 + ny * RADIO

    if objeto == "bola":
        # --- LA BOLA ---
        # %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        moment = (2 / 5) * MASA * (RADIO**2)
        bola = pymunk.Body(MASA, moment)
        bola.position = (pos_x, pos_y)

        # Velocidad inicial puramente en la dirección del plano
        bola.velocity = (ux * VELOCIDAD_MAGNITUD, uy * VELOCIDAD_MAGNITUD)

        # Ángulo inicial alineado con la rampa
        bola.angle = math.atan2(dy, dx)

        shape_bola = pymunk.Circle(bola, RADIO)
        shape_bola.friction = FRICCION_BOLA

        space.add(bola, shape_bola)
        return space, bola
        #####################################################
    elif objeto == "caja":
        # --- LA CAJA ---
        # 1. Nuevo Momento de Inercia para un cuadrado
        # I = (1/6) * m * Lado^2
        moment = (1 / 6) * MASA * (LADO**2)

        caja = pymunk.Body(MASA, moment)
        caja.position = (pos_x, pos_y)
        caja.velocity = (ux * VELOCIDAD_MAGNITUD, uy * VELOCIDAD_MAGNITUD)

        # Ángulo inicial alineado con la rampa
        caja.angle = math.atan2(dy, dx)

        # 2. Cambiamos Circle por Poly (Caja)
        shape_caja = pymunk.Poly.create_box(caja, (LADO, LADO))
        shape_caja.friction = FRICCION_CAJA

        space.add(caja, shape_caja)
        return space, caja


def main():
    pygame.init()
    screen = pygame.display.set_mode((ANCHO, ALTO))
    pygame.display.set_caption("Bola de bolos")
    clock = pygame.time.Clock()
    draw_options = pymunk.pygame_util.DrawOptions(screen)

    space, body = setup_simulation("bola")

    tiempo = 0
    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        screen.fill((240, 240, 240))
        space.debug_draw(draw_options)

        # if (body.position.x>100):
        # 	tiempo=tiempo+1/60.0
        # print(tiempo)

        # Paso de física
        space.step(1 / 60.0)

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    main()
