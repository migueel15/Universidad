import math

import pygame
import pymunk


# --- LA CLASE QUE DEBES ENTENDER ---
class PenduloFriccion:
    def __init__(
        self, espacio, punto_suspension, cuerpo, longitud, coeficiente_friccion
    ):
        self.espacio = espacio
        self.cuerpo = cuerpo
        self.b = coeficiente_friccion

        # 1. Creamos la cuerda (PinJoint)
        # Une un cuerpo estático (el techo) con nuestro cuerpo móvil
        self.cuerda = pymunk.PinJoint(
            espacio.static_body, self.cuerpo, punto_suspension, (0, 0)
        )
        self.cuerda.distance = longitud
        self.espacio.add(self.cuerda)

        # 2. Hacemos que sea AUTÓNOMO
        # Sustituimos la función que calcula la velocidad por una nuestra
        self.cuerpo.velocity_func = self.aplicar_friccion_viscosa

    def aplicar_friccion_viscosa(self, cuerpo, gravedad, damping, dt):
        """
        Esta función se ejecuta automáticamente en cada paso de la física.
        Sustituye al comportamiento por defecto.
        """
        # A. Calculamos la fuerza de rozamiento: F = -b * v (Página 41 de tus apuntes)
        fuerza_freno = -self.b * cuerpo.velocity

        # B. Aplicamos esa fuerza al cuerpo
        cuerpo.apply_force_at_local_point(fuerza_freno)

        # C. IMPORTANTE: Llamamos al método original para que la gravedad
        # y otras fuerzas sigan funcionando normalmente.
        pymunk.Body.update_velocity(cuerpo, gravedad, damping, dt)


# --- CONFIGURACIÓN DEL SIMULADOR (PYGAME) ---
def simular():
    pygame.init()
    screen = pygame.display.set_mode((600, 600))
    clock = pygame.time.Clock()
    espacio = pymunk.Space()
    espacio.gravity = (0, 900)  # Gravedad hacia abajo

    # 1. Crear el cuerpo que colgará (la bola del péndulo)
    masa = 1
    momento = pymunk.moment_for_circle(masa, 0, 20)
    bola = pymunk.Body(masa, momento)
    bola.position = (450, 200)  # La ponemos a un lado para que oscile al soltarla
    forma = pymunk.Circle(bola, 20)
    forma.elasticity = 0.9
    espacio.add(bola, forma)

    # 2. Instanciar nuestra clase personalizada
    punto_anclaje = (300, 100)
    longitud_cuerda = 250
    coeficiente_b = 0.5  # Prueba a cambiar este valor (ej: 0.1 o 2.0)

    pendulo = PenduloFriccion(
        espacio, punto_anclaje, bola, longitud_cuerda, coeficiente_b
    )

    # Bucle principal
    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        # Dibujado
        screen.fill((255, 255, 255))

        # Dibujar anclaje y cuerda
        pygame.draw.circle(screen, (0, 0, 0), punto_anclaje, 5)
        pygame.draw.line(screen, (100, 100, 100), punto_anclaje, bola.position, 2)

        # Dibujar bola
        pos_x, pos_y = bola.position
        pygame.draw.circle(screen, (200, 0, 0), (int(pos_x), int(pos_y)), 20)

        espacio.step(1 / 60.0)
        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    simular()
