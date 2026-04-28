# Documentación de `libs`

Esta carpeta contiene funciones auxiliares para cálculos de dinámica, movimiento, colisiones, rotación, fluidos y conversión de unidades para videojuegos.

Los nombres de funciones y parámetros se mantienen como están en el código para no romper imports existentes. La documentación está escrita en español y usa las convenciones del módulo `physics_utils_dinamica.py`:

- `Vector2`: tupla `(x, y)`.
- `Vector3`: tupla `(x, y, z)`.
- `Matrix3`: matriz 3x3 representada como tres `Vector3`.
- Los ángulos están en radianes salvo que se indique lo contrario.
- Unidades recomendadas: Sistema Internacional `(m, kg, s, N, J, rad)`.

## `physics_utils_dinamica.py`

### Utilidades generales

- `clamp(value, min_value, max_value)`: limita `value` al intervalo cerrado `[min_value, max_value]`. Parámetros: `value` es el número a limitar; `min_value` es el límite inferior; `max_value` es el límite superior.
- `sign(value)`: devuelve `-1`, `0` o `1` según el signo de `value`. Parámetros: `value` es el número que se quiere evaluar.
- `deg_to_rad(degrees)`: convierte grados a radianes. Parámetros: `degrees` es el ángulo expresado en grados.
- `rad_to_deg(rad)`: convierte radianes a grados. Parámetros: `rad` es el ángulo expresado en radianes.
- `almost_zero(value, eps)`: comprueba si un número es prácticamente cero. Parámetros: `value` es el número evaluado; `eps` es la tolerancia máxima para considerarlo cero.

### Vectores 2D

- `vec2_add(a, b)`: suma dos vectores 2D. Parámetros: `a` es el primer vector; `b` es el segundo vector.
- `vec2_sub(a, b)`: resta dos vectores 2D. Parámetros: `a` es el vector minuendo; `b` es el vector sustraendo.
- `vec2_scale(v, k)`: multiplica un vector 2D por un escalar. Parámetros: `v` es el vector; `k` es el factor escalar.
- `vec2_dot(a, b)`: calcula el producto escalar entre dos vectores 2D. Parámetros: `a` es el primer vector; `b` es el segundo vector.
- `vec2_cross_z(a, b)`: calcula la componente `z` del producto vectorial `a x b` en 2D. Parámetros: `a` es el primer vector; `b` es el segundo vector.
- `vec2_norm(v)`: calcula el módulo de un vector 2D. Parámetros: `v` es el vector.
- `vec2_distance(a, b)`: calcula la distancia entre dos puntos o vectores 2D. Parámetros: `a` es el primer punto; `b` es el segundo punto.
- `vec2_unit(v, eps)`: normaliza un vector 2D. Parámetros: `v` es el vector a normalizar; `eps` es la tolerancia mínima para detectar vectores casi nulos.
- `vec2_project(a, b, eps)`: proyecta el vector `a` sobre el vector `b`. Parámetros: `a` es el vector que se proyecta; `b` es el vector base de la proyección; `eps` es la tolerancia para detectar un vector base casi nulo.
- `vec2_perpendicular(v)`: devuelve el vector perpendicular girado `+90` grados. Parámetros: `v` es el vector original.
- `vec2_angle_between(a, b, eps)`: calcula el ángulo entre dos vectores 2D. Parámetros: `a` es el primer vector; `b` es el segundo vector; `eps` es la tolerancia para detectar vectores casi nulos.
- `vec2_rotate(v, angle)`: rota un vector 2D. Parámetros: `v` es el vector a rotar; `angle` es el ángulo de rotación en radianes.
- `vec2_lerp(a, b, t)`: interpola linealmente entre dos vectores 2D. Parámetros: `a` es el vector inicial; `b` es el vector final; `t` es el factor de interpolación, normalmente entre `0` y `1`.

### Vectores 3D

- `vec3_add(a, b)`: suma dos vectores 3D. Parámetros: `a` es el primer vector; `b` es el segundo vector.
- `vec3_sub(a, b)`: resta dos vectores 3D. Parámetros: `a` es el vector minuendo; `b` es el vector sustraendo.
- `vec3_scale(v, k)`: multiplica un vector 3D por un escalar. Parámetros: `v` es el vector; `k` es el factor escalar.
- `vec3_dot(a, b)`: calcula el producto escalar entre dos vectores 3D. Parámetros: `a` es el primer vector; `b` es el segundo vector.
- `vec3_cross(a, b)`: calcula el producto vectorial entre dos vectores 3D. Parámetros: `a` es el primer vector; `b` es el segundo vector.
- `vec3_norm(v)`: calcula el módulo de un vector 3D. Parámetros: `v` es el vector.
- `vec3_distance(a, b)`: calcula la distancia entre dos puntos o vectores 3D. Parámetros: `a` es el primer punto; `b` es el segundo punto.
- `vec3_unit(v, eps)`: normaliza un vector 3D. Parámetros: `v` es el vector a normalizar; `eps` es la tolerancia mínima para detectar vectores casi nulos.
- `vec3_project(a, b, eps)`: proyecta el vector `a` sobre el vector `b`. Parámetros: `a` es el vector que se proyecta; `b` es el vector base de la proyección; `eps` es la tolerancia para detectar un vector base casi nulo.
- `vec3_angle_between(a, b, eps)`: calcula el ángulo entre dos vectores 3D. Parámetros: `a` es el primer vector; `b` es el segundo vector; `eps` es la tolerancia para detectar vectores casi nulos.

### Coordenadas y Cámara

