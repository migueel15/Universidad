/*
 * memoriaDinamica.c
 *
 *  Created on: 7/2/2020
 *      Author: usuario
 */

#include <stdio.h>
#include <stdlib.h>

// main function
int main() {

  // Se declara el puntero y se inicializa a NULL
  int *ptr1 = NULL;

  // se reserva un bloque de 20 enteros
  ptr1 = (int *)malloc(20 * sizeof(int));
  if (ptr1 == NULL) {
    fprintf(stderr, "Error reservando memoria para 20 int\n");
    exit(-1);
  }

  // Le doy valores a la zona de memoria reservada, utilizandola como un array
  for (int i = 0; i < 20; i++) {
    ptr1[i] = i;
  }

  // incrementamos el tamaño del bloque de 20 enteros a 40
  int *ptr2 = (int *)realloc(ptr1, sizeof(int) * 40);
  if (ptr2 == NULL) {
    fprintf(stderr, "Error reallocating memory\n");
    exit(-1);
  }

  for (int i = 0; i < 40; i++) {
    ptr2[i] = i;
  }
  for (int i = 0; i < 40; i++) {
    printf("%d ", ptr2[i]);
  }

  // Cuidado al liberar la memoria cuando varios punteros apuntan a la misma
  // zona free(ptr1) ; free(ptr2) ; //probar a descomentar esta línea
  //
  free(ptr2);

  return 0;
} // main
