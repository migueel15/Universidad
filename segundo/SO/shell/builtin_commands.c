#include "builtin_commands.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

s_Command builtin_commands[] = {{CD, "cd"}, {EXIT, "exit"}, {JOBS, "jobs"}};

e_Builtin check_if_builtin(char *command) {
  for (int i = 0; i < sizeof(builtin_commands) / sizeof(s_Command); i++) {
    if (strcmp(command, builtin_commands[i].commandString) == 0) {
      return builtin_commands[i].commandEnum;
    }
  }
  return -1;
}

void run_builtin_command(e_Builtin COMMAND, char *args[], job *job_list) {
  switch (COMMAND) {
  case CD:
    change_directory(args);
    break;
  case EXIT:
    exit(1);
    break;
  case JOBS:
    block_SIGCHLD();
    print_job_list(job_list);
    unblock_SIGCHLD();
    break;
  }
}

void change_directory(char *args[]) {
  if (args[1] == NULL) {
    chdir(getenv("HOME"));
    return;
  }
  if (chdir(args[1]) != 0) {
    perror("cd");
  };
}
