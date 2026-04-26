import numpy as np
import matplotlib.pyplot as plt
from scipy.integrate import solve_ivp

def simulate_oscillator_scipy():
	# Parametros del sistema
	m, k, b = 0.2, 2.0, 0.1
	x0, v0 = 0.2, 0.0
	t_final = 8.0

	# Definimos el sistema de EDOs para SciPy: y = [posición, velocidad]
	def system_dynamics(t, y):
		x, v = y
		dxdt = v
		dvdt = (-b * v - k * x) / m
		return [dxdt, dvdt]

	# 1. SOLUCIÓN EXACTA (Como antes)
	t_exact = np.linspace(0, t_final, 1000)
	omega_n = np.sqrt(k/m)
	zeta = b / (2 * np.sqrt(m*k))
	omega_d = omega_n * np.sqrt(1 - zeta**2)
	x_exact_cm = 100 * np.exp(-zeta * omega_n * t_exact) * (
		x0 * np.cos(omega_d * t_exact) + (zeta * omega_n * x0 / omega_d) * np.sin(omega_d * t_exact)
	)

	# 2. RUNGE-KUTTA (RK45) usando SciPy
	# t_eval asegura que nos devuelva los puntos en el muestreo 'dt_large'
	dt_large = 0.2
	t_eval_rk = np.arange(0, t_final, dt_large)
	sol_rk = solve_ivp(system_dynamics, [0, t_final], [x0, v0], 
					   method='RK45', t_eval=t_eval_rk)

	# 3. EULER (Implementación manual, ya que SciPy no ofrece Euler por su imprecisión)
	def euler_method(dt):
		t_pts = np.arange(0, t_final, dt)
		x, v = x0, v0
		res = []
		for t in t_pts:
			res.append(x)
			dx, dv = system_dynamics(t, [x, v])
			x += dx * dt
			v += dv * dt
		return t_pts, np.array(res) * 100

	t_el, x_el_cm = euler_method(dt_large)
	dt_small = 0.025
	t_es, x_es_cm = euler_method(dt_small)

	# Gráfica
	plt.figure(figsize=(9, 6))
	plt.plot(t_exact, x_exact_cm, 'k', label='Exacta (Analítica)', linewidth=2)
	plt.plot(t_el, x_el_cm, 'r--', label=f'Euler (dt={dt_large})', alpha=0.8)
	plt.plot(t_es, x_es_cm, 'r', label=f'Euler (dt={dt_small})', alpha=0.4)
	
	# Graficamos la solución de SciPy (RK45)
	plt.plot(sol_rk.t, sol_rk.y[0]*100, 'go', label=f'SciPy RK45 (dt={dt_large})',
			 markersize=6, markeredgecolor='g', markerfacecolor='w', linewidth=2)

	plt.ylim(-25, 25)
	plt.title('Solución con SciPy (RK45) e Inestabilidad de Euler')
	plt.xlabel('Tiempo (s)')
	plt.ylabel('Posición (cm)')
	plt.legend(loc='upper right')
	plt.grid(True, linestyle='--')
	
	plt.savefig("ec_dif02.pdf")
	print("Gráfica guardada como ec_dif02.pdf")
	plt.show()

if __name__ == "__main__":
	simulate_oscillator_scipy()
