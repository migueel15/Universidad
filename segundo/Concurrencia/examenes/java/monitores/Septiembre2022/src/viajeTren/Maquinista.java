package viajeTren;

import java.util.*;
public class Maquinista extends Thread{

	private Tren tren;
	public Maquinista(Tren tren) {
		this.tren = tren;
	}
	private Random r = new Random();
	public void run() {
		while (true) {
			try {
				tren.empiezaViaje();
				Thread.sleep(r.nextInt(1000));
				tren.finViaje();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
