import java.util.Arrays;

public class MainTarifa {
	public static void main(String[] args) {
		int[] estimacion= {1,12,3,4,8,6,11,1,2,12,1,6};
		
		TarifaTelefonica p=new TarifaTelefonica(3,3,1,estimacion);
		System.out.println("El pago acumulado es: " + p.resolverBottomUp());
		System.out.println("El plan que el estudiante seguirá es: ");
		System.out.println(Arrays.toString(p.reconstruirSol()));
		p.imprimeVectorSolucion();
	}

}
