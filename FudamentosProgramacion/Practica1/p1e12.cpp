#include <iostream>

using namespace std;

int main()
{
    int num1, num2;
    cout << "Introduzca el primer número entero: ";
    cin >> num1;
    cout << "Introduzca el segundo número entero: ";
    cin >> num2;
    int suma = num1 + num2;
    cout << "Primer número: " << num1 << endl;
    cout << "Segundo número: " << num2 << endl;
    cout << "Resultado (num1 + num2): " << suma << endl;
}

/*
Los primeros apartados no dan problema ya que no son números grandes.
Los valores de e) y f) sobrepasan la cantidad de bytes a la que puede llegar
los valores int. Para que funcionasen se deberia utilizar el tipo de dato long. 
*/