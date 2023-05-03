
//--------------------------------------------------------------------------
import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
//----------------------------------------------------------------------

import rank.*;
import gui.*;

//--------------------------------------------------------------------------

public class TestRunnerPr61 {
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestLink {
		private Link p1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Link JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Link JUnit Test");
		}
		@Before
		public void setUp() throws Exception {
			// Code executed before each test
			p1 = new Link(new String("AAA"), new String("BBB"));
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void linkCtorTest1() {
			assertEquals("\n> Error: p1.getOrigin():",
						 "AAA",
						 p1.getOrigin());
			assertEquals("\n> Error: p1.getLinked():",
						 "BBB",
						 p1.getLinked());
		}
		@Test(timeout = 1000)
		public void linkEqualsTest1() throws Exception {
			precond("AAA", p1.getOrigin());
			precond("BBB", p1.getLinked());
			//----------------------
			Link p2 = new Link(new String("AAA"), new String("BBB"));
			assertTrue("\n> Error: p1.equals(p2): ", p1.equals(p2));
			//------------------------
			assertTrue("\n> Error: p1.equals((Object)p2): ", p1.equals((Object)p2));
			//------------------------
			Link p3 = new Link(new String("aaa"), new String("bbb"));
			assertTrue("\n> Error: p1.equals(p3): ", p1.equals(p3));
			//------------------------
			Link p4 = new Link("AAA", "CCC");
			assertFalse("\n> Error: p1.equals(p4): ", p1.equals(p4));
			//------------------------
			Link p5 = new Link("CCC", "BBB");
			assertFalse("\n> Error: p1.equals(p5): ", p1.equals(p5));
			//------------------------
			assertFalse("\n> Error: p1.equals(null): ", p1.equals(null));
			assertFalse("\n> Error: p1.equals(\"Esto es un String\"): ", p1.equals("Esto es un String"));
		}
		@Test(timeout = 1000)
		public void linkHashCodeTest1() throws Exception {
			precond("AAA", p1.getOrigin());
			precond("BBB", p1.getLinked());
			//----------------------
			int p1HashCode = p1.hashCode();
			//------------------------
			Link p2 = new Link("AAA", "BBB");
			assertEquals("\n> Error: p2.hashCode(): ", p1HashCode, p2.hashCode());
			//------------------------
			Link p3 = new Link("aaa", "bbb");
			assertEquals("\n> Error: p3.hashCode(): ", p1HashCode, p3.hashCode());
			//------------------------
			Link p4 = new Link("AAA", "CCC");
			assertNotEquals("\n> Error: p4.hashCode(): ", p1HashCode, p4.hashCode());
			//------------------------
			Link p5 = new Link("CCC", "BBB");
			assertNotEquals("\n> Error: p5.hashCode(): ", p1HashCode, p5.hashCode());
			//------------------------
		}
		@Test(timeout = 1000)
		public void linkToStringTest1() {
			precond("AAA", p1.getOrigin());
			precond("BBB", p1.getLinked());
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA->BBB"),
						 normalize(p1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestSite {
		private Site p1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Site JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Site JUnit Test");
		}
		@Before
		public void setUp() throws Exception {
			// Code executed before each test
			p1 = new Site(new String("AAA"));
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void siteCtorTest1() {
			assertEquals("\n> Error: p1.getName():",
						 "AAA",
						 p1.getName());
			assertEquals("\n> Error: p1.getRank():",
						 0.0,
						 p1.getRank(), 0.00001);
		}
		@Test(timeout = 1000)
		public void siteCtorTest2() {
			assertTrue("\n> Error: Site implements Comparable<?>:", ((Object)p1 instanceof Comparable<?>));
		}
		@Test(timeout = 1000)
		public void siteAddRankTest1() throws Exception {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			//----------------------
			p1.addRank(13.5);
			assertEquals("\n> Error: p1.getName(); p1.addRank(13.5):",
						 "AAA",
						 p1.getName());
			assertEquals("\n> Error: p1.getRank(); p1.addRank(13.5):",
						 13.5,
						 p1.getRank(), 0.00001);
			//----------------------
			p1.addRank(4.7);
			assertEquals("\n> Error: p1.getName(); p1.addRank(4.7):",
						 "AAA",
						 p1.getName());
			assertEquals("\n> Error: p1.getRank(); p1.addRank(4.7):",
						 18.2,
						 p1.getRank(), 0.00001);
		}
		@Test(timeout = 1000)
		public void siteEqualsTest1() throws Exception {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			//----------------------
			Site p2 = new Site(new String("AAA"));
			assertTrue("\n> Error: p1.equals(p2): ", p1.equals(p2));
			//------------------------
			assertTrue("\n> Error: p1.equals((Object)p2): ", p1.equals((Object)p2));
			//------------------------
			Site p3 = new Site(new String("aaa"));
			assertTrue("\n> Error: p1.equals(p3): ", p1.equals(p3));
			//------------------------
			Site p4 = new Site(new String("AAA")); p4.addRank(7);
			assertTrue("\n> Error: p1.equals(p4): ", p1.equals(p4));
			//------------------------
			Site p5 = new Site("CCC");
			assertFalse("\n> Error: p1.equals(p5): ", p1.equals(p5));
			//------------------------
			//------------------------
			assertFalse("\n> Error: p1.equals(null): ", p1.equals(null));
			assertFalse("\n> Error: p1.equals(\"Esto es un String\"): ", p1.equals("Esto es un String"));
		}
		@Test(timeout = 1000)
		public void siteHashCodeTest1() throws Exception {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			//----------------------
			int p1HashCode = p1.hashCode();
			//------------------------
			Site p2 = new Site("AAA");
			assertEquals("\n> Error: p2.hashCode(): ", p1HashCode, p2.hashCode());
			//------------------------
			Site p3 = new Site("aaa");
			assertEquals("\n> Error: p3.hashCode(): ", p1HashCode, p3.hashCode());
			//------------------------
			Site p4 = new Site("AAA"); p4.addRank(7);
			assertEquals("\n> Error: p4.hashCode(): ", p1HashCode, p4.hashCode());
			//------------------------
			Site p5 = new Site("CCC");
			assertNotEquals("\n> Error: p5.hashCode(): ", p1HashCode, p5.hashCode());
			//------------------------
		}
		@Test(timeout = 1000)
		public void siteCompareToTest1() throws Exception {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			//----------------------
			Site p2 = new Site("AAA");
			assertEquals("\n> Error: p1.compareTo(p2): ", 0, p1.compareTo(p2));
			//------------------------
			Site p3 = new Site("aaa");
			assertEquals("\n> Error: p1.compareTo(p3): ", 0, p1.compareTo(p3));
			//------------------------
			Site p4 = new Site("AAA"); p4.addRank(7);
			assertEquals("\n> Error: p1.compareTo(p4): ", 0, p1.compareTo(p4));
			//------------------------
			Site p5 = new Site("BBB");
			assertTrue("\n> Error: p1.compareTo(p5): ", p1.compareTo(p5) < 0);
			assertTrue("\n> Error: p5.compareTo(p1): ", p5.compareTo(p1) > 0);
			//------------------------
			Site p6 = new Site("bbb");
			assertTrue("\n> Error: p1.compareTo(p6): ", p1.compareTo(p6) < 0);
			assertTrue("\n> Error: p6.compareTo(p1): ", p6.compareTo(p1) > 0);
		}
		@Test(timeout = 1000)
		public void siteToStringTest1() {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(0.00000)"),
						 normalize(p1.toString()));
		}
		@Test(timeout = 1000)
		public void siteToStringTest2() {
			p1.addRank(7.12345678);
			precond("AAA", p1.getName());
			precond(7.12345678, p1.getRank(), 0.00001);
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(7.12346)"),
						 normalize(p1.toString()));
		}
		@Test(timeout = 1000)
		public void siteToStringTest3() {
			p1.addRank(7.1234444);
			precond("AAA", p1.getName());
			precond(7.1234444, p1.getRank(), 0.00001);
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(7.12344)"),
						 normalize(p1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestWeb {
		private static final String[] inputData = {
			"I->C",
			"J->C",
			"A->C",
			"A->D",
			"B->C",
			"B->F",
			"D->F",
			"E->B",
			"E->H",
			"F->G",
			"F->H",
			"G->E",
			"G->H",
		};
		public static String normalizeWeb(String wstr) {
			try {
				StringBuilder sb = new StringBuilder(normalize(wstr));
				int webidx = sb.indexOf("Web (");
				while (webidx >= 0) {
					webidx += 5;
					for (int i = 0; i < 2; ++i) {
						int inicio = sb.indexOf("[", webidx);
						if (inicio > webidx) {
							int fin = sb.indexOf("]", inicio+1);
							if (fin > inicio) {
								webidx = fin;
								String nuevo = normalizeListStr(sb.substring(inicio, fin+1), "\\s*,\\s*", "[", "]");
								sb.replace(inicio, fin+1, nuevo);
								webidx = inicio + nuevo.length();
							}
						}
					}
					webidx = sb.indexOf("Web (", webidx);
				}
				wstr = sb.toString();
			} catch (Exception ex) {
				// ignore
			}
			return normalize(wstr);
		}
		private static final String inputList = normalizeWeb("Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])");
		/*private*/ static final String[] inputSites = {
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
		};
		private static final String inputListClicks = normalizeWeb("Web([A(0.50000), B(0.67156), C(1.29288), D(0.62500), E(0.68627), F(0.98038), G(0.74509), H(1.10293), I(0.50000), J(0.50000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])");
		public static Web createWeb() throws Exception {
			Web web1 = new Web();
			try {
				for (String arc: inputData) {
					web1.addLink(arc);
				}
			} catch (Exception e) {
				// ignorar
			}
			return web1;
		}
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of Web JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of Web JUnit Test");
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
		public void webCtorTest1() throws Exception {
			Web web2 = new Web();
			assertEquals("\n> Error: web2.Ctor(); web2.toString():",
						 normalize("Web([], [])"),
						 normalize(web2.toString()));
		}
		@Test(timeout = 1000)
		public void webAddLinkTest1() throws Exception {
			Web web1 = createWeb();
			assertEquals("\n> Error: web1.Ctor(); web.addLink(); web1.toString():",
						 inputList,
						 normalizeWeb(web1.toString()));
		}
		@Test(timeout = 1000)
		public void webAddLinkTest2() throws Exception {
			try {
				Web web2 = new Web();
				web2.addLink("AA;BB");
				fail("\n> Error: web2.addLink(AA;BB): No se lanzo ninguna excepcion");
			} catch (IllegalArgumentException e) {
				assertTrue("\n> Error: web2.addLink(AA;BB): exception.getMessage().contains(AA;BB):", e.getMessage().contains("AA;BB"));
			} catch (Exception e) {
				fail("\n> Error: web2.addLink(AA;BB):  la excepcion lanzada no es IllegalArgumentException");
			}
		}
		@Test(timeout = 1000)
		public void webGetSiteTest1() throws Exception {
			Web web1 = createWeb();
			precond(inputList,
					normalizeWeb(web1.toString()));
			for (String st : inputSites) {
				Site s = web1.getSite(st);
				assertEquals("\n> Error: web.getSite("+st+").getName():",
							 st,
							 s.getName());
				assertEquals("\n> Error: web.getSite("+st+").getRank():",
							 0.0,
							 s.getRank(), 0.00001);
			}
		}
		@Test(timeout = 1000)
		public void webGetSiteTest2() throws Exception {
			try {
				Web web2 = new Web();
				Site s = web2.getSite("XXX");
				fail("\n> Error: web2.getSite(XXX): No se lanzo ninguna excepcion");
			} catch (java.util.NoSuchElementException e) {
				assertTrue("\n> Error: web2.getSite(XXX): exception.getMessage().contains(XXX):", e.getMessage().contains("XXX"));
			} catch (Exception e) {
				fail("\n> Error: web2.getSite(XXX):  la excepcion lanzada no es NoSuchElementException");
			}
		}
		@Test(timeout = 1000)
		public void webGetSiteTest3() throws Exception {
			try {
				Web web1 = createWeb();
				Site s = web1.getSite("XXX");
				fail("\n> Error: web1.getSite(XXX): No se lanzo ninguna excepcion");
			} catch (java.util.NoSuchElementException e) {
				assertTrue("\n> Error: web1.getSite(XXX): exception.getMessage().contains(XXX):", e.getMessage().contains("XXX"));
			} catch (Exception e) {
				fail("\n> Error: web1.getSite(XXX):  la excepcion lanzada no es NoSuchElementException");
			}
		}
		@Test(timeout = 1000)
		public void webGetNamesTest1() throws Exception {
			Web web1 = createWeb();
			precond(inputList,
					normalizeWeb(web1.toString()));
			java.util.TreeSet<String> st = new java.util.TreeSet<>(web1.getNames());
			assertEquals("\n> Error: web.getNames():",
						 normalize("[A, B, C, D, E, F, G, H, I, J]"),
						 normalize(st.toString()));
		}
		@Test(timeout = 1000)
		public void webGetNamesTest2() throws Exception {
			Web web1 = new Web();
			precond(normalize("Web([], [])"),
					normalize(web1.toString()));
			java.util.TreeSet<String> st = new java.util.TreeSet<>(web1.getNames());
			assertEquals("\n> Error: web.getNames():",
						 normalize("[]"),
						 normalize(st.toString()));
		}
		@Test(timeout = 1000)
		public void webClickTest1() throws Exception {
			Web web1 = createWeb();
			precond(inputList,
					normalizeWeb(web1.toString()));
			for (String st : inputSites) {
				web1.click(st);
			}
			assertEquals("\n> Error: web1.click("+java.util.Arrays.toString(inputSites)+"); web1.toString():",
						 inputListClicks,
						 normalizeWeb(web1.toString()));
		}
		@Test(timeout = 1000)
		public void webClickTest2() throws Exception {
			Web web1 = createWeb();
			precond(inputList,
					normalizeWeb(web1.toString()));
			web1.click("XXX");
			assertEquals("\n> Error: web1.click(XXX); web1.toString():",
						 inputList,
						 normalizeWeb(web1.toString()));
		}
		@Test(timeout = 1000)
		public void webSimulateClickTest1() throws Exception {
			Web web1 = createWeb();
			//----------------------
			((java.util.Random)getMemberObject(web1, Web.class, java.util.Random.class, "alea")).setSeed(1);
			//----------------------
			precond(inputList,
					normalizeWeb(web1.toString()));
			web1.simulateClick(100);
			assertEquals("\n> Error: web1.simulateClick(); web1.toString():",
						 normalizeWeb("Web([A(5.00000), B(4.87248), C(12.96808), D(5.25000), E(7.49017), F(9.84308), G(7.96075), H(11.32339), I(6.00000), J(5.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
						 normalizeWeb(web1.toString()));
		}
		@Test(timeout = 1000)
		public void webGetSitesByNameTest1() throws Exception {
			Web web1 = createWeb();
			precond(inputList,
					normalizeWeb(web1.toString()));
			java.util.SortedSet<Site> st = web1.getSitesByName();
			assertEquals("\n> Error: web.getSitesByName():",
						 normalize("[A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)]"),
						 normalize(st.toString()));
		}
		@Test(timeout = 1000)
		public void webGetSitesByRankTest1() throws Exception {
			Web web1 = createWeb();
			precond(inputList,
					normalizeWeb(web1.toString()));
			java.util.SortedSet<Site> st = web1.getSitesByRank();
			assertEquals("\n> Error: web.getSitesByRank():",
						 normalize("[A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)]"),
						 normalize(st.toString()));
		}
		@Test(timeout = 1000)
		public void webGetSitesByRankTest2() throws Exception {
			Web web1 = createWeb();
			precond(inputList,
					normalizeWeb(web1.toString()));
			//----------------------
			for (String st : inputSites) {
				web1.click(st);
			}
			precond(inputListClicks,
					normalizeWeb(web1.toString()));
			//----------------------
			java.util.SortedSet<Site> st = web1.getSitesByRank();
			assertEquals("\n> Error: web.getSitesByRank():",
						 normalize("[C(1.29288), H(1.10293), F(0.98038), G(0.74509), E(0.68627), B(0.67156), D(0.62500), A(0.50000), I(0.50000), J(0.50000)]"),
						 normalize(st.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestSiteExtended {
		private SiteExtended p1;
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of SiteExtended JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of SiteExtended JUnit Test");
		}
		@Before
		public void setUp() throws Exception {
			// Code executed before each test
			p1 = new SiteExtended("AAA");
		}
		@After
		public void tearDown() {
			// Code executed after each test
		}
		@Test(timeout = 1000)
		public void siteExtendedCtorTest1() {
			assertEquals("\n> Error: p1.getName():",
						 "AAA",
						 p1.getName());
			assertEquals("\n> Error: p1.getRank():",
						 0.0,
						 p1.getRank(), 0.00001);
			assertTrue("\n> Error: p1.isValid():",
						 p1.isValid());
		}
		@Test(timeout = 1000)
		public void siteExtendedCtorTest2() {
			assertTrue("\n> Error: SiteExtended extends Site:", ((Object)p1 instanceof Site));
		}
		@Test(timeout = 1000)
		public void siteExtendedSetValidTest1() throws Exception {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			precond(true, p1.isValid());
			//----------------------
			p1.setValid(false);
			assertEquals("\n> Error: p1.getName(); p1.setValid(false):",
						 "AAA",
						 p1.getName());
			assertEquals("\n> Error: p1.getRank(); p1.setValid(false):",
						 0.0,
						 p1.getRank(), 0.00001);
			assertFalse("\n> Error: p1.isValid(); p1.setValid(false):",
						 p1.isValid());
		}
		@Test(timeout = 1000)
		public void siteExtendedSetValidTest2() throws Exception {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			precond(true, p1.isValid());
			//----------------------
			p1.setValid(false);
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			precond(false, p1.isValid());
			//----------------------
			p1.setValid(true);
			assertEquals("\n> Error: p1.getName(); p1.setValid(true):",
						 "AAA",
						 p1.getName());
			assertEquals("\n> Error: p1.getRank(); p1.setValid(true):",
						 0.0,
						 p1.getRank(), 0.00001);
			assertTrue("\n> Error: p1.isValid(); p1.setValid(true):",
						 p1.isValid());
		}
		@Test(timeout = 1000)
		public void siteExtendedToStringTest1() {
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			precond(true, p1.isValid());
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(0.00000)"),
						 normalize(p1.toString()));
		}
		@Test(timeout = 1000)
		public void siteExtendedToStringTest1X() {
			p1.setValid(false);
			precond("AAA", p1.getName());
			precond(0.0, p1.getRank(), 0.00001);
			precond(false, p1.isValid());
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(0.00000)*"),
						 normalize(p1.toString()));
		}
		@Test(timeout = 1000)
		public void siteExtendedToStringTest2() {
			p1.addRank(7.12345678);
			precond("AAA", p1.getName());
			precond(7.12345678, p1.getRank(), 0.00001);
			precond(true, p1.isValid());
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(7.12346)"),
						 normalize(p1.toString()));
		}
		@Test(timeout = 1000)
		public void siteExtendedToStringTest2X() {
			p1.setValid(false);
			p1.addRank(7.12345678);
			precond("AAA", p1.getName());
			precond(7.12345678, p1.getRank(), 0.00001);
			precond(false, p1.isValid());
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(7.12346)*"),
						 normalize(p1.toString()));
		}
		@Test(timeout = 1000)
		public void siteExtendedToStringTest3() {
			p1.addRank(7.1234444);
			precond("AAA", p1.getName());
			precond(7.1234444, p1.getRank(), 0.00001);
			precond(true, p1.isValid());
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(7.12344)"),
						 normalize(p1.toString()));
		}
		@Test(timeout = 1000)
		public void siteExtendedToStringTest3X() {
			p1.setValid(false);
			p1.addRank(7.1234444);
			precond("AAA", p1.getName());
			precond(7.1234444, p1.getRank(), 0.00001);
			precond(false, p1.isValid());
			assertEquals("\n> Error: p1.toString():",
						 normalize("AAA(7.12344)*"),
						 normalize(p1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestWebExtended {
		private static final String[] inputData = {
			"I->C",
			"J->C",
			"A->C",
			"A->D",
			"B->C",
			"B->F",
			"D->F",
			"E->B",
			"E->H",
			"F->G",
			"F->H",
			"G->E",
			"G->H",
		};
		private static final String inputList = JUnitTestWeb.normalizeWeb("Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])");
		/*private*/ static final String[] inputSites = {
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
		};
		private static final String inputListClicks = JUnitTestWeb.normalizeWeb("Web([A(0.00000)*, B(0.67059), C(0.66764), D(0.50000), E(0.68235), F(0.91764), G(0.72941), H(1.08234), I(0.00000)*, J(0.00000)*], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])");
		public static WebExtended createWebExtended() throws Exception {
			WebExtended web1 = new WebExtended();
			try {
				for (String arc: inputData) {
					web1.addLink(arc);
				}
			} catch (Exception e) {
				// ignorar
			}
			return web1;
		}
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of WebExtended JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of WebExtended JUnit Test");
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
		public void webExtendedCtorTest1() throws Exception {
			WebExtended web2 = new WebExtended();
			assertEquals("\n> Error: web2.Ctor(); web2.toString():",
						 normalize("Web([], [])"),
						 normalize(web2.toString()));
		}
		@Test(timeout = 1000)
		public void siteExtendedCtorTest2() {
			WebExtended web2 = new WebExtended();
			assertTrue("\n> Error: WebExtended extends Web:", ((Object)web2 instanceof Web));
		}
		@Test(timeout = 1000)
		public void webExtendedAddLinkTest1() throws Exception {
			WebExtended web1 = createWebExtended();
			assertEquals("\n> Error: web1.Ctor(); web.addLink(); web1.toString():",
						 inputList,
						 JUnitTestWeb.normalizeWeb(web1.toString()));
		}
		@Test(timeout = 1000)
		public void webExtendedGetSiteTest1() throws Exception {
			WebExtended web1 = createWebExtended();
			precond(inputList,
					JUnitTestWeb.normalizeWeb(web1.toString()));
			for (String st : inputSites) {
				Site s = web1.getSite(st);
				assertTrue("\n> Error: WebExtended extends Web:", ((Object)s instanceof SiteExtended));
				SiteExtended sx = (SiteExtended)s;
				assertEquals("\n> Error: web.getSite("+st+").getName():",
							 st,
							 sx.getName());
				assertEquals("\n> Error: web.getSite("+st+").getRank():",
							 0.0,
							 sx.getRank(), 0.00001);
				assertTrue("\n> Error:  web.getSite("+st+").isValid():",
						   sx.isValid());
			}
		}
		@Test(timeout = 1000)
		public void webExtendedSwitchSiteWithNameTest1() throws Exception {
			WebExtended web1 = createWebExtended();
			precond(inputList,
					JUnitTestWeb.normalizeWeb(web1.toString()));
			for (String st : inputSites) {
				web1.switchSiteWithName(st);
				Site s = web1.getSite(st);
				assertTrue("\n> Error: WebExtended extends Web:", ((Object)s instanceof SiteExtended));
				SiteExtended sx = (SiteExtended)s;
				assertEquals("\n> Error: web.switchSiteWithName("+st+") ; web.getSite("+st+").getName():",
							 st,
							 sx.getName());
				assertEquals("\n> Error: web.switchSiteWithName("+st+") ; web.getSite("+st+").getRank():",
							 0.0,
							 sx.getRank(), 0.00001);
				assertFalse("\n> Error:  web.switchSiteWithName("+st+") ; web.getSite("+st+").isValid():",
						   sx.isValid());
			}
		}
		@Test(timeout = 1000)
		public void webExtendedClickTest1() throws Exception {
			WebExtended web1 = createWebExtended();
			web1.switchSiteWithName("A");
			web1.switchSiteWithName("I");
			web1.switchSiteWithName("J");
			precond(JUnitTestWeb.normalizeWeb("Web([A(0.00000)*, B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000)*, J(0.00000)*], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
					JUnitTestWeb.normalizeWeb(web1.toString()));
			for (String st : inputSites) {
				web1.click(st);
			}
			assertEquals("\n> Error: web1.click("+java.util.Arrays.toString(inputSites)+"); web1.toString():",
						 inputListClicks,
						 JUnitTestWeb.normalizeWeb(web1.toString()));
		}
		@Test(timeout = 1000)
		public void webExtendedClickTest2() throws Exception {
			WebExtended web1 = createWebExtended();
			precond(inputList,
					JUnitTestWeb.normalizeWeb(web1.toString()));
			web1.click("XXX");
			assertEquals("\n> Error: web1.click(XXX); web1.toString():",
						 inputList,
						 JUnitTestWeb.normalizeWeb(web1.toString()));
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestMainRank {
		@BeforeClass
		public static void beforeClass() {
			// Code executed before the first test method
			System.out.println("Start of MainRank JUnit Test");
		}
		@AfterClass
		public static void  afterClass() {
			// Code executed after the last test method
			System.out.println("End of MainRank JUnit Test");
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
		public void mainRankMainTest1() {
			//----------------------
			((java.util.Random)getMemberObject(null, Web.class, java.util.Random.class, "alea")).setSeed(1);
			//----------------------
			String salida = "";
			SysOutCapture sysOutCapture = new SysOutCapture();
			try {
				sysOutCapture.sysOutCapture();
				MainRank.main(new String[]{});
			} finally {
				salida = sysOutCapture.sysOutRelease();
			}
			assertEquals("\n> Error: MainRank.main():",
						 JUnitTestWeb.normalizeWeb("Web([A(0.00000)*, B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000)*, J(0.00000)*], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Paginas ordenadas alfabeticamente [A(0.00000)*, B(266.08527), C(265.01901), D(202.00000), E(264.34399), F(357.51901), G(289.37903), H(423.30829), I(0.00000)*, J(0.00000)*] Paginas ordenadas por rank [H(423.30829), F(357.51901), G(289.37903), B(266.08527), C(265.01901), E(264.34399), D(202.00000), A(0.00000)*, I(0.00000)*, J(0.00000)*]"),
						 JUnitTestWeb.normalizeWeb(salida));
		}
	}
	//----------------------------------------------------------------------
	//-- VistaControlador --------------------------------------------------
	//----------------------------------------------------------------------
	public static class VistaControlador implements ViewRank {
		private String userNombreFichero;
		private String userNombreSitio;
		private String vistaAreaTexto;
		private String vistaMensajeError;
		private CtrRank ctrl;
		private java.awt.event.ActionEvent createEvent;
		private java.awt.event.ActionEvent switchEvent;
		private java.awt.event.ActionEvent clickEvent;
		private java.awt.event.ActionEvent sitesByNameEvent;
		private java.awt.event.ActionEvent sitesByRankEvent;
		private java.awt.event.ActionEvent simulateEvent;
		public VistaControlador() {
		    userNombreFichero = "";
		    userNombreSitio = "";
		    vistaAreaTexto = "";
		    vistaMensajeError = "";
		    createEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+1, ViewRank.Command.CREATE.name());
		    switchEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+2, ViewRank.Command.SWITCH.name());
		    clickEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+3, ViewRank.Command.CLICK.name());
		    sitesByNameEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+4, ViewRank.Command.BYNAME.name());
		    sitesByRankEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+5, ViewRank.Command.BYRANK.name());
		    simulateEvent = new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_FIRST+6, ViewRank.Command.SIMULATE.name());
		    ctrl = new CtrRank(this);
		}
		@Override public void controler(java.awt.event.ActionListener c) { /*empty*/ }
		@Override public String getFileName() { return userNombreFichero; }
		@Override public String getSiteName() { return userNombreSitio; }
		@Override public void addOutputLine(String m) { vistaAreaTexto += " " + m; }
		@Override public void setError(String m) { vistaMensajeError = m ; }
		public void setUserNombreFichero(String x) { userNombreFichero = x; }
		public void setUserNombreSitio(String x) { userNombreSitio = x; }
		public String getVistaAreaTexto() { return vistaAreaTexto; }
		public String getVistaMensajeError() { return vistaMensajeError; }
		public void pulsaCreate() { ctrl.actionPerformed(createEvent); }
		public void pulsaSwitch() { ctrl.actionPerformed(switchEvent); }
		public void pulsaClick() { ctrl.actionPerformed(clickEvent); }
		public void pulsaSitesByName() { ctrl.actionPerformed(sitesByNameEvent); }
		public void pulsaSitesByRank() { ctrl.actionPerformed(sitesByRankEvent); }
		public void pulsaSimulate() { ctrl.actionPerformed(simulateEvent); }
	}
	//----------------------------------------------------------------------
	//--JUnitTest-----------------------------------------------------------
	//----------------------------------------------------------------------
	public static class JUnitTestCtrRank {
		private VistaControlador vc1;
		private static final String[] inputData = {
			"I->C",
			"J->C",
			"A->C",
			"A->D",
			"B->C",
			"B->F",
			"D->F",
			"E->B",
			"E->H",
			"F->G",
			"F->H",
			"G->E",
			"G->H",
		};
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
			vc1.setUserNombreSitio("");
			assertEquals("\n> Error: Inicio: Area-de-Texto",
						 normalize(""),
						 normalize(vc1.getVistaAreaTexto()));
			assertTrue("\n> Error: Inicio: Estado-Error",
						 vc1.getVistaMensajeError().isEmpty());
		}
		@Test(timeout = 1000)
		public void vistaControladorCreateTest1() {
			vc1.setUserNombreFichero("no-existe.txt");
			vc1.setUserNombreSitio("");
			vc1.pulsaCreate();
			assertEquals("\n> Error: Create Web(file-not-found) ; Area-de-Texto",
						 normalize(""),
						 normalize(vc1.getVistaAreaTexto()));
			assertFalse("\n> Error: Create Web(file-not-found) ; Estado-Error",
						 vc1.getVistaMensajeError().isEmpty());
		}
		@Test(timeout = 1000)
		public void vistaControladorCreateTest2() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("");
				vc1.pulsaCreate();
				assertEquals("\n> Error: Create Web(links.txt): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorClickTest1() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("D");
				vc1.pulsaCreate();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaClick();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Click(D): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Click in D(0.50000)"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; Click(D): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorClickTest2() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("XXX");
				vc1.pulsaCreate();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaClick();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Click(XXX): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertFalse("\n> Error: Create Web(links.txt) ; Click(XXX): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorSwitchTest1() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("D");
				vc1.pulsaCreate();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSwitch();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Switch(D): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Switch D(0.00000)*"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; Switch(D): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorSwitchTest2() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("XXX");
				vc1.pulsaCreate();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSwitch();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Switch(XXX): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertFalse("\n> Error: Create Web(links.txt) ; Switch(XXX): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorSitesByNameTest1() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("");
				vc1.pulsaCreate();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSitesByName();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; SitesByName(): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Sites by Names [A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)]"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; SitesByName(): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorSitesByNameTest2() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("D");
				vc1.pulsaCreate();
				vc1.pulsaClick();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Click in D(0.50000)"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSitesByName();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Click(D) ; SitesByName(): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Click in D(0.50000) Sites by Names [A(0.00000), B(0.00392), C(0.00098), D(0.50000), E(0.01569), F(0.25098), G(0.06274), H(0.08235), I(0.00000), J(0.00000)]"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; Click(D) ; SitesByName(): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorSitesByRankTest1() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("");
				vc1.pulsaCreate();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E])"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSitesByRank();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; SitesByRank(): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Sites by Rank [A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)]"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; SitesByRank(): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorSitesByRankTest2() throws Exception {
			try {
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("D");
				vc1.pulsaCreate();
				vc1.pulsaClick();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Click in D(0.50000)"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSitesByRank();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Click(D) ; SitesByRank(): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Click in D(0.50000) Sites by Rank [D(0.50000), F(0.25098), H(0.08235), G(0.06274), E(0.01569), B(0.00392), C(0.00098), A(0.00000), I(0.00000), J(0.00000)]"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; Click(D) ; SitesByRank(): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		@Test(timeout = 1000)
		public void vistaControladorSimulateTest1() throws Exception {
			try {
				//----------------------
				((java.util.Random)getMemberObject(null, Web.class, java.util.Random.class, "alea")).setSeed(1);
				//----------------------
				createFile("inputdata.txt", inputData);
				//------------------
				vc1.setUserNombreFichero("inputdata.txt");
				vc1.setUserNombreSitio("A");
				vc1.pulsaCreate();
				vc1.pulsaSwitch();
				precond(JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Switch A(0.00000)*"),
						JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				precond(true,
						vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSimulate();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Switch(A) ; Simulate() : Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Switch A(0.00000)* Simulate 1000 click"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; Switch(A) ; Simulate() : Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
				//------------------
				vc1.pulsaSitesByName();
				//------------------
				assertEquals("\n> Error: Create Web(links.txt) ; Switch(A) ; Simulate() ; SitesByName(): Area-de-Texto",
							 JUnitTestWeb.normalizeWeb("Create web Web([A(0.00000), B(0.00000), C(0.00000), D(0.00000), E(0.00000), F(0.00000), G(0.00000), H(0.00000), I(0.00000), J(0.00000)], [B->C, F->G, G->H, A->C, D->F, F->H, A->D, E->H, B->F, J->C, I->C, E->B, G->E]) Switch A(0.00000)* Simulate 1000 click Sites by Names [A(0.00000)*, B(60.46249), C(115.11502), D(57.00000), E(67.85069), F(91.61502), G(77.40357), H(103.71674), I(54.00000), J(63.00000)]"),
							 JUnitTestWeb.normalizeWeb(vc1.getVistaAreaTexto()));
				assertTrue("\n> Error: Create Web(links.txt) ; Switch(A) ; Simulate() ; SitesByName(): Estado-Error",
						   vc1.getVistaMensajeError().isEmpty());
			} finally {
				deleteFile("inputdata.txt");
			}
		}
		//------------------------------------------------------------------
	}
	//----------------------------------------------------------------------
	//--JUnitTestSuite------------------------------------------------------
	//----------------------------------------------------------------------
	@RunWith(Suite.class)
	@Suite.SuiteClasses({ JUnitTestLink.class ,
				JUnitTestSite.class ,
				JUnitTestWeb.class,
				JUnitTestSiteExtended.class ,
				JUnitTestWebExtended.class ,
				JUnitTestMainRank.class ,
				JUnitTestCtrRank.class
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
