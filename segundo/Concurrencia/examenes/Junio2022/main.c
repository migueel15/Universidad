/*
 ============================================================================
 Name        : Main2022.c
 Author      : JBG
 Version     :
 Copyright   : Your copyright notice
 Description :
 ============================================================================
 */

#include "Incidencias.h"
#include <assert.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char const *argv[]) {

  // Comenta al gusto para probar, recuerda que el main que se usará será el que
  // se da sin tus modificaciones.

  // Inicializamos
  int t = 10;
  ListaIncidencias arrayIncidencias[t]; // No hacer caso a la advertencia del
                                        // IntelliSense de VSCode.
  inicializarListaIncidencias(arrayIncidencias, t);
  printf("Inicializado...\n\n");
  for (int i = 0; i < t; i++) {
    assert(arrayIncidencias[i] == NULL);
  }

  printf("Insertamos una primera incidencia con prioridad 0\n");
  char descripcion[200] = "Puerta 002 no cierra correctamente";
  int id = insertarIncidencia(arrayIncidencias, 0, descripcion);
  assert(id == 0);
  assert(arrayIncidencias[0] != NULL);
  assert(arrayIncidencias[0]->puntuacion == -1);
  assert(strcmp(arrayIncidencias[0]->descripcion, descripcion) ==
         0); // Son iguales...
  assert(arrayIncidencias[0]->descripcion !=
         descripcion); // ...y no apuntan al mismo sitio :)

  printf("Insertamos una segunda incidencia con prioridad 0\n");
  strcpy(descripcion, "Puerta 004 no cierra correctamente");
  int id1 = insertarIncidencia(arrayIncidencias, 0, descripcion);
  assert(id1 == 1);
  assert(arrayIncidencias[0] != NULL);
  assert(arrayIncidencias[0]->puntuacion == -1);
  assert(strcmp(arrayIncidencias[0]->siguiente->descripcion, descripcion) ==
         0); // Son iguales...
  assert(arrayIncidencias[0]->siguiente->descripcion !=
         descripcion); // ...y no apuntan al mismo sitio :)

  printf("Insertamos una tercera incidencia con prioridad 9\n");
  strcpy(descripcion, "Puerta 904 no cierra correctamente");
  int id3 = insertarIncidencia(arrayIncidencias, 9, descripcion);
  assert(id3 == 2);
  assert(arrayIncidencias[9] != NULL);
  assert(arrayIncidencias[9]->puntuacion == -1);
  assert(strcmp(arrayIncidencias[9]->descripcion, descripcion) ==
         0); // Son iguales...
  assert(arrayIncidencias[9]->descripcion !=
         descripcion); // ...y no apuntan al mismo sitio :)

  // Mostramos la lista de incidencias
  printf("Mostramos lista de incidencias:\n");
  mostrarIncidencias(arrayIncidencias, t);

  // Evaluamos incidencias
  printf("Evaluamos incidencia con id %i a 5\n", id1);
  evaluarIncidencia(arrayIncidencias, id1, 5, t);
  assert(arrayIncidencias[0] != NULL);
  assert(arrayIncidencias[0]->siguiente != NULL);
  assert(arrayIncidencias[0]->siguiente->puntuacion == 5);

  printf("Evaluamos incidencia con id %i a 100\n", id3);
  evaluarIncidencia(arrayIncidencias, id3, 100, t);
  assert(arrayIncidencias[9] != NULL);
  assert(arrayIncidencias[9]->puntuacion == 100);

  // Mostramos la lista de incidencias
  printf("Mostramos lista de incidencias:\n");
  mostrarIncidencias(arrayIncidencias, t);

  printf("Guardamos lista de incidencias:\n");
  guardarRegistroIncidencias("incidencias.txt", arrayIncidencias, t);

  printf("Cargamos datos de binario\n");
  cargarRegistroIncidencias("incidencias.bin", arrayIncidencias, t);

  printf("Mostramos lista de incidencias tras cargar de binario:\n");
  mostrarIncidencias(arrayIncidencias, t);

  return 0;
}
