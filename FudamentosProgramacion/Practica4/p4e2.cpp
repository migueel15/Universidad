#include <iostream>

using namespace std;

void leer(int &numero) {
  cout << "Introduzca un número: ";
  cin >> numero;
}

void numerosPrimos(int numero) {
  int i = 2, contador = 0;

  while (contador < numero) {
    int divisores = 0;
    for (int divisor = 1; divisor <= i; divisor++) {
      if (i % divisor == 0) {
        divisores++;
      }
    }
    if (divisores <= 2) {
      contador++;
      if (contador < numero) {
        cout << i << ", ";
      } else {
        cout << i << " ";
      }
    }
    i++;
  }
}

int main() {
  int numero;
  leer(numero);
  numerosPrimos(numero);
}