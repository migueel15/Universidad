#include <iostream>
using namespace std;

int main() {
    int numero, i = 0, suma = 0;
    bool esErroneo = false;

    cout << "Introduzca un número: ";
    cin >> numero;

    if ( numero < 0 ) { 
        esErroneo = true;
    }

    while ( i <= numero ) {
        suma += i;
        i++;
    }
    
    if ( esErroneo ) {
        cout << "Error." << endl;
    } else {
        cout << "La suma es: " << suma << endl;
    }   
}
