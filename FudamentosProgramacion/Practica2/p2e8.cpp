#include <iostream>

using namespace std;

int main() {
  int codigo, provincia, operacion, digControl;
  bool codigoCorrecto = false;

  cout << "Introduzca el código numérico de 4 dígitos: ";
  cin >> codigo;

  if (codigo >= 1000 && codigo <= 9999) {
    codigoCorrecto = true;
  }

  if (codigoCorrecto) {
    provincia = codigo / 1000;
    operacion = (codigo / 10) - (codigo / 1000 * 100);
    digControl = codigo % 10;

    cout << "Provincia: " << provincia << endl
         << "Número de operación: " << operacion << endl
         << "Dígito de control: " << digControl << endl
         << "Comprobación: ";

    if (operacion * provincia % 10 == digControl) {
      cout << "correcto" << endl;
    } else {
      cout << "error" << endl;
    }
  } else {
    cout << "Código erróneo" << endl;
  }
}
