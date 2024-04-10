package _3ProdConSincronizados;

public class Productor extends Thread{
    private static java.util.Random r = new java.util.Random();
    private int numIter;
    private Variable<Integer> var;
    public Productor(int numIter,Variable<Integer> var)
    {
        this.numIter = numIter;
        this.var = var;
    }
    public void run()
    {
        int nDato = 0;
        for (int i = 0; i<numIter;i++){
            nDato = r.nextInt(100);
            System.out.println("Productor "+nDato);
            var.almacena(nDato);
        }
    }
}
