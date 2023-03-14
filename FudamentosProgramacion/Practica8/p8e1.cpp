#include <iostream>
#include <string>

using namespace std;

void leerString(string &string) {
  cout << "Introduzca una cadena: ";
  getline(cin, string);
}

void ordenarCadena(string &cadena, const int index) {
  cadena = cadena.substr(0, index) + cadena.substr(index + 1);
}

void mostrarCadena(string &cadena) { cout << cadena << endl; }

void eliminar_vocales(string &cadena) {
  int index = 0;
  while (index < int(cadena.size())) {
    if (cadena[index] == 'a' || cadena[index] == 'e' || cadena[index] == 'i' ||
        cadena[index] == 'o' || cadena[index] == 'u') {
      ordenarCadena(cadena, index);
    } else {
      index++;
    }
  }
}

void mostrarResultado(string &cadena) {
  // Mostrar resultado
  cout << "Cadena resultado: ";
  mostrarCadena(cadena);
}

void mostrarOriginal(string &cadena) {
  // Mostrar original
  cout << "Cadena original: ";
  mostrarCadena(cadena);
}

int main() {
  string cadena;
  leerString(cadena);
  mostrarOriginal(cadena);
  eliminar_vocales(cadena);
  mostrarResultado(cadena);
}
