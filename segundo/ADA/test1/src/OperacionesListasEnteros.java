import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperacionesListasEnteros {
  public static void rotarLista(List<Integer> listaIzq, List<Integer> listaDer, int e) {
    while (e > 0){
      listaIzq.add(listaDer.remove(0));
      listaDer.add(listaIzq.remove(0));
      e--;
    }
  }

  public static void main(String[] args) {
    int e = 1;
    List<Integer> lizq = new ArrayList<>();
    lizq.addAll(Arrays.asList(10,20,30));

    List<Integer> lder = new ArrayList<>();
    lder.addAll(Arrays.asList(40,50,60,70));


    System.out.println(lizq);
    System.out.println(lder);

    rotarLista(lizq,lder,e);

    System.out.println(lizq);
    System.out.println(lder);

  }
}
