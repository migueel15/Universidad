package gui;

import indices.Indice;
import indices.IndiceContador;
import indices.IndiceLineas;
import indices.IndicePosicionesEnLineas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class CtrIndices implements ActionListener {
  private VistaIndices vista;
  private Indice modelo;
  public CtrIndices(VistaIndices vista){
    this.vista = vista;
  }

  private void handleIndiceContador(){
    modelo = new IndiceContador();
  }
  private void handleIndiceLineas(){
    modelo = new IndiceLineas();
  }
  private void handleIndicePosiciones(){
    modelo = new IndicePosicionesEnLineas();
  }
  private void handleCrear() throws FileNotFoundException {
    String delimiter = vista.delimitadores();
    if(delimiter.equals("")){
      delimiter = "[ .,:;\\\\-\\\\!\\\\¡\\\\¿\\\\?]+";
    }
    List<String> texto = vista.listaTexto();
    for(String linea : texto){
      modelo.agregarFrase(linea);
    }
    modelo.resolver(delimiter);
    StringWriter textoCreado = new StringWriter();
    PrintWriter pw = new PrintWriter(textoCreado);
    modelo.presentarIndice(pw);
    vista.salida(textoCreado.toString());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String accion = e.getActionCommand();
    if(accion.equalsIgnoreCase(VistaIndices.CREAR)){
      try{
        switch (vista.opcion()){
          case VistaIndices.INDICEC:
            handleIndiceContador();
            break;
          case VistaIndices.INDICEL:
            handleIndiceLineas();
            break;
          case VistaIndices.INDICEP:
            handleIndicePosiciones();
            break;
        }
        handleCrear();
      }catch (Exception error){
        vista.salida(error.getMessage());
      }
    }
  }
}
