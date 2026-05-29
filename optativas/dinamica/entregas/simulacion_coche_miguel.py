# -*- coding: utf-8 -*-
"""
Simulacion de coche para Miguel.

Mezcla la idea principal de los archivos de la carpeta:
- curva de par y calculo de potencia
- relaciones de cambio y palanca en H
- fisica longitudinal sencilla
- panel de telemetria y animacion 2D sin imagenes externas
"""

import math
import time
import tkinter as tk

RPM_DATOS = [
    1000,
    1500,
    2000,
    2500,
    3000,
    3500,
    4000,
    4500,
    5000,
    5250,
    5500,
    6000,
    6500,
    7000,
    7500,
    8000,
    8500,
]
PAR_DATOS = [
    320,
    345,
    370,
    400,
    415,
    430,
    445,
    455,
    465,
    465,
    460,
    455,
    450,
    440,
    430,
    415,
    390,
]

MARCHAS = {
    "R": -3.50,
    "0": 0.0,
    "1": 3.29,
    "2": 2.16,
    "3": 1.61,
    "4": 1.26,
    "5": 1.03,
    "6": 0.85,
}

DIFERENCIAL = 4.30
EFICIENCIA = 0.87
RADIO_RUEDA = 0.335

MASA = 1380.0
CD = 0.31
AREA_FRONTAL = 2.02
RHO = 1.225
CRR = 0.014
G = 9.81
MU = 1.02
FUERZA_FRENO_MAX = 9000.0

RPM_MIN = 900
RPM_MAX = 8500
RPM_CAMBIO = 7800
VEL_100_MS = 27.7778

BG = "#151515"
PANEL = "#222222"
CARD = "#2B2B2B"
TEXT = "#F4F4F4"
MUTED = "#9C9C9C"
ACCENT = "#00AEEF"
ACCENT_ALT = "#FF6A00"
GOOD = "#48D16D"
ROAD = "#444444"
SKY = "#9FD3FF"


def clamp(valor, minimo, maximo):
    return max(minimo, min(maximo, valor))


def interp_lineal(x, xs, ys):
    if x <= xs[0]:
        return ys[0]
    if x >= xs[-1]:
        return ys[-1]

    for i in range(len(xs) - 1):
        if xs[i] <= x <= xs[i + 1]:
            tramo = (x - xs[i]) / (xs[i + 1] - xs[i])
            return ys[i] + tramo * (ys[i + 1] - ys[i])
    return ys[-1]


def par_motor(rpm):
    rpm = clamp(rpm, RPM_DATOS[0], RPM_DATOS[-1])
    return interp_lineal(rpm, RPM_DATOS, PAR_DATOS)


def potencia_cv(par, rpm):
    return (par * rpm * 2 * math.pi) / (60 * 735.5)


def rpm_desde_velocidad(velocidad_ms, relacion_marcha):
    if velocidad_ms <= 0 or relacion_marcha == 0:
        return RPM_MIN

    rpm_rueda = velocidad_ms / (2 * math.pi * RADIO_RUEDA) * 60
    rpm_motor = rpm_rueda * abs(relacion_marcha) * DIFERENCIAL
    return clamp(rpm_motor, RPM_MIN, RPM_MAX)


