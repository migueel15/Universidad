"""
physics_utils_dinamica.py

Funciones puras de cálculo para la asignatura "Dinámica y movimiento para
videojuegos y gamificación".

Objetivo:
- No usa pymunk, pybullet, pygame ni numpy.
- Solo usa la librería estándar de Python.
- Sirve para calcular valores físicos directamente a partir de parámetros.
- Unidades recomendadas: Sistema Internacional (m, kg, s, N, J, rad).

Convenciones:
- Vectores 2D: tuple[float, float] -> (x, y)
- Vectores 3D: tuple[float, float, float] -> (x, y, z)
- Ángulos en radianes salvo que la función indique explícitamente grados.
"""

from __future__ import annotations

from dataclasses import dataclass
from math import acos, asin, atan2, cos, exp, log, pi, radians, sin, sqrt, tan
from typing import Callable, Iterable, Literal, Sequence

Vector2 = tuple[float, float]
Vector3 = tuple[float, float, float]
Matrix3 = tuple[Vector3, Vector3, Vector3]

G_UNIVERSAL = 6.674e-11  # N m^2 / kg^2
G_EARTH = 9.81  # m/s^2
R_AIR = 287.05  # J/(kg K), aire seco
GAMMA_AIR = 1.4  # coeficiente adiabático aire
RHO_AIR_0 = 1.225  # kg/m^3 a nivel del mar, ISA
T_AIR_0 = 288.15  # K, 15 ºC
LAPSE_RATE = 0.0065  # K/m, troposfera
MU_AIR_20C = 1.8e-5  # Pa s
SPEED_OF_SOUND_15C = 340.29  # m/s aproximado


# =============================================================================
# Utilidades generales
# =============================================================================


def clamp(value: float, min_value: float, max_value: float) -> float:
    """Limita value al intervalo [min_value, max_value]."""
    return max(min_value, min(max_value, value))


def sign(value: float) -> int:
    """Devuelve -1, 0 o 1 según el signo de value."""
    if value > 0:
        return 1
    if value < 0:
        return -1
    return 0


def deg_to_rad(degrees: float) -> float:
    """Convierte grados a radianes."""
    return radians(degrees)


def rad_to_deg(rad: float) -> float:
    """Convierte radianes a grados."""
    return rad * 180.0 / pi


def almost_zero(value: float, eps: float = 1e-12) -> bool:
    """Comprueba si un número es prácticamente cero."""
    return abs(value) < eps


# =============================================================================
# Vectores 2D y 3D
# =============================================================================


def vec2_add(a: Vector2, b: Vector2) -> Vector2:
    return (a[0] + b[0], a[1] + b[1])


def vec2_sub(a: Vector2, b: Vector2) -> Vector2:
    return (a[0] - b[0], a[1] - b[1])


def vec2_scale(v: Vector2, k: float) -> Vector2:
    return (k * v[0], k * v[1])


def vec2_dot(a: Vector2, b: Vector2) -> float:
    return a[0] * b[0] + a[1] * b[1]


def vec2_cross_z(a: Vector2, b: Vector2) -> float:
    """Producto vectorial 2D: devuelve la componente z de a x b."""
    return a[0] * b[1] - a[1] * b[0]


def vec2_norm(v: Vector2) -> float:
    return sqrt(vec2_dot(v, v))


def vec2_distance(a: Vector2, b: Vector2) -> float:
    return vec2_norm(vec2_sub(a, b))


def vec2_unit(v: Vector2, eps: float = 1e-12) -> Vector2:
    n = vec2_norm(v)
    if n < eps:
        raise ValueError("No se puede normalizar un vector casi nulo")
    return (v[0] / n, v[1] / n)


def vec2_project(a: Vector2, b: Vector2, eps: float = 1e-12) -> Vector2:
    """Proyección de a sobre b."""
    denom = vec2_dot(b, b)
    if denom < eps:
        raise ValueError("No se puede proyectar sobre un vector casi nulo")
    return vec2_scale(b, vec2_dot(a, b) / denom)


def vec2_perpendicular(v: Vector2) -> Vector2:
    """Vector perpendicular girado +90 grados."""
    return (-v[1], v[0])


def vec2_angle_between(a: Vector2, b: Vector2, eps: float = 1e-12) -> float:
    """Ángulo entre dos vectores 2D en radianes."""
    na = vec2_norm(a)
    nb = vec2_norm(b)
    if na < eps or nb < eps:
        raise ValueError("No se puede calcular ángulo con vectores casi nulos")
    return acos(clamp(vec2_dot(a, b) / (na * nb), -1.0, 1.0))


def vec2_rotate(v: Vector2, angle: float) -> Vector2:
    """Rota un vector 2D un ángulo en radianes."""
    c, s = cos(angle), sin(angle)
    return (c * v[0] - s * v[1], s * v[0] + c * v[1])


def vec2_lerp(a: Vector2, b: Vector2, t: float) -> Vector2:
    """Interpolación lineal entre a y b."""
    return (a[0] + (b[0] - a[0]) * t, a[1] + (b[1] - a[1]) * t)


def vec3_add(a: Vector3, b: Vector3) -> Vector3:
    return (a[0] + b[0], a[1] + b[1], a[2] + b[2])


def vec3_sub(a: Vector3, b: Vector3) -> Vector3:
    return (a[0] - b[0], a[1] - b[1], a[2] - b[2])


def vec3_scale(v: Vector3, k: float) -> Vector3:
    return (k * v[0], k * v[1], k * v[2])


def vec3_dot(a: Vector3, b: Vector3) -> float:
    return a[0] * b[0] + a[1] * b[1] + a[2] * b[2]


def vec3_cross(a: Vector3, b: Vector3) -> Vector3:
    return (
        a[1] * b[2] - a[2] * b[1],
        a[2] * b[0] - a[0] * b[2],
        a[0] * b[1] - a[1] * b[0],
    )


def vec3_norm(v: Vector3) -> float:
    return sqrt(vec3_dot(v, v))


def vec3_distance(a: Vector3, b: Vector3) -> float:
    return vec3_norm(vec3_sub(a, b))


def vec3_unit(v: Vector3, eps: float = 1e-12) -> Vector3:
    n = vec3_norm(v)
    if n < eps:
        raise ValueError("No se puede normalizar un vector casi nulo")
    return (v[0] / n, v[1] / n, v[2] / n)


def vec3_project(a: Vector3, b: Vector3, eps: float = 1e-12) -> Vector3:
    denom = vec3_dot(b, b)
    if denom < eps:
        raise ValueError("No se puede proyectar sobre un vector casi nulo")
    return vec3_scale(b, vec3_dot(a, b) / denom)


def vec3_angle_between(a: Vector3, b: Vector3, eps: float = 1e-12) -> float:
    na = vec3_norm(a)
    nb = vec3_norm(b)
    if na < eps or nb < eps:
        raise ValueError("No se puede calcular ángulo con vectores casi nulos")
    return acos(clamp(vec3_dot(a, b) / (na * nb), -1.0, 1.0))


