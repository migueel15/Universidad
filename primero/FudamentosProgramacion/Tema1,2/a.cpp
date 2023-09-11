#include <iostream>

using namespace std;

int main(int argc, char const *argv[])
{
    char letra;
    
    cout << "Introduce una letra: ";
    cin >> letra;

    int dist = int(letra) - int('A');

    char minuscula = int('a') + dist;

    cout << minuscula << endl;

    return 0;
}
