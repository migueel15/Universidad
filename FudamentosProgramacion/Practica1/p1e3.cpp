#include <iostream>

using namespace std;

int main()
{
    int numero1, numero2;
    cout << "Introduzca un número entero: ";
    cin >> numero1;
    cout << "Introduzca otro número entero: ";
    cin >> numero2;
    cout << "El valor del primer número introducido es: " << numero1 << endl;
    cout << "El valor del segundo número introducido es: " << numero2 << endl;
}

/* 
En el primer caso no hay ningún problema ya que los valores introducidos son los esperados (dos valores de tipo int).

En el segundo caso el primer input es el esperado y el programa no se detiene. El segundo input no es de tipo int y
esto corr el canal de entrada de datos. Al ser el último no vemos que haya ocurrido ningún error y el valor es convertido a 0.

En el tercer caso al ser el primer input el erróneo la entrada de datos deja de funcionar y es por esto que la segunda entrada
se salta.

*/