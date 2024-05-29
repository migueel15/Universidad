package tallermecanico;

import java.util.concurrent.locks.Condition;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Taller {

	private Lock l = new ReentrantLock();
	private Condition esperaMecanico = l.newCondition();
	private int personaDentro = 0;
	private Condition colaEsperaFinRevision = l.newCondition();
	private boolean hayQueFacturar = false;
	private Condition colaEntradaTaller = l.newCondition();
	private Condition colaFactura = l.newCondition();

	// mecanico espera a que le avise un nuevo cliente
	public void esperaParaRevisar() throws InterruptedException {
		try {
			l.lock();
			while (personaDentro == 0) {
				esperaMecanico.await();
			}
			System.out.println("Mecanico se despierta: Coche en plataforma");
		} finally {
			l.unlock();
		}

	}

	// mecanico termina rivision y avisa a administrativo
	public void finRevision() {
		try {
			l.lock();
			System.out.println("Fin de revision");
			personaDentro = 0;
			hayQueFacturar = true;
			colaEsperaFinRevision.signal();
		} finally {
			l.unlock();
		}

	}

	// cliente espera factura de administrativo
	public void esperaParaFacturar() throws InterruptedException {
		try {
			l.lock();
			while (!hayQueFacturar) {
				colaEsperaFinRevision.await();
			}
			hayQueFacturar = false;
			System.out.println("Haciendo factura");
		} finally {
			l.unlock();
		}

	}

	// administrativo avisa a cliente para recoger su coche
	public void finFactura() {
		try {
			l.lock();
			System.out.println("Factura terminada");
			colaFactura.signal();
		} finally {
			l.unlock();
		}

	}

	// espera para poder entrar
	public void revisarCoche(int id) throws InterruptedException {
		try {
			l.lock();
			while (personaDentro != 0) {
				colaEntradaTaller.await();
			}
			personaDentro = 1;
			System.out.println("El cliente " + id + " sube el coche a la plataforma");
			esperaMecanico.signal();
			colaFactura.await();
			System.out.println("El cliente " + id + " recoge factura y coche");
			colaEntradaTaller.signal();

		} finally {
			l.unlock();
		}

	}
}
