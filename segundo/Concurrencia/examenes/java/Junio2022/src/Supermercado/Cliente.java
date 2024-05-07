package Supermercado;

import java.util.Random;

public class Cliente extends Thread {
	private Supermercado mkt;
	private int user;
	private Random r = new Random();
	public Cliente(Supermercado mkt, int id) {
		this.mkt = mkt;
		this.user = id;
	}
	
    @Override
	public void run() {
		
			for (int i=0; i<2; i++) try {
					sleep(r.nextInt(2000));
					mkt.nuevoCliente(user);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
}
