import java.util.HashSet;
import java.util.Set;


public class Cobertura {

	private Grafo grafo;

	public Cobertura(Grafo g) {
		grafo = g;
	}

	public Set<Integer> getConjuntoCobertura() {
		// buscamos el nodo con mayor número de conexiones. Lo quitamos del grafo. Volvemos a buscar el mejor dentro de los restantes hasta que no queden aristas
		Set<Integer> resultado = new HashSet<>();

		while(grafo.numAristas() > 0){
			int max = 0;
			for(Integer node : grafo.nodosConectados()){
				if(grafo.grado(node) > grafo.grado(max)){
					max = node;
				}
			}
			resultado.add(max);
			for(Integer nCon : grafo.sucesores(max)){
				grafo.removeArista(max, nCon);
			}
		}

		return resultado;
	}
}
