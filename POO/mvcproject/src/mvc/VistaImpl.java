package mvc;

import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.*;
public class VistaImpl extends JPanel implements Vista {
  private JButton sumar;
  private JTextField valor, total;
  private JLabel etq, msg;
  public VistaImpl() {
// construir los componentes visuales
    sumar = new JButton(Vista.SUMAR);
    valor = new JTextField(10);
    etq = new JLabel("Total: ");
    msg = new JLabel(" ");
    total = new JTextField(10);
    total.setEditable(false);
    total.setText("0.0");
//----------------------------
    JPanel panelSuperior = new JPanel();
    panelSuperior.setLayout(new FlowLayout());
    panelSuperior.add(sumar);
    panelSuperior.add(valor);
    panelSuperior.add(etq);
    panelSuperior.add(total);
//----------------------------
    this.setLayout(new BorderLayout());
    this.add(panelSuperior, BorderLayout.CENTER);
    this.add(msg, BorderLayout.SOUTH);
  }
  // Implementación de la interfaz
  @Override
  public double consultarValor() {
    return Double.parseDouble(valor.getText());
  }
  @Override
  public void limpiarValor() {
    valor.setText("");
  }
  @Override
  public void actualizarTotal(double t) {
    total.setText(Double.toString(t));
  }
  @Override
  public void mensaje(String m) {
    msg.setText(m);
  }
  @Override
  public void registrarControlador(ActionListener ctrl) {
    sumar.addActionListener(ctrl);
    sumar.setActionCommand(Vista.SUMAR);
    valor.addActionListener(ctrl);
    valor.setActionCommand(Vista.SUMAR);
  }
}