# =============================================================================
# Coordenadas y cámara
# =============================================================================


def spherical_to_cartesian(r: float, theta: float, phi: float) -> Vector3:
    """
    Coordenadas esféricas a cartesianas.

    Convención:
    - r: distancia radial.
    - theta: ángulo cenital desde +Z.
    - phi: azimut en plano XY desde +X.
    """
    return (
        r * sin(theta) * cos(phi),
        r * sin(theta) * sin(phi),
        r * cos(theta),
    )


def cartesian_to_spherical(
    x: float, y: float, z: float, eps: float = 1e-12
) -> tuple[float, float, float]:
    """Cartesianas a esféricas: devuelve (r, theta, phi)."""
    r = sqrt(x * x + y * y + z * z)
    if r < eps:
        raise ValueError("El origen no tiene theta/phi definidos")
    theta = acos(clamp(z / r, -1.0, 1.0))
    phi = atan2(y, x)
    return r, theta, phi


def pybullet_camera_to_cartesian(distance: float, yaw: float, pitch: float) -> Vector3:
    """
    Coordenadas de cámara estilo PyBullet a vector relativo cartesiano.

    Fórmulas:
    x = d cos(alpha) sin(psi)
    y = -d cos(alpha) cos(psi)
    z = -d sin(alpha)

    yaw y pitch en radianes.
    """
    return (
        distance * cos(pitch) * sin(yaw),
        -distance * cos(pitch) * cos(yaw),
        -distance * sin(pitch),
    )


def cartesian_to_pybullet_camera(
    x: float, y: float, z: float, eps: float = 1e-12
) -> tuple[float, float, float]:
    """Vector relativo cartesiano a (distance, yaw, pitch) estilo PyBullet, en radianes."""
    d = sqrt(x * x + y * y + z * z)
    if d < eps:
        raise ValueError("La cámara no puede estar a distancia cero")
    pitch = -asin(clamp(z / d, -1.0, 1.0))
    yaw = atan2(x, -y)
    return d, yaw, pitch


# =============================================================================
# Matrices de rotación y orientación
# =============================================================================


def mat3_mul_vec3(m: Matrix3, v: Vector3) -> Vector3:
    """Multiplica matriz 3x3 por vector 3D."""
    return (
        vec3_dot(m[0], v),
        vec3_dot(m[1], v),
        vec3_dot(m[2], v),
    )


def mat3_mul(a: Matrix3, b: Matrix3) -> Matrix3:
    """Multiplica dos matrices 3x3."""
    cols = (
        (b[0][0], b[1][0], b[2][0]),
        (b[0][1], b[1][1], b[2][1]),
        (b[0][2], b[1][2], b[2][2]),
    )
    return tuple(tuple(vec3_dot(row, col) for col in cols) for row in a)  # type: ignore[return-value]


def rotation_matrix_x(angle: float) -> Matrix3:
    c, s = cos(angle), sin(angle)
    return ((1.0, 0.0, 0.0), (0.0, c, -s), (0.0, s, c))


def rotation_matrix_y(angle: float) -> Matrix3:
    c, s = cos(angle), sin(angle)
    return ((c, 0.0, s), (0.0, 1.0, 0.0), (-s, 0.0, c))


def rotation_matrix_z(angle: float) -> Matrix3:
    c, s = cos(angle), sin(angle)
    return ((c, -s, 0.0), (s, c, 0.0), (0.0, 0.0, 1.0))


def rotate_vec3_x(v: Vector3, angle: float) -> Vector3:
    return mat3_mul_vec3(rotation_matrix_x(angle), v)


def rotate_vec3_y(v: Vector3, angle: float) -> Vector3:
    return mat3_mul_vec3(rotation_matrix_y(angle), v)


def rotate_vec3_z(v: Vector3, angle: float) -> Vector3:
    return mat3_mul_vec3(rotation_matrix_z(angle), v)


def euler_zxz_matrix(alpha: float, beta: float, gamma: float) -> Matrix3:
    """Matriz de Euler clásica Z-X-Z: Rz(alpha) Rx(beta) Rz(gamma)."""
    return mat3_mul(
        mat3_mul(rotation_matrix_z(alpha), rotation_matrix_x(beta)),
        rotation_matrix_z(gamma),
    )


def tait_bryan_xyz_matrix(roll: float, pitch: float, yaw: float) -> Matrix3:
    """
    Convención tipo roll-pitch-yaw: R = Rz(yaw) Ry(pitch) Rx(roll).
    """
    return mat3_mul(
        mat3_mul(rotation_matrix_z(yaw), rotation_matrix_y(pitch)),
        rotation_matrix_x(roll),
    )


def quaternion_from_axis_angle(
    axis: Vector3, angle: float
) -> tuple[float, float, float, float]:
    """Cuaternión [x, y, z, w] a partir de eje unitario y ángulo."""
    ux, uy, uz = vec3_unit(axis)
    half = angle / 2.0
    s = sin(half)
    return (ux * s, uy * s, uz * s, cos(half))


def quaternion_norm(q: tuple[float, float, float, float]) -> float:
    x, y, z, w = q
    return sqrt(x * x + y * y + z * z + w * w)


def quaternion_normalize(
    q: tuple[float, float, float, float], eps: float = 1e-12
) -> tuple[float, float, float, float]:
    n = quaternion_norm(q)
    if n < eps:
        raise ValueError("No se puede normalizar un cuaternión casi nulo")
    return (q[0] / n, q[1] / n, q[2] / n, q[3] / n)


def quaternion_multiply(
    q1: tuple[float, float, float, float], q2: tuple[float, float, float, float]
) -> tuple[float, float, float, float]:
    """Multiplica cuaterniones en formato [x, y, z, w]."""
    x1, y1, z1, w1 = q1
    x2, y2, z2, w2 = q2
    return (
        w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2,
        w1 * y2 - x1 * z2 + y1 * w2 + z1 * x2,
        w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2,
        w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2,
    )


def quaternion_conjugate(
    q: tuple[float, float, float, float],
) -> tuple[float, float, float, float]:
    return (-q[0], -q[1], -q[2], q[3])


def rotate_vector_by_quaternion(
    v: Vector3, q: tuple[float, float, float, float]
) -> Vector3:
    """Rota v usando un cuaternión [x, y, z, w]."""
    qn = quaternion_normalize(q)
    vq = (v[0], v[1], v[2], 0.0)
    rq = quaternion_multiply(quaternion_multiply(qn, vq), quaternion_conjugate(qn))
    return (rq[0], rq[1], rq[2])


def quaternion_from_euler_xyz(
    roll: float, pitch: float, yaw: float
) -> tuple[float, float, float, float]:
    """Cuaternión [x, y, z, w] desde roll, pitch, yaw."""
    qx = quaternion_from_axis_angle((1.0, 0.0, 0.0), roll)
    qy = quaternion_from_axis_angle((0.0, 1.0, 0.0), pitch)
    qz = quaternion_from_axis_angle((0.0, 0.0, 1.0), yaw)
    return quaternion_normalize(quaternion_multiply(quaternion_multiply(qz, qy), qx))


