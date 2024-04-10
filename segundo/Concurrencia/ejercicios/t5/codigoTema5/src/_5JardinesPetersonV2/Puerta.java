package _5JardinesPetersonV2;

public class Puerta extends Thread {
	private Peterson peterson;
	private Contador visitantes;
	private int iter;
	private int id;

	public Puerta(int id, Contador c, Peterson peterson, int iter) {
		visitantes = c;
		this.iter = iter;
		this.id = id;
		this.peterson = peterson;
		System.out.println("Por la puerta P" + this.id + " se esperan " + iter
				+ " visitantes");
	}

	public void run() {
		for (int i = 0; i < iter; i++) {
			peterson.entrar(id); //Protocolo de entrada
			visitantes.inc();
			peterson.salir(id);  //Protocolo de salida
		}
	}
}
