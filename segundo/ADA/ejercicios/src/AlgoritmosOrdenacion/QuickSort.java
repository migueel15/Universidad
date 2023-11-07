package AlgoritmosOrdenacion;

public class QuickSort {
  private static void intercambia(int[] a, int i , int j){
    int aux = a[i];
    a[i] = a[j];
    a[j] = aux;
  }
  private static int partir(int[] a, int inf, int sup){
    int pivote = a[inf];
    int i = inf+1;
    int j = sup;

    do{
     while (i <= j && a[i] <= pivote){i++;}
     while (i <= j && a[j] > pivote){j--;}
     if(i<j){intercambia(a,i,j);}
    }while (i <= j);
    intercambia(a,inf,j);
    return j;
  }
  public static void ordenar(int[] a, int inf, int sup){
    if(inf < sup){
      int p = partir(a, inf, sup);
      ordenar(a, inf, p-1);
      ordenar(a, p+1, sup);
    }
  }

  public static void main(String[] args) {
    int[] lista = {4,3,6,2,1};
    ordenar(lista,0,lista.length-1);
  }
}