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
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>

#define PORT 80
#define BUFSIZE 1024

int main(void) {
  int listen_fd, conn_fd;
  struct sockaddr_in serv_addr, cli_addr;
  socklen_t cli_len;
  char buffer[BUFSIZE];
  ssize_t n;

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
  serv_addr.sin_prt = htons(PORT);

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

    n = read(conn_fd, buffer, BUFSIZE);
    if (n < 0) {
      perror("read");
    } else {
      printf("server: received %zd bytes\n", n);
      fflush(stdout);
      if (write(conn_fd, buffer, n) < 0)
        perror("write");
    }

    close(conn_fd);
    printf("server: client disconnected\n\n");
    fflush(stdout);
  }

  return 0;
}
