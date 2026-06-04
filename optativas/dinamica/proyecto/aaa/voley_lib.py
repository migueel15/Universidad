from __future__ import annotations

from dataclasses import dataclass, field
import math
from pathlib import Path
import random
from typing import Optional

import pygame
import pymunk
from pymunk import Vec2d


# Official indoor volleyball: 65-67 cm circumference and 260-280 g.
OFFICIAL_BALL_CIRCUMFERENCE_M = 0.66
OFFICIAL_BALL_MASS_KG = 0.270
OFFICIAL_BALL_RADIUS_M = OFFICIAL_BALL_CIRCUMFERENCE_M / (2.0 * math.pi)
COURT_LINE_WIDTH_M = 0.05
BALL_IMAGE_PATH = Path(__file__).resolve().parent / "assets" / "bola.png"
BACKGROUND_IMAGE_PATH = Path(__file__).resolve().parent / "assets" / "fondo.png"
BACKGROUND_OPACITY = round(255 * 0.35)


@dataclass(frozen=True)
class CourtConfig:
    court_length: float = 18.0
    visual_left: float = -3.0
    visual_right: float = 21.0
    visual_bottom: float = -0.3
    net_x: float = 9.0
    net_height: float = 2.43
    serve_x: float = -0.75
    serve_y: float = 2.25
    attack_left_x: float = 3.0
    attack_right_x: float = 15.0
    ground_radius: float = 0.04
    net_radius: float = 0.025


@dataclass(frozen=True)
class BallConfig:
    mass: float = OFFICIAL_BALL_MASS_KG
    radius: float = OFFICIAL_BALL_RADIUS_M
    elasticity: float = 0.82
    friction: float = 0.55


@dataclass(frozen=True)
class Camera:
    width: int = 1400
    height: int = 720
    margin_left: int = 16
    margin_right: int = 16
    margin_top: int = 132
    margin_bottom: int = 70
    world_left: float = -1.70
    world_right: float = 19.20
    world_bottom: float = -0.3
    world_top: float = 7.5

    @property
    def scale(self) -> float:
        usable_w = self.width - self.margin_left - self.margin_right
        usable_h = self.height - self.margin_top - self.margin_bottom
        return min(
            usable_w / (self.world_right - self.world_left),
            usable_h / (self.world_top - self.world_bottom),
        )

    def to_screen(self, point: Vec2d | tuple[float, float]) -> tuple[int, int]:
        p = Vec2d(*point)
        x = self.margin_left + (p.x - self.world_left) * self.scale
        y = self.height - self.margin_bottom - (p.y - self.world_bottom) * self.scale
        return int(x), int(y)

    def length_to_px(self, length_m: float) -> int:
        return max(1, round(length_m * self.scale))


@dataclass(frozen=True)
class ServeConfig:
    key: str
    name: str
    speed: float
    angle_deg: float
    spin: float
    cd: float
    magnus_k: float
    rotational_drag_cm: float
    wind_base: tuple[float, float]
    wind_noise: float
    random_lift_force: float
    color: tuple[int, int, int]
    launch_y: Optional[float] = None
    jump_serve: bool = False


SERVE_PRESETS: dict[str, ServeConfig] = {
    "float": ServeConfig(
        key="float",
        name="Saque flotante",
        speed=17.0,
        angle_deg=14.0,
        spin=0.35,
        cd=0.47,
        magnus_k=0.04,
        rotational_drag_cm=0.010,
        wind_base=(0.0, 0.0),
        wind_noise=3.0,
        random_lift_force=1.0,
        color=(245, 213, 92),
        launch_y=2.45,
    ),
    "topspin": ServeConfig(
        key="topspin",
        name="Saque topspin",
        speed=25.5,
        angle_deg=6.0,
        spin=-120.0,
        cd=0.44,
        magnus_k=0.60,
        rotational_drag_cm=0.020,
        wind_base=(0.0, 0.0),
        wind_noise=0.20,
        random_lift_force=0.0,
        color=(231, 76, 60),
        launch_y=3.10,
        jump_serve=True,
    ),
    "globo": ServeConfig(
        key="globo",
        name="Saque globo",
        speed=13.5,
        angle_deg=59.0,
        spin=-5.0,
        cd=0.47,
        magnus_k=0.12,
        rotational_drag_cm=0.012,
        wind_base=(0.0, 0.0),
        wind_noise=0.15,
        random_lift_force=0.0,
        color=(52, 152, 219),
    ),
}


