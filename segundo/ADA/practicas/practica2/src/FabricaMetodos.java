public class FabricaMetodos {

	public static Metodo crear(Metodo.Orden o) {
		Metodo m = null;
		switch (o) {
		case CTE:
			m = new OrdenConstanteIter();
			break;
		case LOGN:
			m = new OrdenLogIter();
			break;
			/*
		case N:
			m = new OrdenLinealRec();
			break;
			*/

		case NLOGN:
			m = new OrdenNLogIter();
			break;
		case N2:
			m = new OrdenCuadradoIter();
			break;
		case N3:
			m = new OrdenCubicoIter();
			break;
			/*
		case EXP2:
			m = new OrdenExponencial2Rec();
			break;

			 */
		}
		return m;
	}
}
