import pygame
import math
import os

# =====================================================
# CONFIG
# =====================================================

WIDTH, HEIGHT = 1200, 600
FPS = 60

pygame.init()

screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Ferrari F430 - Tema 7")

clock = pygame.time.Clock()

font = pygame.font.SysFont("consolas", 18)
big_font = pygame.font.SysFont("consolas", 28)

# =====================================================
# CARGAR IMAGENES
# =====================================================

BASE_PATH = os.path.dirname(__file__)

car_img = pygame.image.load(
    os.path.join(BASE_PATH, "F430V8.png")
).convert_alpha()

wheel_img = pygame.image.load(
    os.path.join(BASE_PATH, "F430V8_rueda.png")
).convert_alpha()

# tamaño coche
car_img = pygame.transform.scale(car_img, (900, 300))

# tamaño ruedas
wheel_size = 150

wheel_img = pygame.transform.scale(
    wheel_img,
    (wheel_size, wheel_size)
)

# =====================================================
# POSICIONES
# =====================================================

car_x = 140
car_y = 170

front_wheel = (295, 395)
rear_wheel  = (850, 395)

ROAD_Y = 470

# =====================================================
# DATOS FISICOS
# =====================================================

masa = 1450              # kg
radio_rueda = 0.34       # m

diferencial = 4.30
eficiencia = 0.88

marchas = {
    1: 3.29,
    2: 2.16,
    3: 1.61,
    4: 1.27,
    5: 1.03,
    6: 0.82
}

marcha_actual = 1

rpm = 1000
rpm_max = 8200

velocidad = 0            # m/s
aceleracion = 0

# =====================================================
# VARIABLES VISUALES
# =====================================================

wheel_angle = 0

# =====================================================
# FUNCIONES
# =====================================================

def draw_text(text, pos, color=(255,255,255), big=False):

    if big:
        img = big_font.render(text, True, color)
    else:
        img = font.render(text, True, color)

    screen.blit(img, pos)


def draw_rotated_wheel(center, angle):

    rotated = pygame.transform.rotozoom(
        wheel_img,
        -math.degrees(angle),
        1
    )

    rect = rotated.get_rect(center=center)

    screen.blit(rotated, rect)


# =====================================================
# PAR MOTOR SIMPLE
# =====================================================

def par_motor(rpm):

    if rpm < 3000:
        return 320

    elif rpm < 5000:
        return 420

    elif rpm < 7000:
        return 460

    else:
        return 430


# =====================================================
# MAIN LOOP
# =====================================================

running = True

