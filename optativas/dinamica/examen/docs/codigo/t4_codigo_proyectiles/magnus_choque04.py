import pygame
import pymunk
from pymunk import Vec2d
import pymunk.pygame_util
import numpy as np
import math
import sys
from rozamiento_aire import aplicar_newton, aplicar_magnus

# Esto es para poner el gol en pantalla ----------------------------
from functools import lru_cache
import os


@lru_cache(maxsize=1)
def obtener_fuente_gol():
    return pygame.font.SysFont("dejavu sans mono", 100, bold=True)


# -------------------------------------------------------------------

# --- CONFIGURACIÓN ---
WIDTH, HEIGHT = 1200, 800
FPS = 60
SUBSTEPS = 20
dt = 1.0 / (FPS * SUBSTEPS)
PX_M = 13.5
M_PX = 1 / PX_M

slowmotion = 1

# Variables Globales de Estado
zoom_activo = False
factor_zoom = 3.0
empezado_global = False
global V0_balon, W0_balon


# Pixeles de la esquina inferior izquierda del medio campo (Banda Izquierda / Medio campo)
OFF_X = WIDTH - (68 * PX_M * 1.02)
OFF_Y = HEIGHT - 10


def m_a_px(x_m, y_m):
    """
    Convierte metros a píxeles.
    Origen (0,0)m: Centro de la línea de gol.
    x_m: De -34 a 34.
    y_m: De 0 (gol) a 52.5 (medio campo).
    """
    # El centro de la línea de gol está a 34m a la derecha de OFF_X
    # y a 52.5m hacia arriba de OFF_Y
    orig_x = OFF_X + 34 * PX_M
    orig_y = OFF_Y - 52.5 * PX_M

    px_x = orig_x + (x_m * PX_M)
    px_y = orig_y + (y_m * PX_M)

    return Vec2d(px_x, px_y)


##################  DIBUJAR CAMPO ###########################
def dibujar_campo(surface):
    # Colores
    VERDE_CESPED = (34, 139, 34)
    BLANCO = (255, 255, 255)
    surface.fill(VERDE_CESPED)

    # 1. Líneas exteriores del medio campo
    p1 = m_a_px(-34, 0)  # Córner fondo izq
    p2 = m_a_px(34, 0)  # Córner fondo der
    p3 = m_a_px(34, 52.5)  # Córner medio campo der
    p4 = m_a_px(-34, 52.5)  # Córner medio campo izq
    pygame.draw.lines(surface, BLANCO, True, [p1, p2, p3, p4], 2)

    # 2. Semicírculo central (en la línea de abajo del dibujo, y=52.5)
    c_central = m_a_px(0, 52.5)
    r_central = 9.15 * PX_M
    arco_central_rect = pygame.Rect(
        c_central.x - r_central, c_central.y - r_central, r_central * 2, r_central * 2
    )
    pygame.draw.arc(surface, BLANCO, arco_central_rect, 0, math.pi, 2)

    # 3. Área Grande (40.32m ancho, 16.5m alto)
    # Esquina superior izquierda del área en metros: x = -20.16, y = 0
    top_left_ag = m_a_px(-20.16, 0)
    pygame.draw.rect(
        surface, BLANCO, (top_left_ag.x, top_left_ag.y, 40.32 * PX_M, 16.5 * PX_M), 2
    )

    # 4. Área Pequeña (18.32m ancho, 5.5m alto)
    top_left_ap = m_a_px(-9.16, 0)
    pygame.draw.rect(
        surface, BLANCO, (top_left_ap.x, top_left_ap.y, 18.32 * PX_M, 5.5 * PX_M), 2
    )

    # 5. Punto de Penalti (x=0, y=11)
    p_penalti = m_a_px(0, 11)
    pygame.draw.circle(surface, BLANCO, (int(p_penalti.x), int(p_penalti.y)), 3)

    # 6. Arco del área
    radio_arco = 9.15 * PX_M
    arco_rect = pygame.Rect(
        p_penalti.x - radio_arco,
        p_penalti.y - radio_arco,
        radio_arco * 2,
        radio_arco * 2,
    )
    # Usamos los ángulos de la geometría real que ya funcionaban
    pygame.draw.arc(surface, BLANCO, arco_rect, 1.22 * math.pi, 1.78 * math.pi, 2)

    # 7. Portería (sobresale por arriba, y < 0)
    # Esquina superior izquierda de la portería: x = -3.66, y = -2
    top_left_port = m_a_px(-3.66, -2)
    pygame.draw.rect(
        surface, BLANCO, (top_left_port.x, top_left_port.y, 7.32 * PX_M, 2 * PX_M), 2
    )


# ------------------------------------------------------------


