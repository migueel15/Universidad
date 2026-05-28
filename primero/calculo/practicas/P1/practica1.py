import numpy as np
import matplotlib.pyplot as plt

# sistemas de ecuaciones
A = np.array([[2, 1, -1], [-3, -1, 2], [1, 4, 1]])
b = np.array([3, -5, 6])
x = np.linalg.solve(A, b)
print("Solución del sistema de ecuaciones:", x)

# arrays
a = np.linspace(0, 2 * np.pi, 50)
sin_a = np.sin(a)
cos_a = np.cos(a)
plt.plot(a, sin_a, label="sin(a)")
plt.plot(a, cos_a, label="cos(a)")
plt.legend()
plt.title("Funciones seno y coseno")
plt.xlabel("a")
plt.ylabel("Valor")
plt.grid()
plt.show()

# raices n-esimas
n = 7
decimales = 3

# circulo unitario para representacion
fig, ax = plt.subplots()
theta = np.linspace(0, 2 * np.pi, 100)
ax.plot(np.cos(theta), np.sin(theta), "k--", alpha=0.3)

# calculo de las raices n-esimas de la unidad
k = np.arange(n)
theta = 2 * np.pi * k / n
raices = np.exp(1j * theta)
# for i, z in enumerate(raices):
#     print(f"z_{i}: {z:.{decimales}f}")
#
for i in range(n):
    print(
        f"z_{i} = {np.real(raices[i]):.{decimales}f} + {np.imag(raices[i]):.{decimales}f}i"
    )

ax.scatter(raices.real, raices.imag, s=100, c="red")
ax.set_aspect("equal")
ax.grid(True)
ax.set_xlabel("Parte real")
ax.set_ylabel("Parte imaginaria")
plt.show()
