#include <array>
#include <iostream>

using namespace std;

const int TAM = 5;

typedef array<char, TAM> Lista;
typedef array<Lista, TAM> Matriz;

void leerDatos(Matriz &matrizInicial, int &generaciones, bool &esCorrecto) {
  // Leer generaciones
  esCorrecto = true;
  cout << "Introduzca el numero de generaciones: ";
  cin >> generaciones;

  if (generaciones > 0) {
    // Leer matriz
    cout << "Introduzca generacion inicial: " << endl;
    for (int f = 0; f < int(matrizInicial.size()); f++) {
      for (int c = 0; c < int(matrizInicial[f].size()); c++) {
        cin >> matrizInicial[f][c];
      }
    }
    cout << endl;
  } else {
    esCorrecto = false;
    cout << "Error" << endl;
  }
}

int contarAlrededor(Matriz &matriz, int &fila, int &columna) {
  int suma = 0;
  // Quito la propia casilla si esta ocupada para no contarla;
  if (matriz[fila][columna] == 'x') {
    suma--;
  }
  for (int f = fila - 1; f <= fila + 1; f++) {
    for (int c = columna - 1; c <= columna + 1; c++) {
      if (f >= 0 && c >= 0 && f < int(matriz.size()) &&
          c < int(matriz.size())) {
        if (matriz[f][c] == 'x') {
          suma++;
        }
      }
    }
  }
  return suma;
}

void calcularSigGeneracion(Matriz &matriz) {
  Matriz aux = matriz;
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      int alredores = contarAlrededor(aux, f, c);
      if (alredores == 3 && aux[f][c] == 'o') {
        matriz[f][c] = 'x';
      } else if ((alredores == 2 || alredores == 3) && aux[f][c] == 'x') {
        matriz[f][c] = 'x';
      } else {
        matriz[f][c] = 'o';
      }
    }
  }
}

void mostrarMatriz(Matriz &matriz) {
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cout << matriz[f][c];
    }
    cout << endl;
  }
}

void mostrarGeneraciones(Matriz &matriz, int &generaciones) {
  cout << "Genreacion 1 (inicial):" << endl;
  mostrarMatriz(matriz);
  cout << endl;
  for (int i = 2; i <= generaciones; i++) {
    cout << "Generacion " << i << ":" << endl;
    calcularSigGeneracion(matriz);
    mostrarMatriz(matriz);
    cout << endl;
  }
}

int main() {
  bool esCorrecto;
  int generaciones;
  Matriz matriz;
  leerDatos(matriz, generaciones, esCorrecto);
  if (esCorrecto) {
    mostrarGeneraciones(matriz, generaciones);
  }
}