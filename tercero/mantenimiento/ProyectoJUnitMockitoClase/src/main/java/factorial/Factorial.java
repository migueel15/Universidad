package factorial;

public class Factorial {
    /**
     * Calculo del factorial, devuelve un entero o valor negativo se la entrada es incorrecta.
     * @param n numero para calcular el factorial
     * @return el factorial de n
     */
    public int factorial(int n){
        int result=-1;
        if ((n==0) || (n==1)) result=1;
        else if (n>1) result = n*this.factorial(n-1);
        else
            throw new IllegalArgumentException("Factorial de un número negativo no existe");
        return result;
    }
}
