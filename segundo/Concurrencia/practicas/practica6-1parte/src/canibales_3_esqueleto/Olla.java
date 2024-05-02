package canibales_3_esqueleto;

import java.util.concurrent.*;

public class Olla implements IOlla {
	private final int MAX_RACIONES = 10;

	private Semaphore esperaVacia = new Semaphore(1);
	private Semaphore esperaLlena = new Semaphore(0);

	private int numRac = 0;

	public void nuevoExplorador() throws InterruptedException {
		esperaVacia.acquire();
		numRac = MAX_RACIONES;
		System.out.println("Se ha rellenado el caldero");
		esperaLlena.release();
	}

	public void comeRacion(int id) throws InterruptedException {
		esperaLlena.acquire();
		System.out.println("El canival " + id + " ha comido.");
		numRac--;
		if (numRac > 0) {
			esperaLlena.release();
		} else {
			esperaVacia.release();
		}
	}

}
