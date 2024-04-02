/*
 ============================================================================
 Nombre y Apellidos:
 DNI:
 Titulación y Grupo:
 Ordenador:
 ============================================================================
 */

#ifndef MUNDIAL_H_
#define MUNDIAL_H_
#include <stdio.h>
#include <stdlib.h>

#define MAX_CADENA 33

/*
 -----------DEFINICION DE ALIAS DE TIPOS-----------------
 */

typedef struct Nodo {
  int victorias;
  int goles;
  char nombre[MAX_CADENA];
  struct Nodo *sig;
} Equipo;

typedef Equipo *Lista;

/*
 -----------------PROTOTIPOS DE RUTINAS------------------
 */

/**
 * Crea una lista vacia.
 */
Lista Lista_Crear();

/**
 * Imprime por consola el contenido de cada uno de los nodos de la lista.
 */
void Lista_Imprimir(Lista lista);

/**
 * Comprueba si en la lista hay un nodo cuyo nombre coincida con
 * el que se pasa como parametro.
 * Devuelve 1 si lo encuentra, 0 en otro caso.
 */
int Esta(Lista lista, char *elem);

/**
 * Recibe la información para insertar un nuevo equipo.
 * Si el equipo no está en la lista, lo inserta al PRINCIPIO
 * de la lista y se devuelve un 1.
 * En otro caso, no se inserta y se devuelve un 0.
 */
int Lista_Agregar_Al_Principio(Lista *lista, int vict, int goles, char *nom);

/**
 * Elimina de la lista el equipo cuyo nombre coincida con el que
 * se pasa como parametro y se devuelve un 1.
 * Si no se encuentra el equipo, no hace nada y devuelve un 0.
 */
int Eliminar_Equipo(Lista *lista, char *nombre);

/**
 * Elimina todos los nodos de la lista, liberando toda la memoria.
 */
void Lista_Destruir(Lista *lista);

/**
 * Carga desde el fichero resultadosOctavos.txt
 * en modo texto el contenido de la lista de equipos
 * con los resultados de esa fase.
 */
void Lista_Cargar(Lista *lista);

/**
 * Traslada de la lista que recibe como primer parametro
 * a la lista que recibe como segundo parametro, los
 * nodos cuyo valor de victorias sea cero.
 * Además, los elimina de la primera lista.
 */
void Trasladar_Descalificados(Lista *lista, Lista *listaDescalificados);

/**
 * Calcula y devuelve el recuento de goles acumulados
 * por el equipo mas anotador de la fase de OCTAVOS.
 */
int Calcular_Maximos_Goles(Lista lista);

/**
 * Genera una nueva lista enlazada de equipos ordenada en base
 * al número de goles anotados, de MENOR a MAYOR.
 * Es decir, el primer nodo será el equipo menos goleador y el
 * último, el equipo más goleador.
 * Si hay equipos con el mismo número de goles anotados,
 * aparecerán consecutivos pero no importa el orden entre ellos.
 * Devuelve la lista generada.
 */
Lista Crear_Lista_Maximos_Goleadores(Lista lista);

#endif /* MUNDIAL_H_ */

// Fin fichero
// ===========
