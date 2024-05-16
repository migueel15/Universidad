package prodconsMon;
import java.util.Random;
public class Consumidor extends Thread{
	private Buffer b;
	private int id;
	private Random r = new Random();
	
	public Consumidor(Buffer b,int id){
		this.b = b;
		this.id = id;
	}
	
	public void run(){
		int i = 0;
		while (i<20){
			try {
				b.extrae(id);
				Thread.sleep(r.nextInt(200));
				i++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}