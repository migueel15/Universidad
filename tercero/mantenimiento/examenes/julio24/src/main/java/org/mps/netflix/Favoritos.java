/**
 * 
 */
package org.mps.netflix;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa una lista de programas y
 * permite hacer seguimiento del punto en el que se quedo viendolo el usuario.
 *
 */
public class Favoritos {
	private List<Programa> programas;

	/**
	 * Constructor que crea una lista vacia de programas
	 */
	public Favoritos() {
		this.programas = new ArrayList<>();
	}

	/**
	 * Permite añadir un programa a la lista, pero si el programa ya existe
	 * lo actualiza, si no lo añade al final.
	 * La comparacion se realiza por el id del programa.
	 * 
	 * @param programa El programa a añadir o modificar.
	 * @return lanza una excepcion si no existe
	 */
	public void nuevoPrograma(Programa programa) {
		if (programa == null) {
			throw new RuntimeException("Informacion no validad");
		}
		int pos = buscarPrograma(programa.getId());
		if (pos != -1) {
			programas.set(pos, programa);
		} else {
			programas.add(programa);
		}
	}

	/**
	 * Permite actualizar el punto en el que el usuario se quedó visualizandolo.
	 * 
	 * @param id Identificador del programa
	 * @param minutoActual Minuto en el que el usuario ha dejado de ver el programa.
	 * @return lanza una excepcion si no existe

	 */
	public void actualizarMinuto(int id, int minutoActual) {
		int pos = buscarPrograma(id);
		if (pos != -1) {
			programas.get(pos).setMinutoActual(minutoActual);
		}else{
			throw new RuntimeException("El programa no existe");
		}
	}

	/**
	 * Metodo de consulta obtener el tamaño de la lista de programas favoritos del usuario
	 * 
	 * @return devuelve el tamaño de la lista de programas favoritos del usuario
	 */
	public int totalProgramas() {
		return programas.size();
	}

	/**
	 * Metodo que actualiza los titulos de un programa
	 * 
	 * @param patron 
	 * @param id Identificador del programa a consultar
	 * @return Devuelve el minuto donde se qued� el usuario o -1 si el programa no
	 *         est� en la lista.
	 */
	public void actualizarTitulosConPatron(String patron, String titulo) {
		for (Programa p: this.programas){
			if (p.getTitulo().contains(patron)){
				p.setTitulo(titulo);
			}
		}
	}

	/**
	 * Metodo de consulta para recuperar toda la informacion de un programa.
	 * 
	 * @param id Identificador del programa a consultar
	 * @return devuelve el programa o lanza una excepcion si no existe
	 */
	public Programa verPrograma(int id) {
		int pos = buscarPrograma(id);
		if (pos == -1) {
			throw new RuntimeException("Ese programa no existe en la lista");
		}
		return programas.get(pos);
	}

	/**
	 * Elimina un programa
	 * 
	 * @param id Identificador del programa a eliminar
	 * @return Devuelve verdadero si el programa se elimin� correctamente
	 */
	public boolean eliminarPrograma(int id) {
		boolean eliminado = false;
		int pos = buscarPrograma(id);
		if (pos != -1) {
			programas.remove(pos);
			eliminado = true;
		}
		return eliminado;
	}

	/**
	 * M�todo intrno que busca en la estructura un programa a partir de su id
	 * 
	 * @param id Identificador del programa a buscar
	 * @return devuelve la posici�n en la que se encuentra el programa en la
	 *         estructura
	 */
	private int buscarPrograma(int id) {
		int pos = -1;
		int i = 0;
		while (i < programas.size() && pos == -1) {
			if (programas.get(i).getId() == id) {
				pos = i;
			}
			i++;
		}
		return pos;

	}

	/**
	 * Analiza todos los programas favoritos para ver cuales ha empezado a ver el
	 * usuario
	 * 
	 * @return devuelve la lista con los programas que ya ha empezado a ver.
	 */
	public List<Programa> empezados() {
		List<Programa> lista = new ArrayList<>();
		Programa prog;
		for (int i = 0; i < programas.size(); i++) {
			prog = this.programas.get(i);
			if (prog.getMinutoActual() > 0) {
				lista.add(prog);
			}

		}
		return lista;
	}
}
