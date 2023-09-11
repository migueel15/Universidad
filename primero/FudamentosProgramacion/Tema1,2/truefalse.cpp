#include <iostream>

using namespace std;

int main()
{
    int numero;
    cout << "Introduce un número: ";
    cin >> numero;
    
    bool valor1 = false;
    bool valor2 = true;
    bool par = (numero % 2) == 0;
    bool tres_digitos = ( 100 <= numero && numero <= 999);
    bool tres_digitos_par = ( par && tres_digitos);
    bool primo_10 = ((numero == 2) || (numero == 3) || (numero == 5) || (numero == 7));
    bool divisor_100 = (numero !=0) && ( 100 % numero == 0);

    cout << valor1 << valor2 << par << tres_digitos << tres_digitos_par << primo_10 << divisor_100 << endl;
}