package _7PanaderiaLamport;

public class Panaderia {
	/** �Podemos hacer las variables turno y pidiendoTurno "volatile"?
	 *    - La respuesta es s�, pero hay un "pero..."
	 *    
	 *    Pero lo que se notifica al resto de hebras es una modificaci�n
	 *    en la referencia al array.
	 *    
	 *    Por ejemplo, si hacemos: turno = new int[N]; 
	 *    todas las hebras son notificadas de que turno (referencia al array)
	 *    ha cambiado.
	 *    
	 *    No se notifican al resto de hebras los cambios en los elementos del
	 *    array.
	 *    
	 *    Por ejemplo, si hacemos turno[2] = 3; 
	 *    no hay notificaci�n del cambio al resto de las hebras 
	 *    
	 *    ESO SIGNIFICA QUE EN ALGUNAS IMPLEMENTACIONES DE JAVA ESTA
	 *    IMPLEMENTACI�N DEL ALGORITMO DE LAMPORT PODR�A NO FUNCIONAR BIEN
	 *    
	 *    LAS HEBRAS CONSULTAN EL VALOR DE LOS COMPONENTES DE LOS ARRAYS 
	 *    turno Y pidiendoTurno MODIFICADOS POR OTRAS HEBRAS
	 *    
	 *    Posible soluci�n -> Utilizar clases Java que aseguran un acceso
	 *    at�mico a los elementos del array
	 *    
	 *    - array de enteros --> AtomicIntegerArray
	 *    - array de booleanos --> no hay un AtomicBooleanArray
	 */
	private int[] turno;
	private boolean[] pidiendoTurno;

	public Panaderia(int N) {
		turno = new int[N];
		pidiendoTurno = new boolean[N];
	}

	public void cogeTurno(int id) {
		pidiendoTurno[id] = true;
		int max = 0;
		for (int i = 0; i < turno.length; i++)
			if (max < turno[i])
				max = turno[i];
		turno[id] = max + 1;
		pidiendoTurno[id] = false;
		System.out.println("Turno: " + java.util.Arrays.toString(turno));
	}

	private boolean meToca(int id, int i) {
		if (turno[i] > 0 && turno[i] < turno[id])
			return false;
		else if (turno[i] == turno[id] && i < id)
			return false;
		else
			return true;
	}

	public void esperoTurno(int id) {
		for (int i = 0; i < turno.length; i++) {
			while (pidiendoTurno[i])
				Thread.yield();
			while (!meToca(id, i))
				Thread.yield();
		}
	}

	public void salePanaderia(int id) {
		turno[id] = 0;
	}
}

