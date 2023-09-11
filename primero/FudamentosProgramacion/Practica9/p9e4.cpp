#include <array>
#include <iostream>
#include <string>

using namespace std;
const int MAXPALABRAS = 15;

struct s_Palabra {
  int posInicial;
  int posFinal;
  string texto;
};

typedef array<s_Palabra, MAXPALABRAS> Lista;

struct s_Array {
  Lista lista;
  int max = 0;
};

int indicePalabra(const string &palabra, const s_Array &matriz) {
  int indice = matriz.max;
  bool existe = false;
  for (int i = 0; i < int(matriz.lista.size()) && !existe; i++) {
    if (palabra == matriz.lista[i].texto) {
      existe = true;
      indice = i;
    }
  }

  return indice;
}

void procesarPalabra(const string &palabra, s_Array &matriz, int &posicion) {
  int indice = indicePalabra(palabra, matriz);
  if (indice != matriz.max) {
    matriz.lista[indice].posFinal = posicion;
  } else {
    matriz.max++;
    matriz.lista[indice].texto = palabra;
    matriz.lista[indice].posInicial = posicion;
    matriz.lista[indice].posFinal = posicion;
  }
  posicion++;
}

void leerPalabras(s_Array &matriz) {
  string palabra;
  int posicion = 1;
  cout << "Introduzca el texto en minúsculas hasta (fin):" << endl;
  cin >> palabra;
  while (palabra != "fin") {
    procesarPalabra(palabra, matriz, posicion);
    cin >> palabra;
  }
}

void mostrarPalabras(s_Array &array) {
  for (int i = 0; i < array.max; i++) {
    cout << array.lista[i].texto << " " << array.lista[i].posInicial << " "
         << array.lista[i].posFinal << endl;
  }
}

int main() {
  s_Array lista;
  leerPalabras(lista);
  mostrarPalabras(lista);
}
