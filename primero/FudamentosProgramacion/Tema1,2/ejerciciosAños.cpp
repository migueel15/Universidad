#include <iostream>

using namespace std;

int main()
{           
    int dia, mes, year, diasMaximos = 31;
    bool esBisiesto = false, diaCorrecto = false, mesCorrecto = false;

    cout << "Introduce un dia: ";
    cin >> dia;
    cout << "Introduce un mes: ";
    cin >> mes;
    cout << "Introduce un año: ";
    cin >> year;

    if ( year % 400 == 0 || year % 4 == 0 || year % 100 != 0 ){
        esBisiesto = true;
    } else { esBisiesto = false; }
    
    if ( mes == 4 || mes == 6 || mes == 9 || mes == 11 ){
        diasMaximos = 30;
    }

    if ( mes == 2 && esBisiesto ){
        diasMaximos = 29;
    } else { diasMaximos = 28; }

    if ( dia <= diasMaximos && dia >= 1 ){
        diaCorrecto = true;
    }
    
    if ( mes >= 1 && mes <= 12 ){
        mesCorrecto = true;
    }

    if ( diaCorrecto && mesCorrecto ){
        cout << "La fecha " << dia << "-" << mes << "-" << year << " es correcta" << endl;
    }
    else { cout << "La fecha " << dia << "-" << mes << "-" << year << " no es correcta" << endl; }
}