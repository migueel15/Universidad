import cuentapalabras.*;
import java.util.NoSuchElementException;
import java.io.PrintWriter;
import java.io.IOException;
public class Main {
	public static void main(String [] args) {
		String [] datos = {
			"Guerra tenia una jarra y Parra tenia una perra, ",
			"pero la perra de Parra rompio la jarra de Guerra.",
			"Guerra pego con la porra a la perra de Parra. ",
			"!Oiga usted buen hombre de Parra! ",
			"Por que ha pegado con la porra a la perra de Parra.",
			"Porque si la perra de Parra no hubiera roto la jarra de Guerra,",
			"Guerra no hubiera pegado con la porra a la perra de Parra."};
		String delimitadores = "[ .,:;\\-\\!\\!\\?\\?]+"; // " .,:;-!!??" una o varias apariciones
		String [] noSig = { "CON", "LA", "A", "DE", "NO", "SI", "Y", "UNA" };

        System.out.println("ContadorPalabras ...");
		ContadorPalabras contador = new ContadorPalabras();
		ContadorPalabrasSig contadorSig = new ContadorPalabrasSig();
		contadorSig.leeArrayNoSig(noSig);

		// Incluimos todas las palabras que hay en datos
		// teniendo en cuenta los delimitadores
		contador.incluyeTodas(datos, delimitadores);
		contadorSig.incluyeTodas(datos, delimitadores);
		System.out.println(contador + "\n");
		System.out.println(contadorSig + "\n");
		try {
			System.out.println(contador.encuentra("parra"));
			System.out.println(contador.encuentra("Gorra"));
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
		}
		//Repetimos la salida con E/S desde ficheros
		System.out.println("Repetimos la ejecucion tomando la E/S desde/a fichero");
		ContadorPalabrasSig contadorSigFich=null;
		try {
			contador = new ContadorPalabras();
			contadorSigFich = new ContadorPalabrasSig();
			contadorSigFich.leeFicheroNoSig("fichNoSig.txt", delimitadores);
		} catch (IOException e) {
			System.out.println("ERROR:"+ e.getMessage());
		}
		// Incluimos todas las palabras que hay en datos.txt teniendo en cuenta los separadores
		try {
			contador.incluyeTodasFichero("datos.txt", delimitadores);
			contadorSigFich.incluyeTodasFichero("datos.txt", delimitadores);
			System.out.println(contador + "\n");
			System.out.println(contadorSigFich + "\n");
			//metodos para presentar por pantalla
			PrintWriter pw = new PrintWriter(System.out, true);
			contador.presentaPalabras(pw);
			//salida a fichero
			contador.presentaPalabras("salida.txt");
			//metodos para presentar por pantalla para No Significativas
			System.out.println();
			contadorSigFich.presentaPalabras(pw);
			//salida a fichero
			contadorSigFich.presentaPalabras("salidaNoSig.txt");
		} catch (IOException e) {
			System.out.println("ERROR:"+ e.getMessage());
		}
	}
}
