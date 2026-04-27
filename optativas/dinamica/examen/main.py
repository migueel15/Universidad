import pygame
import pymunk

WIDTH, HEIGHT = 1000, 600
GREEN_TABLE = (34, 139, 34)
WHITE = (255, 255, 255)
RED = (200, 0, 0)
FPS = 60
V0 = 250.0
RADIO = 25


class BolaPymunk:
    def __init__(self, space, x, y, color, e, masa=1.0):
        # 1. Crear el cuerpo (Body)
        moment = pymunk.moment_for_circle(masa, 0, RADIO)
        self.body = pymunk.Body(masa, moment)
        self.body.position = (x, y)

        # 2. Crear la forma (Shape)
        self.shape = pymunk.Circle(self.body, RADIO)
        self.shape.elasticity = e
        self.shape.friction = 0.0  # Para simular colisión pura en 1D
        self.color = color

        # 3. Añadir al espacio
        space.add(self.body, self.shape)

    def dibujar(self, pantalla):
        pos = self.body.position
        pygame.draw.circle(pantalla, self.color, (int(pos.x), int(pos.y)), RADIO)


def main():
    pygame.init()
    pantalla = pygame.display.set_mode((WIDTH, HEIGHT))
    reloj = pygame.time.Clock()

    space = pymunk.Space()
    space.gravity = (0, 0)

    b1_blanca = BolaPymunk(space, 50, 150, WHITE, 0.7)
    b1_roja = BolaPymunk(space, 500, 150, RED, 0.2)

    b2_blanca = BolaPymunk(space, 50, 250, WHITE, 1)
    b2_roja = BolaPymunk(space, 500, 250, RED, 1)

    bolas = [b1_blanca, b1_roja, b2_blanca, b2_roja]
    b1_blanca.body.velocity = (V0, 0)
    b1_roja.body.velocity = (-V0, 0)
    b2_blanca.body.velocity = (V0, 0)

    while True:
        dt = 1.0 / FPS
        space.step(dt)

        pantalla.fill(GREEN_TABLE)

        for b in bolas:
            b.dibujar(pantalla)

        pygame.display.flip()
        reloj.tick(FPS)


if __name__ == "__main__":
    main()
