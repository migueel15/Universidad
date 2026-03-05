import pymunk
import pygame

pygame.init()
screen = pygame.display.set_mode((800, 600))
pygame.display.set_caption("Pymunk Test")
reloj = pygame.time.Clock()
space = pymunk.Space()
ground = pymunk.Segment(space.static_body, (0, 550), (800, 550), 5)
ground.elasticity = 0.5
space.add(ground)

mass = 1
lado = 50
momento = pymunk.moment_for_box(mass, (lado, lado))
cuerpo = pymunk.Body(mass, momento)
cuerpo.position = (100, 100)
forma = pymunk.Poly.create_box(cuerpo, (lado, lado))
forma.elasticity = 0.5
space.add(cuerpo, forma)

def calc_gravedad(cuerpo, gravedad):
    cuerpo.apply_force_at_local_point(gravedad)

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
    calc_gravedad(cuerpo, (0, 100))
    screen.fill((255, 255, 255))
    space.step(1/60.0)
    pos = cuerpo.position
    pygame.draw.rect(screen, (255, 0, 0), (pos.x -25, pos.y-25, 50, 50))
    pygame.draw.line(screen, (0, 0, 0), (0, 550), (800, 550), 5)
    pygame.display.flip()
    reloj.tick(60)
