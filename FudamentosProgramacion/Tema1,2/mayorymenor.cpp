#include <iostream>

using namespace std;

int main(int argc, char const *argv[])
{
    char letra;
    cout << "Introduce una letra: ";
    cin >> letra;

    bool respuesta = int(letra) >= int('A') && int(letra) <= int('Z');

    cout << boolalpha << respuesta << endl;
}
