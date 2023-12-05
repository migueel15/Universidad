/**
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 *
 * Maxiphobic Heaps
 */

package dataStructures.heap;

public class MaxiphobicHeap<T extends Comparable<? super T>> implements	Heap<T> {

  // A node for an augmented binary tree
  private static class Node<E> {
    private E elem;        // the element
    private int size;      // the weight of tree rooted at this node
    private Node<E> left;  // left child (null if no left child)
    private Node<E> right; // right child  (null if no right child)
  }

  // Attribute for MaxiphobicHeap class
  private Node<T> root; // reference to root node of this Maxiphobic heap.
                        // null is heap is empty


  // Returns number of elements in tree stored at node
  private static int size(Node<?> node) {
    // todo
    return node != null ? node.size : 0;
  }

  // Merges two heaps. Returns merged heap.
  // Parameters are references to roots of heaps that should be merged.
  // Result should be a reference to root of resulting merged heap.
  private static <T extends Comparable<? super T>>
          Node<T> merge(Node<T> h1, Node<T> h2) {
    if(h1 == null){
      return h2;
    }
    if(h2 == null){
      return h1;
    }

    Node<T> minNode;
    Node<T> notSelected;
    if(h1.elem.compareTo(h2.elem) < 0){
      minNode = h1;
      notSelected = h2;
    }else {
      minNode = h2;
      notSelected = h1;
    }

    Node<T> maxCandidate = notSelected;
    Node<T> minL;
    Node<T> minR;

    if(minNode.left != null){
      minL = minNode.left;

      if(minL.size > maxCandidate.size){
        Node<T> aux = maxCandidate;
        maxCandidate = minL;
        minL = aux;
      }
    }else {minL = null;}

    if(minNode.right != null){
      minR = minNode.right;

      if(minR.size > maxCandidate.size){
        Node<T> aux = maxCandidate;
        maxCandidate = minR;
        minR = aux;
      }
    }else{minR = null;}

    minNode.left = maxCandidate;
    minNode.right = merge(minL,minR);
    minNode.size = minNode.left.size +1;
    if(minNode.right != null){
      minNode.size += minNode.right.size;
    }

    return minNode;
  }

  // Constructor for MaxiphobicHeap class. Creates an empty Maxiphobic heap
  public MaxiphobicHeap() {
    root = null;
  }

  // Returns true if this Maxiphobic heap is empty
  public boolean isEmpty() {
    return root == null;
  }

  // Returns total number of elements in this Maxiphobic heap
  public int size() {
    return root.size;
  }

  // Returns minimum element in this Maxiphobic heap
  public T minElem() {
    return root.elem;
  }

  // Removes minimum element from this Maxiphobic heap
  public void delMin() {
    root = merge(root.left,root.right);
  }

  // insert new element in this Maxiphobic heap
  public void insert(T elem) {
    // todo
    Node<T> n = new Node<>();
    n.elem = elem;
    n.size = 1;
    root = merge(root,n);
  }


  /**
   * Returns representation of this Maxiphobic heap as a String.
   */
  @Override public String toString() {
    String className = getClass().getSimpleName();
    StringBuilder sb = new StringBuilder();
    sb.append(className);
    sb.append("(");
    toStringRec(sb, root);
    sb.append(")");

    return sb.toString();
  }

  private static void toStringRec(StringBuilder sb, Node<?> node) {
    if(node == null) {
      sb.append("null");
    } else {
      sb.append("Node(");
      toStringRec(sb, node.left);
      sb.append(", ");
      sb.append(node.elem);
      sb.append(", ");
      toStringRec(sb, node.right);
      sb.append(")");
    }
  }
}