- `spherical_to_cartesian(r, theta, phi)`: convierte coordenadas esféricas a cartesianas. Parámetros: `r` es la distancia radial; `theta` es el ángulo cenital medido desde `+Z`; `phi` es el azimut en el plano `XY` medido desde `+X`.
- `cartesian_to_spherical(x, y, z, eps)`: convierte coordenadas cartesianas a esféricas. Parámetros: `x` es la coordenada horizontal; `y` es la coordenada lateral; `z` es la coordenada vertical; `eps` es la tolerancia para detectar el origen, donde los ángulos no están definidos.
- `pybullet_camera_to_cartesian(distance, yaw, pitch)`: convierte coordenadas de cámara estilo PyBullet a un vector relativo cartesiano. Parámetros: `distance` es la distancia de la cámara al objetivo; `yaw` es el giro horizontal en radianes; `pitch` es la inclinación vertical en radianes.
- `cartesian_to_pybullet_camera(x, y, z, eps)`: convierte un vector relativo cartesiano a coordenadas de cámara estilo PyBullet. Parámetros: `x` es la componente horizontal; `y` es la componente lateral; `z` es la componente vertical; `eps` es la tolerancia para detectar distancia cero.

### Matrices, Orientación y Cuaterniones

- `mat3_mul_vec3(m, v)`: multiplica una matriz 3x3 por un vector 3D. Parámetros: `m` es la matriz; `v` es el vector.
- `mat3_mul(a, b)`: multiplica dos matrices 3x3. Parámetros: `a` es la primera matriz; `b` es la segunda matriz.
- `rotation_matrix_x(angle)`: crea una matriz de rotación alrededor del eje `X`. Parámetros: `angle` es el ángulo de giro en radianes.
- `rotation_matrix_y(angle)`: crea una matriz de rotación alrededor del eje `Y`. Parámetros: `angle` es el ángulo de giro en radianes.
- `rotation_matrix_z(angle)`: crea una matriz de rotación alrededor del eje `Z`. Parámetros: `angle` es el ángulo de giro en radianes.
- `rotate_vec3_x(v, angle)`: rota un vector 3D alrededor del eje `X`. Parámetros: `v` es el vector; `angle` es el ángulo de giro en radianes.
- `rotate_vec3_y(v, angle)`: rota un vector 3D alrededor del eje `Y`. Parámetros: `v` es el vector; `angle` es el ángulo de giro en radianes.
- `rotate_vec3_z(v, angle)`: rota un vector 3D alrededor del eje `Z`. Parámetros: `v` es el vector; `angle` es el ángulo de giro en radianes.
- `euler_zxz_matrix(alpha, beta, gamma)`: crea una matriz de Euler clásica `Z-X-Z`. Parámetros: `alpha` es el primer giro sobre `Z`; `beta` es el giro sobre `X`; `gamma` es el segundo giro sobre `Z`.
- `tait_bryan_xyz_matrix(roll, pitch, yaw)`: crea una matriz tipo `roll-pitch-yaw`. Parámetros: `roll` es el giro sobre `X`; `pitch` es el giro sobre `Y`; `yaw` es el giro sobre `Z`.
- `quaternion_from_axis_angle(axis, angle)`: crea un cuaternión `[x, y, z, w]` a partir de eje y ángulo. Parámetros: `axis` es el eje de rotación; `angle` es el ángulo de giro en radianes.
- `quaternion_norm(q)`: calcula la norma de un cuaternión. Parámetros: `q` es el cuaternión en formato `[x, y, z, w]`.
- `quaternion_normalize(q, eps)`: normaliza un cuaternión. Parámetros: `q` es el cuaternión en formato `[x, y, z, w]`; `eps` es la tolerancia para detectar cuaterniones casi nulos.
- `quaternion_multiply(q1, q2)`: multiplica dos cuaterniones. Parámetros: `q1` es el primer cuaternión; `q2` es el segundo cuaternión.
- `quaternion_conjugate(q)`: calcula el conjugado de un cuaternión. Parámetros: `q` es el cuaternión en formato `[x, y, z, w]`.
- `rotate_vector_by_quaternion(v, q)`: rota un vector 3D usando un cuaternión. Parámetros: `v` es el vector a rotar; `q` es el cuaternión de rotación en formato `[x, y, z, w]`.
- `quaternion_from_euler_xyz(roll, pitch, yaw)`: crea un cuaternión a partir de ángulos de Euler `XYZ`. Parámetros: `roll` es el giro sobre `X`; `pitch` es el giro sobre `Y`; `yaw` es el giro sobre `Z`.

### Derivación, Integración y Pasos Numéricos

