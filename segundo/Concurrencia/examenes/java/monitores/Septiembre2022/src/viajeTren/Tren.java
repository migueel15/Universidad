package viajeTren;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tren {

	private int MAX_PASAJEROS_POR_VAGON = 5; // (5 , 5)
	private boolean trenLleno = false;
	private int pVagon1 = 0;
	private int pVagon2 = 0;

	Lock l = new ReentrantLock();
	Condition esperaMaquinista = l.newCondition();
	Condition colaParaSubir = l.newCondition();

	Condition esperaDentroVagon1 = l.newCondition();
	Condition esperaDentroVagon2 = l.newCondition();
	Condition primerVagonVacio = l.newCondition();

	public void viaje(int id) throws InterruptedException {
		// espera a subir
		try {
			l.lock();
			while (trenLleno) {
				colaParaSubir.await();
			}

			if (pVagon1 < MAX_PASAJEROS_POR_VAGON) {
				pVagon1++;
				System.out.println("Pasajero " + id + " sube al vagón 1");
				esperaDentroVagon1.await();
			} else if (pVagon2 < MAX_PASAJEROS_POR_VAGON) {
				pVagon2++;
				System.out.println("Pasajero " + id + " sube al vagón 2");
				if (pVagon2 == MAX_PASAJEROS_POR_VAGON) {
					trenLleno = true;
					esperaMaquinista.signal();
				}
				esperaDentroVagon2.await();
			}

			// se bajan
			if (pVagon1 > 0) {
				pVagon1--;
				System.out.println("Pasajero " + id + " se baja del vagón 1");
				if (pVagon1 == 0) {
					trenLleno = false;
					primerVagonVacio.signal();
				}
			} else if (pVagon2 > 0) {
				pVagon2--;
				System.out.println("Pasajero " + id + " se baja del vagón 2");

				if (pVagon2 == 0) {
					// trenLleno = false;
					colaParaSubir.signalAll();
				}
			}

		} finally {
			l.unlock();
		}

	}

	public void empiezaViaje() throws InterruptedException {
		try {
			l.lock();
			while (!trenLleno) {
				esperaMaquinista.await();
			}
			System.out.println("        Maquinista:  empieza el viaje");
		} finally {
			l.unlock();
		}
	}

	public void finViaje() throws InterruptedException {
		try {
			l.lock();
			System.out.println("        Maquinista:  fin del viaje");
			esperaDentroVagon1.signalAll();
			primerVagonVacio.await();
			esperaDentroVagon2.signalAll();
		} finally {
			l.unlock();
		}

	}
}
