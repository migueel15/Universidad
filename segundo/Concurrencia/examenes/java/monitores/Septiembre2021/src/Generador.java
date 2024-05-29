import java.util.Random;

public class Generador extends Thread {
	private final int MAX_VALOR = 100;
	private Random rnd = new Random();
	
	private int nDatos;
	private DatoCompartido dato;
	
	public Generador(int nDatos, DatoCompartido dato) {
		this.dato = dato;
		this.nDatos = nDatos;
	}
	
	public void run() {
		try {
			int d;
			for (int i=0; i<nDatos; i++) {
				d = dato.generaDato(rnd.nextInt(MAX_VALOR));
				System.out.println("Resultado procesamiento = " + d);
				System.out.println("-------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
