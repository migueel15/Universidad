#include <iostream>
using namespace std;

void leer(char &sucesion) {
  cout << "Introduzca sucesión de ceros y unos hasta punto: ";
  cin.get(sucesion);
}

void compararContadores(int &contadorMayor, int &contador) {

  if (contador > contadorMayor) {
    contadorMayor = contador;
  }
  contador = 1;
}

int subSucesion(char &sucesion) {
  int contadorMayor = 0, contador = 1;
  do {
    char a = sucesion;
    cin.get(sucesion);
    char b = sucesion;

    if (b >= a) {
      contador++;
    } else {
      compararContadores(contadorMayor, contador);
    }

  } while (sucesion != '.');
  return contadorMayor;
}

void mostrar(int contadorMayor) {
  cout << "Mayor susucesión ordenada: " << contadorMayor << endl;
}

int main() {
  char sucesion;
  leer(sucesion);
  int contadorMayor = subSucesion(sucesion);
  mostrar(contadorMayor);
}
