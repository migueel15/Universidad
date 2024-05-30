package locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Curso {

	private final int MAX_ALUMNOS_INI = 10;
	private final int ALUMNOS_AV = 3;

	private int alumnosIniciacion = 0;
	private int alumnosEsperandoProyecto = 0;
	private int alumnosTerminadoProyecto = 0;
	private boolean plazasLibres = true;
	private boolean esperaIniciar = true;
	private boolean esperaTerminar = true;

	private Lock l = new ReentrantLock(true);
	private Condition esperaCursarIniciacion = l.newCondition();
	private Condition esperaCursarAvanzado = l.newCondition();
	private Condition esperaIniciarProyecto = l.newCondition();
	private Condition esperaTerminarProyecto = l.newCondition();

	public void esperaPlazaIniciacion(int id) throws InterruptedException {
		try {
			l.lock();
			while (alumnosIniciacion >= MAX_ALUMNOS_INI) {
				esperaCursarIniciacion.await();
			}
			alumnosIniciacion++;
			System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte principiantes");
		} finally {
			l.unlock();
		}
	}

	public void finIniciacion(int id) throws InterruptedException {
		// El alumno termina esta parte de forma individual, no tiene que esperar por
		// los demás
		try {
			l.lock();
			System.out.println("PARTE INICIACION: Alumno " + id + " termina parte principiantes");
			alumnosIniciacion--;
			if (alumnosIniciacion == MAX_ALUMNOS_INI - 1) {
				esperaCursarIniciacion.signalAll();
			}
		} finally {
			l.unlock();
		}
	}

	public void esperaPlazaAvanzado(int id) throws InterruptedException {
		// Espera mientras no haya plazas libres
		try {
			l.lock();
			while (!plazasLibres) {
				// System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya
				// plaza");
				esperaCursarAvanzado.await();
			}

			alumnosEsperandoProyecto++;
			if (alumnosEsperandoProyecto == ALUMNOS_AV) {
				plazasLibres = false;
				esperaIniciar = false;
				esperaTerminar = true;
				alumnosEsperandoProyecto = 0;
				esperaIniciarProyecto.signalAll();
			}

			// System.out.println("Hay " + alumnosEsperandoProyecto + " alumno(s)
			// esperando");
			System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");

			while (esperaIniciar) {
				esperaIniciarProyecto.await();
			}

			System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");
		} finally {
			l.unlock();
		}
	}

	public void finAvanzado(int id) throws InterruptedException {
		// Espera a que los alumnos terminen su parte avanzada
		try {
			l.lock();
			System.out.println("PARTE AVANZADA: Alumno " + id + " termina su parte del proyecto. Espera al resto");

			alumnosTerminadoProyecto++;
			if (alumnosTerminadoProyecto == ALUMNOS_AV) {
				System.out.println("PARTE AVANZADA: Los " + ALUMNOS_AV + " alumnos han terminado su proyecto");
				esperaIniciar = true;
				esperaTerminar = false;
				alumnosTerminadoProyecto = 0;
				esperaTerminarProyecto.signalAll();

				plazasLibres = true;
				esperaCursarAvanzado.signalAll();
			}

			while (esperaTerminar) {
				esperaTerminarProyecto.await();
			}
		} finally {
			l.unlock();
		}
	}
}
