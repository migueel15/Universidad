#include <iostream>

using namespace std;

const double IVA = 12;
const double IVACALC = 1 + (IVA/100); // 1.12
const double DESCUENTO = 5;
const double DESCUENTOCALC = DESCUENTO/100; // 0.05

int main() {
    int unidades;
    double precio, precioConIva, precioTotal;

    cout << "Introduzca la cantidad de unidades adquiridas: ";
    cin >> unidades;
    cout << "Introduzca el precio de una unidad: ";
    cin >> precio;

    precioConIva = unidades * precio * IVACALC;

    if (precioConIva > 300) {
        precioTotal = precioConIva - precioConIva * DESCUENTOCALC;
        cout << "Se aplica descuento del " << DESCUENTO << "%" << endl;
    } else {
        precioTotal = precioConIva;
    }
    cout << "El precio total a pagar es: " << precioTotal << "€"<< endl;
}