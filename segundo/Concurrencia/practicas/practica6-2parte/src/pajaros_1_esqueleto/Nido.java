package pajaros_1_esqueleto;

import java.util.concurrent.Semaphore;

public class Nido {
	private final int maxBichos;
	Semaphore esperaLleno = new Semaphore(1);
	Semaphore esperaVacio = new Semaphore(0);
	Semaphore mutex = new Semaphore(1);

	int numBichos;

	public Nido(int max) {
		maxBichos = max;
		numBichos = 0;
	}

	public void depositarBicho(int id) throws InterruptedException {
		esperaLleno.acquire();
		mutex.acquire();
		numBichos++;
		System.out.println("El padre " + id + " añade un bicho");
		if (numBichos < maxBichos) {
			esperaLleno.release();
		}
		if (numBichos == 1) {
			esperaVacio.release();
		}
		mutex.release();
	}

	public void comerBicho(int id) throws InterruptedException {
		esperaVacio.acquire();
		mutex.acquire();
		numBichos--;
		System.out.println("El hijo " + id + " se come un bicho");
		if (numBichos > 0) {
			esperaVacio.release();
		}
		if (numBichos == maxBichos - 1) {
			esperaLleno.release();
		}
		mutex.release();

	}

}
