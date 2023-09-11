#include <iostream>

using namespace std;

int main()
{
    char c;
    int caracteres = 0;
    cout << "Introduzca el texto terminado en punto: " << endl;
    cin.get(c);

    while (c != '.')
    {
        cout << int(c) << " ";
        caracteres += 1;
        cin.get(c);
    }
    cout << endl << "Número de caracteres leídos: " << caracteres << endl;
}
