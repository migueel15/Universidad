/*
 ============================================================================
 Name        : Tema2-FormaciónEquipos.c
 Author      : 
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "listaDobleCircular.h"
#include "formarEquipos.h"


int main(void) {
	TListaDoble personas,equipo1,equipo2;

	//Creamos el circulo con todas las personas, leyendolas de un fichero
	crearCirculoPersonas("personas.txt",&personas);
	printf("El numero de personas a repartir son: %d\n",longitud(personas));
	mostrarInverso(personas);
	fflush(stdout);

	//Formamos los dos equipos
	formarEquipos(&personas,&equipo1,&equipo2);

	printf("\nEl equipo 1 esta formado por ... ");
	mostrar(equipo1);

	printf("El equipo 2 esta formado por ... ");
	mostrar(equipo2);

	destruir(&personas);
	destruir(&equipo1);
	destruir(&equipo2);
}
