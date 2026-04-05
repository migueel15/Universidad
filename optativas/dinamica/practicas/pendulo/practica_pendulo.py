# SALMA BOULAGNA MORENO Y MIGUEL ANGEL DORADO MALDONADO

import pygame
import pymunk
from pymunk import Vec2d


class DampingPendulum:
    def __init__(
        self,
        space: pymunk.Space,
        length: float,
        body: pymunk.Body,
        suspension_point: Vec2d,
        friction_coefficient: float,
    ) -> None:
        self.space = space
        self.length = float(length)
        self.body = body
        self.suspension_point = suspension_point
        self.friction_coefficient = friction_coefficient

        self.rope = pymunk.PinJoint(
            space.static_body, self.body, suspension_point, (0, 0)
        )
        self.rope.distance = length
        self.space.add(self.rope)

        self.body.velocity_func = self.apply_damping

    def apply_damping(self, body: pymunk.Body, gravity, damping, dt):
        f = -self.friction_coefficient * body.velocity
        body.apply_force_at_local_point(f)
        pymunk.Body.update_velocity(body, gravity, damping, dt)


def run_simulation():
    pygame.init()
    screen = pygame.display.set_mode((600, 600))
    clock = pygame.time.Clock()
    space = pymunk.Space()
    space.gravity = (0, 900)

    mass = 1
    moment = pymunk.moment_for_circle(mass, 0, 20)
    ball = pymunk.Body(mass, moment)
    ball.position = (550, 100)
    form = pymunk.Circle(ball, 20)
    # form.elasticity = 0.9
    space.add(ball)

    suspension_point = (300, 100)
    length = 250
    friction_coefficient = 0.3

    pendulum = DampingPendulum(
        space, length, ball, suspension_point, friction_coefficient
    )

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        screen.fill((255, 255, 255))

        pygame.draw.circle(screen, (0, 0, 0), suspension_point, 5)
        pygame.draw.line(screen, (100, 100, 100), suspension_point, ball.position, 2)

        pos_x, pos_y = ball.position
        pygame.draw.circle(screen, (200, 0, 0), (int(pos_x), int(pos_y)), 20)

        space.step(1 / 60.0)
        pygame.display.flip()
        clock.tick(60)

    pygame.quit()


if __name__ == "__main__":
    run_simulation()
