
//--------------------------------------------------------------------------
import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
//--------------------------------------------------------------------------

import libreria.*;

//--------------------------------------------------------------------------

public class TestRunnerPr23Ej1 {
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestLibro {
		private Libro lb1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Libro JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Libro JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			lb1 = new Libro("Isaac Asimov", "La Fundacion", 7.30);
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void libroCtorTest1() {
			assertEquals("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", 7.30): Autor:", "Isaac Asimov", lb1.getAutor());
			assertEquals("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", 7.30): Titulo:", "La Fundacion", lb1.getTitulo());
			assertEquals("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", 7.30): PrecioBase:", 7.30, lb1.getPrecioBase(), 1e-6);
			assertEquals("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", 7.30): PorcIva:", 10.00, Libro.getIVA(), 1e-6);
		}
		//@Test(timeout = 1000)
		//public void libroCtorTestX1() {
		//	try {
		//		Libro lb2 = new Libro("Isaac Asimov", "La Fundacion", -7.30);
		//		fail("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", -7.30): No se lanzo ninguna excepcion");
		//	} catch (RuntimeException e) {
		//		//assertEquals("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", -7.30): exception.getMessage():", "", e.getMessage());
		//	} catch (Exception e) {
		//		fail("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", -7.30): la excepcion lanzada no es RuntimeException");
		//	}
		//}
		@Test(timeout = 1000)
		public void libroPrecioFinalTest1() {
			precond("Isaac Asimov", lb1.getAutor());
			precond("La Fundacion", lb1.getTitulo());
			precond(7.30, lb1.getPrecioBase(), 1e-6);
			precond(10.0, Libro.getIVA(), 1e-6);
			assertEquals("\n> Error: lb1.getPrecioFinal(): ", 8.03, lb1.getPrecioFinal(), 1e-6);
		}
		@Test(timeout = 1000)
		public void libroSetIvaTest1() {
			precond("Isaac Asimov", lb1.getAutor());
			precond("La Fundacion", lb1.getTitulo());
			precond(7.30, lb1.getPrecioBase(), 1e-6);
			precond(10.0, Libro.getIVA(), 1e-6);
			Libro.setIVA(20.00);
			double ivaActual = Libro.getIVA();
			Libro.setIVA(10.00);
			assertEquals("\n> Error: Libro.setIVA(20.00): ", 20.00, ivaActual, 1e-6);
		}
		@Test(timeout = 1000)
		public void libroToStringTest1() {
			precond("Isaac Asimov", lb1.getAutor());
			precond("La Fundacion", lb1.getTitulo());
			precond(7.30, lb1.getPrecioBase(), 1e-6);
			precond(10.0, Libro.getIVA(), 1e-6);
			assertEquals("\n> Error: lb1.toString():",
						 normalize("(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03)"),
						 normalize(lb1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestLibroOferta {
		private LibroOferta lo1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of LibroOferta JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of LibroOferta JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			lo1 = new LibroOferta("Isaac Asimov", "La Fundacion", 7.30, 20.0);
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void libroOfertaCtorTest1() {
			assertTrue("\n> Error: LibroOferta extends Libro:", ((Object)lo1 instanceof Libro));
			assertEquals("\n> Error: new LibroOferta(\"Isaac Asimov\", \"La Fundacion\", 7.30, 20.0): Autor:", "Isaac Asimov", lo1.getAutor());
			assertEquals("\n> Error: new LibroOferta(\"Isaac Asimov\", \"La Fundacion\", 7.30, 20.0): Titulo:", "La Fundacion", lo1.getTitulo());
			assertEquals("\n> Error: new LibroOferta(\"Isaac Asimov\", \"La Fundacion\", 7.30, 20.0): PrecioBase:", 7.30, lo1.getPrecioBase(), 1e-6);
			assertEquals("\n> Error: new LibroOferta(\"Isaac Asimov\", \"La Fundacion\", 7.30, 20.0): PorcDto:", 20.00, lo1.getDescuento(), 1e-6);
			assertEquals("\n> Error: new LibroOferta(\"Isaac Asimov\", \"La Fundacion\", 7.30, 20.0): PorcIva:", 10.00, LibroOferta.getIVA(), 1e-6);
		}
		//@Test(timeout = 1000)
		//public void libroOfertaCtorTestX1() {
		//	try {
		//		Libro lb2 = new Libro("Isaac Asimov", "La Fundacion", -7.30, -20.0);
		//		fail("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", -7.30, -20.0): No se lanzo ninguna excepcion");
		//	} catch (RuntimeException e) {
		//		//assertEquals("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", -7.30, -20.0): exception.getMessage():", "", e.getMessage());
		//	} catch (Exception e) {
		//		fail("\n> Error: new Libro(\"Isaac Asimov\", \"La Fundacion\", -7.30, -20.0): la excepcion lanzada no es RuntimeException");
		//	}
		//}
		@Test(timeout = 1000)
		public void libroOfertaPrecioFinalTest1() {
			precond("Isaac Asimov", lo1.getAutor());
			precond("La Fundacion", lo1.getTitulo());
			precond(7.30, lo1.getPrecioBase(), 1e-6);
			precond(20.0, lo1.getDescuento(), 1e-6);
			precond(10.0, LibroOferta.getIVA(), 1e-6);
			assertEquals("\n> Error: lo1.getPrecioFinal(): ", 6.424, lo1.getPrecioFinal(), 1e-6);
		}
		@Test(timeout = 1000)
		public void libroOfertaSetIvaTest1() {
			precond("Isaac Asimov", lo1.getAutor());
			precond("La Fundacion", lo1.getTitulo());
			precond(7.30, lo1.getPrecioBase(), 1e-6);
			precond(20.0, lo1.getDescuento(), 1e-6);
			precond(10.0, LibroOferta.getIVA(), 1e-6);
			Libro.setIVA(20.00);
			double ivaActual = Libro.getIVA();
			Libro.setIVA(10.00);
			assertEquals("\n> Error: Libro.setIVA(20.00): ", 20.00, ivaActual, 1e-6);
		}
		@Test(timeout = 1000)
		public void libroOfertaToStringTest1() {
			precond("Isaac Asimov", lo1.getAutor());
			precond("La Fundacion", lo1.getTitulo());
			precond(7.30, lo1.getPrecioBase(), 1e-6);
			precond(20.0, lo1.getDescuento(), 1e-6);
			precond(10.0, LibroOferta.getIVA(), 1e-6);
			assertEquals("\n> Error: lo1.toString():",
						 normalize("(Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995)"),
						 normalize(lo1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestLibreria {
		private Libreria lr1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Libreria JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Libreria JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			lr1 = new Libreria();
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void libreriaCtorTest1() {
			Libreria lr2 = new Libreria();
			assertEquals("\n> Error: new Libreria(): toString():",
						 normalize("[]"),
						 normalize(lr2.toString()));
		}

		@Test(timeout = 1000)
		public void libreriaAddLibroTest1() {
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			assertEquals("\n> Error: addLibro(): toString():",
						 normalize("[(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lr1.toString()));
		}
		@Test(timeout = 1000)
		public void libreriaAddLibroTest2() {
			lr1.addLibro("isaac asimov", "la fundacion", 5.30);
			lr1.addLibro("aldous huxley", "un mundo feliz", 4.50);
			lr1.addLibro("william gibson", "neuromante", 6.30);
			lr1.addLibro("george orwell", "1984", 4.20);
			lr1.addLibro("ray bradbury", "fahrenheit 451", 5.40);
			//------------------------
			assertEquals("\n> Error: addLibro(): toString():",
						 normalize("[(isaac asimov; la fundacion; 5.3; 10.0%; 5.83), (aldous huxley; un mundo feliz; 4.5; 10.0%; 4.95), (william gibson; neuromante; 6.3; 10.0%; 6.93), (george orwell; 1984; 4.2; 10.0%; 4.62), (ray bradbury; fahrenheit 451; 5.4; 10.0%; 5.94)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("George Orwell", "1984", 6.20);
			//------------------------
			assertEquals("\n> Error: addLibro(): toString():",
						 normalize("[(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lr1.toString()));
		}
		@Test(timeout = 1000)
		public void libreriaGetPrecioFinalTest1() {
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			precond(normalize("[(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
					normalize(lr1.toString()));
			//------------------------
			assertEquals("\n> Error: getPrecioFinal(Isaac Asimov, La Fundacion):", 8.03, lr1.getPrecioFinal("Isaac Asimov", "La Fundacion"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(Aldous Huxley, Un Mundo Feliz):", 7.15, lr1.getPrecioFinal("Aldous Huxley", "Un Mundo Feliz"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(William Gibson, Neuromante):", 9.13, lr1.getPrecioFinal("William Gibson", "Neuromante"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(George Orwell, 1984):", 6.82, lr1.getPrecioFinal("George Orwell", "1984"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(Ray Bradbury, Fahrenheit 451):", 8.14, lr1.getPrecioFinal("Ray Bradbury", "Fahrenheit 451"), 1e-6);
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaGetPrecioFinalTest2() {
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			precond(normalize(" [(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
					normalize(lr1.toString()));
			//------------------------
			assertEquals("\n> Error: getPrecioFinal(isaac asimov, la fundacion):", 8.03, lr1.getPrecioFinal("isaac asimov", "la fundacion"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(aldous huxley, un mundo feliz):", 7.15, lr1.getPrecioFinal("aldous huxley", "un mundo feliz"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(william gibson, neuromante):", 9.13, lr1.getPrecioFinal("william gibson", "neuromante"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(george orwell, 1984):", 6.82, lr1.getPrecioFinal("george orwell", "1984"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(ray bradbury, fahrenheit 451):", 8.14, lr1.getPrecioFinal("ray bradbury", "fahrenheit 451"), 1e-6);
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaGetPrecioFinalTest3() {
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			precond(normalize("[(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
					normalize(lr1.toString()));
			//------------------------
			try {
				double precioFinal = lr1.getPrecioFinal("xxx", "xxx");
				fail("\n> Error: getPrecioFinal(xxx, xxx): No se lanzo ninguna excepcion");
			} catch (RuntimeException e) {
				assertEquals("\n> Error: getPrecioFinal(xxx, xxx): exception.getMessage()",
							 normalize("Libro no encontrado (xxx, xxx)"),
							 normalize(e.getMessage()));
			} catch (Exception e) {
				fail("\n> Error: getPrecioFinal(xxx, xxx): la excepcion lanzada no es RuntimeException");
			}
		}
		@Test(timeout = 1000)
		public void libreriaRemLibroTest1() {
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			//------------------------
			//System.out.println("DBG: "+lr1.toString()); assertTrue("",false);
			precond(normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
					normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("Isaac Asimov", "La Fundacion");
			assertEquals("\n> Error: remLibro(Isaac Asimov, La Fundacion): toString():",
						 normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("Aldous Huxley", "Un Mundo Feliz");
			assertEquals("\n> Error: remLibro(Aldous Huxley, Un Mundo Feliz): toString():",
						 normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("William Gibson", "Neuromante");
			assertEquals("\n> Error: remLibro(William Gibson, Neuromante): toString():",
						 normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("George Orwell", "1984");
			assertEquals("\n> Error: remLibro(George Orwell, 1984): toString():",
						 normalize("[(Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("Ray Bradbury", "Fahrenheit 451");
			assertEquals("\n> Error: remLibro(Ray Bradbury, Fahrenheit 451): toString():",
						 normalize("[]"),
						 normalize(lr1.toString()));
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaRemLibroTest2() {
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			//------------------------
			precond(normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
					normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("isaac asimov", "la fundacion");
			assertEquals("\n> Error: remLibro(isaac asimov, la fundacion): toString():",
						 normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("aldous huxley", "un mundo feliz");
			assertEquals("\n> Error: remLibro(aldous huxley, un mundo feliz): toString():",
						 normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("william gibson", "neuromante");
			assertEquals("\n> Error: remLibro(william gibson, neuromante): toString():",
						 normalize("[(George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("george orwell", "1984");
			assertEquals("\n> Error: remLibro(george orwell, 1984): toString():",
						 normalize("[(Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lr1.toString()));
			//------------------------
			lr1.remLibro("ray bradbury", "fahrenheit 451");
			assertEquals("\n> Error: remLibro(ray bradbury, fahrenheit 451): toString():",
						 normalize("[]"),
						 normalize(lr1.toString()));
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaRemLibroTest3() {
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			precond(normalize("[(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
					normalize(lr1.toString()));
			//------------------------
			try {
				lr1.remLibro("xxx", "xxx");
				fail("\n> Error: remLibro(xxx, xxx): No se lanzo ninguna excepcion");
			} catch (RuntimeException e) {
				assertEquals("\n> Error: remLibro(xxx, xxx): exception.getMessage()",
							 normalize("Libro no encontrado (xxx, xxx)"),
							 normalize(e.getMessage()));
			} catch (Exception e) {
				fail("\n> Error: remLibro(xxx, xxx): la excepcion lanzada no es RuntimeException");
			}
		}
		@Test(timeout = 1000)
		public void libreriaToStringTest1() {
			assertEquals("\n> Error: lr1.toString():",
						 normalize("[]"),
						 normalize(lr1.toString()));
		}
		@Test(timeout = 1000)
		public void libreriaToStringTest2() {
			lr1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lr1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lr1.addLibro("William Gibson", "Neuromante", 8.30);
			lr1.addLibro("George Orwell", "1984", 6.20);
			lr1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			assertEquals("\n> Error: lr1.toString():",
						 normalize("[(Isaac Asimov; La Fundacion; 7.3; 10.0%; 8.03), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 10.0%; 6.82), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lr1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestLibreriaOferta {
		private static final String[] autoresOferta = { "george orwell", "isaac asimov" };
		private LibreriaOferta lro1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of LibreriaOferta JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of LibreriaOferta JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			lro1 = new LibreriaOferta(20.0, autoresOferta);
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void libreriaOfertaCtorTest1() {
			assertTrue("\n> Error: LibreriaOferta extends Libreria:", ((Object)lro1 instanceof Libreria));
			assertEquals("\n> Error: new LibreriaOferta(): toString():",
						 normalize("20.0%[george orwell, isaac asimov][]"),
						 normalize(lro1.toString()));
		}
		@Test(timeout = 1000)
		public void libreriaOfertaGetDescuentoTest1() {
			assertEquals("\n> Error: getDescuento():", 20.00, lro1.getDescuento(), 1e-6);
		}
		@Test(timeout = 1000)
		public void libreriaOfertaGetOfertaTest1() {
			assertArrayEquals("\n> Error: getOferta():", autoresOferta, lro1.getOferta());
		}
		@Test(timeout = 1000)
		public void libreriaOfertaSetOfertaTest1() {
			String[] autoresOferta2 = { "Aldous Huxley", "William Gibson" };
			lro1.setOferta(35.00, autoresOferta2);
			assertEquals("\n> Error: getDescuento():", 35.00, lro1.getDescuento(), 1e-6);
			assertArrayEquals("\n> Error: getOferta():", autoresOferta2, lro1.getOferta());
		}
		@Test(timeout = 1000)
		public void libreriaOfertaAddLibroTest1() {
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			//System.out.println("DBG: "+lro1.toString()); assertTrue("",false);
			assertEquals("\n> Error: addLibro(): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lro1.toString()));
		}
		@Test(timeout = 1000)
		public void libreriaOfertaAddLibroTest2() {
			lro1.addLibro("isaac asimov", "la fundacion", 5.30);
			lro1.addLibro("aldous huxley", "un mundo feliz", 4.50);
			lro1.addLibro("william gibson", "neuromante", 6.30);
			lro1.addLibro("george orwell", "1984", 4.20);
			lro1.addLibro("ray bradbury", "fahrenheit 451", 5.40);
			//------------------------
			assertEquals("\n> Error: addLibro(): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(isaac asimov; la fundacion; 5.3; 20.0%; 4.24; 10.0%; 4.664000000000001), (aldous huxley; un mundo feliz; 4.5; 10.0%; 4.95), (william gibson; neuromante; 6.3; 10.0%; 6.93), (george orwell; 1984; 4.2; 20.0%; 3.3600000000000003; 10.0%; 3.696), (ray bradbury; fahrenheit 451; 5.4; 10.0%; 5.94)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("George Orwell", "1984", 6.20);
			//------------------------
			assertEquals("\n> Error: addLibro(): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lro1.toString()));
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaOfertaGetPrecioFinalTest1() {
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			//System.out.println("DBG: "+lro1.toString()); assertTrue("",false);
			precond(normalize("20.0%[george orwell, isaac asimov] [(Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
					normalize(lro1.toString()));
			//------------------------
			assertEquals("\n> Error: getPrecioFinal(Isaac Asimov, La Fundacion):", 6.424, lro1.getPrecioFinal("Isaac Asimov", "La Fundacion"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(Aldous Huxley, Un Mundo Feliz):", 7.15, lro1.getPrecioFinal("Aldous Huxley", "Un Mundo Feliz"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(William Gibson, Neuromante):", 9.13, lro1.getPrecioFinal("William Gibson", "Neuromante"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(George Orwell, 1984):", 5.456, lro1.getPrecioFinal("George Orwell", "1984"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(Ray Bradbury, Fahrenheit 451):", 8.14, lro1.getPrecioFinal("Ray Bradbury", "Fahrenheit 451"), 1e-6);
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaOfertaGetPrecioFinalTest2() {
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			precond(normalize("20.0%[george orwell, isaac asimov] [(Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
					normalize(lro1.toString()));
			//------------------------
			assertEquals("\n> Error: getPrecioFinal(isaac asimov, la fundacion):", 6.424, lro1.getPrecioFinal("isaac asimov", "la fundacion"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(aldous huxley, un mundo feliz):", 7.15, lro1.getPrecioFinal("aldous huxley", "un mundo feliz"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(william gibson, neuromante):", 9.13, lro1.getPrecioFinal("william gibson", "neuromante"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(george orwell, 1984):", 5.456, lro1.getPrecioFinal("george orwell", "1984"), 1e-6);
			assertEquals("\n> Error: getPrecioFinal(ray bradbury, fahrenheit 451):", 8.14, lro1.getPrecioFinal("ray bradbury", "fahrenheit 451"), 1e-6);
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaOfertaGetPrecioFinalTest3() {
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			precond(normalize("20.0%[george orwell, isaac asimov] [(Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
					normalize(lro1.toString()));
			//------------------------
			try {
				double precioFinal = lro1.getPrecioFinal("xxx", "xxx");
				fail("\n> Error: getPrecioFinal(xxx, xxx): No se lanzo ninguna excepcion");
			} catch (RuntimeException e) {
				assertEquals("\n> Error: getPrecioFinal(xxx, xxx): exception.getMessage()",
							 normalize("Libro no encontrado (xxx, xxx)"),
							 normalize(e.getMessage()));
			} catch (Exception e) {
				fail("\n> Error: getPrecioFinal(xxx, xxx): la excepcion lanzada no es RuntimeException");
			}
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaOfertaRemLibroTest1() {
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			//------------------------
			//System.out.println("DBG: "+lro1.toString()); assertTrue("",false);
			precond(normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
					normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("Isaac Asimov", "La Fundacion");
			assertEquals("\n> Error: remLibro(Isaac Asimov, La Fundacion): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("Aldous Huxley", "Un Mundo Feliz");
			assertEquals("\n> Error: remLibro(Aldous Huxley, Un Mundo Feliz): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("William Gibson", "Neuromante");
			assertEquals("\n> Error: remLibro(William Gibson, Neuromante): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("George Orwell", "1984");
			assertEquals("\n> Error: remLibro(George Orwell, 1984): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("Ray Bradbury", "Fahrenheit 451");
			assertEquals("\n> Error: remLibro(Ray Bradbury, Fahrenheit 451): toString():",
						 normalize("20.0%[george orwell, isaac asimov] []"),
						 normalize(lro1.toString()));
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaOfertaRemLibroTest2() {
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			//------------------------
			//System.out.println("DBG: "+lro1.toString()); assertTrue("",false);
			precond(normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
					normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("isaac asimov", "la fundacion");
			assertEquals("\n> Error: remLibro(isaac asimov, la fundacion): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("aldous huxley", "un mundo feliz");
			assertEquals("\n> Error: remLibro(aldous huxley, un mundo feliz): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("william gibson", "neuromante");
			assertEquals("\n> Error: remLibro(william gibson, neuromante): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("george orwell", "1984");
			assertEquals("\n> Error: remLibro(george orwell, 1984): toString():",
						 normalize("20.0%[george orwell, isaac asimov] [(Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lro1.toString()));
			//------------------------
			lro1.remLibro("ray bradbury", "fahrenheit 451");
			assertEquals("\n> Error: remLibro(ray bradbury, fahrenheit 451): toString():",
						 normalize("20.0%[george orwell, isaac asimov] []"),
						 normalize(lro1.toString()));
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaOfertaRemLibroTest3() {
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			//------------------------
			//System.out.println("DBG: "+lro1.toString()); assertTrue("",false);
			precond(normalize("20.0%[george orwell, isaac asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (William Gibson; Neuromante; 8.3; 10.0%; 9.13)]"),
					normalize(lro1.toString()));
			//------------------------
			try {
				lro1.remLibro("xxx", "xxx");
				fail("\n> Error: remLibro(xxx, xxx): No se lanzo ninguna excepcion");
			} catch (RuntimeException e) {
				assertEquals("\n> Error: remLibro(xxx, xxx): exception.getMessage()",
							 normalize("Libro no encontrado (xxx, xxx)"),
							 normalize(e.getMessage()));
			} catch (Exception e) {
				fail("\n> Error: remLibro(xxx, xxx): la excepcion lanzada no es RuntimeException");
			}
			//------------------------
		}
		@Test(timeout = 1000)
		public void libreriaOfertaToStringTest1() {
			assertEquals("\n> Error: lro1.toString():",
						 normalize("20.0%[george orwell, isaac asimov][]"),
						 normalize(lro1.toString()));
		}
		@Test(timeout = 1000)
		public void libreriaOfertaToStringTest2() {
			lro1.addLibro("Isaac Asimov", "La Fundacion", 7.30);
			lro1.addLibro("Aldous Huxley", "Un Mundo Feliz", 6.50);
			lro1.addLibro("William Gibson", "Neuromante", 8.30);
			lro1.addLibro("George Orwell", "1984", 6.20);
			lro1.addLibro("Ray Bradbury", "Fahrenheit 451", 7.40);
			//------------------------
			assertEquals("\n> Error: lro1.toString():",
						 normalize("20.0%[george orwell, isaac asimov][(Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (George Orwell; 1984; 6.2; 20.0 % ; 4.96 ; 10.0 % ; 5.4559999999999995), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14)]"),
						 normalize(lro1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestPruebaLibreriaOferta {
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of PruebaLibreriaOferta JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of PruebaLibreriaOferta JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void PruebaLibreriaOfertaMainTest1() {
			String salida = "";
			SysOutCapture sysOutCapture = new SysOutCapture();
			try {
				sysOutCapture.sysOutCapture();
				PruebaLibreriaOferta.main(new String[]{});
				fail("\n> Error: main(): No se lanzo ninguna excepcion");
			} catch (RuntimeException e) {
				assertEquals("\n> Error: Main(): exception.getMessage()",
							 normalize("Libro no encontrado (Isaac Newton, Arithmetica Universalis)"),
							 normalize(e.getMessage()));
			} catch (Exception e) {
				fail("\n> Error: main(): la excepcion lanzada no es RuntimeException");
			} finally {
				salida = sysOutCapture.sysOutRelease();
			}
			assertEquals("\n> Error: PruebaLibreriaOferta.main():",
					normalize("20.0%[George Orwell, Isaac Asimov] [(George Orwell; 1984; 6.2; 20.0%; 4.96; 10.0%; 5.4559999999999995), (Philip K. Dick; ?Suenan los androides con ovejas electricas?; 3.5; 10.0%; 3.85), (Isaac Asimov; Fundacion e Imperio; 9.4; 20.0%; 7.5200000000000005; 10.0%; 8.272), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (Aldous Huxley; Un Mundo Feliz; 6.5; 10.0%; 7.15), (Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (Isaac Asimov; Segunda Fundacion; 8.1; 20.0%; 6.4799999999999995; 10.0%; 7.127999999999999), (Isaac Newton; Arithmetica Universalis; 10.5; 10.0%; 11.55)] 20.0%[George Orwell, Isaac Asimov] [(Philip K. Dick; ?Suenan los androides con ovejas electricas?; 3.5; 10.0%; 3.85), (Isaac Asimov; Fundacion e Imperio; 9.4; 20.0%; 7.5200000000000005; 10.0%; 8.272), (Ray Bradbury; Fahrenheit 451; 7.4; 10.0%; 8.14), (Isaac Asimov; La Fundacion; 7.3; 20.0%; 5.84; 10.0%; 6.4239999999999995), (William Gibson; Neuromante; 8.3; 10.0%; 9.13), (Isaac Asimov; Segunda Fundacion; 8.1; 20.0%; 6.4799999999999995; 10.0%; 7.127999999999999)] PrecioFinal(Philip K. Dick, ?Suenan los androides con ovejas electricas?): 3.85 PrecioFinal(isaac asimov, fundacion e imperio): 8.272 PrecioFinal(Ray Bradbury, Fahrenheit 451): 8.14 PrecioFinal(Isaac Asimov, La Fundacion): 6.4239999999999995 PrecioFinal(william gibson, neuromante): 9.13 PrecioFinal(Isaac Asimov, Segunda Fundacion): 7.127999999999999"),
					normalize(salida));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTestSuite------------------------------------------------------
	//----------------------------------------------------------------------
	@RunWith(Suite.class)
	@Suite.SuiteClasses({ JUnitTestLibro.class ,
				JUnitTestLibroOferta.class ,
				JUnitTestLibreria.class ,
				JUnitTestLibreriaOferta.class ,
				JUnitTestPruebaLibreriaOferta.class 
				})
				public static class JUnitTestSuite { /*empty*/ }
	//----------------------------------------------------------------------
	//--TestRunner-Inicio-----------------------------------------------------
	//----------------------------------------------------------------------
	public static JUnitCore junitCore = null;
	public static CustomRunListener customRunListener = null;
	public static Result result = null;
	public static void main(String[] args) {
		customRunListener = new CustomRunListener();
		junitCore = new JUnitCore();
		junitCore.addListener(customRunListener);
		result = junitCore.run(JUnitTestSuite.class);
		//result = JUnitCore.runClasses(JUnitTestSuite.class);
		int rc = result.getRunCount();
		int fc = result.getFailureCount();
		//int ac = 0;//result.getAssumptionFailureCount();
		int ac = customRunListener.getTestAssumptionFailureCount();
		int ic = result.getIgnoreCount();
		//--------------------------
		if (fc > 0) {
			System.out.println(">>> ------");
		}
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		if ((ac > 0)||(fc > 0)||(ic > 0)) {
			System.out.println(">>> ------");
			if (ic > 0) {
				System.out.println(">>> Error: Algunos tests ("+ic+") fueron ignorados");
			}
			if (ac > 0) {
				System.out.println(">>> Error: No se pudieron realizar algunos tests ("+ac+") debido a errores en otros metodos");
			}
			if (fc > 0) {
				System.out.println(">>> Error: Fallaron algunos tests ("+fc+") debido a errores en los metodos");
			}
		}
		if (result.wasSuccessful()) {
			System.out.print(">>> JUnit Test Succeeded");
		} else {
			System.out.print(">>> Error: JUnit Test Failed");
		}
		System.out.println(" (Pruebas: "+rc+", Errores: "+fc+", ErrorPrecond: "+ac+", Ignoradas: "+ic+")");
	}
	//----------------------------------------------------------------------
	public static class CustomRunListener extends RunListener {
		private int cntTestAssumptionFailure = 0;
		public int getTestAssumptionFailureCount() {
			return cntTestAssumptionFailure;
		}
		public void testAssumptionFailure(Failure failure) {
			cntTestAssumptionFailure += failure.getDescription().testCount();
		}
	}
	//----------------------------------------------------------------------
	//-- Utils -------------------------------------------------------------
	//----------------------------------------------------------------------
	private static char normalizeUnicode(char ch) {
		switch (ch) {
		case '\n':
		case '\r':
		case '\t':
		case '\f':
			ch = ' ';
			break;
		case '\u20AC':
			ch = 'E';
			break;
		case '\u00A1':
			ch = '!';
			break;
		case '\u00AA':
			ch = 'a';
			break;
		case '\u00BA':
			ch = 'o';
			break;
		case '\u00BF':
			ch = '?';
			break;
		case '\u00C0':
		case '\u00C1':
		case '\u00C2':
		case '\u00C3':
		case '\u00C4':
		case '\u00C5':
		case '\u00C6':
			ch = 'A';
			break;
		case '\u00C7':
			ch = 'C';
			break;
		case '\u00C8':
		case '\u00C9':
		case '\u00CA':
		case '\u00CB':
			ch = 'E';
			break;
		case '\u00CC':
		case '\u00CD':
		case '\u00CE':
		case '\u00CF':
			ch = 'I';
			break;
		case '\u00D0':
			ch = 'D';
			break;
		case '\u00D1':
			ch = 'N';
			break;
		case '\u00D2':
		case '\u00D3':
		case '\u00D4':
		case '\u00D5':
		case '\u00D6':
			ch = 'O';
			break;
		case '\u00D9':
		case '\u00DA':
		case '\u00DB':
		case '\u00DC':
			ch = 'U';
			break;
		case '\u00DD':
			ch = 'Y';
			break;
		case '\u00E0':
		case '\u00E1':
		case '\u00E2':
		case '\u00E3':
		case '\u00E4':
		case '\u00E5':
		case '\u00E6':
			ch = 'a';
			break;
		case '\u00E7':
			ch = 'c';
			break;
		case '\u00E8':
		case '\u00E9':
		case '\u00EA':
		case '\u00EB':
			ch = 'e';
			break;
		case '\u00EC':
		case '\u00ED':
		case '\u00EE':
		case '\u00EF':
			ch = 'i';
			break;
		case '\u00F0':
			ch = 'd';
			break;
		case '\u00F1':
			ch = 'n';
			break;
		case '\u00F2':
		case '\u00F3':
		case '\u00F4':
		case '\u00F5':
		case '\u00F6':
			ch = 'o';
			break;
		case '\u00F9':
		case '\u00FA':
		case '\u00FB':
		case '\u00FC':
			ch = 'u';
			break;
		case '\u00FD':
		case '\u00FF':
			ch = 'y';
			break;
		}
		return ch;
	}
	//------------------------------------------------------------------
	//private static java.util.regex.Pattern float_pattern = java.util.regex.Pattern.compile("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)([eE][+-]?[0-9]+)?");
	private static java.util.regex.Pattern float_pattern = java.util.regex.Pattern.compile("[+-]?(([0-9]+[.][0-9]+([eE][+-]?[0-9]+)?)|([0-9]+[eE][+-]?[0-9]+))");
	private static String normalize_real_numbers(CharSequence csq) {
		String res = "";
		try {
			StringBuilder sbres = new StringBuilder(csq.length());
			java.util.regex.Matcher matcher = float_pattern.matcher(csq);
			int idx = 0;
			while (matcher.find()) {
				int inicio = matcher.start();
				int fin = matcher.end();
				String num1 = csq.subSequence(inicio, fin).toString();
				String formato = "%.6f";
				if (num1.contains("e") || num1.contains("E")) {
					formato = "%.6e";
				}
				double num2 = Double.parseDouble(num1);
				String num3 = String.format(java.util.Locale.UK, formato, num2);
				sbres.append(csq.subSequence(idx, inicio));
				sbres.append(num3);
				idx = fin;
			}
			sbres.append(csq.subSequence(idx, csq.length()));
			res = sbres.toString();
		} catch (Throwable e) {
			res = csq.toString();
		}
		return res;
	}
	//----------------------------------------------------------------------
	private static String normalize(String s1) {
		int sz = s1 == null ? 16 : s1.length() == 0 ? 16 : 2*s1.length();
		StringBuilder sb = new StringBuilder(sz);
		sb.append(' ');
		if (s1 != null) {
			for (int i = 0; i < s1.length(); ++i) {
				char ch = normalizeUnicode(s1.charAt(i));
				char sbLastChar = sb.charAt(sb.length()-1);
				if (Character.isLetter(ch)) {
					if ( ! Character.isLetter(sbLastChar)) {
						if ( ! Character.isSpaceChar(sbLastChar)) {
							sb.append(' ');
						}
					}
					sb.append(ch);
				} else if (Character.isDigit(ch)) {
					if ((i >= 2)
						&& (s1.charAt(i-1) == '.')
						&& Character.isDigit(s1.charAt(i-2))) {
						sb.setLength(sb.length()-2); // "9 ."
						sb.append('.');
					} else if ((i >= 2)
							   && ((s1.charAt(i-1) == 'e')||(s1.charAt(i-1) == 'E'))
							   && Character.isDigit(s1.charAt(i-2))) {
						sb.setLength(sb.length()-2); // "9 e"
						sb.append('e');
					} else if ((i >= 3)
							   && (s1.charAt(i-1) == '+')
							   && ((s1.charAt(i-2) == 'e')||(s1.charAt(i-2) == 'E'))
							   && Character.isDigit(s1.charAt(i-3))) {
						sb.setLength(sb.length()-4); // "9 e +"
						sb.append('e');
					} else if ((i >= 3)
							   && (s1.charAt(i-1) == '-')
							   && ((s1.charAt(i-2) == 'e')||(s1.charAt(i-2) == 'E'))
							   && Character.isDigit(s1.charAt(i-3))) {
						sb.setLength(sb.length()-4); // "9 e -"
						sb.append("e-");
					} else if ( (sbLastChar != '-') && ! Character.isDigit(sbLastChar)) {
						if ( ! Character.isSpaceChar(sbLastChar)) {
							sb.append(' ');
						}
					}
					sb.append(ch);
				} else if (Character.isSpaceChar(ch)) {
					if ( ! Character.isSpaceChar(sbLastChar)) {
						sb.append(' ');
					}
				} else {
					if ( ! Character.isSpaceChar(sbLastChar)) {
						sb.append(' ');
					}
					sb.append(ch);
				}
			}
		}
		if (Character.isSpaceChar(sb.charAt(sb.length()-1))) {
			sb.setLength(sb.length()-1);
		}
		if ((sb.length() > 0) && Character.isSpaceChar(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		return normalize_real_numbers(sb);
	}
	//------------------------------------------------------------------
	private static String normalizeListStr(String listaStr, String delims, String prefix, String suffix) {
		listaStr = listaStr.trim();
		String res = listaStr;
		try {
			if (prefix.length() > 0 && listaStr.startsWith(prefix)) {
				listaStr = listaStr.substring(prefix.length());
			}
			if (suffix.length() > 0 && listaStr.endsWith(suffix)) {
				listaStr = listaStr.substring(0, listaStr.length()-suffix.length());
			}
			listaStr = listaStr.trim();
			java.util.List<String> lista = new java.util.ArrayList<>(java.util.List.of(listaStr.split(delims)));
			lista.sort(null);
			res = lista.toString();
		} catch (Throwable e) {
			// ignorar
		}
		return res;
	}
	//----------------------------------------------------------------------
	private static final String precondMessage = "\n> Aviso: No se pudo realizar el test debido a errores en otros metodos";
	//----------------------------------------------------------------------
	private static void precond(boolean expectedValue, boolean currValue) {
		assumeTrue(precondMessage, expectedValue == currValue);
	}
	private static void precond(char expectedValue, char currValue) {
		assumeTrue(precondMessage, expectedValue == currValue);
	}
	private static void precond(short expectedValue, short currValue) {
		assumeTrue(precondMessage, expectedValue == currValue);
	}
	private static void precond(int expectedValue, int currValue) {
		assumeTrue(precondMessage, expectedValue == currValue);
	}
	private static void precond(long expectedValue, long currValue) {
		assumeTrue(precondMessage, expectedValue == currValue);
	}
	private static void precond(float expectedValue, float currValue, float delta) {
		assumeTrue(precondMessage, Math.abs(expectedValue - currValue) <= delta);
	}
	private static void precond(double expectedValue, double currValue, double delta) {
		assumeTrue(precondMessage, Math.abs(expectedValue - currValue) <= delta);
	}
	private static void precond(Object expectedValue, Object currValue) {
		if ((expectedValue == null)||(currValue == null)) {
			assumeTrue(precondMessage, expectedValue == currValue);
		} else {
			assumeTrue(precondMessage, expectedValue.equals(currValue));
		}
	}
	//------------------------------------------------------------------
	private static void precond(int[] expectedValue, int[] currValue) {
		if ((expectedValue == null)||(currValue == null)) {
			assumeTrue(precondMessage, expectedValue == currValue);
		} else {
			precond(expectedValue.length, currValue.length);
			for (int i = 0; i < expectedValue.length; ++i) {
				precond(expectedValue[i], currValue[i]);
			}
		}
	}
	private static void precond(double[] expectedValue, double[] currValue, double delta) {
		if ((expectedValue == null)||(currValue == null)) {
			assumeTrue(precondMessage, expectedValue == currValue);
		} else {
			precond(expectedValue.length, currValue.length);
			for (int i = 0; i < expectedValue.length; ++i) {
				precond(expectedValue[i], currValue[i], delta);
			}
		}
	}
	private static <T> void precond(T[] expectedValue, T[] currValue) {
		if ((expectedValue == null)||(currValue == null)) {
			assumeTrue(precondMessage, expectedValue == currValue);
		} else {
			precond(expectedValue.length, currValue.length);
			for (int i = 0; i < expectedValue.length; ++i) {
				precond(expectedValue[i], currValue[i]);
			}
		}
	}
	//----------------------------------------------------------------------
	private static void precondNorm(String expectedValue, String currValue) {
		precond(normalize(expectedValue), normalize(currValue));
	}
	private static void precondNorm(String[] expectedValue, String[] currValue) {
		if ((expectedValue == null)||(currValue == null)) {
			assumeTrue(precondMessage, expectedValue == currValue);
		} else {
			precond(expectedValue.length, currValue.length);
			for (int i = 0; i < expectedValue.length; ++i) {
				precond(normalize(expectedValue[i]), normalize(currValue[i]));
			}
		}
	}
	private static void assertEqualsNorm(String msg, String expectedValue, String currValue) {
		assertEquals(msg, normalize(expectedValue), normalize(currValue));
	}
	private static void assertEqualsNorm(String msg, java.util.List<String> expectedValue, java.util.List<String> currValue) {
		assertEquals(msg, expectedValue.size(), currValue.size());
		for (int i = 0; i < expectedValue.size(); ++i) {
			assertEquals(msg, normalize(expectedValue.get(i)), normalize(currValue.get(i)));
		}
	}
	private static void assertArrayEqualsNorm(String msg, String[] expectedValue, String[] currValue) {
		assertEquals(msg, expectedValue.length, currValue.length);
		for (int i = 0; i < expectedValue.length; ++i) {
			assertEquals(msg, normalize(expectedValue[i]), normalize(currValue[i]));
		}
	}
	//----------------------------------------------------------------------
	private static void deleteFile(String filename) {
		new java.io.File(filename).delete();
	}
	private static void createFile(String filename, String inputData) throws Exception {
		try (java.io.PrintWriter pw = new java.io.PrintWriter(filename)) {
			pw.println(inputData);
		}
	}
	private static void createFile(String filename, String[] inputData) throws Exception {
		try (java.io.PrintWriter pw = new java.io.PrintWriter(filename)) {
			for (String linea : inputData) {
				pw.println(linea);
			}
		}
	}
	private static String loadFile(String filename) throws Exception {
		java.util.StringJoiner sj = new java.util.StringJoiner("\n");
		try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(filename))) {
			while (sc.hasNextLine()) {
				sj.add(sc.nextLine());
			}
		}
		return sj.toString();
	}
	//------------------------------------------------------------------
	//------------------------------------------------------------------
	private static Object getMemberObject(Object obj, Class objClass, Class memberClass, String memberName) {
		//--------------------------
		// OBJ puede ser NULL en caso de variable STATIC
		// OBJCLASS puede ser NULL si OBJ no es NULL
		// MEMBERCLASS no puede ser NULL
		// MEMBERNAME puede ser NULL, si solo hay una unica variable de ese tipo
		//--------------------------
		String memberId = (memberName == null ? "" : memberName)+":"+(memberClass == null ? "" : memberClass.getName());
		Object res = null;
		int idx = -1;
		try {
			if ((objClass == null)&&(obj != null)) {
				objClass = obj.getClass();
			}
			if ((objClass != null)&&(memberClass != null)) {
				int cnt = 0;
				int aux = -1;
				java.lang.reflect.Field[] objFields = objClass.getDeclaredFields();
				for (int i = 0; i < objFields.length; ++i) {
					if (memberClass.equals(objFields[i].getType())) {
						if ((memberName != null)&&(memberName.equalsIgnoreCase(objFields[i].getName()))) {
							idx = i;
						} else {
							aux = i;
						}
						++cnt;
					}
				}
				if ((idx < 0)&&(cnt == 1)) {
					idx = aux;	// si solo tiene una variable de ese tipo, no importa el nombre
				}
				if (idx >= 0) {
					objFields[idx].setAccessible(true);
					res = objFields[idx].get(obj);
				}
			}
		} catch (Throwable e) {
			fail("\n> Unexpected Error. getMemberObject["+memberId+"]: " + e);
		}
		if (idx < 0) {
			fail("\n> Error: no ha sido posible encontrar la variable ["+memberId+"]");
		}
		if (res == null) {
			fail("\n> Error: la variable ["+memberId+"] no se ha creado correctamente");
		}
		return res;
	} 
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	private static class SysOutCapture {
		private SysOutErrCapture systemout;
		private SysOutErrCapture systemerr;
		public SysOutCapture() {
			systemout = new SysOutErrCapture(false);
			systemerr = new SysOutErrCapture(true);
		}
		public void sysOutCapture() throws RuntimeException {
			try {
				systemout.sysOutCapture();
			} finally {
				systemerr.sysOutCapture();
			}
		}
		public String sysOutRelease() throws RuntimeException {
			String s1 = "";
			String s2 = "";
			try {
				s1 = systemout.sysOutRelease();
			} finally {
				s2 = systemerr.sysOutRelease();
			}
			return s1 + " " + s2 ;
		}
		//--------------------------
		private static class SysOutErrCapture {
			//--------------------------------
			private java.io.PrintStream sysoutOld;
			private java.io.PrintStream sysoutstr;
			private java.io.ByteArrayOutputStream baos;
			private final boolean systemErr;
			private int estado;
			//--------------------------------
			public SysOutErrCapture(boolean syserr) {
				sysoutstr = null ;
				baos = null;
				sysoutOld = null ;
				estado = 0;
				systemErr = syserr;
			}
			//--------------------------------
			public void sysOutCapture() throws RuntimeException {
				if (estado != 0) {
					throw new RuntimeException("sysOutCapture:BadState");
				} else {
					estado = 1;
					try {
						if (systemErr) {
							sysoutOld = System.err;
						} else {
							sysoutOld = System.out;
						}
						baos = new java.io.ByteArrayOutputStream();
						sysoutstr = new java.io.PrintStream(baos);
						if (systemErr) {
							System.setErr(sysoutstr);
						} else {
							System.setOut(sysoutstr);
						}
					} catch (Throwable e) {
						throw new RuntimeException("sysOutCapture:InternalError");
					}
				}
			}
			//--------------------------------
			public String sysOutRelease() throws RuntimeException {
				String result = "";
				if (estado != 1) {
					throw new RuntimeException("sysOutRelease:BadState");
				} else {
					estado = 0;
					try {
						if (sysoutstr != null) {
							sysoutstr.flush();
						}
						if (baos != null) {
							baos.flush();
							result = new String(baos.toByteArray()); //java.nio.charset.StandardCharsets.ISO_8859_1);
						}
					} catch (Throwable e) {
						throw new RuntimeException("sysOutRelease:InternalError1");
					} finally {
						try {
							if (systemErr) {
								System.setErr(sysoutOld);
							} else {
								System.setOut(sysoutOld);
							}
							if (sysoutstr != null) {
								sysoutstr.close();
								sysoutstr = null;
							}
							if (baos != null) {
								baos.close();
								baos = null;
							}
						} catch (Throwable e) {
							throw new RuntimeException("sysOutRelease:InternalError2");
						}
					}
				}
				return result;
			}
			//--------------------------------
		}
	}
	//----------------------------------------------------------------------
	//--TestRunner-Fin---------------------------------------------------
	//----------------------------------------------------------------------
}
//--------------------------------------------------------------------------
