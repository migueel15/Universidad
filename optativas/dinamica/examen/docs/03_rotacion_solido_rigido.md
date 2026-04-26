# Tema 3 — Dinámica de rotación y sólido rígido

> **Restricción aplicada:** todos los ejemplos están escritos solo con `pymunk` como motor físico, `pygame` como visor y módulos estándar de Python como `math`. Cuando las diapositivas o los códigos originales usan PyBullet, NumPy, SciPy o Tkinter, aquí se mantiene el concepto físico pero se adapta a Pymunk/Pygame.


## Índice

1. Introducción a la rotación
2. Fuerza centrípeta
3. Centro de masas
4. Torque
5. Sólido rígido y momento de inercia
6. Rodadura y deslizamiento
7. Orientación en 2D y adaptación de conceptos 3D
8. Rozamiento por rodadura
9. Caso integrador: billar

---

## 1. Introducción a la rotación

En dinámica de la partícula tratábamos objetos como puntos. En rotación, el objeto tiene extensión espacial. Esto permite que una fuerza aplicada fuera del centro genere giro.

Magnitudes angulares:

\[
\omega = \frac{d\theta}{dt}, \qquad \alpha = \frac{d\omega}{dt}
\]

En Pymunk:

- `body.angle`: orientación en radianes.
- `body.angular_velocity`: velocidad angular en rad/s.
- `body.torque`: torque acumulado antes del step.
- `body.moment`: momento de inercia.

### Código: cuerpo que gira por torque

```python
import pygame
import pymunk

pygame.init()
screen = pygame.display.set_mode((700, 500))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 0)

mass = 2
size = (120, 40)
moment = pymunk.moment_for_box(mass, size)
body = pymunk.Body(mass, moment)
body.position = (350, 250)
shape = pymunk.Poly.create_box(body, size)
space.add(body, shape)

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    body.torque = 5000
    space.step(1 / 60)

    screen.fill((255, 255, 255))
    points = [body.local_to_world(v) for v in shape.get_vertices()]
    pygame.draw.polygon(screen, (220, 120, 40), points)
    pygame.display.flip()
    clock.tick(60)

pygame.quit()
```

---

## 2. Fuerza centrípeta

Para que un cuerpo con velocidad `v` describa una curva de radio `R`, necesita una fuerza perpendicular a la velocidad:

\[
F_c = m\frac{v^2}{R}
\]

Sin esta fuerza, por inercia, el cuerpo continúa en línea recta.

### Condición de agarre

\[
m\frac{v^2}{R} \le F_r
\]

Si la fuerza necesaria supera la fricción disponible, el vehículo derrapa.

### Código: fuerza centrípeta artificial

Basado en el enfoque de `fcentripeta01.py`.

```python
from pymunk import Vec2d


def apply_centripetal_force(body, center, radius):
    v = Vec2d(body.velocity)
    if v.length == 0:
        return

    needed = body.mass * v.length ** 2 / radius
    to_center = Vec2d(*center) - body.position
    if to_center.length == 0:
        return

    force = to_center.normalized() * needed
    body.apply_force_at_world_point(force, body.position)
```

### Peralte

En una curva peraltada, parte del peso ayuda a generar fuerza centrípeta. En 2D con Pymunk, si no modelas la geometría 3D de la carretera, puedes aproximarlo aumentando la fuerza lateral disponible:

```python
def max_lateral_force(mass, g, mu, bank_angle_rad):
    # Aproximación simple: rozamiento + componente útil del peso.
    normal = mass * g
    friction = mu * normal
    bank_help = mass * g * math.sin(bank_angle_rad)
    return friction + bank_help
```

---

## 3. Centro de masas

El centro de masas es el punto donde puede considerarse concentrada la masa para describir la traslación. En Pymunk, el origen local del `Body` se interpreta como su centro de masas.

Para figuras simétricas y densidad uniforme:

- Círculo: CM en el centro geométrico.
- Rectángulo: CM en el centro geométrico.
- Triángulo: baricentro.

### Centroide de un triángulo

\[
x_{CM}=\frac{x_1+x_2+x_3}{3}, \qquad y_{CM}=\frac{y_1+y_2+y_3}{3}
\]

```python
from pymunk import Vec2d


def triangle_centroid(vertices):
    a, b, c = [Vec2d(*v) for v in vertices]
    return (a + b + c) / 3
```

### Centroide de polígono simple

