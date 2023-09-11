import gui.CtrIndices;
import gui.VistaIndices;
import gui.PanelIndices;

import javax.swing.*;

public class MainIndicesGUI {
	//----------------------------------------------------------------------
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	//----------------------------------------------------------------------
	private static void createGUI() {
        VistaIndices vista = new PanelIndices();
        CtrIndices ctr = new CtrIndices(vista);
		vista.controlador(ctr);
		//--------------------------
        JFrame ventana = new JFrame("Indices palabras");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane((JPanel)vista);
        ventana.pack();
        ventana.setVisible(true);
    }
}
