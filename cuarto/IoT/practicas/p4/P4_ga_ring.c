/*
 * P4 - A ring of genetic algorithm subpopulations with MPI
 * P4_ga_ring.c - starting code
 *
 * Every process holds its own subpopulation and evolves it. Every
 * MIGRATE_EVERY generations each process sends one individual to the next
 * process in the ring and receives one from the previous process. When every
 * subpopulation has performed MAX_EVALS evaluations, the best individual of
 * the whole ring is reported, with the number of evaluations after which the
 * maximum was first reached.
 *
 * The problem: maximise x*x + y*y, with x and y encoded in BITS_PER_VAR bits
 * each and read as plain unsigned integers.
 *
 * Run as   mpirun --oversubscribe -np 4 ./P4_ga_ring 1
 * The last number is the seed of the random numbers: the same seed repeats
 * the same run, a different seed gives a different run.
 *
 * WHAT IS GIVEN AND WORKING
 *   the constants, the ring, the migration exchange, the main loop, the
 *   stop condition and the final report. Do not change them.
 *
 * WHAT YOU HAVE TO WRITE
 *   the eight functions marked below. Each one states what it receives and
 *   what it must produce. How you write it is your decision.
 *
 * There are no deliberate defects in this file.
 */

#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* ------------------------------------------------------------------
 * Parameters. Everything below follows from the two first lines, so
 * changing the number of variables or the bits per variable is enough.
 * ------------------------------------------------------------------ */
#define N_VARS 2                  /* how many variables       */
#define BITS_PER_VAR 16           /* bits used by each one    */
#define L (N_VARS * BITS_PER_VAR) /* length of one individual */
#define POP_SIZE 64               /* individuals per process  */
#define P_CROSS 0.8               /* probability of crossover */
#define P_MUT (1.0 / (double)L)   /* probability per bit      */
#define MIGRATE_EVERY 20          /* generations between them */
#define MIGRATE_BEST 1            /* 1 the best, 0 a random   */
#define MAX_EVALS 20000           /* per subpopulation        */
#define X_MIN 0.0                 /* lowest value a variable  */
#define X_MAX 65535.0             /* highest value a variable */
#define TAG_MIGRATE 7

typedef struct {
  char gene[L]; /* one bit per position, each element is 0 or 1 */
  double fit;   /* the value of the function for this gene      */
} Individual;

/* a random number in [0,1) */
static double rnd(void) { return (double)rand() / ((double)RAND_MAX + 1.0); }

/* ==================================================================
 *                    THE EIGHT FUNCTIONS TO WRITE
 * ================================================================== */

/* 1. Read the bits of one variable and return its value.
 *    var is 0 for the first variable, 1 for the second, and so on.
 *    The bits of variable var occupy the positions
 *        var*BITS_PER_VAR  ...  var*BITS_PER_VAR + BITS_PER_VAR - 1
 *    with the most significant bit first.
 *    Return a value between X_MIN and X_MAX. */
double decode(const char *gene, int var) {
  int result = 0;
  for (int i = var * BITS_PER_VAR; i < var * BITS_PER_VAR + BITS_PER_VAR; i++) {
    // 1 -> 10 -> 11
    // (results << 1) = 00
    // 00 | gene[i] = 01
    result = (result << 1) | gene[i];
  }
  return result;
}

/* 2. Return the value of the function for this individual.
 *    The function is x*x + y*y, with x and y obtained by decoding. */
double fitness(const char *gene) {
  double x = decode(gene, 0);
  double y = decode(gene, 1);

  return x * x + y * y;
}

/* 3. Binary tournament. Choose two positions of the subpopulation at random
 *    and return the index of the one with the higher fitness. */
int tournament(const Individual *pop) {
  int ran1 = rand() % POP_SIZE;
  int ran2 = rand() % POP_SIZE;

  double position1 = pop[ran1].fit;
  double position2 = pop[ran2].fit;

  return position1 >= position2 ? ran1 : ran2;
}

/* 4. Two point crossover.
 *    With probability P_CROSS choose two cut points and exchange the segment
 *    between them, so c1 and c2 are the two children.
 *    With the remaining probability the children are copies of the parents.
 *    In both cases c1 and c2 must be complete on return. */
void crossover_two_point(const char *p1, const char *p2, char *c1, char *c2) {}

/* 5. Bit flip mutation. Every bit changes with probability P_MUT. */
void mutate(char *gene) {}

