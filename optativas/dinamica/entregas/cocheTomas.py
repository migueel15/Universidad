# -*- coding: utf-8 -*-
"""
Simulador de Telemetría Dinámica
Interfaz gráfica con palanca H y motor físico de aceleración 2D sin imágenes externas.
"""

import tkinter as tk
from tkinter import ttk, messagebox
import numpy as np
from scipy.interpolate import interp1d
import time

# ===========================================================================
#  FÍSICA DEL MOTOR (DATOS DE EJEMPLO GENÉRICOS)
# ===========================================================================
rpm_datos = np.array([1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 5200, 5500, 6000, 6500])
par_datos = np.array([145,  175,  220,  255,  270,  268,  260,  250,  240,  236,  225,  190,  150])
interp_par = interp1d(rpm_datos, par_datos, kind='cubic', fill_value="extrapolate")

# Relaciones de transmisión de ejemplo
MARCHAS = {"R": -3.00, "0": 0.0, "1": 3.364, "2": 2.054, "3": 1.381, "4": 1.037, "5": 0.821, "6": 0.700}
G_DIF = 3.44   
ETA   = 0.85   
R_W   = 0.30  

# ===========================================================================
#  ESTRUCTURA DINÁMICA DEL VEHÍCULO
# ===========================================================================
class CarSim:
    def __init__(self, canvas, x_inicio, y_inicio, color, usa_rozamiento, nombre):
        self.canvas = canvas
        self.x = x_inicio
        self.y = y_inicio
        self.usa_rozamiento = usa_rozamiento
        self.nombre = nombre
        self.color = color
        
        self.masa = 1215.0
        self.Cd = 0.31                
        self.Area = 1.95              
        self.rho = 1.225              
        self.Crr = 0.015              
        
        self.velocidad_ms = 0.0       
        self.tiempo_0_100 = 0.0
        self.medicion_completada = False

        # Dibujo del coche (sin imágenes externas)
        self.body = self.canvas.create_rectangle(self.x, self.y, self.x + 80, self.y + 25, fill=self.color, outline="black", width=2)
        self.cab = self.canvas.create_polygon(self.x + 20, self.y, self.x + 30, self.y - 12, self.x + 55, self.y - 12, self.x + 75, self.y, fill=self.color, outline="black")
        self.wheel_f = self.canvas.create_oval(self.x + 60, self.y + 15, self.x + 76, self.y + 31, fill="#333", outline="#FFF")
        self.wheel_r = self.canvas.create_oval(self.x + 10, self.y + 15, self.x + 26, self.y + 31, fill="#333", outline="#FFF")
        
        self.label = self.canvas.create_text(self.x + 40, self.y + 12, text="0 km/h", fill="white", font=("Arial", 8, "bold"))

    def actualizar(self, dt, rpm, marcha_str):
        gk = MARCHAS.get(marcha_str, 0.0)
        
        # Cronómetro 0-100
        if gk > 0 and self.velocidad_ms > 0.1 and not self.medicion_completada:
            if self.velocidad_ms < 27.7778: # 100 km/h en m/s
                self.tiempo_0_100 += dt
            else:
                self.medicion_completada = True

        # Fuerzas
        if gk <= 0.0:
            F_traccion = 0.0
        else:
            Tm = float(interp_par(max(1000, min(rpm, 6500))))
            Tr = Tm * gk * G_DIF * ETA
            F_traccion = Tr / R_W
        
        F_drag = 0.0
        F_rodadura = 0.0
        if self.usa_rozamiento and self.velocidad_ms > 0.1:
            F_drag = 0.5 * self.Cd * self.rho * self.Area * (self.velocidad_ms ** 2)
            F_rodadura = self.Crr * self.masa * 9.81
            
        F_neta = F_traccion - (F_drag + F_rodadura)
        aceleracion = F_neta / self.masa
        
        self.velocidad_ms += aceleracion * dt
        if self.velocidad_ms < 0: self.velocidad_ms = 0.0
        
        # Limitar velocidad física al régimen del motor
        if gk > 0.0:
            vel_max_teorica = (rpm * 2 * np.pi / 60) / (gk * G_DIF) * R_W
            if self.velocidad_ms > vel_max_teorica:
                self.velocidad_ms = vel_max_teorica

        # Movimiento visual en el canvas (Efecto bucle continuo)
        escala_px = 3.0
        desplazamiento = self.velocidad_ms * escala_px * dt
        self.x += desplazamiento
        
        if self.x > 800:  # Reset visual para que no desaparezca
            self.x = -80
            
        self.canvas.move(self.body, desplazamiento, 0)
        self.canvas.move(self.cab, desplazamiento, 0)
        self.canvas.move(self.wheel_f, desplazamiento, 0)
        self.canvas.move(self.wheel_r, desplazamiento, 0)
        self.canvas.move(self.label, desplazamiento, 0)
        
        if self.x < 0: # Para actualizar el texto en la posición correcta post-reset
            coords = self.canvas.coords(self.body)
            dx = self.x - coords[0]
            for item in (self.body, self.cab, self.wheel_f, self.wheel_r, self.label):
                self.canvas.move(item, dx, 0)

        self.canvas.itemconfig(self.label, text=f"{self.velocidad_ms * 3.6:.0f}")