- `derivative_forward(f, t, dt)`: aproxima una derivada con diferencia hacia delante. Parámetros: `f` es la función escalar; `t` es el punto de evaluación; `dt` es el paso temporal.
- `derivative_backward(f, t, dt)`: aproxima una derivada con diferencia hacia atrás. Parámetros: `f` es la función escalar; `t` es el punto de evaluación; `dt` es el paso temporal.
- `derivative_central(f, t, dt)`: aproxima una derivada con diferencia central. Parámetros: `f` es la función escalar; `t` es el punto de evaluación; `dt` es el paso temporal.
- `velocity_from_positions(x_current, x_previous, dt)`: calcula velocidad a partir de posiciones consecutivas. Parámetros: `x_current` es la posición actual; `x_previous` es la posición anterior; `dt` es el tiempo transcurrido entre ambas.
- `acceleration_from_velocities(v_current, v_previous, dt)`: calcula aceleración a partir de velocidades consecutivas. Parámetros: `v_current` es la velocidad actual; `v_previous` es la velocidad anterior; `dt` es el tiempo transcurrido entre ambas.
- `riemann_left(f, a, b, n)`: aproxima una integral con suma de Riemann izquierda. Parámetros: `f` es la función a integrar; `a` es el límite inferior; `b` es el límite superior; `n` es el número de subintervalos.
- `trapezoidal_rule(f, a, b, n)`: aproxima una integral con la regla del trapecio. Parámetros: `f` es la función a integrar; `a` es el límite inferior; `b` es el límite superior; `n` es el número de subintervalos.
- `simpson_rule(f, a, b, n)`: aproxima una integral con la regla de Simpson. Parámetros: `f` es la función a integrar; `a` es el límite inferior; `b` es el límite superior; `n` es el número de subintervalos, que debe ser par.
- `euler_step(x, v, a, dt)`: realiza un paso de integración de Euler explícito. Parámetros: `x` es la posición inicial; `v` es la velocidad inicial; `a` es la aceleración; `dt` es el paso temporal.
- `semi_implicit_euler_step(x, v, a, dt)`: realiza un paso de Euler semi-implícito. Parámetros: `x` es la posición inicial; `v` es la velocidad inicial; `a` es la aceleración; `dt` es el paso temporal.
- `rk4_step(y, t, dt, dydt)`: realiza un paso Runge-Kutta de cuarto orden para `y' = f(t, y)`. Parámetros: `y` es el valor actual de la variable; `t` es el tiempo actual; `dt` es el paso temporal; `dydt` es la función derivada que recibe `(t, y)`.

### Dinámica de Partícula, Fuerzas y Energía

- `newton_acceleration(force, mass)`: aplica la segunda ley de Newton `a = F/m`. Parámetros: `force` es la fuerza aplicada; `mass` es la masa del cuerpo.
- `vector_acceleration(force, mass)`: calcula aceleración vectorial a partir de una fuerza 3D. Parámetros: `force` es el vector fuerza; `mass` es la masa del cuerpo.
- `weight_force(mass, g)`: calcula el peso `P = mg`. Parámetros: `mass` es la masa; `g` es la aceleración gravitatoria.
- `gravitational_force_magnitude(m1, m2, r, G)`: calcula el módulo de la fuerza gravitatoria entre dos masas puntuales. Parámetros: `m1` es la primera masa; `m2` es la segunda masa; `r` es la distancia entre ambas; `G` es la constante de gravitación universal.
- `gravitational_force_vector(pos1, mass1, pos2, mass2, G)`: calcula la fuerza gravitatoria sobre el cuerpo 1 debida al cuerpo 2. Parámetros: `pos1` es la posición del cuerpo 1; `mass1` es la masa del cuerpo 1; `pos2` es la posición del cuerpo 2; `mass2` es la masa del cuerpo 2; `G` es la constante de gravitación universal.
- `gravitational_acceleration_from_body(pos, source_pos, source_mass, G)`: calcula la aceleración gravitatoria producida por una masa puntual. Parámetros: `pos` es el punto donde se evalúa la aceleración; `source_pos` es la posición de la masa fuente; `source_mass` es la masa fuente; `G` es la constante de gravitación universal.
- `hooke_force(displacement, k)`: calcula la fuerza de Hooke 1D `F = -kx`. Parámetros: `displacement` es el desplazamiento respecto al equilibrio; `k` es la constante elástica.
- `hooke_force_vector(pos, anchor, rest_length, k, eps)`: calcula la fuerza de un muelle 3D entre un anclaje y una posición. Parámetros: `pos` es la posición del punto unido al muelle; `anchor` es el punto fijo; `rest_length` es la longitud natural del muelle; `k` es la constante elástica; `eps` es la tolerancia para detectar distancia casi nula.
- `viscous_damping_force(velocity, b)`: calcula rozamiento viscoso lineal 1D. Parámetros: `velocity` es la velocidad; `b` es el coeficiente de amortiguamiento viscoso.
- `viscous_damping_force_vector(velocity, b)`: calcula rozamiento viscoso lineal vectorial. Parámetros: `velocity` es el vector velocidad; `b` es el coeficiente de amortiguamiento viscoso.
- `pymunk_damping_velocity(v_old, damping, dt)`: actualiza velocidad con un modelo de amortiguamiento tipo Pymunk. Parámetros: `v_old` es la velocidad anterior; `damping` es el factor de amortiguamiento por segundo; `dt` es el paso temporal.
- `damping_from_b_over_m(b, mass)`: convierte `F = -bv` en un factor continuo equivalente por segundo. Parámetros: `b` es el coeficiente viscoso; `mass` es la masa.
- `coulomb_friction_max(mu_static, normal_force)`: calcula el máximo rozamiento estático. Parámetros: `mu_static` es el coeficiente de rozamiento estático; `normal_force` es la fuerza normal.
- `kinetic_friction(mu_dynamic, normal_force)`: calcula el rozamiento dinámico. Parámetros: `mu_dynamic` es el coeficiente de rozamiento dinámico; `normal_force` es la fuerza normal.
- `inclined_plane_normal(mass, angle, g)`: calcula la normal en un plano inclinado. Parámetros: `mass` es la masa; `angle` es el ángulo del plano en radianes; `g` es la aceleración gravitatoria.
- `inclined_plane_weight_parallel(mass, angle, g)`: calcula la componente tangencial del peso en un plano inclinado. Parámetros: `mass` es la masa; `angle` es el ángulo del plano en radianes; `g` es la aceleración gravitatoria.
- `inclined_plane_acceleration_no_friction(angle, g)`: calcula la aceleración en un plano inclinado sin rozamiento. Parámetros: `angle` es el ángulo del plano en radianes; `g` es la aceleración gravitatoria.
- `inclined_plane_acceleration_with_kinetic_friction(angle, mu_dynamic, g)`: calcula la aceleración en una rampa con rozamiento cinético. Parámetros: `angle` es el ángulo del plano en radianes; `mu_dynamic` es el coeficiente de rozamiento dinámico; `g` es la aceleración gravitatoria.
- `will_slide_on_incline(angle, mu_static)`: indica si un bloque empieza a deslizar en un plano inclinado. Parámetros: `angle` es el ángulo del plano en radianes; `mu_static` es el coeficiente de rozamiento estático.
- `work_constant_force(force, displacement)`: calcula el trabajo de una fuerza constante. Parámetros: `force` es el vector fuerza; `displacement` es el vector desplazamiento.
- `kinetic_energy(mass, speed)`: calcula energía cinética. Parámetros: `mass` es la masa; `speed` es el módulo de la velocidad.
- `gravitational_potential_energy(mass, height, g)`: calcula energía potencial gravitatoria. Parámetros: `mass` es la masa; `height` es la altura; `g` es la aceleración gravitatoria.
- `spring_potential_energy(k, displacement)`: calcula energía potencial elástica. Parámetros: `k` es la constante elástica; `displacement` es la deformación del muelle.
- `mechanical_energy(mass, speed, height, g)`: suma energía cinética y potencial gravitatoria. Parámetros: `mass` es la masa; `speed` es el módulo de la velocidad; `height` es la altura; `g` es la aceleración gravitatoria.
- `power_from_force_velocity(force, velocity)`: calcula potencia instantánea. Parámetros: `force` es el vector fuerza; `velocity` es el vector velocidad.
- `impulse_from_force(force, dt)`: calcula impulso para fuerza constante. Parámetros: `force` es el vector fuerza; `dt` es el intervalo temporal.
- `delta_v_from_impulse(impulse, mass)`: calcula cambio de velocidad a partir del impulso. Parámetros: `impulse` es el vector impulso; `mass` es la masa.

