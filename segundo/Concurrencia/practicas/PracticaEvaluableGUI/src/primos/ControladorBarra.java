package primos;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class ControladorBarra implements PropertyChangeListener {

  private Panel panel;

  public ControladorBarra(Panel panel) { this.panel = panel; }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    // get new value
    if ("progress".equals(evt.getPropertyName())) {
      SwingWorker<?, ?> source = (SwingWorker<?, ?>)evt.getSource();
      int workerId = ((Worker)source).getType();

      if (workerId == 2) {
        panel.progreso1((Integer)evt.getNewValue());
      } else if (workerId == 4) {
        panel.progreso2((Integer)evt.getNewValue());
      } else if (workerId == 6) {
        panel.progreso3((Integer)evt.getNewValue());
      }
    }
  }
}
