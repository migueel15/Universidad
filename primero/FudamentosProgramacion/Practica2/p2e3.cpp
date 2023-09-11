#include <iostream>

using namespace std;

int main() {
    char letra;
    cout << "Introduce un carácter: ";
    cin >> letra;

    if ((letra >= 'A' && letra <= 'Z') || (letra >= 'a' && letra <= 'z')) {
        cout << "Es letra" << endl;
    }
    else if (letra == '.') {
        cout << "Es punto" << endl;
    } else {
        cout << "Error" << endl;
    }
}