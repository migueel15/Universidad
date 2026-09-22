/*
 * P1 - Echo over TCP in C
 * P1_echo_client.c - starting code
 *
 * Sends a message of the requested size to the echo server, waits until
 * exactly the same number of bytes has come back, and prints how long the
 * whole exchange took.
 *
 * This file has NO deliberate defects. Read it: it is the reference for
 * how a TCP client is written, and it is the tool you will use to measure.
 *
 *   usage:  ./P1_echo_client <address> <port> <message size in bytes>
 *   example: ./P1_echo_client 127.0.0.1 9000 1024
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

#define MAXSIZE (1024 * 1024)

static double elapsed_ms(struct timespec a, struct timespec b)
{
    return (b.tv_sec - a.tv_sec) * 1000.0 + (b.tv_nsec - a.tv_nsec) / 1000000.0;
}

int main(int argc, char *argv[])
{
    int sock_fd;
    struct sockaddr_in serv_addr;
    char *out, *in;
    size_t size, sent = 0, received = 0;
    ssize_t n;
    struct timespec t0, t1;

    if (argc != 4) {
        fprintf(stderr, "usage: %s <address> <port> <size in bytes>\n", argv[0]);
        return 1;
    }

    size = (size_t) atoi(argv[3]);
    if (size == 0 || size > MAXSIZE) {
        fprintf(stderr, "size must be between 1 and %d\n", MAXSIZE);
        return 1;
    }

    out = malloc(size);
    in  = malloc(size);
    if (out == NULL || in == NULL) {
        perror("malloc");
        return 1;
    }
    memset(out, 'A', size);

    sock_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (sock_fd < 0) {
        perror("socket");
        return 1;
    }

    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port   = htons((uint16_t) atoi(argv[2]));
    if (inet_pton(AF_INET, argv[1], &serv_addr.sin_addr) != 1) {
        fprintf(stderr, "bad address: %s\n", argv[1]);
        return 1;
    }

    if (connect(sock_fd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        perror("connect");
        return 1;
    }

    clock_gettime(CLOCK_MONOTONIC, &t0);

    while (sent < size) {
        n = write(sock_fd, out + sent, size - sent);
        if (n <= 0) {
            perror("write");
            return 1;
        }
        sent += (size_t) n;
    }

    /* one send is NOT one read: keep reading until everything is back */
    while (received < size) {
        n = read(sock_fd, in + received, size - received);
        if (n == 0) {
            fprintf(stderr, "client: server closed after %zu of %zu bytes\n",
                    received, size);
            return 1;
        }
        if (n < 0) {
            perror("read");
            return 1;
        }
        received += (size_t) n;
    }

    clock_gettime(CLOCK_MONOTONIC, &t1);

    printf("client: %zu bytes echoed in %.3f ms  (%s)\n",
           received, elapsed_ms(t0, t1),
           memcmp(out, in, size) == 0 ? "identical" : "DIFFERENT");

    close(sock_fd);
    free(out);
    free(in);
    return 0;
}
