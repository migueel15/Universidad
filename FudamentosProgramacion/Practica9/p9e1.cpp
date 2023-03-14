#include <array>
#include <iomanip>
#include <iostream>
#include <string>

using namespace std;
const int MAX_NUMBER = 10;

struct WordProperties {
  int letters;
  int repetition = 0;
};

typedef array<WordProperties, MAX_NUMBER> Array;

struct DataTable {
  Array lista;
  int maxLista = 0;
};

void procesarPalabra(string &palabra, DataTable &Tabla) {
  bool wordExist = false;
  for (int i = 0; i < Tabla.maxLista && !wordExist; i++) {
    if (int(palabra.size()) == Tabla.lista[i].letters) {
      wordExist = true;
      Tabla.lista[i].repetition++;
    }
  }
  if (!wordExist) {
    Tabla.lista[Tabla.maxLista].letters = palabra.size();
    Tabla.lista[Tabla.maxLista].repetition++;
    Tabla.maxLista++;
  }
}

void leerPalabras(string &palabras, DataTable &Table) {
  Table = {};
  cout << "Introduzca un texto (fin para terminar): " << endl;
  cin >> palabras;
  while (palabras != "fin") {
    procesarPalabra(palabras, Table);
    cin >> palabras;
  }
}

void mostrarDatos(DataTable &Tabla) {
  cout << "Longitudes Repeticiones" << endl;
  for (int i = 0; i < Tabla.maxLista; i++) {
    cout << setw(10) << left << Tabla.lista[i].letters << " "
         << Tabla.lista[i].repetition << endl;
  }
}

int main() {
  string palabras;
  DataTable Tabla;
  leerPalabras(palabras, Tabla);
  mostrarDatos(Tabla);
}