############### DIBUJAR BALON ################################
def dibujar_balon_visual(surface, body, escala=5):
    radio_visual = list(body.shapes)[0].radius * escala
    pos = body.position
    pygame.draw.circle(
        surface, (255, 255, 255), (int(pos.x), int(pos.y)), int(radio_visual)
    )
    pygame.draw.circle(
        surface, (0, 0, 0), (int(pos.x), int(pos.y)), int(radio_visual), 2
    )
    punto_giro = pos + Vec2d(radio_visual, 0).rotated(body.angle)
    pygame.draw.line(
        surface, (0, 0, 0), (pos.x, pos.y), (punto_giro.x, punto_giro.y), 1
    )


# -------------------------------------------------------------


##################  DIBUJAR CONTENIDO ########################
def dibujar_todo_el_contenido(surface):
    global V0_balon, y0, x0

    dibujar_campo(surface)
    opts = pymunk.pygame_util.DrawOptions(surface)
    space.debug_draw(opts)
    dibujar_balon_visual(surface, balon)

    # ESTA CONDICIÓN ES LA CLAVE:
    # Solo se dibuja si NO ha empezado el tiro Y el balón está quieto.
    # if not empezado_global and balon.velocity.length < 0.1:
    if 1:
        # --- LÍNEA BLANCA (Solo guía de ajuste) ---
        vuni = V0_balon.normalized()
        p_inicio = m_a_px(x0, y0)  # posicion inicial del balon  #balon.position
        p_fin = p_inicio + 450 * vuni
        pygame.draw.line(surface, (200, 200, 200), p_inicio, p_fin, 1)


# -------------------------------------------------------------


################# APLICAR CAMARA #############################
def aplicar_camara():
    global V0_balon, W0_balon
    if not zoom_activo:
        screen.blit(canvas, (0, 0))
    else:
        w_zoom, h_zoom = WIDTH / factor_zoom, HEIGHT / factor_zoom
        x_crop = max(0, min(balon.position.x - w_zoom / 2, WIDTH - w_zoom))
        y_crop = max(0, min(balon.position.y - h_zoom / 2, HEIGHT - h_zoom))
        sub = canvas.subsurface(pygame.Rect(x_crop, y_crop, w_zoom, h_zoom))
        screen.blit(pygame.transform.smoothscale(sub, (WIDTH, HEIGHT)), (0, 0))

    escribe_info(screen, V0_balon, W0_balon)
    pygame.display.flip()


# -----------------------------------------------------------------


# --------------------- escribe info -------------------------------
def escribe_info(surface, velocidad, velocidad_angular):

    direccion = np.atan2(velocidad[1], velocidad[0]) * 180 / np.pi
    color = (255, 255, 255)
    txt_vel = fuente.render(f"Velocidad: {velocidad.length:.2f} km/h", True, color)
    txt_ang_int = fuente.render(
        f"Vel. angular: {velocidad_angular:.2f} rad/s", True, color
    )
    txt_ang_ext = fuente.render(f"Ang. direc: {direccion:.2f} deg", True, color)
    surface.blit(txt_vel, (20, 20))
    surface.blit(txt_ang_int, (20, 45))
    surface.blit(txt_ang_ext, (20, 70))


# ------------------------------------------------------------------


# --- FUNCIONES DE CREACIÓN ---
def esfera(space, masa, radio_px, pos=(0, 0)):
    body = pymunk.Body(masa, pymunk.moment_for_circle(masa, 0, radio_px))
    body.position = pos
    shape = pymunk.Circle(body, radio_px)
    shape.friction = 0.3
    shape.color = (255, 255, 255, 255)
    space.add(body, shape)
    return body


def crear_barrera_real(space, num_jugadores, pos_balon_m):
    # 1. Postes de la portería (y=0 en tu sistema m_a_px)
    postes = [-3.66, 3.66]
    # Elegimos el poste más cercano al balón en el eje X
    poste_objetivo_x = postes[0] if pos_balon_m[0] < 0 else postes[1]

    # 2. Vector desde el balón al poste
    balon_v = Vec2d(pos_balon_m[0], pos_balon_m[1])
    poste_v = Vec2d(poste_objetivo_x, 0)
    direccion_tiro = (poste_v - balon_v).normalized()

    # 3. Punto en la línea a 9.15m del balón
    # OJO: Restamos porque el eje Y crece hacia el campo, y queremos ir hacia la portería
    pos_inicio_barrera = balon_v + direccion_tiro * 9.15

    # 4. Vector perpendicular para extender la barrera hacia el "interior" de la portería
    # Si estamos en la izquierda, extendemos hacia la derecha y viceversa
    lado = 1 if poste_objetivo_x < 0 else -1
    perpendicular = Vec2d(-direccion_tiro.y, direccion_tiro.x) * lado

    jugadores = []
    ancho_tipo = 0.5

    for i in range(num_jugadores):
        # Posición en metros: inicio + desplazamiento lateral
        pos_m = pos_inicio_barrera + perpendicular * (i * (ancho_tipo + 0.1))

        body = pymunk.Body(body_type=pymunk.Body.STATIC)
        body.position = m_a_px(pos_m.x, pos_m.y)

        # El jugador debe mirar hacia el balón (rotamos el cuerpo)
        angulo_hacia_balon = (balon_v - pos_m).angle
        body.angle = angulo_hacia_balon

        shape = pymunk.Poly.create_box(body, (0.5 * PX_M, 0.2 * PX_M))  # 50cm x 20cm
        shape.color = (255, 255, 0, 255)
        shape.friction = 0.5
        space.add(body, shape)
        jugadores.append(body)

    return jugadores


