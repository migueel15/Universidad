import numpy as np
import matplotlib.pyplot as plt
import sympy as sp

# APARTADO 1
# puntos criticos
x, y = sp.symbols("x y")
f = x**3 - 3 * x * y**2
df_dx = sp.diff(f, x)
df_dy = sp.diff(f, y)

eq1 = sp.Eq(df_dx, 0)
eq2 = sp.Eq(df_dy, 0)

puntos_criticos = sp.solve((eq1, eq2), (x, y), dict=True)
print("Puntos críticos:", puntos_criticos)

# analizar puntos críticos con la hessiana
d2f_dx2 = sp.diff(df_dx, x)
d2f_dy2 = sp.diff(df_dy, y)
d2f_dxdy = sp.diff(df_dx, y)
hessiana = sp.Matrix([[d2f_dx2, d2f_dxdy], [d2f_dxdy, d2f_dy2]])
print("Hessiana:", hessiana)
for punto in puntos_criticos:
    hessiana_evaluada = hessiana.subs({x: punto[x], y: punto[y]})
    det_hessiana = hessiana_evaluada.det()
    if det_hessiana > 0:
        if hessiana_evaluada[0, 0] > 0:
            print(f"Punto {punto} es un mínimo local.")
        else:
            print(f"Punto {punto} es un máximo local.")
    elif det_hessiana < 0:
        print(f"Punto {punto} es un punto de silla.")
    else:
        print(f"Punto {punto} es inconcluso.")

# APARTADO 2
x = np.linspace(-np.pi, np.pi, 100)
y = np.linspace(-np.pi, np.pi, 100)
X, Y = np.meshgrid(x, y)
Z = np.sin(X) * np.cos(Y)

fig, ax = plt.subplots()
contour = ax.contourf(X, Y, Z, cmap="RdBu")
plt.colorbar(contour)
ax.set_xlabel("x")
ax.set_ylabel("y")
ax.set_title("f(x, y) = sin(x) cos(y)")
plt.show()
