import pymunk
import pygame

# 1. Configuración de la ventana (Pygame)
pygame.init()
pantalla = pygame.display.set_mode((600, 600))
pygame.display.set_caption("Hello Munk")
reloj = pygame.time.Clock()

# 2. Configuración del mundo físico (Pymunk)
espacio = pymunk.Space()
espacio.gravity = (0, 900) # Gravedad hacia abajo (x, y)

# --- CREAR EL SUELO ---
suelo = pymunk.Segment(espacio.static_body, (0, 550), (600, 550), 5)
suelo.elasticity = 0.5
espacio.add(suelo)

# --- CREAR LA CAJA ---
masa = 1
lado = 50
momento = pymunk.moment_for_box(masa, (lado, lado))
cuerpo_caja = pymunk.Body(masa, momento)
cuerpo_caja.position = (300, 50) # Cae desde arriba en el centro

forma_caja = pymunk.Poly.create_box(cuerpo_caja, (lado, lado))
forma_caja.elasticity = 0.8
espacio.add(cuerpo_caja, forma_caja)

# 3. Bucle Principal
ejecutando = True
while ejecutando:
    for evento in pygame.event.get():
        if evento.type == pygame.QUIT:
            ejecutando = False

    # PASO DE SIMULACIÓN (Discretización Delta t)
    dt = 1/60.0
    espacio.step(dt)

    # Dibujado
    pantalla.fill((255, 255, 255)) # Fondo blanco
    
    # Dibujar suelo
    pygame.draw.line(pantalla, (0, 0, 0), (0, 550), (600, 550), 5)
    
    # Dibujar caja (convertimos posición de pymunk a enteros para pygame)
    x, y = cuerpo_caja.position
    pygame.draw.rect(pantalla, (235, 129, 27), (x - lado/2, y - lado/2, lado, lado))

    pygame.display.flip()
    reloj.tick(60)

pygame.quit()
