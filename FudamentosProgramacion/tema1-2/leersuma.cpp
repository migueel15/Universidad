#include <iostream>

using namespace std;

main()
{
    int numero;
    cout << "Introuduzca una secuencia de numeros: ";
    cin >> numero;
    cout << "Resultado: ";

    while (numero != 0)
    {
        if ( numero % 2 == 0 ){
            cout << numero << " ";
        }
        cin >> numero;
    }
    cout << endl;
}