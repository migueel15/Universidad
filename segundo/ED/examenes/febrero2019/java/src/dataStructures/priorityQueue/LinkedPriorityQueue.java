/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Priority queues implemented as sorted linked lists
 */
package dataStructures.priorityQueue;


/**
 * Priority queue implemented as a sorted linked list. 
 *
 * @param <T>
 */
public class LinkedPriorityQueue<T extends Comparable<? super T>> implements PriorityQueue<T> {
	static private class Node<E> {
		E elem;
		Node<E> next;
		public Node(E x) {
			elem = x;			
			next = null;
		}
	}
	
	private Node<T> first;
	private int size;

	
	/**
	 * Creates an empty queue.
	 */
	public LinkedPriorityQueue() {
		first = null;
		size = 0;
	}
	
	public void clear() {
		first = null;
		size = 0;
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public boolean isEmpty() {
		return size==0;
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws <code>EmptyPriorityQueueException</code> if queue stores no element.
	 */
	public void dequeue() {
		if(isEmpty())
			throw new EmptyPriorityQueueException("dequeue on empty priority queue");
		else {
			first = first.next;
			size--;
		}
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws <code>EmptyQueueException</code> if queue stores no element.
	 */
	public T first() {
		if(isEmpty())
			throw new EmptyPriorityQueueException("first on empty priority queue");
		else
			return first.elem;
	}

	/** 
	 * {@inheritDoc}
	 * Position of new element in queue depends on its priority.
	 * The less the value of the element, the higher its priority. 
	 * <p>Time complexity: O(n)
	 */
	public void enqueue(T x) {
		Node<T> node = new Node<>(x);
		if(isEmpty())
			first = node;
		else if(x.compareTo(first.elem)<0) { // x<first.elem. x has highest priority
			node.next = first;
			first = node;
		} else {	
			Node<T> aux = first;
			Node<T> prev = null;
			while((aux != null) && (x.compareTo(aux.elem)>=0)) { // x>=aux.elem. Advance while x's priority is less or equal to element in aux node
				prev = aux;
				aux = aux.next;
			}
			node.next = aux;
			prev.next = node;			
		}		
		size++;		
	}


	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 */
	public int size() {
		return size;
	}
	
	/** 
	 * Returns representation of priority queue as a String.
	 */
	@Override public String toString() {
    String className = getClass().getSimpleName();
		String s = className+"(";
		for(Node<T> node = first; node != null; node = node.next) 
			s += node.elem + (node.next != null ? "," : "");
		s += ")";
		return s;			
	}
}
