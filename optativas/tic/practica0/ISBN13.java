/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica0;

/**
 *
 * @author monte
 */
public class ISBN13 extends Codificacion {

	private static final int MODULO = 10;

	/**************
	 * 
	 */
	public boolean verificar(String codigo) {
		codigo = codigo.replaceAll("-", "");
		int resultado = 0;
		try {
			for (int i = 0; i < codigo.length(); i++) {
				if (i % 2 == 0)
					resultado += (Integer.parseInt(codigo.substring(i, i + 1)) * 1);
				else
					resultado += (Integer.parseInt(codigo.substring(i, i + 1)) * 3);
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return resultado % MODULO == 0;
	}

	/***********
	 * 
	 */
	public String generarCodigoControl(String codigo) {

		String retorno;
		codigo = codigo.replaceAll("-", "");
		retorno = codigo;
		int resultado = 0;
		if (codigo.length() == 12) {
			try {
				for (int i = 0; i < codigo.length(); i++) {
					if (i % 2 == 0)
						resultado += (Integer.parseInt(codigo.substring(i, i + 1)) * 1);
					else
						resultado += (Integer.parseInt(codigo.substring(i, i + 1)) * 3);
				}
			} catch (NumberFormatException e) {
				return null;
			}

			resultado = resultado % MODULO;
			resultado = (MODULO - resultado) % MODULO;
			retorno = codigo + resultado;
		}
		return retorno;
	}

	/******************
	 * 
	 */
	public String[] corregirDatos(String codigo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