# ----------------------------------------------------
# --- AÑADIR POSTES ---
# Definimos las posiciones de los dos postes en metros
def crear_postes(space):
    radio_poste_m = 0.1
    radio_poste_px = radio_poste_m * PX_M
    posiciones_postes = [-3.66, 3.66]

    postes = []
    for x_poste in posiciones_postes:
        # Creamos un cuerpo estático individual para cada poste
        # o usamos el cuerpo estático global del espacio
        poste_shape = pymunk.Circle(
            space.static_body, radio_poste_px, offset=m_a_px(x_poste, 0)
        )

        # Asignamos la posición convertida a píxeles

        # Propiedades físicas
        poste_shape.elasticity = 0.8
        poste_shape.friction = 0.5
        poste_shape.color = (255, 255, 255, 255)

        # Añadimos la forma al espacio
        space.add(poste_shape)
        postes.append(poste_shape)
    return postes


# ----------------------------------1


def reset_simulacion(x, y):  # x,y en metros
    global empezado_global, zoom_activo, V0_balon, W0_balon
    global FUERA

    FUERA = False

    # 1. Volver al estado de ajuste
    empezado_global = False
    zoom_activo = False

    # 2. Posicionar el balón en el punto de penalti
    balon.position = m_a_px(x, y)
    balon.velocity = (0, 0)
    balon.angular_velocity = 0


# -----------------------------------------------
def detecta_gol(body):
    """
    Comprueba la posición del balón en metros.
    Devuelve True si el balón ha entrado en la portería.
    """
    global FUERA

    orig_x = OFF_X + 34 * PX_M
    orig_y = OFF_Y - 52.5 * PX_M

    pos_m_x = (body.position.x - orig_x) * M_PX
    pos_m_y = (body.position.y - orig_y) * M_PX

    # Comprobación: entre postes (7.32m de ancho) y tras la línea (y < 0)
    if (pos_m_x < -3.66 or pos_m_x > 3.66) and pos_m_y < 0:
        FUERA = True
        return False
    elif (-3.66 < pos_m_x < 3.66) and (pos_m_y < 0) and not (FUERA):
        return True
    return False


########################  BUCLE DE AJUSTE  #########################
def esperar_ajuste_inicial():
    global zoom_activo
    radio = list(balon.shapes)[0].radius
    Da = 5  # Incremento por defecto
    global V0_balon, W0_balon
    angulo_balon = 180.0 / np.pi * np.atan2(V0_balon[1], V0_balon[0])
    V0 = V0_balon.length

    while True:
        # Detectar modificadores (Control)
        mods = pygame.key.get_mods()
        ctrl_presionado = mods & pygame.KMOD_CTRL

        # Dibujo
        dibujar_todo_el_contenido(canvas)
        aplicar_camara()

        # 4. Gestión de eventos
        for event in pygame.event.get():

            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_SPACE:
                    V0_balon = V0 * Vec2d(
                        np.cos(np.radians(angulo_balon)),
                        np.sin(np.radians(angulo_balon)),
                    )
                    return V0_balon, W0_balon  # Salir al bucle de simulación

                if event.key == pygame.K_z:
                    zoom_activo = not zoom_activo

                elif event.key == pygame.K_RIGHT:
                    angulo_balon += Da  # Modifica dirección
                    V0_balon = V0 * Vec2d(
                        np.cos(np.radians(angulo_balon)),
                        np.sin(np.radians(angulo_balon)),
                    )

                elif event.key == pygame.K_LEFT:
                    angulo_balon -= Da
                    V0_balon = V0 * Vec2d(
                        np.cos(np.radians(angulo_balon)),
                        np.sin(np.radians(angulo_balon)),
                    )

                elif event.key == pygame.K_UP:
                    if ctrl_presionado:
                        W0_balon += Da  # velocidad angular
                    else:
                        V0 += Da  # velocidad
                        V0_balon = V0_balon.normalized() * V0
                elif event.key == pygame.K_DOWN:
                    if ctrl_presionado:
                        W0_balon -= Da
                    else:
                        V0 -= Da
                        V0_balon = V0_balon.normalized() * V0
                elif event.key == pygame.K_KP_PLUS:
                    Da = 5
                elif event.key == pygame.K_KP_MINUS:
                    Da = 1


