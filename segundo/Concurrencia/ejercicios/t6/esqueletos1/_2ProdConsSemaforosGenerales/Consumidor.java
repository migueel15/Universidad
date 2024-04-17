package _2ProdConsSemaforosGenerales;

import java.util.Random;

public class Consumidor implements Runnable {
	private Random rnd = new Random();
	
	private int max;
	private Buffer buf;
	
	public Consumidor(Buffer b, int m) {
		if (m <= 0) {
			throw new IllegalArgumentException();
		}
		buf = b;
		max = m;
	}
	public void run() {
		try {
			int x;
			for (int i = 0; i < max; ++i) {
				x = buf.extraer();
				//Thread.sleep(50+rnd.nextInt(10000)); //El consumidor consume muy lento
				Thread.sleep(50+rnd.nextInt(100)); //El consumidor consume muy lento
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

