"""
SIMULACIÓN FERRARI F430 V8 - PAR, MARCHAS, RPM Y MOVIMIENTO

Controles:
    Flecha ARRIBA  -> acelerar
    Flecha ABAJO   -> frenar
    A              -> cambiar modo automatico/manual
    Q / E          -> bajar/subir marcha en modo manual
    R              -> reiniciar
    ESC            -> salir

Física:
    rpm_motor = omega_rueda * relacion_marcha * diferencial * 60/(2*pi)
    par_rueda = par_motor * relacion_marcha * diferencial * eficiencia
    fuerza_traccion = par_rueda / radio_rueda
    F_neta = F_traccion - F_drag - F_rodadura - F_freno
    a = F_neta / masa

"""

import math
import os
import sys

import pygame

# ---------------------------------------------------------------------
# DATOS DE TRANSMISIÓN F430
# ---------------------------------------------------------------------
RELACIONES = {
    1: 3.29,
    2: 2.16,
    3: 1.61,
    4: 1.26,
    5: 1.03,
    6: 0.85,
}
FINAL_DRIVE = 4.30
RADIO_RUEDA_M = 0.38

# ---------------------------------------------------------------------
# PAR MOTOR DEL F430 V8
# ---------------------------------------------------------------------
RPM_DATOS = [1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000,
             5250, 5500, 6000, 6500, 7000, 7500, 8000, 8500]
PAR_DATOS = [320, 345, 370, 400, 415, 430, 445, 455, 465,
             465, 460, 455, 450, 440, 430, 415, 390]

RPM_IDLE = 1000
RPM_LIMIT = 8500
RPM_CAMBIO_SUBIR = 8300
RPM_CAMBIO_BAJAR = 2500
EFICIENCIA_TRANSMISION = 0.88

# ---------------------------------------------------------------------
# DATOS FÍSICOS DEL COCHE
# ---------------------------------------------------------------------
MASA_COCHE = 1450.0      # kg aprox.
CD = 0.34                # coeficiente aerodinámico aproximado
AREA_FRONTAL = 1.95      # m² aproximado
RHO_AIRE = 1.225         # kg/m³
CRR = 0.015              # resistencia a rodadura neumático/asfalto
G = 9.81
MU_AGARRE = 1.15         # límite de tracción simple
FUERZA_FRENO_MAX = 13000 # N

# ---------------------------------------------------------------------
# PYGAME
# ---------------------------------------------------------------------
WIDTH, HEIGHT = 1280, 720
FPS = 60
SCALE_COCHE = 0.55
SCALE_RUEDA = 0.60
ROAD_Y = 560

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
IMG_COCHE = os.path.join(BASE_DIR, "F430V8.png")
IMG_RUEDA = os.path.join(BASE_DIR, "F430V8_rueda.png")


def clamp(x, a, b):
    return max(a, min(b, x))


def interp_lineal(x, xs, ys):
    """Interpolación lineal sencilla, sin scipy."""
    if x <= xs[0]:
        return ys[0]
    if x >= xs[-1]:
        return ys[-1]

    for i in range(len(xs) - 1):
        if xs[i] <= x <= xs[i + 1]:
            t = (x - xs[i]) / (xs[i + 1] - xs[i])
            return ys[i] + t * (ys[i + 1] - ys[i])
    return ys[-1]


def par_y_potencia(rpm):
    """
    Devuelve:
        par motor en Nm
        potencia en CV
    """
    rpm = clamp(rpm, RPM_DATOS[0], RPM_DATOS[-1])
    par = interp_lineal(rpm, RPM_DATOS, PAR_DATOS)
    potencia_cv = (par * rpm * 2 * math.pi) / (60 * 735.5)
    return par, potencia_cv


