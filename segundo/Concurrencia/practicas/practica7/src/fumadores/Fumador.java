package fumadores;
import java.util.*;
public class Fumador extends Thread{
	private static Random r = new Random();
	
	private Mesa m;
	
	private int id;
	public Fumador(Mesa m,int id){
		this.m = m;
		this.id = id;
	}

	public void run(){
		while (true){	
			try {
				m.quieroFumar(id);
				Thread.sleep(r.nextInt(200));
				m.finFumar(id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
