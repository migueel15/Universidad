import libreria.Libreria;
import libreria.LibreriaOferta;

public class PruebaLibreriaOferta {
  public static void main(String[] args) {
    LibreriaOferta libreriaOferta = new LibreriaOferta(20.0, new String[]{"George Orwell", "Isaac Asimov"});

    libreriaOferta.addLibro("george orwell", "1984", 8.20);
    libreriaOferta.addLibro("Philip K. Dick", "¿Sueñan los androides con ovejas eléctricas?", 3.50);
    libreriaOferta.addLibro("Isaac Asimov", "Fundación e Imperio", 9.40);
    libreriaOferta.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
    libreriaOferta.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
    libreriaOferta.addLibro("Isaac Asimov", "La Fundación", 7.30);
    libreriaOferta.addLibro("William Gibson", "Neuromante", 8.30);
    libreriaOferta.addLibro("Isaac Asimov", "Segunda Fundación", 8.10);
    libreriaOferta.addLibro("Isaac Newton", "arithmetica universalis", 7.50);
    libreriaOferta.addLibro("George Orwell", "1984", 6.20);
    libreriaOferta.addLibro("Isaac Newton", "Arithmetica Universalis", 10.50);

    System.out.println(libreriaOferta);

    libreriaOferta.remLibro("George Orwell", "1984");
    libreriaOferta.remLibro("Aldous Huxley", "Un Mundo Feliz");
    libreriaOferta.remLibro("Isaac Newton", "Arithmetica Universalis");

    System.out.println(libreriaOferta);

    libreriaOferta.mostrarPrecioFinal("Philip K. Dick", "¿Sueñan los androides con ovejas eléctricas?");
    libreriaOferta.mostrarPrecioFinal("isaac asimov", "fundación e imperio");
    libreriaOferta.mostrarPrecioFinal("Ray Bradbury", "Fahrenheit 451");
    libreriaOferta.mostrarPrecioFinal("Isaac Asimov", "La Fundación");
    libreriaOferta.mostrarPrecioFinal("william gibson", "neuromante");
    libreriaOferta.mostrarPrecioFinal("Isaac Asimov", "Segunda Fundación");
    libreriaOferta.mostrarPrecioFinal("Isaac Newton", "Arithmetica Universalis");

    System.out.println(libreriaOferta);
  }
}