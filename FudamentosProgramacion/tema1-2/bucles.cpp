#include <iostream>

using namespace std;

int main()
{
    int numero;
    cout << "Introduzca un número: ";
    cin >> numero;
    
    cout << "*" << endl;
    for (int i = 0; i < numero - 2; i++)
    {
        cout << "*";
        for (int j = i; j > 0; j--)
        {
            cout << " ";
        }
        cout << "*" << endl;
    }
    for (int i = 0; i < numero; i++)
    {
        cout << "*";
    }
    cout << endl;
}
