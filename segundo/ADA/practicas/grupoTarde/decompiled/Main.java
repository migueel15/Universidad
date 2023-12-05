/*    */ import net.agsh.towerdefense.Config;
/*    */ import net.agsh.towerdefense.Game;
/*    */ 
/*    */ public class Main {
/*    */   public static void main(String[] args) {
/*  6 */     Game g = Game.getInstance();
/*    */ 
/*    */     
/*  9 */     for (String arg : args) {
/* 10 */       String[] split = arg.split("=");
/* 11 */       if (split.length == 2) {
/* 12 */         String key = split[0];
/* 13 */         String value = split[1];
/* 14 */         Config.Parameter parameter = Config.Parameter.valueOf(key.toUpperCase());
/* 15 */         g.setParam(parameter, Float.parseFloat(value));
/* 16 */       } else if (split.length == 1 && (
/* 17 */         split[0].equals("--version") || split[0].equals("-v"))) {
/* 18 */         System.out.println("Version: " + g.getVersion());
/* 19 */         System.exit(0);
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 24 */     g.init(0L);
/* 25 */     g.play();
/* 26 */     System.out.println("Game over! Score: " + g.getScore());
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/Main.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */