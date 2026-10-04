/*
 * P2 - A concurrent server in C
 * P2_conc_client.c - given complete and correct. Do not modify it.
 *
 * Connects to the server, reads whatever arrives until the server closes,
 * and prints it. It also reports how long the whole exchange took.
 *
 *   usage:  ./P2_conc_client <address> <port> [--leave]
 *
 *   ./P2_conc_client 127.0.0.1 9100
 *       normal client: stays connected and reads everything.
 *
 *   ./P2_conc_client 127.0.0.1 9100 --leave
 *       impatient client: sends nothing, closes at once and exits.
 *       Use this one in the third stage.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define BUFSIZE 1024

int main(int argc, char *argv[])
{
    int sock_fd, leave = 0;
    struct sockaddr_in serv_addr;
    char buffer[BUFSIZE];
    ssize_t n;
    struct timespec t0, t1;

    if (argc < 3) {
        fprintf(stderr, "usage: %s <address> <port> [--leave]\n", argv[0]);
        return 1;
    }
    if (argc == 4 && strcmp(argv[3], "--leave") == 0)
        leave = 1;

    sock_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (sock_fd < 0) { perror("socket"); return 1; }

    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port   = htons((uint16_t) atoi(argv[2]));
    if (inet_pton(AF_INET, argv[1], &serv_addr.sin_addr) != 1) {
        fprintf(stderr, "bad address: %s\n", argv[1]);
        return 1;
    }

    clock_gettime(CLOCK_MONOTONIC, &t0);

    if (connect(sock_fd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        perror("connect");
        return 1;
    }

    if (leave) {
        printf("client: connected and leaving at once\n");
        close(sock_fd);
        return 0;
    }

    while ((n = read(sock_fd, buffer, BUFSIZE - 1)) > 0) {
        buffer[n] = '\0';
        printf("client: %s", buffer);
        fflush(stdout);
    }
    if (n < 0) perror("read");

    clock_gettime(CLOCK_MONOTONIC, &t1);
    printf("client: finished in %.2f seconds\n",
           (t1.tv_sec - t0.tv_sec) + (t1.tv_nsec - t0.tv_nsec) / 1e9);

    close(sock_fd);
    return 0;
}
