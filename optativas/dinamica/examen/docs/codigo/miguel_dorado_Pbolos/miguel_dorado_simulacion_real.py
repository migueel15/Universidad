from typing import Literal
import pymunk
import pygame
import pymunk.pygame_util
import math

import sys

# escala que vamos a usar: 1 metro = 100 píxeles
PIXELS_PER_METER = 100.0

LANE_LENGTH_M = 18.288
BALL_RADIUS_M = 0.108
LANE_START_X = 80
LANE_END_X = int(LANE_START_X + LANE_LENGTH_M * PIXELS_PER_METER)

WIDTH = 1000
HEIGHT = 700
FPS = 60
CAMERA_ZOOM = 1.5

# BALL_RADIUS_PX = 30
BALL_RADIUS_PX = int(BALL_RADIUS_M * PIXELS_PER_METER)
BALL_MASS = 7.0
BALL_INITIAL_VELOCITY_MPS = 7.0
BALL_FRICTION = 0.8

GROUND_START_FRICTION = 0.04
GROUND_END_FRICTION = 0.5
FRICTION_CHANGE_M = 12
FRICTION_CHANGE_PX = int(LANE_START_X + FRICTION_CHANGE_M * PIXELS_PER_METER)

GROUND_Y = 550


def create_space() -> pymunk.Space:
    space = pymunk.Space()
    space.gravity = (0, 981)
    return space


def create_ground(space: pymunk.Space) -> pymunk.Segment:
    body = space.static_body
    shape = pymunk.Segment(body, (LANE_START_X, GROUND_Y), (LANE_END_X, GROUND_Y), 3)
    shape.friction = GROUND_START_FRICTION
    shape.elasticity = 0.0
    space.add(shape)
    return shape


def create_visual_zone(space: pymunk.Space):
    body = space.static_body
    change_x = LANE_START_X + FRICTION_CHANGE_PX

    visual_shape = pymunk.Segment(
        body,
        (change_x, GROUND_Y),
        (LANE_END_X, GROUND_Y),
        3,
    )
    visual_shape.sensor = True
    visual_shape.elasticity = 0.0
    visual_shape.friction = 0.0
    visual_shape.color = (80, 120, 255, 255)

    space.add(visual_shape)
    return visual_shape