# ===========================================================================
#  DISEÑO GUI Y APLICACIÓN
# ===========================================================================
ORANGE, BG, PANEL, TEXT, MUTED = "#FF6600", "#161616", "#222222", "#FFFFFF", "#999999"

class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Simulador Dinámico de Vehículos")
        self.resizable(False, False)
        self.configure(bg=BG)
        self._vars = {}
        self._labels = {}
        self.last_time = time.time()

        self._build_header()
        
        main_layout = tk.Frame(self, bg=BG, padx=20, pady=10)
        main_layout.pack(fill="both", expand=True)
        left_column = tk.Frame(main_layout, bg=BG)
        left_column.pack(side="left", fill="both", expand=True, padx=(0, 15))
        right_column = tk.Frame(main_layout, bg=BG)
        right_column.pack(side="right", fill="both", expand=True, padx=(15, 0))

        self._build_inputs(left_column)
        self._build_results(right_column)
        
        # Pista de aceleración nativa
        self.canvas = tk.Canvas(self, height=180, bg="#2A2A2A", highlightthickness=2, highlightbackground=ORANGE)
        self.canvas.pack(fill="x", padx=20, pady=(0, 20))
        
        # Líneas de carretera
        self.canvas.create_line(0, 90, 900, 90, fill="#555", width=2, dash=(10, 10))
        
        self.coche_azul = CarSim(self.canvas, 20, 50, "#0055AA", usa_rozamiento=True, nombre="Azul (Real)")
        self.coche_rojo = CarSim(self.canvas, 20, 130, "#AA0000", usa_rozamiento=False, nombre="Rojo (Ideal)")

        self._sel_marcha("0")
        self.update_physics() # Iniciar el bucle de físicas

    def _build_header(self):
        f = tk.Frame(self, bg=ORANGE, pady=10)
        f.pack(fill="x")
        tk.Label(f, text="SIMULADOR DINÁMICO", font=("Arial", 14, "bold"), fg="white", bg=ORANGE).pack()

    def _build_inputs(self, parent):
        f = tk.Frame(parent, bg=BG)
        f.pack(fill="both", expand=True)

        tk.Label(f, text="Acelerador (RPM)", font=("Arial", 10, "bold"), fg=TEXT, bg=BG).pack(anchor="w", pady=(0,4))
        self.rpm_var = tk.StringVar(value="3000")
        self.rpm_slider = tk.Scale(f, from_=1000, to=6500, orient="horizontal", variable=self.rpm_var, resolution=50,
                                   bg=BG, fg=TEXT, troughcolor=PANEL, activebackground=ORANGE,
                                   highlightthickness=0, bd=0, font=("Arial", 8), length=260)
        self.rpm_slider.pack(fill="x", pady=(0,15))

        tk.Label(f, text="Caja de Cambios (Posición Palanca)", font=("Arial", 10, "bold"), fg=TEXT, bg=BG).pack(anchor="w", pady=(0,4))
        self.marcha_var = tk.StringVar(value="0")
        
        self.gear_coords = {"R": (45, 25), "1": (95, 25), "3": (145, 25), "5": (195, 25), "0": (145, 60), "2": (95, 95), "4": (145, 95), "6": (195, 95)}
        self.gate_segments = [((45, 60), (195, 60)), ((45, 25), (45, 60)), ((95, 25), (95, 95)), ((145, 25), (145, 95)), ((195, 25), (195, 95))]

        self.shifter = tk.Canvas(f, width=240, height=120, bg=PANEL, highlightthickness=1, highlightbackground="#333")
        self.shifter.pack(pady=5)
        
        for seg in self.gate_segments:
            (x1, y1), (x2, y2) = seg
            self.shifter.create_line(x1, y1, x2, y2, fill=MUTED, width=6, capstyle=tk.ROUND)

        for m, (x, y) in self.gear_coords.items():
            if m not in ("0", "R"):
                self.shifter.create_text(x, y + (-15 if y < 60 else 15), text=m, fill=MUTED, font=("Arial", 9, "bold"))
            elif m == "R":
                 self.shifter.create_text(x, y - 15, text="R", fill=ORANGE, font=("Arial", 9, "bold"))

        self.knob_r = 12
        self.knob = self.shifter.create_oval(0, 0, 0, 0, fill=ORANGE, outline="#FFF", width=1.5)
        self.shifter.bind("<B1-Motion>", self._drag_shifter)
        self.shifter.bind("<ButtonRelease-1>", self._drop_shifter)

    def _build_results(self, parent):
        b1 = tk.LabelFrame(parent, text=" TELEMETRÍA 0-100 KM/H ", font=("Arial", 9, "bold"), fg=ORANGE, bg=PANEL, bd=1, padx=10, pady=5)
        b1.pack(fill="x", pady=(0, 8))
        self._add_metric_field(b1, 0, "Coche Azul (Real):", "t_azul", "s")
        self._add_metric_field(b1, 1, "Coche Rojo (Ideal):", "t_rojo", "s")

        b2 = tk.LabelFrame(parent, text=" DATOS MECÁNICOS ", font=("Arial", 9, "bold"), fg=ORANGE, bg=PANEL, bd=1, padx=10, pady=5)
        b2.pack(fill="x", pady=8)
        self._add_metric_field(b2, 0, "Par Motor (Nm):", "par", "")
        self._add_metric_field(b2, 1, "Potencia (CV):", "pot", "")
        self._add_metric_field(b2, 2, "Relación Total:", "rtot", ":1")

    def _add_metric_field(self, parent, row, label_text, var_key, unit_text):
        tk.Label(parent, text=label_text, font=("Arial", 9), fg=MUTED, bg=PANEL, anchor="w").grid(row=row, column=0, sticky="w", pady=3)
        var = tk.StringVar(value="---")
        self._vars[var_key] = var
        tk.Label(parent, textvariable=var, font=("Courier New", 12, "bold"), fg=TEXT, bg=PANEL, anchor="e", width=7).grid(row=row, column=1, sticky="e", pady=3, padx=5)
        tk.Label(parent, text=unit_text, font=("Arial", 9), fg=MUTED, bg=PANEL, anchor="w").grid(row=row, column=2, sticky="w", pady=3)

    def _drag_shifter(self, event):
        x, y = self._constrain_to_path(event.x, event.y)
        self.shifter.coords(self.knob, x - self.knob_r, y - self.knob_r, x + self.knob_r, y + self.knob_r)

    def _drop_shifter(self, event):
        x, y = self._constrain_to_path(event.x, event.y)
        closest_gear = min(self.gear_coords.keys(), key=lambda m: (x - self.gear_coords[m][0])**2 + (y - self.gear_coords[m][1])**2)
        self._sel_marcha(closest_gear)

    def _constrain_to_path(self, ex, ey):
        closest_p, min_dist = None, float('inf')
        for (x1, y1), (x2, y2) in self.gate_segments:
            l2 = (x1-x2)**2 + (y1-y2)**2
            t = max(0, min(1, ((ex - x1) * (x2 - x1) + (ey - y1) * (y2 - y1)) / l2)) if l2 > 0 else 0
            px, py = x1 + t * (x2 - x1), y1 + t * (y2 - y1)
            dist = (ex - px)**2 + (ey - py)**2
            if dist < min_dist: min_dist, closest_p = dist, (px, py)
        return closest_p

    def _sel_marcha(self, m):
        self.marcha_var.set(m)
        gx, gy = self.gear_coords[m]
        self.shifter.coords(self.knob, gx - self.knob_r, gy - self.knob_r, gx + self.knob_r, gy + self.knob_r)
        
        # Reset de cronos al volver a N
        if m == "0":
            self.coche_azul.tiempo_0_100 = 0.0
            self.coche_azul.medicion_completada = False
            self.coche_rojo.tiempo_0_100 = 0.0
            self.coche_rojo.medicion_completada = False

    def update_physics(self):
        current_time = time.time()
        dt = current_time - self.last_time
        self.last_time = current_time
        
        # Evitar saltos temporales grandes si la ventana se congela
        if dt > 0.1: dt = 0.1 

        rpm = float(self.rpm_var.get())
        marcha = self.marcha_var.get()
        
        self.coche_azul.actualizar(dt, rpm, marcha)
        self.coche_rojo.actualizar(dt, rpm, marcha)
        
        # Actualizar UI con datos teóricos instantáneos
        par_m = float(interp_par(max(1000, min(rpm, 6500))))
        pot_cv = (par_m * rpm * 2 * np.pi) / (60 * 735.5)
        
        self._vars["par"].set(f"{par_m:.1f}")
        self._vars["pot"].set(f"{pot_cv:.1f}")
        self._vars["rtot"].set(f"{MARCHAS.get(marcha, 0) * G_DIF:.2f}")
        
        t_a = f"{self.coche_azul.tiempo_0_100:.2f}" + (" ✓" if self.coche_azul.medicion_completada else "")
        t_r = f"{self.coche_rojo.tiempo_0_100:.2f}" + (" ✓" if self.coche_rojo.medicion_completada else "")
        self._vars["t_azul"].set(t_a if marcha != "0" else "---")
        self._vars["t_rojo"].set(t_r if marcha != "0" else "---")

        self.after(16, self.update_physics) # Bucle a ~60FPS

if __name__ == '__main__':
    app = App()
    app.mainloop()