import pymunk
import pygame
import pymunk.pygame_util
import math

import sys

# escala que vamos a usar: 1 metro = 100 píxeles
PIXELS_PER_METER = 100.0

LANE_LENGTH_M = 18.288
BALL_RADIUS_M = 0.108

WIDTH = LANE_LENGTH_M * PIXELS_PER_METER + 160
HEIGHT = 700
FPS = 60

# BALL_RADIUS_PX = 30
BALL_RADIUS_PX = int(BALL_RADIUS_M * PIXELS_PER_METER)
BALL_MASS = 7.0
BALL_INITIAL_VELOCITY_MPS = 7.0

GROUND_Y = 550


def create_space() -> pymunk.Space:
    space = pymunk.Space()
    space.gravity = (0, 900)
    return space


def create_ground(space: pymunk.Space) -> pymunk.Segment:
    body = space.static_body
    shape = pymunk.Segment(
        body, (80, GROUND_Y), ((80 + LANE_LENGTH_M * PIXELS_PER_METER), GROUND_Y), 3
    )
    shape.friction = 0.2
    shape.elasticity = 0.0
    space.add(shape)
    return shape


def create_ball(space: pymunk.Space) -> tuple[pymunk.Body, pymunk.Circle]:
    moment = pymunk.moment_for_circle(BALL_MASS, 0, BALL_RADIUS_PX)
    body = pymunk.Body(BALL_MASS, moment)
    body.position = (200, GROUND_Y - BALL_RADIUS_PX - 1)
    body.velocity = (BALL_INITIAL_VELOCITY_MPS * PIXELS_PER_METER, 0)

    shape = pymunk.Circle(body, BALL_RADIUS_PX)
    shape.friction = 0.8
    shape.elasticity = 0.0

    space.add(body, shape)
    return body, shape


def render_buttons(
    screen: pygame.Surface,
    font: pygame.font.Font,
    launch_button: pygame.Rect,
    reset_button: pygame.Rect,
) -> None:
    pygame.draw.rect(screen, (70, 130, 180), launch_button, border_radius=8)
    launch_text = font.render("Lanzar", True, (255, 255, 255))
    screen.blit(launch_text, launch_text.get_rect(center=launch_button.center))

    # Botón Reset
    pygame.draw.rect(screen, (180, 80, 80), reset_button, border_radius=8)
    reset_text = font.render("Reset", True, (255, 255, 255))
    screen.blit(reset_text, reset_text.get_rect(center=reset_button.center))


def render_text(
    screen: pygame.Surface, font: pygame.font.Font, text: str, position: tuple[int, int]
) -> None:
    text_surface = font.render(text, True, (0, 0, 0))
    screen.blit(text_surface, position)


def render_stats(
    screen: pygame.Surface, font: pygame.font.Font, ball_body: pymunk.Body | None
) -> None:
    if ball_body is not None:

        INITIAL_X = 20
        INITIAL_Y = 80

        # show velocity, angular velocity
        vel_x, vel_y = ball_body.velocity
        speed = math.sqrt(vel_x**2 + vel_y**2)
        ang_vel = ball_body.angular_velocity

        speed_mts = speed / PIXELS_PER_METER

        # current_state = (
        #     "DESLIZAMIENTO PURO"
        #     if abs(ang_vel) < 1e-3 and vel_x != 0
        #     else "PATINA" if abs(ang_vel) < 1e-3 and vel_x == 0
        #     else "RODAMIENTO"
        # )

        current_state = (
            "RODADURA PURA"
            if abs(ang_vel * BALL_RADIUS_PX - speed) < 1e-1
            else (
                "DESLIZAMIENTO PURO"
                if abs(ang_vel) < 1e-1 and vel_x != 0
                else (
                    "RODADURA CON DESLIZAMIENTO"
                    if abs(vel_x) - abs(ang_vel * BALL_RADIUS_PX) > 1e-1
                    else "PATINA"
                )
            )
        )

        render_text(
            screen, font, f"Velocidad: {speed_mts:.2f} m/s", (INITIAL_X, INITIAL_Y)
        )

        render_text(
            screen,
            font,
            f"Velocidad angular: {ang_vel:.2f} rad/s",
            (INITIAL_X, INITIAL_Y + 30),
        )

        render_text(
            screen,
            font,
            f"Estado: {current_state}",
            (INITIAL_X, INITIAL_Y + 60),
        )


def main() -> None:
    ball_body = None
    ball_shape = None
    simulation_running = False

    track_time = False
    initial_time = 0.0

    pygame.init()
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    pygame.display.set_caption("Bolos - Simulación base")
    clock = pygame.time.Clock()
    font = pygame.font.Font(None, 32)

    space = create_space()
    create_ground(space)

    draw_options = pymunk.pygame_util.DrawOptions(screen)

    launch_button = pygame.Rect(20, 20, 140, 50)
    reset_button = pygame.Rect(180, 20, 140, 50)

    running = True

    while running:
        dt = clock.tick(FPS) / 1000.0

        if track_time and ball_body is not None:
            initial_time += dt

            if track_time and (
                abs(ball_body.angular_velocity * BALL_RADIUS_PX - ball_body.velocity[0])
                < 1
            ):
                track_time = False
                print(f"Tiempo hasta rodadura pura: {initial_time:.2f} segundos")
            else:
                print(
                    abs(
                        ball_body.angular_velocity * BALL_RADIUS_PX
                        - ball_body.velocity[0]
                    )
                )

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

            elif event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:
                mouse_pos = event.pos

                if launch_button.collidepoint(mouse_pos):
                    if ball_body is None:
                        ball_body, ball_shape = create_ball(space)
                        simulation_running = True
                        track_time = True

                elif reset_button.collidepoint(mouse_pos):
                    if ball_body is not None and ball_shape is not None:
                        space.remove(ball_body, ball_shape)
                        ball_body = None
                        ball_shape = None

                    simulation_running = False
                    track_time = False
                    initial_time = 0.0

        if simulation_running:
            space.step(dt)

        screen.fill((245, 245, 245))
        space.debug_draw(draw_options)

        render_buttons(screen, font, launch_button, reset_button)
        render_stats(screen, font, ball_body)

        pygame.display.flip()

    pygame.quit()
    sys.exit()


if __name__ == "__main__":
    main()