@dataclass(frozen=True)
class PlayerPose:
    hip: tuple[float, float]
    head: tuple[float, float]
    shoulder: tuple[float, float]
    hand: tuple[float, float]
    foot1: tuple[float, float]
    foot2: tuple[float, float]
    airborne: bool = False
    hitting: bool = False


PLAYER_HEIGHT_M = 1.90
PLAYER_HEAD_RADIUS_M = 0.11


@dataclass
class ServeResult:
    serve_name: str
    result: str
    flight_time: float
    landing_x: Optional[float]
    max_height: float
    net_cross_height: Optional[float]
    net_clearance: Optional[float]
    final_speed: float


@dataclass
class ServeState:
    config: Optional[ServeConfig] = None
    active: bool = False
    elapsed: float = 0.0
    max_height: float = 0.0
    trajectory: list[Vec2d] = field(default_factory=list)
    result: Optional[ServeResult] = None
    previous_position: Optional[Vec2d] = None
    net_checked: bool = False
    net_cross_height: Optional[float] = None
    net_clearance: Optional[float] = None
    trajectory_timer: float = 0.0

    def start(self, config: ServeConfig, start_position: Vec2d) -> None:
        self.config = config
        self.active = True
        self.elapsed = 0.0
        self.max_height = start_position.y
        self.trajectory = [Vec2d(start_position.x, start_position.y)]
        self.result = None
        self.previous_position = Vec2d(start_position.x, start_position.y)
        self.net_checked = False
        self.net_cross_height = None
        self.net_clearance = None
        self.trajectory_timer = 0.0

    def update(
        self, ball: Volleyball, court: CourtConfig, dt: float
    ) -> Optional[ServeResult]:
        if not self.active or self.config is None or self.previous_position is None:
            return None

        body = ball.body
        pos = Vec2d(body.position.x, body.position.y)
        prev = self.previous_position
        self.elapsed += dt
        self.max_height = max(self.max_height, pos.y)
        self.trajectory_timer += dt

        if self.trajectory_timer >= 1.0 / 45.0:
            self.trajectory.append(Vec2d(pos.x, pos.y))
            self.trajectory_timer = 0.0

        if not self.net_checked and prev.x < court.net_x <= pos.x:
            t = (court.net_x - prev.x) / max(pos.x - prev.x, 1e-9)
            y_net = prev.y + (pos.y - prev.y) * t
            clearance = y_net - court.net_height
            self.net_checked = True
            self.net_cross_height = y_net
            self.net_clearance = clearance
            if y_net - ball.radius <= court.net_height:
                result = ServeResult(
                    serve_name=self.config.name,
                    result="RED",
                    flight_time=self.elapsed,
                    landing_x=None,
                    max_height=self.max_height,
                    net_cross_height=y_net,
                    net_clearance=clearance,
                    final_speed=body.velocity.length,
                )
                self._finish(result)
                return result

        ground_contact_y = ball.radius + court.ground_radius
        if self.elapsed > 0.06 and prev.y > ground_contact_y >= pos.y:
            t = (prev.y - ground_contact_y) / max(prev.y - pos.y, 1e-9)
            landing_x = prev.x + (pos.x - prev.x) * t
            if landing_x < court.net_x:
                result_text = "NO PASA"
            elif landing_x <= court.court_length:
                result_text = "DENTRO"
            else:
                result_text = "FUERA"

            result = ServeResult(
                serve_name=self.config.name,
                result=result_text,
                flight_time=self.elapsed,
                landing_x=landing_x,
                max_height=self.max_height,
                net_cross_height=self.net_cross_height,
                net_clearance=self.net_clearance,
                final_speed=body.velocity.length,
            )

            self.trajectory.append(Vec2d(landing_x, ground_contact_y))
            self._finish(result)
            return result

        self.previous_position = pos
        return None

    def _finish(self, result: ServeResult) -> None:
        self.result = result
        self.active = False


