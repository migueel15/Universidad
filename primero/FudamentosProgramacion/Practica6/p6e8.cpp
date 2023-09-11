#include <array>
#include <iostream>

using namespace std;

const int MAX = 5;
typedef array<int, MAX> Vector;

void leerVector(Vector &v) {
  cout << "Introduzca " << MAX << " numeros enteros: " << endl;
  for (int i = 0; i < MAX; i++) {
    cin >> v[i];
  }
}

void centroVector(const Vector &v, bool &existeCentro, int &indiceCentro) {
  for (int i = 1; i < int(v.size()) - 2  && !existeCentro; i++) {
    indiceCentro = i;
    int valorIzquierda = 0;
    int valorDerecha = 0;

    for (int j = 0; j < i; j++) {
      valorIzquierda += (v[j] * (i-j));
    }

    for (int j = indiceCentro + 1; j < int(v.size()); j++) {
      valorDerecha += (v[j] * (j-i));
    }
    if (valorIzquierda == valorDerecha) {
      existeCentro = true;
    }
  }
}

void mostrar(const bool &existeCentro, const int &indice) {
  if (existeCentro) {
    cout << "El centro de este vestor es el indice " << indice << endl;
  } else {
    cout << "Este vector no tiene centro" << endl;
  }
}

int main() {
  bool existeCentro = false;
  int indiceCentro;
  Vector v = {};
  leerVector(v);
  centroVector(v, existeCentro, indiceCentro);
  mostrar(existeCentro, indiceCentro);
}