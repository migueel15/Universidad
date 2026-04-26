# Tema 4 — Proyectiles

> **Restricción aplicada:** todos los ejemplos están escritos solo con `pymunk` como motor físico, `pygame` como visor y módulos estándar de Python como `math`. Cuando las diapositivas o los códigos originales usan PyBullet, NumPy, SciPy o Tkinter, aquí se mantiene el concepto físico pero se adapta a Pymunk/Pygame.


## Índice

1. Introducción
2. Modelo solo con gravedad
3. Resistencia del aire
4. Variación con la altitud
5. Efecto del viento
6. Rotación y efecto Magnus
7. Simulador Pymunk/Pygame integrador
8. Checklist de implementación

---

## 1. Introducción

Un proyectil es un objeto lanzado al espacio que, tras el lanzamiento, está dominado por fuerzas externas: gravedad, resistencia del aire, viento y, si rota, efecto Magnus.

Modelos progresivos:

1. Solo gravedad: trayectoria parabólica ideal.
2. Gravedad + drag: trayectoria más realista.
3. Drag variable: densidad del aire, Reynolds y Mach.
4. Viento: el drag depende de la velocidad relativa al aire.
5. Rotación: efecto Magnus y torque de frenado.

---

## 2. Modelo solo con gravedad

Condiciones iniciales:

\[
v_{x0}=v_0\cos\theta, \qquad v_{y0}=v_0\sin\theta
\]

Movimiento ideal:

\[
x(t)=x_0+v_{x0}t
\]

\[
y(t)=y_0+v_{y0}t-\frac12gt^2
\]

En Pygame, como `y` crece hacia abajo, si usas coordenadas de pantalla normalmente la gravedad se suma en `+y`.

### Código: trayectoria analítica

```python
import math


def projectile_position(x0, y0, speed, angle_rad, t, g=9.81):
    vx0 = speed * math.cos(angle_rad)
    vy0 = speed * math.sin(angle_rad)
    x = x0 + vx0 * t
    y = y0 + vy0 * t - 0.5 * g * t * t
    return x, y
```

### Código: proyectil simple en Pymunk

```python
import math
import pygame
import pymunk

pygame.init()
screen = pygame.display.set_mode((900, 600))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 900)

mass, radius = 1.0, 15
body = pymunk.Body(mass, pymunk.moment_for_circle(mass, 0, radius))
body.position = (100, 500)
shape = pymunk.Circle(body, radius)
space.add(body, shape)

speed = 600
angle = math.radians(-45)  # negativo porque y crece hacia abajo
body.velocity = (speed * math.cos(angle), speed * math.sin(angle))

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    space.step(1 / 60)
    screen.fill((255, 255, 255))
    pygame.draw.circle(screen, (220, 80, 60), body.position, radius)
    pygame.display.flip()
    clock.tick(60)

pygame.quit()
```

Limitaciones del modelo ideal:

- La masa no influye en la trayectoria.
- El alcance es exagerado.
- La velocidad de salida e impacto son simétricas.
- No hay fuerzas laterales.

---

## 3. Resistencia del aire

La resistencia del aire se opone a la velocidad relativa del proyectil. Hay dos modelos principales:

### Régimen laminar: Stokes

\[
F_v \propto v
\]

Domina en objetos pequeños, fluidos viscosos o velocidades bajas.

### Régimen turbulento: Newton

\[
\vec F_d = -\frac12\rho C_d A |\vec v|\vec v
\]

Domina en aire para proyectiles, balones y vehículos. El factor `|v|v` hace que la magnitud sea proporcional a `v²` y que la dirección sea opuesta a la velocidad.

### Número de Reynolds

\[
Re = \frac{\rho vL}{\mu}
\]

- `ρ`: densidad del fluido.
- `v`: velocidad.
- `L`: longitud característica.
- `μ`: viscosidad dinámica.

### Código: Cd de una esfera sin NumPy

Basado en `rozamiento_aire.py`, eliminando NumPy.

```python
import math


def sphere_cd(reynolds, crisis=False):
    if reynolds <= 0:
        return 0.0

    if reynolds < 1000:
        return (24.0 / reynolds) * (1.0 + 0.15 * reynolds ** 0.687)

    if reynolds <= 2e5 or not crisis:
        return 0.44

    if reynolds <= 3e5:
        return log_interp(reynolds, 2e5, 0.44, 3e5, 0.10)

    if reynolds <= 2e6:
        return log_interp(reynolds, 3e5, 0.10, 2e6, 0.20)

    return 0.20


def log_interp(x, x1, y1, x2, y2):
    lx = math.log10(x)
    lx1, lx2 = math.log10(x1), math.log10(x2)
    ly1, ly2 = math.log10(y1), math.log10(y2)
    ly = ly1 + (ly2 - ly1) * (lx - lx1) / (lx2 - lx1)
    return 10 ** ly
```

