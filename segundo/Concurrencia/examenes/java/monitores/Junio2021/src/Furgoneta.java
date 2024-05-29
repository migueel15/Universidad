import java.util.Random;

// Furgoneta con tecnologia V2X 
// Tiene que unirse aa convoy para ir del origen al destino.
// Si es la primera en unirse al convoy se convierte en la lider.
// Si no es la primera, se une como seguidora.

public class Furgoneta extends Thread {
	private Convoy convoy;
	private int id, lider;
	private Random r;
	
	
	public Furgoneta (int id, Convoy c) {
		convoy = c;
		lider = -1;
		this.id = id;
		r = new Random();
	}
	
	public void run() {
			try{
				Thread.sleep(r.nextInt(50));
				//La furgoneta se une al convoy
				lider = convoy.unir(id);
				if(lider == id) { //Es la furgoneta lider
					convoy.calcularRuta(id);
					Thread.sleep(1000);
					convoy.destino(id);
				}else { //No es la furgoneta lider		
					convoy.seguirLider(id);
				}
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	
}
