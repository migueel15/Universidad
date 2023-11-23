package algorithms;

import analyzer.Algorithm;

public class BinaryExponentiation implements Algorithm {
    long n, sol;
    @Override
    public String getName() {
        return "Binary exponentiation";
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
        return sol + "";
    }

    @Override
    public String getComplexity() {
        return "log(n)";
    }

    @Override
    public long getMaxSize() {
        return 60;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000000; i++) {
            binaryExponentiation(n);
        }
    }

    private void binaryExponentiation(long n) {
        sol = 1;
        long power = 2, M = n;
        while (M > 0) {
            if ((M & 1) == 1) {
                sol *= power;
            }
            power = power * power;
            M = M >> 1;
        }
    }

    public static void main(String[] args) {
        BinaryExponentiation binaryExponentiation = new BinaryExponentiation();
        binaryExponentiation.init(60);
        binaryExponentiation.run();
        System.out.println(binaryExponentiation.getSolution());
    }
}
