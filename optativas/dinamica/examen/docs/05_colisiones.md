# Tema 5 — Colisiones

> **Restricción aplicada:** todos los ejemplos están escritos solo con `pymunk` como motor físico, `pygame` como visor y módulos estándar de Python como `math`. Cuando las diapositivas o los códigos originales usan PyBullet, NumPy, SciPy o Tkinter, aquí se mantiene el concepto físico pero se adapta a Pymunk/Pygame.

## Índice

1. Introducción
2. Conservación de energía y restitución
3. Solapamiento, slop y bias
4. Conservación del momento lineal y angular
5. Colisiones oblicuas
6. Impulso
7. Callbacks de colisión en Pymunk
8. Caso integrador: billar con taco
9. Checklist de implementación

---

## 1. Introducción

Una colisión real implica detección de contacto, intercambio de momento lineal y angular, pérdida o conservación de energía, fricción y deformación. Implementar todo desde cero es complejo; por eso usamos motores como Pymunk.

Magnitudes principales:

\[
\vec p = m\vec v
\]

\[
L = I\omega
\]

El momento lineal y angular se conservan si no hay fuerzas o torques externos netos. La energía cinética no siempre se conserva.

---

## 2. Conservación de energía y restitución

El coeficiente de restitución mide cuánto rebota una colisión:

\[
e = -\frac{v*{2f}-v*{1f}}{v*{2i}-v*{1i}}
\]

- `e = 1`: colisión perfectamente elástica.
- `e = 0`: colisión perfectamente inelástica.
- `0 < e < 1`: colisión real.

En Pymunk se controla con `shape.elasticity`.

```python
ball_shape.elasticity = 0.9
wall_shape.elasticity = 0.8
```

El rebote efectivo depende de las dos formas que chocan.

### Código: dos bolas elásticas

```python
import pygame
import pymunk

pygame.init()
screen = pygame.display.set_mode((800, 400))
clock = pygame.time.Clock()

space = pymunk.Space()
space.gravity = (0, 0)

bodies = []
for x, vx, color in [(250, 250, (60, 130, 230)), (550, -100, (230, 90, 70))]:
    body = pymunk.Body(1, pymunk.moment_for_circle(1, 0, 30))
    body.position = (x, 200)
    body.velocity = (vx, 0)
    shape = pymunk.Circle(body, 30)
    shape.elasticity = 1.0
    shape.friction = 0.0
    shape.color = color
    space.add(body, shape)
    bodies.append((body, shape, color))

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    space.step(1 / 60)
    screen.fill((255, 255, 255))
    for body, shape, color in bodies:
        pygame.draw.circle(screen, color, body.position, 30)
    pygame.display.flip()
    clock.tick(60)

pygame.quit()
```

---

## 3. Solapamiento, slop y bias

Como el tiempo es discreto, un cuerpo puede penetrar ligeramente en otro antes de que el motor corrija el contacto.

En Pymunk:

- `space.collision_slop`: tolerancia de penetración antes de corregir.
- `space.collision_bias`: velocidad/porcentaje de corrección del error.

```python
space = pymunk.Space()
space.collision_slop = 0.1
space.collision_bias = 0.8
```

Si el bias es demasiado agresivo, los objetos pueden salir disparados. Si el slop es demasiado pequeño, puede aparecer vibración/jitter.

---

## 4. Conservación del momento lineal y angular

Si no hay fuerza externa neta:

\[
\frac{d\vec p*{tot}}{dt}=0 \Rightarrow \vec p*{tot}=\text{cte}
\]

Esto vale para colisiones elásticas e inelásticas. En explosiones internas, la suma de momentos de los fragmentos debe coincidir con el momento inicial.

### Código: explosión conservando momento

```python
import math
from pymunk import Vec2d


def explode(space, center, n=8, speed=300, mass=1):
    bodies = []
    for i in range(n):
        angle = 2 * math.pi * i / n
        body = pymunk.Body(mass, pymunk.moment_for_circle(mass, 0, 10))
        body.position = center
        body.velocity = speed * Vec2d(math.cos(angle), math.sin(angle))
        shape = pymunk.Circle(body, 10)
        space.add(body, shape)
        bodies.append(body)
    return bodies
```

