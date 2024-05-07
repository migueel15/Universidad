/*
 ============================================================================
 Nombre y Apellidos:
 DNI:
 Titulación y Grupo:
 Ordenador:
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "mundial.h"

/*
 -----------DEFINICION DE LA RUTINA main-----------------
 */

int main(void) {
  setvbuf(stdout, NULL, _IONBF, 0);
  setvbuf(stderr, NULL, _IONBF, 0);

  srand(time(NULL));

  int opcion;
  int salir = 0;

  int encontrado, mayorNumeroGoles;

  /*
   -----------------VARIABLES GLOBALES---------------------
   */
  Lista listaEquipos;
  listaEquipos = Lista_Crear();
  Lista listaGoleadores;
  listaGoleadores = Lista_Crear();

  Lista listaEquiposDescalificados;
  listaEquiposDescalificados = Lista_Crear();

  /*
   Para realizar el menu de seleccion, utilizamos un bucle while que solo
   terminara cuando la variable "salir" sea verdadera (contenga un 1), cosa que
   sucedera cuando se introduzca por teclado un valor distinto de 1-9.
   */
  while (!salir) {
    printf(
        "****************************************************************\n");
    printf(
        "*   SELECCIONE LA OPERACION QUE DESEE REALIZAR Y PULSE INTRO   *\n");
    printf(
        "****************************************************************\n");
    printf("*   '1' Crear una lista vacia\n");
    printf("*   '2' Imprimir la lista de equipos\n");
    printf("*   '3' Comprobar si un equipo esta en la lista\n");
    printf("*   '4' Agregar equipos al principio de la lista\n");
    printf("*   '5' Eliminar un equipo de la lista\n");
    printf("*   '6' Destruir la lista liberando memoria\n");
    printf("*   '7' Cargar lista de equipos\n");
    printf("*   '8' Trasladar los descalificados\n");
    printf("*   '9' Calcular el mayor numero de goles\n");
    printf("*   '10' Crear la lista ordenada de maximos goleadores\n");
    printf("*   '11' Para salir del programa\n");
    printf(
        "****************************************************************\n");
    scanf("%d", &opcion);

    switch (opcion) {
    case 1:
      listaEquipos = Lista_Crear();
      Lista_Imprimir(listaEquipos);
      break;
    case 2:
      Lista_Imprimir(listaEquipos);
      break;
    case 3:
      encontrado = Esta(listaEquipos, "UMA");
      if (encontrado) {
        printf("El equipo UMA SI esta en la lista.\n");
      } else {
        printf("El equipo UMA NO esta en la lista.\n");
      }
      encontrado = Esta(listaEquipos, "ESP");
      if (encontrado) {
        printf("El equipo ESP SI esta en la lista.\n");
      } else {
        printf("El equipo ESP NO esta en la lista.\n");
      }
      break;
    case 4:
      Lista_Agregar_Al_Principio(&listaEquipos, 0, 0, "URU");
      Lista_Agregar_Al_Principio(&listaEquipos, 0, 0, "KOR");
      Lista_Agregar_Al_Principio(&listaEquipos, 0, 0, "USA");
      Lista_Agregar_Al_Principio(&listaEquipos, 0, 1, "ING");
      Lista_Agregar_Al_Principio(&listaEquipos, 1, 5, "PAR");
      Lista_Agregar_Al_Principio(&listaEquipos, 0, 3, "JAP");
      Lista_Agregar_Al_Principio(&listaEquipos, 1, 1, "ESP");
      if (!Lista_Agregar_Al_Principio(&listaEquipos, 0, 0, "URU")) {
        printf("Uruguay ya estaba en la lista.\n");
      }
      Lista_Agregar_Al_Principio(&listaEquipos, 0, 0, "POR");
      Lista_Imprimir(listaEquipos);
      break;
    case 5:
      if (Eliminar_Equipo(&listaEquipos, "USA")) {
        printf("USA eliminado de la lista.\n");
      } else {
        printf("No se ha encontrado USA en la lista.\n");
      }

      if (Eliminar_Equipo(&listaEquipos, "UMA")) {
        printf("UMA eliminado de la lista.\n");
      } else {
        printf("No se ha encontrado UMA en la lista.\n");
      }

      if (Eliminar_Equipo(&listaEquipos, "ESP")) {
        printf("ESP eliminado de la lista.\n");
      } else {
        printf("No se ha encontrado ESP en la lista.\n");
      }
      Lista_Imprimir(listaEquipos);
      break;
    case 6:
      Lista_Destruir(&listaEquipos);
      Lista_Imprimir(listaEquipos);
      break;
    case 7:
      Lista_Destruir(&listaEquipos);
      Lista_Cargar(&listaEquipos);
      Lista_Imprimir(listaEquipos);
      break;
    case 8:
      Lista_Destruir(&listaEquipos);
      Lista_Cargar(&listaEquipos);
      Lista_Imprimir(listaEquipos);
      Trasladar_Descalificados(&listaEquipos, &listaEquiposDescalificados);
      Lista_Imprimir(listaEquiposDescalificados);
      break;
    case 9:
      Lista_Destruir(&listaEquipos);
      Lista_Cargar(&listaEquipos);
      mayorNumeroGoles = Calcular_Maximos_Goles(listaEquipos);
      printf("Mayor numero de goles: %d\n", mayorNumeroGoles);
      break;
    case 10:
      Lista_Destruir(&listaEquipos);
      Lista_Cargar(&listaEquipos);
      listaGoleadores = Crear_Lista_Maximos_Goleadores(listaEquipos);
      Lista_Imprimir(listaGoleadores);
      break;
    default:
      printf("Programa terminado.\n");
      salir = 1;
    }
  }
  /*
   Antes de salir del programa, nos aseguramos de vaciar la lista
   para que no haya fugas de memoria.
   */
  Lista_Destruir(&listaEquipos);
  Lista_Destruir(&listaEquiposDescalificados);
  Lista_Destruir(&listaGoleadores);
  return 0;
}

// Fin fichero
// ===========