# =============================================================================
# Derivación, integración y pasos numéricos
# =============================================================================


def derivative_forward(f: Callable[[float], float], t: float, dt: float) -> float:
    return (f(t + dt) - f(t)) / dt


def derivative_backward(f: Callable[[float], float], t: float, dt: float) -> float:
    return (f(t) - f(t - dt)) / dt


def derivative_central(f: Callable[[float], float], t: float, dt: float) -> float:
    return (f(t + dt) - f(t - dt)) / (2.0 * dt)


def velocity_from_positions(x_current: float, x_previous: float, dt: float) -> float:
    """Diferencia hacia atrás: v = (x_i - x_{i-1}) / dt."""
    return (x_current - x_previous) / dt


def acceleration_from_velocities(
    v_current: float, v_previous: float, dt: float
) -> float:
    return (v_current - v_previous) / dt


def riemann_left(f: Callable[[float], float], a: float, b: float, n: int) -> float:
    if n <= 0:
        raise ValueError("n debe ser positivo")
    dx = (b - a) / n
    return sum(f(a + i * dx) for i in range(n)) * dx


def trapezoidal_rule(f: Callable[[float], float], a: float, b: float, n: int) -> float:
    if n <= 0:
        raise ValueError("n debe ser positivo")
    dx = (b - a) / n
    total = 0.5 * (f(a) + f(b))
    total += sum(f(a + i * dx) for i in range(1, n))
    return total * dx


def simpson_rule(f: Callable[[float], float], a: float, b: float, n: int) -> float:
    """Regla de Simpson. n debe ser par."""
    if n <= 0 or n % 2 != 0:
        raise ValueError("n debe ser positivo y par")
    dx = (b - a) / n
    total = f(a) + f(b)
    total += 4.0 * sum(f(a + i * dx) for i in range(1, n, 2))
    total += 2.0 * sum(f(a + i * dx) for i in range(2, n, 2))
    return total * dx / 3.0


def euler_step(x: float, v: float, a: float, dt: float) -> tuple[float, float]:
    """Integración de Euler explícita: x_{n+1}=x+v dt, v_{n+1}=v+a dt."""
    return x + v * dt, v + a * dt


def semi_implicit_euler_step(
    x: float, v: float, a: float, dt: float
) -> tuple[float, float]:
    """Euler semi-implícito: primero actualiza v, luego x."""
    v_new = v + a * dt
    return x + v_new * dt, v_new


def rk4_step(
    y: float, t: float, dt: float, dydt: Callable[[float, float], float]
) -> float:
    """Un paso RK4 para y' = f(t, y)."""
    k1 = dydt(t, y)
    k2 = dydt(t + dt / 2.0, y + dt * k1 / 2.0)
    k3 = dydt(t + dt / 2.0, y + dt * k2 / 2.0)
    k4 = dydt(t + dt, y + dt * k3)
    return y + (dt / 6.0) * (k1 + 2 * k2 + 2 * k3 + k4)


# =============================================================================
# Dinámica de partícula, fuerzas y energía
# =============================================================================


def newton_acceleration(force: float, mass: float) -> float:
    """Segunda ley de Newton: a = F/m."""
    if mass <= 0:
        raise ValueError("La masa debe ser positiva")
    return force / mass


def vector_acceleration(force: Vector3, mass: float) -> Vector3:
    if mass <= 0:
        raise ValueError("La masa debe ser positiva")
    return vec3_scale(force, 1.0 / mass)


def weight_force(mass: float, g: float = G_EARTH) -> float:
    """Peso: P = mg."""
    return mass * g


def gravitational_force_magnitude(
    m1: float, m2: float, r: float, G: float = G_UNIVERSAL
) -> float:
    """Ley de gravitación universal: F = G m1 m2 / r^2."""
    if r <= 0:
        raise ValueError("La distancia debe ser positiva")
    return G * m1 * m2 / (r * r)


def gravitational_force_vector(
    pos1: Vector3, mass1: float, pos2: Vector3, mass2: float, G: float = G_UNIVERSAL
) -> Vector3:
    """Fuerza sobre el cuerpo 1 debida al cuerpo 2."""
    r_vec = vec3_sub(pos2, pos1)
    r = vec3_norm(r_vec)
    mag = gravitational_force_magnitude(mass1, mass2, r, G)
    return vec3_scale(vec3_unit(r_vec), mag)


def gravitational_acceleration_from_body(
    pos: Vector3, source_pos: Vector3, source_mass: float, G: float = G_UNIVERSAL
) -> Vector3:
    """Aceleración gravitatoria en pos causada por una masa puntual."""
    r_vec = vec3_sub(source_pos, pos)
    r = vec3_norm(r_vec)
    if r <= 0:
        raise ValueError("La distancia debe ser positiva")
    return vec3_scale(vec3_unit(r_vec), G * source_mass / (r * r))


def hooke_force(displacement: float, k: float) -> float:
    """Ley de Hooke 1D: F = -kx."""
    return -k * displacement


def hooke_force_vector(
    pos: Vector3, anchor: Vector3, rest_length: float, k: float, eps: float = 1e-12
) -> Vector3:
    """Fuerza de un muelle entre anchor y pos."""
    delta = vec3_sub(pos, anchor)
    length = vec3_norm(delta)
    if length < eps:
        return (0.0, 0.0, 0.0)
    stretch = length - rest_length
    return vec3_scale(vec3_unit(delta), -k * stretch)


def viscous_damping_force(velocity: float, b: float) -> float:
    """Rozamiento viscoso lineal 1D: F = -b v."""
    return -b * velocity


def viscous_damping_force_vector(velocity: Vector3, b: float) -> Vector3:
    return vec3_scale(velocity, -b)


def pymunk_damping_velocity(v_old: float, damping: float, dt: float) -> float:
    """Modelo tipo Pymunk: v_new = v_old * damping ** dt."""
    return v_old * (damping**dt)


def damping_from_b_over_m(b: float, mass: float) -> float:
    """
    Factor continuo equivalente por segundo para F=-bv:
    v(t+1) = v(t) exp(-b/m).
    """
    if mass <= 0:
        raise ValueError("La masa debe ser positiva")
    return exp(-b / mass)


def coulomb_friction_max(mu_static: float, normal_force: float) -> float:
    """Máximo rozamiento estático: Fr <= mu_e N."""
    return mu_static * normal_force


def kinetic_friction(mu_dynamic: float, normal_force: float) -> float:
    """Rozamiento dinámico: Fr = mu_d N."""
    return mu_dynamic * normal_force


def inclined_plane_normal(mass: float, angle: float, g: float = G_EARTH) -> float:
    """Normal en plano inclinado: N = mg cos(theta)."""
    return mass * g * cos(angle)


