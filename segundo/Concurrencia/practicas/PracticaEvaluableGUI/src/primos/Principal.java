package primos;

import javax.swing.*;

public class Principal {
  public static void crearGUI() {
    System.out.println("crearGUI() - isEventDispatchThread? " +
                       SwingUtilities.isEventDispatchThread());
    JFrame ventana = new JFrame("Numeros primos");
    Panel panel = new Panel();
    ControladorBarra ctrBarra1 = new ControladorBarra(panel);
    ControladorBarra ctrBarra2 = new ControladorBarra(panel);
    ControladorBarra ctrBarra3 = new ControladorBarra(panel);
    Controlador ctr = new Controlador(panel, ctrBarra1, ctrBarra2, ctrBarra3);

    panel.controlador(ctr);
    ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ventana.setContentPane(panel);
    ventana.pack();
    ventana.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          crearGUI();
        } catch (Exception e) {
          System.out.println("Tarea cancelada");
        }
      }
    });
  }
}
