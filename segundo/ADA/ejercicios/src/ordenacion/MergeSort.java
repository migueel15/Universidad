package ordenacion;

public class MergeSort {
  private static void mezclar(int[] a, int inf, int medio, int sup){
    int i = inf;
    int j = medio+1;
    int[] b = new int[sup-inf+1];
    int k = 0;

    while(i <= medio && j <= sup) {
      if (a[i] <= a[j]) {
        b[k] = a[i];
        i++;
      } else {
        b[k] = a[j];
        j++;
      }
      k++;
    }
    while(i <= medio){
      b[k] = a[i];
      i++;
      k++;
    }
    while(j <= sup){
      b[k] = a[j];
      j++;
      k++;
    }
    k = 0;
    for(int f = inf; f <= sup; f++){
      a[f] = b[k];
      k++;
    }
  }

  public static void ordenar(int[] a, int inf, int sup){
    if(inf < sup){
      ordenar(a,inf,(inf+sup)/2);
      ordenar(a,(inf+sup)/2+1,sup);
      mezclar(a, inf, (inf+sup)/2, sup);
    }
  }

  public static void main(String[] args) {
    int[] lista = {5,2,7,9,1,3,6,4};
    ordenar(lista,0, lista.length-1);
  }
}
