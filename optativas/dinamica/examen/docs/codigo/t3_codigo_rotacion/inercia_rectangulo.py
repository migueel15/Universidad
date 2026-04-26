import pymunk


def ejecutar_comparativa():
    # Configuración inicial
    mass = 1.0
    w, h = 10, 5

    # 	print(pymunk.moment_for_circle(5,0,10))

    # exit()

    # ---------------------------------------------------------
    # 1. RECTÁNGULO USANDO moment_for_box
    # ---------------------------------------------------------
    # Esta función asume que el eje de rotación pasa por el CM.
    # Formula: 1/12 * m * (w^2 + h^2)
    i_box = pymunk.moment_for_box(mass, (w, h))

    body_box = pymunk.Body(mass, i_box)
    # Al crear el shape, Pymunk lo centra en el body (0,0)
    shape_box = pymunk.Poly.create_box(body_box, (w, h))

    # ---------------------------------------------------------
    # 2. POLÍGONO USANDO moment_for_poly
    # ---------------------------------------------------------
    # Definimos los vértices desde la esquina (0,0)
    # Esto desplaza el eje de rotación a un extremo del rectángulo.
    vertices = [(0, 0), (10, 0), (10, 5), (0, 5)]

    # centroide =pymunk.Poly(pymunk.Body(), vertices).center_of_gravity
    # vertices = [p-centroide for p in vertices]

    i_poly = pymunk.moment_for_poly(mass, vertices)

    body_poly = pymunk.Body(mass, i_poly)
    shape_poly = pymunk.Poly(body_poly, vertices)

    # ---------------------------------------------------------
    # IMPRESIÓN DE RESULTADOS
    # ---------------------------------------------------------
    print("--- COMPARATIVA DE MOMENTOS DE INERCIA ---")
    print(f"Caja (moment_for_box):")
    print(f"	- Inercia: {i_box:.4f}")
    print(f"	- Posición del Body: {body_box.position}")
    print(f"	- Centro de la forma: {shape_box.center_of_gravity}")

    print("\nPolígono (moment_for_poly con vértices 0,0 a 10,5):")
    print(f"	- Inercia: {i_poly:.4f}")
    print(f"	- Posición del Body: {body_poly.position}")
    print(f"	- Centro de la forma: {shape_poly.center_of_gravity}")


if __name__ == "__main__":
    ejecutar_comparativa()
