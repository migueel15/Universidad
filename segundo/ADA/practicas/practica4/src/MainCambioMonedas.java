import java.util.Arrays;

public class MainCambioMonedas {
	public static void main (String [] args) {
		//Creamos la instancia del problema
		int m = 8;
		int [] d = {6,1,4};
		CambioMonedas p = new CambioMonedas(m,d);
		int numeroMinimoMonedas = p.resolverBottomUp();
		p.mostrarDatos();
		System.out.println("Solución: " + Arrays.toString(p.reconstruirSol()));
		System.out.println("Número de monedas: " + numeroMinimoMonedas);
	}
}
