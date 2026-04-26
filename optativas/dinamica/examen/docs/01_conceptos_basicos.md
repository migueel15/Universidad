# Tema 1 — Conceptos básicos

> **Restricción aplicada:** todos los ejemplos están escritos solo con `pymunk` como motor físico, `pygame` como visor y módulos estándar de Python como `math`. Cuando las diapositivas o los códigos originales usan PyBullet, NumPy, SciPy o Tkinter, aquí se mantiene el concepto físico pero se adapta a Pymunk/Pygame.


## Índice

1. Física en videojuegos y papel del motor físico
2. Unidades de medida y escalado
3. Sistemas de coordenadas y conversión pantalla-mundo
4. Vectores con `pymunk.Vec2d`
5. Rotaciones y matrices en 2D
6. Derivación numérica
7. Integración numérica
8. Ecuaciones diferenciales ordinarias
9. Checklist de implementación

---

## 1. Física en videojuegos y papel del motor físico

La física en videojuegos no consiste en reproducir la realidad al 100%, sino en construir comportamientos coherentes. Un motor físico resuelve muchos detalles: cuerpos rígidos, fuerzas, colisiones, contactos, fricción e integración temporal. Aun así, el motor no decide el diseño físico por ti. Hay que configurar masas, fricción, elasticidad, amortiguamiento, escala, pasos de tiempo y fuerzas externas.

En este curso se trabaja principalmente con la idea de:

- **Pymunk** como motor físico 2D.
- **Pygame** como visor/renderizador.
- Un bucle de simulación donde en cada frame se leen eventos, se aplican fuerzas, se avanza `space.step(dt)` y se dibuja.

### Código base: ventana + espacio físico

```python
import pygame
import pymunk

WIDTH, HEIGHT = 800, 600
FPS = 60

pygame.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 900)  # En Pymunk normalmente trabajamos en px/s^2

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    dt = 1.0 / FPS
    space.step(dt)

    screen.fill((255, 255, 255))
    pygame.display.flip()
    clock.tick(FPS)

pygame.quit()
```

Este esquema es la base de casi todos los ejemplos de los temas siguientes.

---

## 2. Unidades de medida y escalado

Las magnitudes fundamentales usadas en física son longitud `[L]`, masa `[M]` y tiempo `[T]`. En el Sistema Internacional se usan metros, kilogramos y segundos. En Pymunk, en cambio, no hay unidades físicas obligatorias: el motor solo ve números. Por eso hay que decidir una escala.

Ejemplo típico:

- El mundo físico se calcula en metros.
- La pantalla se dibuja en píxeles.
- Se define un factor `PX_M`: píxeles por metro.

Esto aparece claramente en el código de billar proporcionado, donde la mesa se define en metros y se convierte a pantalla usando una constante de escala.

### Código: conversión metros ↔ píxeles

```python
PX_M = 650.0          # 650 píxeles por metro
M_PX = 1.0 / PX_M     # metros por píxel
OFFSET_Y = 60         # margen superior de interfaz


def to_pygame(point_m):
    """Convierte una posición en metros a coordenadas de pantalla."""
    x_m, y_m = point_m
    return int(x_m * PX_M), int(y_m * PX_M) + OFFSET_Y


def to_world(point_px):
    """Convierte una posición de pantalla a coordenadas físicas en metros."""
    x_px, y_px = point_px
    return x_px * M_PX, (y_px - OFFSET_Y) * M_PX
```

### Unidades importantes

| Magnitud | Dimensión | SI | En Pymunk/Pygame si usas píxeles |
|---|---:|---:|---:|
| Longitud | `[L]` | m | px o m escalados |
| Tiempo | `[T]` | s | s |
| Masa | `[M]` | kg | unidad de masa elegida |
| Velocidad | `[L][T]^-1` | m/s | px/s o m/s escalados |
| Aceleración | `[L][T]^-2` | m/s² | px/s² o m/s² escalados |
| Fuerza | `[M][L][T]^-2` | N | unidad de masa · unidad de longitud / s² |

La regla práctica es: **elige una escala y sé consistente**.

---

## 3. Sistemas de coordenadas y conversión pantalla-mundo

En simulación 2D con Pymunk/Pygame se usa normalmente un plano `(x, y)`. Pygame coloca el origen `(0, 0)` en la esquina superior izquierda, con el eje `y` creciendo hacia abajo. Esto afecta a gravedad, ángulos y dibujos.

