package pajaros_1_esqueleto;

import java.util.Random;

public class Polluelo extends Thread {
	private int id;
	private Nido nido;
	Random r;

	public Polluelo(int id, Nido n) {
		this.id = id;
		nido = n;
		r = new Random();
	}

	public void run() {
		try {
			while (!this.isInterrupted()) {
				Thread.sleep(r.nextInt(40) + 10);
				nido.comerBicho(id);
			}
		} catch (InterruptedException e) {
		} finally {
			System.out.println("Polluelo " + id + " interrumpido");
		}

	}
}
