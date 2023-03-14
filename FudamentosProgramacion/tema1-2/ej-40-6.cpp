#include <iostream>

using namespace std;

int main()
{
    int num1, num2, num3, num4, mayor; // 1, 2, 3, 4
    cout << "Introduce 4 números: ";
    cin >> num1 >> num2 >> num3 >> num4;

    mayor = num1;
    if (num2 > mayor)
    {
        mayor = num2;
    }
    if (num3 > mayor)
    {
        mayor = num3;
    }
    if (num4 > mayor)
    {
        mayor = num4;
    }

    cout << "El n mayor es: " << mayor << endl;
}
