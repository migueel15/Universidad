/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Interface for priority queues
 */
 
package dataStructures.priorityQueue;


/**
 * Interface for priority queues data structures.
 *
 * @param <T> Type of elements in priority queue.
 */
public interface PriorityQueue<T extends Comparable<? super T>> {
	/** 
	 * Test for queue emptiness. 
	 * @return {@code true} if queue is empty, else {@code false}.
	 */
	boolean isEmpty();
	
	/**
	 * Inserts new element in queue.
	 * @param x the element to enqueue.
	 */
	void enqueue(T x);
	
	/** 
	 * Retrieves (without removing) first element in queue.
     * First element is one with highest priority (i.e. minimum value).
	 * @throws EmptyPriorityQueueException if queue is empty.
	 * @return First element in queue.
	 */
	T first();
	
	/**
	 * Removes first element from queue.
     * First element is one with highest priority (i.e. minimum value).
	 * @throws EmptyPriorityQueueException if queue is empty.
	 */
	void dequeue();
}
