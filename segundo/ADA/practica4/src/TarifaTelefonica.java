import java.util.Arrays;

public class TarifaTelefonica {

	private int tarifaPlana, permanencia, tarifaMegas;
	private int[] estimacion;
	private int[] pago;// Pago mínimo a realizar durante los meses de i...n

	public TarifaTelefonica(int tp, int p, int tm, int[] est) {
		tarifaPlana = tp;
		permanencia = p;
		tarifaMegas = tm;
		estimacion = est;
		pago = null;
	}

	public int resolverBottomUp() {
		// n meses
		// dos tipos de tarifa (p, m) / si p -> permanencia

		int[][] matriz= new int[estimacion.length][2];
		
		//***Completar Implementación***
	}

	public int[] reconstruirSol() {
		if (pago == null) {
			throw new RuntimeException("Se debe resolver el problema primero");
		}
		
		//***Completar implementación***
		
	}

	public void imprimeVectorSolucion() {
		System.out.println(Arrays.toString(pago));
	}

}