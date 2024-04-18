package mrusa.semaforosV2;

import java.util.concurrent.Semaphore;

public class Coche{
	
	private int asientos; 	//Capacidad del coche
	
	private int numPas = 0;

	private Semaphore esperaSubir;
	private Semaphore esperaBajar;
	private Semaphore esperaTerminar;
	private Semaphore mutex;
	private boolean moviendose = false;
	
	
	public Coche(int tam){
		asientos = tam;
		esperaSubir = new Semaphore(1);
		esperaBajar = new Semaphore(0);
		esperaTerminar = new Semaphore(1);
		mutex = new Semaphore(1);
	}
	
	public Coche(){
		this(5);
	}
	
	//CS1-Pasajero: espera hasta que se abra la puerta
	public void esperaSubir(int id) throws InterruptedException{
		esperaSubir.acquire();
		mutex.acquire();
		numPas++;
		System.out.println("El pasajero " + id + " sube al coche");
		mutex.release();

		if(numPas < asientos){
			esperaSubir.release();
		}

	}

	//CS2-Pasajero: espera hasta que pueda bajar
	public void esperaBajar(int id) throws InterruptedException{
		mutex.acquire();
		System.out.println("Pasajero " + id + " baja del coche");
		numPas--;
		mutex.release();
	}
	
	//CS-Control: espera hasta que este lleno
	public void esperaLleno() throws InterruptedException{
		//Se espera a que el coche este lleno
		esperaTerminar.acquire();

		System.out.println("Atraccion funcionando");
	}

	public void finVuelta() throws InterruptedException {
		//Avisa a los pasajeros que deben bajar

	}
}