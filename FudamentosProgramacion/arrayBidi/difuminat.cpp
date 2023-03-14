#include <array>
#include <iostream>

using namespace std;

const int CMAX = 4;
const int FMAX = 3;

typedef array<int, CMAX> Fila;
typedef array<Fila, FMAX> Matriz;


void leerMatriz(Matriz &matriz) {
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cin >> matriz[f][c];
    }
  }
  cout << endl;
}

void difuminar(Matriz &matriz) {
  Matriz auxiliar = matriz;
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      // cada elemento //
      matriz[f][c] = 0;
      int contador = 0;
      for (int frango = f - 1; frango <= f + 1; frango++) {
        for (int crango = c - 1; crango <= c + 1; crango++) {
          if (frango >= 0 && frango < int(matriz.size()) && crango >= 0 &&
              crango < int(matriz[f].size())) {
            matriz[f][c] += auxiliar[frango][crango];
            contador++;
          }
        }
      }
      matriz[f][c] /= contador;
    }
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
  Matriz matriz;
  leerMatriz(matriz);
  difuminar(matriz);
  mostrarMatriz(matriz);
}