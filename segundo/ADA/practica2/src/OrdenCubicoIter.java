public class OrdenCubicoIter extends Metodo{
  @Override
  public int codigo(int n) {
    int suma = 0;
    for(int i = 0; i <= n; i++){
      for(int j = 0; j <= n; j++){
        for(int k = 0; k <= n; k++){
          suma++;
        }
      }
    }
    return suma;
  }
}
