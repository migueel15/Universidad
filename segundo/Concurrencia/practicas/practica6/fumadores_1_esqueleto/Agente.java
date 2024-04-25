package fumadores_1_esqueleto;
import java.util.Random;

public class Agente extends Thread {
	private Mesa mesa;
	private Random ingrediente;
	public Agente(Mesa mesa) {
		this.mesa = mesa;
		ingrediente = new Random();
	}
	public void run() {
		try {
			while(!this.isInterrupted()) {
				mesa.poneIngrediente(ingrediente.nextInt(3)); //el random sera 0 1 o 2
			}
		}catch(InterruptedException e) {}
	}
}
