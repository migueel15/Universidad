#include <iostream>

using namespace std;

int main()
{
    int fracciones, contador = 1;
    double pi = 2, numerador = 2;
    bool error = false;

    do
    {
        if ( error )
        {
            cout << "Error. ";
        }
        cout << "Introduzca el número de fracciones: ";
        cin >> fracciones;

        if ( fracciones <= 0 )
        {
            error = true;
        }

    } while ( fracciones <= 0 );

    for (int i = 0; i < fracciones; i++)
    {
        if ( contador == 1 )
        {
            pi *= ( numerador / (numerador - 1) );
            contador = 2;
        }
        else {
            pi *= ( numerador / (numerador + 1) );
            contador = 1;
        }

        if ( contador == 1 ) {
            numerador += 2;
        }
    }

    cout << "El valor de PI con " << fracciones << " fracciones es: " << pi << endl;
}
