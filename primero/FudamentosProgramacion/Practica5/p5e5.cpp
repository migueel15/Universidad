#include <iostream>
using namespace std;

void leer(int &numFilas) {
  do {

    cout << "Introduce el número de filas: ";
    cin >> numFilas;
    if (numFilas <= 0 || numFilas > 10) {
      cout << "Error. ";
    }
  } while (numFilas <= 0 || numFilas > 10);
}

void incremento_circular(int &x, int max) {
  x++;
  if (x >= max) {
    x = 0;
  }
}

void mostrarFilas(int numFilas) {

  for (int fila = 0; fila < numFilas; fila++) {
    int numeroActual = fila;
    for (int columna = 0; columna < numFilas; columna++) {
      cout << numeroActual << " ";
      incremento_circular(numeroActual, numFilas);
    }
    cout << endl;
  }
}

int main() {
  int numFilas;
  leer(numFilas);
  mostrarFilas(numFilas);
}