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
#include "listaDobleCircular.h"

int main(void) {
	TListaDoble lista;
	char persona[20];

	printf("Creando una lista vacia ...\n");
	fflush(stdout);
	crear(&lista);

	printf("Insertando elementos en la lista ...\n");
	printf("Inserta a Pedro ...\n");
	fflush(stdout);
	insertar(&lista,"Pedro");
	printf("Inserta a Antonio ...\n");
	fflush(stdout);
	insertar(&lista,"Antonio");
	printf("Inserta a Maria ...\n");
	fflush(stdout);
	insertar(&lista,"Maria");
	printf("Inserta a Fabiola ...\n");
	fflush(stdout);
	insertar(&lista,"Fabiola");

	printf("Mostrando los elementos en el orden de insercion ...\n");
	fflush(stdout);
	mostrar(lista);

	printf("Mostrando los elementos en el orden inverso al de insercion ...\n");
	fflush(stdout);
	mostrarInverso(lista);

	printf("Borramos un elemento del principio ...\n");
	fflush(stdout);
	borrarPrincipio(&lista);
	mostrarInverso(lista);

	printf("Desde el inicio, nos desplazamos una distancia 2 en el sentido de las agujas del reloj...\n");
	fflush(stdout);
	datosDistanciaD(lista,2,1,0,persona);
	printf("El nodo a distancia 2 contiene %s ...\n",persona);
	fflush(stdout);


	printf("Desde el inicio, nos desplazamos una distancia 4 en el sentido contrario al de las agujas del reloj...\n");
	fflush(stdout);
	datosDistanciaD(lista,4,2,0,persona);
	printf("El nodo a distancia 4 contiene %s ...\n",persona);
	fflush(stdout);

	printf("Borramos el elemento obtenido al desplazar ...\n");
	fflush(stdout);
	borrarNodo(&lista,persona);
	mostrar(lista);

	printf("Creamos un fichero de texto y guardamos la lista ...\n");
	fflush(stdout);
	mostrarEnFicheroTexto("personas2.txt",lista);
	mostrar(lista);

	printf("Creamos la lista a partir de un fichero de texto ...\n");
	fflush(stdout);
	destruir(&lista);
	crearDesdeFicheroTexto("personas.txt",&lista);
	mostrar(lista);

	printf("Creamos un fichero binario y guardamos la lista ...\n");
	fflush(stdout);
	mostrarEnFicheroBinario("personas.dat",lista);

	printf("Creamos la lista a partir de un fichero binario ...\n");
	fflush(stdout);
	destruir(&lista);
	crearDesdeFicheroBinario("personas.dat",&lista);
	mostrar(lista);
	destruir(&lista);
}
