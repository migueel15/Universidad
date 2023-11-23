package algorithms;

import analyzer.Algorithm;

public class CatalanNumber implements Algorithm {
    long n, result;
    @Override
    public String getName() {
        return "Catalan number";
    }

    @Override
    public void init(long n) {
        this.n = n;
    }

    @Override
    public void reset() {

    }

    @Override
    public String getSolution() {
        return result + "";
    }

    @Override
    public String getComplexity() {
        return "n^2";
    }

    @Override
    public long getMaxSize() {
        return 10000;
    }

    @Override
    public void run() {
        for(int i = 0; i < 1000; i++) {
            result = findNthCatalan((int) n);
        }
    }

    long findNthCatalan(int n) {
        long[] catalanArray = new long[n + 1];

        catalanArray[0] = 1;
        catalanArray[1] = 1;

        for (int i = 2; i <= n; i++) {
            catalanArray[i] = 0;
            for (int j = 0; j < i; j++) {
                catalanArray[i] += catalanArray[j] * catalanArray[i - j - 1];
            }
        }

        return catalanArray[n];
    }
}
