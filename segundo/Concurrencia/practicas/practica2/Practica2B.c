/*
 ============================================================================
 Name        : Practica2B.c
 Author      : esc
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include "arbolbb.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/**
 * Pide un n�mero "tam" al usuario, y
 * crea un fichero binario para escritura con el nombre "nfichero"
 * en que escribe "tam" numeros (unsigned int) aleatorios
 * Se utiliza srand(time(NULL)) para establecer la semilla (de la libreria
 * time.h) y rand()%100 para crear un n�mero aleatorio entre 0 y 99.
 */
void creafichero(char *nfichero) {
  int TAM;
  printf("Cuantos numeros quieres: ");
  fflush(stdout);
  scanf("%d", &TAM);

  FILE *fd = fopen(nfichero, "wb");

  if (fd == NULL) {
    perror("Error al crear el fichero");
  } else {
    srand(time(NULL));

    for (int i = 0; i < TAM; i++) {
      // leemos el numero y lo guardamos
      int n = rand() % TAM;
      fwrite(&n, sizeof(int), 1, fd);
    }
  }

  fclose(fd);
}
/**
 * Muestra por pantalla la lista de n�meros (unsigned int) almacenada
 * en el fichero binario "nfichero"
 */
void muestrafichero(char *nfichero) {
  unsigned int valor;
  FILE *fd = fopen(nfichero, "rb");

  if (fd == NULL) {
    perror("No se puede leer el archivo");
  } else {
    while (fread(&valor, sizeof(int), 1, fd) == 1) {
      printf("%d ", valor);
    }
  }

  fclose(fd);
}

/**
 * Guarda en el arbol "*miarbol" los n�meros almacenados en el fichero binario
 * "nfichero"
 */

void cargaFichero(char *nfichero, T_Arbol *miarbol) {
  FILE *file = fopen(nfichero, "rb");
  if (file == NULL) {
    perror("Error al leer el archivo");
  }
  unsigned int valor;
  while (fread(&valor, sizeof(unsigned int), 1, file) == 1) {
    Insertar(miarbol, valor);
  }
  fclose(file);
}

int main(void) {
  // setvbuf(stdout,NULL,_IONBF,0);

  char nfichero[50];
  printf("Introduce el nombre del fichero binario:\n");
  fflush(stdout);
  scanf("%s", nfichero);
  fflush(stdin);
  creafichero(nfichero);
  printf("\nAhora lo leemos y mostramos:\n");
  muestrafichero(nfichero);
  fflush(stdout);

  printf("\nAhora lo cargamos en el arbol\n");
  T_Arbol miarbol;
  Crear(&miarbol);
  cargaFichero(nfichero, &miarbol);
  printf("\nY lo mostramos ordenado\n");
  Mostrar(miarbol);
  fflush(stdout);
  printf("\nAhora lo guardamos ordenado\n");
  FILE *fich;
  fich = fopen(nfichero, "wb");
  Salvar(miarbol, fich);
  fclose(fich);
  printf("\nY lo mostramos ordenado\n");
  muestrafichero(nfichero);
  Destruir(&miarbol);

  return EXIT_SUCCESS;
}
