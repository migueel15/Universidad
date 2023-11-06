package algorithms;

import analyzer.Algorithm;

public class MinMax implements Algorithm {
    long[] v = null;
    long min, max;

    @Override
    public String getName() {
        return "MinMax";
    }

    @Override
    public void init(long n) {
        v = new long[(int) Math.min(n, getMaxSize())];
        for (int i = 0; i < v.length; i++) {
            v[i] = (long) (Math.random() * 1000000);
        }
        this.reset();
    }

    @Override
    public void reset() {
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            minMax(v);
        }
    }

    private void minMax(long[] v) {
        this.min = v[0];
        this.max = v[0];
        for (int i = 1; i < v.length; i++) {
            if (v[i] < this.min) {
                this.min = v[i];
            }
            if (v[i] > this.max) {
                this.max = v[i];
            }
        }
    }

    @Override
    public String getSolution() {
        return "min="+ this.min + ", max=" + this.max;
    }

    @Override
    public String getComplexity() {
        return "n";
    }

    @Override
    public long getMaxSize() {
        return 1000000000;
    }

    public static void main(String[] args) {
        MinMax linearSearch = new MinMax();
        linearSearch.init(10);
        linearSearch.run();
        System.out.println(linearSearch.getSolution());
    }
}
