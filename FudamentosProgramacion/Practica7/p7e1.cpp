#include <array>
#include <iomanip>
#include <iostream>

using namespace std;

const int MAXFIL = 5;
const int MAXCOL = 7;

typedef array<int, MAXCOL> Fila;
typedef array<Fila, MAXFIL> Matriz;

void leerMatriz(Matriz &matriz) {
  cout << "Introduzca " << int(matriz.size()) << " filas de "
       << int(matriz[0].size()) << " números" << endl;

  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cin >> matriz[f][c];
    }
  }
}

void buscar_mayor2d(const Matriz &m, int &mayor, int &fila, int &columna) {
  fila = 0;
  columna = 0;
  mayor = m[0][0];
  for (int f = 0; f < int(m.size()); f++) {
    for (int c = 0; c < int(m[f].size()); c++) {
      if (m[f][c] >= mayor) {
        mayor = m[f][c];
        fila = f;
        columna = c;
      }
    }
  }
}

void mostrarMayor(const int &mayor, const int &fila, const int &columna) {
  cout << "El número " << mayor << " es el mayor elemento de la matriz" << endl;
  cout << "Se encuentra en [" << fila << "]"
       << "[" << columna << "]" << endl;
}

void mostrarMatriz(const Matriz &matriz) {
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cout << setw(3) << matriz[f][c] << " ";
    }
    cout << endl;
  }
}
int main() {
  Matriz matriz;
  int fila, columna, mayor;
  leerMatriz(matriz);
  buscar_mayor2d(matriz, mayor, fila, columna);
  mostrarMayor(mayor, fila, columna);
  mostrarMatriz(matriz);
}
