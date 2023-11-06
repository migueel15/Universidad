package algorithms;

import analyzer.Algorithm;
import analyzer.Chronometer;

public class Quadratic implements Algorithm {
    long sleep;

    @Override
    public String getName() {
        return "Quadratic";
    }

    @Override
    public void init(long n) {
        this.sleep = n;
    }

    @Override
    public void reset() {
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.sleep * this.sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSolution() {
        return "sleep="+ this.sleep;
    }

    @Override
    public String getComplexity() {
        return "n^2";
    }

    @Override
    public long getMaxSize() {
        return 1000000;
    }

    public static void main(String[] args) {
        Quadratic algorithm = new Quadratic();
        Chronometer chronometer = new Chronometer();
        chronometer.start();
        chronometer.pause();
        for (int i = 10; i < 10000; i *= 10) {
            algorithm.init(i);
            chronometer.resume();
            algorithm.run();
            chronometer.pause();
            System.out.println("n=" + i + " in " + chronometer.getElapsedTime() + "ms");
        }
    }
}
