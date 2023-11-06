package algorithms;

import analyzer.Algorithm;
import analyzer.Util;

import java.util.Arrays;

public class MergeSort implements Algorithm {
    int[] v = null;
    int x = -1;

    @Override
    public String getName() {
        return "Merge Sort";
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
        int[] tmp = new int[v.length];
        int[] v_copy = Arrays.copyOf(v, v.length);
        for(int i=0; i < 100; i++) {
            mergeSort(v_copy, tmp, 0, v.length-1);
            v_copy = Arrays.copyOf(v, v.length);
        }
        v = v_copy;
    }

    public void mergeSort(int[] v, int[] tmp, int l, int r){
        if (l < r)
        {
            // Find the middle point
            int m = (l+r)/2;

            // Sort first and second halves
            mergeSort(v, tmp, l, m);
            mergeSort(v, tmp, m+1, r);

            // Merge the sorted halves
            merge(v, tmp, l, m, r);
        }
    }

    public void merge(int[] v, int[] tmp, int l, int m, int r) {
        int i, j, k;
        int n1 = m - l + 1;
        int n2 =  r - m;

        /* Copy data to temp arrays L[] and R[] */
        for (i = 0; i < n1; i++) {
            tmp[i] = v[l + i];
        }
        for (j = 0; j < n2; j++) {
            tmp[j] = v[m + 1+ j];
        }

        /* Merge the temp arrays back into v[l..r]*/
        i = 0; // Initial index of first subarray
        j = 0; // Initial index of second subarray
        k = l; // Initial index of merged subarray
        while (i < n1 && j < n2) {
            if (tmp[i] <= tmp[j]) {
                v[k] = tmp[i];
                i++;
            }
            else {
                v[k] = tmp[j];
                j++;
            }
            k++;
        }

        /* Copy the remaining elements of L[], if there
           are any */
        while (i < n1) {
            v[k] = tmp[i];
            i++;
            k++;
        }

        /* Copy the remaining elements of R[], if there
           are any */
        while (j < n2) {
            v[k] = tmp[j];
            j++;
            k++;
        }
    }

    @Override
    public String getSolution() {
        return "v="+ Arrays.toString(this.v);
    }

    @Override
    public String getComplexity() {
        return "n*log(n)";
    }

    @Override
    public long getMaxSize() {
        return 1000000;
    }

    public static void main(String[] args) {
        MergeSort algorithm = new MergeSort();

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
