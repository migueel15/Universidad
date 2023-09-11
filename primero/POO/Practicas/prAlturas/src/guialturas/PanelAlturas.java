
package guialturas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.io.PrintWriter;
import java.io.File;

public class PanelAlturas extends JPanel implements VistaAlturas, ActionListener {
	//----------------------------------------------------------------------
	private static final String[] TIPO_LISTADO = {
		NUMERO_DE_PAISES_POR_CONTINENTE,
		PAISES_POR_ALTURA,
		PAISES_POR_CONTINENTE,
		PAISES_POR_INICIAL,
		MEDIA_POR_CONTINENTE,
		CONTINENTES_CON_MAS_PAISES,
		PAISES_ORDENADOS_POR_ALTURA,
		PAISES_POR_CONTINENTE_ALTURA,
		PAISES_POR_CONTINENTE_ALTURA_DEC,
	};
	//------------------------------
	private final JFileChooser ofc;
	private JButton limpiar, cargar, listado;
	private JComboBox tipoListado;
	private JTextField filename;
	private JTextArea textArea;
	private JScrollPane scTextArea;
	private JLabel estado;
	private ActionListener externalController;
	public PanelAlturas() {
		String currentDir = System.getProperty("user.dir");
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		ofc = new JFileChooser(currentDir);
		//------------------------
		JPanel supline1 = new JPanel(new FlowLayout());
		limpiar = new JButton(LIMPIAR);
		cargar = new JButton(CARGAR);
		filename = new JTextField(29);
		filename.setEditable(false);
		supline1.add(limpiar);
		supline1.add(cargar);
		supline1.add(filename);
		//------------------------
		JPanel supline2 = new JPanel(new FlowLayout());
		tipoListado = new JComboBox<String>(TIPO_LISTADO);
		tipoListado.setMaximumRowCount(TIPO_LISTADO.length);
		tipoListado.setEditable(false);
		listado = new JButton(LISTADO);
		supline2.add(new JLabel(" TipoListado"));
		supline2.add(tipoListado);
		supline2.add(listado);
		//------------------------
		JPanel supLine = new JPanel(new BorderLayout());
		supLine.add(supline1, BorderLayout.NORTH);
		supLine.add(supline2, BorderLayout.SOUTH);
		//------------------------
		textArea = new JTextArea(5, 30);
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		//caret.setUpdatePolicy(DefaultCaret.UPDATE_WHEN_ON_EDT);
		scTextArea = new JScrollPane(textArea);
		//------------------------
		Box infLine = Box.createHorizontalBox();
		estado = new JLabel(" ");
		infLine.add(estado);
		//------------------------
		this.setLayout(new BorderLayout());
		this.add(supLine, BorderLayout.NORTH);
		this.add(scTextArea, BorderLayout.CENTER);
		this.add(infLine, BorderLayout.SOUTH);
		//--------------------------
		cargar.addActionListener(this);
	}
	//------------------------------------------------------------------
	@Override
	public void registrarControlador(ActionListener ctr) {
		externalController = ctr;
		limpiar.addActionListener(externalController);
		listado.addActionListener(externalController);
	}
	//------------------------------------------------------------------
	@Override
	public String getNombreFichero() {
		return filename.getText();
	}
	@Override
	public String getTipoListado() {
		return TIPO_LISTADO[tipoListado.getSelectedIndex()];
	}
	@Override
	public void limpiar() {
		textArea.setText("");
	}
	@Override
	public void anyadirTexto(String txt) {
		textArea.append(txt);
	}
	@Override
	public void error(String txt) {
		estado.setForeground(Color.RED);
		estado.setText(txt);
	}
	@Override
	public void ok(String txt) {
		estado.setForeground(Color.BLUE);
		estado.setText(txt);
	}
	//------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String c = e.getActionCommand();
			switch (c) {
			case CARGAR:	this.load(e);	break;
			}
		} catch (Throwable t) {
			this.error("Error: "+t.getMessage());
		}
	}
	//------------------------------------------------------------------
	private void load(ActionEvent e) throws Throwable {
		ofc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		ofc.setMultiSelectionEnabled(false);
		int returnVal = ofc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = ofc.getSelectedFile();
			filename.setText(file.getCanonicalPath());
			if (externalController != null) {
				externalController.actionPerformed(e);
			}
		} else {
			filename.setText("");
		}
	}
	//----------------------------------------------------------------------
}
