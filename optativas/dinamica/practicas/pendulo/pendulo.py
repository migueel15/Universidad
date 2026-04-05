from __future__ import annotations

import argparse
import math
from dataclasses import dataclass
import tkinter as tk

import pymunk
from pymunk import Vec2d


@dataclass(frozen=True)
class PendulumState:
    angle: float
    angular_velocity: float


class DampedPendulum:
    """Pendulo rigido con rozamiento tangencial proporcional a la velocidad.

    La clase encapsula:
    - Un punto de suspension estatico.
    - Una restriccion de longitud fija mediante ``PinJoint``.
    - Un rozamiento viscoso que se aplica automaticamente en cada step del Space.

    El cuerpo colgado se considera unido por su centro de masas al extremo de la
    varilla. Si el cuerpo ya tenia una ``velocity_func`` personalizada, se
    conserva y se encadena con el amortiguamiento del pendulo.
    """

    def __init__(
        self,
        length: float,
        body: pymunk.Body,
        suspension_point: tuple[float, float] | Vec2d,
        friction_coefficient: float,
    ) -> None:
        if length <= 0:
            raise ValueError("length debe ser mayor que 0")
        if friction_coefficient < 0:
            raise ValueError("friction_coefficient no puede ser negativo")
        if body.body_type != pymunk.Body.DYNAMIC:
            raise ValueError("El cuerpo colgado debe ser dinamico")

        self.length = float(length)
        self.body = body
        self.suspension_point = Vec2d(*suspension_point)
        self.friction_coefficient = float(friction_coefficient)

        self._pivot = pymunk.Body(body_type=pymunk.Body.STATIC)
        self._pivot.position = self.suspension_point

        # Coloca el cuerpo justo debajo del punto de suspension para que la
        # distancia inicial coincida con la longitud del pendulo.
        self.body.position = self.suspension_point + Vec2d(0, -self.length)

        self._joint = pymunk.PinJoint(self._pivot, self.body, (0, 0), (0, 0))
        self._joint.collide_bodies = False

        self._base_velocity_func = body.velocity_func
        self.body.velocity_func = self._velocity_func

    @property
    def pivot_body(self) -> pymunk.Body:
        return self._pivot

    @property
    def joint(self) -> pymunk.PinJoint:
        return self._joint

    def add_to_space(self, space: pymunk.Space) -> None:
        space.add(self._pivot, self._joint)

    def remove_from_space(self, space: pymunk.Space) -> None:
        if self._joint in space.constraints:
            space.remove(self._joint)
        if self._pivot in space.bodies:
            space.remove(self._pivot)

    def state(self) -> PendulumState:
        radius = self.body.position - self.suspension_point
        tangent = self._tangent(radius)
        angle = math.atan2(radius.x, -radius.y)
        angular_velocity = (
            0.0
            if radius.length == 0
            else self.body.velocity.dot(tangent) / radius.length
        )
        return PendulumState(angle=angle, angular_velocity=angular_velocity)

    def _velocity_func(
        self,
        body: pymunk.Body,
        gravity: tuple[float, float],
        damping: float,
        dt: float,
    ) -> None:
        self._base_velocity_func(body, gravity, damping, dt)

        radius = body.position - self.suspension_point
        if radius.length < 1e-9:
            return

        tangent = self._tangent(radius)
        tangential_speed = body.velocity.dot(tangent)
        damping_accel = -(self.friction_coefficient / body.mass) * tangential_speed
        body.velocity += tangent * (damping_accel * dt)

    @staticmethod
    def _tangent(radius: Vec2d) -> Vec2d:
        return radius.perpendicular_normal()


