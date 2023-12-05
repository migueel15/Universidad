import java.util.Arrays;
import java.util.Random;

import dataStructures.heap.*;

public class HeapSort {
  public static int[] heapSort(int[] xs, Heap<Integer> h) {

    // We firstly empty the heap
    while (!h.isEmpty()) {
      h.delMin();
    }

    // We now insert in heap all elements in array
    for (int x : xs)
      h.insert(x);

    // We now extract in order elements from heap
    System.out.println(h);
    int size = xs.length;
    int[] ys = new int[size];
    for (int i = 0; i < size; i++) {
      ys[i] = h.minElem();
      h.delMin();
    }
    return ys;
  }

  public static void print(int[] xs) {
    for (int x : xs) {
      System.out.print(x + " ");
    }
    System.out.println();
  }

  public static void randomHeap(int seed, int size, Heap<Integer> h) {
    Random rnd = new Random(seed);

    while (!h.isEmpty()) {
      h.delMin();
    }

    for (int i = 0; i < size; i++){
      h.insert(rnd.nextInt());
    }
  }

  static class TestResult {
    boolean passed;
    long nanoseconds;
  }

  public static TestResult testHeapSort(int seed, int size, Heap<Integer> h) {
    Random rnd = new Random(seed);

    int[] xs = new int[size];
    for (int i = 0; i < size; i++)
      xs[i] = rnd.nextInt(0,10);

    long t0 = System.currentTimeMillis();
    int[] ys = heapSort(xs, h);
    long t1 = System.currentTimeMillis();

    TestResult tr = new TestResult();
    tr.nanoseconds = t1 - t0;

    tr.passed = h.isEmpty();
    if(tr.passed) {
      Arrays.sort(xs);
      tr.passed = Arrays.equals(xs, ys);
    }

    return tr;
  }

  static void tests(Heap<Integer> h, String implementation) {
    final int NUM_TESTS = 1;
    final int LENGTH_ARRAY = 10;

    long runtime = 0;

    for (int seed = 0; seed < NUM_TESTS; seed++) {
      TestResult tr = testHeapSort(seed, LENGTH_ARRAY, h);
      if(!tr.passed) {
        System.out.println("Error on test");
        System.exit(0);
      } else {
        runtime += tr.nanoseconds;
      }
    }
    System.out.println("Use " + implementation);
    System.out.println("-> " + NUM_TESTS + " tests passed OK");

    System.out.printf("-> Took %f seconds per test\n", runtime / 1e3 / NUM_TESTS);
  }

  public static void main(String[] args) {
  //  tests(new BinaryHeap<>(), "BinaryHeap<Integer>");
    tests(new MaxiphobicHeap<>(), "MaxiphobicHeap<Integer>");
  }
}