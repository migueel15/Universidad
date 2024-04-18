#ifndef COMMANDS_H
#define COMMANDS_H

enum Internal_Command_Enum {
  CD,
  EXIT,
  // ....
};

struct Command {
  enum Internal_Command_Enum commandEnum;
  const char *commandString;
};

/*
 * Devuelve el enum correspondiente al comando pasado por parametro si se
 * reconoce como builtin. En otro caso devuelve -1.
 */
enum Internal_Command_Enum check_if_builtin(char *command);

void run_builtin_command(enum Internal_Command_Enum enumCommand, char *args[]);

void change_directory(char *args[]);

#endif
