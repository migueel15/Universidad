#include <iostream>

using namespace std;

int main() { 
    int numero;
    cout << "Introduzca un número entero: ";
    cin >> numero;

    if ( numero < 0 ){
        cout << "El número " << numero << " si es negativo" << endl;
    } else {
        cout << "El número " << numero << " no es negativo" << endl;
    }
}
