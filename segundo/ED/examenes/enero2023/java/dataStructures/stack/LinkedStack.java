/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Stack implementation using a linked list
 */
 
package dataStructures.stack;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Stack implemented using a linked list.
 * @param <T> Type of elements in stack.
 */
public class LinkedStack<T> implements Stack<T> {
	static private class Node<E> {
		E elem;
		Node<E> next;

		public Node(E elem, Node<E> next) {
			this.elem = elem;
			this.next = next;
		}
	}
	
	private Node<T> top;
	
	/**
	 * Creates an empty stack.
	 * <p>Time complexity: O(1)
	 */
	public LinkedStack() {
		top = null;
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public boolean isEmpty() {
		return top == null;
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws EmptyStackException {@inheritDoc} 
	 */
	public T top() {
		if(isEmpty())
			throw new EmptyStackException("top on empty stack");
		else
			return top.elem;
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws EmptyStackException {@inheritDoc} 
	 */	
	public void pop() {
		if(isEmpty())
			throw new EmptyStackException("pop on empty stack");
		else
			top = top.next;	
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public void push(T x) {
		top = new Node<>(x, top);
	}
	
	/** 
	 * Returns representation of stack as a String.
	 */
	@Override
	public String toString() {
    String className = getClass().getSimpleName();
		StringJoiner sj = new StringJoiner(", ", className+"(", ")");
		for(Node<T> node = top; node != null; node = node.next) {
			sj.add(node.elem.toString());
		}
		return sj.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LinkedStack<?> that = (LinkedStack<?>) o;

		Node<T> node = top;
		Node<?> thatNode = that.top;

		while (node != null) {
			if(thatNode == null || !Objects.equals(node.elem, thatNode.elem)) {
				return false;
			}
			node = node.next;
			thatNode = thatNode.next;
		}

		return thatNode == null;
	}

	@Override
	public int hashCode() {
		int result = 1;
		for(Node<T> node = top; node != null; node = node.next) {
			result = 31 * result + Objects.hashCode(node.elem);
		}
		return result;
	}
}
