#include <iostream>

using namespace std;

void leer(int &numero) {
  cout << "Introduzca un n�mero: ";
  cin >> numero;
}

void guiones(int numero) {
  int totalGuiones = numero * 2 - 1;
  for (int i = 1; i <= totalGuiones; i++) {
    cout << "-";
  }
}

void saltoLinea() { cout << endl; }

void mostrarEspacios(int numero, int fila) {
  int numEspacios = numero - fila;
  for (int i = 1; i <= numEspacios; i++) {
    cout << " ";
  }
}

void mostrarRombos(int fila) {
  for (int i = 1; i <= fila; i++) {
    cout << "* ";
  }
}

void primeraParte(int numero) {
  for (int fila = 1; fila <= numero; fila++) {
    mostrarEspacios(numero, fila);
    mostrarRombos(fila);
    saltoLinea();
  }
}

void segundaParte(int numero) {
  for (int fila = numero - 1; fila >= 1; fila--) {
    mostrarEspacios(numero, fila);
    mostrarRombos(fila);
    saltoLinea();
  }
}

void rombo(int numero) {
  primeraParte(numero);
  segundaParte(numero);
}

int main() {
  int numero;
  leer(numero);
  guiones(numero);
  saltoLinea();
  rombo(numero);
  guiones(numero);
  saltoLinea();
}