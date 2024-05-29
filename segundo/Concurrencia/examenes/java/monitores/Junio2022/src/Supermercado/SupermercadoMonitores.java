package Supermercado;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SupermercadoMonitores implements Supermercado {

	private boolean superAbierto = true;
	private Cajero permanente;

	Lock l = new ReentrantLock();

	private int numClientes = 0;
	private boolean permanenteOcupado = false;
	Condition esperaPermanente = l.newCondition();
	Condition esperaOcasionales = l.newCondition();
	Condition colaEnCaja = l.newCondition();

	public SupermercadoMonitores() throws InterruptedException {
		permanente = new Cajero(this, true); // crea el primer cajero, el permanente
		permanente.start();

		// TODO
	}

	@Override
	public void fin() throws InterruptedException {
		try {
			l.lock();
			System.out.println("Supermercado cerrado---------------------");
			superAbierto = false;
		} finally {
			l.unlock();
		}

	}

	@Override
	public void nuevoCliente(int id) throws InterruptedException {
		try {
			l.lock();
			if (superAbierto) {
				numClientes++;
				System.out.println("Cliente " + id + " entra al supermercado. Num clientes: " + numClientes);
				if (!permanenteOcupado) {
					permanenteOcupado = true;
					esperaPermanente.signal();
				} else {
					if (numClientes > (3 * Cajero.numCajeros())) {
						Cajero nuevoCajero = new Cajero(this, false);
						nuevoCajero.start();
					}
				}
				colaEnCaja.await();

			} else {
				System.out.println("Supermercado cerrado, cliente " + id + " se va");
			}

		} finally {
			l.unlock();
		}

	}

	@Override
	public boolean permanenteAtiendeCliente(int id) throws InterruptedException {
		try {
			l.lock();
			while (numClientes == 0 && superAbierto) {
				esperaPermanente.await();
			}

			if (numClientes == 0 && !superAbierto) {
				return false;
			} else {
				numClientes--;
				System.out.println("Cajero permanente atiende a cliente. Quedan " + numClientes + " clientes.");
				colaEnCaja.signal();
				permanenteOcupado = false;
				return true;
			}
		} finally {
			l.unlock();
		}
	}

	@Override
	public boolean ocasionalAtiendeCliente(int id) throws InterruptedException {
		try {
			l.lock();
			if (numClientes <= 0) {
				if (!superAbierto) {
					esperaPermanente.signal();
				}
				return false;
			} else {
				numClientes--;
				System.out.println("Cajero ocasional " + id + " atiende a cliente. Quedan " + numClientes + " clientes.");
				colaEnCaja.signal();
				return true;
			}
		} finally {
			l.unlock();
		}
	}

}