### Corrección de Mach

Para velocidades cercanas o superiores al sonido:

```python

def mach_correction(speed, sound_speed=340.0):
    mach = speed / sound_speed
    if mach < 0.8:
        return 1.0
    if mach < 1.2:
        return 1.0 + 1.25 * (mach - 0.8)
    return 1.5 + 0.5 / mach
```

### Aplicar drag en Pymunk

```python
from pymunk import Vec2d
import math


def apply_newton_drag(body, radius_m, rho=1.225, cd=0.44, px_m=100.0):
    # Convertimos velocidad de px/s a m/s.
    v_px = Vec2d(body.velocity)
    v_m = v_px / px_m
    speed = v_m.length
    if speed < 1e-6:
        return

    area = math.pi * radius_m * radius_m
    force_m = -0.5 * rho * cd * area * speed * v_m  # N si masa en kg y metros

    # Convertimos fuerza en m-unidades a px-unidades: F_px = F_m * px_m
    body.apply_force_at_world_point(force_m * px_m, body.position)
```

---

## 4. Variación con la altitud

La densidad del aire disminuye con la altura. En la troposfera se puede usar:

\[
\rho(h) \approx \rho_0\left(1-\frac{Lh}{T_0}\right)^{\frac{g}{RL}-1}
\]

### Código: densidad con altura

```python

def air_density_at_altitude(h_m):
    rho0 = 1.225
    t0 = 288.15
    lapse = 0.0065
    gas_r = 287.05
    g = 9.80665

    h = max(0.0, min(h_m, 11000.0))
    temp = t0 - lapse * h
    exponent = (g / (gas_r * lapse)) - 1.0
    return rho0 * (temp / t0) ** exponent
```

### Velocidad del sonido

\[
c(T) = \sqrt{\gamma RT}
\]

```python
import math


def sound_speed_temp(temp_k):
    gamma = 1.4
    gas_r = 287.05
    return math.sqrt(gamma * gas_r * temp_k)


def sound_speed_altitude(h_m, t0=288.15):
    lapse = 0.0065
    h = max(0.0, min(h_m, 11000.0))
    return sound_speed_temp(t0 - lapse * h)
```

### Altitud en una pantalla Pygame

Si el suelo está en `ground_y`, la altura física puede obtenerse así:

```python

def altitude_from_screen_y(y_px, ground_y_px, px_m):
    return max(0.0, (ground_y_px - y_px) / px_m)
```

---

## 5. Efecto del viento

El viento no añade una fuerza independiente. Cambia la velocidad relativa respecto al aire:

\[
\vec v_{rel} = \vec v_{proyectil} - \vec v_{viento}
\]

El drag se calcula con `v_rel`, no con la velocidad respecto al suelo.

### Código: velocidad relativa

```python
from pymunk import Vec2d


def relative_air_velocity(projectile_velocity, wind_velocity):
    return Vec2d(projectile_velocity) - Vec2d(wind_velocity)
```

### Código: drag con viento

```python
import math
from pymunk import Vec2d


def apply_drag_with_wind(body, radius_m, wind_m_s, rho, cd, px_m):
    v_rel = Vec2d(body.velocity) / px_m - Vec2d(wind_m_s)
    speed = v_rel.length
    if speed < 0.1:
        return

    area = math.pi * radius_m * radius_m
    force_m = -0.5 * rho * cd * area * speed * v_rel
    body.apply_force_at_world_point(force_m * px_m, body.position)
```

Si el proyectil se mueve exactamente con el aire, `v_rel ≈ 0` y la fuerza aerodinámica desaparece.

---

## 6. Rotación y efecto Magnus

Cuando un proyectil gira, arrastra aire cercano y aparece una diferencia de presiones. La fuerza Magnus es perpendicular a la velocidad relativa y al eje de giro.

En 2D:

- Si `ω > 0` en Pymunk/Pygame, la rotación es horaria.
- La fuerza Magnus queda a un lado de la velocidad.

