#include <array>
#include <iomanip>
#include <iostream>

using namespace std;

const int MAX = 5;

typedef array<int, MAX> Fila;
typedef array<Fila, MAX> Cuadrado;
typedef array<int, MAX * MAX> arrayContador;

void leerMatriz(Cuadrado &cuadrado) {
  cout << "Introduzca " << int(cuadrado.size()) << " filas de "
       << int(cuadrado[0].size()) << " numeros:" << endl;
  for (int f = 0; f < int(cuadrado.size()); f++) {
    for (int c = 0; c < int(cuadrado[f].size()); c++) {
      cin >> cuadrado[f][c];
    }
  }
  cout << endl;
}

int sumaFila(const Cuadrado &cuadrado, const int &fila) {
  int suma = 0;
  for (int c = 0; c < int(cuadrado[fila].size()); c++) {
    suma += cuadrado[fila][c];
  }
  return suma;
}

int sumaColumna(const Cuadrado &cuadrado, const int &columna) {
  int suma = 0;
  for (int f = 0; f < int(cuadrado.size()); f++) {
    suma += cuadrado[f][columna];
  }
  return suma;
}

int sumaDiagonal1(const Cuadrado &cuadrado) {
  int suma = 0;
  for (int f = 0; f < int(cuadrado.size()); f++) {
    suma += cuadrado[f][f];
  }
  return suma;
}

int sumaDiagonal2(const Cuadrado &cuadrado) {
  int suma = 0;
  for (int f = 0; f < int(cuadrado.size()); f++) {
    suma += cuadrado[f][int(cuadrado.size()) - f - 1];
  }
  return suma;
}

bool contarRepeticiones(const Cuadrado &cuadrado) {
  bool correcto = true;
  arrayContador repeticiones = {};
  for (int f = 0; f < int(cuadrado.size()) && correcto; f++) {
    for (int c = 0; c < int(cuadrado[f].size()) && correcto; c++) {
      if (correcto &&
          cuadrado[f][c] <= int(cuadrado.size()) * int(cuadrado.size()) &&
          cuadrado[f][c] >= 1) {
        repeticiones[cuadrado[f][c] - 1]++;
        correcto = repeticiones[cuadrado[f][c] - 1] == 1;
      }
    }
  }
  return correcto;
}

bool esMagico(const Cuadrado &A) {
  bool esMagico = true;
  int total = sumaDiagonal1(A);

  esMagico = total == sumaDiagonal2(A);
  for (int f = 0; f < int(A.size()) && esMagico; f++) {
    esMagico = total == sumaFila(A, f);
  }
  for (int c = 0; c < int(A.size()) && esMagico; c++) {
    esMagico = total == sumaColumna(A, c);
  }

  // Ver si estan todos los numeros
  if (esMagico) {
    esMagico = contarRepeticiones(A);
  }
  return esMagico;
}

void mostar(const bool &esMagico, const Cuadrado &cuadrado) {
  cout << "El cuadrado: " << endl;

  // mostrar cuadrado
  for (int f = 0; f < int(cuadrado.size()); f++) {
    for (int c = 0; c < int(cuadrado[f].size()); c++) {
      cout << setw(3) << cuadrado[f][c] << " ";
    }
    cout << endl;
  }
  cout << endl;
 //
  if (esMagico) {
    cout << "sí es mágico" << endl;
  } else {
    cout << "no es mágico" << endl;
  }
}

int main() {
  Cuadrado cuadrado;
  leerMatriz(cuadrado);
  bool magico = esMagico(cuadrado);
  mostar(magico, cuadrado);
}