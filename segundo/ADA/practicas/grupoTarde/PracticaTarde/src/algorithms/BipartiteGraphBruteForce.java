package algorithms;

import analyzer.Algorithm;

public class BipartiteGraphBruteForce implements Algorithm {
    boolean[][] d;
    boolean isBipartite;

    @Override
    public String getName() {
        return "Bipartite graph brute force";
    }

    @Override
    public void init(long n) {
        this.d = new boolean[(int) n][(int) n];
        reset();
    }

    @Override
    public void reset() {
        for (int i = 0; i < this.d.length; i++) {
            this.d[i][i] = false;
            for(int j = 0; j < i; j++) {
                this.d[i][j] = Math.random() > 0.5;
                this.d[j][i] = this.d[i][j];
            }
        }
    }

    @Override
    public String getSolution() {
        return isBipartite ? "Bipartite" : "Not bipartite";
    }

    @Override
    public String getComplexity() {
        return "2^n";
    }

    @Override
    public long getMaxSize() {
        return 1000000;
    }

    @Override
    public void run() {
        int[] colors = new int[this.d.length];
        isBipartite = isBipartiteBruteForce(this.d, colors, 0);
    }

    private boolean isBipartiteBruteForce(boolean[][] d, int[] colors, int n) {
        if(n == d.length) {
            return true;
        }
        for(int c = 0; c < 2; c++) {
            colors[n] = c;
            if(isValid(d, colors, n)) {
                if(isBipartiteBruteForce(d, colors, n + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValid(boolean[][] d, int[] colors, int n) {
        for(int i = 0; i < n; i++) {
            if(d[n][i] && colors[n] == colors[i]) {
                return false;
            }
        }
        return true;
    }
}