def build_demo_space() -> tuple[pymunk.Space, DampedPendulum]:
    space = pymunk.Space()
    space.gravity = (0.0, -981.0)

    mass = 10.0
    radius = 20.0
    moment = pymunk.moment_for_circle(mass, 0, radius)
    bob = pymunk.Body(mass, moment)
    bob_shape = pymunk.Circle(bob, radius)
    bob_shape.elasticity = 0.0
    bob_shape.friction = 0.8

    pendulum = DampedPendulum(
        length=100.0,
        body=bob,
        suspension_point=(320.0, 420.0),
        friction_coefficient=1.5,
    )

    # Se desplaza la bola en angulo inicial sin cambiar la longitud.
    initial_angle = math.radians(35)
    bob.position = pendulum.suspension_point + Vec2d(
        pendulum.length * math.sin(initial_angle),
        -pendulum.length * math.cos(initial_angle),
    )

    space.add(bob, bob_shape)
    pendulum.add_to_space(space)
    return space, pendulum


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Demo visual de un pendulo amortiguado con pymunk."
    )
    parser.add_argument(
        "--no-gui",
        action="store_true",
        help="Ejecuta solo la simulacion y muestra el estado final por consola.",
    )
    parser.add_argument(
        "--steps", type=int, default=600, help="Pasos a ejecutar en modo sin interfaz."
    )
    parser.add_argument(
        "--dt", type=float, default=1 / 120, help="Paso temporal de simulacion."
    )
    return parser.parse_args()


def run_demo(steps: int = 600, dt: float = 1 / 120) -> PendulumState:
    space, pendulum = build_demo_space()
    for _ in range(steps):
        space.step(dt)
    return pendulum.state()


def run_visual_demo(dt: float = 1 / 120) -> None:
    space, pendulum = build_demo_space()
    radius = next(
        shape.radius
        for shape in pendulum.body.shapes
        if isinstance(shape, pymunk.Circle)
    )

    root = tk.Tk()
    root.title("Pendulo amortiguado con pymunk")

    width = 640
    height = 520
    canvas = tk.Canvas(
        root, width=width, height=height, bg="#f4f1ea", highlightthickness=0
    )
    canvas.pack()

    canvas.create_text(
        20,
        20,
        anchor="nw",
        text="Pendulo con rozamiento proporcional a la velocidad",
        fill="#1f2937",
        font=("Helvetica", 15, "bold"),
    )
    info_text = canvas.create_text(
        20,
        48,
        anchor="nw",
        text="",
        fill="#374151",
        font=("Helvetica", 12),
    )

    pivot = pendulum.suspension_point
    pivot_canvas = (pivot.x, height - pivot.y)
    canvas.create_line(
        0, pivot_canvas[1], width, pivot_canvas[1], fill="#e5ddd0", width=2
    )
    canvas.create_oval(
        pivot_canvas[0] - 8,
        pivot_canvas[1] - 8,
        pivot_canvas[0] + 8,
        pivot_canvas[1] + 8,
        fill="#4b5563",
        outline="",
    )

    rod = canvas.create_line(0, 0, 0, 0, fill="#9ca3af", width=6)
    bob = canvas.create_oval(0, 0, 0, 0, fill="#b91c1c", outline="#7f1d1d", width=2)

    running = True

    def redraw() -> None:
        bob_pos = pendulum.body.position
        x = bob_pos.x
        y = height - bob_pos.y
        canvas.coords(rod, pivot_canvas[0], pivot_canvas[1], x, y)
        canvas.coords(bob, x - radius, y - radius, x + radius, y + radius)

        state = pendulum.state()
        canvas.itemconfigure(
            info_text,
            text=(
                f"angulo = {math.degrees(state.angle):6.2f} grados\n"
                f"velocidad angular = {state.angular_velocity:7.3f} rad/s\n"
                f"friccion = {pendulum.friction_coefficient:.2f}"
            ),
        )

    def tick() -> None:
        if not running:
            return
        space.step(dt)
        redraw()
        root.after(max(1, int(dt * 1000)), tick)

    def close() -> None:
        nonlocal running
        running = False
        root.destroy()

    root.protocol("WM_DELETE_WINDOW", close)
    redraw()
    tick()
    root.mainloop()


if __name__ == "__main__":
    args = parse_args()
    if args.no_gui:
        final_state = run_demo(steps=args.steps, dt=args.dt)
        print(f"angulo_final_rad={final_state.angle:.6f}")
        print(f"velocidad_angular_final={final_state.angular_velocity:.6f}")
    else:
        run_visual_demo(dt=args.dt)
