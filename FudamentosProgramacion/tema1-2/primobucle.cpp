#include <iostream>

using namespace std;

int main()
{
    int num1, num2, extra = 0;
    cout << "Introduce un numero: ";
    cin >> num1;
    cout << "Introduce otro numero: ";
    cin >> num2;

    while (num1 != num2)
    {
        if (num2 > num1)
        {
            extra = num1;
            num1 = num2;
            num2 = extra;
        }
        else
        {
            extra = num2;
        };

        num2 = num1 - num2;
        num1 = extra;
    }
    cout << num1 << " " << num2 << endl;
}