class TopspinJumpServeController:
    toss_duration = 0.45
    approach_duration = 0.52
    jump_duration = 0.36
    landing_duration = 0.45

    def __init__(self, court: CourtConfig, config: ServeConfig):
        self.toss_spin = config.spin * 0.25
        self.toss_start_position = Vec2d(court.serve_x, court.serve_y)
        self.hit_position = Vec2d(0.10, config.launch_y or 3.10)
        self.phase = "idle"
        self.elapsed = 0.0
        self.active = False
        self.hit_triggered = False
        self.pose = self._pose_at(0.0)

    @property
    def preparation_duration(self) -> float:
        return self.toss_duration + self.approach_duration + self.jump_duration

    @property
    def preparation_time(self) -> float:
        return min(self.elapsed, self.preparation_duration)

    def start(self, ball: Volleyball) -> None:
        self.phase = "toss"
        self.elapsed = 0.0
        self.active = True
        self.hit_triggered = False
        self.pose = self._pose_at(0.0)
        ball.reset((self.toss_start_position.x, self.toss_start_position.y))

    def reset(self) -> None:
        self.phase = "idle"
        self.elapsed = 0.0
        self.active = False
        self.hit_triggered = False
        self.pose = self._pose_at(0.0)

    def finish(self) -> None:
        self.phase = "finished"
        self.active = False

    def update(self, ball: Volleyball, dt: float) -> bool:
        if not self.active:
            return False

        previous_elapsed = self.elapsed
        self.elapsed += dt
        if self.hit_triggered:
            self.phase = "flight"
            self.pose = self._pose_at(self.elapsed)
            return False

        if self.elapsed >= self.preparation_duration:
            self.elapsed = self.preparation_duration
            self.phase = "hit"
            self.hit_triggered = True
            self.pose = self._pose_at(self.elapsed)
            rotate_dt = self.preparation_duration - previous_elapsed
            self._place_ball(ball, self.hit_position, self.toss_spin, rotate_dt)
            return True

        self.phase = self._phase_for_time(self.elapsed)
        self.pose = self._pose_at(self.elapsed)
        self._place_ball(ball, self._ball_position_at(self.elapsed), self.toss_spin, dt)
        return False

    def phase_label(self) -> str:
        labels = {
            "idle": "inactivo",
            "toss": "lanzamiento",
            "approach": "carrera",
            "jump": "salto",
            "hit": "golpeo",
            "flight": "vuelo",
            "finished": "terminado",
        }
        return labels.get(self.phase, self.phase)

    def _place_ball(
        self,
        ball: Volleyball,
        position: Vec2d,
        angular_velocity: float = 0.0,
        rotate_dt: float = 0.0,
    ) -> None:
        ball.body.position = (position.x, position.y)
        ball.body.velocity = (0.0, 0.0)
        ball.body.force = (0.0, 0.0)
        ball.body.angle += angular_velocity * max(0.0, rotate_dt)
        ball.body.angular_velocity = angular_velocity
        ball.body.torque = 0.0

    def _ball_position_at(self, elapsed: float) -> Vec2d:
        u = self._smoothstep(elapsed / self.preparation_duration)
        x = self.toss_start_position.x + (
            self.hit_position.x - self.toss_start_position.x
        ) * u
        y = self.toss_start_position.y + (
            self.hit_position.y - self.toss_start_position.y
        ) * u
        y += 1.05 * math.sin(math.pi * u)
        return Vec2d(x, y)

    def _pose_at(self, elapsed: float) -> PlayerPose:
        prep_u = self._smoothstep(elapsed / self.preparation_duration)
        hip_x = -1.20 + 0.92 * prep_u
        jump_start = self.toss_duration + self.approach_duration

        if elapsed <= jump_start:
            lift = 0.0
        elif elapsed <= self.preparation_duration:
            jump_u = self._smoothstep((elapsed - jump_start) / self.jump_duration)
            lift = 0.70 * jump_u
        else:
            land_u = self._smoothstep(
                (elapsed - self.preparation_duration) / self.landing_duration
            )
            lift = 0.70 * (1.0 - land_u)

        hip_y = 0.95 + lift
        head_y = PLAYER_HEIGHT_M - PLAYER_HEAD_RADIUS_M + lift
        shoulder = (hip_x + 0.07, 1.52 + lift)
        hitting = (
            self.preparation_duration - 0.08
            <= elapsed
            <= self.preparation_duration + 0.10
        )
        if hitting:
            hand = (self.hit_position.x, self.hit_position.y)
        else:
            reach_u = self._smoothstep(
                (elapsed - self.toss_duration * 0.65)
                / (self.preparation_duration - self.toss_duration * 0.65)
            )
            hand = (hip_x + 0.38, shoulder[1] + 0.70 + 0.28 * reach_u)

        foot_y = 0.0 if lift <= 0.02 else lift
        return PlayerPose(
            hip=(hip_x, hip_y),
            head=(hip_x, head_y),
            shoulder=shoulder,
            hand=hand,
            foot1=(hip_x - 0.30, foot_y),
            foot2=(hip_x + 0.28, foot_y + (0.08 if lift > 0.02 else 0.0)),
            airborne=lift > 0.02,
            hitting=hitting,
        )

    def _phase_for_time(self, elapsed: float) -> str:
        if elapsed < self.toss_duration:
            return "toss"
        if elapsed < self.toss_duration + self.approach_duration:
            return "approach"
        return "jump"

    def _smoothstep(self, value: float) -> float:
        t = max(0.0, min(1.0, value))
        return t * t * (3.0 - 2.0 * t)


