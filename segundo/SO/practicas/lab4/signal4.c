#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

void metodo(int sig) {
  printf("El padre quiere matar al hijo\n");
  sleep(2);
  kill(getppid(), SIGKILL);
  signal(SIGINT, SIG_DFL);
}

int main() {
  pid_t pidfork;
  int status;

  signal(SIGINT, metodo);

  pidfork = fork();   // creamos proceso hijo
  if (pidfork == 0) { /* proceso hijo */
    int i = 0;
    printf("Hijo: pid %d: ejecutando...\n", getpid());
    while (1) {
      sleep(1);
      printf("Hijo: %d seg\n", ++i);
    }      // bucle infinito
  } else { /* proceso padre */
    sleep(5);
    printf("\nPadre: pid %d: mandando señal SIGINT\n", getpid());
    kill(pidfork, SIGINT);
    while (pidfork != wait(&status))
      ;
    if (WIFEXITED(status)) { // el proceso ha terminado con un exit()
      printf("El proceso terminó con estado %d\n", WEXITSTATUS(status));
    } else if (WIFSIGNALED(status)) { // el proceso ha terminado por la
                                      // recepción de una señal
      printf("El proceso terminó al recibir la señal %d\n", WTERMSIG(status));
    } else if (WIFSTOPPED(status)) { // el proceso se ha parado por la recepción
                                     // de una señal
      printf("El proceso se ha parado al recibir la señal %d\n",
             WSTOPSIG(status));
    }
  }
  exit(0);
}