# -------------------------------------------------------------------


#####################################################################
#########     MAIN   ################################################
def main():
    global screen, canvas, space, balon, bota, fuente, empezado_global, zoom_activo
    global V0_balon, W0_balon, x0, y0
    global FUERA

    FUERA = False

    pygame.init()
    pygame.mixer.init()
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    canvas = pygame.Surface((WIDTH, HEIGHT))
    fuente = pygame.font.SysFont("dejavu sans mono", 16)
    clock = pygame.time.Clock()
    space = pymunk.Space()
    space.damping = 1.0

    # posición inicial del balón
    x0 = 8  # en m
    y0 = 22
    radio_m = 0.11
    radio_px = radio_m * PX_M
    area_m2 = np.pi * radio_m**2
    balon = esfera(space, 0.45, radio_px, pos=m_a_px(x0, y0))

    # colocamos la barrera
    pos_balon_m = (x0, y0)  # Por ejemplo, una falta a 25 metros
    balon.position = m_a_px(pos_balon_m[0], pos_balon_m[1])
    barrera = crear_barrera_real(space, 5, pos_balon_m)
    postes = crear_postes(space)

    # ajuste inicial. Modifica las variables globales V0_balon y W0_balon
    V0_balon = Vec2d(0, -80)  # se pasa en km/h
    W0_balon = 0
    esperar_ajuste_inicial()
    empezado_global = True
    balon.velocity = (
        V0_balon / 3.6 * PX_M
    )  # cuando se le asigna al cuerpo se pone en px/s
    balon.angular_velocity = W0_balon

    tiempo = 0
    while True:
        # ------------- FISICA ----------------------------------
        for _ in range(SUBSTEPS):
            tiempo += dt
            fnewton = aplicar_newton(balon, area_m2, M_PX)
            fmagnus = aplicar_magnus(balon, area_m2, M_PX)
            if not (fnewton == None):
                print(
                    f"{balon.velocity.length*M_PX*3.6:.2f}    {fnewton.length*M_PX:.2f}   {fmagnus.length*M_PX:.2f}  {balon.angular_velocity/(2*np.pi):.2f}"
                )
            # 			print(f'{balon.angular_velocity:.2f} {balon.velocity[0]:.2f} {balon.velocity[1]:.2f}')

            # -------------------------------------------------------
            space.step(dt * slowmotion)

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_z:
                    zoom_activo = not zoom_activo
                if event.key == pygame.K_ESCAPE:
                    reset_simulacion(x0, y0)

                    esperar_ajuste_inicial()  # Reentramos al bucle de ajuste
                    balon.velocity = (
                        V0_balon / 3.6 * PX_M
                    )  # cuando se le asigna al cuerpo se pone en px/s
                    balon.angular_velocity = W0_balon

        dibujar_todo_el_contenido(canvas)

        # Comprobamos el gol
        if detecta_gol(balon):
            # Dibujamos el texto encima de todo lo anterior en el canvas
            fuente_g = pygame.font.SysFont("dejavu sans mono", 100, bold=True)
            txt = fuente_g.render("¡¡¡GOOOOOL!!!", True, (255, 255, 0))
            rect = txt.get_rect(center=(WIDTH // 2, HEIGHT // 2))
            canvas.blit(txt, rect)
            # 3. Actualizamos la pantalla para que el usuario vea el texto
            aplicar_camara()
            sonido = "gol.mp3"
            try:  # Reproducir el archivo (esto varía según el sistema operativo)
                if os.name == "nt":  # Windows
                    os.system(f"start {sonido}")
                else:  # macOS o Linux
                    os.system(f"mpg123 {sonido}")
            except:
                print("No se pudo cargar el sonido {sonido}")

            # 4. Pausa y Reset
            pygame.time.delay(3000)
            reset_simulacion(x0, y0)
            esperar_ajuste_inicial()
            balon.velocity = V0_balon / 3.6 * PX_M
            balon.angular_velocity = W0_balon
        else:
            # Si no hay gol, simplemente actualizamos la cámara normal
            aplicar_camara()

        clock.tick(FPS)

        aplicar_camara()

        clock.tick(FPS)


if __name__ == "__main__":
    main()
