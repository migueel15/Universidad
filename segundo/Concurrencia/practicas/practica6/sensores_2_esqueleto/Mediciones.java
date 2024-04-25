package sensores_2_esqueleto;

import java.util.concurrent.*;
import java.util.concurrent.Semaphore;

public class Mediciones {
	private int mediciones[];
	private int numMediciones = 0;

	private Semaphore mutex = new Semaphore(1);
	private Semaphore barrera = new Semaphore(0);
	private Semaphore trabajador = new Semaphore(0);

	public Mediciones() {
		mediciones = new int[3];

	}

	public void nuevaMedicion(int id, int valor) throws InterruptedException {
		mutex.acquire();
		numMediciones++;
		mediciones[id] = valor;

		if (numMediciones == 3) {
			trabajador.release();
		}
		mutex.release();
		barrera.acquire();

		mutex.acquire();
		numMediciones--;

		if (numMediciones > 0) {
			barrera.release();
		}
		mutex.release();

	}

	public int[] leerMediciones() throws InterruptedException {
		// El trabajador se debe quedar bloqueado mientras no haya mediciones
		trabajador.acquire();
		System.out.println("El trabajador lee las mediciones");

		return mediciones;
	}

	public void finTarea() {
		// El trabajador termina y despierta a los tres sensores
		System.out.println("El trabajador termina su tarea");
		barrera.release();
	}

}
