#include <iostream>

using namespace std;

void leer(int &numeroFilas) {
  cout << "Introduzca numeroFilas de filas: ";
  cin >> numeroFilas;
}

void mostrarGuiones(int numeroFilas) {
  int numeroFilasGuiones = numeroFilas * 2 - 1;

  for (int i = 1; i <= numeroFilasGuiones; i++) {
    cout << "-";
  }
}

void saltoLinea() { cout << endl; }

void mostrarEspacios(int fila, int numeroFilas) {
  int numeroEspacios = numeroFilas - fila;

  for (int i = 1; i <= numeroEspacios; i++) {
    cout << " ";
  }
}

void primeraMitad(int fila, int numeroMedio) {
  for (int num = fila; num <= numeroMedio; num++) {
    if (num >= 10) {
      cout << num % 10;
    } else {
      cout << num;
    }
  }
}

void segundaMitad(int fila, int numeroMedio) {
  if (fila > 1) {
    for (int num = numeroMedio - 1; num >= fila; num--) {
      if (num >= 10) {
        cout << num % 10;
      } else {
        cout << num;
      }
    }
  }
}

void mostrarNumeros(int fila) {
  int numeroMedio = fila * 2 - 1;

  primeraMitad(fila, numeroMedio);
  segundaMitad(fila, numeroMedio);
  saltoLinea();
}

void dibujarPiramide(int numeroFilas) {
  for (int fila = 1; fila <= numeroFilas; fila++) {
    mostrarEspacios(fila, numeroFilas);
    mostrarNumeros(fila);
  }
}

int main() {
  int numeroFilas;
  leer(numeroFilas);
  mostrarGuiones(numeroFilas);
  saltoLinea();
  dibujarPiramide(numeroFilas);
  mostrarGuiones(numeroFilas);
  saltoLinea();
}
