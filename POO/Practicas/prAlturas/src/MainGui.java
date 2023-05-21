
import alturas.*;
import guialturas.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainGui {
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
		PanelAlturas vista = new PanelAlturas();
		Mundo mundo = new Mundo();
		ControladorAlturas controlador = new ControladorAlturas(vista, mundo);
		vista.registrarControlador(controlador);
		//--------------------------
		JFrame marco = new JFrame("Alturas");
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marco.setContentPane(vista);
		marco.pack();
		marco.setVisible(true);
	}
	//----------------------------------------------------------------------
}
