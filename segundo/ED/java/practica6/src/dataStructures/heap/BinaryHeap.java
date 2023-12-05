/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Binary Heaps with Arrays
 */
 
package dataStructures.heap;

import java.util.Arrays;

/**
 * Heap implemented as a complete heap-ordered binary tree stored in a array.
 * @param <T> Type of elements in heap.
 */
public class BinaryHeap<T extends Comparable<? super T>> implements Heap<T> {
	private int size;
	private T elements[];

	/**
	 * Creates an empty binary heap. Initial capacity is {@code n} elements.
	 * Capacity is automatically increased when needed.
	 * @param n Initial capacity.
	 * <p>Time complexity: O(1)
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeap(int n) {
		elements = (T[]) new Comparable[n];
		size = 0;
	}

	private static final int DEFAULT_INITIAL_CAPACITY = 128;

	/**
	 * Creates an empty binary heap with default initial capacity.
     * Capacity is automatically increased when needed.
	 * <p>Time complexity: O(1)
	 */
	public BinaryHeap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Copy constructor for Binary Heaps. 
	 * <p>Time complexity: O(n)
	 */	
	public BinaryHeap(BinaryHeap<T> h) {
		elements = Arrays.copyOf(h.elements,h.elements.length);
		size = h.size;
	}
	
	private void ensureCapacity() {
		if(size==elements.length) 
			elements = Arrays.copyOf(elements,2*elements.length);
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
		return (size == 0);
	}
	
	public void clear() {
		size = 0;
	}
	
	// true if elems[idx1] < elems[idx2]
	private boolean lessThan(int idx1, int idx2) {
		return elements[idx1].compareTo(elements[idx2]) < 0;
	}
	
	//swaps elements in array at positions idx1 and idx2
	private void swap(int idx1, int idx2) {
		T aux = elements[idx1];
		elements[idx1] = elements [idx2];
		elements[idx2] = aux;
	}
	
	// root of heap is at idx 0
	private static final int ROOT_INDEX = 0;

	private static boolean isRoot(int idx) {
		return idx==ROOT_INDEX;
	}
	
	private static int parent(int idx) { //returns index for parent of node with index idx
		return (idx-1) / 2;
	}
	
	private static int leftChild(int idx) { //returns index for left child of node with index idx
		return 2*idx+1;
	}

	private static int rightChild(int idx) { //returns index for right child of node with index idx
		return 1+leftChild(idx);
	}
	
	private boolean isNode(int idx) { //returns true if idx corresponds index of a node in tree
		return idx<size;
	}

	private boolean hasLeftChild(int idx) { //returns true if idx has a left child
		return leftChild(idx)<size;
	}
	
	private boolean isLeaf(int idx) { //returns true if idx is index of a leaf node
		return !hasLeftChild(idx);
	}

	private void heapifyUp(int idx) {
		while(!isRoot(idx)) { 
			int idxParent =	parent(idx);
			
			if(lessThan(idx,idxParent)) {
				swap(idx,idxParent);				
				idx = idxParent; // move to parent node for next iteration
			} else
				break;
		}		
	}
	
	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: mostly O(log n). O(n) when heap capacity has to be increased.
	 */
	public void insert(T x) {
		ensureCapacity();
		elements[size] = x;
		heapifyUp(size);
		size++;		
	}	

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(1)
	 * @throws <code>EmptyHeapException</code> if heap stores no element.
	 */
	public T minElem() {
		if(isEmpty())
			throw new EmptyHeapException("minElem on empty heap");
		else
			return elements[ROOT_INDEX];
	}
	
	private void heapifyDown() {
		int idx = ROOT_INDEX;
		
		while(!isLeaf(idx)) {
			int idxChild = leftChild(idx);
			int idxRightChild = rightChild(idx);
			if(isNode(idxRightChild) && lessThan(idxRightChild,idxChild))
				idxChild = idxRightChild; //idxChild is index of child with minimum value 

			if(lessThan(idxChild,idx)) {
				swap(idxChild,idx);
				idx = idxChild; // move to child node for next iteration
			} else
				break;
		}		
	}

	/** 
	 * {@inheritDoc}
	 * <p>Time complexity: O(log n)
	 * @throws <code>EmptyHeapException</code> if heap stores no element.
	 */
	public void delMin() {
		if(isEmpty())
			throw new EmptyHeapException("delMin on empty heap");
		else {
			elements[ROOT_INDEX] = elements[size-1];
			size--;
			heapifyDown();
		}
	}	

	/** 
	 * Returns representation of heap as a String.
	 */
	@Override public String toString() {
    String className = getClass().getSimpleName();
		String s = className+"(";
		for(int i=0; i<size; i++) 
			s += elements[i] + (i<size-1 ? "," : "");
		s += ")";
		return s;
	}
}

