package algorithms;

import analyzer.Algorithm;
import analyzer.Util;

import java.util.Arrays;

public class HeapSort implements Algorithm {
    int[] v = null;
    int x = -1;

    @Override
    public String getName() {
        return "Heap Sort";
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
            heapSort(v_copy, 0, v.length-1);
            v_copy = Arrays.copyOf(v, v.length);
        }
        v = v_copy;
    }

    public void heapSort(int[] v, int l, int r){
        int n = v.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(v, n, i);

        for (int i=n-1; i>=0; i--)
        {
            int temp = v[0];
            v[0] = v[i];
            v[i] = temp;

            heapify(v, i, 0);
        }
    }

    void heapify(int[] v, int n, int i)
    {
        int largest = i;
        int l = 2*i + 1;
        int r = 2*i + 2;

        if (l < n && v[l] > v[largest])
            largest = l;

        if (r < n && v[r] > v[largest])
            largest = r;

        if (largest != i)
        {
            int swap = v[i];
            v[i] = v[largest];
            v[largest] = swap;

            heapify(v, n, largest);
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
}
