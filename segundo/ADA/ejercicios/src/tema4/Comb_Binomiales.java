package tema4;

public class Comb_Binomiales {
  public static int coefBinomiales(int n, int k){
    int [][] C = new int[n+1][k+1];

    for (int i = 0; i <= n; i++){
      for(int j = 0; j <= k; j++){
        if( j == 0 || i == 0 || i == j){
          C[i][j] = 1;
        }else{
          C[i][j] = C[i-1][j-1] + C[i-1][j];
        }
      }
    }
  return C[n][k];
  }

  public static void main(String[] args) {
    int res = coefBinomiales(1,2);
    System.out.println(res);
  }

}
