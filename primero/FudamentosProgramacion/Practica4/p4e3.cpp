#include <iostream>

using namespace std;

void leer(int &a, int &b) {
  cout << "Introduzca dos numeros: ";
  cin >> a >> b;
}

bool esDistinto(int a, int b) {
  if (a < b || a > b) {
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
  if (esDistinto(a, b)) {
    if (sumaDivisores(a) == b && sumaDivisores(b) == a) {
      return true;
    } else {
      return false;
    }
  } else {
    return false;
  }
}

void mostrarAmigos(int a, int b) {
  cout << a << " y " << b << " son amigos" << endl;
}

void mostrarNoAmigos(int a, int b) {
  cout << a << " y " << b << " no son amigos" << endl;
}

int main() //
{
  int a, b;
  leer(a, b);
  if (esAmigo(a, b)) {
    mostrarAmigos(a, b);

  } else {
    mostrarNoAmigos(a, b);
  }
}