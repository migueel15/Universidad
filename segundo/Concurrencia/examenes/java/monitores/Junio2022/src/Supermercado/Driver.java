package Supermercado;

import java.util.Random;

public class Driver {

	public static void main(String[] args) throws InterruptedException {
		final int NC = 15;
		Random r= new Random();
		Supermercado mkt = new SupermercadoSemaforos(); //Comenta esta linea para probar la version con monitores
		//Supermercado mkt = new SupermercadoMonitores(); //Descomenta esta linea para probar la version con monitores
		Cliente[] c1 = new Cliente[NC];
		for (int i=0; i<c1.length; i++) {
			c1[i] = new Cliente (mkt,i);
		}
		for (int i=0; i<c1.length; i++) {
			Thread.sleep(r.nextInt(1500));
			c1[i].start();
		}
		Thread.sleep(r.nextInt(1500));
		Cliente[] c2 = new Cliente[NC];
		for (int i=0; i<c2.length; i++) {
			c2[i] = new Cliente (mkt,i+15);
		}
		for (int i=0; i<c2.length; i++) {
			Thread.sleep(r.nextInt(1500));
			c2[i].start();
		}
		/*for (int i=0; i<c.length; i++) {
			c[i].join();
		}*/
		Thread.sleep(2000);
		mkt.fin();
	}

}
