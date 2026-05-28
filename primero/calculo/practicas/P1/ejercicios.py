import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import sympy as sp

# ejemplo malla
x = np.linspace(-5, 5, 50)
y = np.linspace(-5, 5, 50)
X, Y = np.meshgrid(x, y)

print("Forma de X e Y:", X.shape)

# ejemplo superficie 3d
# Definir campo escalar
Z = X**3 + 2 * Y**2

# Crear gráfico 3D
fig = plt.figure(figsize=(10, 7))
ax = fig.add_subplot(projection="3d")
ax.plot_surface(X, Y, Z, cmap="viridis")
ax.set_xlabel("X")
ax.set_ylabel("Y")
ax.set_zlabel("$z = x^2 + 2y^2$")
plt.title("Superficie cuadrática")
plt.show()

# ejemplo curvas de nivel
x = np.linspace(-5, 5, 100)
y = np.linspace(-5, 5, 100)
X, Y = np.meshgrid(x, y)
Z = X**2 + Y**2

fig, ax = plt.subplots(figsize=(8, 8))
ax.contour(X, Y, Z, levels=[9, 16], colors="black")
ax.set_aspect("equal")
ax.set_title("$x^2 + y^2 = c$")
ax.grid(True)
plt.show()

# ejemplo curvas parametricas
fig, ax = plt.subplots(figsize=(8, 8))

t = np.linspace(0, 2 * np.pi, 100)
x = 3 * np.cos(t)
y = 3 * np.sin(t)

ax.plot(x, y, "b-", linewidth=2)
ax.set_aspect("equal")
ax.grid(True)
ax.set_title(r"Círculo paramétrico: $x=3\cos(t), y=3\sin(t)$")
plt.show()

# ejemplo calculo derivadas
# Definir variable simbólica
x = sp.symbols("x")

# Definir función
f = sp.exp(x / 2) * sp.sin(x / 3) ** 2

# Calcular derivada
df = f.diff(x)
print("f' =")
sp.pprint(df)

# ejemplo derivada parcial
x, y = sp.symbols("x y")
f = x**2 + y**2

# Derivadas parciales de primer orden
df_dx = f.diff(x)  # 2*x
df_dy = f.diff(y)  # 2*y

# Derivadas parciales de segundo orden
d2f_dx2 = f.diff(x, x)  # 2
d2f_dxdy = f.diff(x, y)  # 0

print("∂f/∂x =", df_dx)
print("∂f/∂y =", df_dy)
print("∂²f/∂x² =", d2f_dx2)
print("∂²f/∂x∂y =", d2f_dxdy)

# ejemplo encontrar puntos críticos
x, y = sp.symbols("x y")
f = y * x**2 * sp.exp(x * y)

# Calcular derivadas parciales
df_dx = f.diff(x)
df_dy = f.diff(y)

# Definir ecuaciones
eq1 = sp.Eq(df_dx, 0)
eq2 = sp.Eq(df_dy, 0)

# Resolver sistema
puntos_criticos = sp.solve([eq1, eq2], [x, y])
print("Puntos críticos:", puntos_criticos)
