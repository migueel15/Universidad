package algorithms;

import analyzer.Algorithm;

public class Warshall implements Algorithm {

    boolean[][] d;
    @Override
    public String getName() {
        return "Warshall";
    }

    @Override
    public void init(long n) {
        this.d = new boolean[(int) n][(int) n];
        reset();
    }

    @Override
    public void reset() {
        for (int i = 0; i < this.d.length; i++) {
            for(int j = 0; j < this.d[i].length; j++) {
                this.d[i][j] = Math.random() > 0.5;
            }
        }
    }

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            boolean[][] r = warshall(this.d);
        }
    }

    private boolean[][] warshall(boolean[][] d) {
        int n = d.length;
        boolean[][] r = new boolean[n][n];
        for(int k = 0; k < n; k++) {
            for(int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    r[i][j] = d[i][j] || ( d[i][k] && d[k][j]);
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
        Warshall algorithm = new Warshall();
        algorithm.init(10);
        algorithm.run();
        System.out.println(algorithm.getSolution());
    }
}