def create_ball(space: pymunk.Space) -> tuple[pymunk.Body, pymunk.Circle]:
    # moment = (2 / 5) * BALL_MASS * (BALL_RADIUS_PX**2) ideal de una bola maciza
    moment = (
        0.55 * BALL_MASS * (BALL_RADIUS_PX**2)
    )  # ajustado para simular una bola real

    body = pymunk.Body(BALL_MASS, moment)
    body.position = (200, GROUND_Y - BALL_RADIUS_PX - 1)
    body.velocity = (BALL_INITIAL_VELOCITY_MPS * PIXELS_PER_METER, 0)
    body.angular_velocity = 20

    shape = pymunk.Circle(body, BALL_RADIUS_PX)
    shape.friction = BALL_FRICTION
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
) -> (
    Literal[
        "RODADURA PURA", "DESLIZAMIENTO PURO", "RODADURA CON DESLIZAMIENTO", "PATINA"
    ]
    | None
):
    if ball_body is not None:
        INITIAL_X = 20
        INITIAL_Y = 80

        # show velocity, angular velocity
        vel_x, vel_y = ball_body.velocity
        speed = math.sqrt(vel_x**2 + vel_y**2)
        ang_vel = ball_body.angular_velocity
        tangential_speed = ang_vel * BALL_RADIUS_PX

        speed_mts = speed / PIXELS_PER_METER

        current_state = (
            "RODADURA PURA"
            if abs(tangential_speed - vel_x) < 1e-1
            else (
                "DESLIZAMIENTO PURO"
                if abs(ang_vel) < 1e-1 and vel_x != 0
                else (
                    "RODADURA CON DESLIZAMIENTO"
                    if abs(vel_x) > abs(tangential_speed)
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

        return current_state


def render_calculated_stats(
    screen: pygame.Surface,
    font: pygame.font.Font,
    time_to_rodadura: float | None,
    velocity_at_rodadura: float | None,
) -> None:

    if time_to_rodadura is not None and velocity_at_rodadura is not None:
        INITIAL_X = 200
        INITIAL_Y = HEIGHT - 130

        teorical_time = (2 * BALL_INITIAL_VELOCITY_MPS) / (
            7 * BALL_FRICTION * GROUND_START_FRICTION * 9.81
        )
        teorical_velocity = (
            BALL_INITIAL_VELOCITY_MPS
            - BALL_FRICTION * GROUND_START_FRICTION * 9.81 * teorical_time
        )

        render_text(
            screen,
            font,
            f"Real",
            (INITIAL_X, INITIAL_Y),
        )

        render_text(
            screen,
            font,
            f"Tiempo: {time_to_rodadura:.2f} s",
            (INITIAL_X, INITIAL_Y + 30),
        )

        render_text(
            screen,
            font,
            f"Velocidad: {velocity_at_rodadura:.2f} m/s",
            (INITIAL_X, INITIAL_Y + 60),
        )

        render_text(
            screen,
            font,
            f"Teórico",
            (INITIAL_X + 400, INITIAL_Y),
        )

        render_text(
            screen,
            font,
            f"Tiempo: {teorical_time:.2f} s",
            (INITIAL_X + 400, INITIAL_Y + 30),
        )

        render_text(
            screen,
            font,
            f"Velocidad: {teorical_velocity:.2f} m/s",
            (INITIAL_X + 400, INITIAL_Y + 60),
        )


def update_camera(ball_body: pymunk.Body | None) -> tuple[float, float]:
    camera_y = GROUND_Y - GROUND_Y / CAMERA_ZOOM

    if ball_body is None:
        return 0.0, camera_y

    visible_width = WIDTH / CAMERA_ZOOM
    target_camera_x = ball_body.position.x - visible_width * 0.35
    max_camera_x = max(0, LANE_END_X - visible_width)
    camera_x = max(0.0, min(target_camera_x, max_camera_x))
    return camera_x, camera_y


def main() -> None:
    ball_body = None
    ball_shape = None
    simulation_running = False

    track_time = False
    elapsed_time = 0.0
    time_to_rodadura = None
    velocity_at_rodadura = None
    camera_x = 0.0

    pygame.init()
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    pygame.display.set_caption("Bolos - Simulación base")
    clock = pygame.time.Clock()
    font = pygame.font.Font(None, 32)

    space = create_space()
    ground = create_ground(space)
    friction_separator = create_visual_zone(space)

    draw_options = pymunk.pygame_util.DrawOptions(screen)

    launch_button = pygame.Rect(20, 20, 140, 50)
    reset_button = pygame.Rect(180, 20, 140, 50)

    running = True

    while running:
        dt = clock.tick(FPS) / 1000.0

        if simulation_running and ball_body is not None:
            elapsed_time += dt

            if track_time and (
                abs(ball_body.angular_velocity * BALL_RADIUS_PX - ball_body.velocity[0])
                < 1
            ):
                track_time = False
                time_to_rodadura = elapsed_time
                velocity_at_rodadura = ball_body.velocity[0] / PIXELS_PER_METER

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
                    elapsed_time = 0.0
                    time_to_rodadura = None
                    camera_x = 0.0

        if simulation_running:
            space.step(dt)

            if (
                ball_body is not None
                and ball_body.position.x >= LANE_END_X - BALL_RADIUS_PX
            ):
                ball_body.position = (LANE_END_X - BALL_RADIUS_PX, ball_body.position.y)
                ball_body.velocity = (0, 0)
                ball_body.angular_velocity = 0
                simulation_running = False
                track_time = False

        camera_x, camera_y = update_camera(ball_body)

        screen.fill((245, 245, 245))
        draw_options.transform = pymunk.Transform.scaling(
            CAMERA_ZOOM
        ) @ pymunk.Transform.translation(-camera_x, -camera_y)
        space.debug_draw(draw_options)

        render_buttons(screen, font, launch_button, reset_button)
        ball_status = render_stats(screen, font, ball_body)

        if ball_status is not None and ball_status == "RODADURA PURA" and ball_body:
            ball_body.apply_force_at_world_point(
                (-800, 0), ball_body.position
            )  # se aplica una fuerza contraria a la bola. Uso world point ya que al rotar la bola tambien afectaria a la direccion de la fuerza aplicada en un point.

        if ball_body is not None:
            if ball_body.position.x < FRICTION_CHANGE_PX:
                ground.friction = GROUND_START_FRICTION
            else:
                ground.friction = GROUND_END_FRICTION

        render_text(screen, font, f"Tiempo: {elapsed_time:.2f} s", (20, HEIGHT - 40))
        render_calculated_stats(screen, font, time_to_rodadura, velocity_at_rodadura)
        render_text(screen, font, f"Friccion pista: {ground.friction:.2f}", (700, 40))

        pygame.display.flip()

    pygame.quit()
    sys.exit()


if __name__ == "__main__":
    main()
