
//--------------------------------------------------------------------------
import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
//--------------------------------------------------------------------------

import datos.*;

//--------------------------------------------------------------------------

public class TestRunnerPr31Ej1 {
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestDatosException {
		private DatosException de1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of DatosException JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of DatosException JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			de1 = new DatosException("Mensaje de Error 1");
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void datosExceptionCtorTest1() {
			assertTrue("\n> Error: DatosException extends RuntimeException:", ((Object)de1 instanceof RuntimeException));
			assertEquals("\n> Error: new DatosException(\"Mensaje de Error 1\"): getMessage():", "Mensaje de Error 1", de1.getMessage());
		}
		@Test(timeout = 1000)
		public void datosExceptionCtorTest2() {
			DatosException de2 = new DatosException();
			assertTrue("\n> Error: DatosException extends RuntimeException:", ((Object)de2 instanceof RuntimeException));
			assertEquals("\n> Error: new DatosException(): getMessage():", null, de2.getMessage());
		}
		@Test(timeout = 1000)
		public void datosExceptionToStringTest1() {
			precond("Mensaje de Error 1", de1.getMessage());
			assertEquals("\n> Error: de1.toString():",
						 normalize("datos.DatosException : Mensaje de Error 1"),
						 normalize(de1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestDatos {
		private static final String[] inputData = {
			"5.5", "9.5", "Pepe", "10.5", "Maria", "12.5", "13.5",
			"Paco", "17.5", "20.5", "Ana", "25.5", "Juan", "Lola"
		};
		private static final double[] inputValues = { 5.5, 9.5, 10.5, 12.5, 13.5, 17.5, 20.5, 25.5 };
		private static final String[] inputErrors = { "Pepe", "Maria", "Paco", "Ana", "Juan", "Lola" };
		private static double[] list2arrayDouble(java.util.List<Double> al) {
			double[] dtdt = new double[al.size()];
			for (int i = 0; i < dtdt.length; ++i) {
				dtdt[i] = al.get(i);
			}
			return dtdt;
		}
		private static String[] list2arrayString(java.util.List<String> al) {
			return al.toArray(new String[0]);
		}
		private static void precondDatos(Datos dt) {
			double[] dtdt = list2arrayDouble(dt.getDatos());
			String[] dterr = list2arrayString(dt.getErrores());
			precond(inputValues, dtdt, 1e-6);
			precond(inputErrors, dterr);
		}
		private Datos dt1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Datos JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Datos JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			dt1 = new Datos(inputData, 10.0, 20.0);
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void datosCtorTest1() {
			double[] dtdt = list2arrayDouble(dt1.getDatos());
			String[] dterr = list2arrayString(dt1.getErrores());
			assertArrayEquals("\n> Error: new Datos(): dt1.getDatos():", inputValues, dtdt, 1e-6);
			assertArrayEquals("\n> Error: new Datos(): dt1.getErrores():", inputErrors, dterr);
		}
		@Test(timeout = 1000)
		public void datosCalcMediaTest1() throws Exception {
			precondDatos(dt1);
			assertEquals("\n> Error: Min: 10 ; Max: 20 ; dt1.calcMedia():", 13.5, dt1.calcMedia(), 1e-6);
		}
		@Test(timeout = 1000)
		public void datosCalcMediaTest2() {
			precondDatos(dt1);
			try {
				Datos dt2 = new Datos(inputData, 0.0, 4.0);
				double valor = dt2.calcMedia();
				fail("\n> Error: Min: 0 ; Max: 4 ; calcMedia(): No se lanzo ninguna excepcion");
			} catch (DatosException e) {
				assertEquals("\n> Error: Min: 0 ; Max: 4 ; calcMedia(): exception.getMessage():", "No hay datos en el rango especificado", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: Min: 0 ; Max: 4 ; calcMedia(): la excepcion lanzada no es DatosException");
			}
		}
		@Test(timeout = 1000)
		public void datosCalcDesvTipicaTest1() throws Exception {
			precondDatos(dt1);
			assertEquals("\n> Error: Min: 10 ; Max: 20 ; dt1.calcDesvTipica():", 2.54951, dt1.calcDesvTipica(), 1e-6);
		}
		@Test(timeout = 1000)
		public void datosCalcDesvTipicaTest2() {
			precondDatos(dt1);
			try {
				Datos dt2 = new Datos(inputData, 0.0, 4.0);
				double valor = dt2.calcDesvTipica();
				fail("\n> Error: Min: 0 ; Max: 4 ; calcDesvTipica(): No se lanzo ninguna excepcion");
			} catch (DatosException e) {
				assertEquals("\n> Error: Min: 0 ; Max: 4 ; calcDesvTipica(): exception.getMessage():", "No hay datos en el rango especificado", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: Min: 0 ; Max: 4 ; calcDesvTipica(): la excepcion lanzada no es DatosException");
			}
		}
		@Test(timeout = 1000)
		public void datosSetRangoTest1() throws Exception {
			precondDatos(dt1);
			dt1.setRango("5.7;13.7");
			assertEquals("\n> Error: Min: 5.7 ; Max: 13.7 ; dt1.calcMedia():", 11.5, dt1.calcMedia(), 1e-6);
		}
		@Test(timeout = 1000)
		public void datosSetRangoTest2() {
			precondDatos(dt1);
			try {
				dt1.setRango("10");
				fail("\n> Error: dt1.setRango(\"10\"): No se lanzo ninguna excepcion");
			} catch (DatosException e) {
				assertEquals("\n> Error: dt1.setRango(\"10\"): exception.getMessage():", "Error en los datos al establecer el rango", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: dt1.setRango(\"10\"): la excepcion lanzada no es DatosException");
			}
		}
		@Test(timeout = 1000)
		public void datosSetRangoTest3() {
			precondDatos(dt1);
			try {
				dt1.setRango("10;xxx");
				fail("\n> Error: dt1.setRango(\"10;xxx\"): No se lanzo ninguna excepcion");
			} catch (DatosException e) {
				assertEquals("\n> Error: dt1.setRango(\"10;xxx\"): exception.getMessage():", "Error en los datos al establecer el rango", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: dt1.setRango(\"10;xxx\"): la excepcion lanzada no es DatosException");
			}
		}
		@Test(timeout = 1000)
		public void datosSetRangoTest4() {
			precondDatos(dt1);
			try {
				dt1.setRango("xxx;10");
				fail("\n> Error: dt1.setRango(\"xxx;10\"): No se lanzo ninguna excepcion");
			} catch (DatosException e) {
				assertEquals("\n> Error: dt1.setRango(\"xxx;10\"): exception.getMessage():", "Error en los datos al establecer el rango", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: dt1.setRango(\"xxx;10\"): la excepcion lanzada no es DatosException");
			}
		}
		@Test(timeout = 1000)
		public void datosToStringTest1() {
			precondDatos(dt1);
			assertEquals("\n> Error: dt1.toString():",
						 normalize("Min: 10.0, Max: 20.0, [ 5.5, 9.5, 10.5, 12.5, 13.5, 17.5, 20.5, 25.5 ], [ Pepe, Maria, Paco, Ana, Juan, Lola ], Media: 13.5, DesvTipica: 2.5495097567963922"),
						 normalize(dt1.toString()));
		}
		@Test(timeout = 1000)
		public void datosToStringTest2() {
			precondDatos(dt1);
			Datos dt2 = new Datos(inputData, 0.0, 4.0);
			precondDatos(dt2);
			//------------------------
			assertEquals("\n> Error: dt2.toString():",
						 normalize("Min: 0.0, Max: 4.0, [ 5.5, 9.5, 10.5, 12.5, 13.5, 17.5, 20.5, 25.5 ], [ Pepe, Maria, Paco, Ana, Juan, Lola ], Media: ERROR, DesvTipica: ERROR"),
						 normalize(dt2.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestPruebaDatos {
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of PruebaDatos JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of PruebaDatos JUnit Test");
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
		public void PruebaDatosMainTest1() {
			String salida = "";
			SysOutCapture sysOutCapture = new SysOutCapture();
			try {
				sysOutCapture.sysOutCapture();
				PruebaDatos.main(new String[]{"10", "20", "5", "9", "Pepe", "10", "Maria", "12", "13", "Paco", "17", "20", "Ana", "25", "Juan", "Lola"});
			} finally {
				salida = sysOutCapture.sysOutRelease();
			}
			assertEquals("\n> Error: PruebaDatos.main(\"10\", \"20\", \"5\", \"9\", \"Pepe\", \"10\", \"Maria\", \"12\", \"13\", \"Paco\", \"17\", \"20\", \"Ana\", \"25\", \"Juan\", \"Lola\"):",
						 normalize("Min : 10.0 , Max : 20.0 , [ 5.0 , 9.0 , 10.0 , 12.0 , 13.0 , 17.0 , 20.0 , 25.0 ] , [ Pepe , Maria , Paco , Ana , Juan , Lola ] , Media : 14.4 , DesvTipica : 3.6110940170535577 Min : 0.0 , Max : 4.0 , [ 5.0 , 9.0 , 10.0 , 12.0 , 13.0 , 17.0 , 20.0 , 25.0 ] , [ Pepe , Maria , Paco , Ana , Juan , Lola ] , Media : ERROR , DesvTipica : ERROR  Error en los datos al establecer el rango"),
						 normalize(salida));
		}
		@Test(timeout = 1000)
		public void PruebaDatosMainTest2() {
			String salida = "";
			SysOutCapture sysOutCapture = new SysOutCapture();
			try {
				sysOutCapture.sysOutCapture();
				PruebaDatos.main(new String[]{"10"});
			} finally {
				salida = sysOutCapture.sysOutRelease();
			}
			assertEquals("\n> Error: PruebaDatos.main(\"10\"):",
						 normalize("Error , no hay valores suficientes"),
						 normalize(salida));
		}
		@Test(timeout = 1000)
		public void PruebaDatosMainTest3() {
			String salida = "";
			SysOutCapture sysOutCapture = new SysOutCapture();
			try {
				sysOutCapture.sysOutCapture();
				PruebaDatos.main(new String[]{"10", "hola", "5", "9", "Pepe", "10", "Maria", "12", "13", "Paco", "17", "20", "Ana", "25", "Juan", "Lola"});
			} finally {
				salida = sysOutCapture.sysOutRelease();
			}
			assertEquals("\n> Error: PruebaDatos.main(\"10\", \"hola\", \"5\", \"9\", \"Pepe\", \"10\", \"Maria\", \"12\", \"13\", \"Paco\", \"17\", \"20\", \"Ana\", \"25\", \"Juan\", \"Lola\"):",
						 normalize("Error , al convertir un valor a numero real ( For input string : \"hola\" )"),
						 normalize(salida));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTestSuite------------------------------------------------------
	//----------------------------------------------------------------------
	@RunWith(Suite.class)
	@Suite.SuiteClasses({ JUnitTestDatosException.class ,
				JUnitTestDatos.class ,
				JUnitTestPruebaDatos.class
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