### Osciladores, Muelles y Péndulo

- `angular_frequency_spring(k, mass)`: calcula la frecuencia angular de un muelle sin amortiguar. Parámetros: `k` es la constante elástica; `mass` es la masa.
- `angular_frequency_pendulum(length, g)`: calcula la frecuencia angular de un péndulo para ángulo pequeño. Parámetros: `length` es la longitud del péndulo; `g` es la aceleración gravitatoria.
- `pendulum_period_small_angle(length, g)`: calcula el periodo de un péndulo en aproximación de ángulo pequeño. Parámetros: `length` es la longitud del péndulo; `g` es la aceleración gravitatoria.
- `pendulum_tangential_force(mass, angle, g)`: calcula la fuerza tangencial exacta del péndulo. Parámetros: `mass` es la masa; `angle` es el ángulo respecto a la vertical en radianes; `g` es la aceleración gravitatoria.
- `pendulum_small_angle_force(mass, length, arc_displacement, g)`: calcula la fuerza tangencial linealizada del péndulo. Parámetros: `mass` es la masa; `length` es la longitud del péndulo; `arc_displacement` es el desplazamiento sobre el arco; `g` es la aceleración gravitatoria.
- `critical_damping(mass, k)`: calcula el amortiguamiento crítico. Parámetros: `mass` es la masa; `k` es la constante elástica.
- `damping_ratio(mass, b, k)`: calcula el ratio de amortiguamiento. Parámetros: `mass` es la masa; `b` es el coeficiente de amortiguamiento; `k` es la constante elástica.
- `characteristic_time_damping(mass, b)`: calcula el tiempo característico de amortiguamiento. Parámetros: `mass` es la masa; `b` es el coeficiente de amortiguamiento.
- `damped_oscillator_acceleration(x, v, mass, b, k)`: calcula la aceleración de un oscilador amortiguado. Parámetros: `x` es la posición o desplazamiento; `v` es la velocidad; `mass` es la masa; `b` es el coeficiente de amortiguamiento; `k` es la constante elástica.

### Proyectiles sin Rozamiento

