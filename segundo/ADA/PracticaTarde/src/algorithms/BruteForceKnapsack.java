package algorithms;

import analyzer.Algorithm;

public class BruteForceKnapsack implements Algorithm {
    long n, maxWeight;
    float[] values, weights;
    float solution;

    @Override
    public String getName() {
        return "Bruteforce Knapsack";
    }

    @Override
    public void init(long n) {
        this.n = n;
        reset();
    }

    @Override
    public void reset() {
        values = new float[(int) n];
        weights = new float[(int) n];

        float sumWeight = 0;
        for(int i = 0; i < n; i++) {
            values[i] = (float) (Math.random() * 100);
            weights[i] = (float) (Math.random() * 100);
            sumWeight += weights[i];
        }

        maxWeight = (long) (0.5 * sumWeight + (Math.random() * sumWeight * 0.7));
    }

    @Override
    public String getSolution() {
        return solution + "";
    }

    @Override
    public String getComplexity() {
        return "2^n";
    }

    @Override
    public long getMaxSize() {
        return 1000;
    }

    @Override
    public void run() {
        for (int i=0 ; i < 1000 ; i++) {
            knapSack((int) maxWeight, weights, values, (int) n);
        }
    }

    static float knapSack(float W, float[] wt, float[] val, int n) {
        if (n == 0 || W == 0) {
            return 0;
        }

        if (wt[n - 1] > W) {
            return knapSack(W, wt, val, n - 1);
        } else {
            return Math.max(val[n - 1] + knapSack(W - wt[n - 1], wt, val, n - 1), knapSack(W, wt, val, n - 1));
        }
    }
}