```python
from pymunk import Vec2d


def polygon_centroid(vertices):
    pts = [Vec2d(*v) for v in vertices]
    area2 = 0.0
    cx = 0.0
    cy = 0.0

    for i, p in enumerate(pts):
        q = pts[(i + 1) % len(pts)]
        cross = p.x * q.y - q.x * p.y
        area2 += cross
        cx += (p.x + q.x) * cross
        cy += (p.y + q.y) * cross

    area = area2 / 2.0
    if abs(area) < 1e-9:
        return Vec2d(0, 0)
    return Vec2d(cx / (6 * area), cy / (6 * area))
```

### Crear un polígono centrado en su CM

Si defines un triángulo con vértices `[(0,0), (80,0), (40,80)]`, el origen local no está en el centroide. Conviene recentrar.

```python
import pymunk

vertices = [(0, 0), (80, 0), (40, 80)]
centroid = polygon_centroid(vertices)
local_vertices = [Vec2d(*v) - centroid for v in vertices]

mass = 1
moment = pymunk.moment_for_poly(mass, local_vertices)
body = pymunk.Body(mass, moment)
shape = pymunk.Poly(body, local_vertices)
```

---

## 4. Torque

El torque mide la capacidad de una fuerza para producir giro:

\[
\tau = rF\sin\varphi
\]

En 2D:

\[
\tau_z = r_xF_y - r_yF_x
\]

Pymunk calcula esto automáticamente cuando aplicas una fuerza en un punto distinto del CM.

### Código: fuerza excéntrica

```python
# Fuerza hacia arriba aplicada a 40 px a la derecha del centro.
body.apply_force_at_local_point((0, -500), (40, 0))
```

### Código: equivalencia fuerza + torque

```python
from pymunk import Vec2d


def apply_force_and_debug_torque(body, force, local_point):
    r = Vec2d(*local_point)
    f = Vec2d(*force)
    torque_z = r.x * f.y - r.y * f.x
    body.apply_force_at_local_point(f, r)
    return torque_z
```

---

## 5. Sólido rígido y momento de inercia

Un sólido rígido mantiene constantes las distancias internas. Su movimiento se descompone en:

- Traslación del centro de masas.
- Rotación alrededor del centro de masas.

El momento de inercia `I` es la resistencia al cambio de rotación:

\[
I = \sum m_ir_i^2 \approx \int r^2dm
\]

La ecuación angular equivalente a `F = ma` es:

\[
\sum \tau = I\alpha
\]

### Inercias en Pymunk

```python
import pymunk

mass = 1.0
radius = 30
inertia_circle = pymunk.moment_for_circle(mass, 0, radius)

size = (160, 40)
inertia_box = pymunk.moment_for_box(mass, size)

vertices = [(-20, -20), (20, -20), (0, 30)]
inertia_poly = pymunk.moment_for_poly(mass, vertices)
```

### Teorema de Steiner

Si el eje no pasa por el CM:

\[
I = I_{CM} + md^2
\]

En Pymunk, el parámetro `offset` de algunas funciones de inercia permite tener en cuenta desplazamientos.

```python
mass = 2.0
radius = 20
cm_inertia = pymunk.moment_for_circle(mass, 0, radius)
d = 50
steiner_inertia = cm_inertia + mass * d * d
```

---

## 6. Rodadura y deslizamiento

La condición de rodadura pura es:

\[
v_{CM} = \omega R
\]

Si no se cumple, hay deslizamiento:

- `ω = 0, v > 0`: desliza sin girar.
- `v = 0, ω > 0`: gira en el sitio.
- `v != ωR`: rodadura con deslizamiento.

### Código: comprobar estado de una rueda

```python

def rolling_state(body, radius, tolerance=1.0):
    v = body.velocity.length
    omega_r = abs(body.angular_velocity) * radius
    diff = v - omega_r

    if abs(diff) < tolerance:
        return "rodadura pura"
    if body.angular_velocity == 0 and v > tolerance:
        return "deslizamiento puro"
    if v < tolerance and abs(body.angular_velocity) > 0:
        return "patinaje en sitio"
    return "rodadura con deslizamiento"
```

### Plano inclinado

Para rodadura pura:

\[
a_{CM}=\frac{g\sin\theta}{1+\frac{I}{MR^2}}
\]

Cuanto menor es el momento de inercia relativo, más rápido acelera el cuerpo.

---

## 7. Orientación en 2D y adaptación de conceptos 3D

Las diapositivas explican ángulos de Euler, gimbal lock y cuaterniones en 3D. Bajo la restricción Pymunk/Pygame, trabajamos en 2D, así que no aparece gimbal lock: solo hay un ángulo `body.angle`.

### Código: orientación visual de una bola

En el billar proporcionado, las bolas dibujan una marca que rota con `body.angle`.

