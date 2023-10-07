public class OrdenNLogIter extends Metodo {
  @Override
  public int codigo(int n) {
    int suma = 0;
    for (int i = 1; i <= n; i++) {
      int j = 1;
      while (j <= n) {
        suma++;
        j *= 2;
      }
    }
    return suma;
  }
}
