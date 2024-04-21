#ifndef BUILTIN_COMMANDS_H
#define BUILTIN_COMMANDS_H

#include "job_control.h"

typedef enum BuiltinCommand {
  CD,
  EXIT,
  JOBS,
  // ....
} e_Builtin;

typedef struct Command {
  e_Builtin commandEnum;
  const char *commandString;
} s_Command;

/*
 * Devuelve el enum correspondiente al comando pasado por parametro si se
 * reconoce como builtin. En otro caso devuelve -1.
 */
e_Builtin check_if_builtin(char *command);

void run_builtin_command(e_Builtin COMMAND, char *args[], job *jobs);

void change_directory(char *args[]);

#endif
