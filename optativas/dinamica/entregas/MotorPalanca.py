# -*- coding: utf-8 -*-
"""
Motor coche - Simulador de Telemetría Dinámica
Interfaz gráfica con disposición lado a lado (Controles e Izq. / Datos a la Der.)
"""

import tkinter as tk
from tkinter import ttk, messagebox
import numpy as np
from scipy.interpolate import interp1d

# ===========================================================================
#  DATOS Y CALCULOS (Física intacta)
# ===========================================================================
rpm_datos = np.array([1000,1500,2000,2500,3000,3500,4000,4500,
                      5000,5250,5500,6000,6500,7000,7500,8000,8500])
par_datos = np.array([320,345,370,400,415,430,445,455,
                      465,465,460,455,450,440,430,415,390])
interp_par = interp1d(rpm_datos, par_datos, kind="cubic", fill_value="extrapolate")

MARCHAS = {
    "R": -3.50, "0": 0.0, "1": 3.29, "2": 2.16, "3": 1.61, "4": 1.27, "5": 1.03, "6": 0.82
}
G_DIF = 4.30   # relación diferencial
ETA   = 0.85   # eficiencia mecánica
R_W   = 0.330  # radio rueda (m)

def calcular(rpm, marcha):
    if not (800 <= rpm <= 8500):
        raise ValueError("RPM fuera de rango (800 - 8500).")

    rpm   = float(rpm)
    T_mot = float(interp_par(rpm))
    P_cv  = (T_mot * rpm * 2 * np.pi) / (60 * 735.5)

    if marcha == "0":
        return dict(par_motor=T_mot, potencia=P_cv,
                    par_rueda=0.0, ft_n=0.0, ft_kn=0.0, vel=0.0, gk=0.0)

    gk    = MARCHAS[marcha]
    abs_gk = abs(gk)
    T_rue = T_mot * abs_gk * G_DIF * ETA
    FT_n  = T_rue / R_W
    v_kmh = (rpm * 2*np.pi/60) / (abs_gk*G_DIF) * R_W * 3.6

    return dict(par_motor=T_mot, potencia=P_cv,
                par_rueda=T_rue, ft_n=FT_n, ft_kn=FT_n/1000,
                vel=v_kmh, gk=gk)

# ===========================================================================
#  DISEÑO COLECTIVO DE COLORES (Tema Naranja Competición)
# ===========================================================================
ORANGE       = "#FF6600"  
FERRARI_GOLD = "#B8960C"
BG           = "#161616"  # Un tono más oscuro para el fondo general
PANEL        = "#222222"  # Paneles modulares gris oscuro
TEXT         = "#FFFFFF"  
MUTED        = "#999999"  

