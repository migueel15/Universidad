#include <iostream>

using namespace std;

int main()
{
    char operacion;
    bool resExist = true;

    do
    {
        resExist = true;
        cout << "Operación (+ - * / &): ";
        cin >> operacion;

        if (operacion != '+' && operacion != '-' && operacion != '*' && operacion != '/') {
            if ( operacion != '&' ){
                cout << "ERROR. Operación no válida" << endl;
            }
        } else {
            int a, b, resultado;
            cout << "Operando 1: ";
            cin >> a;
            cout << "Operando 2: ";
            cin >> b;

            switch (operacion)
            {
            case '+':
                resultado = a + b;
                break;
            case '-':
                resultado = a - b;
                break;
            case '*':
                resultado = a * b;
                break;
            case '/':
                if (b == 0)
                {
                    cout << "Error de cálculo. No se puede dividir por 0" << endl;
                    resExist = false;
                    break;
                }
                else
                {
                    resultado = a / b;
                    resExist = true;
                    break;
                }
            default:
                resExist = false;
                break;
            }
            if (resExist)
            {
                cout << "Resultado: " << resultado << endl;
            }
        }

    } while ( operacion != '&' );
    cout << "FIN DEL PROGRAMA";
}
