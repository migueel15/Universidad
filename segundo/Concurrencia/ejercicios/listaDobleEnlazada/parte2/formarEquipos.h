/*
 * formarEquipos.h
 *
 *  Created on: 21 mar. 2021
 *      Author: Monica
 */

#ifndef FORMAREQUIPOS_H_
#define FORMAREQUIPOS_H_

/*Crea una lista enlazada doblemente circular con las personas
	incluidas en el fichero de texto pasado como parametro.
	Mirar fichero personas.txt proporcionado
*/
void crearCirculoPersonas(char *nombre,TListaDoble *personas);

/*Forma dos equipos a partir de la lista de personas en el primer parámetro
 * Como salida se devuelven los dos equipos como listas enlazadas doblemente
 * enlazada y circular.
 *
 * equipo1 y equipo2 podrían ser listas simples, pero para evitar no tener
 * que implementar los dos módulos, aquí todas las listas son del mismo tipo
 */
void formarEquipos(TListaDoble *personas, TListaDoble *equipo1, TListaDoble *equipo2);

#endif /* FORMAREQUIPOS_H_ */
