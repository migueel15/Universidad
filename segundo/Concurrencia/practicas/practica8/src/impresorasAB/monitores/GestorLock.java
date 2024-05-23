package impresorasAB.monitores;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.*;

/**
 * Solución al problema del gestor de impresoras utilizando condiciones.
 * La condición colaGeneral es utilizada por todas las hebras cuando piden
 * una impresora y no hay del tipo que piden.
 *
 * Las colas colaA y colaB son utilizadas en exclusiva por las hebras de tipo A y B, respectivamente.
 */
public class GestorLock implements Gestor {
	private final Lock l = new ReentrantLock(true);
	private final Condition cGeneral = l.newCondition();
	private final Condition cExcA = l.newCondition();
	private final Condition cExcB = l.newCondition();

	private final Queue<Integer> colaA = new LinkedList<>();
	private final Queue<Integer> colaB = new LinkedList<>();

	private int numImpA, numImpB; // número de impresoras de cada tipo

	public GestorLock(int numA, int numB) {
		numImpA = numA;
		numImpB = numB;
	}

	public void qImpresoraA(int id) throws InterruptedException {
		l.lock();
		try {
			System.out.println("Hebra " + id + " pide impresora A");
			while (numImpA == 0) {
				System.out.println("Hebra " + id + " espera impresora A");
				cGeneral.await();
			}
			colaA.add(id);
			while (numImpA == 0 || (!colaA.isEmpty() && colaA.peek() != id)) {
				System.out.println("Hebra " + id + " espera su turno");
				cExcA.await();
			}
			colaA.poll();
			numImpA--;
			System.out.println("Hebra " + id + " imprime en A. Impresoras A disponibles: " + numImpA);
		} finally {
			l.unlock();
		}
	}

	public void qImpresoraB(int id) throws InterruptedException {
		l.lock();
		try {
			System.out.println("Hebra " + id + " pide impresora B");
			while (numImpB == 0) {
				System.out.println("Hebra " + id + " espera impresora B");
				cGeneral.await();
			}
			colaB.add(id);
			while (numImpB == 0 || (!colaB.isEmpty() && colaB.peek() != id)) {
				System.out.println("Hebra " + id + " espera su turno");
				cExcB.await();
			}
			colaB.poll();
			numImpB--;
			System.out.println("Hebra " + id + " imprime en B. Impresoras B disponibles: " + numImpB);
		} finally {
			l.unlock();
		}
	}

	public char qImpresoraAB(int id) throws InterruptedException {
		l.lock();
		try {
			while (numImpA == 0 && numImpB == 0) {
				System.out.println("Hebra " + id + " espera impresora de cualquier tipo");
				cGeneral.await();
			}
			if (numImpA > 0) {
				numImpA--;
				System.out.println("Hebra " + id + " imprime en A. Impresoras A disponibles: " + numImpA);
				return 'A';
			} else {
				numImpB--;
				System.out.println("Hebra " + id + " imprime en B. Impresoras B disponibles: " + numImpB);
				return 'B';
			}
		} finally {
			l.unlock();
		}
	}

	public void dImpresora(char tipo) {
		l.lock();
		try {
			if (tipo == 'A') {
				numImpA++;
				System.out.println("Hebra devuelve impresora A. Impresoras A disponibles: " + numImpA);
				if (!colaA.isEmpty()) {
					cExcA.signal();
				} else {
					cGeneral.signal();
				}
			} else if (tipo == 'B') {
				numImpB++;
				System.out.println("Hebra devuelve impresora B. Impresoras B disponibles: " + numImpB);
				if (!colaB.isEmpty()) {
					cExcB.signal();
				} else {
					cGeneral.signal();
				}
			}
		} finally {
			l.unlock();
		}
	}
}
