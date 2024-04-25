package canibales_3_esqueleto;

import java.util.*;
public class Cocinero extends Thread{

	private IOlla n;
	private static Random r = new Random();
	public Cocinero(IOlla n){
		this.n = n;
	}	
	
	public void run(){
		while (true){
			try {
				Thread.sleep(r.nextInt(20));
				n.nuevoExplorador();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
