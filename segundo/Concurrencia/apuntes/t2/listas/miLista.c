#include "miLista.h"

void insertarAlPrincipio(TLista *ptr, int valor) {
  TLista p_elemento = (TLista)malloc(sizeof(struct TNode));
  p_elemento->valor = valor;
  p_elemento->next = *ptr;
  *ptr = p_elemento;
}
