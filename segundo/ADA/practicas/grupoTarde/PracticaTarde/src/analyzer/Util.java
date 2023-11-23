package analyzer;

import java.io.File;
import java.util.ArrayList;

public class Util {

    // list all files in a directory
    public static ArrayList<String> listFiles(File dir, String extension) {
        ArrayList<String> names = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    names.addAll(listFiles(f, extension));
                } else {
                    if (f.getName().endsWith(extension)) {
                        names.add(f.getName());
                    }
                }
            }
        }

        return names;
    }

    public static int[] nextPermutation(int[] a) {
        int n = a.length;
        int i = n - 1;
        while (i > 0 && a[i - 1] >= a[i]) {
            i--;
        }
        if (i <= 0) {
            return null;
        }

        int j = n - 1;
        while (a[j] <= a[i - 1]) {
            j--;
        }

        int temp = a[i - 1];
        a[i - 1] = a[j];
        a[j] = temp;

        j = n - 1;
        while (i < j) {
            temp = a[i];
            a[i] = a[j];
            a[j] = temp;
            i++;
            j--;
        }

        return a;
    }

    public static int[] sequence(int n) {
        int[] a = new int[n];
        for(int i = 0; i < n; i++) {
            a[i] = i;
        }
        return a;

        //List<Integer> range = IntStream.rangeClosed(0, n-1).boxed().toList();
    }

    public static float average(float[] values) {
        float sum = 0;
        for(float t : values) {
            sum += t;
        }
        return sum / values.length;
    }

    public static double average(double[] values) {
        float sum = 0;
        for(double t : values) {
            sum += t;
        }
        return sum / values.length;
    }

    public static float average(float[] values, int start, int end) {
        float sum = 0;
        for(int i = start; i < end; i++) {
            sum += values[i];
        }
        return sum / (end - start);
    }

    public static float[] normalize(long[] values) {
        long max = 0;
        for(long t : values) {
            if(t > max) {
                max = t;
            }
        }

        float[] normalized = new float[values.length];
        for(int i = 0; i < values.length; i++) {
            normalized[i] = (float) values[i] / max;
        }

        return normalized;
    }

    public static float[] normalize(float[] values) {
        float max = 0;
        for(float t : values) {
            if(t > max) {
                max = t;
            }
        }

        float[] normalized = new float[values.length];
        for(int i = 0; i < values.length; i++) {
            normalized[i] = (float) values[i] / max;
        }

        return normalized;
    }

    public static float[] normalize(int[] values) {
        int max = 0;
        for(int t : values) {
            if(t > max) {
                max = t;
            }
        }

        float[] normalized = new float[values.length];
        for(int i = 0; i < values.length; i++) {
            normalized[i] = (float) values[i] / max;
        }

        return normalized;
    }

    public static float stdDev(float[] values) {
        float mean = average(values);
        float sum = 0;
        for(float t : values) {
            sum += Math.pow(t - mean, 2);
        }
        return (float) Math.sqrt(sum / values.length);
    }
    public static double stdDev(double[] values) {
        double mean = average(values);
        double sum = 0;
        for(double t : values) {
            sum += Math.pow(t - mean, 2);
        }
        return Math.sqrt(sum / values.length);
    }

    public static float stdDev(float[] values, int start, int end) {
        float mean = average(values, start, end);
        float sum = 0;
        for(int i = start; i < end; i++) {
            sum += Math.pow(values[i] - mean, 2);
        }
        return (float) Math.sqrt(sum / (end - start));
    }

    public static double variance(double[] values) {
        double mean = average(values);
        double sum = 0;
        for(double t : values) {
            sum += Math.pow(t - mean, 2);
        }
        return sum / values.length;
    }

    public static float variance(float[] values) {
        float mean = average(values);
        float sum = 0;
        for(float t : values) {
            sum += Math.pow(t - mean, 2);
        }
        return sum / values.length;
    }

    public static String arrayToFormattedString(float[] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for(float t : values) {
            sb.append(String.format("%.4f", t));
            sb.append(separator);
        }
        return sb.toString();
    }

    public static String arrayToFormattedString(double[] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for(double t : values) {
            sb.append(String.format("%.4f", t));
            sb.append(separator);
        }
        return sb.toString();
    }

    public static String arrayToFormattedString(int[] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for(int t : values) {
            sb.append(t);
            sb.append(separator);
        }
        return sb.toString();
    }

    public static String arrayToFormattedString(long[] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for(long t : values) {
            sb.append(t);
            sb.append(separator);
        }
        return sb.toString();
    }

    public static String matrixToFormattedString(float[][] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for(float[] row : values) {
            for(float t : row) {
                sb.append(String.format("%.4f", t));
                sb.append(separator);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String matrixToFormattedString(double[][] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for(double[] row : values) {
            for(double t : row) {
                sb.append(String.format("%.4f", t));
                sb.append(separator);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static float slope(float x1, float y1, float x2, float y2) {
        return (y2 - y1) / (x2 - x1);
    }

    public static float max(float[] times) {
        float max = 0;
        for(float t : times) {
            if(t > max) {
                max = t;
            }
        }
        return max;
    }

    public static boolean isApproxEqual(float v1, int v2, float diff) {
        return Math.abs(v1 - v2) < diff;
    }
}
