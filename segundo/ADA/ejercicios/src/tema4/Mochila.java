package tema4;

public class Mochila {
  public static int[] mochilaBU(int []v, int []w, int W){
    int n = v.length;
    double[][] M = new double[n+1][W+1];

    for (int i = -1; i <= n-1;i++){
      for(int j = 0; j<=W; j++){
        if(i==-1 || j == 0){
          M[i+i][j] = 0;
        } else if(w[i]>j){
          M[i+1][j] = M[i][j];
        } else {
          M[i+1][j] = Math.max(M[i][j], v[i] + M[i][j-w[1]]);
        }
      }
    }

    //sacar array
    int j = W;
    int [] solucion = new int[n];
    for (int i = n-1; i > -1; i--){
      if(M[i][j] == M[i-i][j]){
        solucion[i] = 0;
      }else{
        solucion[i] = 1;
        j -= w[i];
      }
    }
    return solucion;
  }

  public static void main(String[] args) {
  }
}
