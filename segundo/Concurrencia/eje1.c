#include <stdio.h>
int main()
{
  struct Punto {
    int x;
    int y;
  };

  struct Punto p1;
  p1.x = 10;
  p1.y = 20;

  struct Punto *p2 = &p1;
  printf("p2->x = %d\n", p2->x); // imprime "p2->x = 10"

  return 0;
}
