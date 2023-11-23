package algorithms;

import analyzer.Algorithm;
import analyzer.Chronometer;

public class BinarySearch implements Algorithm {
    long[] v = null;
    int x = -1;
    int r = -1;

    @Override
    public String getName() {
        return "Binary Search";
    }

    @Override
    public void init(long n) {
        v = new long[(int) Math.min(n, getMaxSize())];
        for (int i = 0; i < n; i++) {
            v[i] = i;
        }
        this.reset();
    }

    @Override
    public void reset() {
        // pick a random number to search for
        this.x = (int) (Math.random() * v.length * 1.1);
    }

    @Override
    public void run() {
        long iterations = 0;
        int repetitions = 100000;
        for (int i = 0; i < repetitions; i++) {
            iterations += binarySearch(x, 0, v.length - 1);
        }
        //System.out.println("n= "+ v.length +" -> iterations=" + (iterations/repetitions));
    }

    /**
     * Iterative version of the binary search algorithm
     * @param x
     * @param left
     * @param right
     */
    private long binarySearch(int x, int left, int right) {
        long iterations = 0;
        int mid = 0;
        while (left <= right) {
            mid = (left + right) / 2;
            iterations++;
            if (v[mid] == x) {
                this.r = mid;
                return iterations;
            } else if (v[mid] < x) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        this.r = left;
        return iterations;
    }

    @Override
    public String getSolution() {
        return "x="+ this.x + " -> r=" + this.r;
    }

    @Override
    public String getComplexity() {
        return "log(n)";
    }

    @Override
    public long getMaxSize() {
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        BinarySearch binarySearch = new BinarySearch();
        Chronometer chronometer = new Chronometer();
        chronometer.start();
        chronometer.pause();
        for (int i = 10; i < 100000000; i *= 10) {
            binarySearch.init(i);
            chronometer.resume();
            binarySearch.run();
            chronometer.pause();
            System.out.println("n=" + i + " found " + binarySearch.getSolution() + " in " + chronometer.getElapsedTime() + "ms");
        }

        /*binarySearch.init(10);
        binarySearch.run();
        System.out.println(binarySearch.getSolution());*/
    }
}
