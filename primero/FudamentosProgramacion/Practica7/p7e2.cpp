#include <array>
#include <iomanip>
#include <iostream>

using namespace std;

const int MAX = 5;

typedef array<int, MAX> Fila;
typedef array<Fila, MAX> Matriz;

void leerMatriz(Matriz &matriz) {
  cout << "Intrduzca " << int(matriz.size()) << " filas de "
       << int(matriz.size()) << " números" << endl;

  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cin >> matriz[f][c];
    }
  }
  cout << endl;
}

void calcSimetrico(const Matriz &matriz, bool &esSimetrico) {
  esSimetrico = true;
  for (int f = 0; f < int(matriz.size()) && esSimetrico; f++) {
    for (int c = f + 1; c < int(matriz[f].size()) && esSimetrico; c++) {
      if (matriz[f][c] != matriz[c][f]) {
        esSimetrico = false;
      }
    }
  }
}

void mostrarResultado(const bool &esSimetrico) {
  if (esSimetrico) {
    cout << "SI es simétrica" << endl;
  } else {
    cout << "NO es simétrica" << endl;
  }
}

void mostrarMatriz(const Matriz &matriz) {
  cout << "La matriz" << endl;
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cout << setw(3) << matriz[f][c] << " ";
    }
    // int(matriz.size())
    cout << endl;
  }
}

int main() {
  Matriz matriz = {};
  bool esSimetrico;
  leerMatriz(matriz);
  calcSimetrico(matriz, esSimetrico);
  mostrarMatriz(matriz);
  mostrarResultado(esSimetrico);
}