class Volleyball:
    def __init__(
        self, space: pymunk.Space, config: BallConfig, position: tuple[float, float]
    ):
        self.space = space
        self.radius = config.radius
        self.image = pygame.image.load(str(BALL_IMAGE_PATH))
        if pygame.display.get_surface() is not None:
            self.image = self.image.convert_alpha()
        self.scaled_image: pygame.Surface | None = None
        self.scaled_image_diameter = 0
        moment = pymunk.moment_for_circle(config.mass, 0.0, config.radius)
        self.body = pymunk.Body(config.mass, moment)
        self.body.position = position
        self.shape = pymunk.Circle(self.body, config.radius)
        self.shape.elasticity = config.elasticity
        self.shape.friction = config.friction
        self.space.add(self.body, self.shape)

    def reset(self, position: tuple[float, float], angle: float = 0.0) -> None:
        if self.body not in self.space.bodies:
            self.space.add(self.body, self.shape)
        self.body.body_type = pymunk.Body.DYNAMIC
        self.body.position = position
        self.body.velocity = (0.0, 0.0)
        self.body.force = (0.0, 0.0)
        self.body.angle = angle
        self.body.angular_velocity = 0.0
        self.body.torque = 0.0

    def launch(self, config: ServeConfig) -> None:
        angle = math.radians(config.angle_deg)
        self.body.velocity = (
            config.speed * math.cos(angle),
            config.speed * math.sin(angle),
        )
        self.body.angular_velocity = config.spin

    def draw(self, screen: pygame.Surface, camera: Camera) -> None:
        center = camera.to_screen(self.body.position)
        diameter_px = camera.length_to_px(self.radius * 2.0)
        if self.scaled_image is None or self.scaled_image_diameter != diameter_px:
            self.scaled_image = pygame.transform.smoothscale(
                self.image, (diameter_px, diameter_px)
            )
            self.scaled_image_diameter = diameter_px

        angle_deg = math.degrees(self.body.angle) % 360.0
        rotated = pygame.transform.rotozoom(self.scaled_image, angle_deg, 1.0)
        screen.blit(rotated, rotated.get_rect(center=center))


