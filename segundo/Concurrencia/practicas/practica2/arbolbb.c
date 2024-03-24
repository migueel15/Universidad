#include "arbolbb.h"
#include <stdlib.h>

void Crear(T_Arbol *arbol) { *arbol = NULL; }

void Destruir(T_Arbol *arbol) {
  if (*arbol != NULL) {
    if ((*arbol)->izq == NULL && (*arbol)->der == NULL) {
      free(*arbol);
    } else {
      if ((*arbol)->izq != NULL) {
        Destruir(&(*arbol)->izq);
      }
      if ((*arbol)->der != NULL) {
        Destruir(&(*arbol)->der);
      }
    }
  }
}
void Insertar(T_Arbol *arbol, unsigned num) {
  if ((*arbol) == NULL) {
    T_Arbol nodo = malloc(sizeof(struct T_Nodo));
    nodo->dato = num;
    nodo->izq = NULL;
    nodo->der = NULL;
    *arbol = nodo;
  } else if ((*arbol)->dato > num) {
    Insertar(&(*arbol)->izq, num);
  } else {
    Insertar(&(*arbol)->der, num);
  }
}
void Mostrar(T_Arbol arbol) {
  if (arbol != NULL) {
    Mostrar(arbol->izq);
    printf("%d ", arbol->dato);
    Mostrar(arbol->der);
  }
}
void Salvar(T_Arbol arbol, FILE *fichero) {
  if (arbol != NULL) {
    Salvar(arbol->izq, fichero);
    fwrite(&(arbol->dato), sizeof(unsigned int), 1, fichero);
    Salvar(arbol->der, fichero);
  }
}
