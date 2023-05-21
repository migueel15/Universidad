
//--------------------------------------------------------------------------
import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
//----------------------------------------------------------------------

import alturas.*;
import guialturas.*;

//--------------------------------------------------------------------------

public class TestRunnerPr63 {
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestPais {
		private Pais p1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Pais JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Pais JUnit Test");
		}
		@Before
		public void setUp() throws Exception {
			// Code executed before each test
			p1 = new Pais(new String("Turkey"), new String("Euro/Asia"), 1.74);
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void paisCtorTest1() {
			assertEquals("\n> Error: p1.getNombre():",
						 "Turkey",
						 p1.getNombre());
			assertEquals("\n> Error: p1.getContinente():",
						 "Euro/Asia",
						 p1.getContinente());
			assertEquals("\n> Error: p1.getAltura():",
						 1.74,
						 p1.getAltura(), 0.00001);
		}
		@Test(timeout = 1000)
		public void paisCtorTest2() {
			assertTrue("\n> Error: Pais implements Comparable<?>:", ((Object)p1 instanceof Comparable<?>));
		}
		@Test(timeout = 1000)
		public void paisEqualsTest1() throws Exception {
			precond("Turkey", p1.getNombre());
			precond("Euro/Asia", p1.getContinente());
			precond(1.74, p1.getAltura(), 0.00001);
			//----------------------
			Pais p2 = new Pais(new String("Turkey"), new String("Euro/Asia"), 1.74);
			assertTrue("\n> Error: p1.equals(p2): ", p1.equals(p2));
			//------------------------
			assertTrue("\n> Error: p1.equals((Object)p2): ", p1.equals((Object)p2));
			//------------------------
			Pais p3 = new Pais(new String("Turkey"), new String("Asia"), 1.74);
			assertTrue("\n> Error: p1.equals(p3): ", p1.equals(p3));
			//------------------------
			Pais p4 = new Pais(new String("Turkey"), new String("Euro/Asia"), 2.00);
			assertTrue("\n> Error: p1.equals(p4): ", p1.equals(p4));
			//------------------------
			Pais p5 = new Pais("TURKEY", "Euro/Asia", 1.74);
			assertFalse("\n> Error: p1.equals(p5): ", p1.equals(p5));
			//------------------------
			Pais p6 = new Pais("Iceland", "Europe", 1.81);
			assertFalse("\n> Error: p1.equals(p6): ", p1.equals(p6));
			//------------------------
			assertFalse("\n> Error: p1.equals(null): ", p1.equals(null));
			assertFalse("\n> Error: p1.equals(\"Esto es un String\"): ", p1.equals("Esto es un String"));
		}
		@Test(timeout = 1000)
		public void paisHashCodeTest1() throws Exception {
			precond("Turkey", p1.getNombre());
			precond("Euro/Asia", p1.getContinente());
			precond(1.74, p1.getAltura(), 0.00001);
			//----------------------
			int p1HashCode = p1.hashCode();
			//------------------------
			Pais p2 = new Pais("Turkey", "Euro/Asia", 1.74);
			assertEquals("\n> Error: p2.hashCode(): ", p1HashCode, p2.hashCode());
			//------------------------
			Pais p3 = new Pais("Turkey", "Asia", 1.74);
			assertEquals("\n> Error: p3.hashCode(): ", p1HashCode, p3.hashCode());
			//------------------------
			Pais p4 = new Pais("Turkey", "Euro/Asia", 2.00);
			assertEquals("\n> Error: p4.hashCode(): ", p1HashCode, p4.hashCode());
			//------------------------
			Pais p5 = new Pais("TURKEY", "Euro/Asia", 1.74);
			assertNotEquals("\n> Error: p5.hashCode(): ", p1HashCode, p5.hashCode());
			//------------------------
			Pais p6 = new Pais("Iceland", "Europe", 1.81);
			assertNotEquals("\n> Error: p5.hashCode(): ", p1HashCode, p6.hashCode());
		}
		@Test(timeout = 1000)
		public void paisCompareToTest1() throws Exception {
			precond("Turkey", p1.getNombre());
			precond("Euro/Asia", p1.getContinente());
			precond(1.74, p1.getAltura(), 0.00001);
			//----------------------
			Pais p2 = new Pais("Turkey", "Euro/Asia", 1.74);
			assertEquals("\n> Error: p1.compareTo(p2): ", 0, p1.compareTo(p2));
			//------------------------
			Pais p3 = new Pais("Turkey", "Asia", 1.74);
			assertEquals("\n> Error: p1.compareTo(p3): ", 0, p1.compareTo(p3));
			//------------------------
			Pais p4 = new Pais("Turkey", "Euro/Asia", 2.00);
			assertEquals("\n> Error: p1.compareTo(p4): ", 0, p1.compareTo(p4));
			//------------------------
			Pais p5 = new Pais("TURKEY", "Euro/Asia", 1.74);
			assertTrue("\n> Error: p1.compareTo(p5): ", p1.compareTo(p5) > 0);
			assertTrue("\n> Error: p5.compareTo(p1): ", p5.compareTo(p1) < 0);
			//------------------------
			Pais p6 = new Pais("Iceland", "Europe", 1.81);
			assertTrue("\n> Error: p1.compareTo(p6): ", p1.compareTo(p6) > 0);
			assertTrue("\n> Error: p6.compareTo(p1): ", p6.compareTo(p1) < 0);
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestMundo {
		/*private*/ static final String[] inputData = {
			"Albania,Europe,1.74",
			"Algeria,Africa,1.722",
			"Argentina,South America,1.745",
			"Australia,Oceania,1.756",
			"Austria,Europe,1.792",
			"Azerbaijan,Asia,1.718",
			"Bahrain,Asia,1.651",
			"Error,Error,Error",
			"Belgium,Europe,1.786",
			"Bolivia,South America,1.6",
			"Bosnia & Herzegovina,Europe,1.839",
			"Brazil,South America,1.731",
			"Error;Error;Error",
			"Bulgaria,Europe,1.752",
			"Cambodia,Asia,1.625",
			"Cameroon,Africa,1.706",
			"Canada,North America,1.751",
			"Chile,South America,1.71",
			"China,Asia,1.67",
			"Colombia,South America,1.706",
			"Croatia,Europe,1.805",
			"Cuba,North America,1.68",
			"Czech Republic,Europe,1.803",
		};
		/*private*/ static final String[] inputData2 = {
			"Albania,Europe,1.74",
			"Algeria,Africa,1.722",
			"Argentina,America,1.745",
			"Australia,Oceania,1.756",
			"Austria,Europe,1.792",
			"Azerbaijan,Asia,1.718",
			"Bahrain,Asia,1.651",
			"Error,Error,Error",
			"Belgium,Europe,1.786",
			"Bolivia,America,1.6",
			"Bosnia & Herzegovina,Europe,1.839",
			"Brazil,America,1.731",
			"Error;Error;Error",
			"Bulgaria,Europe,1.752",
			"Cambodia,Asia,1.625",
			"Cameroon,Africa,1.706",
			"Canada,America,1.751",
			"Chile,America,1.71",
			"China,Asia,1.67",
			"Colombia,America,1.706",
			"Croatia,Europe,1.805",
			"Cuba,America,1.68",
			"Czech Republic,Europe,1.803",
		};
		/*private*/ static final String inputList = normalize("[ Pais ( Albania , Europe , 1.74 ) , Pais ( Algeria , Africa , 1.722 ) , Pais ( Argentina , South America , 1.745 ) , Pais ( Australia , Oceania , 1.756 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Azerbaijan , Asia , 1.718 ) , Pais ( Bahrain , Asia , 1.651 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Bolivia , South America , 1.6 ) , Pais ( Bosnia & Herzegovina , Europe , 1.839 ) , Pais ( Brazil , South America , 1.731 ) , Pais ( Bulgaria , Europe , 1.752 ) , Pais ( Cambodia , Asia , 1.625 ) , Pais ( Cameroon , Africa , 1.706 ) , Pais ( Canada , North America , 1.751 ) , Pais ( Chile , South America , 1.71 ) , Pais ( China , Asia , 1.67 ) , Pais ( Colombia , South America , 1.706 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Cuba , North America , 1.68 ) , Pais ( Czech Republic , Europe , 1.803 ) ]");
		/*private*/ static final String inputList2 = normalize("[ Pais ( Albania , Europe , 1.74 ) , Pais ( Algeria , Africa , 1.722 ) , Pais ( Argentina , America , 1.745 ) , Pais ( Australia , Oceania , 1.756 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Azerbaijan , Asia , 1.718 ) , Pais ( Bahrain , Asia , 1.651 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Bolivia , America , 1.6 ) , Pais ( Bosnia & Herzegovina , Europe , 1.839 ) , Pais ( Brazil , America , 1.731 ) , Pais ( Bulgaria , Europe , 1.752 ) , Pais ( Cambodia , Asia , 1.625 ) , Pais ( Cameroon , Africa , 1.706 ) , Pais ( Canada , America , 1.751 ) , Pais ( Chile , America , 1.71 ) , Pais ( China , Asia , 1.67 ) , Pais ( Colombia , America , 1.706 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Cuba , America , 1.68 ) , Pais ( Czech Republic , Europe , 1.803 ) ]");
		public static Mundo createMundo() throws Exception {
			Mundo mnd1 = new Mundo();
			try {
				createFile("alts.txt", inputData);
				mnd1.cargar("alts.txt");
			} finally {
				deleteFile("alts.txt");
			}
			return mnd1;
		}
		public static Mundo createMundo2() throws Exception {
			Mundo mnd1 = new Mundo();
			try {
				createFile("alts.txt", inputData2);
				mnd1.cargar("alts.txt");
			} finally {
				deleteFile("alts.txt");
			}
			return mnd1;
		}
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Mundo JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Mundo JUnit Test");
		}
		@Before
		public void setUp() throws Exception {
			// Code executed before each test
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void mundoCtorTest1() throws Exception {
			Mundo mnd1 = createMundo();
			assertEquals("\n> Error: mnd1.getPaises():",
						 inputList,
						 normalize(mnd1.getPaises().toString()));
		}
		@Test(timeout = 1000)
		public void mundoCtorTest2() throws Exception {
			Mundo mnd1 = new Mundo();
			assertEquals("\n> Error: mnd1.getPaises():",
						 normalize("[ ]"),
						 normalize(mnd1.getPaises().toString()));
		}
		@Test(timeout = 1000)
		public void mundoPresentaEnPWTest1() {
			java.util.TreeMap<String,String> map = new java.util.TreeMap<>();
			map.put("pepe", "maria");
			map.put("lola", "juan");
			map.put("luis", "eva");
			String salida = "";
			java.io.StringWriter strwrtr = new java.io.StringWriter();
			try (java.io.PrintWriter pw = new java.io.PrintWriter(strwrtr)) {
				Mundo.presentaEnPW(pw, map);
			} finally {
				salida = strwrtr.toString();
			}
			assertEquals("\n> Error: Mundo.presentaEnPW(map):",
						 normalize("lola juan luis eva pepe maria"),
						 normalize(salida));
		}
		@Test(timeout = 1000)
		public void mundoPresentaEnConsolaTest1() {
			java.util.TreeMap<String,String> map = new java.util.TreeMap<>();
			map.put("pepe", "maria");
			map.put("lola", "juan");
			map.put("luis", "eva");
			String salida = "";
			SysOutCapture sysOutCapture = new SysOutCapture();
			try {
				sysOutCapture.sysOutCapture();
				Mundo.presentaEnConsola(map);
			} finally {
				salida = sysOutCapture.sysOutRelease();
			}
			assertEquals("\n> Error: Mundo.presentaEnConsola(map):",
						 normalize("lola juan luis eva pepe maria"),
						 normalize(salida));
		}
		@Test(timeout = 1000)
		public void mundoCargarTest1() throws Exception {
			try {
				Mundo mnd = new Mundo();
				mnd.cargar("no-existe.txt");
				fail("\n> Error: cargar(xxx): No se lanzo ninguna excepcion");
			} catch (java.io.IOException e) {
				//assertEquals("\n> Error: cargar(xxx): exception.getMessage():", "No existe", e.getMessage());
			} catch (Exception e) {
				fail("\n> Error: cargar(xxx): la excepcion lanzada no es IOException");
			}
			
		}
		@Test(timeout = 1000)
		public void mundoNumeroDePaisesPorContinenteTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.numeroDePaisesPorContinente():",
						 normalize("{ Africa = 2 , Asia = 4 , Europe = 7 , North America = 2 , Oceania = 1 , South America = 5 }"),
						 normalize(mnd1.numeroDePaisesPorContinente().toString()));
		}
		@Test(timeout = 1000)
		public void mundoPaisesPorAlturaTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.paisesPorAltura():",
						 normalize("{ 1.6 = [ Pais ( Bahrain , Asia , 1.651 ) , Pais ( Bolivia , South America , 1.6 ) , Pais ( Cambodia , Asia , 1.625 ) , Pais ( China , Asia , 1.67 ) , Pais ( Cuba , North America , 1.68 ) ] , 1.7 = [ Pais ( Albania , Europe , 1.74 ) , Pais ( Algeria , Africa , 1.722 ) , Pais ( Argentina , South America , 1.745 ) , Pais ( Australia , Oceania , 1.756 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Azerbaijan , Asia , 1.718 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Brazil , South America , 1.731 ) , Pais ( Bulgaria , Europe , 1.752 ) , Pais ( Cameroon , Africa , 1.706 ) , Pais ( Canada , North America , 1.751 ) , Pais ( Chile , South America , 1.71 ) , Pais ( Colombia , South America , 1.706 ) ] , 1.8 = [ Pais ( Bosnia & Herzegovina , Europe , 1.839 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Czech Republic , Europe , 1.803 ) ] }"),
						 normalize(mnd1.paisesPorAltura().toString()));
		}
		@Test(timeout = 1000)
		public void mundoPaisesPorContinenteTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.paisesPorContinente():",
						 normalize("{ Africa = [ Pais ( Algeria , Africa , 1.722 ) , Pais ( Cameroon , Africa , 1.706 ) ] , Asia = [ Pais ( Azerbaijan , Asia , 1.718 ) , Pais ( Bahrain , Asia , 1.651 ) , Pais ( Cambodia , Asia , 1.625 ) , Pais ( China , Asia , 1.67 ) ] , Europe = [ Pais ( Albania , Europe , 1.74 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Bosnia & Herzegovina , Europe , 1.839 ) , Pais ( Bulgaria , Europe , 1.752 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Czech Republic , Europe , 1.803 ) ] , North America = [ Pais ( Canada , North America , 1.751 ) , Pais ( Cuba , North America , 1.68 ) ] , Oceania = [ Pais ( Australia , Oceania , 1.756 ) ] , South America = [ Pais ( Argentina , South America , 1.745 ) , Pais ( Bolivia , South America , 1.6 ) , Pais ( Brazil , South America , 1.731 ) , Pais ( Chile , South America , 1.71 ) , Pais ( Colombia , South America , 1.706 ) ] }"),
						 normalize(mnd1.paisesPorContinente().toString()));
		}
		@Test(timeout = 1000)
		public void mundoPaisesPorInicialTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.paisesPorInicial():",
						 normalize("{ A = [ Pais ( Albania , Europe , 1.74 ) , Pais ( Algeria , Africa , 1.722 ) , Pais ( Argentina , South America , 1.745 ) , Pais ( Australia , Oceania , 1.756 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Azerbaijan , Asia , 1.718 ) ] , B = [ Pais ( Bahrain , Asia , 1.651 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Bolivia , South America , 1.6 ) , Pais ( Bosnia & Herzegovina , Europe , 1.839 ) , Pais ( Brazil , South America , 1.731 ) , Pais ( Bulgaria , Europe , 1.752 ) ] , C = [ Pais ( Cambodia , Asia , 1.625 ) , Pais ( Cameroon , Africa , 1.706 ) , Pais ( Canada , North America , 1.751 ) , Pais ( Chile , South America , 1.71 ) , Pais ( China , Asia , 1.67 ) , Pais ( Colombia , South America , 1.706 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Cuba , North America , 1.68 ) , Pais ( Czech Republic , Europe , 1.803 ) ] }"),
						 normalize(mnd1.paisesPorInicial().toString()));
		}
		@Test(timeout = 1000)
		public void mundoMediaPorContinenteTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.mediaPorContinente():",
						 normalize("{ Africa = 1.714 , Asia = 1.666 , Europe = 1.788142857142857 , North America = 1.7155 , Oceania = 1.756 , South America = 1.6984000000000001 }"),
						 normalize(mnd1.mediaPorContinente().toString()));
		}
		@Test(timeout = 1000)
		public void mundoContinentesConMasPaisesTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.continentesConMasPaises():",
						 normalize("[ Europe ]"),
						 normalize(mnd1.continentesConMasPaises().toString()));
		}
		@Test(timeout = 1000)
		public void mundoContinentesConMasPaisesTest2() throws Exception {
			Mundo mnd1 = createMundo2();
			precond(inputList2, normalize(mnd1.getPaises().toString()));
			//----------------------
			String salida = normalize(mnd1.continentesConMasPaises().toString());
			//----------------------
			assertTrue("\n> Error: mnd1.continentesConMasPaises():",
					   (normalize("[ America, Europe ]").equals(salida)
						|| normalize("[ Europe, America ]").equals(salida)));
		}
		//--------------------------------------------------------------
		@Test(timeout = 1000)
		public void mundoPaisesOrdenadosPorAlturaTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.paisesOrdenadosPorAltura():",
						 normalize("[ Pais ( Bolivia , South America , 1.6 ) , Pais ( Cambodia , Asia , 1.625 ) , Pais ( Bahrain , Asia , 1.651 ) , Pais ( China , Asia , 1.67 ) , Pais ( Cuba , North America , 1.68 ) , Pais ( Cameroon , Africa , 1.706 ) , Pais ( Colombia , South America , 1.706 ) , Pais ( Chile , South America , 1.71 ) , Pais ( Azerbaijan , Asia , 1.718 ) , Pais ( Algeria , Africa , 1.722 ) , Pais ( Brazil , South America , 1.731 ) , Pais ( Albania , Europe , 1.74 ) , Pais ( Argentina , South America , 1.745 ) , Pais ( Canada , North America , 1.751 ) , Pais ( Bulgaria , Europe , 1.752 ) , Pais ( Australia , Oceania , 1.756 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Czech Republic , Europe , 1.803 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Bosnia & Herzegovina , Europe , 1.839 ) ]"),
						 normalize(mnd1.paisesOrdenadosPorAltura().toString()));
		}
		@Test(timeout = 1000)
		public void mundoPaisesPorContinenteAlturaTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.paisesPorContinenteAltura():",
						 normalize("{ Africa = [ Pais ( Cameroon , Africa , 1.706 ) , Pais ( Algeria , Africa , 1.722 ) ] , Asia = [ Pais ( Cambodia , Asia , 1.625 ) , Pais ( Bahrain , Asia , 1.651 ) , Pais ( China , Asia , 1.67 ) , Pais ( Azerbaijan , Asia , 1.718 ) ] , Europe = [ Pais ( Albania , Europe , 1.74 ) , Pais ( Bulgaria , Europe , 1.752 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Czech Republic , Europe , 1.803 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Bosnia & Herzegovina , Europe , 1.839 ) ] , North America = [ Pais ( Cuba , North America , 1.68 ) , Pais ( Canada , North America , 1.751 ) ] , Oceania = [ Pais ( Australia , Oceania , 1.756 ) ] , South America = [ Pais ( Bolivia , South America , 1.6 ) , Pais ( Colombia , South America , 1.706 ) , Pais ( Chile , South America , 1.71 ) , Pais ( Brazil , South America , 1.731 ) , Pais ( Argentina , South America , 1.745 ) ] }"),
						 normalize(mnd1.paisesPorContinenteAltura().toString()));
		}
		@Test(timeout = 1000)
		public void mundoPaisesPorContinenteAlturaDecTest1() throws Exception {
			Mundo mnd1 = createMundo();
			precond(inputList, normalize(mnd1.getPaises().toString()));
			//----------------------
			assertEquals("\n> Error: mnd1.paisesPorContinenteAlturaDec():",
						 normalize("{ Africa = [ Pais ( Algeria , Africa , 1.722 ) , Pais ( Cameroon , Africa , 1.706 ) ] , Asia = [ Pais ( Azerbaijan , Asia , 1.718 ) , Pais ( China , Asia , 1.67 ) , Pais ( Bahrain , Asia , 1.651 ) , Pais ( Cambodia , Asia , 1.625 ) ] , Europe = [ Pais ( Bosnia & Herzegovina , Europe , 1.839 ) , Pais ( Croatia , Europe , 1.805 ) , Pais ( Czech Republic , Europe , 1.803 ) , Pais ( Austria , Europe , 1.792 ) , Pais ( Belgium , Europe , 1.786 ) , Pais ( Bulgaria , Europe , 1.752 ) , Pais ( Albania , Europe , 1.74 ) ] , North America = [ Pais ( Canada , North America , 1.751 ) , Pais ( Cuba , North America , 1.68 ) ] , Oceania = [ Pais ( Australia , Oceania , 1.756 ) ] , South America = [ Pais ( Argentina , South America , 1.745 ) , Pais ( Brazil , South America , 1.731 ) , Pais ( Chile , South America , 1.71 ) , Pais ( Colombia , South America , 1.706 ) , Pais ( Bolivia , South America , 1.6 ) ] }"),
						 normalize(mnd1.paisesPorContinenteAlturaDec().toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//-- VistaControlador --------------------------------------------------
	//----------------------------------------------------------------------
	public static class VistaControlador implements VistaAlturas {
		private String userNombreFichero;
		private String userTipoListado;
		private String vistaAreaTexto;
		private String vistaMensajeEstadoOk;
		private String vistaMensajeEstadoError;
		private ControladorAlturas ctrl;
		private Mundo modelo;
		private java.awt.event.ActionEvent limpiarEvent;
		private java.awt.event.ActionEvent cargarEvent;
		private java.awt.event.ActionEvent listadoEvent;
		public VistaControlador() {
		    userNombreFichero = "";
		    userTipoListado = "";
		    vistaAreaTexto = "";
		    vistaMensajeEstadoOk = "";
		    vistaMensajeEstadoError = "";
		    limpiarEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+1, VistaAlturas.LIMPIAR);
		    cargarEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+2, VistaAlturas.CARGAR);
		    listadoEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+3, VistaAlturas.LISTADO);
			modelo = new Mundo();
		    ctrl = new ControladorAlturas(this, modelo);
		}
		@Override public void registrarControlador(java.awt.event.ActionListener c) { /*empty*/ }
		@Override public String getNombreFichero() { return userNombreFichero; }
		@Override public String getTipoListado() { return userTipoListado; }
		@Override public void limpiar() { vistaAreaTexto = ""; }
		@Override public void anyadirTexto(String m) { vistaAreaTexto += " " + m; }
		@Override public void error(String m) { vistaMensajeEstadoError = m ; }
		@Override public void ok(String m) { vistaMensajeEstadoOk = m ; }
		public void setUserNombreFichero(String x) { userNombreFichero = x; }
		public void setUserTipoListado(String x) { userTipoListado = x; }
		public String getVistaAreaTexto() { return vistaAreaTexto; }
		public String getVistaMensajeEstadoOk() { return vistaMensajeEstadoOk; }
		public String getVistaMensajeEstadoError() { return vistaMensajeEstadoError; }
		public void pulsaLimpiar() { ctrl.actionPerformed(limpiarEvent); }
		public void pulsaCargar() { ctrl.actionPerformed(cargarEvent); }
		public void pulsaListado() { ctrl.actionPerformed(listadoEvent); }
		public Mundo getModelo() { return modelo; }
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestControladorAlturas {
		private VistaControlador vc1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of VistaControlador JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of VistaControlador JUnit Test");
		}
		@Before
		public void setUp() {
			// Code executed before each test
			vc1 = new VistaControlador();
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void vistaControladorCtorTest1() {
			vc1.setUserNombreFichero("no-existe.txt");
			vc1.setUserTipoListado(VistaAlturas.PAISES_POR_ALTURA);
			assertEquals("\n> Error: Inicio: Area-de-Texto",
						 normalize(""),
						 normalize(vc1.getVistaAreaTexto()));
			assertEquals("\n> Error: Inicio: Estado-Ok",
						 normalize("Inicio"),
						 normalize(vc1.getVistaMensajeEstadoOk()));
			assertEquals("\n> Error: Inicio: Estado-Error",
						 normalize(""),
						 normalize(vc1.getVistaMensajeEstadoError()));
		}
		@Test(timeout = 1000)
		public void vistaControladorLimpiarTest1() {
			vc1.setUserNombreFichero("no-existe.txt");
			vc1.setUserTipoListado(VistaAlturas.PAISES_POR_ALTURA);
			vc1.anyadirTexto("Debe estar vacio");
			vc1.pulsaLimpiar();
			assertEquals("\n> Error: Inicio: Area-de-Texto",
						 normalize(""),
						 normalize(vc1.getVistaAreaTexto()));
			assertEquals("\n> Error: Inicio: Estado-Ok",
						 normalize("Operacion correcta"),
						 normalize(vc1.getVistaMensajeEstadoOk()));
			assertEquals("\n> Error: Inicio: Estado-Error",
						 normalize(""),
						 normalize(vc1.getVistaMensajeEstadoError()));
		}
		@Test(timeout = 1000)
		public void vistaControladorCargarTest1() {
			vc1.setUserNombreFichero("no-existe.txt");
			vc1.setUserTipoListado(VistaAlturas.PAISES_POR_ALTURA);
			vc1.pulsaCargar();
			assertEquals("\n> Error: Inicio: Area-de-Texto",
						 normalize(""),
						 normalize(vc1.getVistaAreaTexto()));
			assertEquals("\n> Error: Inicio: Estado-Ok",
						 normalize("Inicio"),
						 normalize(vc1.getVistaMensajeEstadoOk()));
			assertTrue("\n> Error: Inicio: Estado-Error",
					   (normalize("Error: no-existe.txt (No existe el fichero o el directorio)").equals(normalize(vc1.getVistaMensajeEstadoError()))
						|| normalize("Error: no-existe.txt").equals(normalize(vc1.getVistaMensajeEstadoError()))));
		}
		@Test(timeout = 1000)
		public void vistaControladorCargarTest2() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.PAISES_POR_ALTURA);
				vc1.pulsaCargar();
				assertEquals("\n> Error: Cargar: Lista-de-Paises",
							 JUnitTestMundo.inputList,
							 normalize(vc1.getModelo().getPaises().toString()));
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize(""),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest1() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.NUMERO_DE_PAISES_POR_CONTINENTE);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("NumeroDePaisesPorContinente ----------------- Africa 2 Asia 4 Europe 7 North America 2 Oceania 1 South America 5"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}

		@Test(timeout = 1000)
		public void vistaControladorListadoTest2() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.PAISES_POR_ALTURA);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("PaisesPorAltura ----------------- 1.6	[Pais(Bahrain, Asia, 1.651), Pais(Bolivia, South America, 1.6), Pais(Cambodia, Asia, 1.625), Pais(China, Asia, 1.67), Pais(Cuba, North America, 1.68)] 1.7	[Pais(Albania, Europe, 1.74), Pais(Algeria, Africa, 1.722), Pais(Argentina, South America, 1.745), Pais(Australia, Oceania, 1.756), Pais(Austria, Europe, 1.792), Pais(Azerbaijan, Asia, 1.718), Pais(Belgium, Europe, 1.786), Pais(Brazil, South America, 1.731), Pais(Bulgaria, Europe, 1.752), Pais(Cameroon, Africa, 1.706), Pais(Canada, North America, 1.751), Pais(Chile, South America, 1.71), Pais(Colombia, South America, 1.706)] 1.8	[Pais(Bosnia & Herzegovina, Europe, 1.839), Pais(Croatia, Europe, 1.805), Pais(Czech Republic, Europe, 1.803)]"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest3() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.PAISES_POR_CONTINENTE);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("PaisesPorContinente ----------------- Africa	[Pais(Algeria, Africa, 1.722), Pais(Cameroon, Africa, 1.706)] Asia	[Pais(Azerbaijan, Asia, 1.718), Pais(Bahrain, Asia, 1.651), Pais(Cambodia, Asia, 1.625), Pais(China, Asia, 1.67)] Europe	[Pais(Albania, Europe, 1.74), Pais(Austria, Europe, 1.792), Pais(Belgium, Europe, 1.786), Pais(Bosnia & Herzegovina, Europe, 1.839), Pais(Bulgaria, Europe, 1.752), Pais(Croatia, Europe, 1.805), Pais(Czech Republic, Europe, 1.803)] North America	[Pais(Canada, North America, 1.751), Pais(Cuba, North America, 1.68)] Oceania	[Pais(Australia, Oceania, 1.756)] South America	[Pais(Argentina, South America, 1.745), Pais(Bolivia, South America, 1.6), Pais(Brazil, South America, 1.731), Pais(Chile, South America, 1.71), Pais(Colombia, South America, 1.706)]"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest4() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.PAISES_POR_INICIAL);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("PaisesPorInicial ----------------- A	[Pais(Albania, Europe, 1.74), Pais(Algeria, Africa, 1.722), Pais(Argentina, South America, 1.745), Pais(Australia, Oceania, 1.756), Pais(Austria, Europe, 1.792), Pais(Azerbaijan, Asia, 1.718)] B	[Pais(Bahrain, Asia, 1.651), Pais(Belgium, Europe, 1.786), Pais(Bolivia, South America, 1.6), Pais(Bosnia & Herzegovina, Europe, 1.839), Pais(Brazil, South America, 1.731), Pais(Bulgaria, Europe, 1.752)] C	[Pais(Cambodia, Asia, 1.625), Pais(Cameroon, Africa, 1.706), Pais(Canada, North America, 1.751), Pais(Chile, South America, 1.71), Pais(China, Asia, 1.67), Pais(Colombia, South America, 1.706), Pais(Croatia, Europe, 1.805), Pais(Cuba, North America, 1.68), Pais(Czech Republic, Europe, 1.803)]"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest5() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.MEDIA_POR_CONTINENTE);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("MediaPorContinente ----------------- Africa 1.714 Asia 1.666 Europe 1.788142857142857 North America 1.7155 Oceania 1.756 South America 1.6984000000000001"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest6() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.CONTINENTES_CON_MAS_PAISES);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("ContinentesConMasPaises ----------------- [Europe]"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest7() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.PAISES_ORDENADOS_POR_ALTURA);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("PaisesOrdenadosPorAltura ----------------- [Pais(Bolivia, South America, 1.6), Pais(Cambodia, Asia, 1.625), Pais(Bahrain, Asia, 1.651), Pais(China, Asia, 1.67), Pais(Cuba, North America, 1.68), Pais(Cameroon, Africa, 1.706), Pais(Colombia, South America, 1.706), Pais(Chile, South America, 1.71), Pais(Azerbaijan, Asia, 1.718), Pais(Algeria, Africa, 1.722), Pais(Brazil, South America, 1.731), Pais(Albania, Europe, 1.74), Pais(Argentina, South America, 1.745), Pais(Canada, North America, 1.751), Pais(Bulgaria, Europe, 1.752), Pais(Australia, Oceania, 1.756), Pais(Belgium, Europe, 1.786), Pais(Austria, Europe, 1.792), Pais(Czech Republic, Europe, 1.803), Pais(Croatia, Europe, 1.805), Pais(Bosnia & Herzegovina, Europe, 1.839)]"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest8() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.PAISES_POR_CONTINENTE_ALTURA);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("PaisesPorContinenteAltura ----------------- Africa	[Pais(Cameroon, Africa, 1.706), Pais(Algeria, Africa, 1.722)] Asia	[Pais(Cambodia, Asia, 1.625), Pais(Bahrain, Asia, 1.651), Pais(China, Asia, 1.67), Pais(Azerbaijan, Asia, 1.718)] Europe	[Pais(Albania, Europe, 1.74), Pais(Bulgaria, Europe, 1.752), Pais(Belgium, Europe, 1.786), Pais(Austria, Europe, 1.792), Pais(Czech Republic, Europe, 1.803), Pais(Croatia, Europe, 1.805), Pais(Bosnia & Herzegovina, Europe, 1.839)] North America	[Pais(Cuba, North America, 1.68), Pais(Canada, North America, 1.751)] Oceania	[Pais(Australia, Oceania, 1.756)] South America	[Pais(Bolivia, South America, 1.6), Pais(Colombia, South America, 1.706), Pais(Chile, South America, 1.71), Pais(Brazil, South America, 1.731), Pais(Argentina, South America, 1.745)]"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorListadoTest9() throws Exception {
			try {
				createFile("alts.txt", JUnitTestMundo.inputData);
				//------------------
				vc1.setUserNombreFichero("alts.txt");
				vc1.setUserTipoListado(VistaAlturas.PAISES_POR_CONTINENTE_ALTURA_DEC);
				vc1.pulsaCargar();
				precond(JUnitTestMundo.inputList,
						normalize(vc1.getModelo().getPaises().toString()));
				vc1.pulsaListado();
				assertEquals("\n> Error: Cargar: Area-de-Texto",
							 normalize("PaisesPorContinenteAlturaDec ----------------- Africa	[Pais(Algeria, Africa, 1.722), Pais(Cameroon, Africa, 1.706)] Asia	[Pais(Azerbaijan, Asia, 1.718), Pais(China, Asia, 1.67), Pais(Bahrain, Asia, 1.651), Pais(Cambodia, Asia, 1.625)] Europe	[Pais(Bosnia & Herzegovina, Europe, 1.839), Pais(Croatia, Europe, 1.805), Pais(Czech Republic, Europe, 1.803), Pais(Austria, Europe, 1.792), Pais(Belgium, Europe, 1.786), Pais(Bulgaria, Europe, 1.752), Pais(Albania, Europe, 1.74)] North America	[Pais(Canada, North America, 1.751), Pais(Cuba, North America, 1.68)] Oceania	[Pais(Australia, Oceania, 1.756)] South America	[Pais(Argentina, South America, 1.745), Pais(Brazil, South America, 1.731), Pais(Chile, South America, 1.71), Pais(Colombia, South America, 1.706), Pais(Bolivia, South America, 1.6)]"),
							 normalize(vc1.getVistaAreaTexto()));
				assertEquals("\n> Error: Cargar: Estado-Ok",
							 normalize("Operacion correcta"),
							 normalize(vc1.getVistaMensajeEstadoOk()));
				assertEquals("\n> Error: Cargar: Estado-Error",
							 normalize(""),
							 normalize(vc1.getVistaMensajeEstadoError()));
			} finally {
				deleteFile("alts.txt");
			}
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTestSuite------------------------------------------------------
	//----------------------------------------------------------------------
	@RunWith(Suite.class)
	@Suite.SuiteClasses({ JUnitTestPais.class ,
				JUnitTestMundo.class,
				JUnitTestControladorAlturas.class
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