- `ProjectileState2D`: estado de proyectil 2D. Campos: `x` es la posición horizontal; `y` es la posición vertical; `vx` es la velocidad horizontal; `vy` es la velocidad vertical.
- `projectile_initial_velocity(v0, angle)`: calcula las componentes iniciales de velocidad. Parámetros: `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes.
- `projectile_position_no_drag(x0, y0, v0, angle, t, g)`: calcula posición de proyectil sin arrastre. Parámetros: `x0` es la posición horizontal inicial; `y0` es la posición vertical inicial; `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `t` es el tiempo; `g` es la aceleración gravitatoria.
- `projectile_velocity_no_drag(v0, angle, t, g)`: calcula velocidad de proyectil sin arrastre. Parámetros: `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `t` es el tiempo; `g` es la aceleración gravitatoria.
- `projectile_state_no_drag(x0, y0, v0, angle, t, g)`: calcula estado completo de proyectil sin arrastre. Parámetros: `x0` es la posición horizontal inicial; `y0` es la posición vertical inicial; `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `t` es el tiempo; `g` es la aceleración gravitatoria.
- `projectile_time_to_peak(v0, angle, g)`: calcula el tiempo hasta la altura máxima. Parámetros: `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `g` es la aceleración gravitatoria.
- `projectile_max_height(y0, v0, angle, g)`: calcula la altura máxima. Parámetros: `y0` es la altura inicial; `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `g` es la aceleración gravitatoria.
- `projectile_time_of_flight_from_height(y0, v0, angle, y_target, g)`: calcula el tiempo positivo hasta alcanzar una altura objetivo. Parámetros: `y0` es la altura inicial; `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `y_target` es la altura objetivo; `g` es la aceleración gravitatoria.
- `projectile_range_from_height(x0, y0, v0, angle, y_target, g)`: calcula el alcance hasta una altura objetivo. Parámetros: `x0` es la posición horizontal inicial; `y0` es la altura inicial; `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `y_target` es la altura objetivo; `g` es la aceleración gravitatoria.
- `projectile_range_level_ground(v0, angle, g)`: calcula el alcance con salida y llegada a la misma altura. Parámetros: `v0` es la rapidez inicial; `angle` es el ángulo de lanzamiento en radianes; `g` es la aceleración gravitatoria.
- `projectile_angle_for_range_level(v0, range_, g)`: calcula los dos ángulos posibles para alcanzar un alcance en terreno plano. Parámetros: `v0` es la rapidez inicial; `range_` es el alcance objetivo; `g` es la aceleración gravitatoria.

### Aire, Arrastre, Viento, Mach y Magnus

- `frontal_area_circle(radius)`: calcula el área frontal de un círculo. Parámetros: `radius` es el radio del círculo.
- `frontal_area_sphere(diameter)`: calcula el área frontal de una esfera. Parámetros: `diameter` es el diámetro de la esfera.
- `reynolds_number(rho, speed, characteristic_length, mu)`: calcula el número de Reynolds. Parámetros: `rho` es la densidad del fluido; `speed` es la rapidez relativa; `characteristic_length` es la longitud característica; `mu` es la viscosidad dinámica.
- `drag_regime_from_reynolds(Re)`: clasifica el régimen de arrastre según Reynolds. Parámetros: `Re` es el número de Reynolds.
- `stokes_drag_magnitude(mu, radius_equiv, speed, shape_factor)`: calcula el módulo del arrastre de Stokes. Parámetros: `mu` es la viscosidad dinámica; `radius_equiv` es el radio equivalente; `speed` es la rapidez relativa; `shape_factor` es el factor de corrección por forma.
- `stokes_drag_force_vector(velocity, mu, radius_equiv, shape_factor, eps)`: calcula fuerza de arrastre de Stokes vectorial. Parámetros: `velocity` es la velocidad relativa al fluido; `mu` es la viscosidad dinámica; `radius_equiv` es el radio equivalente; `shape_factor` es el factor de corrección por forma; `eps` es la tolerancia para detectar velocidad casi nula.
- `sphere_cd_intermediate(Re)`: aproxima el coeficiente de arrastre de una esfera en transición. Parámetros: `Re` es el número de Reynolds.
- `newton_drag_magnitude(rho, Cd, area, speed)`: calcula el módulo del arrastre cuadrático. Parámetros: `rho` es la densidad del fluido; `Cd` es el coeficiente de arrastre; `area` es el área frontal; `speed` es la rapidez relativa.
- `newton_drag_force_vector(velocity, rho, Cd, area, eps)`: calcula el arrastre cuadrático vectorial. Parámetros: `velocity` es la velocidad relativa al fluido; `rho` es la densidad del fluido; `Cd` es el coeficiente de arrastre; `area` es el área frontal; `eps` es la tolerancia para detectar velocidad casi nula.
- `relative_velocity(projectile_velocity, wind_velocity)`: calcula la velocidad relativa respecto al aire. Parámetros: `projectile_velocity` es la velocidad del proyectil; `wind_velocity` es la velocidad del viento.
- `drag_force_with_wind(projectile_velocity, wind_velocity, rho, Cd, area, eps)`: calcula arrastre usando viento. Parámetros: `projectile_velocity` es la velocidad del proyectil; `wind_velocity` es la velocidad del viento; `rho` es la densidad del aire; `Cd` es el coeficiente de arrastre; `area` es el área frontal; `eps` es el umbral para evitar ruido numérico.
- `air_temperature_at_altitude(altitude_m, T0, lapse_rate)`: calcula temperatura ISA aproximada en troposfera. Parámetros: `altitude_m` es la altitud en metros; `T0` es la temperatura al nivel del mar en Kelvin; `lapse_rate` es el gradiente térmico vertical.
- `air_density_at_altitude(altitude_m, rho0, T0, lapse_rate, R, g)`: calcula densidad del aire en troposfera. Parámetros: `altitude_m` es la altitud en metros; `rho0` es la densidad al nivel del mar; `T0` es la temperatura al nivel del mar; `lapse_rate` es el gradiente térmico vertical; `R` es la constante específica del gas; `g` es la aceleración gravitatoria.
- `speed_of_sound_from_temperature(temp_k, gamma, R)`: calcula velocidad del sonido desde temperatura. Parámetros: `temp_k` es la temperatura en Kelvin; `gamma` es el coeficiente adiabático; `R` es la constante específica del gas.
- `speed_of_sound_at_altitude(altitude_m, T0)`: calcula velocidad del sonido a una altitud. Parámetros: `altitude_m` es la altitud en metros; `T0` es la temperatura al nivel del mar en Kelvin.
- `mach_number(speed, speed_of_sound)`: calcula el número de Mach. Parámetros: `speed` es la rapidez del objeto; `speed_of_sound` es la velocidad local del sonido.
- `mach_correction_factor(M)`: calcula un factor de corrección por Mach. Parámetros: `M` es el número de Mach.
- `cd_with_mach_correction(Cd_reynolds, speed, speed_of_sound)`: ajusta un coeficiente de arrastre con corrección de Mach. Parámetros: `Cd_reynolds` es el coeficiente calculado por Reynolds; `speed` es la rapidez del objeto; `speed_of_sound` es la velocidad local del sonido.
- `bernoulli_pressure_total(static_pressure, rho, speed, height, g)`: calcula presión total de Bernoulli. Parámetros: `static_pressure` es la presión estática; `rho` es la densidad del fluido; `speed` es la rapidez; `height` es la altura; `g` es la aceleración gravitatoria.
- `pressure_difference_from_speeds(rho, speed_a, speed_b)`: calcula diferencia de presión por Bernoulli simplificado. Parámetros: `rho` es la densidad del fluido; `speed_a` es la rapidez en el punto A; `speed_b` es la rapidez en el punto B.
- `magnus_force_direction_2d(velocity, omega_z, eps)`: estima dirección cualitativa de Magnus en 2D. Parámetros: `velocity` es la velocidad 2D; `omega_z` es la velocidad angular alrededor de `Z`; `eps` es la tolerancia para detectar velocidad o giro casi nulos.
- `magnus_force_simplified(velocity, omega, coefficient)`: calcula fuerza de Magnus simplificada. Parámetros: `velocity` es la velocidad lineal; `omega` es la velocidad angular; `coefficient` agrupa constantes físicas y empíricas del modelo.

