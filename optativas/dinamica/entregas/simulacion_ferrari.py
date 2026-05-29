import pygame
import numpy as np
from scipy.interpolate import interp1d
import math


# ========== CLASE DEL VEHÍCULO ==========
class FerrariF430:
    def __init__(self):
        # Datos del motor
        self.rpm_datos = np.array(
            [1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 5250, 5500, 6000, 6500, 7000, 7500, 8000, 8500])
        self.par_datos = np.array([320, 345, 370, 400, 415, 430, 445, 455, 465, 465, 460, 455, 450, 440, 430, 415, 390])
        self.interp_par = interp1d(self.rpm_datos, self.par_datos, kind='cubic', fill_value="extrapolate")

        # Transmisión
        self.relaciones = {1: 3.29, 2: 2.16, 3: 1.61, 4: 1.26, 5: 1.03, 6: 0.85}
        self.final_drive = 4.30
        self.radio_rueda = 0.342  # metros
        self.eficiencia = 0.88

        # Parámetros físicos
        self.masa = 1450  # kg
        self.g = 9.81
        self.Cd = 0.33
        self.A = 2.03
        self.rho = 1.225
        self.Crr = 0.015
        self.Cl = -0.31

        # Estado del vehículo
        self.posicion_x = 100  # posición horizontal en pantalla
        self.velocidad = 0.0  # m/s
        self.rpm_motor = 1000.0
        self.marcha_actual = 1
        self.angulo_rueda = 0  # ángulo de rotación de la rueda

        # Límites
        self.rpm_min = 800
        self.rpm_max = 8500
        self.rpm_cambio = 8200

    def get_par_motor(self, rpm):
        rpm = max(self.rpm_min, min(rpm, self.rpm_max))
        return float(self.interp_par(rpm))

    def get_potencia_cv(self, rpm, par):
        return (par * rpm * 2 * np.pi) / (60 * 735.5)

    def calcular_fuerza_traccion(self, rpm, marcha):
        par_motor = self.get_par_motor(rpm)
        relacion_marcha = self.relaciones[marcha]
        par_rueda = par_motor * relacion_marcha * self.final_drive * self.eficiencia
        fuerza_traccion = par_rueda / self.radio_rueda
        return fuerza_traccion, par_motor

    def calcular_resistencias(self, velocidad):
        Fd = 0.5 * self.Cd * self.rho * self.A * velocidad ** 2
        Fn_grav = self.masa * self.g
        F_down = 0.5 * self.rho * velocidad ** 2 * self.A * abs(self.Cl)
        Fr = self.Crr * (Fn_grav + F_down)
        return Fd + Fr, F_down

    def calcular_fuerza_normal_total(self, velocidad):
        Fn_grav = self.masa * self.g
        F_down = 0.5 * self.rho * velocidad ** 2 * self.A * abs(self.Cl)
        return Fn_grav + F_down

    def obtener_rpm_desde_velocidad(self, velocidad, marcha):
        if velocidad <= 0:
            return self.rpm_min
        relacion_marcha = self.relaciones[marcha]
        rpm_rueda = velocidad / (2 * np.pi * self.radio_rueda) * 60
        rpm_motor = rpm_rueda * relacion_marcha * self.final_drive
        return max(self.rpm_min, min(rpm_motor, self.rpm_max))

    def actualizar(self, dt, acelerador, freno):
        # Velocidad angular de la rueda para animación (rad/s)
        velocidad_angular = self.velocidad / self.radio_rueda
        self.angulo_rueda -= math.degrees(velocidad_angular * dt)
        if self.angulo_rueda >= 360:
            self.angulo_rueda -= 360

        if acelerador > 0:
            self.rpm_motor = self.obtener_rpm_desde_velocidad(self.velocidad, self.marcha_actual)
            fuerza_traccion, par_motor = self.calcular_fuerza_traccion(self.rpm_motor, self.marcha_actual)
            fuerzas_resistivas, downforce = self.calcular_resistencias(self.velocidad)

            Fn_total = self.calcular_fuerza_normal_total(self.velocidad)
            mu = 0.9
            fuerza_max_adherencia = mu * Fn_total

            fuerza_traccion_aplicada = min(fuerza_traccion, fuerza_max_adherencia)
            fuerza_neta = fuerza_traccion_aplicada - fuerzas_resistivas
            aceleracion = fuerza_neta / self.masa

            self.velocidad += aceleracion * dt
            self.velocidad = max(0, self.velocidad)

            # Movimiento horizontal (el coche se mueve hacia la derecha)
            self.posicion_x += self.velocidad * dt * 2
            if self.posicion_x > self.ANCHO:
                self.posicion_x = 0

            self.rpm_motor = self.obtener_rpm_desde_velocidad(self.velocidad, self.marcha_actual)

            return par_motor, fuerza_traccion_aplicada, fuerzas_resistivas, aceleracion, downforce

        elif freno > 0:
            fuerzas_resistivas, downforce = self.calcular_resistencias(self.velocidad)
            fuerza_freno = freno * 5000
            fuerza_neta = -min(fuerzas_resistivas + fuerza_freno, self.masa * 12)

            self.velocidad += fuerza_neta / self.masa * dt
            self.velocidad = max(0, self.velocidad)

            self.posicion_x += self.velocidad * dt * 2
            if self.posicion_x > self.ANCHO:
                self.posicion_x = 0

            self.rpm_motor = self.obtener_rpm_desde_velocidad(self.velocidad, self.marcha_actual)

            return None, 0, fuerzas_resistivas, fuerza_neta / self.masa, downforce

        else:
            fuerzas_resistivas, downforce = self.calcular_resistencias(self.velocidad)
            fuerza_neta = -fuerzas_resistivas
            aceleracion = fuerza_neta / self.masa

            self.velocidad += aceleracion * dt
            self.velocidad = max(0, self.velocidad)

            self.posicion_x += self.velocidad * dt * 2
            if self.posicion_x > self.ANCHO:
                self.posicion_x = 0

            self.rpm_motor = self.obtener_rpm_desde_velocidad(self.velocidad, self.marcha_actual)

            return None, 0, fuerzas_resistivas, aceleracion, downforce

    def cambiar_marcha(self, direccion):
        nueva_marcha = self.marcha_actual + direccion
        if 1 <= nueva_marcha <= 6:
            self.marcha_actual = nueva_marcha
            self.rpm_motor = self.obtener_rpm_desde_velocidad(self.velocidad, self.marcha_actual)
            return True
        return False


