/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package practica0;

/**
 *
 * @author
 */
public class NIF extends Codificacion {

	private static final String NIF_TABLA = "TRWAGMYFPDXBNJZSQVHLCKE";

	@Override
	public boolean verificar(String nif) {
		int dni = Integer.parseInt(nif.substring(0, nif.length() - 1));
		try {
			return NIF_TABLA.charAt(dni % NIF_TABLA.length()) == nif.charAt(nif.length() - 1);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/*************
	 * 
	 * @param codigo
	 * @return
	 */
	public String generarCodigoControl(String codigo) {

		if (codigo != null) {
			int dni = Integer.parseInt(codigo, 10);
			return codigo + NIF_TABLA.charAt(dni % NIF_TABLA.length());
		} else
			return null;
	}

	public String[] corregirDatos(String codigo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
