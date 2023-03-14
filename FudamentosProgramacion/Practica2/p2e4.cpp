#include <iostream>

using namespace std;

int main() {
    int dia, mes, year;
    cout << "Introduzca el dia: ";
    cin >> dia;
    cout << "Introduzca el mes: ";
    cin >> mes;
    cout << "Introduzca el año:  ";
    cin >> year;

    cout << "Dia: " << dia << endl;

    switch (mes) {
    case 1:
        cout << "Mes: Enero" << endl;
        break;
    case 2:
        cout << "Mes: Febrero" << endl;
        break;
    case 3:
        cout << "Mes: Marzo" << endl;
        break;
    case 4:
        cout << "Mes: Abril" << endl;
        break;
    case 5:
        cout << "Mes: Mayo" << endl;
        break;
    case 6:
        cout << "Mes: Junio" << endl;
        break;
    case 7:
        cout << "Mes: Julio" << endl;
        break;
    case 8:
        cout << "Mes: Agosto" << endl;
        break;
    case 9:
        cout << "Mes: Septiembre" << endl;
        break;
    case 10:
        cout << "Mes: Octubre" << endl;
        break;
    case 11:
        cout << "Mes: Noviembre" << endl;
        break;
    case 12:
        cout << "Mes: Diciembre" << endl;
        break;
    
    default:
        cout << "Mes: Error" << endl;
        break;
    }

    cout << "Año: " << year << endl;
}