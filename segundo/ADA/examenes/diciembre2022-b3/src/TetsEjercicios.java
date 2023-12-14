import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TetsEjercicios {
  public static void main(String[] args) {
    int K = 5;
    int M = 3;
    ArrayList<Integer> n = new ArrayList<>(Arrays.asList(2,1,2));
    ArrayList<ArrayList<Integer>> p =
        new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(1,2)),
            new ArrayList<>(Arrays.asList(1,2,3)),
            new ArrayList<>(Arrays.asList(2,3)),
            new ArrayList<>(List.of(1)),
            new ArrayList<>(List.of(2))
        ));
    ejercicio1 ej1 = new ejercicio1(K,M,n,p);

    System.out.println(ej1.resolverVA(ej1.getResultado()) ?
        ej1.getResultado() : "No hay solucion");
  }
}
