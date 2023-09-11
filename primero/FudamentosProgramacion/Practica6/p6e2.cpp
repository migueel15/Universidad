#include <array>
#include <iostream>

using namespace std;
const int MAX = 20;

typedef array<double, MAX> Lista;

struct TEstaturas {
  int maximo = 0;
  Lista lista;
};

void leer_lista(TEstaturas &testatura) {
  do {
    cout << "Cuantas estaturas va a introducir (maximo 20): ";
    cin >> testatura.maximo;
    if (testatura.maximo >= 0 &&
        testatura.maximo <= int(testatura.lista.size())) {
      cout << "Introduzca las " << testatura.maximo << " estaturas: ";
      for (int i = 0; i < testatura.maximo; i++) {
        cin >> testatura.lista[i];
      }
    } else {
      cout << "Argumento no válido." << endl;
    }

  } while (testatura.maximo < 0 ||
           testatura.maximo > int(testatura.lista.size()));
}

double media(const TEstaturas &testatura) {
  double media = 0;
  for (int i = 0; i < testatura.maximo; i++) {
    media += testatura.lista[i];
  }
  return media / testatura.maximo;
}

int alumnosAltos(const TEstaturas &testatura, const double &media) {
  int alumnosAltos = 0;
  for (int i = 0; i < testatura.maximo; i++) {
    if (testatura.lista[i] >= media) {
      alumnosAltos++;
    }
  }
  return alumnosAltos;
}

int alumnosBajos(const TEstaturas &testatura, const double &media) {
  int alumnosBajos = 0;
  for (int i = 0; i < testatura.maximo; i++) {
    if (testatura.lista[i] < media) {
      alumnosBajos++;
    }
  }
  return alumnosBajos;
}

void mostrar_datos(const double &media, const int &aAltos, const int &aBajos) {
  cout << "La media es: " << media << endl;
  cout << "Numero de alumnos más altos que la media: " << aAltos << endl;
  cout << "Numero de alumnos más bajos que la media: " << aBajos << endl;
}

int main() {
  TEstaturas testatura;
  leer_lista(testatura);
  double resmedia = media(testatura);
  int aAltos = alumnosAltos(testatura, resmedia);
  int aBajos = alumnosBajos(testatura, resmedia);
  mostrar_datos(resmedia, aAltos, aBajos);
}