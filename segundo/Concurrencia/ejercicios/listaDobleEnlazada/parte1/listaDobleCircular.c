#include "listaDobleCircular.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int estaVacia(TListaDoble l) {
  if (l == NULL) {
    return 1;
  } else {
    return 0;
  }
}

void crear(TListaDoble *l) { *l = NULL; }

TListaDoble crearNodo(char persona[20], TListaDoble ant, TListaDoble sig) {
  TListaDoble nodo = malloc(sizeof(struct TNodo));
  if (nodo != NULL) {
    nodo->ant = ant;
    nodo->sig = sig;
    strcpy(nodo->persona, persona);
  } else {
    printf("Error: La asignacion de memoria falló");
  }
  return nodo;
}

void crearDesdeFicheroTexto(char *nomFic, TListaDoble *l) {
  char nombre[20];
  FILE *file = fopen(nomFic, "rt");
  if (file == NULL) {
    printf("Error: La lectura del fichero falló");
  }

  while (fscanf(file, "%s", nombre) != EOF) {
    insertar(l, nombre);
  }

  fclose(file);
}

void insertar(TListaDoble *l, char *persona) {
  if (*l == NULL) {
    // esta vacia
    *l = crearNodo(persona, NULL, NULL);
    (*l)->ant = *l;
    (*l)->sig = *l;
  } else if ((*l)->ant == *l && (*l)->sig == *l) {
    // un elemento
    TListaDoble nodo = crearNodo(persona, *l, *l);
    (*l)->ant = nodo;
    (*l)->sig = nodo;
    *l = nodo;
  } else {
    // mas de un elemento
    TListaDoble cabeza = *l;
    TListaDoble ptr = *l;
    while (ptr->sig != cabeza) {
      ptr = ptr->sig;
    }
    TListaDoble nodo = crearNodo(persona, ptr, cabeza);
    ptr->sig = nodo;
    cabeza->ant = nodo;
    (*l) = nodo;
  }
}

void mostrar(TListaDoble l) {
  TListaDoble cabeza = l;
  TListaDoble ptr = l->ant;
  while (ptr != cabeza) {
    printf("%s\n", ptr->persona);
    fflush(NULL);
    ptr = ptr->ant;
  }
  printf("%s\n", ptr->persona);
  fflush(NULL);
}

void mostrarInverso(TListaDoble l) {
  TListaDoble ultimo = l->ant;
  while (l != ultimo) {
    printf("%s\n", l->persona);
    fflush(NULL);
    l = l->sig;
  }
  printf("%s\n", l->persona);
  fflush(NULL);
}
