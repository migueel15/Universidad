#include <iostream>

using namespace std;

int main()
{
    bool ok = (3.0 * (0.1 / 3.0)) == ((3.0 * 0.1) / 3.0);
    cout << "Resultado de (3.0 * (0.1 / 3.0)) == ((3.0 * 0.1) / 3.0): "
    << boolalpha << ok << " -> ERROR" << endl;
}

/*
Este programa comprueba si las dos operaciones son equivalentes.
En caso afirmativo la variable OK devuelve true.
En este caso 1.0 no es igual a 0.1 por lo que devuelve false
*/