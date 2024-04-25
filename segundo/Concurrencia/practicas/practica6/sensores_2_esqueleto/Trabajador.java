package sensores_2_esqueleto;
import java.util.*;

public class Trabajador extends Thread{
	private Mediciones m;
	private Random r = new Random();
	
	public Trabajador(Mediciones m){
		this.m = m;
	}
	
	public void run(){
		while (true){			
			try {
				System.out.println(java.util.Arrays.toString(m.leerMediciones()));
				Thread.sleep(r.nextInt(300)); //tareas
				m.finTarea();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
