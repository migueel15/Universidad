#include <iostream>

using namespace std;

int main()
{
    int a = 6;
    int b = 14;
    int auxiliar;
    cout << "a vale " << a << " y b vale " << b << endl;
    // ¿Qué hacen estas tres sentencias?
    auxiliar = a;
    a = b;
    b = auxiliar;
    cout << "a vale " << a << " y b vale " << b << endl;
}

/*
Se utiliza una variable auxiliar para poder hacer un cambio de valor.
Se intecambian los valores de a y b.
*/