La suma de velocidades queda compensada si todas las masas son iguales y las direcciones están repartidas uniformemente.

### Momento angular

\[
L = \sum_i \vec r_i \times m_i\vec v_i
\]

Para un sólido rígido:

\[
L = I\omega
\]

```python

def angular_momentum(body):
    return body.moment * body.angular_velocity
```

---

## 5. Colisiones oblicuas

En una colisión oblicua 2D:

1. Se identifica la línea de acción: normal al contacto.
2. Se descompone la velocidad en normal y tangencial.
3. Sin fricción, la colisión afecta a la componente normal.
4. Con fricción, también cambia la componente tangencial y puede aparecer rotación.

### Código: descomponer velocidad

```python
from pymunk import Vec2d


def split_velocity(velocity, normal):
    v = Vec2d(velocity)
    n = Vec2d(normal).normalized()
    v_normal = v.dot(n) * n
    v_tangent = v - v_normal
    return v_normal, v_tangent
```

### Línea de acción entre dos bolas

```python

def contact_normal_between_circles(body_a, body_b):
    delta = body_b.position - body_a.position
    if delta.length == 0:
        return Vec2d(1, 0)
    return delta.normalized()
```

### Visualización de normal y tangente

```python
import pygame
from pymunk import Vec2d


def draw_vector(screen, origin, vector, color, scale=1.0):
    o = Vec2d(origin)
    v = Vec2d(vector) * scale
    pygame.draw.line(screen, color, o, o + v, 3)
```

---

## 6. Impulso

El impulso es el efecto acumulado de una fuerza durante un tiempo corto:

\[
\vec J = \int Fdt = \Delta \vec p
\]

Pymunk permite aplicar directamente impulsos:

```python
body.apply_impulse_at_world_point((jx, jy), body.position)
```

### Impulso para fijar una velocidad deseada

```python
from pymunk import Vec2d


def impulse_for_velocity_change(body, delta_v):
    return body.mass * Vec2d(delta_v)


def add_velocity(body, delta_v):
    impulse = impulse_for_velocity_change(body, delta_v)
    body.apply_impulse_at_world_point(impulse, body.position)
```

### Impulso excéntrico: traslación + rotación

```python

def hit_off_center(body, impulse, local_point):
    world_point = body.local_to_world(local_point)
    body.apply_impulse_at_world_point(impulse, world_point)
```

Esto es clave para simular golpes de taco, impactos laterales o choques que hacen girar.

---

## 7. Callbacks de colisión en Pymunk

Pymunk permite enganchar código a eventos de colisión:

- `begin`: se llama al empezar el contacto.
- `pre_solve`: antes de resolver cada step de contacto.
- `post_solve`: después de resolver, útil para leer impulso aplicado.
- `separate`: cuando dejan de tocarse.

En versiones recientes de Pymunk puede usarse `space.on_collision`.

### Código: detectar una colisión

```python

def on_begin(arbiter, space, data):
    print("Colisión detectada")
    return True  # True permite que Pymunk resuelva la colisión

shape_a.collision_type = 1
shape_b.collision_type = 2
space.on_collision(1, 2, begin=on_begin)
```

### Puentear el comportamiento por defecto

Si devuelves `False`, el solver ignora esa colisión. Esto permite aplicar un impulso manual.

```python
from pymunk import Vec2d


def manual_hit(arbiter, space, data):
    shape_ball, shape_cue = arbiter.shapes
    ball = shape_ball.body
    cue = shape_cue.body

    direction = Vec2d(ball.position - cue.position)
    if direction.length == 0:
        return False
    direction = direction.normalized()

    impulse = direction * 2.0
    ball.apply_impulse_at_world_point(impulse, ball.position)

    return False  # Evita que Pymunk aplique además su impulso normal
```

---

## 8. Caso integrador: billar con taco

El billar proporcionado usa:

