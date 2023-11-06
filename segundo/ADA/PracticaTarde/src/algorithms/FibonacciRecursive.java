package algorithms;

import analyzer.Algorithm;

public class FibonacciRecursive implements Algorithm {

    long n, r;
    @Override
    public String getName() {
        return "Fibonacci Recursive";
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
        this.r = fibonnaicRecursive(n);
    }

    private long fibonnaicRecursive(long n) {
        if(n <= 1) {
            return n;
        }
        return fibonnaicRecursive(n - 1) + fibonnaicRecursive(n - 2);
    }

    @Override
    public String getSolution() {
        return "fib("+ this.n + ") = " + this.r;
    }

    @Override
    public String getComplexity() {
        return "2^n";
    }

    @Override
    public long getMaxSize() {
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        FibonacciRecursive linearSearch = new FibonacciRecursive();
        linearSearch.init(10);
        linearSearch.run();
        System.out.println(linearSearch.getSolution());
    }
}
