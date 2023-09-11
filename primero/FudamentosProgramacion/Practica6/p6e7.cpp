#include <array>
#include <iostream>

using namespace std;

const int MAX = 10;
typedef array<int, MAX> Lista;

struct TLista {
  int max = 0;
  Lista lista = {};
};

void leerLista(TLista &lista1) {
  do {
    cout << "Cuántos números desea introducir (máximo 10): ";
    cin >> lista1.max;
    if (lista1.max > MAX || lista1.max <= 0) {
      cout << "Error. El número no está en rango." << endl;
    }
  } while (lista1.max > MAX || lista1.max <= 0);

  cout << "Introduzca " << lista1.max << " números: ";
  for (int i = 0; i < lista1.max; i++) {
    cin >> lista1.lista[i];
  }
}

void leerReps(int &x) {
  cout << "Introduzca el numero de repeticiones para realizar la criba: ";
  cin >> x;
}

void criba(const TLista &lista1, const int &x, TLista &lista2) {
  int posicionL1 = 0;
  int posicionL2 = 0;
  int repeticiones = 0;

  while (posicionL1 < lista1.max) {

    // check if number is new //
    bool esNuevo = false;
    while (!esNuevo) {
      int contador = 0;
      for (int i = posicionL1 - 1; i >= 0; i--) {
        if (lista1.lista[posicionL1] == lista1.lista[i]) {
          contador++;
        }
      }
      if (contador >= 1) {
        posicionL1++;

        contador = 0;
      } else {
        esNuevo = true;
      }
    }

    // count reps of number in lista1 //
    for (int i = posicionL1; i < lista1.max; i++) {
      if (lista1.lista[i] == lista1.lista[posicionL1]) {
        repeticiones++;
      }
    }

    // add number to lista2 //
    if (repeticiones == x) {
      lista2.lista[posicionL2] = lista1.lista[posicionL1];
      posicionL2++;
      lista2.max++;
    }

    // next position //
    posicionL1++;
    repeticiones = 0;
  }
};

void mostrarLista(const TLista &lista) {
  if (lista.max > 0) {
    cout << "La lista cribada es: ";
    for (int i = 0; i < lista.max; i++) {
      cout << lista.lista[i] << " ";
    }
    cout << endl;
  } else {
    cout << "No hay lista cribada." << endl;
  }
}

int main() {
  int x;
  TLista lista1;
  TLista lista2;
  leerLista(lista1);
  leerReps(x);
  criba(lista1, x, lista2);
  mostrarLista(lista2);
}
