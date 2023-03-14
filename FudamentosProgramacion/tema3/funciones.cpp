#include <iostream>

using namespace std;

void ordenarValores(int &mayor, int &menor) {
  int extra;
  if (mayor < menor) {
    extra = menor;
    menor = mayor;
    mayor = extra;
  }
}

int sumaTotal(int mayor, int menor, int &contador) {
  int resultado = 0;
  for (int i = 0; i < menor; i++) {
    resultado += mayor;
    contador++;
  }
  return resultado;
}

void mostrar(int resultado, int contador, int mayor, int menor) {
  cout << "El mayor es: " << mayor << endl
       << "El menor es: " << menor << endl
       << "El resultado es: " << resultado << endl
       << "Contador: " << contador << endl;
}

int main() {
  int mayor, menor, contador = 0;
  cout << "Introduzca dos numeros: ";
  cin >> mayor >> menor;
  ordenarValores(mayor, menor);
  int resultado = sumaTotal(mayor, menor, contador);
  mostrar(resultado, contador, mayor, menor);
}