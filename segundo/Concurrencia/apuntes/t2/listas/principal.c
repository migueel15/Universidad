#include "miLista.h"
#include <stdio.h>
#include <stdlib.h>

void agregarAlPrincipio(TLista *ptr, int valor) {
  TLista p_elemento = (TLista)malloc(sizeof(struct TNode));
  p_elemento->valor = valor;
  p_elemento->next = *ptr;
  *ptr = p_elemento;
}

int main() {
  TLista head = NULL;
  agregarAlPrincipio(&head, 10);
  agregarAlPrincipio(&head, 30);
  agregarAlPrincipio(&head, 50);

  TLista pointer = head;
  while (pointer != NULL) {
    printf("%d\n", pointer->valor);
    pointer = pointer->next;
  }

  return 0;
}
