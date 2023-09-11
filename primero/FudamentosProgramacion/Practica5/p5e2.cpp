#include <iostream>
using namespace std;

void leer(int &numero) {
  do {
    if (numero < 0) {

      cout << "Error. ";
    }
    cout << "Introduzca un número: ";
    cin >> numero;
  } while (numero < 0);
}

void calcSigNumero(int &a, int &b) {
  int extra = b;
  b += a;
  a = extra;
}

void mostrarResultado(int posicion, int valor) {
  cout << "fibonacci(" << posicion << "): " << valor;
}

int calcFibonacci(int numero, int a, int b) {
  for (int i = 2; i <= numero; i++) {
    calcSigNumero(a, b);
  }
  return b;
}

int main() {
  int posicion, a = 0, b = 1;
  leer(posicion);
  int resultado = calcFibonacci(posicion, a, b);
  mostrarResultado(posicion, resultado);
}