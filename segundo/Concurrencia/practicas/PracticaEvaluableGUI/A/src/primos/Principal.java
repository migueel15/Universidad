package primos;

import javax.swing.*;

public class Principal {
  public static void crearGUI() {
    System.out.println("crearGUI() - isEventDispatchThread? " +
                       SwingUtilities.isEventDispatchThread());
    JFrame ventana = new JFrame("Numeros primos");
    Panel panel = new Panel();
    Controlador ctr = new Controlador(panel);
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
