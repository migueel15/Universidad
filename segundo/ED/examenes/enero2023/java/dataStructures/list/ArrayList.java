/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * List implementation using an array
 */

package dataStructures.list;

import dataStructures.stack.ArrayStack;

import java.util.*;

/**
 * List implemented using an array of elements.
 * Size of array (capacity) is automatically 
 * increased when it runs out of capacity.
 * @param <T> Type of elements in list.
 */
public class ArrayList<T> implements List<T> {
	protected T[] elements;
	protected int size;

	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	/**
	 * Creates an empty list. Initial capacity is {@code initialCapacity} elements.
   * Capacity is automatically increased when needed.
	 * @param initialCapacity Initial capacity.
	 * @throws IllegalArgumentException if initial capacity is less than 1.
	 * <p>Time complexity: O(1)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList(int initialCapacity) {
		if(initialCapacity <= 0) {
			throw new IllegalArgumentException("initial capacity must be greater than 0");
		}
		elements = (T[]) new Object[initialCapacity];
		size = 0;
	}

	/**
	 * Creates an empty list with default initial capacity.
   * Capacity is automatically increased when needed.
	 * <p>Time complexity: O(1)
	 */
	public ArrayList() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public int size() {
		return size;
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	private void validateIndex(int i) {
		if (i < 0 || i >= size())
			throw new ListException("Invalid position " + i);
	}

	private void ensureCapacity() {
		if (size >= elements.length) {
			elements = Arrays.copyOf(elements, 2*elements.length);
		}
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws ListException {@inheritDoc} 
	 */
	public T get(int i) {
		validateIndex(i);
		return elements[i];
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws ListException {@inheritDoc} 
	 */
	public void set(int i, T x) {
		validateIndex(i);
		elements[i] = x;
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(n)
	 * @throws ListException {@inheritDoc} 
	 */
	public void insert(int i, T x) {
		ensureCapacity();
		if (i != size) {
			validateIndex(i);
			for (int pos = size; pos > i; pos--) 
				elements[pos] = elements[pos - 1];
		}
		elements[i] = x;
		size++;
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: mostly O(1). O(n) when stack capacity has to be increased.
	 */
	public void append(T x) {
		ensureCapacity();
		elements[size] = x;
		size++;		
	}

	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(n) 
	 */
	public void prepend(T x) {
		ensureCapacity();
		for (int pos = size; pos > 0; pos--) 
			elements[pos] = elements[pos - 1];
		elements[0] = x;
		size++;		
	}
	
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(n)
	 * @throws ListException {@inheritDoc} 
	 */
	public void remove(int i) {
		validateIndex(i);
		for (int pos = i; pos < size - 1; pos++) 
			elements[pos] = elements[pos + 1];
		size--;
	}
  
  
	/** 
	 * Iterator over elements in list.
	 * Note that {@code remove} method is not supported. Note also 
	 * that list structure should not be modified during iteration as 
	 * iterator state may become inconsistent.
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return new ArrayListIterator();
	}

	private class ArrayListIterator implements Iterator<T> {
		int current;

		public ArrayListIterator(){
			current = 0;
		}
		
		public boolean hasNext() {
			return current < size;
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();

			T x = elements[current];
			current = current + 1;
			return x;
		}	
	}  
	
	/** 
	 * Returns representation of list as a String.
	 */
	@Override
	public String toString() {
		String className = getClass().getSimpleName();
		StringJoiner sj = new StringJoiner(", ", className+"(", ")");
		for(int i=0; i<size; i++) {
			sj.add(elements[i].toString());
		}
		return sj.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ArrayList<?> that = (ArrayList<?>) o;
		if(size != that.size) {
			return false;
		}
		for(int i=0; i<size; i++) {
			if(!Objects.equals(elements[i], that.elements[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + Integer.hashCode(size);
		for(int i=0; i<size; i++) {
			result = 31 * result + Objects.hashCode(elements[i]);
		}
		return result;
	}
}
