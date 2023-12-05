/*     */ package net.agsh.towerdefense.gui;
/*     */ 
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import net.agsh.towerdefense.Enemy;
/*     */ import net.agsh.towerdefense.Game;
/*     */ import net.agsh.towerdefense.Point2D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends MouseAdapter
/*     */ {
/*     */   public void mouseClicked(MouseEvent evt) {
/* 183 */     Game g = Game.getInstance();
/*     */     
/* 185 */     if (g.getMap() != null && g.getCurrentRound() != null && g.getCurrentRound().getEnemiesAlive() != null) {
/*     */       
/* 187 */       Point2D mapPointPosition = new Point2D(evt.getX() / GameWindow.this.mapPanel.getScale(), evt.getY() / GameWindow.this.mapPanel.getScale());
/*     */       
/* 189 */       Enemy closest = null;
/* 190 */       float closestDistance = Float.MAX_VALUE;
/* 191 */       for (Enemy enemy : Game.getInstance().getCurrentRound().getEnemiesAlive()) {
/* 192 */         float distance = enemy.getPosition().distance(mapPointPosition);
/* 193 */         if (distance < closestDistance && distance < enemy.getRadius()) {
/* 194 */           closest = enemy;
/* 195 */           closestDistance = distance;
/*     */         } 
/*     */       } 
/*     */       
/* 199 */       if (closest != null)
/* 200 */         GameWindow.this.mapPanel.setSelectedEnemy(closest); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/gui/GameWindow$6.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */