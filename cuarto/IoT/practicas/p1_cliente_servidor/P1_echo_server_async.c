/*
 * P1 - Echo over TCP in C
 * P1_echo_server.c - starting code
 *
 * A TCP server that returns to the client exactly the bytes it received.
 *
 * This file contains THREE deliberate defects:
 *   - one stops it from compiling
 *   - one stops it from running
 *   - one lets it run, and lets small messages work, but is still wrong
 *
 * Find them, fix them, and write a short comment above each fix saying
 * what was wrong and how you noticed it.
 */

#include <arpa/inet.h>
#include <netinet/in.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

/*
 * Un puerto menor a 1024 es un puerto privilegiado que necesita
 * privilegios de administrador para poder ser usado.
 * Como el script de ejecución usa el puerto 9000, es el que defino aquí
 */

#define PORT 9000
#define BUFSIZE 1024

// maneja la recogida de hijos zombie
void clean_child(int sig) {
  while (waitpid(-1, NULL, WNOHANG) > 0) {
  }
}

int main(void) {
  int listen_fd, conn_fd;
  struct sockaddr_in serv_addr, cli_addr;
  socklen_t cli_len;
  char buffer[BUFSIZE];
  ssize_t n;

  signal(SIGCHLD, clean_child);

  listen_fd = socket(AF_INET, SOCK_STREAM, 0);
  if (listen_fd < 0) {
    perror("socket");
    exit(1);
  }

  /* The port is released for immediate reuse. Without this, a server that
     has just been stopped leaves the port reserved for about a minute and
     the next run fails with "Address already in use". This is given to
     you: it is not one of the defects. */
  {
    int yes = 1;
    if (setsockopt(listen_fd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(yes)) <
        0) {
      perror("setsockopt");
      exit(1);
    }
  }

  memset(&serv_addr, 0, sizeof(serv_addr));
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
  /*
   * Fallo de typo:
   * El compilador avisa de que sin_prt no es un miembro del struct serv_addr.
   */
  serv_addr.sin_port = htons(PORT);

  if (bind(listen_fd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
    perror("bind");
    exit(1);
  }

  if (listen(listen_fd, 8) < 0) {
    perror("listen");
    exit(1);
  }

  printf("server: listening on port %d\n", PORT);
  fflush(stdout);

  for (;;) {
    cli_len = sizeof(cli_addr);
    conn_fd = accept(listen_fd, (struct sockaddr *)&cli_addr, &cli_len);
    if (conn_fd < 0) {
      perror("accept");
      continue;
    }

    printf("server: client connected from %s:%d\n",
           inet_ntoa(cli_addr.sin_addr), ntohs(cli_addr.sin_port));
    fflush(stdout);

    pid_t pid = fork();

    if (pid < 0) {
      perror("fork");
      close(conn_fd);
      continue;
    }

    if (pid == 0) {
      close(listen_fd);

      /*
       * El servidor solo espera un mensaje del cliente.
       * Como el buffer está definido a 1024 bytes, este es el máximo
       * tamaño que recibirá por mensaje.
       *
       * Para mensajes más grandes es necesario ir iterando hasta quedarse sin
       * bytes de lectura.
       * La función read devuelve la cantidad de bytes leidos por lo que podemos
       * saber cuando terminar de leer.
       */
      while ((n = read(conn_fd, buffer, BUFSIZE)) > 0) {
        printf("server: received %zd bytes\n", n);
        fflush(stdout);

        /*
         * Al escribir ocurre algo parecido que al leer. Es posible que el
         * número de bytes escritos sea menor al definido en la función.
         *
         * Se va llevando una variable total que acumule los bytes escritos.
         * Por lo general se va a mandar n de primeras pero es posible que por
         * llenado de discos u otros casos no se mande al completo.
         */

        ssize_t total = 0;
        sleep(2);

        while (total < n) {
          ssize_t current = write(conn_fd, buffer + total, n - total);
          if (current <= 0) {
            if (current < 0) {
              perror("write");
            }
            break;
          }
          total += current;
        }
      }

      if (n < 0) {
        perror("read");
      }

      printf("server: client disconnected\n\n");
      fflush(stdout);
      close(conn_fd);
      exit(0);

    } else {
      close(conn_fd);
    }
  }

  return 0;
}
