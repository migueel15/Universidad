/*
 * driver.c
 *
 *  Created on: 26 mar. 2024
 *      Author: mpinto
 */

#include "Procesos.h"
#include <stdio.h>
#include <stdlib.h>

/**
 * (1 punto)
 * @brief Lee el contenido de la lista de procesos desde un fichero binario y
 * crea una lista de procesos. El fichero almacena un numero de valores de tipo
 * unsigned, multiplo de 3, que representan el siguiente formato:
 *       <prioridad><id><tiempo><prioridad><id><tiempo><prioridad><id><tiempo>...
 * @param nombre nombre del fichero
 * @param lp lista de procesos que se devuelve
 */
void leerProcesosDeFicheroBinario(char *nombre, LProcesos *lp) {
  FILE *fd = fopen(nombre, "rb");
  if (fd == NULL) {
    perror("No se ha podido leer del fichero");
    exit(1);
  }
  unsigned valores[3];

  while (fread(valores, sizeof(unsigned), 3, fd) == 3) {
    insertar(lp, valores[0], valores[1], valores[2]);
  }
  fclose(fd);
}

/**
 * (1 punto)
 * @brief Vuelca el contenido de la lista de procesos a un fichero.
 * El formato del fichero tiene que ser el siguiente:
 *  Prioridad <prioridad>
 * 		id:<id> tiempo:<tiempo>
 * 		id:<id> tiempo:<tiempo>
 *		...
 *  Prioridad <prioridad>
 *  	Sin procesos
 * 	Prioridad <prioridad>
 * 		id:<id> tiempo:<tiempo>
 * 		id:<id> tiempo:<tiempo>
 * 		...
 * Podéis abrir el fichero de ejemplo prueba.txt que se da con los recursos para
 *ver el formato
 * @param nombre nombre del fichero de texto donde volcar los datos de la lista
 * @param lp lista de procesos a volcar
 */
void guardarProcesosAFicheroTexto(char *nombre, LProcesos lp) {
  FILE *fd = fopen(nombre, "w");
  if (fd == NULL) {
    perror("No se ha podido leer del fichero");
    exit(1);
  }

  for (int i = MAX_PRIORIDAD; i >= MIN_PRIORIDAD; i--) {
    guardarFicheroPrioridad(lp, i, fd);
  }

  fclose(fd);
}

int main() {
  LProcesos lp;
  crear(&lp);

  printf("Insertamos procesos y los mostramos.\n");
  insertar(&lp, 0, 121, 10);
  insertar(&lp, 5, 157, 0);
  insertar(&lp, 5, 163, 15);
  insertar(&lp, 5, 123, 10);
  insertar(&lp, 4, 151, 20);
  insertar(&lp, 3, 162, 10);
  insertar(&lp, 5, 123, 20);
  insertar(&lp, 4, 167, 30);
  insertar(&lp, 1, 201, 20);
  insertar(&lp, 3, 178, 20);
  insertar(&lp, 4, 151, 30);
  insertar(&lp, 5, 176, 20);

  mostrar(lp);

  printf("Ejecutamos 4 veces con 1 procesador, siempre con tiempo de ejecucion "
         "10.\n");
  ejecutarMultiplesVeces(&lp, INTERVALO, 4);
  mostrar(lp);

  printf("Ejecutamos con 8 procesadores y tiempo de ejecucion 10.\n");
  int noUsados = ejecutarMultiplesProcesadores(&lp, INTERVALO, 8);
  mostrar(lp);
  if (noUsados == 0) {
    printf("Se han utilizado todos los procesadores disponibles.\n");
  } else {
    printf("%d procesadores sin utilizar.\n", noUsados);
  }

  printf("Guardamos los procesos de la lista con prioridad 4 en un fichero de "
         "texto.\n");
  FILE *fd = fopen("procesosPrio4.txt", "wt");
  if (fd != NULL) {
    guardarFicheroPrioridad(lp, 4, fd);
  }
  fclose(fd);

  printf("Extraemos los procesos con prioridad 4 de la lista.\n");
  LProcesos prioridad4 = extraerProcesos(&lp, 4);
  printf("Mostramos la nueva lista generada ...\n");
  mostrar(prioridad4);
  printf("Mostramos la lista original modificada ...\n");
  mostrar(lp);

  printf("Borramos la lista y la volvemos a crear desde el fichero binario.\n");
  borrar(&lp);
  leerProcesosDeFicheroBinario("procesos.bin", &lp);
  mostrar(lp);
  printf("Guardamos la lista en un fichero de texto.\n");
  guardarProcesosAFicheroTexto("procesos.txt", lp);
  return 0;
}
