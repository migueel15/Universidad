package recursos;

import java.util.*;
public class Proceso extends Thread{
	private static Random r = new Random();
	private int NUM;
	private int id;
	private Control c;
	
	public Proceso(int id, Control c, int num){
		this.id = id;
		this.c = c;
		this.NUM = num;
	}
		
	public void run(){
		while (true){			
			try {
				int rec = r.nextInt(this.NUM-1)+1; //rec > 0 && rec <=NUM
				c.qRecursos(id, rec);
				Thread.sleep(r.nextInt(200));
				c.libRecursos(id, rec);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
}
