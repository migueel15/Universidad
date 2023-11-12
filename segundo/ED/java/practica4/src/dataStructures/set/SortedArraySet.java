/* @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Sets implemented using a sorted array
 */

package dataStructures.set;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public class SortedArraySet<T extends Comparable<? super T>> implements SortedSet<T> {
  private T[] elements;  // Array storing elements in this set
  private int size;      // Number of elements in this set

  // INVARIANT: Elements are sorted in ascending order within the
  //            array and there are no repetitions in the
  //            structure

  private final static int INITIAL_CAPACITY = 10;

  @SuppressWarnings("unchecked")
  public SortedArraySet() {
    elements = (T[]) new Comparable[INITIAL_CAPACITY];
    size = 0;
  }

  @SuppressWarnings("unchecked")
  public SortedArraySet(int initialCapacity) {
    elements = (T[]) new Comparable[initialCapacity];
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  private void ensureCapacity() {
    if (size == elements.length)
      elements = Arrays.copyOf(elements, elements.length * 2);
  }

  private class Finder {
    boolean found;
    int index;

    // Uses binary search to search for elem in array.
    // If elem is found:
    //   * found is set to true and index is set to index of cell in array containing elem.
    // If elem is not found:
    //   * found is set to false and index is set to index of cell in array
    //   where elem should be stored.
    Finder(T elem) {
      // todo
    }
  }

  public void insert(T elem) {
    // todo
    //  Implement insert by using Finder

  }

  public boolean isElem(T elem) {
    // todo
    //  Implement isElem by using Finder
    return false;
  }

  public void delete(T elem) {
    // todo
    //  Implement delete by using Finder
  }

  // An iterator for this class
  private class SortedArraySetIterator implements Iterator<T> {
    private int index;

    public SortedArraySetIterator() {
      // todo
    }

    public boolean hasNext() {
      // todo
      return false;
    }

    public T next() {
      // todo
      return null;
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new SortedArraySetIterator();
  }

  @Override
  public String toString() {
    String className = getClass().getSimpleName();
    StringJoiner sj = new StringJoiner(", ", className + "(", ")");
    for (int i = 0; i < size; i++)
      sj.add(elements[i].toString());
    return sj.toString();
  }

  // Adds a new element at the end of SortedArraySet.
  // precondition: elem should be larger than any element in set
  private void append(T elem) {
    assert size == 0 || elem.compareTo(elements[size - 1]) > 0 : "append: precondition failed";
    // todo
  }

  // Copy constructor: builds a new SortedLinkedSet with the same
  // elements as parameter sortedSet
  public SortedArraySet(SortedSet<T> sortedSet) {
    // todo
  }

  public static <T extends Comparable<? super T>>
  SortedArraySet<T> union(SortedArraySet<T> set1, SortedArraySet<T> set2) {
    // todo Should compute a new SortedArraySet including all elements which are
    //      in set1 or in set2.
    //      Neither set1 nor set2 should be modified.

    // todo
    return null;
  }

  public static <T extends Comparable<? super T>>
  SortedArraySet<T> intersection(SortedArraySet<T> set1, SortedArraySet<T> set2) {
    // todo Should compute a new SortedArraySet including only common elements in
    //      set1 and in set2.
    //      Neither set1 nor set2 should be modified.

    // todo
    return null;
  }

  public static <T extends Comparable<? super T>>
  SortedArraySet<T> difference(SortedArraySet<T> set1, SortedArraySet<T> set2) {
    // todo Should compute a new SortedArraySet including all elements in
    //      set1 which are not in set2.
    //      Neither set1 nor set2 should be modified.

    // todo
    return null;
  }
}
