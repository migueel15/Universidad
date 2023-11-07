package analyzer;

import java.util.Arrays;

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


        if(algorithm.getComplexity() != "Constant" && algorithm.getComplexity() != "Cubic" && algorithm.getComplexity() != "Exponential"){



        int[] longitudes = {10,20,30};
        double[] tiempos = new double[3];
        for(int k = 0; k < 3; k++) {
            double[] t1 = new double[longitudes.length];
            for (int i = 0; i < longitudes.length; i++) {
                algorithm.init(longitudes[i]);
                Chronometer chrono = new Chronometer();
                algorithm.run();
                chrono.stop();
                double time = chrono.getElapsedTime();
                t1[i] = time * 100;
            }
            tiempos[k] = media(t1);
        }

        double[] valores = calcularRatiosDosADos(tiempos);
        double sum = 0;
        for(int i = 0; i < valores.length; i++){
            sum += valores[i];
        }
        sum = sum / valores.length;

        //System.out.println(Arrays.toString(valores));
        System.out.println(String.format("%15s",algorithm.getComplexity()) + " --> " + String.format("%.2f",media(valores)*10));
        //System.out.print((double)sum + " " + algorithm.getComplexity() + " " + Arrays.toString(tiempos) + " >> 15:" + sum);


        }
        return "log(n)";

    }
    private static double[] calcularRatiosDosADos(double[] tiempos){
        double[] ratios = new double[tiempos.length-1];
        for(int i = 0; i < ratios.length; i++){
            if(tiempos[i] == 0){
                tiempos[i] = 0.1;
            }
                ratios[i] = tiempos[i+1] / tiempos[i];
        }
        return ratios;
    }

    private static double media(double[] lista){
        double suma = 0;
        for(int i = 0; i < lista.length; i++){
            suma += lista[i];
        }
        return suma/lista.length;
    }
}
