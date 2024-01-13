/**
 * @author Data Structures. ETSI Informática. UMA.
 */

package dataStructures.graph;

import dataStructures.set.Set;
import dataStructures.tuple.Tuple2;

/**
 * Interface for an undirected weighted graph
 *
 * @param <V> Type for vertices in graph
 * @param <W> Type for weights in edges
 */
public interface WeightedGraph<V, W> {

    /**
     * Interface for a weighted edge in a graph
     *
     * @param <V> Type for vertices in edge
     * @param <W> Type for weight in edge
     */
    interface WeightedEdge<V, W> extends Comparable<WeightedEdge<V, W>> {
        /**
         * One vertex in edge
         */
        V source();

        /**
         * The other vertex in edge
         */
        V destination();

        /**
         * Weight of the edge
         */
        W weight();
    }


    /**
     * Inserts a new vertex in graph.
     *
     * @param v Vertex to be inserted
     */
    void addVertex(V v);


    /**
     * Adds a new weighted edge to graph or replaces a previous edge connecting same vertices
     *
     * @param v1 one the vertices in edge
     * @param v2 the other vertex in edge
     * @param w  the weight of the edge
     * @throws GraphException if one of the nodes hasn't been inserted previously in graph
     */
    void addEdge(V v1, V v2, W w);


    /**
     * Successors of a vertex with corresponding weights
     *
     * @param v vertex for which we want to obtain its successors
     * @return Successors of a vertex v with corresponding weights
     * @throws GraphException is vertex is not in graph
     */
    Set<Tuple2<V,W>> successors(V v);


    /**
     * Set including all vertices in this graph
     *
     * @return Set including all vertices in this graph
     */
    Set<V> vertices();

    /**
     * Set including all weighted edges in this graph
     *
     * @return Set including all weighted edges in this graph
     */
    Set<WeightedEdge<V, W>> edges();


    /**
     * true if graph has no vertices
     */
    boolean isEmpty();

    /**
     * Total number of vertices in graph
     *
     * @return Total number of vertices in graph
     */
    int numVertices();

    /**
     * Total number of edges in graph
     *
     * @return Total number of edges in graph
     */
    int numEdges();
}
