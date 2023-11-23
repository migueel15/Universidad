package algorithms;

import analyzer.Algorithm;
import analyzer.Chronometer;

public class Linearithmic implements Algorithm {
    long sleep;

    @Override
    public String getName() {
        return "Linearithmic";
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
            Thread.sleep((long) (this.sleep * Math.log(this.sleep)));
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
        return "n*log(n)";
    }

    @Override
    public long getMaxSize() {
        return 10000000;
    }

    public static void main(String[] args) {
        Linearithmic linear = new Linearithmic();
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
