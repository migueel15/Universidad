#include <iostream>

using namespace std;

int main()
{
    int mayor, menor, contador = 0;
    cout << "Introduzca un numero: ";
    cin >> mayor;
    cout << "Introduzca otro numero: ";
    cin >> menor;

    while (menor <= mayor)
    {
        mayor -= menor;
        contador++;
    }
    cout << "Resto: " << mayor << " Cociente: " << contador;
}
