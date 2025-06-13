package org.mps.netflix;

/**
 * Clase que representa un programa. Donde se almacena datos del mismo
 * y el minuto en el que el usuario se quedo la ultima vez. 
 *
 */
public interface Programa {
	
	/**
	 * Metodo de consulta del ID
	 * @return devuelve el id
	 */
	public int getId();
	
	/**
	 * 
	 * Metodo de consulta del Minuto Actual
	 * @return Devuelve el minuto donde el usuario se quedo viendo el programa
	 */
	public int getMinutoActual();
	
	/**
	 * Metodo de actualizacion del minutoActual
	 * @param minutoActual recibe el minuto donde el usuario se quedo viendo el programa
	 */
	public void setMinutoActual(int minutoActual);
	
	/**
	 * Metodo de actualizacion del titulo
	 * @param titulo recibe el nuevo titulo
	 */
	public void setTitulo(String titulo);

	/**
	 * Metodo de consulta del Titulo
	 * @return Devuelve el titulo de programa
	 */
	public String getTitulo();
	/**
	 * Metodo de consulta de la Duracion
	 * @return devuelve la duracion del programa
	 */
	public int getDuracion();
	
	/**
	 * Metodo de consulta del genero
	 * @return devuelve el genero del programa
	 */
	public String getGenero();
	
	/**
	 * Metodo toString
	 * @return devuelve la informacion del rograma
	 */
	public String toString() ;
	
}
