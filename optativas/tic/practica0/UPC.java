/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica0;

/**
 *
 * @author
 */
public class UPC extends Codificacion {

	private static final int MULTIPLO = 10;

	/**********************
	 * 
	 * @param codigo
	 * @return
	 */
	public boolean verificar(String codigo) {
		int resultado = 0;
		int v;
		try {
			for (int i = 0; i < codigo.length(); i++) {
				v = Integer.parseInt(codigo.substring(i, i + 1));
				resultado += i % 2 == 0 ? v : v * 3;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return resultado % MULTIPLO == 0;
	}

	/**************
	 * 
	 * @param codigo
	 * @return
	 */
	public String generarCodigoControl(String codigo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String[] corregirDatos(String codigo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