class F430:
    def __init__(self):
        self.x_m = 0.0
        self.v_m_s = 0.0
        self.a_m_s2 = 0.0
        self.marcha = 1
        self.auto = True
        self.rpm = RPM_IDLE
        self.throttle = 0.0
        self.brake = 0.0
        self.par_motor = 0.0
        self.potencia_cv = 0.0
        self.par_rueda = 0.0
        self.f_traccion = 0.0
        self.f_drag = 0.0
        self.f_rodadura = 0.0
        self.f_freno = 0.0
        self.estado_ruedas = "agarre"
        self.angulo_rueda = 0.0

    def reset(self):
        self.__init__()

    def calcular_rpm_por_velocidad(self):
        omega_rueda = self.v_m_s / RADIO_RUEDA_M
        rpm = omega_rueda * RELACIONES[self.marcha] * FINAL_DRIVE * 60 / (2 * math.pi)
        return max(RPM_IDLE, rpm)

    def cambio_automatico(self):
        if not self.auto:
            return

        if self.rpm > RPM_CAMBIO_SUBIR and self.marcha < 6:
            self.marcha += 1
        elif self.rpm < RPM_CAMBIO_BAJAR and self.marcha > 1:
            # Solo baja si no va demasiado rápido para la marcha inferior
            marcha_inferior = self.marcha - 1
            omega_rueda = self.v_m_s / RADIO_RUEDA_M
            rpm_inferior = omega_rueda * RELACIONES[marcha_inferior] * FINAL_DRIVE * 60 / (2 * math.pi)
            if rpm_inferior < RPM_LIMIT:
                self.marcha -= 1

    def subir_marcha(self):
        if not self.auto and self.marcha < 6:
            self.marcha += 1

    def bajar_marcha(self):
        if not self.auto and self.marcha > 1:
            marcha_inferior = self.marcha - 1
            omega_rueda = self.v_m_s / RADIO_RUEDA_M
            rpm_inferior = omega_rueda * RELACIONES[marcha_inferior] * FINAL_DRIVE * 60 / (2 * math.pi)
            if rpm_inferior < RPM_LIMIT:
                self.marcha -= 1

    def update(self, dt):
        self.rpm = self.calcular_rpm_por_velocidad()
        self.cambio_automatico()
        self.rpm = self.calcular_rpm_por_velocidad()

        # Si toca limitador, se corta el par.
        limitador = self.rpm >= RPM_LIMIT
        self.par_motor, self.potencia_cv = par_y_potencia(self.rpm)
        par_usable = self.par_motor * self.throttle
        if limitador:
            par_usable *= 0.15

        relacion_total = RELACIONES[self.marcha] * FINAL_DRIVE
        self.par_rueda = par_usable * relacion_total * EFICIENCIA_TRANSMISION
        f_motor = self.par_rueda / RADIO_RUEDA_M

        # Límite de agarre: el neumático no puede transmitir fuerza infinita.
        f_max_agarre = MU_AGARRE * MASA_COCHE * G
        if f_motor > f_max_agarre:
            self.f_traccion = f_max_agarre
            self.estado_ruedas = "patinando"
        else:
            self.f_traccion = f_motor
            self.estado_ruedas = "agarre"

        self.f_drag = 0.5 * RHO_AIRE * CD * AREA_FRONTAL * self.v_m_s**2
        self.f_rodadura = CRR * MASA_COCHE * G if self.v_m_s > 0.05 else 0.0
        self.f_freno = self.brake * FUERZA_FRENO_MAX

        f_neta = self.f_traccion - self.f_drag - self.f_rodadura - self.f_freno
        self.a_m_s2 = f_neta / MASA_COCHE

        self.v_m_s += self.a_m_s2 * dt
        if self.v_m_s < 0:
            self.v_m_s = 0
            self.a_m_s2 = 0

        self.x_m += self.v_m_s * dt

        omega_rueda = self.v_m_s / RADIO_RUEDA_M
        self.angulo_rueda += omega_rueda * dt


def cargar_imagenes():
    coche = pygame.image.load(IMG_COCHE).convert_alpha()
    rueda = pygame.image.load(IMG_RUEDA).convert_alpha()

    coche = pygame.transform.smoothscale(
        coche,
        (int(coche.get_width() * SCALE_COCHE), int(coche.get_height() * SCALE_COCHE))
    )
    rueda = pygame.transform.smoothscale(
        rueda,
        (int(rueda.get_width() * SCALE_RUEDA), int(rueda.get_height() * SCALE_RUEDA))
    )
    return coche, rueda


def texto(screen, font, msg, x, y, color=(245, 245, 245)):
    surf = font.render(msg, True, color)
    screen.blit(surf, (x, y))


def barra(screen, x, y, w, h, valor, color):
    pygame.draw.rect(screen, (70, 70, 70), (x, y, w, h), border_radius=4)
    pygame.draw.rect(screen, color, (x, y, int(w * clamp(valor, 0, 1)), h), border_radius=4)
    pygame.draw.rect(screen, (230, 230, 230), (x, y, w, h), 2, border_radius=4)


