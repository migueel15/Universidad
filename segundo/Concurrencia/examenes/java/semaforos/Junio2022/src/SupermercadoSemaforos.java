package Supermercado;

import java.util.concurrent.Semaphore;

public class SupermercadoSemaforos implements Supermercado {

	private Cajero permanente;
	private int numClientes;

	private Semaphore mutex = new Semaphore(1);

	private Semaphore controlSupermercado = new Semaphore(1);
	private boolean supermercadoAbierto = true;

	public SupermercadoSemaforos() throws InterruptedException {
		permanente = new Cajero(this, true); // crea el primer cajero, el permanente
		permanente.start();
	}

	@Override
	public void fin() throws InterruptedException {
		supermercadoAbierto = false;
		controlSupermercado.acquire();
	}

	@Override
	public void nuevoCliente(int id) throws InterruptedException {
		controlSupermercado.acquire();
		mutex.acquire();
		numClientes++;
		System.out.println("Llega cliente " + id + ". Hay " + numClientes);
		if (numClientes > 3 * Cajero.numCajeros()) {
			new Cajero(this, false).start();
			System.out.println("El nuevo cajero " + Cajero.numCajeros() + " " +
					"comienza a servir a un cliente.");
		}
		mutex.release();
		controlSupermercado.release();
	}

	@Override
	public boolean permanenteAtiendeCliente(int id) throws InterruptedException {
		int clientesActuales = 0;
		mutex.acquire();
		if (numClientes > 0) {
			numClientes--;
			clientesActuales = numClientes;
			System.out.println("Cajero permanente atiende a cliente " + id + ". " +
					"Quedan " + numClientes);
		}
		mutex.release();

		return supermercadoAbierto || clientesActuales != 0;
	}

	@Override
	public boolean ocasionalAtiendeCliente(int id) throws InterruptedException {
		mutex.acquire();
		if (numClientes > 0) {
			numClientes--;
		}
		System.out.println("Cajero ocasional atiende a cliente " + id + ". " +
				"Quedan " + numClientes);
		mutex.release();

		return supermercadoAbierto || numClientes != 0;
	}

}
