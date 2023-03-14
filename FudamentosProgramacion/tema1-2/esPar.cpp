#include <iostream>

using namespace std;

int main(int argc, char const *argv[])
{
    int numero;
    bool esPar;

    cout << "Introduce un número: ";
    cin >> numero;
    if (numero % 2 == 0)
    {
       cout << boolalpha << true << endl;
    } else cout << boolalpha << false << endl;
    
}