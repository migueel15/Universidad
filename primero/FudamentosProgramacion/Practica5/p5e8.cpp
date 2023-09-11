#include <iostream>
using namespace std;

void leer(int &secuencia) {
  cout << "Introduzca secesión de enteros hsta cero: ";
  cin >> secuencia;
}

int siguienteValor(int &secuencia) {
  cin >> secuencia;
  return secuencia;
}

bool checkPendiente(int a, int b) {
  bool subida;
  if (b > a) {
    subida = true;
  } else {
    subida = false;
  }
  return subida;
}

void calcularBajada(int &secuencia, bool &subida, int &ancho) {
  int a, b;
  do {
    a = secuencia;
    b = siguienteValor(secuencia);
    ancho++;
  } while (b < a && b != 0);
  subida = true;
}

int calcularSubida(int &secuencia, bool &subida, int &ancho) {
  int a, b;
  do {
    a = secuencia;
    b = siguienteValor(secuencia);
    ancho++;
  } while (b > a && b != 0);
  subida = false;
  return ancho;
}

int calcularMontValle(int secuencia) {
  int anchoMax = 1;
  int a = secuencia;
  int b = siguienteValor(secuencia);
  bool subida = checkPendiente(a, b);
  while (secuencia != 0) {
    if (subida) {
      int ancho = 1;
      calcularSubida(secuencia, subida, ancho);
      if (secuencia != 0) {
        calcularBajada(secuencia, subida, ancho);
      }
      if (ancho > anchoMax) {
        anchoMax = ancho;
      }

    } else {
      int ancho = 1;
      calcularBajada(secuencia, subida, ancho);
      if (ancho > anchoMax) {
        anchoMax = ancho;
      }
    }
  }
  return anchoMax;
}

void mostrar(int montMax) { cout << "Mayor Montaña: " << montMax << endl; }

int main() {
  int secuencia, montMax;
  leer(secuencia);
  montMax = calcularMontValle(secuencia);
  mostrar(montMax);
}