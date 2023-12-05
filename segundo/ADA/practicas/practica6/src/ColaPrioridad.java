import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ColaPrioridad {
	private SortedSet<Estado> cola;
	
	public ColaPrioridad() {
		cola = new TreeSet<Estado>();
	}
	
	public boolean estaVacia() {
		return cola.size() == 0;
	}
	
	public void insertar(Estado e) {
		cola.add(e);
	}
	
	public Estado extraer() {
		Estado res = null;
		if (cola.size()>0) {
			res = cola.first();
			cola.remove(res);
		}
		return res;
	}
	
	public void eliminar(Estado e) {
		cola.remove(e);
	}
	
	public void eliminar(int valorCota) {
		Iterator<Estado> it = cola.iterator();
		while (it.hasNext()) {
			if (it.next().cota() >= valorCota) {
				it.remove();
			}
		}
	}
}
