#include <iostream>

using namespace std;

int main()
{
    int numero;
    cout << "Introduce un numero: ";
    cin >> numero;
    
    int n1 = (numero / 100) % 10;

    cout << n1 << endl;
}