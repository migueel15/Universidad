package _5JardinesPetersonV3;

public class Puerta extends Thread {
	private Contador visitantes; //recurso que asegura la exclusi�n mutua
	private int iter;
	private int id;

	public Puerta(int id, Contador c, int iter) {
		visitantes = c;
		this.iter = iter;
		this.id = id;
		System.out.println("Por la puerta P" + this.id + " se esperan " + iter
				+ " visitantes");
	}

	public void run() {
		for (int i = 0; i < iter; i++) {
			visitantes.inc(id); //el acceso en exclusi�n mutua se asegura en la implementaci�n de inc
		}
	}
}
