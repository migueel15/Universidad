#include <iostream>

using namespace std;

int main(int argc, char const *argv[])
{
    int x;
    int y;
    int h;

    cout << "Introduce dos números: ";
    cin >> x >> y;

    h = x;
    x = y;
    y = h;

    cout << "X: " << x << ", Y: " << y <<endl;
}