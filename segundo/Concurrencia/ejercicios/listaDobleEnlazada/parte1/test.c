#include "listaDobleCircular.h"
#include <stdio.h>

int main() {
  TListaDoble lista = NULL;
  crearDesdeFicheroTexto("personas.txt", &lista);
  // printf("%s", lista->persona);
  // lista = lista->ant;
  // printf("%s", lista->persona);
  // lista = lista->ant;
  // printf("%s", lista->persona);
  // lista = lista->ant;
  // printf("%s", lista->persona);
  // lista = lista->ant;
  // printf("%s", lista->persona);
  // lista = lista->ant;
  // printf("%s", lista->persona);
  // lista = lista->ant;
  // printf("%s", lista->persona);
  // lista = lista->ant;
  // printf("%s", lista->persona);
  // lista = lista->ant;
  mostrar(lista);
  mostrarInverso(lista);
}
