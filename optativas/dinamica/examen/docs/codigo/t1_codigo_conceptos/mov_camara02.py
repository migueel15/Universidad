'''Movimiento con las flechas. Zoom con + y -'''

import pybullet as p
import pybullet_data
import time
import tkinter as tk
import numpy as np


INICIO_X=2200
INICIO_Y=150


def clamp(x, x_min, x_max):
    return max(x_min, min(x_max, x))

def rad(deg):
    return deg*np.pi/180.0

def deg(rad):
    return rad*180/np.pi

def wrap_angle_deg(a):
    return (a + 180.0) % 360.0 - 180.0
    
def wrap_zero360_deg(a):
	return (a) % 360

class App:
    def __init__(self):
        # --- Parámetros cámara ---
        self.YAW_SPEED_DEG = 0.2
        self.PITCH_SPEED_DEG = 0.1
        self.PITCH_MIN_DEG = -89.0
        self.PITCH_MAX_DEG = 89.0
        self.ZOOM_MIN = 0.4
        self.ZOOM_SMOOTH_ALPHA = 0.6
        self.ZOOM_SPEED=0.01  # 1% de aumento o disminución

        self.EPS_YAW_DEG = 0.05
        self.EPS_PITCH_DEG = 0.05
        self.EPS_DIST = 1e-3

        # --- Estado cámara ---
        self.yaw = 45.0
        self.pitch = -30.0
        self.dist = 3.0
        self.x=self.y=self.z=0
        self.actualiza_coord()

        # --- Estado HUD ---
        self.last_yaw = None
        self.last_pitch = None
        self.last_dist = None

        # --- Estado PyBullet/log ---
        self.log_id = -1
        self.running = True

        # --- Teclas ---
        self.KEY_LEFT = p.B3G_LEFT_ARROW
        self.KEY_RIGHT = p.B3G_RIGHT_ARROW
        self.KEY_UP = p.B3G_UP_ARROW
        self.KEY_DOWN = p.B3G_DOWN_ARROW
        self.KEY_QUIT = 27 
        self.KEY_PLUS = 43
        self.KEY_MINUS = 45

        # --- Crear ventana Tk ---
        self.root = tk.Tk()
        self.root.title("HUD Cámara")
        width, height = 670, 150
        
        # AJUSTE: +0+0 sitúa la ventana en la esquina superior izquierda de la pantalla
        self.root.geometry(f"{width}x{height}+{INICIO_X}+{INICIO_Y}")
        
        self.root.resizable(False, False)
        self.root.configure(bg="white")
        self.root.attributes("-topmost", True)

        try:
            font = ("Consolas", 12)
        except:
            font = ("TkFixedFont", 12)

        self.label_title = tk.Label(self.root, text="Coordenadas de la cámara", bg="white", fg="black",
            font=("Segoe UI", 14, "bold"), anchor="w")
        self.label_title.pack(fill="x", padx=8, pady=(6, 0))

        self.label_vals = tk.Label(self.root, text="(iniciando...)", bg="white", fg="black",
            font=font, anchor="w", justify="left")
        self.label_vals.pack(fill="x", padx=8, pady=(2, 6))

        self.root.protocol("WM_DELETE_WINDOW", self.request_quit)

        # --- Inicializar PyBullet ---
        self.init_pybullet()
        
        # --- Dibujar Ejes ---
        self.dibujar_ejes()

        # --- Arrancar loop ---
        self.root.after(0, self.tick)

    def dibujar_ejes(self):
        """ Dibuja los ejes X (Rojo), Y (Verde) y Z (Azul) en el origen """
        longitud = 2.0
        # Eje X - Rojo
        p.addUserDebugLine([0, 0, 0], [longitud, 0, 0], [1, 0, 0], lineWidth=15)
        # Eje Y - Verde
        p.addUserDebugLine([0, 0, 0], [0, longitud, 0], [0, 1, 0], lineWidth=15)
        # Eje Z - Azul
        p.addUserDebugLine([0, 0, 0], [0, 0, longitud], [0, 0, 1], lineWidth=15)

    def init_pybullet(self):
        p.connect(p.GUI,\
          options=f"--width=1200 --height= 600 --window_left={INICIO_X-500} --window_top={INICIO_Y}")
        p.setAdditionalSearchPath(pybullet_data.getDataPath())
        p.configureDebugVisualizer(p.COV_ENABLE_GUI, 0)
        p.configureDebugVisualizer(p.COV_ENABLE_SHADOWS, 1)
        p.setGravity(0, 0, -9.81)
        p.setRealTimeSimulation(0)

        p.loadURDF("plane.urdf")
        cube_half = 0.5
        self.cube_pos = [0.0, 0.0, cube_half]
        col = p.createCollisionShape(p.GEOM_BOX, halfExtents=[cube_half, cube_half, cube_half])
        vis = p.createVisualShape(p.GEOM_BOX, halfExtents=[cube_half, cube_half, cube_half],
            rgbaColor=[0.2, 0.6, 1.0, 1.0])
        p.createMultiBody(baseMass=0.0, baseCollisionShapeIndex=col, baseVisualShapeIndex=vis,
            basePosition=self.cube_pos, baseOrientation=[0, 0, 0, 1])

        p.resetDebugVisualizerCamera(self.dist, self.yaw, self.pitch, self.cube_pos)
        #si se activa esto puede grabr la sesion (pero sólo graba la ventana principal)
        #self.log_id = p.startStateLogging(p.STATE_LOGGING_VIDEO_MP4, "sesion.mp4")

    def request_quit(self):
        self.running = False

    def shutdown(self):
        try:
            if self.log_id != -1: p.stopStateLogging(self.log_id)
        except: pass
        try:
            if p.isConnected(): p.disconnect()
        except: pass
        try: self.root.quit()
        except: pass

    def tick(self):
        if not self.running or not p.isConnected():
            self.shutdown()
            return

        cam_info = p.getDebugVisualizerCamera()
        dist_gui = cam_info[10]
        self.dist = self.ZOOM_SMOOTH_ALPHA * dist_gui + (1.0 - self.ZOOM_SMOOTH_ALPHA) * self.dist
        if self.dist < self.ZOOM_MIN: self.dist = self.ZOOM_MIN

        keys = p.getKeyboardEvents()
        if (self.KEY_QUIT in keys) and (keys[self.KEY_QUIT] & p.KEY_WAS_TRIGGERED):
            self.running = False
            self.root.after(0, self.tick)
            return

        if self.KEY_LEFT in keys and keys[self.KEY_LEFT] & p.KEY_IS_DOWN: self.yaw -= self.YAW_SPEED_DEG
        if self.KEY_RIGHT in keys and keys[self.KEY_RIGHT] & p.KEY_IS_DOWN: self.yaw += self.YAW_SPEED_DEG
        if self.KEY_UP in keys and keys[self.KEY_UP] & p.KEY_IS_DOWN: self.pitch -= self.PITCH_SPEED_DEG
        if self.KEY_DOWN in keys and keys[self.KEY_DOWN] & p.KEY_IS_DOWN: self.pitch += self.PITCH_SPEED_DEG
        if self.KEY_PLUS in keys and keys[self.KEY_PLUS] & p.KEY_IS_DOWN: self.dist = self.dist*(1-self.ZOOM_SPEED)
        if self.KEY_MINUS in keys and keys[self.KEY_MINUS] & p.KEY_IS_DOWN: self.dist = self.dist*(1+self.ZOOM_SPEED)


        self.yaw = wrap_angle_deg(self.yaw)
        self.pitch = clamp(self.pitch, self.PITCH_MIN_DEG, self.PITCH_MAX_DEG)
        self.dist = clamp(self.dist, 1, 1000000)
        
        

        self.actualiza_coord()
        p.resetDebugVisualizerCamera(self.dist, self.yaw, self.pitch, self.cube_pos)

        need = False
        if self.last_yaw is None or abs(self.yaw - self.last_yaw) > self.EPS_YAW_DEG: need = True
        if self.last_pitch is None or abs(self.pitch - self.last_pitch) > self.EPS_PITCH_DEG: need = True
        if self.last_dist is None or abs(self.dist - self.last_dist) > self.EPS_DIST: need = True

        if need:
            txt = f"Cartesianas x: {self.x:.2f} m      y: {self.y:.1f} m      z: {self.z:.1f} m\n"+\
				  f"Esféricas   r: {self.dist:.2f} m      \u03C6: {wrap_zero360_deg(self.yaw-90):.1f}°      \u03B8: {wrap_zero360_deg(self.pitch+90):.1f}°\n\n"+\
				  f"PyBullet    Distance:{self.dist:.2f} m   Yaw: {self.yaw:.1f}°  Pitch:{self.pitch:.1f}°"
                  
            self.label_vals.config(text=txt)
            self.last_yaw, self.last_pitch, self.last_dist = self.yaw, self.pitch, self.dist

        p.stepSimulation()
        self.root.after(4, self.tick)

    def actualiza_coord(self): 
        self.z=-self.dist*np.sin(rad(self.pitch))
        rho=self.dist*np.cos(rad(self.pitch))
        self.x= rho*np.sin(rad(self.yaw))
        self.y=-rho*np.cos(rad(self.yaw))

    def run(self):
        self.root.mainloop()

def main():
    app = App()
    app.run()

if __name__ == "__main__":
    main()
