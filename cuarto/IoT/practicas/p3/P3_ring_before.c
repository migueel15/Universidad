/*
 * P3 - A word around a ring with MPI
 * P3_ring.c - starting code
 *
 * Process 0 reads a word from the keyboard and sends it to process 1.
 * Every process prints that it has the word and passes it to the next one.
 * When the word returns to process 0 the program reports it and ends.
 *
 * This file contains THREE deliberate defects, of three different kinds:
 *   - one that the compiler rejects
 *   - one that appears the first time you run it
 *   - one that appears only when you use more than two processes
 *
 * Above each correction, write a comment saying what was wrong and how you
 * noticed it.
 */

#include <mpi.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define MAXWORD 64
#define TAG 1

int main(int argc, char *argv[]) {
  int rank, size, next, prev;
  char word[MAXWORD];
  MPI_Status status;

  MPI_Init(&argc, &argv);
  /* Al compilar el programa se mostraba un error en la línea 31 y 32. Al
   * tratarse de una ejecución en la que tenemos 4 procesos, debemos definir
   * MPI_COMM_WORLD. De esta forma cada uno de los procesos obtendrá en rank su
   * identificador y en size el número total de procesos.
   */
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  MPI_Comm_size(MPI_COMM_WORLD, &size);

  /* El cálculo del siguiente no era adecuado. Con 4 procesos, el proceso con id
   * 3 enviaba al proceso con id 4, el cual es inválido. Mostraba un error de
   * MPI_ERR_RANK. Se resuelve usando el operador módulo para que el último
   * proceso vuelva a principio del anillo.
   */
  next = (rank + 1) % size;
  prev = (rank - 1 + size) % size;

  sleep(2);

  if (rank == 0) {
    printf("type a word and press enter: ");
    fflush(stdout);
    if (scanf("%63s", word) != 1) {
      printf("no word was read\n");
      MPI_Finalize();
      return 1;
    }
    printf("process 0 sends the word %s\n", word);
    fflush(stdout);

    MPI_Send(word, MAXWORD, MPI_CHAR, next, TAG, MPI_COMM_WORLD);
    MPI_Recv(word, MAXWORD, MPI_CHAR, prev, TAG, MPI_COMM_WORLD, &status);

    printf("the word %s came back to process 0, the ring is closed\n", word);
    fflush(stdout);
  } else {

    /* Siempre se esperaba la respuesta del proceso 0. Cuando se ejecutaba solo
     * se mostraba una comunicación. Todos los procesos esperaban al proceso con
     * id 0. El proceso con id 0 solo enviaba a 1. Al tratarse de una cadena de
     * eventos, cada proceso debe esperar el mensaje del proceso con id anterior
     * al suyo.
     */
    MPI_Recv(word, MAXWORD, MPI_CHAR, prev, TAG, MPI_COMM_WORLD, &status);

    printf("process %d has the word %s\n", rank, word);
    fflush(stdout);

    MPI_Send(word, MAXWORD, MPI_CHAR, next, TAG, MPI_COMM_WORLD);
  }

  MPI_Finalize();
  return 0;
}
