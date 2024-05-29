import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Convoy {
	private int TAM_CONVOY;
	private int numFurgonetas = 0;

	private int idLider = -1;
	private boolean terminado = false;

	Lock l = new ReentrantLock();
	Condition esperaLider = l.newCondition();
	Condition esperaSeguidoras = l.newCondition();
	Condition esperaAbandonoConvoy = l.newCondition();

	public Convoy(int tam) {
		TAM_CONVOY = tam;
	}

	public int unir(int id) {
		try {
			l.lock();
			if (numFurgonetas == 0) {
				idLider = id;
				numFurgonetas++;
				System.out.println("** Furgoneta " + id + " lidera del convoy **");
				return id;
			} else {
				numFurgonetas++;
				System.out.println("Furgoneta " + id + " seguidora: sigue a " + idLider + " en el convoy");
				if (numFurgonetas == TAM_CONVOY) {
					esperaLider.signal();
				}

				return idLider;
			}

		} finally {
			l.unlock();
		}
	}

	/**
	 * La furgoneta lider espera a que todas las furgonetas se unan al convoy
	 * Cuando esto ocurre calcula la ruta y se pone en marcha
	 * 
	 * @throws InterruptedException
	 */
	public void calcularRuta(int id) throws InterruptedException {
		try {
			l.lock();
			while (numFurgonetas < TAM_CONVOY) {
				esperaLider.await();
			}
			System.out.println("** Furgoneta " + id + " lider:  ruta calculada, nos ponemos en marcha **");
		} finally {
			l.unlock();
		}
	}

	/**
	 * La furgoneta lider avisa al las furgonetas seguidoras que han llegado al
	 * destino y deben abandonar el convoy
	 * La furgoneta lider espera a que todas las furgonetas abandonen el convoy
	 * 
	 * @throws InterruptedException
	 **/
	public void destino(int id) throws InterruptedException {
		try {
			l.lock();
			terminado = true;
			esperaSeguidoras.signalAll();
			esperaAbandonoConvoy.await();
			System.out.println("** Furgoneta " + id + " lider abandona el convoy **");
		} finally {
			l.unlock();
		}

	}

	/**
	 * Las furgonetas seguidoras hasta que la lider avisa de que han llegado al
	 * destino
	 * y abandonan el convoy
	 * 
	 * @throws InterruptedException
	 **/
	public void seguirLider(int id) throws InterruptedException {
		try {
			l.lock();
			while (!terminado) {
				esperaSeguidoras.await();
			}
			System.out.println("Furgoneta " + id + " abandona el convoy");
			numFurgonetas--;

			if (numFurgonetas == 1) {
				esperaAbandonoConvoy.signal();
			}
		} finally {
			l.unlock();
		}
	}

	/**
	 * Programa principal. No modificar
	 **/
	public static void main(String[] args) {
		final int NUM_FURGO = 10;
		Convoy c = new Convoy(NUM_FURGO);
		Furgoneta flota[] = new Furgoneta[NUM_FURGO];

		for (int i = 0; i < NUM_FURGO; i++)
			flota[i] = new Furgoneta(i, c);

		for (int i = 0; i < NUM_FURGO; i++)
			flota[i].start();
	}

}
