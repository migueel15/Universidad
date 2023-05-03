package gui;
import gui.ViewRank;

import rank.Web;
import rank.WebExtended;

import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class CtrRank implements ActionListener {
  private ViewRank vista;
  private WebExtended modelo;

  public CtrRank(ViewRank vista) {
    this.vista = vista;
  }

  private void readDataFile(String file) throws IOException {
    try (Scanner sc = new Scanner(Path.of(file))) {
      while (sc.hasNextLine()) {
        String linea = sc.nextLine();
        modelo.addLink(linea);
      }
    }
  }
  private void handleCreateWeb() {
    modelo = new WebExtended();
    String file = vista.getFileName();
    try {
      readDataFile(file);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    vista.addOutputLine("Create web");
    vista.addOutputLine(modelo.toString());
  }
  private void handleSwitchSite(){
    String site = vista.getSiteName();
    modelo.switchSiteWithName(site);
    vista.addOutputLine("Switch " + modelo.getSite(site).toString());
  }
  private void handleClick(){
    String site = vista.getSiteName();
    modelo.click(site);
    vista.addOutputLine("Click in " + modelo.getSite(site).toString());
  }
  private void handleByName(){
    vista.addOutputLine("Sites by Names");
    vista.addOutputLine(modelo.getSitesByName().toString());
  }
  private void handleByRank(){
    vista.addOutputLine("Sites by Rank");
    vista.addOutputLine(modelo.getSitesByRank().toString());
  }
  private void handleSimulate(){
    modelo.simulateClick(1000);
    vista.addOutputLine("Simulate 1000 click");
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    ViewRank.Command command = ViewRank.Command.valueOf(e.getActionCommand());
    try{
      switch (command){
        case CREATE:
          handleCreateWeb();
          break;
        case SWITCH:
          handleSwitchSite();
          break;
        case CLICK:
          handleClick();
          break;
        case BYNAME:
          handleByName();
          break;
        case BYRANK:
          handleByRank();
          break;
        case SIMULATE:
          handleSimulate();
          break;
      }
    }catch (Exception error){
      vista.setError(error.getMessage());
    }
  }
}
