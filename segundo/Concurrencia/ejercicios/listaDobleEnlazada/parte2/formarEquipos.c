/*
 * formarEquipos.c
 *
 *  Created on: 21 mar. 2021
 *      Author: Monica
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "listaDobleCircular.h"
#include "formarEquipos.h"

/** ¡¡IMPORTANTE!!
 *
 *   En este modulo se utiliza el modulo "listaDobleCircular".
 *
 *   No podemos asumir como está implementada la lista.
 *   Solo conocemos la interfaz
 *
 *   Por ello:
 *   	 - Solo es posible utilizar las funciones en el fichero listaDobleCircular.h
 *       - No esta permitido manejar la lista directamente a a través de sus punteros.
 */

//Funcion privada para calcular la distancia de forma aleatoria
int calcularDistancia(){
	//Se obtiene un numero aleatorio entre 0 y 6
	return (rand() % 7);
}

/*Crea una lista enlazada doblemente circular con las personas
	incluidas en el fichero de texto pasado como parametro.
	Mirar fichero personas.txt proporcionado
*/
void crearCirculoPersonas(char *nombre,TListaDoble *personas){

}

/*Forma dos equipos a partir de la lista de personas en el primer parámetro
 * Como salida se devuelven los dos equipos como listas enlazadas doblemente
 * enlazada y circular.
 *
 * equipo1 y equipo2 podrían ser listas simples, pero para evitar implementar
 * tambien el modulo con la lista simple, todas las listas son del mismo tipo
 */
void formarEquipos(TListaDoble *personas, TListaDoble *equipo1, TListaDoble *equipo2){
	//En la implementacion de esta funcion SOLO se hacen llamadas a las funciones
	//de manejo de la lista enlazada definidas en el fichero listaDobleCircular.h

	//Generacion de numeros aleatorios: Para crear una semilla distinta en cada ejecución
	//Solo se debe llamar una vez. Por eso no se invoca dentro de la funcion calcularDistancia()
	srand (time(NULL));

	/* Hay que implementar lo siguiente:
	 *   Calculamos la distancia de forma aleatoria
	 *   Obtenemos la persona que se encuentra a distancia D desde el comienzo de la lista
	 *   en el sentido de las agujas de reloj
	 *   Insertamos esa persona en el equipo1
	 *   Borramos a esa persona de la lista
	 *   Calculamos la posición de inicio para seguir seleccionando a la siguiente persona
	 *   Se puede hacer con:
	 *
	 *   int l = longitud(*personas);
		 if (l != 0){ //Para evitar division por 0
			ini = (ini+d)%l; //Calculamos el nuevo inicio desplazandonos a la derecha (entre 0 y el numero de elementos en la lista)
		 }
	 *
	 *   Repetimos lo mismo (calcular distancia, obtener persona, insertar y borrar) pero
	 *   moviendonos en el sentido contrario a las agujas del reloj
	 *   Calculamos la posición de inicio para seguir seleccionando a la siguiente persona
	 *   Se puede hacer con:
	 *
	 *   int l = longitud(*personas);
		 if (l !=0){
		 	ini = (ini-d+l)%l; //Nos desplazamos a la izquierda (entre 0 y el numero de elementos en la lista
		 	                   //evitamos que nos de un numero negativo.
		 }
	 */
}
