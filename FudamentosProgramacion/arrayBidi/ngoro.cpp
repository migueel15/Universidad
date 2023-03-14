#include <array>
#include <iostream>

using namespace std;

const int CMAX = 5;
const int FMAX = CMAX - 1;

typedef array<int, CMAX> Lista;
typedef array<Lista, FMAX> Matriz;

void recDiagonal(Matriz &matriz, int &fila, int &col, int &numeroActual) {
  // for (int f = col; f < int(matriz.size()); f++) {
  //   if (f < int(matriz[f].size())) {
  //     matriz[f][col] = numeroActual;
  //     numeroActual++;
  //     col = f;
  //     fila = f;
  //   }
  // }
  for (int f = fila; f < int(matriz.size()); f++) {
    if (col < int(matriz[0].size())) {
      matriz[f][col] = numeroActual;
      numeroActual++;
      fila = f;
      col++;
    }
  }
  cout << "Fila: " << fila << " Col: " << col << endl;
}

void ngoro(Matriz &matriz, int &numeroActual) {
  int col = 0;
  int fila = 0;
  while (numeroActual <= int(matriz.size()) * int(matriz[0].size())) {
    recDiagonal(matriz, fila, col, numeroActual);
    cout << col << " " << fila << endl;

    if (col >= CMAX) {
      col = 0;
    } else {
      col++;
    }
    if (fila >= FMAX) {
      fila = 0;
    } else {
      fila++;
    }
    cout << col << " " << fila << endl;
  }
}

void mostrarMatriz(Matriz &matriz) {
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cout << matriz[f][c] << " ";
    }
    cout << endl;
  }
}

int main() {
  Matriz matriz = {};
  int numeroActual = 1;
  ngoro(matriz, numeroActual);
  mostrarMatriz(matriz);
}