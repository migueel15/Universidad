#include <iostream>

using namespace std;

void leer(int &a, int &b) {
  cout << "Introduzca dos numeros: ";
  cin >> a >> b;
}

bool mayorQue0(int a, int b) {
  if (a > 0 && b > 0) {
    return true;
  } else {
    return false;
  }
}

bool esMenor(int a, int b) {
  if (a < b) {
    return true;
  } else {
    return false;
  }
}

int sumaDivisores(int numero) {
  int contador = 0;

  for (int divisor = 1; divisor < numero; divisor++) {
    if (numero % divisor == 0) {
      contador += divisor;
    }
  }
  return contador;
}

bool esAmigo(int a, int b) {
  if (esMenor(a, b)) {
    if (sumaDivisores(a) == b && sumaDivisores(b) == a) {
      return true;
    } else {
      return false;
    }
  } else {
    return false;
  }
}

void mostrar(int a, int b) { cout << "Amigos: " << a << ", " << b << endl; }

void mostrarTodosAmigos(int a, int numMax) {
  while (a <= numMax - 1) {
    for (int b = a + 1; b < numMax; b++) {
      if (esAmigo(a, b)) {
        mostrar(a, b);
      };
    }
    a++;
  }
}

void error() { cout << "Error" << endl; }

int main() {
  int a, numMax;
  leer(a, numMax);
  if (mayorQue0(a, numMax) && esMenor(a, numMax)) {
    mostrarTodosAmigos(a, numMax);
  } else {
    error();
  }
}