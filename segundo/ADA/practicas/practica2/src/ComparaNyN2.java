import java.util.Arrays;

public class ComparaNyN2 {
  public static void main(String[] args) {
    //int[] tam = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000};
    int[] tam = {10,100,500,1000,2000,5000,10000,50000,100000,500000,1000000};
    OrdenLogIter c = new OrdenLogIter();
    OrdenConstanteIter cte = new OrdenConstanteIter();
    OrdenCuadradoIter cuadrado = new OrdenCuadradoIter();

    long[] valoresa = Complejidad.medirTiempos(c,tam);
    long[] valoresb = Complejidad.medirTiempos(cte,tam);
    long[] valoresc = Complejidad.medirTiempos(cuadrado,tam);


    mostrar("Log", valoresa);
    mostrar("Cte", valoresb);
    mostrar("Cuadrado", valoresc);
  }

  private static void mostrar(String algo, long[] valores){
    System.out.println(algo + Arrays.toString(valores) + " " + (double) valores[valores.length-1] / valores[2]);
  }
}