#include <iostream>
#include <string>

using namespace std;

void leerCadena(string &cadena) {
  cout << "Introduzca un valor numérico: ";
  cin >> cadena;
}

int convertToInt(string &cadena) {
  int valor = 0;
  for (int i = 0; i < int(cadena.size()); i++) {
    int numero = cadena[i] - '0';
    valor = valor * 10 + numero;
  }
  return valor;
}

int calcDoble(int numero) { return numero * 2; }

void mostrarDatos(string &cadena) {
  cout << "Entrada: " << cadena << endl;
  int valor = convertToInt(cadena);
  cout << "Valor: " << valor << endl;
  valor = calcDoble(valor);
  cout << "Doble: " << valor << endl;
}

int main() {
  string cadena;
  leerCadena(cadena);
  mostrarDatos(cadena);
}