# ========== CLASE DEL JUEGO ==========
class SimuladorF430:
    def __init__(self):
        pygame.init()

        # Configuración de pantalla
        self.ANCHO = 1400
        self.ALTO = 800
        self.pantalla = pygame.display.set_mode((self.ANCHO, self.ALTO))
        pygame.display.set_caption("Ferrari F430 Simulator - Conducción Visual")

        # Colores
        self.NEGRO = (0, 0, 0)
        self.BLANCO = (255, 255, 255)
        self.ROJO = (255, 50, 50)
        self.VERDE = (50, 255, 50)
        self.AZUL = (50, 150, 255)
        self.GRIS = (80, 80, 80)
        self.GRIS_CLARO = (120, 120, 120)
        self.AMARILLO = (255, 255, 50)
        self.NARANJA = (255, 150, 0)
        self.PLATA = (192, 192, 192)

        # Fuentes
        self.fuente_grande = pygame.font.Font(None, 64)
        self.fuente_media = pygame.font.Font(None, 36)
        self.fuente_peq = pygame.font.Font(None, 24)
        self.fuente_digital = pygame.font.Font(None, 48)

        # Reloj
        self.reloj = pygame.time.Clock()

        # Cargar imágenes
        self.cargar_imagenes()

        # Inicializar vehículo
        self.coche = FerrariF430()
        self.coche.ANCHO = self.ANCHO  # Pasar referencia para movimiento

        # Controles
        self.acelerador = 0.0
        self.freno = 0.0

        # Estado del juego
        self.t = 0
        self.dt = 1 / 60

        # Efectos visuales
        self.line_offset = 0

    def cargar_imagenes(self):
        """Carga las imágenes con las dimensiones especificadas"""
        try:
            # Cargar coche (1225x404) - mirando hacia la derecha
            self.img_coche_original = pygame.image.load("F430V8.png").convert_alpha()

            # Escalar coche para la pantalla (ancho ~350px)
            escala_coche = 350 / 1225
            nuevo_ancho_coche = int(1225 * escala_coche)
            nuevo_alto_coche = int(404 * escala_coche)
            self.img_coche = pygame.transform.scale(self.img_coche_original, (nuevo_ancho_coche, nuevo_alto_coche))

            print(f"✅ Coche cargado: {nuevo_ancho_coche}x{nuevo_alto_coche}px")

            # Cargar rueda (195x194)
            self.img_rueda_original = pygame.image.load("F430V8_rueda.png").convert_alpha()

            # Escalar rueda (tamaño proporcionado ~50x50)
            escala_rueda = 60 / 195
            nuevo_ancho_rueda = int(195 * escala_rueda)
            nuevo_alto_rueda = int(194 * escala_rueda)
            self.img_rueda = pygame.transform.scale(self.img_rueda_original, (nuevo_ancho_rueda, nuevo_alto_rueda))

            print(f"✅ Rueda cargada: {nuevo_ancho_rueda}x{nuevo_alto_rueda}px")

        except Exception as e:
            print(f"⚠️ Error cargando imágenes: {e}")
            print("Usando modo sin imágenes")
            self.img_coche = None
            self.img_rueda = None

    def dibujar_fondo(self):
        """Dibuja el fondo con movimiento horizontal (de izquierda a derecha)"""
        # Cielo degradado
        for i in range(self.ALTO - 150):
            color = (135 - i // 5, 206 - i // 3, 235 - i // 2)
            if color[0] > 0 and color[1] > 0 and color[2] > 0:
                pygame.draw.line(self.pantalla, color, (0, i), (self.ANCHO, i))

        # Suelo
        pygame.draw.rect(self.pantalla, (40, 40, 40), (0, self.ALTO - 150, self.ANCHO, 150))

        # Asfalto con textura
        for i in range(0, self.ANCHO, 20):
            pygame.draw.rect(self.pantalla, (50, 50, 50), (i, self.ALTO - 150, 10, 150))

        # Línea del horizonte
        pygame.draw.line(self.pantalla, self.BLANCO, (0, self.ALTO - 150), (self.ANCHO, self.ALTO - 150), 4)

        # Líneas de carril en movimiento (scroll horizontal)
        self.line_offset = (self.line_offset - self.coche.velocidad * 3) % 100
        for x in range(-50, self.ANCHO, 80):
            pygame.draw.rect(self.pantalla, self.BLANCO, (x + self.line_offset, self.ALTO - 80, 40, 6))

        # Bordes de carretera
        for x in range(-50, self.ANCHO, 60):
            pygame.draw.rect(self.pantalla, self.AMARILLO, (x + self.line_offset * 1.5, self.ALTO - 55, 15, 4))
            pygame.draw.rect(self.pantalla, self.AMARILLO, (x + self.line_offset * 1.5, self.ALTO - 140, 15, 4))

        # Árboles en movimiento (de izquierda a derecha)
        tree_offset = (self.coche.posicion_x * 0.5) % (self.ANCHO + 200)
        for i in range(5):
            x = i * 250 - tree_offset
            # Solo dibujar si está dentro de la pantalla (con un margen)
            if -50 < x < self.ANCHO + 50:
                # Tronco
                pygame.draw.rect(self.pantalla, (101, 67, 33), (x, self.ALTO - 200, 15, 50))
                # Copa del árbol (círculos, no rectángulos estirados)
                pygame.draw.circle(self.pantalla, (34, 139, 34), (x + 7, self.ALTO - 215), 20)
                pygame.draw.circle(self.pantalla, (0, 100, 0), (x + 0, self.ALTO - 225), 15)
                pygame.draw.circle(self.pantalla, (0, 100, 0), (x + 14, self.ALTO - 225), 15)

    def dibujar_velocimetro(self, velocidad_kmh, rpm):
        """Dibuja el velocímetro con fondo gris"""
        centro_x, centro_y = 180, self.ALTO - 120
        radio = 100

        # Fondo gris del velocímetro
        pygame.draw.circle(self.pantalla, self.GRIS, (centro_x, centro_y), radio + 5)
        pygame.draw.circle(self.pantalla, self.GRIS_CLARO, (centro_x, centro_y), radio + 2)
        pygame.draw.circle(self.pantalla, self.NEGRO, (centro_x, centro_y), radio)

        # Marcas de velocidad
        for velocidad in range(0, 361, 30):
            angulo = -135 + (velocidad / 360) * 270
            angulo_rad = math.radians(angulo)
            x1 = centro_x + math.cos(angulo_rad) * (radio - 15)
            y1 = centro_y + math.sin(angulo_rad) * (radio - 15)
            x2 = centro_x + math.cos(angulo_rad) * (radio - 5)
            y2 = centro_y + math.sin(angulo_rad) * (radio - 5)
            pygame.draw.line(self.pantalla, self.BLANCO, (x1, y1), (x2, y2), 3)

            if velocidad % 60 == 0:
                texto = self.fuente_peq.render(str(velocidad), True, self.BLANCO)
                x_text = centro_x + math.cos(angulo_rad) * (radio - 25) - 10
                y_text = centro_y + math.sin(angulo_rad) * (radio - 25) - 10
                self.pantalla.blit(texto, (x_text, y_text))

        # Aguja del velocímetro
        velocidad_max = 360
        angulo = -135 + (min(velocidad_kmh, velocidad_max) / velocidad_max) * 270
        angulo_rad = math.radians(angulo)
        punta_x = centro_x + math.cos(angulo_rad) * (radio - 25)
        punta_y = centro_y + math.sin(angulo_rad) * (radio - 25)

        pygame.draw.line(self.pantalla, self.ROJO, (centro_x, centro_y), (punta_x, punta_y), 5)
        pygame.draw.circle(self.pantalla, self.ROJO, (centro_x, centro_y), 10)
        pygame.draw.circle(self.pantalla, self.NEGRO, (centro_x, centro_y), 5)

        # Display digital central
        texto_vel = self.fuente_digital.render(f"{int(velocidad_kmh):3d}", True, self.BLANCO)
        rect_vel = texto_vel.get_rect(center=(centro_x, centro_y))
        self.pantalla.blit(texto_vel, rect_vel)

        texto_label = self.fuente_peq.render("km/h", True, self.BLANCO)
        self.pantalla.blit(texto_label, (centro_x - 15, centro_y + 25))

    def dibujar_tacometro(self, rpm):
        """Dibuja el tacómetro con fondo gris"""
        centro_x, centro_y = self.ANCHO - 180, self.ALTO - 120
        radio = 100

        # Fondo gris del tacómetro
        pygame.draw.circle(self.pantalla, self.GRIS, (centro_x, centro_y), radio + 5)
        pygame.draw.circle(self.pantalla, self.GRIS_CLARO, (centro_x, centro_y), radio + 2)
        pygame.draw.circle(self.pantalla, self.NEGRO, (centro_x, centro_y), radio)

        # Marcas de RPM
        for rpm_val in range(0, 9001, 1000):
            angulo = -135 + (rpm_val / 9000) * 270
            angulo_rad = math.radians(angulo)
            x1 = centro_x + math.cos(angulo_rad) * (radio - 15)
            y1 = centro_y + math.sin(angulo_rad) * (radio - 15)
            x2 = centro_x + math.cos(angulo_rad) * (radio - 5)
            y2 = centro_y + math.sin(angulo_rad) * (radio - 5)
            pygame.draw.line(self.pantalla, self.BLANCO, (x1, y1), (x2, y2), 3)

            if rpm_val % 2000 == 0:
                texto = self.fuente_peq.render(f"{rpm_val // 1000}k", True, self.BLANCO)
                x_text = centro_x + math.cos(angulo_rad) * (radio - 30) - 15
                y_text = centro_y + math.sin(angulo_rad) * (radio - 30) - 10
                self.pantalla.blit(texto, (x_text, y_text))

        # Zona roja
        angulo_rojo_start = -135 + (self.coche.rpm_cambio / 9000) * 270
        angulo_rojo_end = -135 + 270

        for ang in np.arange(angulo_rojo_start, angulo_rojo_end, 2):
            ang_rad = math.radians(ang)
            x_in = centro_x + math.cos(ang_rad) * (radio - 12)
            y_in = centro_y + math.sin(ang_rad) * (radio - 12)
            x_out = centro_x + math.cos(ang_rad) * (radio - 3)
            y_out = centro_y + math.sin(ang_rad) * (radio - 3)
            pygame.draw.line(self.pantalla, self.ROJO, (x_in, y_in), (x_out, y_out), 2)

        # Aguja del tacómetro
        angulo = -135 + (min(rpm, 9000) / 9000) * 270
        angulo_rad = math.radians(angulo)
        punta_x = centro_x + math.cos(angulo_rad) * (radio - 25)
        punta_y = centro_y + math.sin(angulo_rad) * (radio - 25)

        pygame.draw.line(self.pantalla, self.VERDE, (centro_x, centro_y), (punta_x, punta_y), 5)
        pygame.draw.circle(self.pantalla, self.VERDE, (centro_x, centro_y), 10)
        pygame.draw.circle(self.pantalla, self.NEGRO, (centro_x, centro_y), 5)

        # Display digital central
        texto_rpm = self.fuente_digital.render(f"{int(rpm):4d}", True, self.BLANCO)
        rect_rpm = texto_rpm.get_rect(center=(centro_x, centro_y))
        self.pantalla.blit(texto_rpm, rect_rpm)

        texto_label = self.fuente_peq.render("RPM", True, self.BLANCO)
        self.pantalla.blit(texto_label, (centro_x - 15, centro_y + 25))

    def dibujar_coche(self):
        """Dibuja el Ferrari mirando hacia la derecha con 2 ruedas"""
        if self.img_coche and self.img_rueda:
            # Posición del coche (centrado verticalmente en la carretera)
            x_pos = self.ANCHO // 3  # Coche fijo en pantalla, fondo se mueve
            y_pos = self.ALTO - self.img_coche.get_height() - 30

            # Sombra
            sombra_surf = pygame.Surface((self.img_coche.get_width(), self.img_coche.get_height()), pygame.SRCALPHA)
            sombra_surf.fill((0, 0, 0, 80))
            self.pantalla.blit(sombra_surf, (x_pos + 5, y_pos + 10))

            # Cuerpo del coche
            self.pantalla.blit(self.img_coche, (x_pos, y_pos))

            # Calcular posiciones de las 2 ruedas (coche 2D perfil)
            ancho_coche = self.img_coche.get_width()
            alto_coche = self.img_coche.get_height()
            rueda_ancho = self.img_rueda.get_width()
            rueda_alto = self.img_rueda.get_height()

            # Rueda trasera (izquierda en la imagen, porque mira a la derecha)
            rueda_trasera_x = x_pos - 40 + ancho_coche * 0.2
            rueda_trasera_y = y_pos - 35 + alto_coche * 0.82

            # Rueda delantera (derecha en la imagen)
            rueda_delantera_x = x_pos + 8 + ancho_coche * 0.68
            rueda_delantera_y = y_pos - 35 + alto_coche * 0.82

            # Rotar ruedas según velocidad
            angulo_rotacion = self.coche.angulo_rueda

            # Dibujar rueda trasera
            rueda_rot_trasera = pygame.transform.rotate(self.img_rueda, angulo_rotacion)
            rect_trasera = rueda_rot_trasera.get_rect(
                center=(rueda_trasera_x + rueda_ancho // 2, rueda_trasera_y + rueda_alto // 2))
            self.pantalla.blit(rueda_rot_trasera, rect_trasera)

            # Dibujar rueda delantera
            rueda_rot_delantera = pygame.transform.rotate(self.img_rueda, angulo_rotacion)
            rect_delantera = rueda_rot_delantera.get_rect(
                center=(rueda_delantera_x + rueda_ancho // 2, rueda_delantera_y + rueda_alto // 2))
            self.pantalla.blit(rueda_rot_delantera, rect_delantera)

            # Efecto de brillo a altas velocidades
            if self.coche.velocidad > 30:
                brillo = pygame.Surface((self.img_coche.get_width(), self.img_coche.get_height()), pygame.SRCALPHA)
                intensidad = min(100, int(self.coche.velocidad / 3))
                brillo.fill((255, 255, 255, intensidad // 3))
                self.pantalla.blit(brillo, (x_pos, y_pos))

            # Luz de freno (roja atrás - lado izquierdo del coche)
            if self.freno > 0:
                luz_freno_x = x_pos + 15
                luz_freno_y = y_pos + alto_coche // 2
                pygame.draw.circle(self.pantalla, self.ROJO, (luz_freno_x, luz_freno_y), 15)
                pygame.draw.circle(self.pantalla, (255, 100, 100), (luz_freno_x, luz_freno_y), 8)
                # Efecto de halo
                for r in range(20, 35, 5):
                    alpha_surf = pygame.Surface((r * 2, r * 2), pygame.SRCALPHA)
                    pygame.draw.circle(alpha_surf, (255, 50, 50, 100 - r * 2), (r, r), r)
                    self.pantalla.blit(alpha_surf, (luz_freno_x - r, luz_freno_y - r))

            # Luz delantera (blanca/amarilla - lado derecho)
            luz_delantera_x = x_pos + ancho_coche - 10
            luz_delantera_y = y_pos + alto_coche // 2
            if self.acelerador > 0 or self.coche.velocidad > 0:
                pygame.draw.circle(self.pantalla, self.AMARILLO, (luz_delantera_x, luz_delantera_y), 12)
                pygame.draw.circle(self.pantalla, (255, 255, 150), (luz_delantera_x, luz_delantera_y), 6)
                # Haz de luz
                for offset in range(10, 50, 10):
                    alpha = max(0, 50 - offset)
                    pygame.draw.circle(self.pantalla, (255, 255, 150, alpha),
                                       (luz_delantera_x + offset, luz_delantera_y), 8)

            # Humo al patinar (acelerando fuerte en bajas velocidades)
            if self.acelerador > 0.7 and self.coche.velocidad < 15 and self.coche.marcha_actual <= 2:
                humo_x = rueda_trasera_x + np.random.randint(-10, 20)
                humo_y = rueda_trasera_y + rueda_alto // 2
                pygame.draw.circle(self.pantalla, (100, 100, 100), (humo_x, humo_y), np.random.randint(4, 10))
                pygame.draw.circle(self.pantalla, (80, 80, 80), (humo_x - 3, humo_y - 2), np.random.randint(3, 7))
                pygame.draw.circle(self.pantalla, (60, 60, 60), (humo_x - 6, humo_y - 4), np.random.randint(2, 5))

        else:
            # Modo sin imágenes - dibujo simple
            x_pos = self.ANCHO // 3
            y_pos = self.ALTO - 130
            pygame.draw.rect(self.pantalla, self.ROJO, (x_pos, y_pos, 300, 80))
            pygame.draw.circle(self.pantalla, self.NEGRO, (x_pos + 60, y_pos + 65), 20)
            pygame.draw.circle(self.pantalla, self.NEGRO, (x_pos + 240, y_pos + 65), 20)

    def dibujar_interfaz(self, velocidad_kmh, rpm, marcha, par_motor, potencia_cv, aceleracion, downforce):
        """Dibuja toda la información del vehículo"""
        # Panel izquierdo
        panel_x = 20
        panel_y = 20

        # Fondo del panel
        s = pygame.Surface((320, 280))
        s.set_alpha(200)
        s.fill(self.NEGRO)
        self.pantalla.blit(s, (panel_x, panel_y))
        pygame.draw.rect(self.pantalla, self.PLATA, (panel_x, panel_y, 320, 280), 2)

        # Datos
        textos = [
            ("FERRARI F430 V8", self.AMARILLO, self.fuente_media),
            (f"Marcha: {marcha}ª", self.BLANCO, self.fuente_peq),
            (f"Par: {par_motor:.0f} Nm", self.BLANCO, self.fuente_peq),
            (f"Potencia: {potencia_cv:.0f} CV", self.BLANCO, self.fuente_peq),
            (f"Aceleracion: {aceleracion:.2f} m/s²", self.BLANCO, self.fuente_peq),
            (f"Downforce: {downforce / 1000:.2f} kN", self.BLANCO, self.fuente_peq),
            (f"Velocidad: {velocidad_kmh:.1f} km/h", self.VERDE, self.fuente_peq)
        ]

        y_offset = panel_y + 10
        for texto, color, fuente in textos:
            render = fuente.render(texto, True, color)
            self.pantalla.blit(render, (panel_x + 15, y_offset))
            y_offset += 35

        # Barra de potencia
        barra_x = panel_x + 15
        barra_y = panel_y + 250
        barra_width = 290
        barra_height = 15

        pygame.draw.rect(self.pantalla, self.GRIS, (barra_x, barra_y, barra_width, barra_height))

        potencia_ratio = min(1.0, potencia_cv / 500)
        if potencia_ratio > 0.8:
            color_barra = self.ROJO
        elif potencia_ratio > 0.5:
            color_barra = self.NARANJA
        else:
            color_barra = self.VERDE

        pygame.draw.rect(self.pantalla, color_barra, (barra_x, barra_y, barra_width * potencia_ratio, barra_height))

        texto_potencia = self.fuente_peq.render("POTENCIA", True, self.BLANCO)
        self.pantalla.blit(texto_potencia, (barra_x, barra_y - 18))

        # Advertencias
        if rpm > self.coche.rpm_cambio and marcha < 6:
            if pygame.time.get_ticks() % 1000 < 500:
                adv_texto = self.fuente_grande.render("¡CAMBIO!", True, self.ROJO)
                self.pantalla.blit(adv_texto, (self.ANCHO // 2 - 80, 80))
                adv_texto2 = self.fuente_media.render("RECOMENDADO", True, self.ROJO)
                self.pantalla.blit(adv_texto2, (self.ANCHO // 2 - 70, 140))
        elif rpm < 1200 and marcha > 1:
            if pygame.time.get_ticks() % 1000 < 500:
                adv_texto = self.fuente_media.render("RPM BAJAS", True, self.AMARILLO)
                self.pantalla.blit(adv_texto, (self.ANCHO // 2 - 70, 100))

    def ejecutar(self):
        """Bucle principal del simulador"""
        print("\n" + "=" * 70)
        print("              FERRARI F430 SIMULATOR - MODO VISUAL HD")
        print("=" * 70)
        print("\n🎮 CONTROLES:")
        print("  ⬆️  o  A  →  Acelerar")
        print("  ⬇️  o  S  →  Frenar")
        print("  ➡️  o  W  →  Subir marcha")
        print("  ⬅️  o  Q  →  Bajar marcha")
        print("  R         →  Reiniciar simulación")
        print("  ESC       →  Salir")
        print("\n📊 DATOS DEL VEHÍCULO:")
        print(f"  • Peso: 1450 kg")
        print(f"  • Potencia máxima: ~490 CV")
        print(f"  • Par máximo: ~465 Nm")
        print(f"  • Velocidad máxima: ~320 km/h")
        print("=" * 70 + "\n")

        corriendo = True

        while corriendo:
            # Procesar eventos
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    corriendo = False
                elif event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        corriendo = False
                    elif event.key == pygame.K_r:
                        self.coche = FerrariF430()
                        self.coche.ANCHO = self.ANCHO
                        self.acelerador = 0.0
                        self.freno = 0.0
                        print("✅ Vehículo reiniciado")

            # Controles continuos
            teclas = pygame.key.get_pressed()

            self.acelerador = 1.0 if (teclas[pygame.K_UP] or teclas[pygame.K_a]) else 0.0
            self.freno = 1.0 if (teclas[pygame.K_DOWN] or teclas[pygame.K_s]) else 0.0

            # Cambio de marchas con debounce
            if teclas[pygame.K_RIGHT] or teclas[pygame.K_w]:
                if self.coche.cambiar_marcha(1):
                    print(f"⚙️  Cambio a {self.coche.marcha_actual}ª marcha")
                    pygame.time.wait(150)
            elif teclas[pygame.K_LEFT] or teclas[pygame.K_q]:
                if self.coche.cambiar_marcha(-1):
                    print(f"⚙️  Cambio a {self.coche.marcha_actual}ª marcha")
                    pygame.time.wait(150)

            # Actualizar física
            resultado = self.coche.actualizar(self.dt, self.acelerador, self.freno)

            if self.acelerador > 0 and len(resultado) == 5:
                par_motor, ft, fr, aceleracion, downforce = resultado
                potencia_cv = self.coche.get_potencia_cv(self.coche.rpm_motor, par_motor)
            elif len(resultado) == 5:
                par_motor = self.coche.get_par_motor(self.coche.rpm_motor)
                potencia_cv = self.coche.get_potencia_cv(self.coche.rpm_motor, par_motor)
                aceleracion = resultado[3] if len(resultado) > 3 else 0
                downforce = resultado[4] if len(resultado) > 4 else 0
            else:
                par_motor = self.coche.get_par_motor(self.coche.rpm_motor)
                potencia_cv = self.coche.get_potencia_cv(self.coche.rpm_motor, par_motor)
                aceleracion = 0
                downforce = 0

            velocidad_kmh = self.coche.velocidad * 3.6

            self.pantalla.fill((135, 206, 235))  # Color del cielo
            # Dibujar todo
            self.dibujar_fondo()
            self.dibujar_coche()
            self.dibujar_velocimetro(velocidad_kmh, self.coche.rpm_motor)
            self.dibujar_tacometro(self.coche.rpm_motor)
            self.dibujar_interfaz(velocidad_kmh, self.coche.rpm_motor,
                                  self.coche.marcha_actual, par_motor,
                                  potencia_cv, aceleracion, downforce)

            # Velocímetro digital extra grande (opcional)
            if velocidad_kmh > 300:
                mega_texto = self.fuente_grande.render(f"{int(velocidad_kmh)}", True, self.ROJO)
                self.pantalla.blit(mega_texto, (self.ANCHO // 2 - 40, self.ALTO // 2 - 50))

            pygame.display.flip()
            self.reloj.tick(60)
            self.t += self.dt

        pygame.quit()
        print("\n🏁 ¡Gracias por conducir el Ferrari F430!")


# ========== MAIN ==========
if __name__ == "__main__":
    simulador = SimuladorF430()
    simulador.ejecutar()