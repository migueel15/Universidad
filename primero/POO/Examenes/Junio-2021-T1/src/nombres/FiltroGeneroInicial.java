package nombres;

public class FiltroGeneroInicial implements Filtro{
  char genero;
  char inicial;
  public FiltroGeneroInicial(char genero, char inicial){
    this.genero = genero;
    this.inicial = inicial;
  }
  @Override
  public boolean criterio(Nombre nombre) {
    char genero = nombre.getGenero();
    char letra = nombre.getNombre().charAt(0);

    return this.genero == genero && this.inicial == letra;
  }
}
