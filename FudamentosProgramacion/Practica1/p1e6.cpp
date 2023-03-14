#include <iostream>

using namespace std;

const int KILOaBYTE = 1024;
const int MEGAaKILO = 1024;
int main()
{

    int bytesIniciales, mBytes, kBytes, bytesRestantes;
    cout << "Introduzca una cantidad de Bytes: ";
    cin >> bytesIniciales;

    mBytes = bytesIniciales / (MEGAaKILO * KILOaBYTE);
    bytesRestantes = bytesIniciales % (MEGAaKILO * KILOaBYTE);
    kBytes = bytesRestantes / KILOaBYTE;
    bytesRestantes = bytesRestantes % KILOaBYTE;

    cout << bytesIniciales << " Bytes corresponden a:" << endl;
    cout << "Mibytes = " << mBytes << endl;
    cout << "Kibytes = " << kBytes << endl;
    cout << "Bytes = " << bytesRestantes << endl;
}