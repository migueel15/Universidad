/*     */ package net.agsh.towerdefense.gui;
/*     */ 
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JLabel;
/*     */ import net.agsh.towerdefense.Entity;
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
/*     */ class null
/*     */   implements MouseMotionListener
/*     */ {
/*     */   public void mouseDragged(MouseEvent e) {}
/*     */   
/*     */   public void mouseMoved(MouseEvent e) {
/* 146 */     Game g = Game.getInstance();
/*     */     
/* 148 */     if (g.getMap() != null && g.getMap().getObstacles() != null && g.getMap().getTowers() != null && g
/* 149 */       .getCurrentRound() != null) {
/*     */       
/* 151 */       ArrayList<Entity> entities = new ArrayList<>(Game.getInstance().getMap().getObstacles());
/* 152 */       entities.addAll(Game.getInstance().getMap().getTowers());
/* 153 */       if (g.getCurrentRound().getEnemiesAlive() != null) {
/* 154 */         entities.addAll(Game.getInstance().getCurrentRound().getEnemiesAlive());
/*     */       }
/*     */       
/* 157 */       Point2D mapPointPosition = new Point2D(e.getX() / GameWindow.this.mapPanel.getScale(), e.getY() / GameWindow.this.mapPanel.getScale());
/*     */       
/* 159 */       Entity closest = null;
/* 160 */       float closestDistance = Float.MAX_VALUE;
/* 161 */       for (Entity entity : entities) {
/* 162 */         float distance = entity.getPosition().distance(mapPointPosition);
/* 163 */         if (distance < closestDistance && distance < entity.getRadius()) {
/* 164 */           closest = entity;
/* 165 */           closestDistance = distance;
/*     */         } 
/*     */       } 
/*     */       
/* 169 */       String entityDescription = "";
/* 170 */       if (closest != null) {
/* 171 */         entityDescription = closest.toString();
/*     */       }
/*     */ 
/*     */       
/* 175 */       statusBar.setText("X: " + (int)(e.getX() / GameWindow.this.mapPanel.getScale()) + " Y: " + 
/* 176 */           (int)(e.getY() / GameWindow.this.mapPanel.getScale()) + " " + entityDescription);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/gui/GameWindow$5.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */