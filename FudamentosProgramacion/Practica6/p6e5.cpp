#include <array>
#include <cmath>
#include <iostream>

using namespace std;
const int MAXIMO = 100;
typedef array<int, MAXIMO> Lista;

struct Datos {
  int MAX = 0;
  Lista lista = {};
};

void crearLista(Datos &datos) {
  do {
    cout << "Introduzca el valor de K (<= " << int(datos.lista.size()) << "): ";
    cin >> datos.MAX;
    if (datos.MAX <= MAXIMO && datos.MAX >= 0) {
      for (int i = 0; i < datos.MAX; i++) {
        datos.lista[i] = i;
      }
    } else {
      cout << "K fuera de rango." << endl;
    }
  } while (datos.MAX > MAXIMO || datos.MAX < 0);
}

void eratostenes(Datos &datos) {
  datos.lista[1] = 0; // Tachamos el 1;

  for (int i = 2; i < sqrt(datos.MAX); i++) {
    for (int j = i; j < datos.MAX; j++) {
      if (datos.lista[j] != i && datos.lista[j] % i == 0) {
        datos.lista[j] = 0;
      }
    }
  }

  // ordenar valores
  int posicion = 0;
  for (int i = 0; i < datos.MAX; i++) {
    if (datos.lista[i] != 0) {
      datos.lista[posicion] = datos.lista[i];
      datos.lista[i] = 0;
      posicion++;
    }
  }
  datos.MAX = posicion;
}

void mostrarPrimos(const Datos &datos) {
  for (int i = 0; i < datos.MAX; i++) {
    cout << datos.lista[i] << " ";
  }
  cout << endl;
}

int main() {
  Datos datos;
  crearLista(datos);
  eratostenes(datos);
  mostrarPrimos(datos);
}