\[
\vec F_M = \frac12\rho v_{rel}^2 C_M A \hat u_M
\]

El coeficiente `CM` puede aproximarse mediante:

\[
S=\frac{r\omega}{v}, \qquad C_M \approx \frac{kS}{2+S}
\]

### Código: signo sin NumPy

```python

def sign(x):
    if x > 0:
        return 1
    if x < 0:
        return -1
    return 0
```

### Código: aplicar Magnus

```python
import math
from pymunk import Vec2d


def apply_magnus(body, radius_m, wind_m_s, rho, k, px_m):
    v_rel = Vec2d(body.velocity) / px_m - Vec2d(wind_m_s)
    speed = v_rel.length
    omega = body.angular_velocity

    if speed < 0.1 or abs(omega) < 1e-6:
        return

    # Perpendicular a la velocidad. En Pygame y crece hacia abajo.
    u = Vec2d(-v_rel.y, v_rel.x).normalized() * sign(omega)

    spin_ratio = abs(radius_m * omega) / speed
    cm = (k * spin_ratio) / (2.0 + spin_ratio)
    area = math.pi * radius_m * radius_m

    force_m = 0.5 * rho * speed * speed * cm * area * u
    body.apply_force_at_world_point(force_m * px_m, body.position)
```

Valores orientativos de `k`:

| Deporte | k |
|---|---:|
| Golf | 1.3 |
| Tenis | 1.1 |
| Béisbol | 0.9 |
| Fútbol | 0.7 |
| Voleibol | 0.6 |

### Torque de frenado rotacional

\[
\tau_{drag} = -\frac12\rho \omega^2 R^5 C_m
\]

```python
import math


def apply_rotational_drag(body, radius_m, rho, cm, px_m):
    omega = body.angular_velocity
    if abs(omega) < 1e-4:
        body.angular_velocity = 0
        return

    tau_m = 0.5 * rho * omega * omega * (radius_m ** 5) * cm
    body.torque += -math.copysign(tau_m * px_m * px_m, omega)
```

---

## 7. Simulador Pymunk/Pygame integrador

El siguiente ejemplo combina gravedad, densidad con altura, drag, viento y Magnus.

```python
import math
import pygame
import pymunk
from pymunk import Vec2d

WIDTH, HEIGHT = 1000, 650
FPS = 60
PX_M = 80.0
GROUND_Y = 600

pygame.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 9.81 * PX_M)
space.damping = 1.0

radius_m = 0.11
radius_px = int(radius_m * PX_M)
mass = 0.43
moment = pymunk.moment_for_circle(mass, 0, radius_px)
ball = pymunk.Body(mass, moment)
ball.position = (100, GROUND_Y - radius_px)
ball.velocity = (28 * PX_M, -22 * PX_M)
ball.angular_velocity = 35.0
shape = pymunk.Circle(ball, radius_px)
shape.elasticity = 0.6
space.add(ball, shape)

wind = Vec2d(4.0, 0.0)  # m/s
trail = []

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    altitude = altitude_from_screen_y(ball.position.y, GROUND_Y, PX_M)
    rho = air_density_at_altitude(altitude)
    cd = 0.44

    apply_drag_with_wind(ball, radius_m, wind, rho, cd, PX_M)
    apply_magnus(ball, radius_m, wind, rho, k=0.7, px_m=PX_M)
    apply_rotational_drag(ball, radius_m, rho, cm=0.018, px_m=PX_M)

    space.step(1 / FPS)
    trail.append(tuple(ball.position))
    trail = trail[-200:]

    screen.fill((245, 245, 245))
    pygame.draw.line(screen, (0, 0, 0), (0, GROUND_Y), (WIDTH, GROUND_Y), 2)
    if len(trail) > 1:
        pygame.draw.lines(screen, (80, 80, 80), False, trail, 2)
    pygame.draw.circle(screen, (230, 120, 60), ball.position, radius_px)
    pygame.display.flip()
    clock.tick(FPS)

pygame.quit()
```

---

## 8. Checklist de implementación

- Usa gravedad ideal para prototipos rápidos.
- Para balística realista, desactiva damping global artificial y aplica drag manual.
- Usa `v_rel = v_proyectil - v_viento`.
- Recalcula drag en cada step.
- Usa substepping si el proyectil es rápido.
- Para Magnus, usa una perpendicular a `v_rel` y el signo de `angular_velocity`.
- Si usas metros reales, define `PX_M` y convierte velocidades y fuerzas.
