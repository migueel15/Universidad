package Supermercado;

import java.util.Random;
import java.util.concurrent.atomic.*;
public class Cajero extends Thread {
	private static Random rnd = new Random();
	private static boolean algunoCreado = false;
	private static AtomicInteger numCajeros = new AtomicInteger(0);
	private static AtomicInteger currentId = new AtomicInteger(0);
	private boolean soyPermanente;
	private Supermercado mkt;
	private int id;

	public Cajero(Supermercado mkt, boolean permanente) throws InterruptedException {
		if (algunoCreado && permanente)
			throw new RuntimeException("Solo el primer cajero puede ser permanente");
		if (!algunoCreado && !permanente)
			throw new RuntimeException("El primer cajero tiene que ser permanente");
		algunoCreado = true;
		soyPermanente = permanente;
		this.mkt = mkt;
		this.id = currentId.getAndIncrement();
		numCajeros.incrementAndGet();
	}

    @Override
	public void run() {
		try {
			if (! soyPermanente) {
				System.out.println("El nuevo cajero "+id+" comienza a servir a un cliente.");
				sleep(500+rnd.nextInt(400));
			}
			if (soyPermanente) {
				while (mkt.permanenteAtiendeCliente(id)) {
					sleep(500+rnd.nextInt(400));
				}
			} else {
				while (mkt.ocasionalAtiendeCliente(id)) {
					sleep(500+rnd.nextInt(400));
				}
			}
		} catch (InterruptedException e) { e.printStackTrace(); }
		//System.out.println("Cajero "+id+" termina.");
		numCajeros.decrementAndGet();
    }

	public static int numCajeros() {
		return numCajeros.get();
	}

	public int id() {
		return id;
	}
}
