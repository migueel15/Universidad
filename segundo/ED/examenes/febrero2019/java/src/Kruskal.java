
/**----------------------------------------------
 * -- Estructuras de Datos.  2018/19
 * -- 2º Curso del Grado en Ingeniería [Informática | del Software | de Computadores].
 * -- Escuela Técnica Superior de Ingeniería en Informática. UMA
 * --
 * -- Examen 4 de febrero de 2019
 * --
 * -- ALUMNO/NAME: Miguel Angel Dorado Maldonado
 * -- GRADO/STUDIES: Software A
 * -- NÚM. MÁQUINA/MACHINE NUMBER: xxx
 * --
 * ----------------------------------------------
 */

import dataStructures.graph.WeightedGraph;
import dataStructures.graph.WeightedGraph.WeightedEdge;

import dataStructures.dictionary.Dictionary;
import dataStructures.dictionary.HashDictionary;
import dataStructures.priorityQueue.PriorityQueue;
import dataStructures.priorityQueue.LinkedPriorityQueue;
import dataStructures.set.Set;
import dataStructures.set.HashSet;

public class Kruskal {
  public static <V, W> Set<WeightedEdge<V, W>> kruskal(WeightedGraph<V, W> g) {
    // create prority queue
    PriorityQueue<WeightedEdge<V, W>> PQ = new LinkedPriorityQueue<>();
    for (WeightedEdge<V, W> edge : g.edges()) {
      PQ.enqueue(edge);
    }

    // create DICT
    Dictionary<V, V> DICT = new HashDictionary<>();
    for (V vertice : g.vertices()) {
      DICT.insert(vertice, vertice);
    }

    // set resultado
    Set<WeightedEdge<V, W>> T = new HashSet<>();

    while (!PQ.isEmpty()) {
      WeightedEdge<V, W> arista = PQ.first();
      PQ.dequeue();

      if (!representante(arista.source(), DICT).equals(representante(arista.destination(), DICT))) {
        DICT.insert(representante(arista.destination(), DICT), arista.source());
        T.insert(arista);
      }
    }

    return T;
  }

  private static <V> V representante(V v, Dictionary<V, V> DICT) {
    V actual = v;
    while (!actual.equals(DICT.valueOf(actual))) {
      actual = DICT.valueOf(actual);
    }
    return actual;
  }

  // Sólo para evaluación continua / only for part time students
  public static <V, W> Set<Set<WeightedEdge<V, W>>> kruskals(WeightedGraph<V, W> g) {

    // COMPLETAR

    return null;
  }
}
