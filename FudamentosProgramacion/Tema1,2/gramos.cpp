#include <iostream>

using namespace std;

const int grsToToneladas = 1000000;
const int grsToKilos = 1000;

int main(int argc, char const *argv[])
{
    int grsInput;
    int grsRestantes;
    int toneladas;
    int kilos;
    int gramos;

    cout << "Introduce una cantidad de gramos: ";
    cin >> grsInput;

    toneladas = grsInput / grsToToneladas;
    grsRestantes = grsInput % grsToToneladas;
    kilos = grsRestantes / grsToKilos;
    gramos = grsRestantes % grsToKilos;

    cout << grsInput << " grs equivalen a " << toneladas << " Tn, " << kilos << " Kg, " << gramos << " gr" << endl;

    return 0;
}