/* 6. Place one individual in the subpopulation, which is kept in order from
 *    the best at position 0 to the worst at position POP_SIZE-1.
 *    The individual enters only if it is better than or equal to the worst.
 *    When it enters, the worst one leaves and the order is preserved. */
void insert_sorted(Individual *pop, const Individual *ind) {}

/* 7. Choose the individual to send in a migration.
 *    If MIGRATE_BEST is 1 send the best of the subpopulation.
 *    If it is 0 send one chosen at random. Write it into out. */
void migrate_select(const Individual *pop, Individual *out) {}

/* 8. Receive an individual that arrived from the previous process.
 *    It enters the subpopulation only if it is better than or equal to the
 *    worst one already there. */
void migrate_accept(Individual *pop, const Individual *in) {}

/* ==================================================================
 *                  GIVEN AND WORKING, DO NOT CHANGE
 * ================================================================== */

static int cmp(const void *a, const void *b) {
  double d = ((const Individual *)b)->fit - ((const Individual *)a)->fit;
  return (d > 0) - (d < 0);
}

int main(int argc, char *argv[]) {
  int rank, size, next, prev, i, seed, evals = 0, gen = 0, evals_best = 0;
  double maximum = 2.0 * X_MAX * X_MAX, best_so_far;
  Individual pop[POP_SIZE], c1, c2, out, in;
  MPI_Status st;
  struct {
    double fit;
    int rank;
  } mine, best;
  struct {
    int evals;
    int rank;
  } m_first, first;

  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  next = (rank + 1) % size;
  prev = (rank - 1 + size) % size;
  seed = (argc > 1) ? atoi(argv[1]) : 0; /* ./P4_ga_ring 3 uses seed 3 */
  srand(1234 + rank * 77 + seed);

  /* a random subpopulation, then sorted from best to worst */
  for (i = 0; i < POP_SIZE; i++) {
    int k;
    for (k = 0; k < L; k++)
      pop[i].gene[k] = (char)(rnd() < 0.5);
    pop[i].fit = fitness(pop[i].gene);
  }
  qsort(pop, POP_SIZE, sizeof(Individual), cmp);
  best_so_far = pop[0].fit;

  while (evals < MAX_EVALS) {
    int i1 = tournament(pop);
    int i2 = tournament(pop);

    crossover_two_point(pop[i1].gene, pop[i2].gene, c1.gene, c2.gene);
    mutate(c1.gene);
    mutate(c2.gene);
    c1.fit = fitness(c1.gene);
    c2.fit = fitness(c2.gene);
    evals += 2;

    insert_sorted(pop, &c1);
    insert_sorted(pop, &c2);
    gen++;

    if (gen % MIGRATE_EVERY == 0 && size > 1) {
      migrate_select(pop, &out);
      MPI_Sendrecv(&out, sizeof(Individual), MPI_BYTE, next, TAG_MIGRATE, &in,
                   sizeof(Individual), MPI_BYTE, prev, TAG_MIGRATE,
                   MPI_COMM_WORLD, &st);
      migrate_accept(pop, &in);
    }

    /* the evaluation count at which the best of this process last improved */
    if (pop[0].fit > best_so_far) {
      best_so_far = pop[0].fit;
      evals_best = evals;
    }
  }

  printf("process %d best %.0f at x=%.0f y=%.0f, first reached after %d "
         "evaluations\n",
         rank, pop[0].fit, decode(pop[0].gene, 0), decode(pop[0].gene, 1),
         evals_best);
  fflush(stdout);

  /* the best value of the ring, and the first process that reached the maximum
   */
  mine.fit = pop[0].fit;
  mine.rank = rank;
  MPI_Reduce(&mine, &best, 1, MPI_DOUBLE_INT, MPI_MAXLOC, 0, MPI_COMM_WORLD);
  m_first.evals = (pop[0].fit >= maximum) ? evals_best : MAX_EVALS + 1;
  m_first.rank = rank;
  MPI_Reduce(&m_first, &first, 1, MPI_2INT, MPI_MINLOC, 0, MPI_COMM_WORLD);
  if (rank == 0) {
    printf("\nbest of the whole ring: %.0f, found by process %d\n"
           "the maximum possible is %.0f\n",
           best.fit, best.rank, maximum);
    if (first.evals <= MAX_EVALS)
      printf("the maximum was first reached by process %d after %d of its %d "
             "evaluations\n",
             first.rank, first.evals, MAX_EVALS);
    else
      printf("the maximum was not reached\n");
  }

  MPI_Finalize();
  return 0;
}