### Dinámica de Rotación, Centro de Masas y Rodadura

- `angular_velocity(theta_initial, theta_final, dt)`: calcula velocidad angular media. Parámetros: `theta_initial` es el ángulo inicial; `theta_final` es el ángulo final; `dt` es el intervalo temporal.
- `angular_acceleration(omega_initial, omega_final, dt)`: calcula aceleración angular media. Parámetros: `omega_initial` es la velocidad angular inicial; `omega_final` es la velocidad angular final; `dt` es el intervalo temporal.
- `centripetal_acceleration(speed, radius)`: calcula aceleración centrípeta. Parámetros: `speed` es la rapidez tangencial; `radius` es el radio de giro.
- `centripetal_force(mass, speed, radius)`: calcula fuerza centrípeta. Parámetros: `mass` es la masa; `speed` es la rapidez tangencial; `radius` es el radio de giro.
- `centrifugal_force_apparent(mass, speed, radius)`: calcula el módulo de la fuerza centrífuga aparente. Parámetros: `mass` es la masa; `speed` es la rapidez tangencial; `radius` es el radio de giro.
- `max_curve_speed_from_friction(mu, radius, g)`: calcula velocidad máxima en curva plana limitada por rozamiento. Parámetros: `mu` es el coeficiente de rozamiento; `radius` es el radio de la curva; `g` es la aceleración gravitatoria.
- `required_friction_for_curve(speed, radius, g)`: calcula el coeficiente mínimo de rozamiento para una curva plana. Parámetros: `speed` es la rapidez; `radius` es el radio de la curva; `g` es la aceleración gravitatoria.
- `motorcycle_lean_angle(speed, radius, g)`: calcula el ángulo de inclinación de una moto en curva. Parámetros: `speed` es la rapidez; `radius` es el radio de la curva; `g` es la aceleración gravitatoria.
- `banked_curve_speed_no_friction(radius, bank_angle, g)`: calcula la velocidad ideal en una curva peraltada sin rozamiento. Parámetros: `radius` es el radio de la curva; `bank_angle` es el ángulo de peralte; `g` es la aceleración gravitatoria.
- `center_of_mass_discrete(masses, positions)`: calcula el centro de masas de puntos discretos. Parámetros: `masses` es la secuencia de masas; `positions` es la secuencia de posiciones 3D asociadas.
- `triangle_centroid(p1, p2, p3)`: calcula el centroide de un triángulo. Parámetros: `p1` es el primer vértice; `p2` es el segundo vértice; `p3` es el tercer vértice.
- `polygon_signed_area(vertices)`: calcula el área firmada de un polígono. Parámetros: `vertices` es la secuencia ordenada de vértices 2D.
- `polygon_centroid(vertices, eps)`: calcula el centroide de un polígono simple. Parámetros: `vertices` es la secuencia ordenada de vértices 2D; `eps` es la tolerancia para detectar área casi nula.
- `torque_2d(r, force)`: calcula torque escalar en 2D. Parámetros: `r` es el vector desde el punto de giro hasta el punto de aplicación; `force` es la fuerza aplicada.
- `torque_magnitude(r, force, angle_between)`: calcula el módulo del torque. Parámetros: `r` es el brazo de palanca; `force` es el módulo de la fuerza; `angle_between` es el ángulo entre ambos en radianes.
- `angular_acceleration_from_torque(torque, inertia)`: calcula aceleración angular desde torque. Parámetros: `torque` es el torque aplicado; `inertia` es el momento de inercia.
- `angular_momentum(inertia, omega)`: calcula momento angular de un sólido rígido simple. Parámetros: `inertia` es el momento de inercia; `omega` es la velocidad angular.
- `moment_of_inertia_point_masses(masses, radii)`: calcula momento de inercia de masas puntuales. Parámetros: `masses` es la secuencia de masas; `radii` es la secuencia de distancias al eje.
- `inertia_thin_ring(mass, radius)`: calcula momento de inercia de un aro fino. Parámetros: `mass` es la masa; `radius` es el radio.
- `inertia_annulus(mass, inner_radius, outer_radius)`: calcula momento de inercia de un anillo plano. Parámetros: `mass` es la masa; `inner_radius` es el radio interior; `outer_radius` es el radio exterior.
- `inertia_solid_cylinder_axis(mass, radius)`: calcula momento de inercia de cilindro o disco macizo respecto a su eje central. Parámetros: `mass` es la masa; `radius` es el radio.
- `inertia_solid_sphere(mass, radius)`: calcula momento de inercia de esfera maciza. Parámetros: `mass` es la masa; `radius` es el radio.
- `inertia_hollow_sphere(mass, radius)`: calcula momento de inercia de esfera hueca delgada. Parámetros: `mass` es la masa; `radius` es el radio.
- `inertia_spherical_shell_thick(mass, inner_radius, outer_radius)`: calcula momento de inercia de corteza esférica gruesa. Parámetros: `mass` es la masa; `inner_radius` es el radio interior; `outer_radius` es el radio exterior.
- `inertia_ring_diameter_axis(mass, radius)`: calcula momento de inercia de un anillo respecto a un diámetro. Parámetros: `mass` es la masa; `radius` es el radio.
- `inertia_disk_diameter_axis(mass, radius)`: calcula momento de inercia de un disco respecto a un diámetro. Parámetros: `mass` es la masa; `radius` es el radio.
- `inertia_cylinder_central_perpendicular(mass, radius, length)`: calcula momento de inercia de un cilindro respecto a un eje central perpendicular. Parámetros: `mass` es la masa; `radius` es el radio; `length` es la longitud.
- `inertia_rod_center(mass, length)`: calcula momento de inercia de una varilla respecto a su centro. Parámetros: `mass` es la masa; `length` es la longitud.
- `inertia_rectangular_plate_center(mass, a, b)`: calcula momento de inercia de una placa rectangular respecto a su centro. Parámetros: `mass` es la masa; `a` es un lado de la placa; `b` es el otro lado de la placa.
- `parallel_axis_theorem(inertia_cm, mass, distance)`: aplica el teorema de ejes paralelos. Parámetros: `inertia_cm` es el momento de inercia en el centro de masas; `mass` es la masa; `distance` es la distancia entre ejes.
- `rolling_condition_speed(omega, radius)`: calcula velocidad lineal en rodadura pura. Parámetros: `omega` es la velocidad angular; `radius` es el radio.
- `rolling_condition_omega(v_cm, radius)`: calcula velocidad angular en rodadura pura. Parámetros: `v_cm` es la velocidad del centro de masas; `radius` es el radio.
- `rolling_contact_speed(v_cm, omega, radius)`: calcula la velocidad del punto de contacto inferior. Parámetros: `v_cm` es la velocidad del centro de masas; `omega` es la velocidad angular; `radius` es el radio.
- `rolling_state(v_cm, omega, radius, eps)`: clasifica el estado de rodadura. Parámetros: `v_cm` es la velocidad del centro de masas; `omega` es la velocidad angular; `radius` es el radio; `eps` es la tolerancia para considerar velocidades nulas o rodadura pura.
- `rolling_acceleration_incline(angle, inertia, mass, radius, g)`: calcula aceleración de rodadura pura en un plano inclinado. Parámetros: `angle` es el ángulo del plano; `inertia` es el momento de inercia; `mass` es la masa; `radius` es el radio; `g` es la aceleración gravitatoria.
- `rolling_acceleration_solid_sphere(angle, g)`: calcula aceleración de rodadura de esfera maciza. Parámetros: `angle` es el ángulo del plano; `g` es la aceleración gravitatoria.
- `rolling_acceleration_solid_cylinder(angle, g)`: calcula aceleración de rodadura de cilindro macizo. Parámetros: `angle` es el ángulo del plano; `g` es la aceleración gravitatoria.
- `rolling_acceleration_hollow_cylinder(angle, g)`: calcula aceleración de rodadura de cilindro hueco. Parámetros: `angle` es el ángulo del plano; `g` es la aceleración gravitatoria.
- `rolling_friction_torque(Crr, radius, normal_force)`: calcula torque de rozamiento por rodadura. Parámetros: `Crr` es el coeficiente de resistencia a la rodadura; `radius` es el radio; `normal_force` es la fuerza normal.
- `rolling_friction_torque_limited(Crr, radius, normal_force, inertia, omega, dt)`: calcula torque de rodadura limitado para no invertir el giro en un paso. Parámetros: `Crr` es el coeficiente de resistencia a la rodadura; `radius` es el radio; `normal_force` es la fuerza normal; `inertia` es el momento de inercia; `omega` es la velocidad angular; `dt` es el paso temporal.

