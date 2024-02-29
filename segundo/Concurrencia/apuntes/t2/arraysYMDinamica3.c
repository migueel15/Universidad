#include <stdio.h>
#include <stdlib.h>

/* Cadena de caracteres y memoria dinámica
*/

//cabecera de las funciones.
void leerCadena(char *cad, int max);
void mostrarCadena(const char *cad,int max);

int main() {
  //char cadena[20]; //declaramos una cadena de caracteres. En este caso no necesitaríamos reservar memoria de forma dinámica
  char * cadena; //declaramos una cadena de caracteres
  int max = 0;

  printf("Introduzca tamaño maximo de la cadena: ");
  scanf("%d\n",&max); 
  /* MUY IMPORTANTE EL '\n' EN EL SCANF ANTERIOR
     Si no ponemos el \n, se queda en el buffer de teclado
     Al estar \n en el buffer y hacer scanf("%c",&car) en leerCadena
     se leería el \n en lugar del primer caracter de la cadena
     CUIDADO AL MEZCLAR LA LECTURA DE NUMEROS Y CADENAS DE CARACTERES
  */

  //necesitamos reservar memoria para la cadena de forma dinámica
  cadena = (char *)malloc(max*sizeof(char));

  if (cadena == NULL){ //Comprobamos si se ha reservado bien la memoria
    printf("Error reservando memoria. ");
    exit(-1);
  }

  //Cuando llamamos a leerCadena, cadena ya tiene memoria asignada
  leerCadena(cadena,max);
  mostrarCadena(cadena,max);

  return 0;
} // main

void leerCadena(char *cad, int max){
  //se asume que el parámetro cad tiene la memoria necesaria para utilizar este parámetro
  int i = 0;
  char car;

  if (max > 0){
    scanf("%c",&car);
    while (i<max && car !='\n'){
      cad[i]=car;
      i++;
      scanf("%c",&car);
    }
    if (i<max) cad[i]='\0';
  }
}

void mostrarCadena(const char *cad, int max){
  int i=0;

  while (i< max || cad[i]!='\0'){
    printf("%c",cad[i]);
    i++;
  }
  printf("\n");
}