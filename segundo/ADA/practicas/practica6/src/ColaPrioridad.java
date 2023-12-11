import java.util.Iterator;
import java.util.PriorityQueue;

public class ColaPrioridad {
	private PriorityQueue<Estado> cola;
	
	public ColaPrioridad() {
		cola = new PriorityQueue<Estado>();
	}
	/**
	 * Indica si la cola no tiene elementos
	 */
	public boolean estaVacia() {
		return cola.size() == 0;
	}
	
	/**
	 * Inserta el estado dado en la cola de prioridad
	 */
	public void insertar(Estado e) {
		cola.add(e);
	}
	
	/**
	 * Extrae la cabeza de la cola de prioridad
	 */
	public Estado extraer() {
		Estado res = null;
		if (cola.size()>0) {
			res = cola.poll();
		}
		return res;
	}
	
	/**
	 * Elimina el estado dado de la cola de prioridad
	 */
	public void eliminar(Estado e) {
		cola.remove(e);
	}
	
	/**
	 * Elimina todos los estados de la cola cuyo valor de cota no sea menor
	 * que el valor dado por parámetro
	 */
	public void eliminar(int valorCota) {
		Iterator<Estado> it = cola.iterator();
		while (it.hasNext()) {
			if (it.next().cota() >= valorCota) {
				it.remove();
			}
		}
	}
}
