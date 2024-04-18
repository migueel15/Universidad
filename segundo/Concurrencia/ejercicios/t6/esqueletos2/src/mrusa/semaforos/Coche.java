package mrusa.semaforos;

import java.util.concurrent.Semaphore;

public class Coche{
	
	private int asientos; 	//Capacidad del coche
	
	private int numPas = 0;
	private Semaphore mutex = new Semaphore(1); //exclusion mutua
	
	private Semaphore esperaSubir = new Semaphore(1);
	private Semaphore esperaBajar = new Semaphore(0);
	private Semaphore esperaLleno = new Semaphore(0);
	
	
	public Coche(int tam){
		asientos = tam;
	}
	
	public Coche(){
		asientos = 5;
	}
	
	//CS1-Pasajero: espera hasta que se abra la puerta
	//CS2-Pasajero: espera hasta que pueda bajar
	public void darVuelta(int id) throws InterruptedException{
		
		
	}

	//CS-Control: espera hasta que este lleno
	public void esperaLleno() throws InterruptedException{
		//Se espera a que el coche este lleno
		esperaLleno.acquire();

	}

	public void finVuelta() throws InterruptedException {
		//Avisa a los pasajeros que deben bajar
		esperaBajar.release();
	}
}