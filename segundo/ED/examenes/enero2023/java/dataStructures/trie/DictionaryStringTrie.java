/******************************************************************************
 * Student's name:
 * Student's group:
 * Identity number (DNI if Spanish/passport if Erasmus):
 *
 * Data Structures. Grados en Informática. UMA.
 *****************************************************************************/

package dataStructures.trie;

import dataStructures.dictionary.AVLDictionary;
import dataStructures.dictionary.Dictionary;
import dataStructures.list.LinkedList;
import dataStructures.list.List;
import dataStructures.tuple.Tuple2;

import java.util.Objects;

public class DictionaryStringTrie<V> {
  protected static class Node<V> {
    V value;
    Dictionary<Character, Node<V>> children;

    Node() {
      this.value = null;
      this.children = new AVLDictionary<>();
    }
  }

  protected Node<V> root;

 /*****************************************************************************
  * DO NOT WRITE ANY CODE ABOVE
  ****************************************************************************/

  // | = Exercise a - constructor
  public DictionaryStringTrie() {
    // TODO
  }

  // | = Exercise b - isEmpty
  public boolean isEmpty() {
    // TODO
    return false;
  }

  // | = Exercise c - sizeValue
  protected static <V> int sizeValue(V value) {
    // TODO
    return 0;
  }

  // | = Exercise d - size
  public int size() {
    // TODO
    return 0;
  }

  protected static <V> int size(Node<V> node) {
    // TODO
    return 0;
  }

  // | = Exercise e - childOf
  protected static <V> Node<V> childOf(char c, Node<V> node) {
    // TODO
    return null;
  }

  // | = Exercise f - search
  public V search(String str) {
    // TODO
    return null;
  }

  protected static <V> V search(String str, Node<V> node) {
    // TODO
    return null;
  }

  // | = Exercise g - insert
  public void insert(String str, V value) {
    // TODO
  }

  protected static <V> Node<V> insert(String str, V value, Node<V> node) {
    // TODO
    return null;
  }

  /*****************************************************************************
   * ONLY FOR PART TIME STUDENTS
   ****************************************************************************/

  // | = Exercise e1 - strings
  public List<String> strings() {
    // TODO
    return null;
  }

  protected static <V> List<String> strings(Node<V> node) {
    // TODO
    return null;
  }

  // | = Exercise e2 - fromList
  public static DictionaryStringTrie<Integer> fromList(List<String> list) {
    // TODO
    return null;
  }

