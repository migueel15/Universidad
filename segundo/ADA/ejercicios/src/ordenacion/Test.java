package ordenacion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
  public static void main(String[] args) {
    int[] a = lista();
    QuickSort qk = new QuickSort();
    long inicio = System.nanoTime();
    qk.ordenar(a,0,a.length-1);
    long tiempo = System.nanoTime()-inicio;
    double tiemposeg = tiempo * Math.pow(10, -6);
    System.out.println(tiemposeg);
    System.out.println(Arrays.toString(a));


    int[] b = lista();
    Burbuja bur = new Burbuja();
    long iniciob = System.nanoTime();
    bur.ordenar(b);
    long tiempob = System.nanoTime()-iniciob;
    double tiempobseg = tiempob * Math.pow(10, -6);
    System.out.println(tiempobseg);
    System.out.println(Arrays.toString(b));

    int[] c = lista();
    MergeSort mrg = new MergeSort();
    long inicioc = System.nanoTime();
    mrg.ordenar(c,0, c.length-1 );
    long tiempoc = System.nanoTime()-inicioc;
    double tiempocseg = tiempoc * Math.pow(10, -6);
    System.out.println(tiempocseg);
    System.out.println(Arrays.toString(c));
  }

  private static int[] lista() {
    int[]b = {8,3,2,9,7,1,5,8,3,2,9,7,1,5,8,3,2,9,7,1,5,8,3,2,9,7,1,5,4};
    return b;

  }
}
