package algorithms;

import analyzer.Algorithm;

public class FastExponentiation implements Algorithm {
    long n, result;

    @Override
    public String getName() {
        return "Fast exponentiation";
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
        return "log(n)";
    }

    @Override
    public long getMaxSize() {
        return 1000000;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            result = fastExponentiation(n);
        }
    }

    private long fastExponentiation(long n) {
        if(n == 0) {
            return 1;
        } else if(n == 1) {
            return 2;
        } else if(n % 2 == 0) {
            long tmp = fastExponentiation(n / 2);
            return tmp * tmp;
        } else {
            long tmp = fastExponentiation((n - 1) / 2);
            return tmp * tmp * 2;
        }
    }
}
