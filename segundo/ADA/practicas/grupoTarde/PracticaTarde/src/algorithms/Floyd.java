package algorithms;

import analyzer.Algorithm;

public class Floyd implements Algorithm {

    double[][] d;
    @Override
    public String getName() {
        return "Floyd";
    }

    @Override
    public void init(long n) {
        this.d = new double[(int) n][(int) n];
        reset();
    }

    @Override
    public void reset() {
        for (int i = 0; i < this.d.length; i++) {
            for(int j = 0; j < this.d[i].length; j++) {
                this.d[i][j] = Math.random();
            }
        }
    }

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            double[][] r = floyd(this.d);
        }
    }

    private double[][] floyd(double[][] d) {
        int n = d.length;
        double[][] r = new double[n][n];
        for(int k = 0; k < n; k++) {
            for(int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    r[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                }
            }
        }
        return r;
    }

    @Override
    public String getSolution() {
        return "";
    }

    @Override
    public String getComplexity() {
        return "n^3";
    }

    @Override
    public long getMaxSize() {
        return 1000000;
    }

    public static void main(String[] args) {
        Floyd algorithm = new Floyd();
        algorithm.init(10);
        algorithm.run();
        System.out.println(algorithm.getSolution());
    }
}
