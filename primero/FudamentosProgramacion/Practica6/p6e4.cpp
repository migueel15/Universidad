#include <array>
#include <iostream>

using namespace std;
const int MAXSIZE = 10;
typedef array<int, MAXSIZE> Contador;

void contarNumeros(Contador &contador) {
  cout << "Introduzca una secuencia de números (hasta negativo): ";
  int numero = 0;
  do {
    cin >> numero;
    if (numero >= 0) {
      contador[numero]++;
    }
  } while (numero >= 0);
}

void mostrarGuiones(const int &MAXSIZE) {
  for (int i = 0; i < (MAXSIZE * 2) - 1; i++) {
    cout << "-";
  }
  cout << endl;
}

void mostrarHistograma(Contador &contador) {
  int maxNumber = 0;
  for (int i = 0; i < MAXSIZE; i++) {
    if (contador[i] > maxNumber) {
      maxNumber = contador[i];
    }
  }

  for (int i = maxNumber; i > 0; i--) {
    for (int j = 0; j < MAXSIZE; j++) {
      if (contador[j] == i && i > 0) {
        cout << "* ";
        contador[j]--;
      } else {
        cout << "  ";
      }
    }
    cout << endl;
  }
}

void mostrarNumeros(const int &MAXSIZE) {
  for (int i = 0; i < MAXSIZE; i++) {
    cout << i << " ";
  }
  cout << endl;
}

int main() {
  Contador contador = {};
  contarNumeros(contador);
  mostrarGuiones(MAXSIZE);
  mostrarHistograma(contador);
  mostrarGuiones(MAXSIZE);
  mostrarNumeros(MAXSIZE);
}