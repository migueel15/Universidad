public class DosCasos extends Metodo {
  public DosCasos(){
    super(Orden.CTE,Orden.N);
  }
  @Override
  public int codigo(int n) {
    int suma = 0;
    if (this.ordenMejorCaso().equals(Orden.CTE)) {
      suma++;
    } else {
      for (int i = 0; i < n; i++) {
        suma++;
      }
    }
    return suma;
  }
}
