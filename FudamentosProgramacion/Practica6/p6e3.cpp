#include <array>
#include <iostream>

using namespace std;
const int MAXSIZE = 10;
typedef array<int, MAXSIZE> Contador;

void contarNumeros(Contador &contador) {
  cout << "Introduzca una secuencia de números (hasta negativo): ";
  int numero;
  do {
    cin >> numero;
    if (numero >= 0) {
      contador[numero]++;
    }
  } while (numero >= 0);
}

void mostrarLista(const Contador &contador) {
  cout << "La frecuencia de cada dígito es: " << endl;
  for (int i = 0; i < MAXSIZE; i++) {
    cout << i << ": " << contador[i] << endl;
  }
}

int main() {
  Contador contador = {};
  contarNumeros(contador);
  mostrarLista(contador);
}