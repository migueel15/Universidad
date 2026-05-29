from __future__ import annotations

from dataclasses import dataclass, field
from enum import IntEnum
import math
import random
from typing import Optional

import pygame
import pymunk
from pymunk import Vec2d


class CollisionType(IntEnum):
    BALL = 1
    GROUND = 2
    NET = 3


@dataclass(frozen=True)
class CourtConfig:
    court_length: float = 18.0
    half_length: float = 9.0
    visual_left: float = -3.0
    visual_right: float = 21.0
    visual_bottom: float = -0.3
    visual_top: float = 8.0
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
    mass: float = 0.270
    radius: float = 0.105
    elasticity: float = 0.58
    friction: float = 0.55


@dataclass(frozen=True)
class Camera:
    width: int = 1400
    height: int = 720
    margin_left: int = 70
    margin_right: int = 360
    margin_top: int = 45
    margin_bottom: int = 75
    world_left: float = -3.0
    world_right: float = 21.0
    world_bottom: float = -0.3
    world_top: float = 8.0

    @property
    def scale(self) -> float:
        usable_w = self.width - self.margin_left - self.margin_right
        usable_h = self.height - self.margin_top - self.margin_bottom
        return min(usable_w / (self.world_right - self.world_left), usable_h / (self.world_top - self.world_bottom))

    def to_screen(self, point: Vec2d | tuple[float, float]) -> tuple[int, int]:
        p = Vec2d(*point)
        x = self.margin_left + (p.x - self.world_left) * self.scale
        y = self.height - self.margin_bottom - (p.y - self.world_bottom) * self.scale
        return int(x), int(y)

    def length_to_px(self, length_m: float) -> int:
        return int(length_m * self.scale)


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
    description: str
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
        description="Poca rotacion: hace una parabola leve, pero el viento y las rafagas deforman la trayectoria.",
        launch_y=2.45,
    ),
    "topspin": ServeConfig(
        key="topspin",
        name="Saque topspin",
        speed=22.0,
        angle_deg=8.0,
        spin=-88.0,
        cd=0.44,
        magnus_k=0.60,
        rotational_drag_cm=0.020,
        wind_base=(0.0, 0.0),
        wind_noise=0.20,
        random_lift_force=0.0,
        color=(231, 76, 60),
        description="Alta velocidad, salto y spin hacia delante para que el balon caiga antes.",
        launch_y=3.20,
        jump_serve=True,
    ),
    "globo": ServeConfig(
        key="globo",
        name="Saque globo",
        speed=12.4,
        angle_deg=46.0,
        spin=-5.0,
        cd=0.47,
        magnus_k=0.12,
        rotational_drag_cm=0.012,
        wind_base=(0.0, 0.0),
        wind_noise=0.15,
        random_lift_force=0.0,
        color=(52, 152, 219),
        description="Menor velocidad y angulo alto: domina la parabola de la gravedad.",
    ),
}


@dataclass
class ServeResult:
    serve_name: str
    result: str
    flight_time: float
    landing_x: Optional[float]
    max_height: float
    net_cross_height: Optional[float]
    net_clearance: Optional[float]
    speed_at_net: Optional[float]
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
    landed: bool = False
    net_hit: bool = False
    net_cross_height: Optional[float] = None
    net_clearance: Optional[float] = None
    speed_at_net: Optional[float] = None
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
        self.landed = False
        self.net_hit = False
        self.net_cross_height = None
        self.net_clearance = None
        self.speed_at_net = None
        self.trajectory_timer = 0.0

    def update(self, ball: Volleyball, court: CourtConfig, dt: float) -> Optional[ServeResult]:
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
            speed_net = body.velocity.length
            clearance = y_net - court.net_height
            self.net_checked = True
            self.net_cross_height = y_net
            self.net_clearance = clearance
            self.speed_at_net = speed_net
            if y_net - ball.radius <= court.net_height:
                self.net_hit = True
                result = ServeResult(
                    serve_name=self.config.name,
                    result="RED",
                    flight_time=self.elapsed,
                    landing_x=None,
                    max_height=self.max_height,
                    net_cross_height=y_net,
                    net_clearance=clearance,
                    speed_at_net=speed_net,
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
                speed_at_net=self.speed_at_net,
                final_speed=body.velocity.length,
            )

            if self.net_checked and result.net_cross_height is None:
                for a, b in zip(self.trajectory, self.trajectory[1:]):
                    if a.x <= court.net_x <= b.x:
                        tt = (court.net_x - a.x) / max(b.x - a.x, 1e-9)
                        y_net = a.y + (b.y - a.y) * tt
                        result.net_cross_height = y_net
                        result.net_clearance = y_net - court.net_height
                        break

            self.landed = True
            self.trajectory.append(Vec2d(landing_x, ground_contact_y))
            self._finish(result)
            return result

        self.previous_position = pos
        return None

    def _finish(self, result: ServeResult) -> None:
        self.result = result
        self.active = False


