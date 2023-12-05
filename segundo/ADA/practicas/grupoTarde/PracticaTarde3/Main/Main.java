package Main;

import net.agsh.towerdefense.Config;
import net.agsh.towerdefense.Game;
import net.agsh.towerdefense.Random;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
  public static void main(String[] args) throws IOException {
    Game g = Game.getInstance();
    String[] var2 = args;
    int var3 = args.length;

    for(int var4 = 0; var4 < var3; ++var4) {
      String arg = var2[var4];
      String[] split = arg.split("=");
      if (split.length == 2) {
        String key = split[0];
        String value = split[1];
        Config.Parameter parameter = Config.Parameter.valueOf(key.toUpperCase());
        g.setParam(parameter, Float.parseFloat(value));
      } else if (split.length == 1 && (split[0].equals("--version") || split[0].equals("-v"))) {
        System.out.println("Version: " + g.getVersion());
        System.exit(0);
      }
    }

    ArrayList<ArrayList<Float>> listaRes = new ArrayList<>();
    Random r = new Random();
    /*
      g.setParam(Config.Parameter.WEIGHT_DISPERSION, r.nextInt(0,60));
      g.setParam(Config.Parameter.WEIGHT_DAMAGE, r.nextInt(30,100));
      g.setParam(Config.Parameter.WEIGHT_COOLDOWN, r.nextInt(0,10));
      g.setParam(Config.Parameter.WEIGHT_RANGE, r.nextInt(0,30));
    */
    g.setParam(Config.Parameter.WEIGHT_DISPERSION,34);
    g.setParam(Config.Parameter.WEIGHT_DAMAGE, 31);
    g.setParam(Config.Parameter.WEIGHT_COOLDOWN, 0);
    g.setParam(Config.Parameter.WEIGHT_RANGE, 17);

      g.init(0L);
      g.play();
      System.out.println("Game over! Score: " + g.getScore());
      ArrayList<Float> actual = new ArrayList<>();
      actual.add(g.getParam(Config.Parameter.WEIGHT_DISPERSION));
      actual.add(g.getParam(Config.Parameter.WEIGHT_DAMAGE));
      actual.add(g.getParam(Config.Parameter.WEIGHT_COOLDOWN));
      actual.add(g.getParam(Config.Parameter.WEIGHT_RANGE));
      actual.add((float)g.getScore());

      if(g.getScore() < 30){
        FileWriter fw = new FileWriter("result.txt", true);
        fw.write(  actual + "\n");
        fw.close();
      }
  }
}
