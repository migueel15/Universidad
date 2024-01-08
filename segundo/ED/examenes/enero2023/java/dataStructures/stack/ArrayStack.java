/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Stack implementation using an array
 */
 
package dataStructures.stack;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Stack implemented using an array of elements. Size of array
 * (capacity) is automatically increased when it runs out of capacity.
 * @param <T> Type of elements in stack. 
 */
public class ArrayStack<T> implements Stack<T> {
	protected T[] elements;
	protected int nextFree;
	
	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/**
	 * Creates an empty stack. Initial capacity is {@code initialCapacity} elements.
   * Capacity is automatically increased when needed.
	 * @param initialCapacity Initial capacity.
	 * @throws IllegalArgumentException if initial capacity is less than 1.
	 * <p>Time complexity: O(1)
	 */
	@SuppressWarnings("unchecked")
	public ArrayStack(int initialCapacity) {
		if(initialCapacity <= 0) {
			throw new IllegalArgumentException("initial capacity must be greater than 0");
		}
		elements = (T[]) new Object[initialCapacity];
		nextFree = 0;
	}

	/**
	 * Creates an empty stack with default initial capacity.
   * Capacity is automatically increased when needed.
	 * <p>Time complexity: O(1)
	 */
	public ArrayStack() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public boolean isEmpty() {
		return nextFree == 0;
	}

	private void ensureCapacity() {
    if (nextFree >= elements.length) {
    	elements = Arrays.copyOf(elements, 2*elements.length);
    }	
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: mostly O(1). O(n) when stack capacity has to be increased. 
	 */
	public void push(T x) {
		ensureCapacity();
		elements[nextFree] = x;
		nextFree++;
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws EmptyStackException {@inheritDoc} 
	 */
	public T top() {
		if (isEmpty()){
			throw new EmptyStackException("top on empty stack");
		}
		else
			return elements[nextFree-1];
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws EmptyStackException {@inheritDoc} 
	 */
	public void pop() {
		if (isEmpty()){
			throw new EmptyStackException("pop on empty stack");
		}
		else
			nextFree--;
	}
	
	/** 
	 * Returns representation of stack as a String.
	 */
	@Override
	public String toString() {
    String className = getClass().getSimpleName();
		StringJoiner sj = new StringJoiner(", ", className+"(", ")");
		for(int i=nextFree-1; i>=0; i--)  {
			sj.add(elements[i].toString());
		}
		return sj.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ArrayStack<?> that = (ArrayStack<?>) o;
		if(nextFree != that.nextFree) {
			return false;
		}
		for(int i=0; i<nextFree; i++) {
			if(!Objects.equals(elements[i], that.elements[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + Integer.hashCode(nextFree);
		for(int i=0; i<nextFree; i++) {
			result = 31 * result + Objects.hashCode(elements[i]);
		}
		return result;
	}
}
