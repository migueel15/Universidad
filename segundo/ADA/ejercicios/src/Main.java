import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<Integer> lista = new ArrayList(Arrays.asList(1,2,3,4,5));
    System.out.println(lista);
    int e = -2;
    e *= -1;
    while (e > 0){
      int ultimo = lista.remove(lista.size()-1);
      lista.add(0,ultimo);
      e--;
    }
    System.out.println(lista);
  }
}
