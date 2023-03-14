#include <iostream>

using namespace std;

int main() {
    int mes, dias = 0;
    bool esIncorrecto = false;

    cout << "Introduzca un mes: ";
    cin >> mes;

    switch (mes) {
    case 1:
    case 3:
    case 5:
    case 7:
    case 8:
    case 10:
    case 12:
        dias = 31;
        break;
    case 4:
    case 6:
    case 9:
    case 11:
        dias = 30;
        break;
    case 2:
        dias = 28;
        break;
    default:
        cout << "Mes Incorrecto" << endl;
        esIncorrecto = true;
        break;
    }
    
    if (!esIncorrecto) {
        cout << "Ese mes tiene " << dias << " dias" << endl;
    }
}