/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Exceptions for priority queues
 */
 
package dataStructures.priorityQueue;

public class EmptyPriorityQueueException extends RuntimeException {

	private static final long serialVersionUID = 2551856243837331348L;

	public EmptyPriorityQueueException() {
		super();
	}

	public EmptyPriorityQueueException(String msg) {
		super(msg);
	}
}
