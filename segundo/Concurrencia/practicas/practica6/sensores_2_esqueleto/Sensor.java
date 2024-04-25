package sensores_2_esqueleto;
import java.util.*;
public class Sensor extends Thread{
	private static Random r = new Random();
	private Mediciones m;
	private int id;
	
	public Sensor(Mediciones m,int id){
		this.m = m;
		this.id = id;
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(r.nextInt(200));//tiempo de medir
				int valor = r.nextInt(10);
				System.out.println("Sensor "+ id + ": mide " + valor);
				m.nuevaMedicion(id,valor);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
