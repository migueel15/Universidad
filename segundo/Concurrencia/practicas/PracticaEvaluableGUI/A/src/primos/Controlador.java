package primos;

import java.awt.event.*;

public class Controlador implements ActionListener {
	private Panel panel;
	private Worker workerTwin;
	private Worker workerCousin;
	private Worker workerSexy;

	public Controlador(Panel panel) {
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("numero1")) {
			workerTwin = new Worker(panel.numero1(), panel, 2);
			workerTwin.execute();
			panel.mensajeTwin("Calculando primos twin...");
		} else if (e.getActionCommand().equals("numero2")) {
			workerCousin = new Worker(panel.numero2(), panel, 4);
			workerCousin.execute();
			panel.mensajeCousin("Calculando primos cousin...");
		} else if (e.getActionCommand().equals("numero3")) {
			workerSexy = new Worker(panel.numero3(), panel, 6);
			workerSexy.execute();
			panel.mensajeSexy("Calculando primos sexy...");
		} else if (e.getActionCommand().equals("fin")) {
			if (workerTwin != null && !workerTwin.isDone()) {
				workerTwin.cancel(true);
			}
			if (workerCousin != null && !workerCousin.isDone()) {
				workerCousin.cancel(true);
			}
			if (workerSexy != null && !workerSexy.isDone()) {
				workerSexy.cancel(true);
			}
			panel.limpiaAreaTwin();
			panel.limpiaAreaCousin();
			panel.limpiaAreaSexy();
			panel.mensaje("Fin de la ejecución");
		}
	}
}
