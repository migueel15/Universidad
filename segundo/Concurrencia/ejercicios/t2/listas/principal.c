#include "miLista.h"
#include <stdio.h>
#include <stdlib.h>

int main() {
  TLista head = NULL;
  // insertarAlPrincipio(&head, 10);
  // insertarAlPrincipio(&head, 30);
  // insertarAlPrincipio(&head, 50);
  insertarAlPrincipio(&head, 10);

  TLista pointer = head;
  while (pointer != NULL) {
    printf("%d\n", pointer->valor);
    pointer = pointer->next;
  }

  return 0;
}
