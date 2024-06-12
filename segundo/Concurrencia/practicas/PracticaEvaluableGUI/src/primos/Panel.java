package primos;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Panel extends JPanel {

	private JLabel etiqueta1 = new JLabel("¿cuántos de primos twin quieres?");
	private JTextField numero1 = new JTextField(3);
	private JLabel etiqueta2 = new JLabel("¿cuántos de primos cousin quieres?");
	private JTextField numero2 = new JTextField(3);
	private JLabel etiqueta3 = new JLabel("¿cuántos de primos sexy quieres?");
	private JTextField numero3 = new JTextField(3);
	private JLabel mensaje = new JLabel("GUI creada");
	private JTextArea listaPrimos1 = new JTextArea(10, 40);
	private JTextArea listaPrimos2 = new JTextArea(10, 40);
	private JTextArea listaPrimos3 = new JTextArea(10, 40);
	private JScrollPane scroll1 = new JScrollPane(listaPrimos1);
	private JScrollPane scroll2 = new JScrollPane(listaPrimos2);
	private JScrollPane scroll3 = new JScrollPane(listaPrimos3);
	private JLabel mensaje1 = new JLabel("Area  primos 'twin' creada");
	private JLabel mensaje2 = new JLabel("Area  primos 'cousin' creada");
	private JLabel mensaje3 = new JLabel("Area  primos 'sexy' creada");
	private JButton fin = new JButton("Cancelar");

	private JProgressBar progreso1 = new JProgressBar(0, 100);
	private JProgressBar progreso2 = new JProgressBar(0, 100);
	private JProgressBar progreso3 = new JProgressBar(0, 100);

	// Ya implementado el constructor y los atributos.
	public Panel() {
		this.setLayout(new BorderLayout());
		JPanel norte = new JPanel();
		// norte.add(etiqueta);norte.add(numero);
		norte.add(fin);
		JPanel centro = new JPanel();
		centro.setLayout(new GridLayout(1, 3));
		progreso1.setValue(0);
		progreso1.setStringPainted(true);
		progreso2.setValue(0);
		progreso2.setStringPainted(true);
		progreso3.setValue(0);
		progreso3.setStringPainted(true);
		JPanel izdarriba = new JPanel();
		izdarriba.add(etiqueta1);
		izdarriba.add(numero1);
		izdarriba.add(progreso1);
		JPanel centroarriba = new JPanel();
		centroarriba.add(etiqueta2);
		centroarriba.add(numero2);
		JPanel dcharriba = new JPanel();
		dcharriba.add(etiqueta3);
		dcharriba.add(numero3);
		JPanel izquierda = new JPanel();
		izquierda.setLayout(new BorderLayout());

		izquierda.add(BorderLayout.NORTH, izdarriba);
		izquierda.add(BorderLayout.CENTER, scroll1);
		JPanel izqabajo = new JPanel();
		izqabajo.add(mensaje1);
		izqabajo.add(progreso1);
		izquierda.add(BorderLayout.SOUTH, izqabajo);
		JPanel centro1 = new JPanel();
		centro1.setLayout(new BorderLayout());
		centro1.add(BorderLayout.NORTH, centroarriba);
		centro1.add(BorderLayout.CENTER, scroll2);
		JPanel centroAbajo = new JPanel();
		centroAbajo.add(mensaje2);
		centroAbajo.add(progreso2);
		centro1.add(BorderLayout.SOUTH, centroAbajo);
		JPanel derecha = new JPanel();
		derecha.setLayout(new BorderLayout());
		derecha.add(BorderLayout.NORTH, dcharriba);
		derecha.add(BorderLayout.CENTER, scroll3);
		JPanel dchaAbajo = new JPanel();
		dchaAbajo.add(mensaje3);
		dchaAbajo.add(progreso3);
		derecha.add(BorderLayout.SOUTH, dchaAbajo);
		centro.add(izquierda);
		centro.add(centro1);
		centro.add(derecha);
		this.add(BorderLayout.NORTH, norte);
		this.add(BorderLayout.CENTER, centro);
		this.add(BorderLayout.SOUTH, mensaje);
	}

	// Debes registrar el ctr como observador de los botones y JTextField
	// correspondiendes
	public void controlador(ActionListener ctr) {
		fin.addActionListener(ctr);
		fin.setActionCommand("fin");

		numero1.addActionListener(ctr);
		numero1.setActionCommand("numero1");

		numero2.addActionListener(ctr);
		numero2.setActionCommand("numero2");

		numero3.addActionListener(ctr);
		numero3.setActionCommand("numero3");
	}

	// Devuelve el entero que tiene el JTextField numero 1
	public int numero1() {
		return Integer.parseInt(numero1.getText());
	}

	// Devuelve el entero que tiene el JTextField numero 2
	public int numero2() {
		return Integer.parseInt(numero2.getText());
	}

	// Devuelve el entero que tiene el JTextField numero 3
	public int numero3() {
		return Integer.parseInt(numero3.getText());
	}

	// Añade a JTextArea listaPrimos1 la lista que se le pasa.
	// Recuerda meter retornos de carro para que salga como en la captura de
	// pantalla
	public void escribePrimosTwin(List<Primos> list) {
		int contador = 1;
		for (int i = 0; i < list.size(); i++) {
			listaPrimos1.append(list.get(i).toString() + " ");

			// si es multiplo de 5 añado retorno de carro
			if (contador == 5) {
				listaPrimos1.append("\n");
				contador = 1;
			} else {
				contador++;
			}
		}
	}

	// Añade a JTextArea listaPrimos2 la lista que se le pasa.
	// Recuerda meter retornos de carro para que salga como en la captura de
	// pantalla
	public void escribePrimosCousin(List<Primos> list) {
		int contador = 1;
		for (int i = 0; i < list.size(); i++) {
			listaPrimos2.append(list.get(i).toString() + " ");

			// si es multiplo de 5 añado retorno de carro
			if (contador == 5) {
				listaPrimos2.append("\n");
				contador = 1;
			} else {
				contador++;
			}
		}
	}

	// Añade a JTextArea listaPrimos3 la lista que se le pasa.
	// Recuerda meter retornos de carro para que salga como en la captura de
	// pantalla

	public void escribePrimosSexy(List<Primos> list) {
		int contador = 1;
		for (int i = 0; i < list.size(); i++) {
			listaPrimos3.append(list.get(i).toString() + " ");

			// si es multiplo de 5 añado retorno de carro
			if (contador == 5) {
				listaPrimos3.append("\n");
				contador = 1;
			} else {
				contador++;
			}
		}
	}

	// Limpia el JTextArea listaPrimos1
	public void limpiaAreaTwin() {
		listaPrimos1.setText("");
	}

	// Limpia el JTextArea listaPrimos2
	public void limpiaAreaCousin() {
		listaPrimos2.setText("");
	}

	// Limpia el JTextArea listaPrimos3
	public void limpiaAreaSexy() {
		listaPrimos3.setText("");
	}

	// Establece mensaje que se le pasa el JLabel mensaje
	public void mensaje(String str) {
		mensaje.setText(str);
	}

	// Establece mensaje que se le pasa el JLabel mensaje1
	public void mensajeTwin(String str) {
		mensaje1.setText(str);
	}

	// Establece mensaje que se le pasa el JLabel mensaje2
	public void mensajeCousin(String str) {
		mensaje2.setText(str);
	}

	// Establece mensaje que se le pasa el JLabel mensaje3
	public void mensajeSexy(String str) {
		mensaje3.setText(str);
	}

	// Establece el nivel de progreso n en el JProgressBar progreso1
	public void progreso1(int n) {
		progreso1.setValue(n);
	}

	// Establece el nivel de progreso n en el JProgressBar progreso2
	public void progreso2(int n) {
		progreso2.setValue(n);
	}

	// Establece el nivel de progreso n en el JProgressBar progreso3
	public void progreso3(int n) {
		progreso3.setValue(n);
	}
}
