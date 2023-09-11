#include <iostream>

using namespace std;

main()
{
    int numero;
    cin >> numero;

    for (int fila = 0; fila < numero; fila++)
    {
        cout << "*";
        if (fila == 0)
        {
            for (int columna = 0; columna < numero-1; columna++)
            {
                cout << "*";
            }
        }
        else
        {
            for (int columna = 0; columna < numero - 2; columna++)
            {
                cout << " ";
            }
            cout << "*" << endl;
        }
    }
}