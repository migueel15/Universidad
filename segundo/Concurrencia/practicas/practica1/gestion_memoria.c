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
  printf("--------------------\n");
  while (ptr != NULL) {
    printf("Desde %d a %d: Libre\n", ptr->inicio, ptr->fin);
    ptr = ptr->sig;
  }
}

T_Manejador crearNodo(unsigned ini, unsigned tam, T_Manejador sig) {
  T_Manejador nodo = malloc(sizeof(struct T_Nodo));
  nodo->inicio = ini;
  nodo->fin = ini + tam - 1;
  nodo->sig = sig;
  return nodo;
}

void devolver(T_Manejador *manejador, unsigned tam, unsigned dir) {
  T_Manejador ant = NULL;
  T_Manejador ptr = *manejador;
  int encontrado = 0;

  // Buscamos los nodos que nos interesa
  while (ptr != NULL && !encontrado) {
    if (dir >= ptr->inicio) {
      // avanzar
      ant = ptr;
      ptr = ptr->sig;
    } else {
      encontrado = 1;
    }
  }

  if (ant == NULL) {
    // SE INSERTA AL PRINCIPIO
    if (dir + tam < ptr->inicio) {
      // se crea un nodo
      T_Manejador nodo = crearNodo(dir, tam, ptr);
      *manejador = nodo;

    } else {
      // se aumenta el inicio de ptr
      ptr->inicio = dir;
    }
  } else if (ptr == NULL) {
    // SE INSERTA AL FINAL
    if (dir - 1 != ant->fin) {
      // crear nuevo nodo
      T_Manejador nodo = crearNodo(dir, tam, NULL);
      ant->sig = nodo;
    } else {
      // aumentar ultimo nodo
      ant->fin += tam;
    }
  } else {
    // SE INSERTA ENTRE NODOS
    if (ant->fin + 1 == dir && ptr->inicio != (dir + tam)) {
      // ampliar anterior
      ant->fin = ant->fin + tam - 1;
    } else if (ant->fin != dir) {
      // crear un nuevo nodo
      T_Manejador nuevo = crearNodo(dir, tam, ptr);
      if (nuevo != NULL) {
        ant->sig = nuevo;
      }
    } else {
      // ocupa todo el hueco de tiene que unir
      ant->fin = ptr->fin;
      ant->sig = ptr->sig;
      free(ptr);
    }
  }
}
