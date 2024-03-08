#include "gestion_memoria.h"
#include <stdio.h>
#include <stdlib.h>

const unsigned MAX_MEMORY = 1000;

void crear(T_Manejador *manejador) {
  *manejador = malloc(sizeof(struct T_Nodo));
  if (*manejador != NULL) {
    (*manejador)->inicio = 0;
    (*manejador)->fin = MAX_MEMORY - 1;
    (*manejador)->sig = NULL;
  }
}

void destruir(T_Manejador *manejador) {
  while (*manejador != NULL) {
    T_Manejador aux = *manejador;
    *manejador = (*manejador)->sig;
    free(aux);
  }
}

void obtener(T_Manejador *manejador, unsigned tam, unsigned *dir,
             unsigned *ok) {
  T_Manejador ant = NULL;
  T_Manejador ptr = *manejador;

  *ok = 0;
  while (!(*ok) && ptr != NULL) {
    unsigned tam_actual = ptr->fin - ptr->inicio + 1;
    if (tam <= tam_actual) {
      *ok = 1;
      *dir = ptr->inicio;
      unsigned restante = tam - tam_actual;
      if (restante > 0) {
        ptr->inicio += tam;
      } else {
        // eliminar nodo
        if (ant == NULL) { // cabeza
          (*manejador) = (*manejador)->sig;
        } else {
          ant->sig = ptr->sig;
        }
        free(ptr);
      }
    } else {
      // siguiente hueco
      ant = ptr;
      ptr = ptr->sig;
    }
  }
}

void mostrar(T_Manejador manejador) {
  T_Manejador ptr = manejador;
  while (ptr != NULL) {
    printf("Desde %d a %d: Libre\n", ptr->inicio, ptr->fin);
    ptr = ptr->sig;
  }
}

void devolver(T_Manejador *manejador, unsigned tam, unsigned dir) {
  T_Manejador ant = NULL;
  T_Manejador ptr = *manejador;

  while (ptr != NULL) {
    if (dir <= ptr->inicio) {
      // añadir aqui
      T_Manejador p_nodo = malloc(sizeof(struct T_Nodo));

    } else {
      // rotar
    }
  }
}
