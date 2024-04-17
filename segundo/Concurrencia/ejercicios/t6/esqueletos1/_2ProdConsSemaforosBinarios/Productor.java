package _2ProdConsSemaforosBinarios;

import java.util.Random;

public class Productor implements Runnable {
	private Random rnd = new Random();
	
	private int max;
	private Buffer buf;
	
	public Productor(Buffer b, int m) {
		if (m <= 0) {
			throw new IllegalArgumentException();
		}
		buf = b;
		max = m;
	}
	
	public void run() {
		try {
			for (int i = 0; i < max; ++i) {
				buf.insertar(i);
				Thread.sleep(50+rnd.nextInt(100));
				//Thread.sleep(50+rnd.nextInt(10000)); //El productor produce muy lento
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
