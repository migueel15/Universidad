/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica0;

public abstract class Codificacion {

	/**
	 * Comprueba si el codigo es valido.
	 *
	 * @param codigo Codigo a comprobar.
	 * @return Verdadero si es Codigo es valido, falso en otro caso.
	 */
	public abstract boolean verificar(String codigo);

	/**
	 * Devuelve el Codigo valido a partir del Codigo incorrecto que recibe como
	 * parametro.
	 *
	 * @param codigo Codigo incorrecto.
	 * @return Codigo corregido.
	 */
	public String corregirControl(String codigo) {
		if (verificar(codigo))
			return codigo;
		else
			return generarCodigoControl(codigo);

	};

	/****************
	 * Genera el Codigo control a partir de los datos
	 * 
	 * @param codigo
	 * @return
	 */
	public abstract String generarCodigoControl(String codigo);

	/************
	 * Supuestamente el Codigo de control esta bien buscar donde es el error.
	 * 
	 * @param codigo
	 * @return
	 */
	public abstract String[] corregirDatos(String codigo);
}
