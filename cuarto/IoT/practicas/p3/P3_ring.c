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

#define MAXWORD 64
#define TAG 1

int main(int argc, char *argv[]) {
  int rank, size, next, prev;
  char word[MAXWORD];
  MPI_Status status;

  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_ALL, &rank);
  MPI_Comm_size(MPI_COMM_ALL, &size);

  next = rank + 1;
  prev = (rank - 1 + size) % size;

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
    MPI_Recv(word, MAXWORD, MPI_CHAR, 0, TAG, MPI_COMM_WORLD, &status);

    printf("process %d has the word %s\n", rank, word);
    fflush(stdout);

    MPI_Send(word, MAXWORD, MPI_CHAR, next, TAG, MPI_COMM_WORLD);
  }

  MPI_Finalize();
  return 0;
}
