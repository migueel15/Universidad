package algorithms;

import analyzer.Algorithm;
import analyzer.Util;

import java.util.Arrays;

public class SelectionSort implements Algorithm {
    int[] v = null;
    int x = -1;

    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public void init(long n) {
        //System.out.println("Initializing " + getName() + " with n=" + n);
        v = new int[(int) Math.min(n, getMaxSize())];

        this.reset();
        //System.out.println("Initialized " + getName());
    }

    @Override
    public void reset() {
        //System.out.println("Resetting " + getName());
        // fill the array with random numbers
        for (int i = 0; i < v.length; i++) {
            v[i] = (int) (Math.random() * v.length * 2);
        }
        //System.out.println("Reset " + getName());
    }

    @Override
    public void run() {
        for(int i=0; i < 10000; i++) {
            selectionSort(v);
        }
    }

    public void selectionSort(int[] v){
        int n = v.length;
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++) {
                if (v[j] < v[min_idx]) {
                    min_idx = j;
                }
            }

            // Swap the found minimum element with the first
            // element
            int temp = v[min_idx];
            v[min_idx] = v[i];
            v[i] = temp;
        }
    }

    @Override
    public String getSolution() {
        return "v="+ Arrays.toString(this.v);
    }

    @Override
    public String getComplexity() {
        return "n^2";
    }

    @Override
    public long getMaxSize() {
        return 10000;
    }

    public static void main(String[] args) {
        SelectionSort algorithm = new SelectionSort();

        for(int n=1; n<10; n++) {
            System.out.println("n=" + n);
            int[] sorted = Util.sequence(n);
            int[] current = Arrays.copyOf(sorted, sorted.length);
            do {
                algorithm.v = Arrays.copyOf(current, current.length);
                //System.out.print(Arrays.toString(bubleSort.v));
                algorithm.run();
                //System.out.println("->" + Arrays.toString(bubleSort.v));
                if (!Arrays.equals(algorithm.v, sorted)) {
                    System.out.println("Error");
                }
                current = Util.nextPermutation(current);
            } while (current != null);
        }
    }
}
