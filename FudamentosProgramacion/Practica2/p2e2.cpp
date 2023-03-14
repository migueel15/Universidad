#include <iostream>

using namespace std;

int main() {
  int num1, num2, num3;
  cout << "Introduzca tres números enteros: ";
  cin >> num1 >> num2 >> num3;

  if (num1 > num2 && num1 > num3) {
    cout << "El numero mayor es: " << num1 << endl;
  } else if (num2 > num1 && num2 > num3) {
    cout << "El numero mayor es: " << num2 << endl;
  } else if (num3 > num1 && num3 > num2) {
    cout << "El numero mayor es: " << num3 << endl;
  } else {
    cout << "No existe un único número mayor" << endl;
  }
}
