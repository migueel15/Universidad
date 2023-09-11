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

void calcularBajada(int &secuencia, bool &subida, int &mont, int &valle) {
  int a, b;
  do {
    a = secuencia;
    b = siguienteValor(secuencia);
    mont++;
    valle++;
  } while (b < a && b != 0);
  subida = true;
}

void calcularSubida(int &secuencia, bool &subida, int &mont, int &valle) {
  int a, b;
  do {
    a = secuencia;
    b = siguienteValor(secuencia);
    mont++;
    valle++;
  } while (b > a && b != 0);
  subida = false;
}

void calcularMontValle(int secuencia, int &montMax, int &valleMax) {
  int valle = 1;
  montMax = 1;
  valleMax = 1;
  int a = secuencia;
  int b = siguienteValor(secuencia);
  bool subida = checkPendiente(a, b);

  while (secuencia != 0) {
    if (subida) {
      int mont = 1;
      calcularSubida(secuencia, subida, mont, valleMax);
      if (secuencia != 0) {
        valle = 1;
        calcularBajada(secuencia, subida, mont, valle);
      }
      if (valle > valleMax) {
        valleMax = valle;
      }
      if (mont > montMax) {
        montMax = mont;
      }

    } else {
      int mont = 1;
      valle = 1;
      calcularBajada(secuencia, subida, mont, valle);
      if (valle > valleMax) {
        valleMax = valle;
      }
      if (mont > montMax) {
        montMax = mont;
      }
    }
  }
}

void mostrar(int montMax, int valleMax) {
  cout << "Mayor Montaña: " << montMax << endl
       << "Mayor Valle: " << valleMax << endl;
}

int main() {
  int secuencia, montMax, valleMax;
  leer(secuencia);
  calcularMontValle(secuencia, montMax, valleMax);
  mostrar(montMax, valleMax);
}