class Volleyball:
    def __init__(self, space: pymunk.Space, config: BallConfig, position: tuple[float, float]):
        self.space = space
        self.mass = config.mass
        self.radius = config.radius
        moment = pymunk.moment_for_circle(config.mass, 0.0, config.radius)
        self.body = pymunk.Body(config.mass, moment)
        self.body.position = position
        self.shape = pymunk.Circle(self.body, config.radius)
        self.shape.elasticity = config.elasticity
        self.shape.friction = config.friction
        self.shape.collision_type = CollisionType.BALL
        self.space.add(self.body, self.shape)

    def reset(self, position: tuple[float, float]) -> None:
        if self.body not in self.space.bodies:
            self.space.add(self.body, self.shape)
        self.body.body_type = pymunk.Body.DYNAMIC
        self.body.position = position
        self.body.velocity = (0.0, 0.0)
        self.body.force = (0.0, 0.0)
        self.body.angle = 0.0
        self.body.angular_velocity = 0.0
        self.body.torque = 0.0

    def launch(self, config: ServeConfig) -> None:
        angle = math.radians(config.angle_deg)
        self.body.velocity = (config.speed * math.cos(angle), config.speed * math.sin(angle))
        self.body.angular_velocity = config.spin

    def freeze(self) -> None:
        self.body.velocity = (0.0, 0.0)
        self.body.force = (0.0, 0.0)
        self.body.angular_velocity = 0.0
        self.body.torque = 0.0

    def draw(self, screen: pygame.Surface, camera: Camera) -> None:
        center = camera.to_screen(self.body.position)
        radius_px = max(4, camera.length_to_px(self.radius))
        pygame.draw.circle(screen, (245, 245, 245), center, radius_px)
        pygame.draw.circle(screen, (35, 35, 35), center, radius_px, 2)
        angle = self.body.angle
        p1 = Vec2d(math.cos(angle), math.sin(angle)) * self.radius * 0.85
        p2 = Vec2d(-math.sin(angle), math.cos(angle)) * self.radius * 0.65
        for d in (p1, -p1, p2, -p2):
            end = camera.to_screen(Vec2d(self.body.position.x, self.body.position.y) + d)
            pygame.draw.line(screen, (35, 35, 35), center, end, 2)


