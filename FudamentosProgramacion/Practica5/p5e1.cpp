#include <iostream>
using namespace std;

void leer(double &X) {
  do {
    cout << "Introduzca el valor de X [0..1]: ";
    cin >> X;
  } while (X > 1 || X < 0);
}

double calcExponencial(double X, int exponente) {
  double numeroBase = X;
  for (int i = exponente; i >= 2; i--) {
    X *= numeroBase;
  }
  return X;
}

double calcFactorial(int factorial) {
  for (int i = factorial - 1; i >= 1; i--) {
    factorial *= i;
  }
  return factorial;
}

double calcularSucesion(double X) {
  double sumando, resultado = 1;
  int grado = 1;
  do {
    sumando = calcExponencial(X, grado) / calcFactorial(grado);
    resultado += sumando;
    grado++;
  } while (sumando >= 0.0001);
  return resultado;
}

void mostrar(double resultado) { cout << "Serie: " << resultado << endl; }

int main() {
  double X;
  leer(X);
  double resultado = calcularSucesion(X);
  mostrar(resultado);
}