package mrusa.semaforos;
import java.util.*;
public class Pasajero extends Thread{
		private int id;
	private Coche c;
	private static Random r = new Random();
	
	public Pasajero(int id,Coche c){
		this.id = id;
		this.c = c;
	}
		
	public void run(){
		boolean fin = false;
		while (!this.isInterrupted()&& !fin){
			try{
				c.darVuelta(id);
				Thread.sleep(r.nextInt(500));
			}catch(InterruptedException ie){
				fin = true;
			}			
		}
	}
}
