/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Sets implemented as AVL trees
 */

package dataStructures.set;

import java.util.Iterator;

import dataStructures.searchTree.AVL;
import dataStructures.searchTree.SearchTree;

/**
 * Sets implemented using AVL trees. Note that elements should define an
 * order relation ({@link java.lang.Comparable}).
 *
 * @param <T> Type of elements in set.
 */
public class AVLSet<T extends Comparable<? super T>> implements Set<T>{
	
	private static class Nothing{};
	private Nothing nothing = new Nothing();
	
	private SearchTree<T,Nothing> tree;	
	
	/**
	 * Creates a new empty set.
	 * <p>Time complexity: O(1)
	 */
	public AVLSet() {
		tree = new AVL<>();
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 */
	public void delete(T elem) {
		tree.delete(elem);		
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 */
	public void insert(T elem) {
		tree.insert(elem,nothing);
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 */
	public boolean isElem(T elem) {
		return tree.isElem(elem);
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public int size() {
		return tree.size();
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public boolean isEmpty() {
		return tree.isEmpty();
	}

	/** 
	 * Iterator over elements in set.
	 * Note that {@code remove} method is not supported. Note also 
	 * that set should not be modified during iteration as 
	 * iterator state may become inconsistent.
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return tree.inOrder().iterator();
	}
	
	/** 
	 * Returns representation of set as a String.
	 */
	public String toString() {
    String className = getClass().getSimpleName();
		String s = className+"(";
		Iterator<T> it = this.iterator();
		while(it.hasNext()) 
			s += it.next() + (it.hasNext() ? "," : "");
		s += ")";
		return s;
	}
}