def inclined_plane_weight_parallel(
    mass: float, angle: float, g: float = G_EARTH
) -> float:
    """Componente tangencial del peso: mg sin(theta)."""
    return mass * g * sin(angle)


def inclined_plane_acceleration_no_friction(angle: float, g: float = G_EARTH) -> float:
    return g * sin(angle)


def inclined_plane_acceleration_with_kinetic_friction(
    angle: float, mu_dynamic: float, g: float = G_EARTH
) -> float:
    """Aceleración bajando por una rampa con rozamiento cinético."""
    return g * (sin(angle) - mu_dynamic * cos(angle))


def will_slide_on_incline(angle: float, mu_static: float) -> bool:
    """Un bloque empieza a deslizar si tan(theta) > mu_static."""
    return tan(angle) > mu_static


def work_constant_force(force: Vector3, displacement: Vector3) -> float:
    """Trabajo: W = F · Δr."""
    return vec3_dot(force, displacement)


def kinetic_energy(mass: float, speed: float) -> float:
    return 0.5 * mass * speed * speed


def gravitational_potential_energy(
    mass: float, height: float, g: float = G_EARTH
) -> float:
    return mass * g * height


def spring_potential_energy(k: float, displacement: float) -> float:
    return 0.5 * k * displacement * displacement


def mechanical_energy(
    mass: float, speed: float, height: float, g: float = G_EARTH
) -> float:
    return kinetic_energy(mass, speed) + gravitational_potential_energy(mass, height, g)


def power_from_force_velocity(force: Vector3, velocity: Vector3) -> float:
    """Potencia instantánea: P = F · v."""
    return vec3_dot(force, velocity)


def impulse_from_force(force: Vector3, dt: float) -> Vector3:
    """Impulso aproximado para fuerza constante: J = F dt."""
    return vec3_scale(force, dt)


def delta_v_from_impulse(impulse: Vector3, mass: float) -> Vector3:
    """Cambio de velocidad: Δv = J/m."""
    if mass <= 0:
        raise ValueError("La masa debe ser positiva")
    return vec3_scale(impulse, 1.0 / mass)


# =============================================================================
# Osciladores, muelles y péndulo
# =============================================================================


def angular_frequency_spring(k: float, mass: float) -> float:
    """Frecuencia angular de un muelle sin amortiguar: omega = sqrt(k/m)."""
    if mass <= 0:
        raise ValueError("La masa debe ser positiva")
    return sqrt(k / mass)


def angular_frequency_pendulum(length: float, g: float = G_EARTH) -> float:
    """Frecuencia angular de péndulo pequeño: omega = sqrt(g/L)."""
    if length <= 0:
        raise ValueError("La longitud debe ser positiva")
    return sqrt(g / length)


def pendulum_period_small_angle(length: float, g: float = G_EARTH) -> float:
    return 2.0 * pi / angular_frequency_pendulum(length, g)


def pendulum_tangential_force(mass: float, angle: float, g: float = G_EARTH) -> float:
    """Fuerza tangencial exacta del péndulo: F = -mg sin(theta)."""
    return -mass * g * sin(angle)


def pendulum_small_angle_force(
    mass: float, length: float, arc_displacement: float, g: float = G_EARTH
) -> float:
    """Aproximación lineal: F ≈ -(mg/L) x."""
    if length <= 0:
        raise ValueError("La longitud debe ser positiva")
    return -(mass * g / length) * arc_displacement


def critical_damping(mass: float, k: float) -> float:
    """Amortiguamiento crítico: b_c = 2 sqrt(mk)."""
    if mass <= 0 or k < 0:
        raise ValueError("mass debe ser positiva y k no negativa")
    return 2.0 * sqrt(mass * k)


def damping_ratio(mass: float, b: float, k: float) -> float:
    """Ratio de amortiguamiento: zeta = b / (2 sqrt(mk))."""
    return b / critical_damping(mass, k)


def characteristic_time_damping(mass: float, b: float) -> float:
    """Tiempo característico tau = 2m/b para oscilador amortiguado."""
    if b == 0:
        raise ValueError("b no puede ser cero")
    return 2.0 * mass / b


def damped_oscillator_acceleration(
    x: float, v: float, mass: float, b: float, k: float
) -> float:
    """Para m x'' + b x' + kx = 0: a = (-b v - kx)/m."""
    if mass <= 0:
        raise ValueError("La masa debe ser positiva")
    return (-b * v - k * x) / mass


# =============================================================================
# Proyectiles sin rozamiento
# =============================================================================


@dataclass(frozen=True)
class ProjectileState2D:
    x: float
    y: float
    vx: float
    vy: float


def projectile_initial_velocity(v0: float, angle: float) -> Vector2:
    """Componentes iniciales: vx0=v0 cos(theta), vy0=v0 sin(theta)."""
    return (v0 * cos(angle), v0 * sin(angle))


def projectile_position_no_drag(
    x0: float, y0: float, v0: float, angle: float, t: float, g: float = G_EARTH
) -> Vector2:
    vx0, vy0 = projectile_initial_velocity(v0, angle)
    return (x0 + vx0 * t, y0 + vy0 * t - 0.5 * g * t * t)


def projectile_velocity_no_drag(
    v0: float, angle: float, t: float, g: float = G_EARTH
) -> Vector2:
    vx0, vy0 = projectile_initial_velocity(v0, angle)
    return (vx0, vy0 - g * t)


def projectile_state_no_drag(
    x0: float, y0: float, v0: float, angle: float, t: float, g: float = G_EARTH
) -> ProjectileState2D:
    x, y = projectile_position_no_drag(x0, y0, v0, angle, t, g)
    vx, vy = projectile_velocity_no_drag(v0, angle, t, g)
    return ProjectileState2D(x=x, y=y, vx=vx, vy=vy)


def projectile_time_to_peak(v0: float, angle: float, g: float = G_EARTH) -> float:
    return v0 * sin(angle) / g


def projectile_max_height(
    y0: float, v0: float, angle: float, g: float = G_EARTH
) -> float:
    vy0 = v0 * sin(angle)
    return y0 + (vy0 * vy0) / (2.0 * g)


def projectile_time_of_flight_from_height(
    y0: float, v0: float, angle: float, y_target: float = 0.0, g: float = G_EARTH
) -> float:
    """
    Tiempo positivo en el que y(t)=y_target.
    Resuelve: y0 + vy0 t - 1/2 g t^2 = y_target.
    """
    vy0 = v0 * sin(angle)
    disc = vy0 * vy0 + 2.0 * g * (y0 - y_target)
    if disc < 0:
        raise ValueError("No hay intersección real con la altura objetivo")
    return (vy0 + sqrt(disc)) / g


def projectile_range_from_height(
    x0: float,
    y0: float,
    v0: float,
    angle: float,
    y_target: float = 0.0,
    g: float = G_EARTH,
) -> float:
    t = projectile_time_of_flight_from_height(y0, v0, angle, y_target, g)
    return x0 + v0 * cos(angle) * t


