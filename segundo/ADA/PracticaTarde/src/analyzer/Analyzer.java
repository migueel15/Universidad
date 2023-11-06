package analyzer;

public class Analyzer implements Runnable {
    Algorithm algorithm;
    long maxExecutionTime;
    String complexity = null;

    public Analyzer(Algorithm algorithm, long maxExecutionTime) {
        this.algorithm = algorithm;
        this.maxExecutionTime = maxExecutionTime;
    }

    public String getComplexity() {
        return complexity;
    }

    @Override
    public void run() {
        complexity = findComplexityOf(algorithm, maxExecutionTime);
    }

    static String findComplexityOf(Algorithm algorithm, long maxExecutionTime) {
        // Modify the content of this method in order to find the complexity of the given algorithm.
        // You can delete any of the following instructions if you don't need them. You can also
        // add new instructions or new methods, but you cannot modify the signature of this method
        // nor the existing methods.

        Chronometer chrono = new Chronometer();
        chrono.pause();
        int n = 10;
        algorithm.init(n);
        chrono.resume();
        algorithm.run();
        long time = chrono.getElapsedTime();
        String complexity = "1";
        if(time > 0.1) {
            complexity = "log(n)";
        }
        return complexity;
    }
}
