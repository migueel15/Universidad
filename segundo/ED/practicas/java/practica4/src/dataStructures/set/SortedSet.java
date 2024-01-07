/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Interface for sets.
 */

package dataStructures.set;

/**
 * Interface for sorted sets (collection of non-repeated elements)
 * for which iterator provides elements sorted in ascending order.
 *
 * @param <T> Type of elements in set.
 */
public interface SortedSet<T extends Comparable<? super T>> extends Set<T> {}
