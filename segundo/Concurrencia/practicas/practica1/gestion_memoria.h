/*
 * gestion_memoria.h

 *
 *  Created on: dd/mm/aaaa
 *  Author: name
 */

#ifndef _GESTION_MEMORIA_
#define _GESTION_MEMORIA_

typedef struct T_Nodo *T_Manejador;

struct T_Nodo {
  unsigned inicio;
  unsigned fin;
  T_Manejador sig;
};

/* Crea la estructura utilizada para gestionar la memoria disponible.
 * Inicialmente, sólo un nodo desde 0 a MAX
 */
void crear(T_Manejador *manejador);

/* Destruye la estructura utilizada (libera todos los nodos de la lista.
 * El parámetro manejador debe terminar apuntando a NULL
 */
void destruir(T_Manejador *manejador);

/* Devuelve en “dir” la dirección de memoria “simulada” (unsigned) donde
 * comienza el trozo de memoria continua de tamaño “tam” solicitada. Si la
 * operación se pudo llevar a cabo, es decir, existe un trozo con capacidad
 * suficiente, devolvera TRUE (1) en “ok”; FALSE (0) en otro caso.
 */
void obtener(T_Manejador *manejador, unsigned tam, unsigned *dir, unsigned *ok);

/* Muestra el estado actual de la memoria, bloques de memoria libre */
void mostrar(T_Manejador manejador);

/* Devuelve el trozo de memoria continua de tamaño “tam” y que
 * comienza en “dir”.
 * Se puede suponer que se trata de un trozo obtenido previamente.
 */
void devolver(T_Manejador *manejador, unsigned tam, unsigned dir);

#endif
