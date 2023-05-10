package gui;

import java.awt.event.ActionListener;
import java.util.List;

public interface VistaIndices {
    String CREAR = "Crear";
    String INDICEC = "Contador";
    String INDICEL = "Lineas";
    String INDICEP = "Posicion";
    List<String> listaTexto();
    void controlador(ActionListener ctr);
    String opcion();
    String delimitadores();
    void salida(String texto);
}