En las diapositivas también se explican coordenadas 3D y esféricas para cámaras orbitales. Como aquí la restricción es Pymunk/Pygame, la idea útil se adapta a 2D: para mover una cámara, un proyectil o una guía de tiro, basta con expresar una dirección por radio y ángulo.

### Código: dirección a partir de un ángulo

```python
import math
from pymunk import Vec2d


def direction_from_angle(angle_rad):
    """Vector unitario en la dirección angle_rad."""
    return Vec2d(math.cos(angle_rad), math.sin(angle_rad))


def polar_to_cartesian(radius, angle_rad):
    """Coordenadas polares 2D a cartesianas."""
    d = direction_from_angle(angle_rad)
    return radius * d.x, radius * d.y
```

Esto es lo mismo que se usa en el billar para transformar `cue_angle` y `cue_power` en impulso:

```python
impulse_x = math.cos(cue_angle) * cue_power
impulse_y = math.sin(cue_angle) * cue_power
```

---

## 4. Vectores con `pymunk.Vec2d`

Un vector tiene módulo, dirección y sentido. En simulaciones aparecen constantemente:

- Posición: `body.position`
- Velocidad: `body.velocity`
- Fuerza: argumento de `apply_force_at_world_point`
- Impulso: argumento de `apply_impulse_at_world_point`

Pymunk ofrece `Vec2d`, que simplifica operaciones como suma, resta, normalización, distancia y rotación.

### Operaciones básicas

```python
from pymunk import Vec2d

v = Vec2d(3, 4)
print(v.length)       # 5.0
print(v.normalized()) # Vec2d(0.6, 0.8)

p = Vec2d(100, 50)
q = Vec2d(130, 90)
print(p.get_distance(q))
```

### Producto escalar y proyección

El producto escalar mide cuánto apunta un vector en la dirección de otro:

\[
\vec a \cdot \vec b = |a||b|\cos(\theta)
\]

En juegos se usa para comprobar campo de visión, velocidad en una dirección, fricción tangencial o si un objeto se mueve a favor/en contra de una superficie.

```python
from pymunk import Vec2d


def project(a, b):
    """Proyección de a sobre b."""
    b = Vec2d(*b)
    a = Vec2d(*a)
    if b.length == 0:
        return Vec2d(0, 0)
    return (a.dot(b) / b.dot(b)) * b

velocity = Vec2d(120, 40)
forward = Vec2d(1, 0)
parallel = project(velocity, forward)
perpendicular = velocity - parallel
```

### Producto vectorial en 2D

En 2D no obtenemos un vector completo, sino la componente `z` del producto vectorial:

\[
\tau_z = r_xF_y - r_yF_x
\]

Esa componente es precisamente el torque escalar que hace girar un cuerpo en Pymunk.

```python
from pymunk import Vec2d


def cross_z(a, b):
    a = Vec2d(*a)
    b = Vec2d(*b)
    return a.x * b.y - a.y * b.x

r = Vec2d(20, 0)      # punto de aplicación respecto al CM
F = Vec2d(0, 100)     # fuerza vertical
print(cross_z(r, F))  # torque positivo
```

---

## 5. Rotaciones y matrices en 2D

Las diapositivas presentan matrices de rotación y ángulos de Euler. En 2D solo necesitamos la rotación alrededor del eje perpendicular a la pantalla. La matriz equivalente es:

\[
R(\theta)=
\begin{pmatrix}
\cos\theta & -\sin\theta\\
\sin\theta & \cos\theta
\end{pmatrix}
\]

Con `Vec2d.rotated(angle)` no hace falta escribir la matriz a mano.

### Código: rotar un vector

```python
import math
from pymunk import Vec2d

v = Vec2d(1, 0)
rotado_90 = v.rotated(math.radians(90))
print(rotado_90)  # aproximadamente Vec2d(0, 1)
```

### Código: dibujar un cuerpo rectangular rotado

```python
import pygame
import pymunk


def draw_poly(screen, shape, color=(220, 120, 40)):
    body = shape.body
    points = []
    for vertex in shape.get_vertices():
        world = body.local_to_world(vertex)
        points.append((int(world.x), int(world.y)))
    pygame.draw.polygon(screen, color, points)
```

