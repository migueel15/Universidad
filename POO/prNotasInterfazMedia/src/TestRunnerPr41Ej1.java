
//--------------------------------------------------------------------------
import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
//--------------------------------------------------------------------------

import notas.*;

//--------------------------------------------------------------------------

public class TestRunnerPr41Ej1 {
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestAlumnoException {
		private AlumnoException ae1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of AlumnoException JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of AlumnoException JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			ae1 = new AlumnoException("Mensaje de Error 1");
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void alumnoExceptionCtorTest1() {
			assertTrue("\n> Error: AlumnoException extends Exception:", ((Object)ae1 instanceof Exception));
			assertFalse("\n> Error: AlumnoException extends RuntimeException:", ((Object)ae1 instanceof RuntimeException));
			assertEquals("\n> Error: new AlumnoException(\"Mensaje de Error 1\"): getMessage():", "Mensaje de Error 1", ae1.getMessage());
		}
		@Test(timeout = 1000)
		public void alumnoExceptionCtorTest2() {
			AlumnoException ae2 = new AlumnoException();
			assertTrue("\n> Error: AlumnoException extends Exception:", ((Object)ae2 instanceof Exception));
			assertFalse("\n> Error: AlumnoException extends RuntimeException:", ((Object)ae2 instanceof RuntimeException));
			assertEquals("\n> Error: new AlumnoException(): getMessage():", null, ae2.getMessage());
		}
		@Test(timeout = 1000)
		public void alumnoExceptionToStringTest1() {
			precond("Mensaje de Error 1", ae1.getMessage());
			assertEquals("\n> Error: ae1.toString():",
						 normalize("notas.AlumnoException : Mensaje de Error 1"),
						 normalize(ae1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestAlumno {
		private Alumno an1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Alumno JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Alumno JUnit Test");
		}
		@Before
		public void setUp() throws Exception {
			// Code executed before each test
			an1 = new Alumno("22456784F", "Gonzalez Perez, Juan", 5.5);
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void alumnoCtorTest1() {
			assertEquals("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\", 5.5): DNI:", "22456784F", an1.getDni());
			assertEquals("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\", 5.5): Nombre:", "Gonzalez Perez, Juan", an1.getNombre());
			assertEquals("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\", 5.5): Calificacion:", 5.5, an1.getCalificacion(), 1e-6);
		}
		@Test(timeout = 1000)
		public void alumnoCtorTest2() throws Exception {
			Alumno an2 = new Alumno("22456784F", "Gonzalez Perez, Juan");
			assertEquals("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\"): DNI:", "22456784F", an2.getDni());
			assertEquals("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\"): Nombre:", "Gonzalez Perez, Juan", an2.getNombre());
			assertEquals("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\"): Calificacion:", 0.0, an2.getCalificacion(), 1e-6);
		}
		@Test(timeout = 1000)
		public void alumnoCtorTestX1() {
			try {
				Alumno an2 = new Alumno("22456784F", "Gonzalez Perez, Juan", -1.0);
				fail("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\", -1.0): No se lanzo ninguna excepcion");
			} catch (AlumnoException e) {
				//assertEquals("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\", -1.0): exception.getMessage():", "calificacion negativa", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: new Alumno(\"22456784F\", \"Gonzalez Perez, Juan\", -1.0): la excepcion lanzada no es AlumnoException");
			}
		}
		@Test(timeout = 1000)
		public void alumnoEqualsTest1() throws Exception {
			precond("22456784F", an1.getDni());
			precond("Gonzalez Perez, Juan", an1.getNombre());
			precond(5.5, an1.getCalificacion(), 1e-6);
			//------------------------
			Alumno an2 = new Alumno("22456784F", "Gonzalez Perez, Juan", 7.7);
			precond("22456784F", an2.getDni());
			precond("Gonzalez Perez, Juan", an2.getNombre());
			precond(7.7, an2.getCalificacion(), 1e-6);
			assertTrue("\n> Error: an1.equals(an2): ", an1.equals(an2));
			//------------------------
			assertTrue("\n> Error: an1.equals((Object)an2): ", an1.equals((Object)an2));
			//------------------------
			Alumno an3 = new Alumno("22456784f", "Gonzalez Perez, Juan", 7.7);
			precond("22456784f", an3.getDni());
			precond("Gonzalez Perez, Juan", an3.getNombre());
			precond(7.7, an3.getCalificacion(), 1e-6);
			assertTrue("\n> Error: an1.equals(an3): ", an1.equals(an3));
			//------------------------
			Alumno an4 = new Alumno("22456784F", "gonzalez perez, juan", 5.5);
			precond("22456784F", an4.getDni());
			precond("gonzalez perez, juan", an4.getNombre());
			precond(5.5, an4.getCalificacion(), 1e-6);
			assertFalse("\n> Error: an1.equals(an4): ", an1.equals(an4));
			//------------------------
			Alumno an5 = new Alumno("11111111A", "Gonzalez Perez, Juan", 5.5);
			precond("11111111A", an5.getDni());
			precond("Gonzalez Perez, Juan", an5.getNombre());
			precond(5.5, an5.getCalificacion(), 1e-6);
			assertFalse("\n> Error: an1.equals(an5): ", an1.equals(an5));
			//------------------------
			assertFalse("\n> Error: an1.equals(null): ", an1.equals(null));
			assertFalse("\n> Error: an1.equals(\"Esto es un String\"): ", an1.equals("Esto es un String"));
		}
		@Test(timeout = 1000)
		public void alumnoHashCodeTest1() throws Exception {
			precond("22456784F", an1.getDni());
			precond("Gonzalez Perez, Juan", an1.getNombre());
			precond(5.5, an1.getCalificacion(), 1e-6);
			int an1HashCode = an1.hashCode();
			//------------------------
			Alumno an2 = new Alumno("22456784F", "Gonzalez Perez, Juan", 7.7);
			precond("22456784F", an2.getDni());
			precond("Gonzalez Perez, Juan", an2.getNombre());
			precond(7.7, an2.getCalificacion(), 1e-6);
			assertEquals("\n> Error: an2.hashCode(): ", an1HashCode, an2.hashCode());
			//------------------------
			Alumno an3 = new Alumno("22456784f", "Gonzalez Perez, Juan", 7.7);
			precond("22456784f", an3.getDni());
			precond("Gonzalez Perez, Juan", an3.getNombre());
			precond(7.7, an3.getCalificacion(), 1e-6);
			assertEquals("\n> Error: an3.hashCode(): ", an1HashCode, an3.hashCode());
			//------------------------
			Alumno an4 = new Alumno("22456784F", "gonzalez perez, juan", 5.5);
			precond("22456784F", an4.getDni());
			precond("gonzalez perez, juan", an4.getNombre());
			precond(5.5, an4.getCalificacion(), 1e-6);
			assertNotEquals("\n> Error: an4.hashCode(): ", an1HashCode, an4.hashCode());
			//------------------------
			Alumno an5 = new Alumno("11111111A", "Gonzalez Perez, Juan", 5.5);
			precond("11111111A", an5.getDni());
			precond("Gonzalez Perez, Juan", an5.getNombre());
			precond(5.5, an5.getCalificacion(), 1e-6);
			assertNotEquals("\n> Error: an5.hashCode(): ", an1HashCode, an5.hashCode());
		}
		@Test(timeout = 1000)
		public void alumnoToStringTest1() {
			precond("22456784F", an1.getDni());
			precond("Gonzalez Perez, Juan", an1.getNombre());
			precond(5.5, an1.getCalificacion(), 1e-6);
			assertEquals("\n> Error: an1.toString():",
						 normalize("Gonzalez Perez, Juan 22456784F"),
						 normalize(an1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestAsignatura {
		private static final String nmAsg = "Algebra";
		private static final String[] inputData = {
			"25653443S;Garcia Gomez, Juan;8.1",
			"23322443K;Lopez Turo, Manuel;4.3",
			"24433522M;Merlo Martinez, Juana;5.3",
			"53553421D;Santana Medina, Petra;-7.1",
			"55343442L,Godoy Molina, Marina;6.3",
			"34242432J;Fernandez Vara, Pedro;2.k",
			"42424312G;Lopez Gama, Luisa;7.1"
		};
		private static final java.util.List<Alumno> inputValues = crearAlumnos();
		private static final java.util.List<String> inputErrors = crearErrores();
		private static java.util.List<Alumno> crearAlumnos() {
			java.util.List<Alumno> alumnos = new java.util.ArrayList<>();
			try {
				alumnos.add(new Alumno("25653443S", "Garcia Gomez, Juan", 8.1));
				alumnos.add(new Alumno("23322443K", "Lopez Turo, Manuel", 4.3));
				alumnos.add(new Alumno("24433522M", "Merlo Martinez, Juana", 5.3));
				alumnos.add(new Alumno("42424312G", "Lopez Gama, Luisa", 7.1));
			} catch (Exception e) {
				fail("\n> Error: crearAlumnos: lanza excepcion no esperada");
			}
			return alumnos;
		}
		private static java.util.List<String> crearErrores() {
			java.util.List<String> errores = new java.util.ArrayList<>();
			try {
				errores.add("ERROR. Calificacion negativa: 53553421D;Santana Medina, Petra;-7.1");
				errores.add("ERROR. Faltan datos: 55343442L,Godoy Molina, Marina;6.3");
				errores.add("ERROR. Calificacion no numerica: 34242432J;Fernandez Vara, Pedro;2.k");
			} catch (Exception e) {
				fail("\n> Error: crearErrores: lanza excepcion no esperada");
			}
			return errores;
		}
		private Asignatura asg1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Asignatura JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Asignatura JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			asg1 = new Asignatura(nmAsg, inputData);
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void asignaturaCtorTest1() {
			assertEquals("\n> Error: new Asignatura(): asg1.getAlumnos():", inputValues, asg1.getAlumnos());
			assertEqualsNorm("\n> Error: new Asignatura(): asg1.getErrores():", inputErrors, asg1.getErrores());
		}
		@Test(timeout = 1000)
		public void asignaturaCalcMediaTest1() throws Exception {
			assertEquals("\n> Error: asg1.getMedia():", 6.20, asg1.getMedia(), 1e-6);
		}
		@Test(timeout = 1000)
		public void asignaturaCalcMediaTest2() {
			try {
				String[] datos = { "xxx" };
				Asignatura asg2 = new Asignatura(nmAsg, datos);
				double valor = asg2.getMedia();
				fail("\n> Error: getMedia(): No se lanzo ninguna excepcion");
			} catch (AlumnoException e) {
				//assertEquals("\n> Error: getMedia(): exception.getMessage():", "No hay alumnos", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: getMedia(): la excepcion lanzada no es AlumnoException");
			}
		}
		@Test(timeout = 1000)
		public void asignaturaGetCalificacionTest1() throws Exception {
			assertEquals("\n> Error: asg1.getCalificacion(Garcia Gomez, Juan):", 8.1, asg1.getCalificacion(inputValues.get(0)), 1e-6);
			assertEquals("\n> Error: asg1.getCalificacion(Lopez Turo, Manuel):", 4.3, asg1.getCalificacion(inputValues.get(1)), 1e-6);
			assertEquals("\n> Error: asg1.getCalificacion(Merlo Martinez, Juana):", 5.3, asg1.getCalificacion(inputValues.get(2)), 1e-6);
			assertEquals("\n> Error: asg1.getCalificacion(Lopez Gama, Luisa):", 7.1, asg1.getCalificacion(inputValues.get(3)), 1e-6);
		}
		@Test(timeout = 1000)
		public void asignaturaGetCalificacionTest2() {
			try {
				Alumno an2 = new Alumno("34242432J", "Fernandez Vara, Pedro");
				double valor = asg1.getCalificacion(an2);
				fail("\n> Error: asg1.getCalificacion(Fernandez Vara, Pedro): No se lanzo ninguna excepcion");
			} catch (AlumnoException e) {
				//assertEquals("\n> Error: asg1.getCalificacion(Fernandez Vara, Pedro): exception.getMessage():", "El alumno Fernandez Vara, Pedro 34242432J no se encuentra", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: asg1.getCalificacion(Fernandez Vara, Pedro): la excepcion lanzada no es AlumnoException");
			}
		}
		@Test(timeout = 1000)
		public void asignaturaToStringTest1() {
			assertEquals("\n> Error: asg1.toString():",
						 normalize("Algebra: { [Garcia Gomez, Juan 25653443S, Lopez Turo, Manuel 23322443K, Merlo Martinez, Juana 24433522M, Lopez Gama, Luisa 42424312G], [ERROR. Calificacion negativa: 53553421D;Santana Medina, Petra;-7.1, ERROR. Faltan datos: 55343442L,Godoy Molina, Marina;6.3, ERROR. Calificacion no numerica: 34242432J;Fernandez Vara, Pedro;2.k] }"),
						 normalize(asg1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTestSuite------------------------------------------------------
	//----------------------------------------------------------------------
	@RunWith(Suite.class)
	@Suite.SuiteClasses({ JUnitTestAlumnoException.class ,
				JUnitTestAlumno.class ,
				JUnitTestAsignatura.class
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