class CocheMiguel:
    def __init__(self):
        self.reset()

    def reset(self):
        self.velocidad_ms = 0.0
        self.aceleracion_ms2 = 0.0
        self.distancia_m = 0.0
        self.rpm = RPM_MIN
        self.marcha = "0"
        self.acelerador = 0.0
        self.freno = 0.0
        self.par_motor_nm = par_motor(self.rpm)
        self.potencia_cv = 0.0
        self.par_rueda_nm = 0.0
        self.f_traccion_n = 0.0
        self.f_drag_n = 0.0
        self.f_rodadura_n = 0.0
        self.f_freno_n = 0.0
        self.f_neta_n = 0.0
        self.angulo_rueda = 0.0
        self.estado_traccion = "sin carga"
        self.tiempo_0_100 = 0.0
        self.objetivo_0_100 = False

    def actualizar(self, dt, marcha, acelerador, freno):
        relacion = MARCHAS[marcha]
        sentido = 1 if relacion > 0 else -1 if relacion < 0 else 0

        self.marcha = marcha
        self.acelerador = acelerador
        self.freno = freno

        if relacion == 0:
            self.rpm = clamp(RPM_MIN + acelerador * 2800, RPM_MIN, 4200)
        else:
            self.rpm = rpm_desde_velocidad(abs(self.velocidad_ms), relacion)

        self.par_motor_nm = par_motor(self.rpm)
        par_util = self.par_motor_nm * acelerador if relacion != 0 else 0.0
        self.potencia_cv = potencia_cv(self.par_motor_nm * acelerador, self.rpm)

        self.par_rueda_nm = par_util * abs(relacion) * DIFERENCIAL * EFICIENCIA
        fuerza_motor = self.par_rueda_nm / RADIO_RUEDA if relacion != 0 else 0.0
        limite_agarre = MU * MASA * G

        if fuerza_motor > limite_agarre:
            self.f_traccion_n = limite_agarre
            self.estado_traccion = "agarre limitado"
        elif relacion == 0:
            self.f_traccion_n = 0.0
            self.estado_traccion = "punto muerto"
        elif acelerador > 0:
            self.f_traccion_n = fuerza_motor
            self.estado_traccion = "traccion estable"
        else:
            self.f_traccion_n = 0.0
            self.estado_traccion = "retencion libre"

        velocidad_abs = abs(self.velocidad_ms)
        self.f_drag_n = 0.5 * RHO * CD * AREA_FRONTAL * velocidad_abs**2
        self.f_rodadura_n = CRR * MASA * G if velocidad_abs > 0.05 else 0.0
        self.f_freno_n = freno * FUERZA_FRENO_MAX

        f_neta = self.f_traccion_n * sentido
        if velocidad_abs > 0.05:
            f_neta -= math.copysign(
                self.f_drag_n + self.f_rodadura_n + self.f_freno_n, self.velocidad_ms
            )
        elif sentido != 0 and acelerador > 0:
            f_neta -= self.f_freno_n * sentido
        else:
            f_neta = 0.0

        self.f_neta_n = f_neta

        velocidad_previa = self.velocidad_ms
        self.aceleracion_ms2 = self.f_neta_n / MASA
        self.velocidad_ms += self.aceleracion_ms2 * dt

        if velocidad_previa > 0 > self.velocidad_ms and acelerador == 0:
            self.velocidad_ms = 0.0
        if velocidad_previa < 0 < self.velocidad_ms and acelerador == 0:
            self.velocidad_ms = 0.0
        if abs(self.velocidad_ms) < 0.03 and freno > 0.1:
            self.velocidad_ms = 0.0

        self.distancia_m += self.velocidad_ms * dt
        self.angulo_rueda += (self.velocidad_ms / RADIO_RUEDA) * dt

        if (
            self.velocidad_ms > 0.1
            and marcha not in ("0", "R")
            and acelerador > 0
            and not self.objetivo_0_100
        ):
            if self.velocidad_ms < VEL_100_MS:
                self.tiempo_0_100 += dt
            else:
                self.objetivo_0_100 = True


