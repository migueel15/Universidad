#include <iostream>

using namespace std;

const double EQUIVALENCIA = 166.386;

int main(int argc, char const *argv[])
{
    int pesetas;
    cout << "Introduce un número de pesetas: ";
    cin >> pesetas;
    double euros = pesetas / EQUIVALENCIA;
    if (euros == 1)
    {
        cout << pesetas << " pesetas" << " equivalen a " << euros << " euro" <<endl;
    }
    else cout << pesetas << " pesetas"<< " equivalen a " << euros << " euros" <<endl;
    
    return 0;
}
