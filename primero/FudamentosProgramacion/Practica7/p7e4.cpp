#include <array>
#include <iostream>

using namespace std;

const int MAX = 10;
typedef array<int, MAX> Lista;

void leerLista(Lista &lista) {
  cout << "Introduzca " << int(lista.size()) << " números: ";
  for (int i = 0; i < int(lista.size()); i++) {
    cin >> lista[i];
  }
}

int buscarMayor(const Lista &lista) {
  int mayor = lista[0];
  for (int i = 0; i < int(lista.size()); i++) {
    if (lista[i] > mayor) {
      mayor = lista[i];
    }
  }
  return mayor;
}

void mostrarDatos(const Lista &lista) {
  int mayor = buscarMayor(lista);

  int ciclos = int(lista.size());
  while (ciclos > 0) {
    int contador = 0;
    for (int i = 0; i < int(lista.size()); i++) {
      if (lista[i] == mayor) {
        contador++;
        ciclos--; // Ahorro ciclos si hay nï¿½ repetidos
      }
    }
    // Mostrar los datos
    cout << mayor << " aparece " << contador;
    if (contador == 1) {
      cout << " vez, en posición ";
    } else {
      cout << " veces, en posiciones ";
    }

    for (int i = 0; i < int(lista.size()); i++) {
      if (lista[i] == mayor) {
        cout << i + 1 << " ";
      }
    }
    cout << endl;

    // Iniciar otro numero mayor (menor que el anterior)
    int aux = mayor;
    int i = 0;
    while (aux >= mayor) {
      aux = lista[i];
      i++;
    }
    for (int i = 0; i < int(lista.size()); i++) {
      if (lista[i] > aux && lista[i] < mayor) {
        aux = lista[i];
      }
    }
    mayor = aux;
  }
}

int main() {
  Lista lista = {};
  leerLista(lista);
  mostrarDatos(lista);
}