- Bolas como círculos con masa real aproximada.
- Taco como polígono/trapecio.
- `collision_type` para distinguir bola y taco.
- Impulsos para transferir momento a la bola.
- Elasticidad y fricción para bolas y bandas.

### Bola con masa, radio y colisión

```python
import pygame
import pymunk
from pymunk import Vec2d

PX_M = 1000

class Ball:
    def __init__(self, space, pos_m, radius_m=0.0285, mass=0.170):
        moment = pymunk.moment_for_circle(mass, 0, radius_m)
        self.body = pymunk.Body(mass, moment)
        self.body.position = pos_m
        self.radius_m = radius_m

        self.shape = pymunk.Circle(self.body, radius_m)
        self.shape.friction = 0.1
        self.shape.elasticity = 0.8
        self.shape.collision_type = 1
        self.shape.parent = self
        space.add(self.body, self.shape)

    def draw(self, screen, color=(255, 255, 255)):
        pos = (int(self.body.position.x * PX_M), int(self.body.position.y * PX_M))
        r = int(self.radius_m * PX_M)
        pygame.draw.circle(screen, color, pos, r)
        pygame.draw.circle(screen, (0, 0, 0), pos, r, 2)

        mark = self.body.position + Vec2d(self.radius_m, 0).rotated(self.body.angle)
        pygame.draw.line(screen, (0, 0, 0), pos, (int(mark.x * PX_M), int(mark.y * PX_M)), 2)
```

### Taco como polígono

```python
class Cue:
    def __init__(self, space, pos_m, angle_rad=0.0):
        length = 1.45
        base_w = 0.030
        tip_w = 0.012
        mass = 0.54

        vertices = [
            (-length, -base_w / 2),
            (-length, base_w / 2),
            (0, tip_w / 2),
            (0, -tip_w / 2),
        ]

        moment = pymunk.moment_for_poly(mass, vertices)
        self.body = pymunk.Body(mass, moment)
        self.body.position = pos_m
        self.body.angle = angle_rad

        self.shape = pymunk.Poly(self.body, vertices)
        self.shape.friction = 0.7
        self.shape.elasticity = 0.4
        self.shape.collision_type = 2
        self.shape.parent = self
        space.add(self.body, self.shape)

    def draw(self, screen):
        points = []
        for v in self.shape.get_vertices():
            p = self.body.local_to_world(v)
            points.append((int(p.x * PX_M), int(p.y * PX_M)))
        pygame.draw.polygon(screen, (100, 50, 20), points)
```

### Handler bola-taco con impulso manual

```python
from pymunk import Vec2d


def cue_ball_begin(arbiter, space, data):
    ball_shape, cue_shape = arbiter.shapes
    ball = ball_shape.body
    cue = cue_shape.body

    relative_v = Vec2d(cue.velocity - ball.velocity)
    if relative_v.length == 0:
        return False

    normal = (ball.position - cue.position)
    if normal.length == 0:
        normal = relative_v
    normal = normal.normalized()

    # Solo transferimos la parte del movimiento del taco dirigida hacia la bola.
    closing_speed = max(0.0, relative_v.dot(normal))
    impulse_mag = cue.mass * closing_speed * 0.8
    impulse = normal * impulse_mag

    # Si el punto de impacto no coincide con el centro, generará rotación.
    contact_point = arbiter.contact_point_set.points[0].point_a
    ball.apply_impulse_at_world_point(impulse, contact_point)

    # Se ignora el solver normal para no duplicar el golpe.
    return False
```

---

## 9. Checklist de implementación

- Usa `shape.elasticity` para el rebote.
- Usa `shape.friction` para transferencia tangencial y rotación.
- Ajusta `collision_slop` y `collision_bias` si hay jitter o penetraciones.
- Usa `collision_type` para distinguir objetos.
- Devuelve `True` en callbacks si quieres que Pymunk resuelva la colisión.
- Devuelve `False` si quieres sustituir la física por un impulso manual.
- En choques rápidos, usa substepping.
- Para análisis, calcula momento lineal, energía cinética y momento angular antes/después.
