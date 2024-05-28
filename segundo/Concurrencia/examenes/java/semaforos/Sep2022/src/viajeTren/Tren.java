package viajeTren;

import java.util.concurrent.Semaphore;

public class Tren {
	private int MAX_PASAJEROS_BAGON = 10;
	private int pasajeros1 = 0;
	private int pasajeros2 = 0;

	private Semaphore mutexPasajeros = new Semaphore(1);
	private Semaphore lleno = new Semaphore(1);
	private Semaphore enMarcha = new Semaphore(0);
	private Semaphore bajando[] = new Semaphore[MAX_PASAJEROS_BAGON*2];
	private Semaphore primerBagonVacio = new Semaphore(0);
	private Semaphore segundoBagonVacio = new Semaphore(0);

	public void viaje(int id) throws InterruptedException {
		lleno.acquire();
		mutexPasajeros.acquire();

		if(pasajeros1 < MAX_PASAJEROS_BAGON){
			pasajeros1++;
			System.out.println("El pasajero " + id + " sube al bagon 1." + " B1:" + pasajeros1 + " B2:" + pasajeros2);
		} else if (pasajeros2 < MAX_PASAJEROS_BAGON) {
			pasajeros2++;
			System.out.println("El pasajero " + id + " sube al bagon 2." + " B1:" + pasajeros1 + " B2:" + pasajeros2);
		}

		if(pasajeros1+pasajeros2 < 2*MAX_PASAJEROS_BAGON){
			lleno.release();
		}else{
			enMarcha.release();
		}
		bajando[id] = new Semaphore(0);
		mutexPasajeros.release();
		bajando[id].acquire();
		mutexPasajeros.acquire();
		if(id < MAX_PASAJEROS_BAGON){
			pasajeros1--;
			System.out.println("El pasajero " + id + " baja del bagon 1." + " B1:" + pasajeros1 + " B2:" + pasajeros2);
			if(pasajeros1 == 0){
				primerBagonVacio.release();
			}
		}else{
			pasajeros2--;
			System.out.println("El pasajero " + id + " baja del bagon 2." + " B1:" + pasajeros1 + " B2:" + pasajeros2);
			if(pasajeros2 == 0){
				segundoBagonVacio.release();
			}
		}
		mutexPasajeros.release();
	}

	public void empiezaViaje() throws InterruptedException {
		enMarcha.acquire();
		System.out.println("        Maquinista:  empieza el viaje");
	}

	public void finViaje() throws InterruptedException {
		System.out.println("        Maquinista:  fin del viaje");
		for(int i = 0; i < MAX_PASAJEROS_BAGON;i++){
			bajando[i].release();
		}
		primerBagonVacio.acquire();
		for(int i = 0; i < MAX_PASAJEROS_BAGON;i++){
			bajando[MAX_PASAJEROS_BAGON+i].release();
		}

		segundoBagonVacio.acquire();
		System.out.println("*******************************");
		lleno.release();
	}
}
