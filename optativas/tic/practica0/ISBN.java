/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package practica0;

/**
 *
 * @author
 */
public class ISBN extends Codificacion {

	private static final int MODULO = 11;

	/*************
	 * 
	 * @param codigo
	 * @return
	 */
	public boolean verificar(String codigo) {
		codigo = codigo.replaceAll("-", "");
		int resultado = 0;
		try {
			for (int i = 0; i < codigo.length(); i++) {
				resultado += Integer.parseInt(codigo.substring(i, i + 1)) * (i + 1);
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return resultado % MODULO == 0;
	}

	/***************
	 * 
	 * @param codigo
	 * @return
	 */
	@Override
	public String generarCodigoControl(String codigo) {

		String retorno;
		int resultado = 0;
		codigo = codigo.replaceAll("-", "");
		retorno = codigo;

		if (codigo.length() == 9) {
			try {
				for (int i = 0; i < codigo.length(); i++) {
					resultado += (Integer.parseInt(codigo.substring(i, i + 1)) * (i + 1));
				}
				retorno = codigo + (resultado % MODULO);
			} catch (NumberFormatException e) {
				return null;
			}
		}

		return retorno;
	}

	@Override
	public String[] corregirDatos(String codigo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
