#include <iostream>

using namespace std;

const double EQUIVALENCIA = 166.386;

int main()
{
    int pesetas;
    cout << "Introduzca la cantidad de pesetas: ";
    cin >> pesetas;
    double euros = pesetas / EQUIVALENCIA;
    cout << pesetas << " pesetas equivalen a " << euros << " euros" << endl;
}