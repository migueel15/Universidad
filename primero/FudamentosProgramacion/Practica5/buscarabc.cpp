#include <iostream>
using namespace std;

void leer(char &frase) {
  cout << "introduzca frase: ";
  cin.get(frase);
}

void buscarABC(char secuencia, bool &aparece) {
  int posicion = 0, posA = 0, posB = 0, posC = 0;

  while (secuencia != '.') {
    posicion++;
    if (secuencia == 'a') {
      posA = posicion;
    }
    if (secuencia == 'b') {
      posB = posicion;
    }
    if (secuencia == 'c') {
      posC = posicion;
    }

    cin.get(secuencia);
    if (posA == posB - 1 && posB == posC - 1) {
      aparece = true;
    }
  }
}

int main() {
  char frase;
  leer(frase);
  bool aparece = false;
  buscarABC(frase, aparece);
  cout << aparece;
}