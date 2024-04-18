/**
UNIX Shell Project

Sistemas Operativos
Grados I. Informatica, Computadores & Software
Dept. Arquitectura de Computadores - UMA

Some code adapted from "Fundamentos de Sistemas Operativos", Silberschatz et al.

To compile and run the program:
   $ gcc Shell_project.c job_control.c -o Shell
   $ ./Shell
        (then type ^D to exit program)

**/

#include "commands.h"
#include "job_control.h" // remember to compile with module job_control.c
#include <stdio.h>
#include <unistd.h>

#define MAX_LINE                                                               \
  256 /*erto 256 chars per line, per command, should be enough. */

// -----------------------------------------------------------------------
//                            MAIN
// -----------------------------------------------------------------------

int main(void) {
  char inputBuffer[MAX_LINE]; /* buffer to hold the command entered */
  int background;             /* equals 1 if a command is followed by '&' */
  int internal_command;
  char *args[MAX_LINE / 2]; /* command line (of 256) has max of 128 arguments */
  // probably useful variables:
  int pid_fork, pid_wait; /* pid for created and waited process */
  int status;             /* status returned by wait */
  enum status status_res; /* status processed by analyze_status() */
  int info;               /* info processed by analyze_status() */

  /* Program terminates normally inside get_command() after ^D is typed*/

  terminal_signals(SIG_IGN);
  while (1) {
    printf("COMMAND->");
    fflush(stdout);
    get_command(inputBuffer, MAX_LINE, args,
                &background); /* get next command */

    if (args[0] == NULL) {
      continue;
    }

    enum Internal_Command_Enum COMMAND = check_if_builtin(args[0]);
    if (COMMAND != -1) {
      run_builtin_command(COMMAND, args);
    } else {
      pid_fork = fork();

      if (pid_fork == -1) {
        perror("Error al crear el proceso hijo");
      }

      if (pid_fork == 0) {
        setpgid(getpid(), getpid());
        if (background == 0) {
          tcsetpgrp(STDIN_FILENO, getpid());
        }
        terminal_signals(SIG_DFL);
        execvp(args[0], args);
        perror("Error al ejecutar el comando");
        exit(1);
      } else {
        if (background == 0) {
          pid_wait = waitpid(pid_fork, &status, WUNTRACED);
          tcsetpgrp(STDIN_FILENO, getpid());
          status_res = analyze_status(status, &info);
          printf("Foreground pid: %d, command: %s, %s, info: %d\n", pid_wait,
                 args[0], status_strings[status_res], info);
        } else {
          printf("Background job runing... pid: %d, command: %s\n", pid_fork,
                 args[0]);
        }
      }
    }

  } // end while
}