class VolleyCourt:
    def __init__(self, space: pymunk.Space, config: CourtConfig):
        self.space = space
        self.config = config
        self.background_image = pygame.image.load(str(BACKGROUND_IMAGE_PATH))
        if pygame.display.get_surface() is not None:
            self.background_image = self.background_image.convert()
        self.background_surface: pygame.Surface | None = None
        self.background_size = (0, 0)
        self._create_static_world()

    def _create_static_world(self) -> None:
        c = self.config
        ground = pymunk.Segment(
            self.space.static_body,
            (c.visual_left, 0.0),
            (c.visual_right, 0.0),
            c.ground_radius,
        )
        ground.elasticity = 0.72
        ground.friction = 0.90
        net = pymunk.Segment(
            self.space.static_body,
            (c.net_x, 0.0),
            (c.net_x, c.net_height),
            c.net_radius,
        )
        net.elasticity = 0.18
        net.friction = 0.65
        self.space.add(ground, net)

    def draw(
        self,
        screen: pygame.Surface,
        camera: Camera,
        jumping_serve: bool = False,
        player_pose: Optional[PlayerPose] = None,
    ) -> None:
        self._draw_background(screen)
        self._draw_floor(screen, camera)
        self._draw_court_lines(screen, camera)
        self._draw_net(screen, camera)
        self._draw_player(screen, camera, jumping_serve, player_pose)

    def _draw_background(self, screen: pygame.Surface) -> None:
        screen_size = screen.get_size()
        if self.background_surface is None or self.background_size != screen_size:
            image_w, image_h = self.background_image.get_size()
            scale = max(screen_size[0] / image_w, screen_size[1] / image_h)
            scaled_size = (math.ceil(image_w * scale), math.ceil(image_h * scale))
            scaled = pygame.transform.smoothscale(self.background_image, scaled_size)
            self.background_surface = pygame.Surface(screen_size)
            self.background_surface.fill((216, 235, 247))
            scaled.set_alpha(BACKGROUND_OPACITY)
            offset = (
                (screen_size[0] - scaled_size[0]) // 2,
                (screen_size[1] - scaled_size[1]) // 2,
            )
            self.background_surface.blit(scaled, offset)
            self.background_size = screen_size

        screen.blit(self.background_surface, (0, 0))

    def _draw_floor(self, screen: pygame.Surface, camera: Camera) -> None:
        left = camera.to_screen((self.config.visual_left, 0.0))
        right = camera.to_screen((self.config.visual_right, 0.0))
        bottom = camera.to_screen((self.config.visual_right, self.config.visual_bottom))
        pygame.draw.rect(
            screen,
            (222, 169, 104),
            (left[0], left[1], right[0] - left[0], bottom[1] - left[1]),
        )
        ground_width = camera.length_to_px(self.config.ground_radius * 2.0)
        pygame.draw.line(screen, (110, 74, 42), left, right, ground_width)

    def _draw_court_lines(self, screen: pygame.Surface, camera: Camera) -> None:
        c = self.config
        court_line_width = camera.length_to_px(COURT_LINE_WIDTH_M)
        for x in (0.0, c.attack_left_x, c.net_x, c.attack_right_x, c.court_length):
            p1 = camera.to_screen((x, 0.0))
            line_height = 0.22 if x in (c.attack_left_x, c.attack_right_x) else 0.35
            p2 = camera.to_screen((x, line_height))
            pygame.draw.line(screen, (255, 255, 255), p1, p2, court_line_width)

        start = camera.to_screen((0.0, 0.0))
        end = camera.to_screen((c.court_length, 0.0))
        pygame.draw.line(screen, (255, 255, 255), start, end, court_line_width)

        serve_a = camera.to_screen((c.visual_left, 0.0))
        serve_b = camera.to_screen((0.0, 0.0))
        pygame.draw.line(screen, (200, 90, 55), serve_a, serve_b, 5)

    def _draw_net(self, screen: pygame.Surface, camera: Camera) -> None:
        c = self.config
        base = camera.to_screen((c.net_x, 0.0))
        top = camera.to_screen((c.net_x, c.net_height))
        net_width = camera.length_to_px(c.net_radius * 2.0)
        pygame.draw.line(screen, (35, 35, 35), base, top, net_width)
        for i in range(9):
            y = c.net_height * i / 8
            p1 = camera.to_screen((c.net_x - 0.08, y))
            p2 = camera.to_screen((c.net_x + 0.08, y))
            pygame.draw.line(screen, (80, 80, 80), p1, p2, 1)

    def _draw_player(
        self,
        screen: pygame.Surface,
        camera: Camera,
        jumping_serve: bool,
        player_pose: Optional[PlayerPose],
    ) -> None:
        body_color = (40, 40, 40)
        if player_pose is not None:
            hip = camera.to_screen(player_pose.hip)
            head = camera.to_screen(player_pose.head)
            shoulder = camera.to_screen(player_pose.shoulder)
            hand = camera.to_screen(player_pose.hand)
            foot1 = camera.to_screen(player_pose.foot1)
            foot2 = camera.to_screen(player_pose.foot2)
            if player_pose.airborne:
                shadow = camera.to_screen((player_pose.hip[0], 0.02))
                pygame.draw.ellipse(
                    screen,
                    (115, 80, 48),
                    (shadow[0] - 24, shadow[1] - 5, 48, 10),
                    1,
                )
            if player_pose.hitting:
                body_color = (125, 28, 22)
        elif jumping_serve:
            hip = camera.to_screen((-0.28, 1.65))
            head = camera.to_screen((-0.28, 2.49))
            shoulder = camera.to_screen((-0.21, 2.22))
            hand = camera.to_screen((0.10, 3.10))
            foot1 = camera.to_screen((-0.58, 0.70))
            foot2 = camera.to_screen((0.00, 0.78))
        else:
            hip = camera.to_screen((-1.05, 0.95))
            head = camera.to_screen((-1.05, PLAYER_HEIGHT_M - PLAYER_HEAD_RADIUS_M))
            shoulder = camera.to_screen((-0.98, 1.52))
            hand = camera.to_screen((-0.68, 2.20))
            foot1 = camera.to_screen((-1.32, 0.0))
            foot2 = camera.to_screen((-0.78, 0.0))
        head_radius = camera.length_to_px(PLAYER_HEAD_RADIUS_M)
        pygame.draw.circle(screen, body_color, head, head_radius)
        pygame.draw.line(screen, body_color, head, hip, 5)
        pygame.draw.line(screen, body_color, hip, foot1, 5)
        pygame.draw.line(screen, body_color, hip, foot2, 5)
        pygame.draw.line(screen, body_color, shoulder, hand, 5)


