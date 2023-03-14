#include <iostream>

using namespace std;

int filas;

int main(int argc, char const *argv[])
{

    int numero;
    cout << "Introduce un numero: " << endl;
    cin >> numero;

    if ( numero < 0) 
    {
        numero *= -1;
    }
    
    cout << numero << endl;


   
    return 0;
}

