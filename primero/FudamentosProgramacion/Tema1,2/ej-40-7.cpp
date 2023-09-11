#include <iostream>

using namespace std;

int main()
{
    double num1, num2, resultado = 0.0;
    char operador;
     // 1, 2, 3, 4
    cout << "Introduce 2 números: ";
    cin >> num1 >> num2;
    cout << "Intoduce un operador aritmético: " << endl;
    cin >> operador;

    switch (operador)
    {
    case '+':
        resultado = num1 + num2;
        break;
    case '-':
        resultado = num1 - num2;
        break;  
    case '*':
        resultado = num1 * num2;
        break; 
    case '/':
        resultado = num1 / num2;
        break; 
    default:
        cout << "El operador no es correcto";
        break;
    }

    cout << "El resultado es: " << resultado << endl;

/*    if (operador == '+')
    {
        resultado = num1 + num2;
    }
    else if (operador == '-')
    {
        resultado = num1 - num2;
    }
    else if (operador == '*')
    {
        resultado = num1 * num2;
    }
    else if (operador == '/')
    {
        resultado = num1 / num2;
    }
    else
    {
        cout << "El operador no es correcto. Introduce (+, -, *, /)" << endl;
    }
    
    cout << "El resuldado es: " << resultado << endl;
*/
}