def projectile_range_level_ground(v0: float, angle: float, g: float = G_EARTH) -> float:
    """Alcance con salida y llegada a la misma altura: R = v0^2 sin(2theta)/g."""
    return (v0 * v0 * sin(2.0 * angle)) / g


def projectile_angle_for_range_level(
    v0: float, range_: float, g: float = G_EARTH
) -> tuple[float, float]:
    """Ángulos posibles para alcanzar R a misma altura."""
    value = g * range_ / (v0 * v0)
    if value < -1.0 or value > 1.0:
        raise ValueError("No existe ángulo real para ese alcance con esa velocidad")
    a = asin(value)
    return a / 2.0, (pi - a) / 2.0


# =============================================================================
# Aire, arrastre, viento, Mach y Magnus
# =============================================================================


def frontal_area_circle(radius: float) -> float:
    return pi * radius * radius


def frontal_area_sphere(diameter: float) -> float:
    return pi * (diameter / 2.0) ** 2


def reynolds_number(
    rho: float, speed: float, characteristic_length: float, mu: float
) -> float:
    """Re = rho v L / mu."""
    if mu <= 0:
        raise ValueError("La viscosidad debe ser positiva")
    return rho * abs(speed) * characteristic_length / mu


def drag_regime_from_reynolds(
    Re: float,
) -> Literal["laminar", "transicion", "turbulento", "crisis/supercritico"]:
    if Re < 1.0:
        return "laminar"
    if Re < 1.0e3:
        return "transicion"
    if Re < 2.0e5:
        return "turbulento"
    return "crisis/supercritico"


def stokes_drag_magnitude(
    mu: float, radius_equiv: float, speed: float, shape_factor: float = 1.0
) -> float:
    """Módulo Stokes: F = 6 pi mu K r v."""
    return 6.0 * pi * mu * shape_factor * radius_equiv * abs(speed)


def stokes_drag_force_vector(
    velocity: Vector3,
    mu: float,
    radius_equiv: float,
    shape_factor: float = 1.0,
    eps: float = 1e-12,
) -> Vector3:
    speed = vec3_norm(velocity)
    if speed < eps:
        return (0.0, 0.0, 0.0)
    mag = stokes_drag_magnitude(mu, radius_equiv, speed, shape_factor)
    return vec3_scale(vec3_unit(velocity), -mag)


def sphere_cd_intermediate(Re: float) -> float:
    """Aproximación para esfera en transición: Cd ≈ 24/Re * (1 + 0.15 Re^0.687)."""
    if Re <= 0:
        raise ValueError("Re debe ser positivo")
    return (24.0 / Re) * (1.0 + 0.15 * (Re**0.687))


def newton_drag_magnitude(rho: float, Cd: float, area: float, speed: float) -> float:
    """Módulo de arrastre cuadrático: Fd = 1/2 rho Cd A v^2."""
    return 0.5 * rho * Cd * area * speed * speed


def newton_drag_force_vector(
    velocity: Vector3, rho: float, Cd: float, area: float, eps: float = 1e-12
) -> Vector3:
    """F_drag = -1/2 rho Cd A |v| v."""
    speed = vec3_norm(velocity)
    if speed < eps:
        return (0.0, 0.0, 0.0)
    factor = -0.5 * rho * Cd * area * speed
    return vec3_scale(velocity, factor)


def relative_velocity(projectile_velocity: Vector3, wind_velocity: Vector3) -> Vector3:
    """v_rel = v_proyectil - v_viento."""
    return vec3_sub(projectile_velocity, wind_velocity)


def drag_force_with_wind(
    projectile_velocity: Vector3,
    wind_velocity: Vector3,
    rho: float,
    Cd: float,
    area: float,
    eps: float = 0.1,
) -> Vector3:
    """Arrastre usando velocidad relativa al aire. Umbral eps para evitar ruido numérico."""
    v_rel = relative_velocity(projectile_velocity, wind_velocity)
    if vec3_norm(v_rel) < eps:
        return (0.0, 0.0, 0.0)
    return newton_drag_force_vector(v_rel, rho, Cd, area)


def air_temperature_at_altitude(
    altitude_m: float, T0: float = T_AIR_0, lapse_rate: float = LAPSE_RATE
) -> float:
    """Temperatura ISA en troposfera, limitada a 0..11000 m."""
    h = clamp(altitude_m, 0.0, 11000.0)
    return T0 - lapse_rate * h


def air_density_at_altitude(
    altitude_m: float,
    rho0: float = RHO_AIR_0,
    T0: float = T_AIR_0,
    lapse_rate: float = LAPSE_RATE,
    R: float = R_AIR,
    g: float = 9.80665,
) -> float:
    """
    Densidad del aire en troposfera:
    rho(h) = rho0 * (T(h)/T0)^(g/(R L)-1)
    """
    T = air_temperature_at_altitude(altitude_m, T0, lapse_rate)
    exponent = (g / (R * lapse_rate)) - 1.0
    return rho0 * (T / T0) ** exponent


def speed_of_sound_from_temperature(
    temp_k: float, gamma: float = GAMMA_AIR, R: float = R_AIR
) -> float:
    """c = sqrt(gamma R T)."""
    if temp_k <= 0:
        raise ValueError("La temperatura en Kelvin debe ser positiva")
    return sqrt(gamma * R * temp_k)


def speed_of_sound_at_altitude(altitude_m: float, T0: float = T_AIR_0) -> float:
    return speed_of_sound_from_temperature(air_temperature_at_altitude(altitude_m, T0))


def mach_number(speed: float, speed_of_sound: float) -> float:
    if speed_of_sound <= 0:
        raise ValueError("La velocidad del sonido debe ser positiva")
    return abs(speed) / speed_of_sound


def mach_correction_factor(M: float) -> float:
    """
    Factor psi(M):
    - M < 0.8: 1
    - 0.8 <= M < 1.2: 1 + 1.25(M - 0.8)
    - M >= 1.2: 1.5 + 0.5/M
    """
    if M < 0.8:
        return 1.0
    if M < 1.2:
        return 1.0 + 1.25 * (M - 0.8)
    return 1.5 + 0.5 / M


def cd_with_mach_correction(
    Cd_reynolds: float, speed: float, speed_of_sound: float
) -> float:
    return Cd_reynolds * mach_correction_factor(mach_number(speed, speed_of_sound))


def bernoulli_pressure_total(
    static_pressure: float,
    rho: float,
    speed: float,
    height: float = 0.0,
    g: float = G_EARTH,
) -> float:
    """Bernoulli: P + rho g h + 1/2 rho v^2."""
    return static_pressure + rho * g * height + 0.5 * rho * speed * speed


def pressure_difference_from_speeds(
    rho: float, speed_a: float, speed_b: float
) -> float:
    """
    Diferencia por Bernoulli simplificado.
    Devuelve P_a - P_b = 1/2 rho (v_b^2 - v_a^2).
    """
    return 0.5 * rho * (speed_b * speed_b - speed_a * speed_a)


