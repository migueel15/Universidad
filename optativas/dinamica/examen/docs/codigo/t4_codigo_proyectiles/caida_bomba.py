"""La bomba le hemos puesto el centro de presión (punto donde se aplica la
fuerza de drag, separada del centro de masas, por ese motivo cuando
empieza a caer se produce un giro. Si no ponemos un torque que lo
frene, sigue oscilando"""

import pygame
import pymunk
import pymunk.pygame_util
from rozamiento_aire import aplicar_newton
import numpy as np


###########################################################################
def crear_forma_compuesta(space, pos, R, masa):  # R debe ser en px
    # 1. Crear el cuerpo único
    # Calculamos momento de inercia aproximado (suma de las partes)
    body = pymunk.Body(masa, pymunk.moment_for_circle(masa, 0, R))
    body.position = pos
    space.add(body)

    # 2. Círculo (Centro en 0,0)
    circulo = pymunk.Circle(body, R, (0, 0))
    circulo.elasticity = 0

    # 3. Rectángulo
    # empieza en el diametro vertical del circulo y tiene 3Rx2R (lxa)
    verts = [(0, -R), (3 * R, -R), (3 * R, R), (0, R)]
    rectangulo = pymunk.Poly(body, verts)
    rectangulo.elasticity = 0

    # 4. Triángulo Equilátero (Lado 3R)
    # Lado L = 3R. Vértice en x = R.
    L = 3 * R
    h = (3**0.5 / 2) * L
    # Definimos vértices relativos (Vértice en (R, 0), base a la izquierda)
    verts_tri = [
        (2 * R, 0),  # Vértice derecho
        (2 * R + h, L / 2),  # Esquina superior base
        (2 * R + 0.85 * h, 0),
        # (2*R + h, -L/2)          # Esquina inferior base
    ]
    triangulo = pymunk.Poly(body, verts_tri)
    triangulo.elasticity = 0
    # --------------------------------
    verts_tri2 = [
        (2 * R, 0),  # Vértice derecho
        (2 * R + h, -L / 2),  # Esquina superior base
        (2 * R + 0.85 * h, 0),
        # (2*R + h, -L/2)          # Esquina inferior base
    ]
    triangulo2 = pymunk.Poly(body, verts_tri2)
    triangulo2.elasticity = 0
    # -----------------------------------------
    verts_rec2 = [
        (2 * R, -0.1 * R),
        (2 * R + h, -0.1 * R),
        (2 * R + h, 0.1 * R),
        (2 * R, 0.1 * R),
    ]
    rectangulo2 = pymunk.Poly(body, verts_rec2)
    rectangulo2.elasticity = 0

    # Añadir todas las formas al espacio
    space.add(circulo, rectangulo, triangulo, triangulo2, rectangulo2)
    return body


#####################################################################


# --- CONFIGURACIÓN Y ESCALA ---
RES = 600
# La pantalla deben ser 10 m
M_PX = 200 / RES  # metros / pixel
PX_M = 1 / M_PX  # convierte pixeles a metros
RADIO_M = 1.35
RADIO_PX = RADIO_M * PX_M
AREA_M2 = RADIO_M * (RADIO_M * 6)  # el área la pongo un poco a ojo
###################################################################


def main():
    pygame.init()
    screen = pygame.display.set_mode((RES, RES))
    clock = pygame.time.Clock()

    # --- OPCIONES DE DIBUJADO AUTOMÁTICO ---
    draw_options = pymunk.pygame_util.DrawOptions(screen)

    # --- ESPACIO FÍSICO ---
    space = pymunk.Space()
    space.gravity = (0, 9.8 * PX_M)  # Gravedad en pixeles

    # --- SUELO (Estático, Elasticidad 0) ---
    suelo_y = RES - 10
    static_body = space.static_body
    floor = pymunk.Segment(static_body, (0, suelo_y), (RES, suelo_y), 5)
    floor.elasticity = 0.0
    space.add(floor)

    # --- ESFERA (Dinámica) ---
    # masa = 1800.0 #(4000 lb)

    # Se coloca casi arriba
    body = crear_forma_compuesta(space, [RES / 2, 50], RADIO_PX, masa=200)

    body.angular_damping = 1  # Valor entre 0 y 1

    trayectoria = []
    cont = 0
    trayectoria.append(body.position)
    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        ########################################################################
        fuerza = aplicar_newton(
            body, AREA_M2, M_PX, Cd=1.05, offset=(3 * RADIO_M, 0), v_viento=(0, 0)
        )
        # ------------- Simula la acción de las aletas --------------------
        factor_estabilidad = 0.8
        body.torque -= body.angular_velocity * body.moment * factor_estabilidad
        # ---------------------------------------------------------------
        ########################################################################

        # Actualización (60 steps por segundo)
        dt = 1.0 / 60.0
        space.step(dt)

        cont += 1
        if cont & 5 == 0:
            trayectoria.append(body.position)

        # Borra pantalla
        screen.fill((255, 255, 255))
        # DIBUJADO AUTOMÁTICO: Dibuja todas las formas en el espacio
        space.debug_draw(draw_options)

        if len(trayectoria) > 2:
            pygame.draw.lines(screen, (200, 200, 200), False, trayectoria, 1)

        # Dibujar suelo
        # pygame.draw.line(screen, (0, 0, 0), (0, suelo_y), (RES, suelo_y), 5)

        # Dibujar esfera
        # pos = body.position
        # pygame.draw.circle(screen, (200, 0, 0), (int(pos.x), int(pos.y)), int(RADIO_PX))

        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    main()
