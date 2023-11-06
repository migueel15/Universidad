package algorithms;

import analyzer.Algorithm;
import analyzer.Util;

public class MatrixMultiplication implements Algorithm {
    long n;
    double matrix[][] = null, result[][] = null;

    @Override
    public String getName() {
        return "Matrix multiplication";
    }

    @Override
    public void init(long n) {
        this.n = n;
        reset();
    }

    @Override
    public void reset() {
        matrix = new double[(int) n][(int) n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                matrix[i][j] = Math.random();
            }
        }
        result = new double[(int) n][(int) n];
    }

    @Override
    public String getSolution() {
        return Util.matrixToFormattedString(result, " ");
    }

    @Override
    public String getComplexity() {
        return "n^3";
    }

    @Override
    public long getMaxSize() {
        return 10000000;
    }

    @Override
    public void run() {
        matrixMultiplication();
    }

    private void matrixMultiplication() {
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                result[i][j] = 0;
                for(int k = 0; k < n; k++) {
                    result[i][j] += matrix[i][k] * matrix[k][j];
                }
            }
        }
    }
}
