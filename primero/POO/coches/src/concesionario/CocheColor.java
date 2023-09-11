package concesionario;
import java.util.Arrays;

public class CocheColor extends Coche{
  private static final String[] COLORES = {"Negro", "Rojo", "Verde", "Azul", "Amarillo", "Blanco"};
  private static final double[] PRECIOS = {0.0, 10.0, 20.0, 30.0, 40.0, 50.0};
  private int coloridx;

  public CocheColor(String modelo, double precio){
    this(modelo, precio, COLORES[0]);
  }

  private static int buscarColorIdx(String color){
    int index = -1;
    for(int i = 0; i< COLORES.length && index < 0; i++){
      if(color.equalsIgnoreCase(COLORES[i])){
        index = i;
      }
    }
    return index > 0 ? index : 0;
  }
  public CocheColor(String modelo, double precio, String color) {
    super(modelo, precio);
    coloridx = buscarColorIdx(color);
  }

  public String getColor(){
    return COLORES[coloridx];
  }
  public void setColor(String color){
    coloridx = buscarColorIdx(color);
  }

  @Override
  public String getModelo() {
    return super.getModelo() + " " + this.getColor();
  }

  protected double getPrecioColor(){
    return PRECIOS[coloridx];
  }

  @Override
  protected double getPrecioBase() {
    return super.getPrecioBase() + getPrecioColor();
  }

  public static String[] coloresDisponibles(){
    return Arrays.copyOf(COLORES,COLORES.length);
  }
}
