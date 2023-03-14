import libreria.LibreriaOfertaFlex;
import libreria.OfertaAutor;
import libreria.OfertaFlex;

public class PruebaLibreriaOfertaFlex {
  public static void main(String[] args) {

    OfertaAutor ofertaFlex = new OfertaAutor(20, new String[]{"George Orwell", "Isaac Asimov"});
    LibreriaOfertaFlex libreriaOfertaFlex = new LibreriaOfertaFlex(ofertaFlex);

    libreriaOfertaFlex.addLibro("george orwell", "1984", 8.20);
    libreriaOfertaFlex.addLibro("Philip K. Dick", "¿Sueñan los androides con ovejas eléctricas?", 3.50);
    libreriaOfertaFlex.addLibro("Isaac Asimov", "Fundación e Imperio", 9.40);
    libreriaOfertaFlex.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
    libreriaOfertaFlex.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
    libreriaOfertaFlex.addLibro("Isaac Asimov", "La Fundación", 7.30);
    libreriaOfertaFlex.addLibro("William Gibson", "Neuromante", 8.30);
    libreriaOfertaFlex.addLibro("Isaac Asimov", "Segunda Fundación", 8.10);
    libreriaOfertaFlex.addLibro("Isaac Newton", "arithmetica universalis", 7.50);
    libreriaOfertaFlex.addLibro("George Orwell", "1984", 6.20);
    libreriaOfertaFlex.addLibro("Isaac Newton", "Arithmetica Universalis", 10.50);

    System.out.println(libreriaOfertaFlex);

    libreriaOfertaFlex.remLibro("George Orwell", "1984");
    libreriaOfertaFlex.remLibro("Aldous Huxley", "Un Mundo Feliz");
    libreriaOfertaFlex.remLibro("Isaac Newton", "Arithmetica Universalis");

    System.out.println(libreriaOfertaFlex);

    libreriaOfertaFlex.mostrarPrecioFinal("Philip K. Dick", "¿Sueñan los androides con ovejas eléctricas?");
    libreriaOfertaFlex.mostrarPrecioFinal("isaac asimov", "fundación e imperio");
    libreriaOfertaFlex.mostrarPrecioFinal("Ray Bradbury", "Fahrenheit 451");
    libreriaOfertaFlex.mostrarPrecioFinal("Isaac Asimov", "La Fundación");
    libreriaOfertaFlex.mostrarPrecioFinal("william gibson", "neuromante");
    libreriaOfertaFlex.mostrarPrecioFinal("Isaac Asimov", "Segunda Fundación");
    libreriaOfertaFlex.mostrarPrecioFinal("Isaac Newton", "Arithmetica Universalis");

  }

}