  /*****************************************************************************
   * DO NOT WRITE ANY CODE BELOW
   ****************************************************************************/

  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (root != null) {
      sb.append(root.getClass().getSimpleName());
      sb.append(' ');
      sb.append(root.value);
      sb.append('\n');
      toString(sb,1, root);
    }
    return sb.toString();
  }

  private static <V> void toString(StringBuilder sb, int n, Node<V> node) {
    for (Tuple2<Character, Node<V>> par : node.children.keysValues()) {
      char c = par._1();
      Node<V> child = par._2();
      tabulate(sb, n);
      sb.append(c);
      sb.append(" -> ");
      sb.append(node.getClass().getSimpleName());
      sb.append(' ');
      sb.append(child.value);
      sb.append('\n');
      toString(sb, n + 1, child);
    }
  }

  private static void tabulate(StringBuilder sb, int n) {
    for (int i = 0; i < 6*n; i++) {
      sb.append(' ');
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DictionaryStringTrie<?> that = (DictionaryStringTrie<?>) o;
    return equals(root, that.root);
  }

  private static <V> boolean equals(Node<V> node, Node<?> that) {
    if (node == that) return true;
    if(!Objects.equals(node.value, that.value))
      return false;
    // same values
    for(char c : node.children.keys())
      if(!that.children.isDefinedAt(c))
        return false;
    for(char c : that.children.keys())
      if(!node.children.isDefinedAt(c))
        return false;
    // same keys
    for(Tuple2<Character, Node<V>> t : node.children.keysValues()) {
      char c = t._1();
      Node<V> child = t._2();
      if(!equals(child, that.children.valueOf(c)))
        return false;
    }
    // same associations
    return true;
  }

  public static DictionaryStringTrie<Integer> sampleTrie() {
    // bat -> 0  be -> 1  bed -> 2  cat -> 3  to -> 4  toe -> 5
    DictionaryStringTrie<Integer> trie = new DictionaryStringTrie<>();
    Node<Integer> n0 = new Node<>();
    Dictionary<Character, Node<Integer>> d0 = n0.children;
    Node<Integer> n1 = new Node<>();
    Dictionary<Character, Node<Integer>> d1 = n1.children;
    Node<Integer> n2 = new Node<>();
    Dictionary<Character, Node<Integer>> d2 = n2.children;
    Node<Integer> n3 = new Node<>();
    Dictionary<Character, Node<Integer>> d3 = n3.children;
    Node<Integer> n4 = new Node<>();
    Dictionary<Character, Node<Integer>> d4 = n4.children;
    Node<Integer> n5 = new Node<>();
    Dictionary<Character, Node<Integer>> d5 = n5.children;
    Node<Integer> n6 = new Node<>();
    Dictionary<Character, Node<Integer>> d6 = n6.children;
    Node<Integer> n7 = new Node<>();
    Dictionary<Character, Node<Integer>> d7 = n7.children;
    Node<Integer> n8 = new Node<>();
    Node<Integer> n9 = new Node<>();
    Node<Integer> n10 = new Node<>();
    Node<Integer> n11 = new Node<>();
    d0.insert('b',n1);
    d0.insert('c',n2);
    d0.insert('t',n3);
    d1.insert('a',n4);
    d1.insert('e',n5);
    d2.insert('a',n6);
    d3.insert('o',n7);
    d4.insert('t',n8);
    d5.insert('d',n9);
    n5.value = 1;
    d6.insert('t',n10);
    d7.insert('e',n11);
    n7.value = 4;
    n8.value = 0;
    n9.value = 2;
    n10.value = 3;
    n11.value = 5;
    trie.root = n0;
    return trie;
  }

  public static DictionaryStringTrie<Integer> sampleTrie1() {
    // a -> 3  b -> 2  c -> 1
    DictionaryStringTrie<Integer> trie = new DictionaryStringTrie<>();
    Node<Integer> n0 = new Node<>();
    Node<Integer> n1 = new Node<>();
    n1.value = 3;
    Node<Integer> n2 = new Node<>();
    n2.value = 2;
    Node<Integer> n3 = new Node<>();
    n3.value = 1;
    n0.children.insert('a', n1);
    n0.children.insert('b', n2);
    n0.children.insert('c', n3);
    trie.root = n0;
    return trie;
  }

  public static DictionaryStringTrie<Integer> sampleTrie2() {
    // a -> 1  ab -> 2  abc -> 3  abd -> 4  acdef -> 5
    DictionaryStringTrie<Integer> trie = new DictionaryStringTrie<>();
    Node<Integer> n0 = new Node<>();
    Node<Integer> n1 = new Node<>();
    n1.value = 1;
    Node<Integer> n2 = new Node<>();
    n2.value = 2;
    Node<Integer> n3 = new Node<>();
    n3.value = 3;
    Node<Integer> n4 = new Node<>();
    n4.value = 4;
    Node<Integer> n5 = new Node<>();
    Node<Integer> n6 = new Node<>();
    Node<Integer> n7 = new Node<>();
    Node<Integer> n8 = new Node<>();
    n8.value = 5;
    n0.children.insert('a', n1);
    n1.children.insert('b', n2);
    n1.children.insert('c', n5);
    n2.children.insert('c', n3);
    n2.children.insert('d', n4);
    n5.children.insert('d', n6);
    n6.children.insert('e', n7);
    n7.children.insert('f', n8);
    trie.root = n0;
    return trie;
  }

  public static DictionaryStringTrie<Integer> sampleTrie3() {
    // abcd -> 1
    DictionaryStringTrie<Integer> trie = new DictionaryStringTrie<>();
    Node<Integer> n0 = new Node<>();
    Node<Integer> n1 = new Node<>();
    Node<Integer> n2 = new Node<>();
    Node<Integer> n3 = new Node<>();
    Node<Integer> n4 = new Node<>();
    n4.value = 1;
    n0.children.insert('a', n1);
    n1.children.insert('b', n2);
    n2.children.insert('c', n3);
    n3.children.insert('d', n4);
    trie.root = n0;
    return trie;
  }

  public static DictionaryStringTrie<Integer> sampleTrie4() {
    // abcd -> 1  def -> 2
    DictionaryStringTrie<Integer> trie = new DictionaryStringTrie<>();
    Node<Integer> n0 = new Node<>();
    Node<Integer> n1 = new Node<>();
    Node<Integer> n2 = new Node<>();
    Node<Integer> n3 = new Node<>();
    Node<Integer> n4 = new Node<>();
    n4.value = 1;
    Node<Integer> n5 = new Node<>();
    Node<Integer> n6 = new Node<>();
    Node<Integer> n7 = new Node<>();
    n7.value = 2;
    n0.children.insert('a', n1);
    n0.children.insert('d', n5);
    n1.children.insert('b', n2);
    n2.children.insert('c', n3);
    n3.children.insert('d', n4);
    n5.children.insert('e', n6);
    n6.children.insert('f', n7);
    trie.root = n0;
    return trie;
  }
}
