#include <iostream>
using namespace std;

void leer(int &primerNum, int &segundoNum) {
  cout << "Introduzca un intervalo (dos números): ";
  cin >> primerNum >> segundoNum;
}

void mostrarError() { cout << "Error" << endl; }

bool intervCorrecto(int primerNum, int segundoNum) {
  return (primerNum < segundoNum && primerNum > 0);
}

int mcd(int primerNum, int segundoNum) {
  int mayor = segundoNum, menor = primerNum;

  while (mayor > menor) {
    int extra = menor;
    menor = mayor - menor;
    mayor = extra;

    if (menor > mayor) {
      int extra = menor;
      menor = mayor;
      mayor = extra;
    }
  }
  return mayor;
}

bool esCoprimo(int mcd) { return mcd == 1; }

void mostrarCoprimos(int primerNum, int segundoNum) {
  cout << "Coprimos: " << primerNum << ", " << segundoNum << endl;
}

void calcCoprimos(int primerNum, int segundoNum) {
  for (int i = primerNum; i <= segundoNum; i++) {
    for (int j = primerNum + 1; j <= segundoNum; j++) {
      int maxcdiv = mcd(i, j);
      if (esCoprimo(maxcdiv)) {
        mostrarCoprimos(i, j);
      }
    }
  }
}

int main() {
  int primerNum, segundoNum;
  leer(primerNum, segundoNum);
  if (intervCorrecto(primerNum, segundoNum)) {
    calcCoprimos(primerNum, segundoNum);
  } else {
    mostrarError();
  }
}