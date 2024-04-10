package _8PanaderiaLamportAtomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class Panaderia {
	/** ¿Podemos hacer las variables turno y pidiendoTurno "volatile"?
	 *    - La respuesta es sí, pero hay un "pero..."
	 *    
	 *    Pero lo que se notifica al resto de hebras es una modificación
	 *    en la referencia al array.
	 *    
	 *    Por ejemplo, si hacemos: turno = new int[N]; 
	 *    todas las hebras son notificadas de que turno (referencia al array)
	 *    ha cambiado.
	 *    
	 *    No se notifican al esto de hebras los cambios en los elementos del
	 *    array.
	 *    
	 *    Por ejemplo, si hacemos turno[2] = 3; 
	 *    no hay notificación del cambio al resto de las hebras 
	 *    
	 *    ESO SIGNIFICA QUE EN ALGUNAS IMPLEMENTACIONES DE JAVA ESTA
	 *    IMPLEMENTACIÓN DEL ALGORITMO DE LAMPORT PODRÍA NO FUNCIONAR BIEN
	 *    
	 *    LAS HEBRAS CONSULTAN EL VALOR DE LOS COMPONENTES DE LOS ARRAYS 
	 *    turno Y pidiendoTurno MODIFICADOS POR OTRAS HEBRAS
	 *    
	 *    Posible solución -> Utilizar clases Java que aseguran un acceso
	 *    atómico a los elementos del array
	 *    
	 *    - array de enteros --> AtomicIntegerArray
	 *    - array de booleanos --> No hay un AtomicBooleanArray
	 */
	private AtomicIntegerArray turno;
	private AtomicIntegerArray pidiendoTurno;

	public Panaderia(int N) {
		turno = new AtomicIntegerArray(N); //elementos inicialmente a 0
		pidiendoTurno = new AtomicIntegerArray(N); //elementos a 0 --> false
	}

	public void cogeTurno(int id) {
		pidiendoTurno.set(id, 1); //1 --> true;
		int max = 0;
		for (int i = 0; i < turno.length(); i++)
			if (max < turno.get(i))
				max = turno.get(i);
		turno.set(id,max + 1);
		pidiendoTurno.set(id, 0); //0 --> true;
		System.out.println("Turno: " + turno);
	}

	private boolean meToca(int id, int i) {
		if (turno.get(i) > 0 && turno.get(i) < turno.get(id))
			return false;
		else if (turno.get(i) == turno.get(id) && i < id)
			return false;
		else
			return true;
	}

	public void esperoTurno(int id) {
		for (int i = 0; i < turno.length(); i++) {
			while (pidiendoTurno.get(i)==1)
				Thread.yield();
			while (!meToca(id, i))
				Thread.yield();
		}
	}

	public void salePanaderia(int id) {
		turno.set(id, 0);
	}
}

