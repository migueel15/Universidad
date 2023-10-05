package ejer16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matematicas {
  public static void mostrarLista(List<Integer> lista, int i) {
    if(i < 0 || i > lista.size()){
      System.out.println(-1);
    }
    else if(i < lista.size()-1){
      mostrarLista(lista, i + 1);
      System.out.print(lista.get(i));

    }
    else{
      System.out.print(lista.get(i));
    }
  }


  public static void main(String[] args) {
    int e = 4;
    List<Integer> l = new ArrayList<>();
    l.addAll(Arrays.asList(10,20,30,40,50,60,70));
    System.out.println(l);
    mostrarLista(l, e);
  }
}