### Colisiones, Momento e Impulso

- `linear_momentum(mass, velocity)`: calcula momento lineal 1D. Parámetros: `mass` es la masa; `velocity` es la velocidad.
- `linear_momentum_vector(mass, velocity)`: calcula momento lineal vectorial. Parámetros: `mass` es la masa; `velocity` es el vector velocidad.
- `total_momentum(masses, velocities)`: suma el momento lineal total. Parámetros: `masses` es la secuencia de masas; `velocities` es la secuencia de velocidades 3D.
- `center_of_mass_velocity(masses, velocities)`: calcula velocidad del centro de masas. Parámetros: `masses` es la secuencia de masas; `velocities` es la secuencia de velocidades 3D.
- `coefficient_of_restitution(v1_i, v2_i, v1_f, v2_f)`: calcula coeficiente de restitución. Parámetros: `v1_i` es la velocidad inicial del cuerpo 1; `v2_i` es la velocidad inicial del cuerpo 2; `v1_f` es la velocidad final del cuerpo 1; `v2_f` es la velocidad final del cuerpo 2.
- `combined_elasticity(e1, e2)`: combina elasticidades estilo Pymunk. Parámetros: `e1` es la elasticidad del primer cuerpo; `e2` es la elasticidad del segundo cuerpo.
- `collision_1d_final_velocities(m1, v1, m2, v2, e)`: calcula velocidades finales de una colisión 1D. Parámetros: `m1` es la masa del cuerpo 1; `v1` es la velocidad inicial del cuerpo 1; `m2` es la masa del cuerpo 2; `v2` es la velocidad inicial del cuerpo 2; `e` es el coeficiente de restitución.
- `perfectly_inelastic_velocity(m1, v1, m2, v2)`: calcula velocidad común tras una colisión perfectamente inelástica. Parámetros: `m1` es la masa del cuerpo 1; `v1` es la velocidad inicial del cuerpo 1; `m2` es la masa del cuerpo 2; `v2` es la velocidad inicial del cuerpo 2.
- `impulse_1d_for_collision(m1, v1, m2, v2, e)`: calcula el impulso normal 1D aplicado al cuerpo 1. Parámetros: `m1` es la masa del cuerpo 1; `v1` es la velocidad del cuerpo 1; `m2` es la masa del cuerpo 2; `v2` es la velocidad del cuerpo 2; `e` es el coeficiente de restitución.
- `reflect_velocity_against_wall(velocity, normal, restitution)`: calcula rebote contra una pared. Parámetros: `velocity` es la velocidad entrante; `normal` es la normal de la pared; `restitution` es el coeficiente de restitución.
- `decompose_velocity_normal_tangent(velocity, normal)`: separa velocidad en componente normal y tangencial. Parámetros: `velocity` es la velocidad a descomponer; `normal` es la normal del contacto.
- `oblique_collision_no_friction_2d(m1, v1, m2, v2, normal, e)`: resuelve colisión oblicua 2D sin fricción. Parámetros: `m1` es la masa del cuerpo 1; `v1` es la velocidad inicial del cuerpo 1; `m2` es la masa del cuerpo 2; `v2` es la velocidad inicial del cuerpo 2; `normal` es la normal de contacto; `e` es el coeficiente de restitución.
- `angular_momentum_particles(positions, masses, velocities)`: calcula momento angular total de partículas. Parámetros: `positions` son las posiciones 3D; `masses` son las masas; `velocities` son las velocidades 3D.
- `orbital_speed_from_angular_momentum(radius_initial, speed_initial, radius_final)`: usa conservación de momento angular para calcular velocidad orbital final. Parámetros: `radius_initial` es el radio inicial; `speed_initial` es la rapidez inicial; `radius_final` es el radio final.
- `point_velocity_from_rotation(v_cm, omega, r_cm_to_point)`: calcula velocidad de un punto de un sólido rígido. Parámetros: `v_cm` es la velocidad del centro de masas; `omega` es la velocidad angular; `r_cm_to_point` es el vector desde el centro de masas al punto.
- `cm_velocity_for_desired_point_velocity(v_point, omega, r_cm_to_point)`: calcula velocidad del centro de masas necesaria para una velocidad de punto deseada. Parámetros: `v_point` es la velocidad deseada del punto; `omega` es la velocidad angular; `r_cm_to_point` es el vector desde el centro de masas al punto.

