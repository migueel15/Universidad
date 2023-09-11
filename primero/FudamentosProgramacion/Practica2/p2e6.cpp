#include <iostream>

using namespace std;

const double GASTOS = 1;
const double PRIMEROS100 = 0.50;
const double SIG150 = 0.35;
const double RESTO = 0.25;

int main() {
    int consumo;
    double consumoRestante, precio, precioFinal;

    cout << "Introduzca el consumo del contador: ";
    cin >> consumo;

    if (consumo > 250) {
        consumoRestante = consumo - 250;
        precio = 100 * PRIMEROS100 + 150 * SIG150 + consumoRestante * RESTO;
    } 
    else if (consumo > 100 && consumo <= 250) {
        consumoRestante = consumo - 100;
        precio = 100 * PRIMEROS100 + consumoRestante * SIG150;
    }
    else {
        precio = consumo * PRIMEROS100;
    }

    precioFinal = precio + GASTOS;

    cout << "Consumo: " << consumo <<" Kwh. Importe: " << precioFinal << " €" << endl;
}