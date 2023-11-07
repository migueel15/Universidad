package primerParcial;

public class Main {
  public static void ElemPerdido(int[]a, int k){
    ElemPerdido(a,0,a.length-1,k);
  }

  public static int ElemPerdido(int[]a, int inf, int sup, int k){
    if(sup < inf){return -1;}
    int pos = (inf + sup -1)/2;

    if(a[pos] != pos * k){
      if(a[pos-1] == (pos-1)*k){
        return pos * k;
      }else{
        return ElemPerdido(a, inf, pos+1, k);
      }
    }else{
      if(pos == sup){return -1;}
      return ElemPerdido(a, pos+1, sup, k);
    }
  }

  public static void main(String[] args) {
    int[] a = {0,2,4,6,10,12};
    System.out.println(ElemPerdido(a,0,a.length-1,2));
  }
}
