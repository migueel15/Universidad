package mvc;

import java.awt.event.ActionListener;
public interface Vista {
  public static final String SUMAR = "Sumar";
  public double consultarValor();
  public void limpiarValor();
  public void actualizarTotal(double t);
  public void mensaje(String m);
  public void registrarControlador(ActionListener ctrl);
}
