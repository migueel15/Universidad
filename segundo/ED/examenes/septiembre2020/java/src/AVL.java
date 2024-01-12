
/**
 * Student's name: Miguel Angel Dorado Maldonado
 *
 * Student's group: Software A
 */

import dataStructures.list.List;
import dataStructures.list.ListException;
import dataStructures.list.ArrayList;
import dataStructures.list.LinkedList;

import java.util.Iterator;

class Bin {
  private int remainingCapacity; // capacity left for this bin
  private List<Integer> weights; // weights of objects included in this bin

  public Bin(int initialCapacity) {
    remainingCapacity = initialCapacity;
    weights = new ArrayList<>();
  }

  // returns capacity left for this bin
  public int remainingCapacity() {
    return remainingCapacity;
  }

  // adds a new object to this bin
  public void addObject(int weight) {
    if (weight <= remainingCapacity) {
      weights.append(weight);
      remainingCapacity -= weight;
    } else {
      throw new ListException("peso mayor a la capacidad del cubo");
    }
  }

  // returns an iterable through weights of objects included in this bin
  public Iterable<Integer> objects() {
    // todo
    // SOLO PARA ALUMNOS SIN EVALUACION CONTINUA
    // ONLY FOR STUDENTS WITHOUT CONTINUOUS ASSESSMENT
    return null;
  }

  public String toString() {
    String className = getClass().getSimpleName();
    StringBuilder sb = new StringBuilder(className);
    sb.append("(");
    sb.append(remainingCapacity);
    sb.append(", ");
    sb.append(weights.toString());
    sb.append(")");
    return sb.toString();
  }
}

// Class for representing an AVL tree of bins
public class AVL {
  static private class Node {
    Bin bin; // Bin stored in this node
    int height; // height of this node in AVL tree
    int maxRemainingCapacity; // max capacity left among all bins in tree rooted at this node
    Node left, right; // left and right children of this node in AVL tree

    // recomputes height of this node
    void setHeight() {
      if (left == null && right == null) {
        height = 1;
      } else if (left == null) {
        height = right.height + 1;
      } else if (right == null) {
        height = left.height + 1;
      } else {
        height = Math.max(left.height, right.height) + 1;
      }
    }

    // recomputes max capacity among bins in tree rooted at this node
    void setMaxRemainingCapacity() {
      if (left == null && right == null) {
        maxRemainingCapacity = bin.remainingCapacity();
      } else if (left == null) {
        maxRemainingCapacity = Math.max(bin.remainingCapacity(), right.maxRemainingCapacity);
      } else if (right == null) {
        maxRemainingCapacity = Math.max(bin.remainingCapacity(), left.maxRemainingCapacity);
      } else {
        int maxChildCapacity = Math.max(left.maxRemainingCapacity, right.maxRemainingCapacity);
        maxRemainingCapacity = Math.max(maxChildCapacity, maxRemainingCapacity);
      }
    }

    // left-rotates this node. Returns root of resulting rotated tree
    Node rotateLeft() {
      Node newRoot = right;
      this.right = newRoot.left;
      newRoot.left = this;

      this.setMaxRemainingCapacity();
      this.setHeight();

      newRoot.setMaxRemainingCapacity();
      newRoot.setHeight();

      return newRoot;
    }
  }

  private static int height(Node node) {
    return node == null ? 0 : node.height;
  }

  private static int maxRemainingCapacity(Node node) {
    return node.maxRemainingCapacity;
  }

  private Node root; // root of AVL tree

  public AVL() {
    this.root = null;
  }

  // adds a new bin at the end of right spine.
  private void addNewBin(Bin bin) {
    root = addNewBinRec(bin, root);
  }

  private Node addNewBinRec(Bin bin, Node node) {
    if (node == null) {
      Node newNode = new Node();
      newNode.bin = bin;
      newNode.maxRemainingCapacity = bin.remainingCapacity();
      newNode.height = 1;

      return newNode;
    } else {
      node.right = addNewBinRec(bin, node.right);

      if (node.left != null && node.right != null) {
        if (node.right.height - node.left.height > 1) {
          node.rotateLeft();
        }
      }

      node.setHeight();
      node.setMaxRemainingCapacity();
      return node;
    }
  }

  // adds an object to first suitable bin. Adds
  // a new bin if object cannot be inserted in any existing bin
  public void addFirst(int initialCapacity, int weight) {
    addFirstRec(initialCapacity, weight, root);
  }

  private void addFirstRec(int initialCapacity, int weight, Node node) {
    if (node == null || node.maxRemainingCapacity < weight) {
      Bin bin = new Bin(initialCapacity);
      bin.addObject(weight);
      addNewBin(bin);
    } else if (node.left != null && node.left.maxRemainingCapacity >= weight) {
      addFirstRec(initialCapacity, weight, node.left);
    } else if (node.bin.remainingCapacity() >= weight) {
      node.bin.addObject(weight);
    } else {
      addFirstRec(initialCapacity, weight, node.right);
    }
  }

  public void addAll(int initialCapacity, int[] weights) {
    for (int weight : weights) {
      addFirst(initialCapacity, weight);
    }
  }

  public List<Bin> toList() {
    List<Bin> lista = new ArrayList<>();
    addInOrder(lista, root);

    return lista;
  }

  private void addInOrder(List<Bin> lista, Node node) {
    if (node != null) {
      if (node.left != null) {
        addInOrder(lista, node.left);
      }

      lista.append(node.bin);

      if (node.right != null) {
        addInOrder(lista, node.right);
      }
    }
  }

  public String toString() {
    String className = getClass().getSimpleName();
    StringBuilder sb = new StringBuilder(className);
    sb.append("(");
    stringBuild(sb, root);
    sb.append(")");
    return sb.toString();
  }

  private static void stringBuild(StringBuilder sb, Node node) {
    if (node == null)
      sb.append("null");
    else {
      sb.append(node.getClass().getSimpleName());
      sb.append("(");
      sb.append(node.bin);
      sb.append(", ");
      sb.append(node.height);
      sb.append(", ");
      sb.append(node.maxRemainingCapacity);
      sb.append(", ");
      stringBuild(sb, node.left);
      sb.append(", ");
      stringBuild(sb, node.right);
      sb.append(")");
    }
  }
}

class LinearBinPacking {
  public static List<Bin> linearBinPacking(int initialCapacity, List<Integer> weights) {
    // todo
    // SOLO PARA ALUMNOS SIN EVALUACION CONTINUA
    // ONLY FOR STUDENTS WITHOUT CONTINUOUS ASSESSMENT
    return null;
  }

  public static Iterable<Integer> allWeights(Iterable<Bin> bins) {
    // todo
    // SOLO PARA ALUMNOS SIN EVALUACION CONTINUA
    // ONLY FOR STUDENTS WITHOUT CONTINUOUS ASSESSMENT
    return null;
  }
}
