import pymunk
import pygame
import pymunk.pygame_util
import numpy as np
import tkinter as tk
from tkinter import messagebox # Necesaria para el error en confirmar

######################## parte de TKINTER #############################
######################## parte de TKINTER #############################
def pedir_angulo_gui(angulo='30.0',roz='0.5'):
    """
    Crea una ventana de Tkinter con dos botones.
    Devuelve el valor (float) si acepta, o -1 si sale o cancela.
    """
    # Ahora el resultado guarda ambos valores
    resultado = {"angulo": angulo, "rozamiento": roz} 

    root = tk.Tk()
    root.title("Entrada de Parámetros")
    root.geometry("300x220") # Un poco más alta para el nuevo campo
    root.attributes("-topmost", True)

    tk.Label(root, text="Introduce el ángulo de la fuerza:", 
             font=("Arial", 10)).pack(pady=5)
    
    entry = tk.Entry(root)
    entry.insert(0, angulo)
    entry.pack(pady=5)
    
    # Campo para el coeficiente de rozamiento
    tk.Label(root, text="Introduce el coeficiente de rozamiento:", 
             font=("Arial", 10)).pack(pady=5)
    
    entry_mu = tk.Entry(root)
    entry_mu.insert(0, roz)
    entry_mu.pack(pady=5)
    
    entry.focus_set()

    def confirmar():
        try:
            valor = float(entry.get())
            mu = float(entry_mu.get())
            resultado["angulo"] = valor
            resultado["rozamiento"] = mu
            root.destroy()
        except ValueError:
            messagebox.showerror("Error", "Introduce un número válido")

    def salir():
        resultado["angulo"] = -1
        root.destroy()

    # Contenedor para los botones (uno al lado del otro)
    frame_botones = tk.Frame(root)
    frame_botones.pack(pady=15)

    btn_aceptar = tk.Button(frame_botones, text="Aceptar", command=confirmar, width=10)
    btn_aceptar.pack(side=tk.LEFT, padx=5)

    btn_salir = tk.Button(frame_botones, text="Salir", command=salir, width=10)
    btn_salir.pack(side=tk.LEFT, padx=5)

    # 'Enter' confirma, 'Escape' sale
    root.bind('<Return>', lambda event: confirmar())
    root.bind('<Escape>', lambda event: salir())

    root.mainloop()
    return resultado # Devolvemos el diccionario completo
#--------------------------------------
def ventana_final():
    root = tk.Tk()
    root.title("")
    root.geometry("200x100")
    root.attributes("-topmost", True)
    
    tk.Label(root, text="Simulación terminada").pack(pady=10)
    
    # El botón destruye la ventana y permite que el código siga
    btn = tk.Button(root, text="OK", command=root.destroy, width=10)
    btn.pack(pady=5)
    
    root.mainloop()

#####################################################################


def run_simulation(parametros):
    angulo_grados = parametros["angulo"]
    mu_rozamiento = parametros["rozamiento"]
    
    pygame.init()
    screen = pygame.display.set_mode((800, 400))
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("Arial", 18) # Fuente para el texto
    draw_options = pymunk.pygame_util.DrawOptions(screen)

    # 1. Configuración del Espacio
    space = pymunk.Space()
    space.gravity = (0, 981)

    # 2. Creación del Suelo (Plano horizontal)
    ground = pymunk.Segment(space.static_body, (50, 350), (750, 350), 5)
    ground.friction = mu_rozamiento # Usamos el valor pedido
    space.add(ground)

    # 3. Creación de la Caja
    mass = 1
    size = (50, 50)
    moment = pymunk.moment_for_box(mass, size)
    body = pymunk.Body(mass, moment)
    body.position = (100, 320)
    
    shape = pymunk.Poly.create_box(body, size)
    shape.friction = mu_rozamiento # Usamos el valor pedido
    space.add(body, shape)

    # Parámetros de la fuerza
    alpha = angulo_grados * np.pi / 180.0
    fuerza_magnitud = 400
    
    # Vector de fuerza
    force_vector = (fuerza_magnitud * np.cos(alpha), -fuerza_magnitud * np.sin(alpha))

    running = True
    v0=0
    
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        # --- MODIFICACIÓN 2: Fuerza externa solo hasta el centro (400px) ---
        if body.position.x < 400:
            body.apply_force_at_local_point(force_vector, (0, 0))

        # Paso de física
        dt = 1.0 / 60.0
        space.step(dt)
        
        # Dibujado
        screen.fill((255, 255, 255))
        space.debug_draw(draw_options)

        # --- MODIFICACIÓN 2: Flecha solo hasta el centro ---
        if body.position.x < 400:
            start_p = body.position
            end_p = start_p + pymunk.Vec2d(80 * np.cos(alpha), -80 * np.sin(alpha))
            pygame.draw.line(screen, (255, 0, 0), start_p, end_p, 3)
            
            # Punta de la flecha dinámica
            angle_arrow = -alpha
            p1 = end_p
            p2 = end_p + pymunk.Vec2d(10, 0).rotated(angle_arrow + 2.6)
            p3 = end_p + pymunk.Vec2d(10, 0).rotated(angle_arrow - 2.6)
            pygame.draw.polygon(screen, (255, 0, 0), [p1, p2, p3])

        aceleracion=(body.velocity.x-v0)/dt
        v0=body.velocity.x
        screen.blit(font.render(f'Velocidad    = {body.velocity.x/10:0.2f} m/s',True,(0,0,0)), (20,20))
        screen.blit(font.render(f'Aceleración = {aceleracion/10:0.2f} m/s²',True,(0,0,0)), (20,45))
        screen.blit(font.render(f'Fx = {force_vector[0]:0.2f} m/s²',True,(0,0,0)), (20,70))

        pygame.display.flip()
        clock.tick(60)
        if body.position.x> 1000 or body.position.y<0:  # 800 -100:
            ventana_final()
            pygame.quit()
            running=False
            return parametros["angulo"],parametros["rozamiento"]

    pygame.quit()
    return parametros["angulo"],parametros["rozamiento"]

if __name__ == "__main__":
    
    angulo='30.0'
    rozamiento='0.5'
    while 1:
        res_gui = pedir_angulo_gui(angulo, rozamiento)
        if res_gui["angulo"] < 0: exit()
        angulo, rozamiento = res_gui0=run_simulation(res_gui)
