#include <array>
#include <iomanip>
#include <iostream>

using namespace std;
const int MAX = 5;

typedef array<int, MAX> Fila;
typedef array<Fila, MAX> Cuadrado;

void cambiarPosicion(Cuadrado &cuadrado, int &f, int &c) {
  int fant = f;
  int cant = c;
  if (fant == 0) {
    f = int(cuadrado.size()) - 1;
  } else {
    f -= 1;
  }
  if (cant == 0) {
    c = int(cuadrado.size()) - 1;
  } else {
    c -= 1;
  }
  // Combrobar si el siguiente ya está puesto
  if (cuadrado[f][c] > 0) {
    f = fant + 1;
    c = cant;
  }
}

void construirMagico(Cuadrado &A) {
  A = {};
  if ((int(A.size())) % 2 != 0) {
    int f = 0;
    int c = int(A[0].size()) / 2;
    int contador = 1;

    while (contador <= int(A.size()) * int(A.size())) {
      A[f][c] = contador;
      cambiarPosicion(A, f, c);

      contador++;
    }
  }
}

void mostarCuadrado(Cuadrado &cuadrado) {
  if ((int(cuadrado.size())) % 2 != 0) {

    cout << "El cuadrado magico para N = " << int(cuadrado.size())
         << " es:" << endl;
    for (int f = 0; f < int(cuadrado.size()); f++) {
      for (int c = 0; c < int(cuadrado[f].size()); c++) {
        cout << setw(3) << cuadrado[f][c] << " ";
      }
      cout << endl;
    }
  }
}

int main() {
  Cuadrado cuadrado;
  construirMagico(cuadrado);
  mostarCuadrado(cuadrado);
}
