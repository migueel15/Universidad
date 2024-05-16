package prodconsMon;

import java.util.*;
public class Productor extends Thread{
	private Buffer b;
	private Random r = new Random();
	public Productor(Buffer b){
		this.b = b;
	}
	
	public void run(){
		int i = 0;
		while (i<20){
			try {
				//Thread.sleep(r.nextInt(200));
				b.almacenar(i);
				i++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
