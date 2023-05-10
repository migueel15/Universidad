package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class PanelIndices extends JPanel implements VistaIndices {
    JButton jbCrear;
    JTextArea jtaTexto;
    JTextArea jtaSalida;
    JTextField jtfDelim;
    JComboBox<String> jcbOpcion;

    public PanelIndices() {
        jbCrear = new JButton("Crear indice");
        jtaTexto = new JTextArea(10,40);
        jtaSalida = new JTextArea(15,40);
        setLayout(new BorderLayout());

        String [] opciones = {INDICEC, INDICEL, INDICEP};

        jtfDelim = new JTextField(20);
        jtfDelim.setBorder(BorderFactory.createTitledBorder("Delimitadores"));
        jcbOpcion = new JComboBox<>(opciones);
        jcbOpcion.setBorder(BorderFactory.createTitledBorder("Tipo de indice"));
        JPanel jbOpciones = new JPanel();
        jbOpciones.setLayout(new GridLayout(1,2));
        jbOpciones.add(jtfDelim);
        jbOpciones.add(jcbOpcion);
        add(jbOpciones, BorderLayout.CENTER);

        JPanel jpSalida = new JPanel();
        jpSalida.setLayout(new GridLayout(2,1));
        JScrollPane jspTexto = new JScrollPane(jtaTexto);
        jspTexto.setBorder(BorderFactory.createTitledBorder("Escribir o copiar el texto"));
        jpSalida.add(jspTexto);
        JScrollPane jspSalida = new JScrollPane(jtaSalida);
        jspSalida.setBorder(BorderFactory.createTitledBorder("Salida del indice"));
        jpSalida.add(jspSalida);
        add(jpSalida, BorderLayout.SOUTH);
        jtaSalida.setEditable(false);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.add(jbCrear);
        add(panelBotones,BorderLayout.EAST);
    }

    public List<String> listaTexto() {
        List<String> lista = new ArrayList<>();
        for (String linea : jtaTexto.getText().split("\\n"))
            lista.add(linea);
        return lista;
    }

    public void controlador(ActionListener ctr) {
        jbCrear.setActionCommand(CREAR);
        jbCrear.addActionListener(ctr);
    }

    public String opcion() {
        return (String) jcbOpcion.getSelectedItem();
    }

    public String delimitadores() {
        return jtfDelim.getText();
    }

    public void salida(String s) {
        jtaSalida.setText(s);
    }
}