class AirModel:
    def __init__(self, rho: float = 1.225):
        self.rho = rho
        self.gust = Vec2d(0.0, 0.0)
        self.gust_target = Vec2d(0.0, 0.0)
        self.gust_timer = 0.0
        self.last_wind = Vec2d(0.0, 0.0)

    def reset(self) -> None:
        self.gust = Vec2d(0.0, 0.0)
        self.gust_target = Vec2d(0.0, 0.0)
        self.gust_timer = 0.0
        self.last_wind = Vec2d(0.0, 0.0)

    def apply(self, ball: Volleyball, config: ServeConfig, dt: float) -> None:
        wind = self._wind(config, dt)
        v = Vec2d(ball.body.velocity.x, ball.body.velocity.y)
        v_rel = v - wind
        speed = v_rel.length
        if speed <= 1e-6:
            return

        area = math.pi * ball.radius * ball.radius
        drag = -0.5 * self.rho * config.cd * area * speed * v_rel
        ball.body.apply_force_at_world_point(drag, ball.body.position)

        if abs(ball.body.angular_velocity) > 1e-6 and config.magnus_k > 0:
            spin_ratio = ball.radius * abs(ball.body.angular_velocity) / max(speed, 1e-6)
            cm = config.magnus_k * spin_ratio / (2.0 + spin_ratio)
            direction = Vec2d(-v_rel.y, v_rel.x)
            if direction.length > 1e-6:
                direction = direction.normalized() * math.copysign(1.0, ball.body.angular_velocity)
                magnus = 0.5 * self.rho * speed * speed * cm * area * direction
                ball.body.apply_force_at_world_point(magnus, ball.body.position)

        if config.random_lift_force > 0:
            noise = Vec2d(-v_rel.y, v_rel.x)
            if noise.length > 1e-6:
                noise = noise.normalized()
                strength = random.uniform(-config.random_lift_force, config.random_lift_force)
                ball.body.apply_force_at_world_point(noise * strength, ball.body.position)

        omega = ball.body.angular_velocity
        if abs(omega) > 1e-6 and config.rotational_drag_cm > 0:
            torque_mag = 0.5 * self.rho * omega * omega * ball.radius**5 * config.rotational_drag_cm
            ball.body.torque += -math.copysign(torque_mag, omega)

    def _wind(self, config: ServeConfig, dt: float) -> Vec2d:
        self.gust_timer -= dt
        if self.gust_timer <= 0:
            self.gust_timer = random.uniform(0.10, 0.22)
            self.gust_target = Vec2d(
                random.uniform(-config.wind_noise, config.wind_noise),
                random.uniform(-0.35 * config.wind_noise, 0.35 * config.wind_noise),
            )
        alpha = min(1.0, dt * 8.0)
        self.gust = self.gust + (self.gust_target - self.gust) * alpha
        self.last_wind = Vec2d(*config.wind_base) + self.gust
        return self.last_wind


def setup_space() -> pymunk.Space:
    space = pymunk.Space()
    space.gravity = (0.0, -9.81)
    space.damping = 1.0
    space.collision_slop = 0.01
    return space


def draw_trajectory(
    screen: pygame.Surface,
    camera: Camera,
    points: list[Vec2d],
    color: tuple[int, int, int],
) -> None:
    if len(points) < 2:
        return
    screen_points = [camera.to_screen(p) for p in points]
    pygame.draw.lines(screen, color, False, screen_points, 2)
    for p in screen_points[::8]:
        pygame.draw.circle(screen, color, p, 2)


def draw_text_block(
    screen: pygame.Surface,
    font: pygame.font.Font,
    lines: list[str],
    x: int,
    y: int,
    color: tuple[int, int, int] = (25, 25, 25),
    line_height: int = 22,
) -> None:
    for index, line in enumerate(lines):
        surface = font.render(line, True, color)
        screen.blit(surface, (x, y + index * line_height))
