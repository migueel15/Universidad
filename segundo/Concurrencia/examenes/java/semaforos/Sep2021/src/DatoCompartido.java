import java.util.concurrent.Semaphore;

public class DatoCompartido {
	private int dato;
	private int nProcesadores;
	private int procPend;

	private Semaphore mutex = new Semaphore(1);
	private Semaphore controlGenerador = new Semaphore(1);
	private Semaphore finProceso = new Semaphore(0);
	private Semaphore[] procesadores;
	
	/* Recibe como par�metro el n�mero de procesadores que tienen que manipular 
	 * cada dato generado. Debe ser un n�mero mayor que 0. 
	 */
	public DatoCompartido(int nProcesadores) {
		this.nProcesadores = nProcesadores;
		procesadores = new Semaphore[nProcesadores];
		if(nProcesadores <= 0) {
			throw new IllegalArgumentException("El numero de procesadores debe ser mayor que 0");
		}
		for(int i = 0; i < nProcesadores; i++){
			procesadores[i] = new Semaphore(0);
		}
	}
	
	public int generaDato(int d) throws InterruptedException {
		controlGenerador.acquire();
		mutex.acquire();
		dato = d;
		System.out.println("Dato a procesar: " + dato);
		procPend = nProcesadores;
		System.out.println("Numero de procesadores pendientes: " + procPend);
		// for each
		for (int i = 0; i < nProcesadores; i++) {
			procesadores[i].release();
		}
		mutex.release();

		finProceso.acquire();

		return dato;
	}

	public int leeDato(int id) throws InterruptedException {
		procesadores[id].acquire();
		mutex.acquire();
		System.out.println("	Procesador " + id + " ha leido el dato: " + dato);

		return dato;
	}
	
	/*  El Procesador con identificador id almacena en el recurso compartido el resultado 
	 *  de haber procesado el dato. Una vez hecho esto actuara de una de las dos formas siguientes: 
	 *  
	 *  (1) Si aun hay procesadores esperando a procesar el dato los avisara, 
	 *  (2) Si el era el ultimo procesador avisara al Generador de que han terminado. 
	 * 
	 */
	public void actualizaDato(int id, int datoActualizado) throws InterruptedException {
		dato = datoActualizado;
		procPend--;
		if(procPend == 0) {
			controlGenerador.release();
			finProceso.release();
		}

		System.out.println("	Procesador " + id + " ha procesado el dato. Nuevo dato: " + dato);

		System.out.println("Numero de procesadores pendientes: " + procPend);
		mutex.release();

	}
}
