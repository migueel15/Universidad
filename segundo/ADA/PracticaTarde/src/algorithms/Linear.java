package algorithms;

import analyzer.Algorithm;
import analyzer.Chronometer;

public class Linear implements Algorithm {
    long sleep;

    @Override
    public String getName() {
        return "Linear";
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
            Thread.sleep(this.sleep);
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
        return "n";
    }

    @Override
    public long getMaxSize() {
        return 1000000;
    }

    public static void main(String[] args) {
        Linear linear = new Linear();
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
