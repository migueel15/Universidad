package _5JardinesPetersonV2;

/** Sincronizaci?n con algoritmo de Peterson
 * 
 *   - Sincronizaci?n fuera de la variable compartida
 *   - M?todos adicionales en la clase Peterson para no distinguir en la hebra
 */
public class Jardines {
	private static int VISITANTES = 100000;

	public static void main(String[] args) {
		Contador visitantes = new Contador(); // Entidad pasiva. Recurso
												// compartido
		Peterson peterson = new Peterson();
		Puerta p1 = new Puerta(1, visitantes, peterson, VISITANTES);
		Puerta p2 = new Puerta(2, visitantes, peterson, VISITANTES);

		p1.start(); // Entidad activa
		p2.start(); // Entidad activa

		try {
			p1.join();
			p2.join();
		} catch (InterruptedException e) {
			System.out.println("La hebra ha sido interrumpida");
		}
		System.out.println("\nEl numero de visitantes contabilizado es "
				+ visitantes.valor());
		System.out.println("\nDeberian ser " + (VISITANTES * 2));
		System.out.println("La diferencia es: "
				+ (VISITANTES * 2 - visitantes.valor()));
	}
}
