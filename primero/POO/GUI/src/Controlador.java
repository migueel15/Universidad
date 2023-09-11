import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador implements ActionListener {
  private Modelo modelo;
  private Vista vista;
  public Controlador(Vista v, Modelo m) {
    this.vista = v;
    this.modelo = m;
    vista.registrarControlador(this);
  }
  private void accionSumar(){
    double valor = vista.getValor();
    modelo.incTotal(valor);
    vista.setTotal(modelo.getTotal());
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      String nombre = e.getActionCommand();
      switch (nombre) {
        case Vista.SUMAR:
          accionSumar();
          break;
        default:
          throw new RuntimeException("Operacion erronea");
      }
      vista.mensaje("Operacion correcta");
    }catch (Exception error){
      vista.mensaje("Error");
  }
}
}
