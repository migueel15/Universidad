/**
 * @author Data Structures. ETSI Informática. UMA.
 */

import dataStructures.graph.WeightedGraph;
import dataStructures.graph.DictionaryWeightedGraph;

public class KruskalTest {

    public static void main(String[] args) {
        WeightedGraph<String, Integer> g1 = new DictionaryWeightedGraph<>();
        g1.addVertex("a");
        g1.addVertex("b");
        g1.addVertex("c");
        g1.addVertex("d");
        g1.addVertex("e");

        g1.addEdge("a", "b", 3);
        g1.addEdge("a", "d", 7);
        g1.addEdge("b", "c", 4);
        g1.addEdge("b", "d", 2);
        g1.addEdge("c", "d", 5);
        g1.addEdge("c", "e", 6);
        g1.addEdge("d", "e", 4);

        test(g1, "g1");


        WeightedGraph<String, Integer> g2 = new DictionaryWeightedGraph<>();
        g2.addVertex("a");
        g2.addVertex("b");
        g2.addVertex("c");
        g2.addVertex("d");
        g2.addVertex("e");
        g2.addVertex("f");
        g2.addVertex("g");

        g2.addEdge("a", "b", 7);
        g2.addEdge("a", "d", 5);
        g2.addEdge("b", "c", 8);
        g2.addEdge("b", "d", 9);
        g2.addEdge("b", "e", 7);
        g2.addEdge("c", "e", 5);
        g2.addEdge("d", "e", 15);
        g2.addEdge("d", "f", 6);
        g2.addEdge("e", "f", 8);
        g2.addEdge("e", "g", 9);
        g2.addEdge("f", "g", 11);

        test(g2, "g2");
    }


    static <V, W> void test(WeightedGraph<V, W> g, String name) {
        System.out.println(name + ": " + g.numVertices() + " vertices y " + g.numEdges() + " aristas");
        System.out.println(g);
        System.out.println("kruskal: " + Kruskal.kruskal(g));
        System.out.println();
    }

}

  /* Salida esperada:

  g1: 5 vertices y 7 aristas
  DictionaryWeightedGraph(vertices=(a, b, c, d, e), edges=(WE(b,d,2), WE(b,c,4), WE(a,d,7), WE(c,d,5), WE(a,b,3), WE(d,e,4), WE(c,e,6)))
  kruskal: HashSet(WE(b,d,2),WE(b,c,4),WE(a,b,3),WE(d,e,4))

  g2: 7 vertices y 11 aristas
  DictionaryWeightedGraph(vertices=(a, b, c, d, e, f, g), edges=(WE(a,b,7), WE(f,g,11), WE(d,f,6), WE(a,d,5), WE(e,f,8), WE(b,c,8), WE(c,e,5), WE(b,e,7), WE(e,g,9), WE(b,d,9), WE(d,e,15)))
  kruskal: HashSet(WE(a,b,7),WE(d,f,6),WE(a,d,5),WE(c,e,5),WE(b,e,7),WE(e,g,9))

  */