# ===========================================================================
#  VENTANA PRINCIPAL
# ===========================================================================
class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Motor coche - Panel de Telemetría")
        self.resizable(False, False)
        self.configure(bg=BG)

        self._vars = {}
        self._labels = {}

        # 1. Cabecera superior
        self._build_header()

        # 2. Contenedor Principal dividido en dos columnas (Lado a Lado)
        main_layout = tk.Frame(self, bg=BG, padx=20, pady=15)
        main_layout.pack(fill="both", expand=True)

        left_column = tk.Frame(main_layout, bg=BG)
        left_column.pack(side="left", fill="both", expand=True, padx=(0, 15))

        right_column = tk.Frame(main_layout, bg=BG)
        right_column.pack(side="right", fill="both", expand=True, padx=(15, 0))

        # 3. Construir elementos en sus respectivas columnas
        self._build_inputs(left_column)
        self._build_button(left_column)
        
        self._build_results(right_column)
        self._build_formula(right_column)

        # 4. Pie de página inferior
        self._build_footer()

        # Centrar en pantalla automáticamente
        self.update_idletasks()
        w = self.winfo_width()
        h = self.winfo_height()
        x = (self.winfo_screenwidth()  - w) // 2
        y = (self.winfo_screenheight() - h) // 2
        self.geometry(f"+{x}+{y}")

    def _build_header(self):
        f = tk.Frame(self, bg=ORANGE, pady=10)
        f.pack(fill="x")
        tk.Label(f, text="MOTOR COCHE", font=("Arial", 16, "bold"), fg="white", bg=ORANGE).pack()
        tk.Label(f, text="Cálculo Dinámico de Prestaciones en Tiempo Real", font=("Arial", 9), fg="#FFDDCC", bg=ORANGE).pack()

    # ── COLUMNA IZQUIERDA: CONTROLES DE ENTRADA ─────────────────────────────
    def _build_inputs(self, parent):
        f = tk.Frame(parent, bg=BG)
        f.pack(fill="both", expand=True)

        # Entrada numérica RPM
        tk.Label(f, text="Revoluciones del motor (RPM)", font=("Arial", 10, "bold"), fg=TEXT, bg=BG).pack(anchor="w", pady=(0,4))
        
        self.rpm_var = tk.StringVar(value="5250")
        entry_frame = tk.Frame(f, bg=PANEL, highlightthickness=1, highlightbackground="#444")
        entry_frame.pack(fill="x", pady=(0,6), ipady=2)
        
        tk.Entry(entry_frame, textvariable=self.rpm_var, font=("Arial", 13, "bold"), 
                 bg=PANEL, fg=TEXT, insertbackground=TEXT, relief="flat", bd=4, width=12).pack(side="left", fill="x", expand=True)
        tk.Label(entry_frame, text="rpm", font=("Arial", 10), fg=MUTED, bg=PANEL, padx=8).pack(side="right")

        # Deslizador RPM
        self.rpm_slider = tk.Scale(f, from_=800, to=8500, orient="horizontal", variable=self.rpm_var, resolution=50,
                                   bg=BG, fg=TEXT, troughcolor=PANEL, activebackground=ORANGE,
                                   highlightthickness=0, bd=0, font=("Arial", 8), length=260)
        self.rpm_slider.pack(fill="x", pady=(0,15))
        self.rpm_var.trace_add("write", self._sync_slider)

        # Palanca de Cambios Interactiva
        tk.Label(f, text="Posición de la Palanca de Cambios", font=("Arial", 10, "bold"), fg=TEXT, bg=BG).pack(anchor="w", pady=(0,4))

        self.marcha_var = tk.StringVar(value="1")
        
        # Coordenadas físicas de la H
        self.gear_coords = {
            "R": (45, 25),
            "1": (95, 25),  "3": (145, 25),  "5": (195, 25),
            "0": (145, 60),  # Punto muerto reubicado exactamente entre el 3 y el 4
            "2": (95, 95),  "4": (145, 95),  "6": (195, 95)
        }
        
        self.gate_segments = [
            ((45, 60), (195, 60)),    # Carril neutro horizontal
            ((45, 25), (45, 60)),     # Carril R
            ((95, 25), (95, 95)),     # Carril 1-2
            ((145, 25), (145, 95)),   # Carril 3-4
            ((195, 25), (195, 95))    # Carril 5-6
        ]

        # Lienzo del Shifter
        self.shifter = tk.Canvas(f, width=240, height=120, bg=PANEL, highlightthickness=1, highlightbackground="#333")
        self.shifter.pack(pady=5)
        
        # Pintar rejilla cromada/gris
        gate_color = "#999999"
        for seg in self.gate_segments:
            (x1, y1), (x2, y2) = seg
            self.shifter.create_line(x1, y1, x2, y2, fill=gate_color, width=6, capstyle=tk.ROUND)

        # Pintar números de marchas (Sin la letra N enmedio)
        for m, (x, y) in self.gear_coords.items():
            if m not in ("0", "R"):
                offset = -15 if y < 60 else 15
                self.shifter.create_text(x, y + offset, text=m, fill=MUTED, font=("Arial", 9, "bold"))
            elif m == "R":
                 self.shifter.create_text(x, y - 15, text="R", fill=ORANGE, font=("Arial", 9, "bold"))

        # Pomo naranja
        self.knob_r = 12
        self.knob = self.shifter.create_oval(0, 0, 0, 0, fill=ORANGE, outline="#FFF", width=1.5)

        self.shifter.bind("<B1-Motion>", self._drag_shifter)
        self.shifter.bind("<ButtonRelease-1>", self._drop_shifter)
        
        self.info_label = tk.Label(f, text="", font=("Arial", 9, "italic"), fg=ORANGE, bg=BG)
        self.info_label.pack(anchor="w", pady=(5,0))

        self._sel_marcha("1")

    def _build_button(self, parent):
        tk.Button(parent, text="RECALCULAR DATOS", font=("Arial", 11, "bold"),
                  bg=ORANGE, fg="white", activebackground="#CC5500", activeforeground="white",
                  relief="flat", bd=0, pady=8, cursor="hand2", command=self._calcular).pack(fill="x", pady=(10, 0))

    # ── COLUMNA DERECHA: PANEL DE TELEMETRÍA (NUEVO DISEÑO MODULAR) ─────────
    def _build_results(self, parent):
        # Contenedor de bloques
        container = tk.Frame(parent, bg=BG)
        container.pack(fill="both", expand=True)

        # --- BLOQUE 1: DIAGNÓSTICO DEL MOTOR ---
        b1 = tk.LabelFrame(container, text=" DIAGNÓSTICO MOTOR ", font=("Arial", 9, "bold"), fg=ORANGE, bg=PANEL, bd=1, relief="solid", padx=10, pady=5)
        b1.pack(fill="x", pady=(0, 8))
        b1.columnconfigure(1, weight=1)
        self._add_metric_field(b1, 0, "Par mecánico:", "par_motor", "Nm")
        self._add_metric_field(b1, 1, "Potencia efectiva:", "potencia", "CV")

        # --- BLOQUE 2: CONFIGURACIÓN DE TRANSMISIÓN ---
        b2 = tk.LabelFrame(container, text=" RELACIONES DE CAMBIO ", font=("Arial", 9, "bold"), fg=ORANGE, bg=PANEL, bd=1, relief="solid", padx=10, pady=5)
        b2.pack(fill="x", pady=8)
        b2.columnconfigure(1, weight=1)
        self._add_metric_field(b2, 0, "Marcha activa:", "marcha_sel", "")
        self._add_metric_field(b2, 1, "Relación interna (gk):", "gk", ":1")
        self._add_metric_field(b2, 2, "Relación del sistema:", "total_ratio", ":1")

        # --- BLOQUE 3: DINÁMICA DE TRACCIÓN (RUEDA) ---
        b3 = tk.LabelFrame(container, text=" OUTPUT EN RUEDA Y VELOCIDAD ", font=("Arial", 9, "bold"), fg=ORANGE, bg=PANEL, bd=1, relief="solid", padx=10, pady=5)
        b3.pack(fill="x", pady=(8, 0))
        b3.columnconfigure(1, weight=1)
        self._add_metric_field(b3, 0, "Par torsor final:", "par_rueda", "Nm")
        self._add_metric_field(b3, 1, "Fuerza de empuje:", "ft_kn", "kN")
        self._add_metric_field(b3, 2, "Velocidad teórica:", "vel", "km/h")

    def _add_metric_field(self, parent, row, label_text, var_key, unit_text):
        """Función auxiliar para crear filas de datos de forma limpia y uniforme"""
        tk.Label(parent, text=label_text, font=("Arial", 9), fg=MUTED, bg=PANEL, anchor="w").grid(row=row, column=0, sticky="w", pady=3)
        
        var = tk.StringVar(value="---")
        self._vars[var_key] = var
        
        lbl_val = tk.Label(parent, textvariable=var, font=("Courier New", 12, "bold"), fg=TEXT, bg=PANEL, anchor="e", width=9)
        lbl_val.grid(row=row, column=1, sticky="e", pady=3, padx=5)
        self._labels[var_key] = lbl_val
        
        tk.Label(parent, text=unit_text, font=("Arial", 9), fg=MUTED, bg=PANEL, anchor="w", width=5).grid(row=row, column=2, sticky="w", pady=3)

    def _build_formula(self, parent):
        f = tk.Frame(parent, bg=PANEL, padx=15, pady=8, highlightthickness=1, highlightbackground="#333")
        f.pack(fill="x", pady=(12, 0))
        tk.Label(f, text="Ecuaciones de física aplicadas:", font=("Arial", 8, "bold"), fg=ORANGE, bg=PANEL).pack(anchor="w")
        formulas = [
            "• T_rueda = T_motor * gk * G_dif * η",
            "• F_empuje = T_rueda / R_rueda",
            "• Velocidad = ω_motor / (gk * G_dif) * R_rueda"
        ]
        for txt in formulas:
            tk.Label(f, text=txt, font=("Courier", 8), fg="#888", bg=PANEL).pack(anchor="w")

    def _build_footer(self):
        tk.Label(self, text="Simulador Dinámico de Cinemática vehicular - Modelo Modular",
                 font=("Arial", 8), fg="#444", bg=BG).pack(pady=(8, 6))

    # ── LÓGICA MECÁNICA DE LA PALANCA RESTRINGIDA ───────────────────────────
    def _drag_shifter(self, event):
        closest_point = self._constrain_to_path(event.x, event.y)
        self._update_knob_coords(closest_point)

    def _drop_shifter(self, event):
        closest_point = self._constrain_to_path(event.x, event.y)
        self._update_knob_coords(closest_point)
        
        closest_gear = "0"
        min_dist = float('inf')
        for m, (gx, gy) in self.gear_coords.items():
            dist = (closest_point[0] - gx)**2 + (closest_point[1] - gy)**2
            if dist < min_dist:
                min_dist = dist
                closest_gear = m
        self._sel_marcha(closest_gear)

    def _constrain_to_path(self, ex, ey):
        closest_p = None
        min_dist = float('inf')
        for seg in self.gate_segments:
            (x1, y1), (x2, y2) = seg
            l2 = (x1-x2)**2 + (y1-y2)**2
            if l2 == 0: continue
            t = max(0, min(1, ((ex - x1) * (x2 - x1) + (ey - y1) * (y2 - y1)) / l2))
            px = x1 + t * (x2 - x1)
            py = y1 + t * (y2 - y1)
            dist = (ex - px)**2 + (ey - py)**2
            if dist < min_dist:
                min_dist = dist
                closest_p = (px, py)
        return closest_p

    def _update_knob_coords(self, point):
        x, y = point
        self.shifter.coords(self.knob, x - self.knob_r, y - self.knob_r, x + self.knob_r, y + self.knob_r)

    def _sel_marcha(self, m):
        self.marcha_var.set(m)
        self._update_knob_coords(self.gear_coords[m])

        if m == "0":
            txt = "Punto muerto (Selección libre)"
        elif m == "R":
            txt = "Inversión de marcha seleccionada [R]"
        else:
            txt = f"Relación de {m}ª velocidad engranada"
        self.info_label.configure(text=txt)

        if hasattr(self, '_vars') and 'par_motor' in self._vars:
            self._calcular()

    def _sync_slider(self, *args):
        try:
            v = int(float(self.rpm_var.get()))
            self.rpm_slider.set(max(800, min(8500, v)))
        except (ValueError, tk.TclError):
            pass

    def _calcular(self):
        try:
            rpm    = float(self.rpm_var.get())
            marcha = self.marcha_var.get()
            res    = calcular(rpm, marcha)
        except ValueError as e:
            messagebox.showerror("Error", str(e))
            return

        neutral = (marcha == "0")
        reverse = (marcha == "R")
        
        if neutral:   c_trans = MUTED
        elif reverse: c_trans = ORANGE
        else:         c_trans = TEXT

        total_ratio = MARCHAS[marcha] * G_DIF

        self._vars["par_motor"].set(f"{res['par_motor']:.1f}")
        self._vars["potencia"].set(f"{res['potencia']:.1f}")
        self._vars["marcha_sel"].set("Punto Muerto" if neutral else ("Atrás [R]" if reverse else f"{marcha}ª"))
        self._vars["gk"].set(f"{MARCHAS[marcha]:.2f}")
        self._vars["total_ratio"].set(f"{total_ratio:.2f}")
        self._vars["par_rueda"].set(f"{res['par_rueda']:.1f}")
        self._vars["ft_kn"].set(f"{res['ft_kn']:.2f}")
        self._vars["vel"].set(f"{res['vel']:.1f}")

        for key in ("marcha_sel", "gk", "total_ratio", "par_rueda", "ft_kn", "vel"):
            self._labels[key].configure(fg=c_trans)

if __name__ == '__main__':
    app = App()
    app.mainloop()