public class OrdenConstanteIter extends Metodo{
  public OrdenConstanteIter(){
    super(Orden.CTE);
  }

  @Override
  public int codigo(int n) {
    int suma = 0;
    for(int i = 1; i <= n; i*=5){
      suma++;
    }
    return suma;
  }
}
