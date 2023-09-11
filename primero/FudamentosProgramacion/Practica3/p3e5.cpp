#include <iostream>

using namespace std;

int main()
{
    int numero;
    bool esX;

    do
    {
        cout << "Introduzca un número: ";
        cin >> numero;
    } while (numero <= 0);

    for (int i = 1; i <= numero; i++)
    {

        if (i % 2 == 0)
        {
            esX = false;
        }
        else
        {
            esX = true;
        }

        for ( int j = 1; j <= numero; j++ ) {
            if (esX)
            {
                cout << "x";
                esX = false;
            }
            else
            {
                cout << "o";
                esX = true;
            }
        }
        cout << endl;
    }
}
