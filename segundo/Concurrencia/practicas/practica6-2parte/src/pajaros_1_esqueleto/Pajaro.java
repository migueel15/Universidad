package pajaros_1_esqueleto;

import java.util.Random;

public class Pajaro extends Thread {
	private Nido nido;
	private int id;
	private Random r;

	public Pajaro(int id, Nido n) {
		this.id = id;
		nido = n;
		r = new Random();
	}

	public void run() {
		try {
			while (!this.isInterrupted()) {
				Thread.sleep(r.nextInt(30)); // volando - atrapa bicho
				nido.depositarBicho(id);

			}
		} catch (InterruptedException e) {
		} finally {
			System.out.println("Pajaro " + id + " interrumpido");
		}
	}
}
