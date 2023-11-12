import dataStructures.set.SortedArraySet;
import dataStructures.set.SortedLinkedSet;

public class Main {
  public static void main(String[] args) {
    SortedLinkedSetTest();
    SortedArraySetTest();
  }

  public static void SortedLinkedSetTest() {
    SortedLinkedSet<Integer> s1 = new SortedLinkedSet<>();
    for(int x : new int[]{3, 7, 2, 1, 3, 4})
      s1.insert(x);
    System.out.println("s1: "+s1);

    SortedLinkedSet<Integer> s2 = new SortedLinkedSet<>();
    for(int x : new int[]{0, 3, 5, 4, 1, 8})
      s2.insert(x);
    System.out.println("s2: "+s2);

    s2.delete(1);
    s2.delete(10);
    System.out.println("s2 after deletes: "+s2);

    SortedLinkedSet<Integer> s3 = SortedLinkedSet.union(s1, s2);
    System.out.println("s3: "+s3);

    SortedLinkedSet<Integer> s1Copy = new SortedLinkedSet<>(s1);
    System.out.println("s1Copy: "+s1Copy);

    s1Copy.union(s2);
    System.out.println("s1Copy after union: "+s1Copy);

    SortedLinkedSet<Integer> s4 = SortedLinkedSet.intersection(s1, s2);
    System.out.println("s4: "+s4);

    SortedLinkedSet<Integer> s5 = SortedLinkedSet.difference(s1, s2);
    System.out.println("s5: "+s5);

    System.out.println();
  }

  public static void SortedArraySetTest() {
    SortedArraySet<Integer> s1 = new SortedArraySet<>();
    for(int x : new int[]{3, 7, 2, 1, 3, 4})
      s1.insert(x);
    System.out.println("s1: "+s1);

    SortedArraySet<Integer> s2 = new SortedArraySet<>();
    for(int x : new int[]{0, 3, 5, 4, 1, 8})
      s2.insert(x);
    System.out.println("s2: "+s2);

    s2.delete(1);
    s2.delete(10);
    System.out.println("s2 after deletes: "+s2);

    SortedArraySet<Integer> s3 = SortedArraySet.union(s1, s2);
    System.out.println("s3: "+s3);

    SortedArraySet<Integer> s1Copy = new SortedArraySet<>(s1);
    System.out.println("s1Copy: "+s1Copy);

    // s1Copy.union(s2);
    // System.out.println("s1Copy after union: "+s1Copy);

    SortedArraySet<Integer> s4 = SortedArraySet.intersection(s1, s2);
    System.out.println("s4: "+s4);

    SortedArraySet<Integer> s5 = SortedArraySet.difference(s1, s2);
    System.out.println("s5: "+s5);

    System.out.println();  }
}
