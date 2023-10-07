import java.util.Arrays;

public class ComparaNyN2 {
  public static void main(String[] args) {
    int[] tam = {1, 10, 100, 1000, 10000, 100000, 1000000};
    OrdenCuadradoIter c = new OrdenCuadradoIter();
    OrdenConstanteIter cte = new OrdenConstanteIter();

    long[] valoresa = Complejidad.medirTiempos(c,tam);
    long[] valoresb = Complejidad.medirTiempos(cte,tam);


    System.out.println(Arrays.toString(valoresa));
    System.out.println(Arrays.toString(valoresb));
  }
}