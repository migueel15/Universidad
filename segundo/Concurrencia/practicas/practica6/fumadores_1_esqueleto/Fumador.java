package fumadores_1_esqueleto;
import java.util.Random;

public class Fumador extends Thread {
	private Random r;
	private int id; 
	private Mesa mesa;
	// 0 - Tabaco || 1 - Papel || 2 - Cerilla
	
	public Fumador(int id, Mesa mesa) {
		this.id = id;
		this.mesa = mesa;
		r = new Random();
	}
	
	public void run() {
		try {
		while(!this.isInterrupted()) {
			mesa.quiereFumar(id);
			Thread.sleep(100 +  r.nextInt(100));
			mesa.terminaFumar(id);
		}
		}catch(InterruptedException e) {}
	}
}
