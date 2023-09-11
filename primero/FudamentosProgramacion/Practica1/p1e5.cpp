#include <iostream>

using namespace std;

int main()
{
    char letra1, letra2, letra3, letra4;
    char cod1, cod2, cod3, cod4;
    cout << "Introduzca una palabra de 4 letras: ";
    cin >> letra1 >> letra2 >> letra3 >> letra4;
    cod1 = letra1 + 1;
    cod2 = letra2 + 1;
    cod3 = letra3 + 1;
    cod4 = letra4 + 1;
    cout << "La palabra [" << letra1 << letra2 << letra3 << letra4 << "] " <<
            "transformada es [" << cod1 << cod2 << cod3 << cod4 << "]";
}