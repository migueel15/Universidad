#include <iostream>

using namespace std;

void checkError() { cout << "Error. "; }

void leerNumero(int &numeroFilas) {
  do {
    if (numeroFilas < 0 || numeroFilas >= 10) {
      checkError();
    }
    cout << "Introduzca el numero de filas (menor de 10): ";
    cin >> numeroFilas;

  } while (numeroFilas < 0 || numeroFilas >= 10);
}

void guiones(int numeroFilas) {
  numeroFilas = numeroFilas * 2 - 1;

  for (int i = 1; i <= numeroFilas; i++) {
    cout << "-";
  }
}

void addEspacios(int fila, int numeroFilas) {
  int ancho = fila;

  // Añade los espacios correspondientes a la izquierda.
  for (int i = numeroFilas; i > ancho; i--) {
    cout << " ";
  }
}

void addPrimeraMitad(int ancho) {
  for (int i = 1; i <= ancho; i++) {
    cout << i;
  }
}

void addSegundaMitad(int ancho) {
  for (int i = ancho - 1; i >= 1; i--) {
    cout << i;
  }
}

void saltoLinea() { cout << endl; }

void mostrarPiramide(int numeroFilas) {

  for (int fila = 1; fila <= numeroFilas; fila++) {
    int ancho = fila;

    addEspacios(ancho, numeroFilas);
    addPrimeraMitad(ancho);
    addSegundaMitad(ancho);
    saltoLinea();
  }
}

int main() {
  int numeroFilas = 0;

  leerNumero(numeroFilas);
  guiones(numeroFilas);
  saltoLinea();
  mostrarPiramide(numeroFilas);
  guiones(numeroFilas);
  saltoLinea();
}