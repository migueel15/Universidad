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
