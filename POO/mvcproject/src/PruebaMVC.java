import javax.swing.JFrame;
import mvc.*;
public class PruebaMVC {
  public static void main(String[] args) {
    Modelo modelo = new Modelo();
    VistaImpl vista = new VistaImpl();
    Controlador ctrl = new Controlador(vista, modelo);
    vista.registrarControlador(ctrl);
    JFrame marco = new JFrame("PruebaMVC");
    marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    marco.setContentPane(vista);
    marco.pack();
    marco.setVisible(true);
  }
}
