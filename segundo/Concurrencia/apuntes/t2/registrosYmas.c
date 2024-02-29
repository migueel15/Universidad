/*
 * registros.c
 */
#include <stdio.h>
#include <string.h>

// Define el tipo struct Punto
struct Punto {
  int x, y;
};

// Redefine struct Punto como Punto
typedef struct Punto Punto;
struct Rectangulo {
  Punto p1, p2; // Se puede definir así por el typedef anterior
};

// Define el tipo Data
union Data {
  int i;
  float f;
  char str[20];
};

enum months { ENE, FEB, MAR, ABR, MAY, JUN, JUL, AGO, SEP, OCT, NOV, DIC };

const double e = 2.71828182845905;

int main() {
  struct Punto p1 = {2, 3}; // Si solo se define el registro
  Punto p2 = p1;            // Por haber redefinido struct Punto como Punto

  printf("%d %d\n", p1.x, p1.y);
  printf("%d %d\n", p2.x, p2.y);

  // INFO: UNION Permite el uso de distintos tipos de datos en la misma posición
  // de memoria. Se queda guardada el último acceso.
  // En este caso se guarda 10 en memória y después se sobreescribe con 220.5
  union Data data;
  data.i = 10;
  data.f = 220.5;

  printf("data.i : %d\n", data.i);
  printf("data.f : %f\n", data.f);

  enum months m;
  m = FEB;
  if (m == FEB) {
    printf("FEBRERO");
  }

  char cad[5] = "hola";
  // char cad[5] = {'h','o','l','a','\0'};

  strcpy(cad, "hola");
  printf("\n%s", cad);
  printf("\n%c", cad[0]);

  return 0;
}
