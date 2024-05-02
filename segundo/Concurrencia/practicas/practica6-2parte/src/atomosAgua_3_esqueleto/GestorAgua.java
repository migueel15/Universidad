package atomosAgua_3_esqueleto;

import java.util.concurrent.Semaphore;

public class GestorAgua {
	private int nHidrogeno = 0;
	private Semaphore hidrogenoLleno = new Semaphore(1);
	private Semaphore oxigenoLleno = new Semaphore(0);

	private Semaphore mutexH = new Semaphore(1);
	private Semaphore barrera = new Semaphore(1);

	public void hListo(int id) throws InterruptedException {
		hidrogenoLleno.acquire();
		mutexH.acquire();
		nHidrogeno++;
		System.out.println(id + " añade H");

		if (nHidrogeno < 2) {
			hidrogenoLleno.release();
		}

		if (nHidrogeno == 2) {
			oxigenoLleno.acquire();
			nHidrogeno = 0;
			barrera.release();
			hidrogenoLleno.release();
			System.out.println("Se abre la barrera");
		}
		mutexH.release();
	}

	public void oListo(int id) throws InterruptedException {
		barrera.acquire();
		oxigenoLleno.release();
		System.out.println(id + " añade O");
	}
}
