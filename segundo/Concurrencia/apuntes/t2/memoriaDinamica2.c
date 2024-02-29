#include <stdio.h>
#include <stdlib.h>

// main function
int main() {
  int num;
  int *datos;
  int i;

  printf("Cuantos datos de tipo int quiere introducir: ");
  scanf("%d",&num);

  //reservamos la memoria que necesitemos
  datos = (int*)malloc(num*sizeof(int));

  printf("Introduzca %d datos: ",num);
  for (i=0; i<num; i++){
	  scanf("%d",&datos[i]);
    //scanf("%d",datos+i);
  }

  printf("Los datos introducidos son: ");
  for (i=0; i<num; i++){
	  printf("%d ",datos[i]);
    //printf("%d ",*(datos+i));
  }

  //IMPORTANTE: Liberar la memoria dinámica reservada
  free(datos);

  return 0;
} // main