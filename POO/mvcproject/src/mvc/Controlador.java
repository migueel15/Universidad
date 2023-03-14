package mvc;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Controlador implements ActionListener {
  private Vista vista;
  private Modelo modelo;
  public Controlador(Vista v, Modelo m) {
    vista = v;
    modelo = m;
    vista.limpiarValor();
    vista.mensaje("Inicio");
// vista.registrarControlador(this); // realizado en MainApp
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      String c = e.getActionCommand();
      switch (c) {
        case Vista.SUMAR: sumarValor(); break;
        default: vista.mensaje("Error: Comando desconocido "+c); break;
      }
    } catch (NumberFormatException ex) {
      vista.mensaje("Error: "+ex.getMessage());
    } catch (Exception ex) {
      vista.mensaje("Error: "+ex.toString());
    }
  }
  private void sumarValor() {
    modelo.sumarValor(vista.consultarValor());
    vista.actualizarTotal(modelo.consultarTotal());
    vista.limpiarValor();
    vista.mensaje("Operación correcta");
  }
}