#include <iostream>
#include <iomanip>

using namespace std;

const int seg_1_minuto = 60;
const int seg_1_hora = seg_1_minuto * 60;
const int seg_1_dia = seg_1_hora * 24;
const int seg_1_semana = seg_1_dia * 7;

int main()
{
    int segIniciales, segRestantes, segundos, minutos, horas, dias, semanas;
    cout << "Introduzca los segundos: ";
    cin >> segIniciales;

    semanas = segIniciales / seg_1_semana;
    segRestantes = segIniciales % seg_1_semana;

    dias = segRestantes / seg_1_dia;
    segRestantes = segRestantes % seg_1_dia;

    horas = segRestantes / seg_1_hora;
    segRestantes = segRestantes % seg_1_hora;

    minutos = segRestantes / seg_1_minuto;
    segundos = segRestantes % seg_1_minuto;

    // cout << semanas << " - " << dias << " - " << horas << " - " << minutos << " - " << segundos << endl;
    cout << segIniciales << " segundos equivalen a [" << setfill(' ') << setw(3) << semanas << "] semanas, "
         << dias << " dias "
         << setfill('0') << setw(2) << horas << ":"
         << setfill('0') << setw(2) << minutos << ":"
         << setfill('0') << setw(2) << segundos << endl;
}