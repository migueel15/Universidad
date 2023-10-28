package AlgoritmosOrdenacion;

public class Burbuja {
  public void ordenar(int[] a){
    for (int i = 0; i < a.length; i++){
      for (int j = 0; j < a.length; j++){
        if(a[i] < a[j]){
          int aux = a[i];
          a[i] = a[j];
          a[j] = aux;
        }
      }
    }
  }
}
