# Tema 2 — Dinámica de la partícula

> **Restricción aplicada:** todos los ejemplos están escritos solo con `pymunk` como motor físico, `pygame` como visor y módulos estándar de Python como `math`. Cuando las diapositivas o los códigos originales usan PyBullet, NumPy, SciPy o Tkinter, aquí se mantiene el concepto físico pero se adapta a Pymunk/Pygame.


## Índice

1. Introducción
2. Leyes de Newton
3. Primer proyecto Pymunk/Pygame
4. Fuerzas gravitatorias
5. Fuerza elástica
6. Rozamiento y damping
7. Trabajo y energía
8. Checklist de implementación

---

## 1. Introducción

La dinámica de la partícula estudia cómo las fuerzas modifican el movimiento de un objeto cuando podemos aproximarlo como un punto con masa. En videojuegos, esta aproximación sirve para proyectiles, partículas, cajas simples, esferas y muchos objetos antes de entrar en rotación compleja.

Conceptos clave:

- La fuerza es vectorial.
- La masa mide inercia.
- El motor físico integra numéricamente el movimiento.
- El programador suele inyectar fuerzas explícitas: gravedad, viento, muelles, impulsos, rozamiento manual, etc.

---

## 2. Leyes de Newton

### Primera ley: inercia

Si la fuerza neta es cero, el objeto mantiene su estado: reposo o movimiento rectilíneo uniforme.

\[
\sum \vec F = 0 \Rightarrow \vec a = 0 \Rightarrow \vec v = \text{cte}
\]

En Pymunk puedes observarlo creando un espacio sin gravedad ni damping.

```python
import pygame
import pymunk

pygame.init()
screen = pygame.display.set_mode((700, 400))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 0)
space.damping = 1.0

body = pymunk.Body(1, pymunk.moment_for_circle(1, 0, 20))
body.position = (100, 200)
body.velocity = (150, 0)
shape = pymunk.Circle(body, 20)
space.add(body, shape)

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    space.step(1 / 60)
    screen.fill((255, 255, 255))
    pygame.draw.circle(screen, (60, 130, 220), body.position, 20)
    pygame.display.flip()
    clock.tick(60)

pygame.quit()
```

### Segunda ley: fuerza y aceleración

\[
\vec F = m\vec a
\]

En Pymunk aplicamos fuerzas con métodos como:

```python
body.apply_force_at_world_point((fx, fy), body.position)
```

Si se aplica en el centro de masas, cambia principalmente la traslación. Si se aplica fuera del centro, también genera torque.

```python
# Fuerza horizontal en el centro: traslación
body.apply_force_at_world_point((500, 0), body.position)

# Fuerza fuera del centro: traslación + rotación
body.apply_force_at_world_point((500, 0), body.position + (0, -20))
```

### Tercera ley: acción y reacción

Si un objeto ejerce una fuerza sobre otro, el segundo ejerce una fuerza igual y opuesta. En colisiones normales, Pymunk ya se encarga de esta pareja acción-reacción. Si aplicas fuerzas manuales entre dos cuerpos, debes aplicarlas a ambos.

```python
from pymunk import Vec2d


def apply_pair_force(body_a, body_b, force_on_b):
    f = Vec2d(*force_on_b)
    body_b.apply_force_at_world_point(f, body_b.position)
    body_a.apply_force_at_world_point(-f, body_a.position)
```

---

## 3. Primer proyecto Pymunk/Pygame

La estructura mínima viene del ejemplo `hello_munk.py`: crear ventana, crear `Space`, añadir suelo, crear cuerpo + shape y avanzar la simulación.

### Código completo: caja cayendo sobre suelo

```python
import pygame
import pymunk

WIDTH, HEIGHT = 600, 600
FPS = 60

pygame.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 900)

floor = pymunk.Segment(space.static_body, (0, 550), (600, 550), 5)
floor.elasticity = 0.5
floor.friction = 0.8
space.add(floor)

mass = 1.0
size = 50
moment = pymunk.moment_for_box(mass, (size, size))
body = pymunk.Body(mass, moment)
body.position = (300, 50)
shape = pymunk.Poly.create_box(body, (size, size))
shape.elasticity = 0.8
shape.friction = 0.8
space.add(body, shape)

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    space.step(1.0 / FPS)

    screen.fill((255, 255, 255))
    pygame.draw.line(screen, (0, 0, 0), floor.a, floor.b, 5)

    points = [body.local_to_world(v) for v in shape.get_vertices()]
    pygame.draw.polygon(screen, (235, 129, 27), points)

    pygame.display.flip()
    clock.tick(FPS)

pygame.quit()
```

### Body vs Shape

- `Body`: masa, momento de inercia, posición, velocidad, ángulo.
- `Shape`: geometría de colisión, fricción, elasticidad.
- `Space`: mundo físico que integra y resuelve contactos.

---

## 4. Fuerzas gravitatorias

### Gravedad uniforme cerca de la Tierra

En la superficie terrestre se simplifica como:

\[
P = mg
\]

En Pymunk, si trabajas en píxeles, una gravedad visual típica es entre `600` y `1000 px/s²`.

```python
space = pymunk.Space()
space.gravity = (0, 900)
```

### Gravitación universal manual

Para órbitas y atracción entre cuerpos:

\[
F_G = G\frac{m_1m_2}{r^2}
\]

En videojuegos se suele usar una constante ajustada, porque las escalas reales astronómicas son enormes.

```python
import math
from pymunk import Vec2d


def gravitational_force(body, attractor_pos, strength, min_distance=10):
    direction = Vec2d(*attractor_pos) - body.position
    r2 = max(direction.length ** 2, min_distance ** 2)
    if direction.length == 0:
        return Vec2d(0, 0)
    return direction.normalized() * (strength * body.mass / r2)
```

