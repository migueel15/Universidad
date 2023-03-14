#include <iostream>

using namespace std;

const int valorTeoria = 70, valorPractica = 30;
int main()
{
    double notaTeoria, notaPractica, notaTotal;
    cout << "Introduzca la nota de teoria: ";
    cin >> notaTeoria;
    cout << "Introduzca la nota de practica: ";
    cin >> notaPractica;

    notaTeoria = notaTeoria * valorTeoria / 100;
    notaPractica = notaPractica * valorPractica / 100;
    notaTotal = notaTeoria + notaPractica;

    cout << "La calificación es: " << notaTotal << endl;
}