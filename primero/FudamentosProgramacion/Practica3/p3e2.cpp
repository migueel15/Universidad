#include <iostream>

using namespace std;

int main() {
    int numeroCoches, precioModelo;
    double precioMedio, precioTotal = 0.0;

    cout << "Introduzca número de modelos de coche: ";
    cin >> numeroCoches;

    for ( int modelo = 1; modelo <= numeroCoches; modelo++) {
        cout << "Precio modelo " << modelo << ": ";
        cin >> precioModelo;

        precioTotal += precioModelo;
    }

    precioMedio = precioTotal / numeroCoches;
    cout << "El valor medio de los " << numeroCoches << " modelos de coche asciende a: " << precioMedio << " €" << endl;
}
