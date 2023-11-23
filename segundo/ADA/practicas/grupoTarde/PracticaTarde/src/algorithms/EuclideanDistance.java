package algorithms;

import analyzer.Algorithm;

public class EuclideanDistance implements Algorithm {
    long n;
    double distance;

    @Override
    public String getName() {
        return "Euclidean distance";
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
        return distance + "";
    }

    @Override
    public String getComplexity() {
        return "1";
    }

    @Override
    public long getMaxSize() {
        return 10000000;
    }

    @Override
    public void run() {
        for(int i = 0; i < 1000000000; i++) {
             distance = euclideanDistance(0, 0, n, n);
        }
    }

    public static double euclideanDistance(double x1, double y1, double x2, double y2) {
        double dX = Math.pow(x2 - x1, 2);
        double dY = Math.pow(y2 - x1, 2);
        return Math.sqrt(dX + dY);
    }
}
