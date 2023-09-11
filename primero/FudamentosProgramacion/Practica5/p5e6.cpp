#include <iostream>
using namespace std;

void leer(double &X) {
  do {
    cout << "Introduzca el valor de X [0..1]: ";
    cin >> X;
    if (X < 0 || X > 1) {
      cout << "Error. ";
    }
  } while (X < 0 || X > 1);
}

double fraccionIndependiente(int posSumando) {
  double num = 1, den = 2;
  int valorNum = 1, valorDen = 2;
  for (int i = 2; i <= posSumando; i++) {
    valorNum += 2;
    num *= valorNum;
    valorDen += 2;
    den *= valorDen;
  }

  return num / den;
}

double exponencial(double X, int &exponente) {
  double multiplicador = X;
  exponente += 2;
  for (int i = 1; i < exponente; i++) {
    X *= multiplicador;
  }
  return X;
}

double calcularTaylor(double X) {
  int exponente = 1;
  double resultado = X;
  for (int i = 1; i < 7; i++) {
    resultado +=
        (fraccionIndependiente(i) * (exponencial(X, exponente) / exponente));
  }

  return resultado;
}

void mostrar(double resultado) { cout << "Serie: " << resultado; }

int main() {
  double X;
  leer(X);
  double resultado = calcularTaylor(X);
  mostrar(resultado);
}