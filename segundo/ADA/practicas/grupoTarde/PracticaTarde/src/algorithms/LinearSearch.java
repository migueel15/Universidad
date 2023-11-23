package algorithms;

import analyzer.Algorithm;

public class LinearSearch implements Algorithm {
    long[] v = null;
    int x = -1;
    int r = -1;

    @Override
    public String getName() {
        return "Linear Search";
    }

    @Override
    public void init(long n) {
        v = new long[(int) Math.min(n, getMaxSize())];
        for (int i = 0; i < v.length; i++) {
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
        for (int i = 0; i < 10000; i++) {
            linearSearch(x, 0, v.length);
        }
    }

    private void linearSearch(int x, int left, int right) {
        for (int i = left; i < right; i++) {
            if (v[i] == x) {
                this.r = i;
                return;
            }
        }
        this.r = -1;
    }

    @Override
    public String getSolution() {
        return "x="+ this.x + " -> r=" + this.r;
    }

    @Override
    public String getComplexity() {
        return "n";
    }

    @Override
    public long getMaxSize() {
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        LinearSearch linearSearch = new LinearSearch();
        linearSearch.init(10);
        linearSearch.run();
        System.out.println(linearSearch.getSolution());
    }
}