class VolleyCourt:
    def __init__(self, space: pymunk.Space, config: CourtConfig):
        self.space = space
        self.config = config
        self.static_shapes: list[pymunk.Shape] = []
        self._create_static_world()

    def _create_static_world(self) -> None:
        c = self.config
        ground = pymunk.Segment(self.space.static_body, (c.visual_left, 0.0), (c.visual_right, 0.0), c.ground_radius)
        ground.elasticity = 0.35
        ground.friction = 0.90
        ground.collision_type = CollisionType.GROUND
        net = pymunk.Segment(self.space.static_body, (c.net_x, 0.0), (c.net_x, c.net_height), c.net_radius)
        net.elasticity = 0.18
        net.friction = 0.65
        net.collision_type = CollisionType.NET
        self.space.add(ground, net)
        self.static_shapes.extend([ground, net])

    def draw(self, screen: pygame.Surface, camera: Camera, font: pygame.font.Font, jumping_serve: bool = False) -> None:
        c = self.config
        screen.fill((216, 235, 247))
        self._draw_floor(screen, camera)
        self._draw_court_lines(screen, camera, font)
        self._draw_net(screen, camera, font)
        self._draw_player(screen, camera, jumping_serve)

    def _draw_floor(self, screen: pygame.Surface, camera: Camera) -> None:
        left = camera.to_screen((self.config.visual_left, 0.0))
        right = camera.to_screen((self.config.visual_right, 0.0))
        bottom = camera.to_screen((self.config.visual_right, self.config.visual_bottom))
        pygame.draw.rect(screen, (222, 169, 104), (left[0], left[1], right[0] - left[0], bottom[1] - left[1]))
        pygame.draw.line(screen, (110, 74, 42), left, right, 4)

    def _draw_court_lines(self, screen: pygame.Surface, camera: Camera, font: pygame.font.Font) -> None:
        c = self.config
        for x, label in [
            (0.0, "fondo"),
            (c.attack_left_x, "3 m"),
            (c.net_x, "red"),
            (c.attack_right_x, "3 m"),
            (c.court_length, "fondo"),
        ]:
            p1 = camera.to_screen((x, 0.0))
            p2 = camera.to_screen((x, 0.22 if x in (c.attack_left_x, c.attack_right_x) else 0.35))
            pygame.draw.line(screen, (255, 255, 255), p1, p2, 3)
            text = font.render(label, True, (45, 45, 45))
            screen.blit(text, (p1[0] - text.get_width() // 2, p1[1] + 8))

        start = camera.to_screen((0.0, 0.0))
        end = camera.to_screen((c.court_length, 0.0))
        pygame.draw.line(screen, (255, 255, 255), start, end, 2)

        serve_a = camera.to_screen((c.visual_left, 0.0))
        serve_b = camera.to_screen((0.0, 0.0))
        pygame.draw.line(screen, (200, 90, 55), serve_a, serve_b, 5)
        text = font.render("zona de saque", True, (120, 55, 35))
        screen.blit(text, (serve_a[0] + 20, serve_a[1] + 30))

    def _draw_net(self, screen: pygame.Surface, camera: Camera, font: pygame.font.Font) -> None:
        c = self.config
        base = camera.to_screen((c.net_x, 0.0))
        top = camera.to_screen((c.net_x, c.net_height))
        pygame.draw.line(screen, (35, 35, 35), base, top, 5)
        for i in range(9):
            y = c.net_height * i / 8
            p1 = camera.to_screen((c.net_x - 0.08, y))
            p2 = camera.to_screen((c.net_x + 0.08, y))
            pygame.draw.line(screen, (80, 80, 80), p1, p2, 1)
        text = font.render(f"{c.net_height:.2f} m", True, (35, 35, 35))
        screen.blit(text, (top[0] + 8, top[1] - 20))

    def _draw_player(self, screen: pygame.Surface, camera: Camera, jumping_serve: bool) -> None:
        if jumping_serve:
            hip = camera.to_screen((-1.05, 1.25))
            head = camera.to_screen((-1.05, 2.02))
            shoulder = camera.to_screen((-0.98, 1.72))
            hand = camera.to_screen((-0.68, 3.12))
            foot1 = camera.to_screen((-1.32, 0.42))
            foot2 = camera.to_screen((-0.78, 0.55))
        else:
            hip = camera.to_screen((-1.05, 0.95))
            head = camera.to_screen((-1.05, 1.72))
            shoulder = camera.to_screen((-0.98, 1.42))
            hand = camera.to_screen((-0.68, 2.20))
            foot1 = camera.to_screen((-1.32, 0.0))
            foot2 = camera.to_screen((-0.78, 0.0))
        pygame.draw.circle(screen, (40, 40, 40), head, 13)
        pygame.draw.line(screen, (40, 40, 40), head, hip, 5)
        pygame.draw.line(screen, (40, 40, 40), hip, foot1, 5)
        pygame.draw.line(screen, (40, 40, 40), hip, foot2, 5)
        pygame.draw.line(screen, (40, 40, 40), shoulder, hand, 5)
        pygame.draw.circle(screen, (245, 245, 245), hand, 6)


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


def draw_trajectory(screen: pygame.Surface, camera: Camera, points: list[Vec2d], color: tuple[int, int, int]) -> None:
    if len(points) < 2:
        return
    screen_points = [camera.to_screen(p) for p in points]
    pygame.draw.lines(screen, color, False, screen_points, 2)
    for p in screen_points[::8]:
        pygame.draw.circle(screen, color, p, 2)


def format_result(result: Optional[ServeResult]) -> list[str]:
    if result is None:
        return ["Sin resultado"]
    landing = "-" if result.landing_x is None else f"{result.landing_x:.2f} m"
    net = "-" if result.net_cross_height is None else f"{result.net_cross_height:.2f} m"
    clearance = "-" if result.net_clearance is None else f"{result.net_clearance:.2f} m"
    speed_net = "-" if result.speed_at_net is None else f"{result.speed_at_net:.2f} m/s"
    return [
        f"Resultado: {result.result}",
        f"Tiempo: {result.flight_time:.2f} s",
        f"Caida x: {landing}",
        f"Altura max: {result.max_height:.2f} m",
        f"Altura en red: {net}",
        f"Margen red: {clearance}",
        f"Vel. en red: {speed_net}",
        f"Vel. final: {result.final_speed:.2f} m/s",
    ]


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