def magnus_force_direction_2d(
    velocity: Vector2, omega_z: float, eps: float = 1e-12
) -> Vector2:
    """
    Dirección cualitativa del efecto Magnus en 2D.
    Para omega_z positivo y velocidad +x, devuelve dirección +y.
    """
    speed = vec2_norm(velocity)
    if speed < eps or almost_zero(omega_z, eps):
        return (0.0, 0.0)
    perp = vec2_perpendicular(vec2_unit(velocity))
    return vec2_scale(perp, sign(omega_z))


def magnus_force_simplified(
    velocity: Vector3, omega: Vector3, coefficient: float
) -> Vector3:
    """
    Modelo simplificado: F_M = coefficient * (omega x v).
    El coefficient agrupa rho, radio, área y constantes empíricas.
    """
    return vec3_scale(vec3_cross(omega, velocity), coefficient)


# =============================================================================
# Dinámica de rotación, centro de masas y rodadura
# =============================================================================


def angular_velocity(theta_initial: float, theta_final: float, dt: float) -> float:
    return (theta_final - theta_initial) / dt


def angular_acceleration(omega_initial: float, omega_final: float, dt: float) -> float:
    return (omega_final - omega_initial) / dt


def centripetal_acceleration(speed: float, radius: float) -> float:
    if radius <= 0:
        raise ValueError("El radio debe ser positivo")
    return speed * speed / radius


def centripetal_force(mass: float, speed: float, radius: float) -> float:
    return mass * centripetal_acceleration(speed, radius)


def centrifugal_force_apparent(mass: float, speed: float, radius: float) -> float:
    """Mismo módulo que la centrípeta, sentido opuesto en sistema no inercial."""
    return centripetal_force(mass, speed, radius)


def max_curve_speed_from_friction(
    mu: float, radius: float, g: float = G_EARTH
) -> float:
    """Curva plana: m v^2/R <= mu mg -> v_max = sqrt(mu g R)."""
    if radius < 0:
        raise ValueError("El radio no puede ser negativo")
    return sqrt(mu * g * radius)


def required_friction_for_curve(
    speed: float, radius: float, g: float = G_EARTH
) -> float:
    """mu mínimo en curva plana: mu >= v^2/(gR)."""
    if radius <= 0:
        raise ValueError("El radio debe ser positivo")
    return speed * speed / (g * radius)


def motorcycle_lean_angle(speed: float, radius: float, g: float = G_EARTH) -> float:
    """tan(phi)=v^2/(gR). Devuelve phi en radianes."""
    return atan2(speed * speed, g * radius)


def banked_curve_speed_no_friction(
    radius: float, bank_angle: float, g: float = G_EARTH
) -> float:
    """Peralte ideal sin rozamiento: v = sqrt(R g tan(theta))."""
    return sqrt(radius * g * tan(bank_angle))


def center_of_mass_discrete(
    masses: Sequence[float], positions: Sequence[Vector3]
) -> Vector3:
    """Centro de masas discreto 3D."""
    if len(masses) != len(positions) or not masses:
        raise ValueError("masses y positions deben tener la misma longitud no nula")
    total = sum(masses)
    if total == 0:
        raise ValueError("La masa total no puede ser cero")
    sx = sum(m * p[0] for m, p in zip(masses, positions))
    sy = sum(m * p[1] for m, p in zip(masses, positions))
    sz = sum(m * p[2] for m, p in zip(masses, positions))
    return (sx / total, sy / total, sz / total)


def triangle_centroid(p1: Vector2, p2: Vector2, p3: Vector2) -> Vector2:
    return ((p1[0] + p2[0] + p3[0]) / 3.0, (p1[1] + p2[1] + p3[1]) / 3.0)


def polygon_signed_area(vertices: Sequence[Vector2]) -> float:
    """Área firmada mediante la fórmula del zapatero."""
    n = len(vertices)
    if n < 3:
        raise ValueError("Un polígono necesita al menos 3 vértices")
    total = 0.0
    for i in range(n):
        x1, y1 = vertices[i]
        x2, y2 = vertices[(i + 1) % n]
        total += x1 * y2 - x2 * y1
    return 0.5 * total


def polygon_centroid(vertices: Sequence[Vector2], eps: float = 1e-12) -> Vector2:
    """Centroide de un polígono simple con vértices ordenados."""
    A = polygon_signed_area(vertices)
    if abs(A) < eps:
        raise ValueError("El área del polígono es casi cero")
    cx = 0.0
    cy = 0.0
    n = len(vertices)
    for i in range(n):
        x1, y1 = vertices[i]
        x2, y2 = vertices[(i + 1) % n]
        cross = x1 * y2 - x2 * y1
        cx += (x1 + x2) * cross
        cy += (y1 + y2) * cross
    return (cx / (6.0 * A), cy / (6.0 * A))


def torque_2d(r: Vector2, force: Vector2) -> float:
    """Torque escalar 2D: tau = r_x F_y - r_y F_x."""
    return vec2_cross_z(r, force)


def torque_magnitude(r: float, force: float, angle_between: float) -> float:
    """Módulo: tau = r F sin(phi)."""
    return r * force * sin(angle_between)


def angular_acceleration_from_torque(torque: float, inertia: float) -> float:
    """alpha = tau / I."""
    if inertia <= 0:
        raise ValueError("El momento de inercia debe ser positivo")
    return torque / inertia


def angular_momentum(inertia: float, omega: float) -> float:
    """L = I omega."""
    return inertia * omega


def moment_of_inertia_point_masses(
    masses: Sequence[float], radii: Sequence[float]
) -> float:
    """I = sum(m_i r_i^2)."""
    if len(masses) != len(radii):
        raise ValueError("masses y radii deben tener la misma longitud")
    return sum(m * r * r for m, r in zip(masses, radii))


def inertia_thin_ring(mass: float, radius: float) -> float:
    return mass * radius * radius


def inertia_annulus(mass: float, inner_radius: float, outer_radius: float) -> float:
    return 0.5 * mass * (inner_radius * inner_radius + outer_radius * outer_radius)


def inertia_solid_cylinder_axis(mass: float, radius: float) -> float:
    """Cilindro/disco macizo respecto a eje central longitudinal: I=1/2 mR^2."""
    return 0.5 * mass * radius * radius


def inertia_solid_sphere(mass: float, radius: float) -> float:
    return (2.0 / 5.0) * mass * radius * radius


def inertia_hollow_sphere(mass: float, radius: float) -> float:
    return (2.0 / 3.0) * mass * radius * radius


def inertia_spherical_shell_thick(
    mass: float, inner_radius: float, outer_radius: float
) -> float:
    """
    Corteza esférica gruesa:
    I = 2/5 m (R2^5 - R1^5)/(R2^3 - R1^3)
    """
    denom = outer_radius**3 - inner_radius**3
    if denom == 0:
        raise ValueError("Los radios no pueden ser iguales")
    return (2.0 / 5.0) * mass * ((outer_radius**5 - inner_radius**5) / denom)


