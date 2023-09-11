#include <iostream>

using namespace std;

int main()
{
    int num1, num2, num3, mayor; // 4, 7, 2
    cout << "Introduce 3 números: ";
    cin >> num1 >> num2 >> num3;
    
    if (num1 > num2)
    {
        if (num1 > num3)
        {
            mayor = num1;
        }else
        {
            mayor = num3;
        }
    }
    else
    {
        if (num2 > num3)
        {
            mayor = num2;
        }else
        {
            mayor = num3;
        }
    }

    cout << "El n mayor es: " << mayor << endl;
}