### Tablas de Coeficientes

- `drag_coefficient_2d(shape)`: consulta un coeficiente de arrastre 2D. Parámetros: `shape` es la clave de forma en `DRAG_COEFFICIENTS_2D`.
- `drag_coefficient_3d(shape)`: consulta un coeficiente de arrastre 3D. Parámetros: `shape` es la clave de forma en `DRAG_COEFFICIENTS_3D`.
- `laminar_shape_factor(shape)`: consulta un factor de forma para arrastre laminar. Parámetros: `shape` es la clave de forma en `LAMINAR_SHAPE_FACTORS`.
- `viscosity_20c(fluid)`: consulta viscosidad dinámica a `20 °C`. Parámetros: `fluid` es la clave del fluido en `VISCOSITIES_PA_S_20C`.

### Conversión de Escala para Videojuegos

- `meters_to_pixels(meters, pixels_per_meter)`: convierte metros a píxeles. Parámetros: `meters` es la distancia en metros; `pixels_per_meter` es la escala de píxeles por metro.
- `pixels_to_meters(pixels, pixels_per_meter)`: convierte píxeles a metros. Parámetros: `pixels` es la distancia en píxeles; `pixels_per_meter` es la escala de píxeles por metro.
- `acceleration_mps2_to_pxps2(accel, pixels_per_meter)`: convierte aceleración de `m/s^2` a `px/s^2`. Parámetros: `accel` es la aceleración en metros por segundo cuadrado; `pixels_per_meter` es la escala de píxeles por metro.
- `acceleration_pxps2_to_mps2(accel_px, pixels_per_meter)`: convierte aceleración de `px/s^2` a `m/s^2`. Parámetros: `accel_px` es la aceleración en píxeles por segundo cuadrado; `pixels_per_meter` es la escala de píxeles por metro.

## `pymunk.py`

- `hola()`: imprime un saludo de prueba. Parámetros: no recibe parámetros.
