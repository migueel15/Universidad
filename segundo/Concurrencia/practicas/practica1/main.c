#include "gestion_memoria.h"
#include <stdio.h>
#include <stdlib.h>

int main() {

  T_Manejador manej;
  unsigned ok;
  unsigned dir;

  crear(&manej);
  mostrar(manej);

  obtener(&manej, 500, &dir,
          &ok); /* Se ha hecho una foto. Se necesita memoria */
  if (ok) {
    mostrar(manej);
    printf("la direccion de comienzo es: %d\n", dir);
  } else {
    printf("No es posible obtener esa memoria\n");
  }

  devolver(&manej, 200,
           0); /* Se ha enviado parte de la foto. Ya se puede borrar */
  mostrar(manej);
}