def inertia_ring_diameter_axis(mass: float, radius: float) -> float:
    """Anillo respecto a un diámetro: I=1/2 mR^2."""
    return 0.5 * mass * radius * radius


def inertia_disk_diameter_axis(mass: float, radius: float) -> float:
    """Disco respecto a un diámetro: I=1/4 mR^2."""
    return 0.25 * mass * radius * radius


def inertia_cylinder_central_perpendicular(
    mass: float, radius: float, length: float
) -> float:
    """Cilindro respecto a eje central perpendicular: I=1/4 mR^2 + 1/12 mL^2."""
    return 0.25 * mass * radius * radius + (1.0 / 12.0) * mass * length * length


def inertia_rod_center(mass: float, length: float) -> float:
    return (1.0 / 12.0) * mass * length * length


def inertia_rectangular_plate_center(mass: float, a: float, b: float) -> float:
    return (1.0 / 12.0) * mass * (a * a + b * b)


def parallel_axis_theorem(inertia_cm: float, mass: float, distance: float) -> float:
    """Steiner: I = I_cm + m d^2."""
    return inertia_cm + mass * distance * distance


def rolling_condition_speed(omega: float, radius: float) -> float:
    """Rodadura pura: v_cm = omega R."""
    return omega * radius


def rolling_condition_omega(v_cm: float, radius: float) -> float:
    if radius <= 0:
        raise ValueError("El radio debe ser positivo")
    return v_cm / radius


def rolling_contact_speed(v_cm: float, omega: float, radius: float) -> float:
    """Velocidad del punto de contacto inferior: v_p = v_cm - omega R."""
    return v_cm - omega * radius


def rolling_state(
    v_cm: float, omega: float, radius: float, eps: float = 1e-9
) -> Literal[
    "rodadura pura", "deslizamiento puro", "rotacion pura", "rodadura con deslizamiento"
]:
    if abs(v_cm) < eps and abs(omega) < eps:
        return "rodadura pura"
    if abs(omega) < eps and abs(v_cm) >= eps:
        return "deslizamiento puro"
    if abs(v_cm) < eps and abs(omega) >= eps:
        return "rotacion pura"
    if abs(rolling_contact_speed(v_cm, omega, radius)) < eps:
        return "rodadura pura"
    return "rodadura con deslizamiento"


def rolling_acceleration_incline(
    angle: float, inertia: float, mass: float, radius: float, g: float = G_EARTH
) -> float:
    """
    Aceleración de rodadura pura en plano inclinado:
    a = g sin(theta) / (1 + I/(mR^2)).
    """
    if mass <= 0 or radius <= 0:
        raise ValueError("mass y radius deben ser positivos")
    return g * sin(angle) / (1.0 + inertia / (mass * radius * radius))


def rolling_acceleration_solid_sphere(angle: float, g: float = G_EARTH) -> float:
    return (5.0 / 7.0) * g * sin(angle)


def rolling_acceleration_solid_cylinder(angle: float, g: float = G_EARTH) -> float:
    return (2.0 / 3.0) * g * sin(angle)


def rolling_acceleration_hollow_cylinder(angle: float, g: float = G_EARTH) -> float:
    return 0.5 * g * sin(angle)


def rolling_friction_torque(Crr: float, radius: float, normal_force: float) -> float:
    """Torque de rozamiento por rodadura: tau = Crr R Fn."""
    return Crr * radius * normal_force


def rolling_friction_torque_limited(
    Crr: float,
    radius: float,
    normal_force: float,
    inertia: float,
    omega: float,
    dt: float,
) -> float:
    """
    Torque de rodadura limitado para no invertir el giro en un paso:
    min(Crr R Fn, I |omega| / dt) con signo opuesto a omega.
    """
    tau = rolling_friction_torque(Crr, radius, normal_force)
    tau_stop = inertia * abs(omega) / dt
    return -sign(omega) * min(tau, tau_stop)


# =============================================================================
# Colisiones 1D/2D, momento e impulso
# =============================================================================


def linear_momentum(mass: float, velocity: float) -> float:
    return mass * velocity


def linear_momentum_vector(mass: float, velocity: Vector3) -> Vector3:
    return vec3_scale(velocity, mass)


def total_momentum(masses: Sequence[float], velocities: Sequence[Vector3]) -> Vector3:
    if len(masses) != len(velocities):
        raise ValueError("masses y velocities deben tener la misma longitud")
    total = (0.0, 0.0, 0.0)
    for m, v in zip(masses, velocities):
        total = vec3_add(total, linear_momentum_vector(m, v))
    return total


def center_of_mass_velocity(
    masses: Sequence[float], velocities: Sequence[Vector3]
) -> Vector3:
    total_mass = sum(masses)
    if total_mass == 0:
        raise ValueError("La masa total no puede ser cero")
    return vec3_scale(total_momentum(masses, velocities), 1.0 / total_mass)


def coefficient_of_restitution(
    v1_i: float, v2_i: float, v1_f: float, v2_f: float
) -> float:
    """e = -(v2f - v1f)/(v2i - v1i)."""
    denom = v2_i - v1_i
    if denom == 0:
        raise ValueError("Las velocidades relativas iniciales no pueden ser iguales")
    return -(v2_f - v1_f) / denom


def combined_elasticity(e1: float, e2: float) -> float:
    """Coeficiente de restitución combinado estilo Pymunk: e_total = e1 * e2."""
    return e1 * e2


def collision_1d_final_velocities(
    m1: float, v1: float, m2: float, v2: float, e: float = 1.0
) -> tuple[float, float]:
    """
    Colisión 1D con coeficiente de restitución e.
    Conserva momento lineal y cumple v2f - v1f = -e(v2i - v1i).
    """
    if m1 <= 0 or m2 <= 0:
        raise ValueError("Las masas deben ser positivas")
    v1f = ((m1 - e * m2) * v1 + (1.0 + e) * m2 * v2) / (m1 + m2)
    v2f = ((m2 - e * m1) * v2 + (1.0 + e) * m1 * v1) / (m1 + m2)
    return v1f, v2f


def perfectly_inelastic_velocity(m1: float, v1: float, m2: float, v2: float) -> float:
    """Velocidad común tras colisión perfectamente inelástica."""
    if m1 + m2 == 0:
        raise ValueError("La masa total no puede ser cero")
    return (m1 * v1 + m2 * v2) / (m1 + m2)


def impulse_1d_for_collision(
    m1: float, v1: float, m2: float, v2: float, e: float = 1.0
) -> float:
    """
    Impulso normal 1D aplicado sobre el cuerpo 1:
    J = -(1+e)(v1-v2) / (1/m1 + 1/m2)
    """
    if m1 <= 0 or m2 <= 0:
        raise ValueError("Las masas deben ser positivas")
    return -(1.0 + e) * (v1 - v2) / (1.0 / m1 + 1.0 / m2)


