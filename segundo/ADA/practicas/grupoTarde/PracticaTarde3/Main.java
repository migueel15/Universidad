import net.agsh.towerdefense.Game;
import net.agsh.towerdefense.Config.Parameter;
import net.agsh.towerdefense.Random;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
  public Main() {
  }

  public static void main(String[] args) {
    Game g = Game.getInstance();
    Random r = new Random();

    g.setParam(Parameter.W_DAM,
        Math.round(r.nextFloat(80.0f,120.0f) * 1000.0f)/1000.0f);
    g.setParam(Parameter.W_CODW,
        Math.round(r.nextFloat(-1.0f,0.0f)*1000.0f)/1000.0f);
    g.setParam(Parameter.W_DISP,
        Math.round(r.nextFloat(-50.0f,-10.0f) *1000.0f)/1000.0f);
    g.setParam(Parameter.W_RAN,
        Math.round(r.nextFloat(0.0f,0.0f)*1000.0f)/1000.0f);



    for(String arg : args) {
      String[] split = arg.split("=");
      if (split.length == 2) {
        String key = split[0];
        String value = split[1];
        Parameter parameter = Parameter.valueOf(key.toUpperCase());
        g.setParam(parameter, Float.parseFloat(value));
      } else if (split.length == 1 && (split[0].equals("--version") || split[0].equals("-v"))) {
        System.out.println("Version: " + g.getVersion());
        System.exit(0);
      }
    }

    g.init(0L);
    g.play();

      try{
        String linea =
            "SCORE: " + g.getScore() +
                " -> DAM:" + g.getParam(Parameter.W_DAM) + ";CODW:" + g.getParam(Parameter.W_CODW) + ";DISP:" + g.getParam(Parameter.W_DISP) +
                ";RAN:" + g.getParam(Parameter.W_RAN) +
                "\n";
        if(g.getScore() < 21){
          FileWriter fw = new FileWriter("resultados.txt", true);
          fw.write(linea);
          fw.close();
        }
        System.out.println(linea);
      }catch (IOException e){
    }
  }
}
