package fumadores_1_esqueleto;

import java.util.concurrent.Semaphore;

import dataStructures.list.ArrayList;
import dataStructures.list.List;

public class Mesa {
	// 0 (Papel) /1 (Tabaco) /2 (Cerillas);
	// ingrediente == 0 - en la mesa están los ing. 1 y 2
	// ingrediente == 1 - en la mesa están los ing. 0 y 2
	// ingrediente == 2 - en la mesa están los ing. 0 y 1
	private int ingrediente;

	List<Semaphore> lista;
	Semaphore agente = new Semaphore(1);

	public Mesa() {
		ingrediente = -1; // -1 indica que no hay nada en la mesa
		lista = new ArrayList<>();
		Semaphore papeles = new Semaphore(0);
		Semaphore tabaco = new Semaphore(0);
		Semaphore cerillas = new Semaphore(0);
		lista.add(papeles);
		lista.add(tabaco);
		lista.add(cerillas);
	}

	public void quiereFumar(int id) throws InterruptedException {
		lista.get(id).acquire();
		System.out.println("Fumador " + id + " empieza a fumar");
	}

	/* El fumador id indica que ha terminado de fumar */
	public void terminaFumar(int id) {
		System.out.println("Fumador " + id + " termina de fumar ");
		agente.release();

	}

	// CS_Agente - El agente tiene que esperar si la mesa no está vacía
	public void poneIngrediente(int ing) throws InterruptedException {
		agente.acquire();
		ingrediente = ing;
		System.out.println("Agente pone ingredientes que necesita fumador " + ingrediente);
		lista.get(ing).release();

	}
}
