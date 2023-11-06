package algorithms;

import analyzer.Algorithm;

public class LinearSearchConstant implements Algorithm {
    long[] v = null;
    int x = -1;
    int r = -1;

    @Override
    public String getName() {
        return "Linear Search Constant";
    }

    @Override
    public void init(long n) {
        v = new long[10000000];
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
        //for (int i = 0; i < 100; i++) {
            linearSearch(x, 0, v.length);
        //}
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
        return "1";
    }

    @Override
    public long getMaxSize() {
        return 1000000000;
    }

    public static void main(String[] args) {
        LinearSearchConstant linearSearch = new LinearSearchConstant();
        linearSearch.init(10);
        linearSearch.run();
        System.out.println(linearSearch.getSolution());
    }
}
