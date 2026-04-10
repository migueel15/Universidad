import numpy as np
import matplotlib.pyplot as plt
import sympy as sp

# 1.1
puntos = np.linspace(0, 3 * np.pi, 8)
print("Array de puntos:", puntos)
print("Número de elementos:", len(puntos))

# 1.2
A = np.array([[3,1,2],[1,4,-1],[2,-3,5]])
b = np.array([11,4,16])
x = np.linalg.solve(A, b)
print("Solución del sistema de ecuaciones:", x)

resultado = np.dot(A, x)
print("Resultado de A · x:", resultado, "es igual a b:", b)

# 1.3
t = puntos
sin_t = np.sin(t)
cos_t = np.cos(t)
print("sin(t):", sin_t)
print("cos(t):", cos_t)

plt.plot(t, sin_t, label="sin(t)", color="blue")
plt.plot(t, cos_t, label="cos(t)", color="red")
plt.legend()
plt.xlabel("t")
plt.ylabel("Valor")
plt.title("Funciones seno y coseno")
plt.grid()
plt.show()

# 2

# 2.1
x = np.linspace(-np.pi, np.pi, 80)
y = np.linspace(-np.pi, np.pi, 80)
X, Y = np.meshgrid(x, y)
Z = np.sin(X) * np.cos(Y)

# Crear gráfico 3D
fig = plt.figure(figsize=(10, 7))
ax = fig.add_subplot(projection="3d")
ax.plot_surface(X, Y, Z, cmap="viridis")
ax.set_xlabel("X")
ax.set_ylabel("Y")
ax.set_zlabel("$z = \sin(x) \cos(y)$")
plt.title("Superficie 3D")
plt.show()

# 2.2
fig, ax = plt.subplots()
contour = ax.contourf(X, Y, Z, levels=12, cmap="RdBu")
plt.colorbar(contour)
ax.set_xlabel("x")
ax.set_ylabel("y")
ax.set_title("Curvas de nivel de f(x, y) = sin(x) cos(y)")
plt.show()

# 3.1
x, y = sp.symbols("x y")
g = x**3 - 3 * x + y**2 - 2 * y
print("Función g(x, y):", g)

# derivadas parciales
dg_dx = sp.diff(g, x)
dg_dy = sp.diff(g, y)
print("dg/dx =", dg_dx)
print("dg/dy =", dg_dy)

# 3.2
eq1 = sp.Eq(dg_dx, 0)
eq2 = sp.Eq(dg_dy, 0)

puntos_criticos = sp.solve((eq1, eq2), (x, y), dict=True)
print("Tiene", len(puntos_criticos), "puntos críticos.")
for punto in puntos_criticos:
    print(f"Punto crítico: {punto}")

# calculo de la hessiana usando las segundasa derivadas parciales
d2g_dx2 = sp.diff(dg_dx, x)
d2g_dy2 = sp.diff(dg_dy, y)
d2g_dxdy = sp.diff(dg_dx, y)
hessiana = sp.Matrix([[d2g_dx2, d2g_dxdy], [d2g_dxdy, d2g_dy2]])
print("Hessiana:", hessiana)
# por cada punto evaluamos la hessiana y calculamos el determinante
for punto in puntos_criticos:
    hessiana_evaluada = hessiana.subs({x: punto[x], y: punto[y]})
    det_hessiana = hessiana_evaluada.det()
    print("Determinante de la hessieana en el punto", punto, ":", det_hessiana)