### Substepping para fuerzas centrales

Las órbitas son sensibles al `dt`. Si el paso es grande, el objeto avanza en segmentos demasiado largos y la trayectoria se deforma. El substepping divide un frame en varios pasos físicos.

```python
dt_frame = 1.0 / 60.0
substeps = 20
dt = dt_frame / substeps

for _ in range(substeps):
    force = gravitational_force(satellite, (400, 300), strength=5_000_000)
    satellite.apply_force_at_world_point(force, satellite.position)
    space.step(dt)
```

---

## 5. Fuerza elástica

La ley de Hooke modela muelles:

\[
F_s = -kx
\]

- `k`: rigidez.
- `x`: desplazamiento respecto al equilibrio.
- Signo negativo: fuerza recuperadora.

### Muelle amortiguado con `DampedSpring`

```python
spring = pymunk.DampedSpring(
    space.static_body,
    body,
    (300, 80),   # anclaje en el techo
    (0, 0),      # anclaje en el cuerpo
    rest_length=250,
    stiffness=120,
    damping=6,
)
space.add(spring)
```

### Dibujar el muelle en zigzag

```python
import math
import pygame


def draw_zigzag_spring(screen, start, end, steps=24, width=12):
    sx, sy = start
    ex, ey = end
    dx, dy = ex - sx, ey - sy
    length = math.hypot(dx, dy)
    if length == 0:
        return

    ux, uy = dx / length, dy / length
    px, py = -uy, ux

    points = [start]
    for i in range(1, steps):
        t = i / steps
        offset = width if i % 2 else -width
        points.append((sx + dx * t + px * offset, sy + dy * t + py * offset))
    points.append(end)

    pygame.draw.lines(screen, (80, 80, 80), False, points, 2)
```

### Relación con el péndulo

Para ángulos pequeños:

\[
\sin\theta \approx \theta, \qquad F \approx -mg\frac{x}{L}
\]

Por tanto, un péndulo simple se comporta como un muelle con rigidez efectiva `k = mg/L`.

---

## 6. Rozamiento y damping

### Fricción sólida

La fricción de Coulomb depende de la normal:

\[
F_r \le \mu_e N, \qquad F_r = \mu_d N
\]

En Pymunk, la fricción de contacto se configura en las `Shape`.

```python
floor.friction = 0.8
box_shape.friction = 0.6
```

### Damping viscoso

El damping modela pérdidas proporcionales a velocidad:

\[
\vec F_v = -b\vec v
\]

Pymunk tiene un damping global:

```python
space.damping = 0.98
```

Internamente escala velocidades con algo equivalente a:

```python
v_new = v_old * damping ** dt
```

### Fricción manual de una partícula

Para aprender el concepto, puede aplicarse una fuerza opuesta al movimiento.

```python
from pymunk import Vec2d


def apply_linear_drag(body, b):
    v = Vec2d(body.velocity)
    force = -b * v
    body.apply_force_at_world_point(force, body.position)
```

### Fricción tipo Coulomb manual

```python
from pymunk import Vec2d


def apply_coulomb_friction(body, mu, g=900, dt=1/60):
    v = Vec2d(body.velocity)
    speed = v.length
    if speed < 1e-3:
        body.velocity = (0, 0)
        return

    normal = body.mass * g
    friction_mag = mu * normal
    stop_force = body.mass * speed / dt
    force_mag = min(friction_mag, stop_force)
    body.apply_force_at_world_point(-v.normalized() * force_mag, body.position)
```

---

## 7. Trabajo y energía

### Trabajo

\[
W = \vec F \cdot \vec d = Fd\cos\theta
\]

Solo realiza trabajo la componente de la fuerza en la dirección del desplazamiento.

```python
from pymunk import Vec2d


def work(force, displacement):
    return Vec2d(*force).dot(Vec2d(*displacement))
```

### Energía cinética

\[
E_c = \frac12mv^2
\]

```python

def kinetic_energy(body):
    return 0.5 * body.mass * body.velocity.length ** 2
```

### Energía potencial gravitatoria

\[
E_p = mgh
\]

En Pygame `y` crece hacia abajo, así que la altura respecto al suelo puede calcularse como `ground_y - body.position.y`.

```python

def gravitational_potential_energy(body, ground_y, g=9.81, px_m=100):
    height_m = max(0.0, (ground_y - body.position.y) / px_m)
    return body.mass * g * height_m
```

### Energía elástica

\[
E_k = \frac12k\Delta x^2
\]

```python

def spring_energy(k, current_length, rest_length):
    dx = current_length - rest_length
    return 0.5 * k * dx * dx
```

### Potencia

\[
P = \frac{W}{\Delta t} = \vec F \cdot \vec v
\]

```python

def power(force, velocity):
    return Vec2d(*force).dot(Vec2d(*velocity))
```

En gameplay, la potencia sirve para limitar motores de vehículos, propulsores o sistemas que no pueden entregar fuerza ilimitada.

---

## 8. Checklist de implementación

- Crea siempre `Body` y `Shape`, y añade ambos al `Space`.
- Configura `shape.friction` y `shape.elasticity` en los objetos que colisionan.
- Usa `space.gravity` para gravedad uniforme.
- Usa fuerzas manuales para gravedad orbital, viento, drag o efectos especiales.
- Usa substepping en órbitas, colisiones rápidas o fuerzas muy intensas.
- Si calculas energía, recuerda si tus unidades son píxeles o metros.
