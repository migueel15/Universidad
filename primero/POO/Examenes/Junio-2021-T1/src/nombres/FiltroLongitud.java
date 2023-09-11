package nombres;

public class FiltroLongitud implements Filtro{
  private int longitud;
  public FiltroLongitud(int longitud){
    this.longitud = longitud;
  }
  @Override
  public boolean criterio(Nombre nombre) {
    return nombre.getNombre().length() == longitud;
  }
}
