package primos;

import java.awt.event.*;

public class Controlador implements ActionListener {
  private Panel panel;
  private Worker workerTwin;
  private Worker workerCousin;
  private Worker workerSexy;
  private ControladorBarra ctrBarra1;
  private ControladorBarra ctrBarra2;
  private ControladorBarra ctrBarra3;

  public Controlador(Panel panel, ControladorBarra ctrBarra1,
                     ControladorBarra ctrBarra2, ControladorBarra ctrBarra3) {
    this.panel = panel;
    this.ctrBarra1 = ctrBarra1;
    this.ctrBarra2 = ctrBarra2;
    this.ctrBarra3 = ctrBarra3;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("numero1")) {
      workerTwin = new Worker(panel.numero1(), panel, 2);
      workerTwin.addPropertyChangeListener(ctrBarra1);
      workerTwin.execute();
      panel.mensajeTwin("Calculando primos twin...");
    } else if (e.getActionCommand().equals("numero2")) {
      workerCousin = new Worker(panel.numero2(), panel, 4);
      workerCousin.addPropertyChangeListener(ctrBarra2);
      workerCousin.execute();
      panel.mensajeCousin("Calculando primos cousin...");
    } else if (e.getActionCommand().equals("numero3")) {
      workerSexy = new Worker(panel.numero3(), panel, 6);
      workerSexy.addPropertyChangeListener(ctrBarra3);
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
      panel.progreso1(0);
      panel.limpiaAreaCousin();
      panel.progreso2(0);
      panel.limpiaAreaSexy();
      panel.progreso3(0);
      panel.mensaje("Fin de la ejecución");
    }
  }
}
