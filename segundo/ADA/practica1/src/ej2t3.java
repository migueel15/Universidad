import java.util.List;

public class ej2t3 {
  public static boolean existesuma(List<Integer>v, int a){
    boolean existe = false;
    v.sort(); //quicksort
    int i = 0;
    int j = v.size() - 1;

    while(i < j && !existe){

      int suma = v.get(i) + v.get(j);
      if(suma == a){
        existe = true;
      }else if (a < suma){
        j--;
      }else{
        i++;
      }

    }

    return existe;
  }
}
