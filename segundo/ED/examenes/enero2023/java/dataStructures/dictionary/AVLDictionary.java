/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Dictionaries implemented as AVL trees
 */

package dataStructures.dictionary;
import dataStructures.searchTree.AVL;
import dataStructures.searchTree.SearchTree;
import dataStructures.tuple.Tuple2;

import java.util.StringJoiner;


/**
 * Dictionaries (finite maps) associating different keys to values
 * implemented as AVL trees indexed by keys. Note that keys should define an
 * order relation ({@link java.lang.Comparable}).
 *
 * @param <K> Type of keys.
 * @param <V> Types of values.
 */
public class AVLDictionary<K extends Comparable<? super K>,V> implements Dictionary<K,V> {
	private final SearchTree<K,V> avl;
	
	/**
	 * Creates an empty dictionary.
	 * <p>Time complexity: O(1)
	 */
	public AVLDictionary() {
		avl = new AVL<>();
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public boolean isEmpty() {
		return avl.isEmpty();
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public int size() {
		return avl.size();
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 */
	public void insert(K k, V v) {
		avl.insert(k, v);
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 */
	public void delete(K k) {
		avl.delete(k);
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 */
	public V valueOf(K k) {
		return avl.search(k);
	}	
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 */
	public boolean isDefinedAt(K k) {
		return avl.isElem(k);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public Iterable<K> keys() {
		return avl.inOrder();
	}

	/** 
	 * {@inheritDoc}
	 */
	public Iterable<V> values() {
		return avl.values();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public Iterable<Tuple2<K,V>> keysValues() {
		return avl.keysValues();
	}
		
	/** 
	 * Returns representation of dictionary object as a String.
	 */
	public String toString() {
    String className = getClass().getSimpleName();
		StringJoiner sj = new StringJoiner(", ", className+"(", ")");
		for(Tuple2<K,V> t : keysValues()) {
			StringBuilder sb = new StringBuilder(t._1().toString()).append("->").append(t._2().toString());
			sj.add(sb);
		}
	  return sj.toString();
	}
}