def dibujar_fondo(screen, coche):
    screen.fill((26, 29, 35))
    pygame.draw.rect(screen, (90, 90, 90), (0, ROAD_Y, WIDTH, HEIGHT - ROAD_Y))
    pygame.draw.line(screen, (240, 240, 240), (0, ROAD_Y), (WIDTH, ROAD_Y), 4)

    # Marcas de carretera desplazadas para dar sensación de movimiento.
    offset = int((coche.x_m * 25) % 160)
    for x in range(-offset, WIDTH + 160, 160):
        pygame.draw.rect(screen, (235, 235, 235), (x, ROAD_Y + 75, 80, 8), border_radius=3)


def dibujar_coche(screen, coche_img, rueda_img, coche):
    car_x = 300
    car_y = ROAD_Y - coche_img.get_height() + 45
    screen.blit(coche_img, (car_x, car_y))

    # Posiciones aproximadas de las ruedas sobre la imagen escalada.
    rimg = pygame.transform.rotate(rueda_img, -math.degrees(coche.angulo_rueda))
    rw, rh = rimg.get_size()
    rueda_trasera = (car_x + 115, car_y + coche_img.get_height() - 50)
    rueda_delantera = (car_x + coche_img.get_width() - 143, car_y + coche_img.get_height() - 47)

    for cx, cy in [rueda_trasera, rueda_delantera]:
        screen.blit(rimg, (cx - rw / 2, cy - rh / 2))


def dibujar_hud(screen, font, big_font, coche):
    kmh = coche.v_m_s * 3.6
    texto(screen, big_font, f"{kmh:6.1f} km/h", 30, 25)
    texto(screen, big_font, f"Marcha: {coche.marcha}   {'AUTO' if coche.auto else 'MANUAL'}", 30, 70)

    texto(screen, font, f"RPM motor: {coche.rpm:7.0f}", 30, 130)
    barra(screen, 180, 133, 260, 18, coche.rpm / RPM_LIMIT, (220, 40, 40))

    texto(screen, font, f"Acelerador", 30, 165)
    barra(screen, 180, 168, 260, 18, coche.throttle, (40, 190, 70))

    texto(screen, font, f"Freno", 30, 200)
    barra(screen, 180, 203, 260, 18, coche.brake, (220, 80, 40))

    datos = [
        f"Par motor:        {coche.par_motor:8.1f} Nm",
        f"Potencia:         {coche.potencia_cv:8.1f} CV",
        f"Par en rueda:     {coche.par_rueda:8.1f} Nm",
        f"F traccion:       {coche.f_traccion:8.1f} N",
        f"F aire:           {coche.f_drag:8.1f} N",
        f"F rodadura:       {coche.f_rodadura:8.1f} N",
        f"F freno:          {coche.f_freno:8.1f} N",
        f"Aceleracion:      {coche.a_m_s2:8.2f} m/s²",
        f"Distancia:        {coche.x_m:8.1f} m",
        f"Estado ruedas:    {coche.estado_ruedas}",
    ]

    y = 245
    for linea in datos:
        texto(screen, font, linea, 30, y)
        y += 25

    texto(screen, font, "Controles: UP acelerar | DOWN frenar | A auto/manual | Q/E marchas | R reset", 30, HEIGHT - 35, (220, 220, 220))


def main():
    pygame.init()
    pygame.display.set_caption("Ferrari F430 V8 - Simulacion de transmision")
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("consolas", 20)
    big_font = pygame.font.SysFont("consolas", 34, bold=True)

    coche_img, rueda_img = cargar_imagenes()
    coche = F430()

    running = True
    while running:
        dt = clock.tick(FPS) / 1000.0

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    running = False
                elif event.key == pygame.K_r:
                    coche.reset()
                elif event.key == pygame.K_a:
                    coche.auto = not coche.auto
                elif event.key == pygame.K_e:
                    coche.subir_marcha()
                elif event.key == pygame.K_q:
                    coche.bajar_marcha()

        keys = pygame.key.get_pressed()
        coche.throttle = 1.0 if keys[pygame.K_UP] else 0.0
        coche.brake = 1.0 if keys[pygame.K_DOWN] else 0.0

        coche.update(dt)

        dibujar_fondo(screen, coche)
        dibujar_coche(screen, coche_img, rueda_img, coche)
        dibujar_hud(screen, font, big_font, coche)

        pygame.display.flip()

    pygame.quit()
    sys.exit()


if __name__ == "__main__":
    main()
