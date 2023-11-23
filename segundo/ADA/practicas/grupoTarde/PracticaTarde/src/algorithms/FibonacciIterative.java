package algorithms;

import analyzer.Algorithm;

public class FibonacciIterative implements Algorithm {

    long n, r;
    @Override
    public String getName() {
        return "Fibonacci Iterative";
    }

    @Override
    public void init(long n) {
        this.n = n;
    }

    @Override
    public void reset() {
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            this.r = fibonnaicIterative(n);
        }
    }

    private long fibonnaicIterative(long n) {
        if(n <= 1) {
            return n;
        }
        long f1 = 0;
        long f2 = 1;
        long f = 0;
        for(int i = 2; i <= n; i++) {
            f = f1 + f2;
            f1 = f2;
            f2 = f;
        }
        return f;
    }

    @Override
    public String getSolution() {
        return "fib("+ this.n + ") = " + this.r;
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
        FibonacciIterative linearSearch = new FibonacciIterative();
        linearSearch.init(10);
        linearSearch.run();
        System.out.println(linearSearch.getSolution());
    }
}
