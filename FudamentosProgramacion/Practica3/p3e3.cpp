#include <iostream>

using namespace std;

int main() {
    int numero;

    do {
        cout << "Introduzca un número: ";
        cin >> numero;
    } while ( numero <= 0);

    for ( int i = 1; i <=numero ; i++) {
        cout << "x";
    }

    cout << endl;
}