class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Simulacion Coche Miguel")
        self.configure(bg=BG)
        self.resizable(False, False)

        self.coche = CocheMiguel()
        self.acelerador_var = tk.DoubleVar(value=0.0)
        self.freno_var = tk.DoubleVar(value=0.0)
        self.marcha_var = tk.StringVar(value="0")
        self.info_marcha_var = tk.StringVar(value="Palanca en punto muerto")
        self.metricas = {}
        self.knob_r = 12
        self.road_offset = 0.0
        self.last_time = time.time()

        self._build_header()

        cuerpo = tk.Frame(self, bg=BG, padx=18, pady=14)
        cuerpo.pack(fill="both", expand=True)

        izquierda = tk.Frame(cuerpo, bg=BG)
        izquierda.pack(side="left", fill="both", padx=(0, 12))

        derecha = tk.Frame(cuerpo, bg=BG)
        derecha.pack(side="right", fill="both")

        self._build_controls(izquierda)
        self._build_metrics(derecha)
        self._build_canvas()
        self._build_footer()

        self.bind("<Escape>", lambda event: self.destroy())
        self.bind("<r>", lambda event: self._reiniciar())

        self._seleccionar_marcha("0")
        self._centrar()
        self.after(16, self._bucle)

    def _build_header(self):
        cabecera = tk.Frame(self, bg=ACCENT, pady=10)
        cabecera.pack(fill="x")
        tk.Label(
            cabecera,
            text="SIMULACION COCHE MIGUEL",
            bg=ACCENT,
            fg="white",
            font=("Arial", 16, "bold"),
        ).pack()
        tk.Label(
            cabecera,
            text="Telemetria, caja de cambios y animacion 2D autocontenida",
            bg=ACCENT,
            fg="#EAF8FF",
            font=("Arial", 9),
        ).pack()

    def _build_controls(self, parent):
        bloque = tk.Frame(parent, bg=BG)
        bloque.pack(fill="both")

        caja_palanca = tk.LabelFrame(
            bloque,
            text=" PALANCA EN H ",
            bg=PANEL,
            fg=ACCENT_ALT,
            font=("Arial", 9, "bold"),
            padx=10,
            pady=10,
            bd=1,
        )
        caja_palanca.pack(fill="x", pady=(0, 10))

        self.gear_coords = {
            "R": (45, 25),
            "1": (95, 25),
            "3": (145, 25),
            "5": (195, 25),
            "0": (145, 60),
            "2": (95, 95),
            "4": (145, 95),
            "6": (195, 95),
        }
        self.gate_segments = [
            ((45, 60), (195, 60)),
            ((45, 25), (45, 60)),
            ((95, 25), (95, 95)),
            ((145, 25), (145, 95)),
            ((195, 25), (195, 95)),
        ]

        self.shifter = tk.Canvas(
            caja_palanca,
            width=240,
            height=120,
            bg=CARD,
            highlightthickness=1,
            highlightbackground="#3A3A3A",
        )
        self.shifter.pack()

        for (x1, y1), (x2, y2) in self.gate_segments:
            self.shifter.create_line(
                x1, y1, x2, y2, fill="#999999", width=6, capstyle=tk.ROUND
            )

        for marcha, (x, y) in self.gear_coords.items():
            if marcha in ("0", "R"):
                continue
            offset = -15 if y < 60 else 15
            self.shifter.create_text(
                x, y + offset, text=marcha, fill=MUTED, font=("Arial", 9, "bold")
            )

        self.shifter.create_text(
            45, 12, text="R", fill=ACCENT_ALT, font=("Arial", 9, "bold")
        )
        self.knob = self.shifter.create_oval(
            0, 0, 0, 0, fill=ACCENT, outline="#F4F4F4", width=1.5
        )
        self.shifter.bind("<B1-Motion>", self._drag_shifter)
        self.shifter.bind("<ButtonRelease-1>", self._drop_shifter)

        tk.Label(
            caja_palanca,
            textvariable=self.info_marcha_var,
            bg=PANEL,
            fg=TEXT,
            font=("Arial", 9),
        ).pack(anchor="w", pady=(8, 0))

        pedales = tk.LabelFrame(
            bloque,
            text=" CONTROLES ",
            bg=PANEL,
            fg=ACCENT_ALT,
            font=("Arial", 9, "bold"),
            padx=10,
            pady=10,
            bd=1,
        )
        pedales.pack(fill="x")

        tk.Label(
            pedales, text="Acelerador", bg=PANEL, fg=TEXT, font=("Arial", 10, "bold")
        ).pack(anchor="w")
        tk.Scale(
            pedales,
            from_=0,
            to=100,
            variable=self.acelerador_var,
            orient="horizontal",
            resolution=1,
            bg=PANEL,
            fg=TEXT,
            troughcolor="#3B3B3B",
            activebackground=ACCENT,
            highlightthickness=0,
            bd=0,
            length=260,
        ).pack(fill="x", pady=(0, 10))

        tk.Label(
            pedales, text="Freno", bg=PANEL, fg=TEXT, font=("Arial", 10, "bold")
        ).pack(anchor="w")
        tk.Scale(
            pedales,
            from_=0,
            to=100,
            variable=self.freno_var,
            orient="horizontal",
            resolution=1,
            bg=PANEL,
            fg=TEXT,
            troughcolor="#3B3B3B",
            activebackground=ACCENT_ALT,
            highlightthickness=0,
            bd=0,
            length=260,
        ).pack(fill="x")

        botones = tk.Frame(pedales, bg=PANEL)
        botones.pack(fill="x", pady=(10, 0))
        tk.Button(
            botones,
            text="Reset",
            command=self._reiniciar,
            bg=ACCENT,
            fg="white",
            relief="flat",
            bd=0,
            padx=12,
            pady=6,
            cursor="hand2",
        ).pack(side="left", fill="x", expand=True, padx=(0, 5))
        tk.Button(
            botones,
            text="Soltar pedales",
            command=self._soltar_pedales,
            bg=ACCENT_ALT,
            fg="white",
            relief="flat",
            bd=0,
            padx=12,
            pady=6,
            cursor="hand2",
        ).pack(side="left", fill="x", expand=True, padx=(5, 0))

        tk.Label(
            bloque,
            text="Consejo: mete 1a, acelera y cambia cerca de 7800 rpm. Tecla R para reiniciar.",
            bg=BG,
            fg=MUTED,
            justify="left",
            font=("Arial", 8),
            wraplength=280,
        ).pack(anchor="w", pady=(10, 0))

    def _build_metrics(self, parent):
        self._crear_tarjeta(
            parent,
            " MOTOR ",
            [
                ("RPM", "rpm", "rpm"),
                ("Par", "par", "Nm"),
                ("Potencia", "potencia", "CV"),
            ],
        ).pack(fill="x", pady=(0, 10))

        self._crear_tarjeta(
            parent,
            " TRANSMISION ",
            [
                ("Marcha", "marcha", ""),
                ("Relacion total", "relacion", ":1"),
                ("Par rueda", "par_rueda", "Nm"),
                ("F traccion", "f_traccion", "N"),
                ("Estado", "estado", ""),
            ],
        ).pack(fill="x", pady=(0, 10))

        self._crear_tarjeta(
            parent,
            " DINAMICA ",
            [
                ("Velocidad", "velocidad", "km/h"),
                ("Aceleracion", "aceleracion", "m/s2"),
                ("Drag", "drag", "N"),
                ("Rodadura", "rodadura", "N"),
                ("Freno", "freno", "N"),
                ("Distancia", "distancia", "m"),
                ("0-100", "cero_cien", "s"),
            ],
        ).pack(fill="x")

    def _crear_tarjeta(self, parent, titulo, campos):
        tarjeta = tk.LabelFrame(
            parent,
            text=titulo,
            bg=PANEL,
            fg=ACCENT_ALT,
            font=("Arial", 9, "bold"),
            padx=10,
            pady=8,
            bd=1,
        )
        tarjeta.columnconfigure(1, weight=1)

        for fila, (texto, clave, unidad) in enumerate(campos):
            tk.Label(tarjeta, text=texto, bg=PANEL, fg=MUTED, font=("Arial", 9)).grid(
                row=fila, column=0, sticky="w", pady=3
            )
            var = tk.StringVar(value="---")
            self.metricas[clave] = var
            tk.Label(
                tarjeta,
                textvariable=var,
                bg=PANEL,
                fg=TEXT,
                font=("Courier New", 11, "bold"),
                width=12,
                anchor="e",
            ).grid(row=fila, column=1, sticky="e", padx=8, pady=3)
            tk.Label(tarjeta, text=unidad, bg=PANEL, fg=MUTED, font=("Arial", 9)).grid(
                row=fila, column=2, sticky="w", pady=3
            )

        return tarjeta

    def _build_canvas(self):
        self.canvas = tk.Canvas(
            self,
            width=940,
            height=250,
            bg="#1A1A1A",
            highlightthickness=2,
            highlightbackground=ACCENT,
        )
        self.canvas.pack(padx=18, pady=(4, 18))

    def _build_footer(self):
        tk.Label(
            self,
            text="Inspirado en las practicas de la carpeta: motor, RPM, marchas, telemetria y movimiento.",
            bg=BG,
            fg="#666666",
            font=("Arial", 8),
        ).pack(pady=(0, 8))

    def _centrar(self):
        self.update_idletasks()
        ancho = self.winfo_width()
        alto = self.winfo_height()
        x = (self.winfo_screenwidth() - ancho) // 2
        y = (self.winfo_screenheight() - alto) // 2
        self.geometry(f"+{x}+{y}")

    def _soltar_pedales(self):
        self.acelerador_var.set(0.0)
        self.freno_var.set(0.0)

    def _reiniciar(self):
        self.coche.reset()
        self.road_offset = 0.0
        self._soltar_pedales()
        self._seleccionar_marcha("0")

    def _drag_shifter(self, event):
        x, y = self._constrain_to_path(event.x, event.y)
        self.shifter.coords(
            self.knob,
            x - self.knob_r,
            y - self.knob_r,
            x + self.knob_r,
            y + self.knob_r,
        )

    def _drop_shifter(self, event):
        x, y = self._constrain_to_path(event.x, event.y)
        marcha = min(
            self.gear_coords.keys(),
            key=lambda m: (x - self.gear_coords[m][0]) ** 2
            + (y - self.gear_coords[m][1]) ** 2,
        )
        self._seleccionar_marcha(marcha)

    def _constrain_to_path(self, ex, ey):
        mejor_punto = None
        mejor_dist = float("inf")

        for (x1, y1), (x2, y2) in self.gate_segments:
            largo2 = (x2 - x1) ** 2 + (y2 - y1) ** 2
            if largo2 == 0:
                continue
            t = max(
                0.0, min(1.0, ((ex - x1) * (x2 - x1) + (ey - y1) * (y2 - y1)) / largo2)
            )
            px = x1 + t * (x2 - x1)
            py = y1 + t * (y2 - y1)
            dist = (ex - px) ** 2 + (ey - py) ** 2
            if dist < mejor_dist:
                mejor_dist = dist
                mejor_punto = (px, py)

        return mejor_punto

    def _seleccionar_marcha(self, marcha):
        self.marcha_var.set(marcha)
        x, y = self.gear_coords[marcha]
        color = ACCENT_ALT if marcha == "R" else ACCENT if marcha != "0" else "#B0B0B0"
        self.shifter.itemconfig(self.knob, fill=color)
        self.shifter.coords(
            self.knob,
            x - self.knob_r,
            y - self.knob_r,
            x + self.knob_r,
            y + self.knob_r,
        )

        if marcha == "0":
            self.info_marcha_var.set("Palanca en punto muerto")
        elif marcha == "R":
            self.info_marcha_var.set("Marcha atras seleccionada")
        else:
            self.info_marcha_var.set(f"Marcha {marcha} engranada")

    def _bucle(self):
        ahora = time.time()
        dt = min(0.05, ahora - self.last_time)
        self.last_time = ahora

        marcha = self.marcha_var.get()
        acelerador = self.acelerador_var.get() / 100.0
        freno = self.freno_var.get() / 100.0

        self.coche.actualizar(dt, marcha, acelerador, freno)
        self.road_offset = (self.road_offset + self.coche.velocidad_ms * dt * 45) % 140

        self._actualizar_metricas()
        self._dibujar_escena()
        self.after(16, self._bucle)

    def _actualizar_metricas(self):
        relacion_total = MARCHAS[self.marcha_var.get()] * DIFERENCIAL

        self.metricas["rpm"].set(f"{self.coche.rpm:7.0f}")
        self.metricas["par"].set(f"{self.coche.par_motor_nm:7.1f}")
        self.metricas["potencia"].set(f"{self.coche.potencia_cv:7.1f}")
        self.metricas["marcha"].set(
            "Neutro" if self.coche.marcha == "0" else self.coche.marcha
        )
        self.metricas["relacion"].set(f"{relacion_total:7.2f}")
        self.metricas["par_rueda"].set(f"{self.coche.par_rueda_nm:7.1f}")
        self.metricas["f_traccion"].set(f"{self.coche.f_traccion_n:7.0f}")
        self.metricas["estado"].set(self.coche.estado_traccion)
        self.metricas["velocidad"].set(f"{self.coche.velocidad_ms * 3.6:7.1f}")
        self.metricas["aceleracion"].set(f"{self.coche.aceleracion_ms2:7.2f}")
        self.metricas["drag"].set(f"{self.coche.f_drag_n:7.0f}")
        self.metricas["rodadura"].set(f"{self.coche.f_rodadura_n:7.0f}")
        self.metricas["freno"].set(f"{self.coche.f_freno_n:7.0f}")
        self.metricas["distancia"].set(f"{self.coche.distancia_m:7.1f}")

        cero_cien = f"{self.coche.tiempo_0_100:5.2f}"
        if self.coche.objetivo_0_100:
            cero_cien += " ok"
        self.metricas["cero_cien"].set(cero_cien)

    def _dibujar_escena(self):
        self.canvas.delete("dinamico")
        ancho = int(self.canvas["width"])
        alto = int(self.canvas["height"])
        road_y = 150

        self.canvas.create_rectangle(
            0, 0, ancho, road_y, fill=SKY, outline="", tags="dinamico"
        )
        self.canvas.create_rectangle(
            0, road_y, ancho, alto, fill=ROAD, outline="", tags="dinamico"
        )
        self.canvas.create_line(
            0, road_y, ancho, road_y, fill="#F1F1F1", width=4, tags="dinamico"
        )

        arbol_offset = (self.road_offset * 0.45) % 220
        for x in range(-220, ancho + 220, 220):
            px = x - arbol_offset
            self.canvas.create_rectangle(
                px + 18, 90, px + 30, 150, fill="#7A4D22", outline="", tags="dinamico"
            )
            self.canvas.create_oval(
                px, 55, px + 48, 105, fill="#2D9357", outline="", tags="dinamico"
            )
            self.canvas.create_oval(
                px + 14, 40, px + 62, 90, fill="#267A49", outline="", tags="dinamico"
            )

        for x in range(-140, ancho + 140, 140):
            self.canvas.create_rectangle(
                x - self.road_offset,
                road_y + 40,
                x + 60 - self.road_offset,
                road_y + 50,
                fill="#FAFAFA",
                outline="",
                tags="dinamico",
            )

        self._dibujar_coche(170, 125)

        self.canvas.create_text(
            70,
            30,
            text=f"{self.coche.velocidad_ms * 3.6:5.1f} km/h",
            fill="#111111",
            font=("Arial", 21, "bold"),
            tags="dinamico",
        )
        self.canvas.create_text(
            78,
            58,
            text=f"RPM {int(self.coche.rpm):4d}   Marcha {self.coche.marcha}",
            fill="#111111",
            font=("Consolas", 12, "bold"),
            tags="dinamico",
        )

        self._dibujar_barra(
            660, 30, 190, 14, self.acelerador_var.get() / 100.0, ACCENT, "Acelerador"
        )
        self._dibujar_barra(
            660, 64, 190, 14, self.freno_var.get() / 100.0, ACCENT_ALT, "Freno"
        )

        if self.coche.rpm > RPM_CAMBIO and self.coche.marcha not in ("0", "R", "6"):
            self.canvas.create_text(
                748,
                118,
                text="SUBE MARCHA",
                fill=ACCENT_ALT,
                font=("Arial", 18, "bold"),
                tags="dinamico",
            )
        elif self.coche.marcha == "R":
            self.canvas.create_text(
                735,
                118,
                text="MODO REVERSA",
                fill=ACCENT_ALT,
                font=("Arial", 18, "bold"),
                tags="dinamico",
            )
        elif self.coche.objetivo_0_100:
            self.canvas.create_text(
                760,
                118,
                text="0-100 COMPLETO",
                fill=GOOD,
                font=("Arial", 18, "bold"),
                tags="dinamico",
            )

    def _dibujar_barra(self, x, y, ancho, alto, valor, color, texto):
        self.canvas.create_text(
            x,
            y - 12,
            text=texto,
            fill="#111111",
            font=("Arial", 10, "bold"),
            tags="dinamico",
        )
        self.canvas.create_rectangle(
            x,
            y,
            x + ancho,
            y + alto,
            fill="#D7D7D7",
            outline="#222222",
            tags="dinamico",
        )
        self.canvas.create_rectangle(
            x,
            y,
            x + ancho * clamp(valor, 0.0, 1.0),
            y + alto,
            fill=color,
            outline="",
            tags="dinamico",
        )

    def _dibujar_coche(self, x, y):
        color_coche = (
            ACCENT_ALT
            if self.coche.marcha == "R"
            else ACCENT if self.coche.marcha != "0" else "#808080"
        )

        self.canvas.create_rectangle(
            x,
            y,
            x + 240,
            y + 52,
            fill=color_coche,
            outline="#111111",
            width=2,
            tags="dinamico",
        )
        self.canvas.create_polygon(
            x + 42,
            y,
            x + 92,
            y - 26,
            x + 165,
            y - 26,
            x + 210,
            y,
            fill=color_coche,
            outline="#111111",
            width=2,
            tags="dinamico",
        )
        self.canvas.create_polygon(
            x + 92,
            y - 22,
            x + 118,
            y - 6,
            x + 159,
            y - 6,
            x + 182,
            y - 22,
            fill="#CBE9FF",
            outline="#1B4B62",
            tags="dinamico",
        )
        self.canvas.create_rectangle(
            x + 210,
            y + 18,
            x + 228,
            y + 30,
            fill="#FFE58A",
            outline="",
            tags="dinamico",
        )
        self.canvas.create_rectangle(
            x + 12, y + 18, x + 28, y + 30, fill="#FFB0B0", outline="", tags="dinamico"
        )
        self.canvas.create_text(
            x + 120,
            y + 22,
            text="MIGUEL",
            fill="white",
            font=("Arial", 11, "bold"),
            tags="dinamico",
        )

        self._dibujar_rueda(x + 65, y + 57, 24)
        self._dibujar_rueda(x + 188, y + 57, 24)

    def _dibujar_rueda(self, cx, cy, radio):
        self.canvas.create_oval(
            cx - radio,
            cy - radio,
            cx + radio,
            cy + radio,
            fill="#202020",
            outline="#F3F3F3",
            width=2,
            tags="dinamico",
        )
        self.canvas.create_oval(
            cx - 8,
            cy - 8,
            cx + 8,
            cy + 8,
            fill="#8A8A8A",
            outline="#EAEAEA",
            tags="dinamico",
        )

        for fase in (0.0, math.pi / 2, math.pi, 3 * math.pi / 2):
            angulo = self.coche.angulo_rueda + fase
            x2 = cx + math.cos(angulo) * (radio - 4)
            y2 = cy + math.sin(angulo) * (radio - 4)
            self.canvas.create_line(
                cx, cy, x2, y2, fill="#DADADA", width=2, tags="dinamico"
            )


if __name__ == "__main__":
    app = App()
    app.mainloop()
