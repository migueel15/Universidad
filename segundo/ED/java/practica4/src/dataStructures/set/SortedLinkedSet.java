/* @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Sets implemented using a sorted linked structure
 */

package dataStructures.set;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public class SortedLinkedSet<T extends Comparable<? super T>> implements SortedSet<T> {
  // A node in the linked structure
  static private class Node<E> {
    E elem;
    Node<E> next;

    Node(E x, Node<E> node) {
      elem = x;
      next = node;
    }
  }

  // INVARIANTS: Nodes for elements included in the set are kept in a sorted
  //             linked structure (sorted in ascending order with respect to
  //             their elements). In addition, there should be no repetitions
  //             of elements in the linked structure.
  private Node<T> first; // Reference to first (with the smallest element) node
  private int size;      // Number of elements in this set

  // Constructs an empty set
  public SortedLinkedSet() {
    first = null;
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  private class Finder {
    boolean found;
    Node<T> previous, current;

    Finder(T elem) {
      // todo
      //  An invocation of this constructor should
      //  search for elem in this class sorted linked structure.
      //  Attribute found should be set to true if elem was
      //  found or false otherwise.
      //  At the end of the search, current
      //  should be a reference to the node storing elem
      //  and previous should be a reference to node
      //  before current (or null if elem was found at first node).
      current = first;
      previous = null;
      found = false;
      while (current != null && !found && current.elem.compareTo(elem) <= 0){
        if(current.elem.compareTo(elem) == 0){
          found = true;
        }else{
          previous = current;
          current = current.next;
        }
      }
    }
  }

  public void insert(T elem) {
    // todo
    //  Implement insert by using Finder.
    //  insert should add a new node for elem
    //  to this class sorted linked structure
    //  if elem is not yet in this set.
    Finder finder = new Finder(elem);
    Node<T> actual = finder.current;
    if(!finder.found){
      if(first == null){
        first = new Node<>(elem, finder.current);
      }
      else if(finder.previous == null){
        first = new Node<>(elem, first);
      }else{
        finder.previous.next = new Node<>(elem, finder.previous.next);
      }
    }
  }

  public boolean isElem(T elem) {
    // todo
    //  Implement isElem by using Finder.
    //  isElem should return true if elem
    //  is in this class sorted linked structure
    //  or false otherwise.
    return new Finder(elem).found;
  }

  public void delete(T elem) {
    // todo
    //  Implement delete by using Finder.
    //  delete should remove the node containing elem
    //  from this class sorted linked structure
    //  if elem is in this set.
    Finder finder = new Finder(elem);
    if(finder.found){
      finder.previous.next = finder.current.next;
    }
  }

  public String toString() {
    String className = getClass().getSimpleName();
    StringJoiner sj = new StringJoiner(", ", className + "(", ")");
    for (Node<T> node = first; node != null; node = node.next)
      sj.add(node.elem.toString());
    return sj.toString();
  }

  /**
   * Iterator over elements in this set.
   * Note that {@code remove} method is not supported. Note also
   * that linked structure should not be modified during iteration as
   * iterator state may become inconsistent.
   *
   * @see Iterable#iterator()
   */
  public Iterator<T> iterator() {
    return new LinkedSetIterator();
  }

  private class LinkedSetIterator implements Iterator<T> {
    Node<T> current; // A reference to node with value that will be iterated next

    public LinkedSetIterator() {
      // todo
      //   Initialize iterator by making current a reference to first node
    }

    public boolean hasNext() {
      // todo
      //  Check if all elements have already been returned by this iterator
      return false;
    }

    public T next() {
      // todo
      //  Check if there are still more elements to be returned (raise
      //  NoSuchElementException otherwise), return next element and
      //  advance iterator to next node for next iteration.
      return null;
    }
  }

  // private constructor for building a SortedLinkedSet
  // by providing a reference to first node and size
  private SortedLinkedSet(Node<T> first, int size) {
    this.first = first;
    this.size = size;
  }

  // a buffer can be used to construct a SortedLinkedSet
  // efficiently in an incremental way by appending elements
  // in ascending order
  private static class SortedLinkedSetBuffer<T extends Comparable<? super T>> {
    Node<T> first, last; // references to first and last nodes in buffer
    int size;            // number of elements in buffer

    // Builds an empty buffer
    SortedLinkedSetBuffer() {
      first = null;
      last = null;
      size = 0;
    }

    // Adds a new element at the end of buffer.
    // precondition: elem should be larger than any element
    // currently in buffer
    void append(T elem) {
      assert first == null || elem.compareTo(last.elem) > 0 : "SortedLinkedSetBuffer.append: precondition failed";
      Node<T> node = new Node<>(elem, null);
      if (first == null) {
        first = node;
      } else {
        last.next = node;
      }
      last = node;
      size++;
    }

    // Builds a SortedLinkedSet using this buffer.
    SortedLinkedSet<T> toSortedLinkedSet() {
      return new SortedLinkedSet<>(first, size);
    }
  }

  // Copy constructor: builds a new SortedLinkedSet with the same
  // elements as parameter sortedSet.
  public SortedLinkedSet(SortedSet<T> sortedSet) {
    // todo
    //  Implement this copy constructor using a SortedLinkedSetBuffer
  }

  public static <T extends Comparable<? super T>>
  SortedLinkedSet<T> union(SortedLinkedSet<T> set1, SortedLinkedSet<T> set2) {
    // todo Should compute a new SortedLinkedSet including all elements which are
    //      in set1 or in set2.
    //      Neither set1 nor set2 should be modified.
    //      Implement this method by using a SortedLinkedSetBuffer.
    return null;
  }

  public static <T extends Comparable<? super T>>
  SortedLinkedSet<T> intersection(SortedLinkedSet<T> set1, SortedLinkedSet<T> set2) {
    // todo Should compute a new SortedLinkedSet including only common elements in
    //      set1 and in set2.
    //      Neither set1 nor set2 should be modified.
    //      Implement this method by using a SortedLinkedSetBuffer.
    return null;
  }

  public static <T extends Comparable<? super T>>
  SortedLinkedSet<T> difference(SortedLinkedSet<T> set1, SortedLinkedSet<T> set2) {
    // todo Should compute a new SortedLinkedSet including all elements in
    //      set1 which are not in set2.
    //      Neither set1 nor set2 should be modified.
    //      Implement this method by using a SortedLinkedSetBuffer.
    return null;
  }

  public void union(SortedSet<T> sortedSet) {
    // todo Should modify this set so that it becomes the union of
    //  this set and parameter sortedSet.
    //  Parameter sortedSet should not be modified.
    //  Should reuse current nodes in this set and should create new nodes for
    //  elements copied from sortedSet.
  }
}


