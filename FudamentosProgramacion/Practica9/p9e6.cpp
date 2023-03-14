#include <array>
#include <iostream>
#include <string>
using namespace std;

const int MAXCOL = 4;
const int MAXFIL = 3;

struct s_Numero {
  int numero = 10000000;
  int repeticiones = 0;
};

typedef array<int, MAXCOL> Lista;
typedef array<Lista, MAXFIL> Matriz;

typedef array<s_Numero, 12> ArrayContador;

struct s_Contador {
  ArrayContador array;
  int max = 0;
};

void leerK(int &k) {
  cout << "Introduzca k: ";
  cin >> k;
}

struct index {
  int f;
  int c;
};

void leerColeccion(Matriz &matriz) {
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      cin >> matriz[f][c];
    }
  }
}

int getIndex(s_Contador &arrayContador, int numero) {
  int indice = arrayContador.max;
  for (int i = 0; i < int(arrayContador.array.size()); i++) {
    if (numero == arrayContador.array[i].numero) {
      indice = i;
    }
  }
  return indice;
}

void contarColeccion(const Matriz &matriz, s_Contador &arrayContador) {
  for (int f = 0; f < int(matriz.size()); f++) {
    for (int c = 0; c < int(matriz[f].size()); c++) {
      int index = getIndex(arrayContador, matriz[f][c]);
      if (index == arrayContador.max) {
        arrayContador.array[arrayContador.max].numero = matriz[f][c];
        arrayContador.max++;
      } else {
        arrayContador.array[index].repeticiones++;
      }
    }
  }
}

void ordernarColeccion(s_Contador &contador) {
  for (int i = 0; i < int(contador.array.size()); i++) {
    for (int n = 0; n < int(contador.array.size()); n++) {

      if (contador.array[n].numero < contador.array[n + 1].numero) {
        contador.array[n] = contador.array[n + 1];
      }
    }
  }
}

void mostrarContador(s_Contador &contador) {
  for (int i = 0; i < int(contador.max); i++) {
    cout << contador.array[i].numero
         << " Rep: " << contador.array[i].repeticiones + 1 << endl;
  }
}

int main() {
  int k;
  Matriz matriz;
  s_Contador contador;
  leerK(k);
  leerColeccion(matriz);
  contarColeccion(matriz, contador);
  mostrarContador(contador);
}
