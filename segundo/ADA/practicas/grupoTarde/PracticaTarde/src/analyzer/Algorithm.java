package analyzer;

public interface Algorithm extends Runnable {
    String getName();

    void init(long n);

    void reset();

    String getSolution();

    String getComplexity();

    long getMaxSize();
}
