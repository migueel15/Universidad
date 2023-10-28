package ejercicios;

public class ProblemaInversiones {

  private static int mezclar(int[] a, int inf, int medio, int sup, int sum){
    int i = inf;
    int j = medio+1;
    int[] b = new int[sup-inf+1];
    int k = 0;

    int inversiones = 0;

    while(i <= medio && j <= sup) {
      if (a[i] <= a[j]) {
        b[k] = a[i];
        i++;
      } else {
        b[k] = a[j];
        inversiones += medio-i+1;
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
    return inversiones + sum;
  }
  public static int numInversiones(int[] v) {
    return numInv(v,0,v.length-1);
  }

  private static int ordenar(int[] a, int prim, int ult, int suma){
    int sum = suma;
    if(prim < ult){
      sum = ordenar(a,prim,(prim+ult)/2,sum);
      sum = ordenar(a,(prim+ult)/2+1,ult,sum);
      sum = mezclar(a,prim,(prim+ult)/2,ult,sum);
    }

    return sum;
  }

  private static int numInv(int[] a, int prim, int ult) {
    return ordenar(a,prim,ult,0);
  }

  public static void main(String[] args) {
    int[] lista =  {-2,8,4,7,3}; // -2 3 4 7 8
    int inversions = numInversiones(lista);
    System.out.println("Number of inversions: " + inversions);
  }
}

