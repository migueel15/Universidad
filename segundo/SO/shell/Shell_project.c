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

#include "builtin_commands.h"
#include "job_control.h" // remember to compile with module job_control.c
#include "parse_redir.h"
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define MAX_LINE                                                               \
  256 /*erto 256 chars per line, per command, should be enough. */
job *job_list;

void manejador(int sig) {
  mask_signal(SIGCHLD, SIG_BLOCK);
  int pid_wait;
  int status;
  int info;
  enum status status_analyzed;
  job *current;

  for (int i = 1; i <= list_size(job_list); i++) {
    current = get_item_bypos(job_list, i);
    if (current->state != FOREGROUND) {
      pid_wait =
          waitpid(current->pgid, &status, WNOHANG | WUNTRACED | WCONTINUED);
      if (pid_wait == current->pgid) { // este proceso ha cambiado
        status_analyzed = analyze_status(status, &info);

        if (status_analyzed == SUSPENDED) {
          printf("Background pid: %d, command: %s, Suspended, info: %d\n",
                 pid_wait, current->command, info);
          current->state = STOPPED;
        } else if (status_analyzed == CONTINUED) {
          printf("Background pid: %d, command: %s, Continued, info: %d\n",
                 pid_wait, current->command, info);
          current->state = BACKGROUND;
        } else if (status_analyzed == SIGNALED || status_analyzed == EXITED) {
          printf("Background pid: %d, command: %s, Exited, info: %d\n",
                 pid_wait, current->command, info);
          delete_job(job_list, current);
        }
      }
    }
  }
  mask_signal(SIGCHLD, SIG_UNBLOCK);
}

// -----------------------------------------------------------------------
//                            MAIN
// -----------------------------------------------------------------------
//

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
  char *file_in;
  char *file_out;

  /* Program terminates normally inside get_command() after ^D is typed*/

  job_list = new_list("Lista de procesos");
  terminal_signals(SIG_IGN);
  signal(SIGCHLD, manejador);

  while (1) {
    printf("COMMAND->");
    fflush(stdout);
    get_command(inputBuffer, MAX_LINE, args, &background);
    parse_redirections(args, &file_in, &file_out);

    if (args[0] == NULL) {
      continue;
    }

    // -------- BUILTIN COMMANDS -------- //
    // returns -1 if not a builtin command
    e_Builtin COMMAND = check_if_builtin(args[0]);
    if (COMMAND != -1) {
      run_builtin_command(COMMAND, args, job_list);
      continue;
    }
    // --------------------------------- //

    pid_fork = fork();
    if (pid_fork == -1) {
      perror("Error al crear el proceso hijo");
      continue;
    }

    if (pid_fork == 0) { // hijo
      setpgid(getpid(), getpid());

      if (background == 0) {
        tcsetpgrp(STDIN_FILENO, getpid());
      }
      terminal_signals(SIG_DFL);
      execvp(args[0], args);
      perror("Error al ejecutar el comando");
      exit(1);
    } else { // padre
      if (background == 0) {
        pid_wait = waitpid(pid_fork, &status, WUNTRACED);
        tcsetpgrp(STDIN_FILENO, getpid());
        status_res = analyze_status(status, &info);
        if (status_res == SUSPENDED) {
          mask_signal(SIGCHLD, SIG_BLOCK);
          job *newjob = new_job(pid_fork, args[0], STOPPED);
          add_job(job_list, newjob);
          printf("Foreground pid: %d, command: %s, %s, info: %d\n", pid_wait,
                 args[0], status_strings[status_res], info);
          mask_signal(SIGCHLD, SIG_UNBLOCK);
        } else if (status_res == EXITED || status_res == SIGNALED) {
          printf("Foreground pid: %d, command: %s, %s, info: %d\n", pid_wait,
                 args[0], status_strings[status_res], info);
        }
      } else {
        mask_signal(SIGCHLD, SIG_BLOCK);
        job *newjob = new_job(pid_fork, args[0], BACKGROUND);
        add_job(job_list, newjob);
        printf("Background job runing... pid: %d, command: %s\n", pid_fork,
               args[0]);
        mask_signal(SIGCHLD, SIG_UNBLOCK);
      }
    }

  } // end while
}
