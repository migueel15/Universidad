package rusa_2_esqueleto;

public class Pasajero extends Thread {
	private int id;
	private Coche coche;
	
	public Pasajero(int id, Coche c) {
		coche = c;
		this.id = id;
	}
	
	public void run() {
		boolean fin = false;
		try {
			while(!isInterrupted() && !fin) {
				Thread.sleep(100);
				coche.quieroSubir(id);
				coche.quieroBajar(id);
			}
		}catch(InterruptedException e) {
			System.out.println("Pasajero"+id+" interrumpido");
			fin = true;
		}
		
	}
}
