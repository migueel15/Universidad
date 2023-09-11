package guialturas;

import alturas.Mundo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class ControladorAlturas implements ActionListener {
  private VistaAlturas vista;
  private Mundo modelo;
  public ControladorAlturas(VistaAlturas vista, Mundo mundo){
    this.vista = vista;
    this.modelo = mundo;
    vista.ok("Inicio");
  }

  private void handleLimpiar(){
    vista.limpiar();
  }
  private void handleCargar() throws IOException {
    String file = vista.getNombreFichero();
    modelo.cargar(file);
  }
  private void handleListado(){
    StringWriter stwt = new StringWriter();
    Map sortedMap = null;
    Collection lista = null;

    switch (vista.getTipoListado()){
      case VistaAlturas.PAISES_POR_ALTURA:
        sortedMap = modelo.paisesPorAltura();
        break;
      case VistaAlturas.PAISES_POR_INICIAL:
        sortedMap = modelo.paisesPorInicial();
        break;
      case VistaAlturas.PAISES_ORDENADOS_POR_ALTURA:
        lista = modelo.paisesOrdenadosPorAltura();
        break;
      case VistaAlturas.CONTINENTES_CON_MAS_PAISES:
        lista = modelo.continentesConMasPaises();
        break;
      case VistaAlturas.MEDIA_POR_CONTINENTE:
        sortedMap = modelo.mediaPorContinente();
        break;
      case VistaAlturas.NUMERO_DE_PAISES_POR_CONTINENTE:
        sortedMap = modelo.numeroDePaisesPorContinente();
        break;
      case VistaAlturas.PAISES_POR_CONTINENTE:
        sortedMap = modelo.paisesPorContinente();
        break;
      case VistaAlturas.PAISES_POR_CONTINENTE_ALTURA:
        sortedMap = modelo.paisesPorContinenteAltura();
        break;
      case VistaAlturas.PAISES_POR_CONTINENTE_ALTURA_DEC:
        sortedMap = modelo.paisesPorContinenteAlturaDec();
        break;
    }
    PrintWriter pw = new PrintWriter(stwt);
    vista.anyadirTexto(vista.getTipoListado());
    vista.anyadirTexto("\n-----------------\n");
    if(sortedMap != null){
      Mundo.presentaEnPW(pw,sortedMap);
      vista.anyadirTexto(stwt.toString());
    }else{
      vista.anyadirTexto(lista.toString());
    }
    vista.anyadirTexto("\n");

  }
  @Override
  public void actionPerformed(ActionEvent e) {

    try{
      switch (e.getActionCommand()){
        case VistaAlturas.LIMPIAR:
          handleLimpiar();
          break;
        case VistaAlturas.CARGAR:
          handleCargar();
          break;
        case VistaAlturas.LISTADO:
          handleListado();
          break;

      }
      vista.ok("Operación correcta");
    } catch (Exception error){
      vista.error("Error: " + error.getMessage());
    }
  }
}