while running:

    dt = 1 / FPS

    # =================================================
    # EVENTOS
    # =================================================

    for event in pygame.event.get():

        if event.type == pygame.QUIT:
            running = False

    keys = pygame.key.get_pressed()

    # =================================================
    # FUERZAS Y MOVIMIENTO
    # =================================================

    Tm = 0
    Ft = 0
    F_drag = 0
    F_rodadura = 0
    F_freno = 0
    F_neta = 0

    # -------------------------------------------------
    # RPM DEL MOTOR
    # rpm_motor = omega_rueda * relacion * diferencial
    # -------------------------------------------------

    omega_rueda = velocidad / radio_rueda

    rpm = (
        omega_rueda
        * marchas[marcha_actual]
        * diferencial
        * 60
        / (2 * math.pi)
    )

    rpm = max(1000, rpm)

    # -------------------------------------------------
    # PAR MOTOR
    # -------------------------------------------------

    Tm = par_motor(rpm)

    # -------------------------------------------------
    # PAR EN RUEDA
    # par_rueda = par_motor * relacion * diferencial
    # -------------------------------------------------

    relacion = marchas[marcha_actual]

    par_rueda = (
        Tm
        * relacion
        * diferencial
        * eficiencia
    )

    # -------------------------------------------------
    # FUERZA TRACCION
    # fuerza_traccion = par_rueda / radio_rueda
    # -------------------------------------------------

    Ft = par_rueda / radio_rueda

    # -------------------------------------------------
    # RESISTENCIA AERODINAMICA
    # -------------------------------------------------

    rho = 1.225
    Cd = 0.20
    A = 2.03

    F_drag = (
        0.5
        * rho
        * Cd
        * A
        * velocidad**2
    )

    # -------------------------------------------------
    # RESISTENCIA RODADURA
    # -------------------------------------------------

    Crr = 0.008
    g = 9.81

    F_rodadura = Crr * masa * g

    # -------------------------------------------------
    # FRENADO
    # -------------------------------------------------

    if keys[pygame.K_DOWN]:
        F_freno = 6000

    # -------------------------------------------------
    # FUERZA NETA
    # F_neta = Ft - F_drag - F_rodadura - F_freno
    # -------------------------------------------------

    if keys[pygame.K_UP]:

        F_neta = (
            Ft
            - F_drag
            - F_rodadura
            - F_freno
        )

    else:

        F_neta = (
            -F_drag
            -F_rodadura
            -F_freno
        )

    # -------------------------------------------------
    # NEWTON
    # a = F_neta / masa
    # -------------------------------------------------

    aceleracion = F_neta / masa

    velocidad += aceleracion * dt

    # evitar velocidades negativas
    if velocidad < 0:
        velocidad = 0

    # =================================================
    # RPM DESDE VELOCIDAD
    # =================================================

    omega_rueda = velocidad / radio_rueda

    rpm = (
        omega_rueda
        * marchas[marcha_actual]
        * diferencial
        * 60
        / (2 * math.pi)
    )

    rpm = max(1000, rpm)

    # =================================================
    # CAMBIO AUTOMATICO
    # =================================================

        # subir marcha
    if rpm > rpm_max and marcha_actual < 6:

        marcha_actual += 1

        velocidad *= 0.98

    # bajar marcha
    if rpm < 2500 and marcha_actual > 1:

        marcha_actual -= 1

    # =================================================
    # GIRAR RUEDAS
    # =================================================

    wheel_angle += omega_rueda * dt * 2

    # =================================================
    # DIBUJO
    # =================================================

    # cielo azul claro
    screen.fill((135, 206, 235))

    # carretera
    pygame.draw.rect(
        screen,
        (70,70,70),
        (0,ROAD_Y,WIDTH,HEIGHT-ROAD_Y)
    )

    # lineas carretera
    for x in range(0, WIDTH, 140):

        pygame.draw.rect(
            screen,
            (240,240,240),
            (x, ROAD_Y + 55, 70, 8)
        )

    # coche
    screen.blit(car_img, (car_x, car_y))

    # ruedas
    draw_rotated_wheel(front_wheel, wheel_angle)
    draw_rotated_wheel(rear_wheel, wheel_angle)

    # =================================================
    # HUD
    # =================================================

    hud_x = 20
    hud_y = 20
    espacio = 28

    draw_text(
        f"{velocidad*3.6:.1f} km/h",
        (hud_x, hud_y),
        (0,0,0),
        big=True
    )

    draw_text(
        f"RPM: {rpm:.0f}",
        (hud_x, hud_y + espacio*2),
        (0,0,0)
    )

    draw_text(
        f"Marcha: {marcha_actual}",
        (hud_x, hud_y + espacio*3),
        (0,0,0)
    )

    draw_text(
        f"ω rueda: {omega_rueda:.2f}",
        (hud_x, hud_y + espacio*4),
        (0,0,0)
    )

    draw_text(
        f"a = {aceleracion:.2f} m/s²",
        (hud_x, hud_y + espacio*5),
        (0,0,0)
    )

    draw_text(
        f"F traccion = {Ft:.0f} N",
        (hud_x, hud_y + espacio*6),
        (0,0,0)
    )

    draw_text(
        f"F drag = {F_drag:.0f} N",
        (hud_x, hud_y + espacio*7),
        (0,0,0)
    )

    draw_text(
        f"F freno = {F_freno:.0f} N",
        (hud_x, hud_y + espacio*8),
        (0,0,0)
    )

    draw_text(
        "↑ acelerar",
        (hud_x, hud_y + espacio*10),
        (20,20,20)
    )

    draw_text(
        "↓ frenar",
        (hud_x, hud_y + espacio*11),
        (20,20,20)
    )

    pygame.display.flip()

    clock.tick(FPS)

pygame.quit()