```python
import math
import pygame


def draw_rotating_mark(screen, body, radius):
    center = body.position
    end = center + pymunk.Vec2d(radius, 0).rotated(body.angle)
    pygame.draw.line(screen, (0, 0, 0), center, end, 2)
```

### Código: texto rotado con una bola

```python
import math
import pygame


def draw_rotated_text(screen, font, text, center, angle_rad):
    surface = font.render(text, True, (0, 0, 0))
    rotated = pygame.transform.rotate(surface, -math.degrees(angle_rad))
    rect = rotated.get_rect(center=center)
    screen.blit(rotated, rect)
```

---

## 8. Rozamiento por rodadura

El rozamiento por rodadura aparece por deformación de los cuerpos en contacto. La normal no pasa exactamente por debajo del CM y genera un torque de frenado:

\[
\tau = C_{rr}RF_N
\]

`Crr` suele ser mucho menor que la fricción por deslizamiento.

### Torque manual de rodadura

```python
import math


def apply_rolling_resistance(body, radius, crr, g=900, dt=1/60):
    omega = body.angular_velocity
    if abs(omega) < 1e-3:
        body.angular_velocity = 0
        return

    normal = body.mass * g
    tau_roll = crr * radius * normal
    tau_stop = body.moment * abs(omega) / dt
    tau = min(tau_roll, tau_stop)

    body.torque += -math.copysign(tau, omega)
```

### Fricción avanzada inspirada en `billar04.py`

En el billar original se distingue entre deslizamiento inicial y rodadura posterior. La idea es usar más fricción mientras la bola patina y menos cuando rueda.

```python
from pymunk import Vec2d


def apply_ball_friction(body, radius, state, dt, g=9.8):
    """
    state contiene:
      state['is_rolling']
      state['v_check']
    """
    v = Vec2d(body.velocity)
    speed = v.length

    if speed < 0.005:
        body.velocity = (0, 0)
        body.angular_velocity = 0
        return

    if not state['is_rolling'] and speed <= (5 / 7) * state['v_check']:
        state['is_rolling'] = True

    mu_sliding = 0.015
    crr_rolling = 0.01
    mu = crr_rolling if state['is_rolling'] else mu_sliding

    fn = body.mass * g
    f_mag = mu * fn
    f_stop = body.mass * speed / dt
    f_final = min(f_mag, f_stop)

    body.apply_force_at_world_point(-v.normalized() * f_final, body.position)
```

---

## 9. Caso integrador: billar

El billar une muchos elementos del tema:

- Cuerpos circulares con masa e inercia.
- Elasticidad en bolas y bandas.
- Fricción bola-bola y bola-banda.
- Impulso inicial de la bola blanca.
- Rodadura y frenado.
- Conversión metros ↔ píxeles.
- Dibujo de bolas con rotación.

### Código: bola con impulso

```python
import pymunk
from pymunk import Vec2d

class Ball:
    def __init__(self, space, pos, radius=0.0285, mass=0.17):
        moment = pymunk.moment_for_circle(mass, 0, radius)
        self.body = pymunk.Body(mass, moment)
        self.body.position = pos
        self.shape = pymunk.Circle(self.body, radius)
        self.shape.elasticity = 0.8
        self.shape.friction = 0.055
        self.radius = radius
        self.state = {"is_rolling": False, "v_check": 0.0}
        space.add(self.body, self.shape)

    def apply_impulse_as_velocity(self, velocity_vector):
        impulse = self.body.mass * Vec2d(*velocity_vector)
        self.body.apply_impulse_at_world_point(impulse, self.body.position)
        self.state["is_rolling"] = False
        self.state["v_check"] = self.body.velocity.length
```

### Código: guía de tiro paralela

```python
from pymunk import Vec2d
import pygame


def draw_aiming_help(screen, ball_body, radius_px, angle, to_screen):
    direction = Vec2d(1, 0).rotated(angle)
    normal = Vec2d(-direction.y, direction.x)
    pos_px = to_screen(ball_body.position)
    length = 1000

    for sign in (1, -1):
        start = Vec2d(*pos_px) + sign * normal * radius_px
        end = start + direction * length
        pygame.draw.line(screen, (255, 150, 150), start, end, 1)
```

---

## Checklist de implementación

- Usa `moment_for_circle`, `moment_for_box` o `moment_for_poly`.
- Re-centra polígonos si los vértices no están definidos respecto al CM.
- Aplica fuerzas fuera del CM para generar rotación.
- Recuerda que `body.angular_velocity` está en rad/s.
- Para rodadura realista, combina velocidad lineal, velocidad angular y torque de frenado.
- En billar o deportes, usa dimensiones reales y conversión a píxeles.
