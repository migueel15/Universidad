#include <array>
#include <iostream>

using namespace std;

const int ARRAYSIZE = 5;
typedef array<double, ARRAYSIZE> Vector;

void leer_lista(Vector &v) {
  cout << "Introduzca " << ARRAYSIZE << " números: ";
  for (int i = 0; i < ARRAYSIZE; i++) {
    cin >> v[i];
  }
}

double buscar_mayor(const Vector &v) {
  double mayor = v[0];
  for (int i = 1; i < int(v.size()); i++) {
    if (v[i] > mayor) {
      mayor = v[i];
    }
  }
  return mayor;
}

void mostrar_mayor(double &mayor) {
  cout << "El mayor elemento de la lista es " << mayor << endl;
}

void mostrar_lista(const Vector &v) {
  cout << "Lista: ";
  for (int i = 0; i < int(v.size()); i++) {
    cout << v[i] << " ";
  }
  cout << endl;
}

int main() {
  Vector lista;
  leer_lista(lista);
  double mayor = buscar_mayor(lista);
  mostrar_mayor(mayor);
  mostrar_lista(lista);
}