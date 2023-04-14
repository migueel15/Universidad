import java.awt.event.ActionListener;

public interface Vista {
  public static String SUMAR = "Sumar";
  public void registrarControlador(ActionListener c);
  public double getValor();
  public void setTotal(double valor);
  public void mensaje(String m);
}
