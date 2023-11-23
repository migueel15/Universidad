package algorithms;

import analyzer.Algorithm;
import analyzer.Chronometer;

public class Logarithmic implements Algorithm {
    long sleep;

    @Override
    public String getName() {
        return "Logarithmic";
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
            Thread.sleep((long) Math.log(this.sleep));
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
        return "log(n)";
    }

    @Override
    public long getMaxSize() {
        return 100000000;
    }

    public static void main(String[] args) {
        Logarithmic linear = new Logarithmic();
        Chronometer chronometer = new Chronometer();
        chronometer.start();
        chronometer.pause();
        for (int i = 10; i < 10000; i *= 10) {
            linear.init(i);
            chronometer.resume();
            linear.run();
            chronometer.pause();
            System.out.println("n=" + i + " in " + chronometer.getElapsedTime() + "ms");
        }
    }
}
