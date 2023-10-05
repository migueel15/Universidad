package ejer11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperacionesListasEnteros {
  public static void rotarLista(List<Integer> lista, int e) {
    while (e>0){
      lista.add(lista.remove(0));
      e--;
    }
  }

  public static void main(String[] args) {
    int e = 3;
    List<Integer> l = new ArrayList<>();
    l.addAll(Arrays.asList(10,20,30,40,50,60,70));
    System.out.println(l);
    rotarLista(l, e);
    System.out.println(l);
  }

}
