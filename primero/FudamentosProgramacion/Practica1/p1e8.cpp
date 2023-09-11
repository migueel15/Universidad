#include <iostream>

using namespace std;

int main()
{
    char letra1, letra2, letra3, letra4;
    char letraMayus1, letraMayus2, letraMayus3, letraMayus4;
    cout << "Introduzca una palabra de 4 letras: ";
    cin >> letra1 >> letra2 >> letra3 >> letra4;

    letraMayus1 = char('A') + (letra1 - char('a'));
    letraMayus2 = char('A') + (letra2 - char('a'));
    letraMayus3 = char('A') + (letra3 - char('a'));
    letraMayus4 = char('A') + (letra4 - char('a'));

    cout << "La palabra [" << letra1 << letra2 << letra3 << letra4 <<
            "] transformada es [" << letraMayus1 << letraMayus2 << letraMayus3 << letraMayus4 << "]" << endl;
}