public class OrdenCuadradoIter extends Metodo{
  public OrdenCuadradoIter(){
    super(Orden.N2);
  }
  @Override
  public int codigo(int n) {
    int suma = 0;
    for(int i = 0; i <= n; i++){
      for(int j = 0; j <= n; j++){
        suma++;
      }
    }
    return suma;
  }


}