Esta técnica es importante porque un `pygame.draw.rect` no rota el rectángulo. Para dibujar correctamente una caja física, conviene transformar sus vértices locales al mundo.

---

## 6. Derivación numérica

La derivada mide una tasa de cambio. En simulación, el tiempo no es continuo, avanza en saltos `dt`. Por eso se usan diferencias finitas.

### Diferencia hacia atrás

\[
v_i \approx \frac{x_i - x_{i-1}}{\Delta t}
\]

Es la más usada en tiempo real porque tenemos la posición actual y la anterior.

### Código: estimar velocidad y aceleración

```python
from pymunk import Vec2d

class NumericDerivative:
    def __init__(self, initial_position):
        self.prev_pos = Vec2d(*initial_position)
        self.prev_vel = Vec2d(0, 0)

    def update(self, current_position, dt):
        current_pos = Vec2d(*current_position)
        velocity = (current_pos - self.prev_pos) / dt
        acceleration = (velocity - self.prev_vel) / dt

        self.prev_pos = current_pos
        self.prev_vel = velocity
        return velocity, acceleration
```

Aunque Pymunk ya proporciona `body.velocity`, esta técnica es útil para calcular aceleración o para depurar lo que está haciendo el motor.

---

## 7. Integración numérica

Integrar consiste en acumular cambios. En física de videojuegos se usa para avanzar de aceleración a velocidad y de velocidad a posición.

### Euler explícito

```python
x = x + v * dt
v = v + a * dt
```

Es simple, pero puede ganar o perder energía artificialmente.

### Euler semimplícito

```python
v = v + a * dt
x = x + v * dt
```

Suele ser más estable para juegos. Muchos motores físicos usan variantes de este enfoque.

### Código: integración manual de una partícula

```python
from pymunk import Vec2d

class Particle:
    def __init__(self, pos, vel, mass):
        self.pos = Vec2d(*pos)
        self.vel = Vec2d(*vel)
        self.mass = mass

    def step(self, force, dt):
        a = Vec2d(*force) / self.mass
        self.vel += a * dt
        self.pos += self.vel * dt
```

En Pymunk, normalmente no integras tú manualmente: aplicas fuerzas y llamas a `space.step(dt)`. Pero entender esto ayuda a depurar errores de energía, jitter o explosiones numéricas.

---

## 8. Ecuaciones diferenciales ordinarias

Una EDO describe cómo cambia una magnitud en el tiempo. En dinámica aparecen porque:

\[
v = \frac{dx}{dt}, \qquad a = \frac{dv}{dt} = \frac{d^2x}{dt^2}
\]

Un ejemplo clásico es el oscilador amortiguado:

\[
ma = -kx - bv
\]

- `-kx`: fuerza elástica recuperadora.
- `-bv`: fuerza viscosa disipativa.
- Si `b² < 4mk`, el sistema oscila.
- Si `b² = 4mk`, hay amortiguamiento crítico.
- Si `b² > 4mk`, vuelve lentamente sin oscilar.

### Código: oscilador amortiguado con Pymunk

```python
import pygame
import pymunk

WIDTH, HEIGHT = 600, 700
FPS = 60

pygame.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 900)

anchor = (300, 80)
body = pymunk.Body(1.0, pymunk.moment_for_circle(1.0, 0, 30))
body.position = (300, 500)
shape = pymunk.Circle(body, 30)
space.add(body, shape)

spring = pymunk.DampedSpring(
    space.static_body,
    body,
    anchor,
    (0, 0),
    rest_length=250,
    stiffness=120,
    damping=4,
)
space.add(spring)

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    space.step(1.0 / FPS)

    screen.fill((255, 255, 255))
    pygame.draw.line(screen, (0, 0, 0), anchor, body.position, 2)
    pygame.draw.circle(screen, (70, 130, 230), body.position, 30)
    pygame.display.flip()
    clock.tick(FPS)

pygame.quit()
```

---

## 9. Checklist de implementación

- Usa un `dt` fijo para la física, normalmente `1/60`.
- Convierte entre metros y píxeles si trabajas con dimensiones reales.
- Recuerda que en Pygame el eje `y` crece hacia abajo.
- Usa `Vec2d` para evitar errores de vectores.
- Aplica fuerzas antes de `space.step(dt)`.
- Dibuja después del paso físico.
- Para sistemas sensibles, usa substepping: varios `space.step(dt / n)` por frame.
