import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Grafo no dirigido donde no hay lazos, es decir, un nodo no está conectado
 * consigo mismo
 **/

public class Grafo {
  private Set<Integer> nodos;
  private Map<Integer, Set<Integer>> sucesores; // nodos con su conjunto de vértices conectados
  private int numAristas;

  public Grafo(int[][] adyacencia) {
    numAristas = 0;
    nodos = new HashSet<>();
    sucesores = new HashMap<Integer, Set<Integer>>();
    for (int i = 0; i < adyacencia.length; i++) {
      nodos.add(i);
      for (int j = i + 1; j < adyacencia.length; j++) {
        if (adyacencia[i][j] == 1) {
          addArista(i, j);
        }
      }
    }

  }

  /* Constructor de copia */
  public Grafo(Grafo otroGrafo) {
    nodos = new HashSet<Integer>(otroGrafo.nodos);
    numAristas = otroGrafo.numAristas;
    sucesores = new HashMap<Integer, Set<Integer>>(otroGrafo.sucesores);
  }

  /* Añade la arista i-j al grafo */
  public void addArista(int i, int j) {
    numAristas++;
    addEnlace(i, j);
    addEnlace(j, i);
  }

  private void addEnlace(int i, int j) {
    Set<Integer> conectados = sucesores.get(i);
    if (conectados == null) {
      conectados = new HashSet<Integer>();
      sucesores.put(i, conectados);
    }
    conectados.add(j);
  }

  /* Elimina la arista i-j del grafo, si existe */
  public void removeArista(Integer i, Integer j) {
    Set<Integer> conectados = sucesores.get(i);
    if (conectados != null && conectados.contains(j)) {
      numAristas--;
      removeEnlace(i, j);
      removeEnlace(j, i);
    }
  }

  private void removeEnlace(Integer i, Integer j) {
    sucesores.get(i).remove(j);
    if (sucesores.get(i).size() == 0) {
      sucesores.remove(i);
    }
  }

  /* Devuelve el número de aristas del grafo */
  public int numAristas() {
    return numAristas;
  }

  /* Devuelve el grado del nodo i */
  public int grado(Integer i) {
    Set<Integer> aux = sucesores.get(i);
    return (aux == null) ? 0 : aux.size();
  }

  /* Devuelve los nodos conectados al nodo i */
  public Set<Integer> sucesores(Integer i) {
    return new HashSet<Integer>(sucesores.get(i));
  }

  /* Devuelve todos los nodos con conexiones del grafo */
  public Set<Integer> nodosConectados() {
    return new HashSet<Integer>(sucesores.keySet());
  }

  /* Devuelve todos los nodos del grafo */
  public Set<Integer> nodos() {
    return nodos;
  }
}