def reflect_velocity_against_wall(
    velocity: Vector2, normal: Vector2, restitution: float = 1.0
) -> Vector2:
    """
    Rebote contra pared con normal unitaria.
    v' = v - (1+e)(v·n)n
    """
    n = vec2_unit(normal)
    vn = vec2_dot(velocity, n)
    return vec2_sub(velocity, vec2_scale(n, (1.0 + restitution) * vn))


def decompose_velocity_normal_tangent(
    velocity: Vector2, normal: Vector2
) -> tuple[Vector2, Vector2]:
    """Descompone v en componente normal y tangencial respecto a una normal de contacto."""
    n = vec2_unit(normal)
    v_n = vec2_scale(n, vec2_dot(velocity, n))
    v_t = vec2_sub(velocity, v_n)
    return v_n, v_t


def oblique_collision_no_friction_2d(
    m1: float,
    v1: Vector2,
    m2: float,
    v2: Vector2,
    normal: Vector2,
    e: float = 1.0,
) -> tuple[Vector2, Vector2]:
    """
    Colisión oblicua 2D sin fricción.
    Solo modifica las componentes normales. Las tangenciales se conservan.
    """
    n = vec2_unit(normal)
    t = vec2_perpendicular(n)

    v1n = vec2_dot(v1, n)
    v2n = vec2_dot(v2, n)
    v1t = vec2_dot(v1, t)
    v2t = vec2_dot(v2, t)

    v1n_f, v2n_f = collision_1d_final_velocities(m1, v1n, m2, v2n, e)

    v1_f = vec2_add(vec2_scale(n, v1n_f), vec2_scale(t, v1t))
    v2_f = vec2_add(vec2_scale(n, v2n_f), vec2_scale(t, v2t))
    return v1_f, v2_f


def angular_momentum_particles(
    positions: Sequence[Vector3], masses: Sequence[float], velocities: Sequence[Vector3]
) -> Vector3:
    """Momento angular total: L = sum r_i x m_i v_i."""
    if not (len(positions) == len(masses) == len(velocities)):
        raise ValueError("positions, masses y velocities deben tener la misma longitud")
    total = (0.0, 0.0, 0.0)
    for r, m, v in zip(positions, masses, velocities):
        total = vec3_add(total, vec3_cross(r, vec3_scale(v, m)))
    return total


def orbital_speed_from_angular_momentum(
    radius_initial: float, speed_initial: float, radius_final: float
) -> float:
    """
    Conservación L para fuerza central y misma masa:
    m r_i v_i = m r_f v_f -> v_f = r_i v_i / r_f.
    """
    if radius_final == 0:
        raise ValueError("radius_final no puede ser cero")
    return radius_initial * speed_initial / radius_final


def point_velocity_from_rotation(
    v_cm: Vector3, omega: Vector3, r_cm_to_point: Vector3
) -> Vector3:
    """Velocidad de un punto del rígido: v_P = v_CM + omega x r."""
    return vec3_add(v_cm, vec3_cross(omega, r_cm_to_point))


def cm_velocity_for_desired_point_velocity(
    v_point: Vector3, omega: Vector3, r_cm_to_point: Vector3
) -> Vector3:
    """v_CM = v_P - omega x r."""
    return vec3_sub(v_point, vec3_cross(omega, r_cm_to_point))


# =============================================================================
# Tablas útiles de coeficientes
# =============================================================================

DRAG_COEFFICIENTS_2D: dict[str, float] = {
    "placa_plana": 2.0,
    "cilindro_cuadrado": 2.1,
    "cilindro_cuadrado_girado": 1.6,
    "semicilindro_convexo": 1.7,
    "semicilindro_plano": 1.2,
    "cilindro_circular": 1.2,
    "cilindro_eliptico_2_1": 0.6,
    "semicilindro_concavo": 2.3,
}

DRAG_COEFFICIENTS_3D: dict[str, float] = {
    "placa_cuadrada": 1.17,
    "cubo_cara_plana": 1.06,
    "cubo_girado": 0.805,
    "hemisferio_solido": 0.42,
    "cono_60": 0.5,
    "esfera": 0.44,
    "elipsoide_2_1": 0.27,
    "hemisferio_concavo": 1.4,
    "hemisferio_convexo": 0.39,
}

LAMINAR_SHAPE_FACTORS: dict[str, float] = {
    "esfera": 1.0,
    "cubo": 1.08,
    "prisma_rectangular_1_1_2": 1.14,
}

VISCOSITIES_PA_S_20C: dict[str, float] = {
    "aire": 1.8e-5,
    "agua": 1.0e-3,
    "aceite_oliva": 0.081,
    "glicerina": 1.48,
    "miel": 10.0,
}


def drag_coefficient_2d(shape: str) -> float:
    try:
        return DRAG_COEFFICIENTS_2D[shape]
    except KeyError as exc:
        raise KeyError(f"Forma 2D no conocida: {shape}") from exc


def drag_coefficient_3d(shape: str) -> float:
    try:
        return DRAG_COEFFICIENTS_3D[shape]
    except KeyError as exc:
        raise KeyError(f"Forma 3D no conocida: {shape}") from exc


def laminar_shape_factor(shape: str) -> float:
    try:
        return LAMINAR_SHAPE_FACTORS[shape]
    except KeyError as exc:
        raise KeyError(f"Factor de forma laminar no conocido: {shape}") from exc


def viscosity_20c(fluid: str) -> float:
    try:
        return VISCOSITIES_PA_S_20C[fluid]
    except KeyError as exc:
        raise KeyError(f"Fluido no conocido: {fluid}") from exc


# =============================================================================
# Conversión de escala para videojuegos
# =============================================================================


def meters_to_pixels(meters: float, pixels_per_meter: float) -> float:
    return meters * pixels_per_meter


def pixels_to_meters(pixels: float, pixels_per_meter: float) -> float:
    if pixels_per_meter == 0:
        raise ValueError("pixels_per_meter no puede ser cero")
    return pixels / pixels_per_meter


def acceleration_mps2_to_pxps2(accel: float, pixels_per_meter: float) -> float:
    return accel * pixels_per_meter


def acceleration_pxps2_to_mps2(accel_px: float, pixels_per_meter: float) -> float:
    return pixels_to_meters(accel_px, pixels_per_meter)


# =============================================================================
# Pequeña batería de pruebas manuales
# =============================================================================

if __name__ == "__main__":
    # Ejemplos rápidos para comprobar que el módulo funciona.
    print(
        "Proyectil 20 m/s, 45º, t=1:",
        projectile_state_no_drag(0, 0, 20, radians(45), 1),
    )
    print(
        "Re esfera pelota tenis aprox:", reynolds_number(1.225, 50, 0.067, MU_AIR_20C)
    )
    print("Cd esfera transición Re=500:", sphere_cd_intermediate(500))
    print("v max curva R=50, mu=0.8:", max_curve_speed_from_friction(0.8, 50))
    print("Colisión 1D elástica:", collision_1d_final_velocities(1, 10, 1, 0, 1))
