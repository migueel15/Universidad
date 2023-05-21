
package guialturas;

import java.awt.event.ActionListener;

public interface VistaAlturas {
	//--Acciones de Botones --------
	public static final String LIMPIAR = "Limpiar";
	public static final String CARGAR = "Cargar";
	public static final String LISTADO = "Listado";
	//--Tipos de Listado------------
	public static final String NUMERO_DE_PAISES_POR_CONTINENTE = "NumeroDePaisesPorContinente";
	public static final String PAISES_POR_ALTURA = "PaisesPorAltura";
	public static final String PAISES_POR_CONTINENTE = "PaisesPorContinente";
	public static final String PAISES_POR_INICIAL = "PaisesPorInicial";
	public static final String MEDIA_POR_CONTINENTE = "MediaPorContinente";
	public static final String CONTINENTES_CON_MAS_PAISES = "ContinentesConMasPaises";
	public static final String PAISES_ORDENADOS_POR_ALTURA = "PaisesOrdenadosPorAltura";
	public static final String PAISES_POR_CONTINENTE_ALTURA = "PaisesPorContinenteAltura";
	public static final String PAISES_POR_CONTINENTE_ALTURA_DEC = "PaisesPorContinenteAlturaDec";
	//------------------------------
	/**
	 * Registrar el controlador.
	 */
	public void registrarControlador(ActionListener ctr);
	/**
	 * Devuelve el nombre del fichero de alturas a cargar.
	 * @return String con el nombre del fichero de alturas a cargar.
	 */
	public String getNombreFichero();
	/**
	 * Devuelve el nombre del tipo de listado a realizar.
	 * @return String con el nombre del tipo de listado a realizar.
	 */
	public String getTipoListado();
	/**
	 * Borra todo el contenido del campo de texto de listados.
	 */
	public void limpiar();
	/**
	 * Anyade un mensaje al campo de texto de listados.
	 * @param mensaje String con el mensaje a anyadir.
	 */
	public void anyadirTexto(String mensaje);
	/**
	 * Muestra un mensaje de error en la barra de estado.
	 * @param mensaje String con el mensaje a mostrar.
	 */
	public void error(String mensaje);
	/**
	 * Muestra un mensaje de informacion en la barra de estado.
	 * @param mensaje String con el mensaje a mostrar.
	 */
	public void ok(String mensaje);
}
