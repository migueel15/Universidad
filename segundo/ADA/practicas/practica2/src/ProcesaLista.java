import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcesaLista extends Metodo{
	private List<Integer> lista;
	
	public ProcesaLista() {
		super();
		lista = null;
		
	}
	public void setLista(List<Integer> l) {
		lista = l;
	}
		
	public List<Integer> getLista() {
		return lista;
	}
	
	/**
	 * Procesamos todos los elementos de la lista lista.
	 * return El número de elementos procesados en realidad.
	 */
	@Override
	public int codigo(int n) {
		procesaLista(lista);
		return n>lista.size()?n:lista.size();
	}
	
	private void procesaLista(List<Integer> lista) {
		List<Integer> elUnicos = new ArrayList<>();
		if(lista.size() > 0) {
			elUnicos.add(lista.get(0));
			for (int i = 1; i < lista.size(); i++) {
				if(lista.get(i) % 2 != 0){
					if (lista.get(i) != lista.get(i - 1)) {
						elUnicos.add(lista.get(i));
					}
				}else {
					if (lista.get(i) != lista.get(i - 1)) {
						elUnicos.add(lista.get(i));
						elUnicos.add(lista.get(i));
					}
				}

			}
			lista.clear();
			lista.addAll(elUnicos);
		}
	}
}
