#include <iostream>

using namespace std;

int main() {
    int numero, suma = 0;
    bool esErroneo = false;

    cout << "Introduzca un número: ";
    cin >> numero;

    if ( numero < 0 ) { 
        esErroneo = true;
    }

    for ( int i = 0; i <= numero; i++ ) {
        suma += i;
    }

    if ( esErroneo ) {
        cout << "Error." << endl;
    } else {
        cout << "La suma es: " << suma << endl;
    } 
}
