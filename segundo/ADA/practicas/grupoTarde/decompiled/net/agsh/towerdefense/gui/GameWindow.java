/*     */ package net.agsh.towerdefense.gui;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import net.agsh.towerdefense.Enemy;
/*     */ import net.agsh.towerdefense.Entity;
/*     */ import net.agsh.towerdefense.Game;
/*     */ 
/*     */ public class GameWindow {
/*     */   JFrame frame;
/*     */   
/*     */   public GameWindow() {
/*  15 */     createWindow();
/*     */ 
/*     */     
/*  18 */     (new Thread(() -> {
/*     */           Game g = Game.getInstance();
/*     */           
/*     */           float refreshRate = g.getParam(Config.Parameter.GUI_REFRESH_RATE);
/*     */           long refreshPeriod = (long)(1000.0F / refreshRate);
/*     */           while (!g.gameOver()) {
/*     */             try {
/*     */               Thread.sleep(refreshPeriod);
/*  26 */             } catch (InterruptedException e) {
/*     */               e.printStackTrace();
/*     */             } 
/*     */             update();
/*     */           } 
/*  31 */         })).start();
/*     */   } MapPanel mapPanel;
/*     */   private void createWindow() {
/*  34 */     final Game g = Game.getInstance();
/*     */     
/*  36 */     this.frame = new JFrame("Tower Defense");
/*  37 */     this.frame.setDefaultCloseOperation(3);
/*  38 */     float aspectRatio = g.getParam(Config.Parameter.MAP_SIZE_X) / g.getParam(Config.Parameter.MAP_SIZE_Y);
/*  39 */     int width = 800;
/*  40 */     int height = (int)(width / aspectRatio);
/*  41 */     this.frame.setSize(width, height + 80 + 30);
/*  42 */     this.frame.setLocationRelativeTo((Component)null);
/*  43 */     this.frame.setResizable(true);
/*     */     
/*  45 */     JMenuBar menuBar = new JMenuBar();
/*  46 */     JMenu archivoMenu = new JMenu("Archivo");
/*  47 */     JMenuItem abrirMenuItem = new JMenuItem("Abrir");
/*  48 */     JMenuItem guardarMenuItem = new JMenuItem("Guardar");
/*  49 */     JMenuItem salirMenuItem = new JMenuItem("Salir");
/*     */     
/*  51 */     archivoMenu.add(abrirMenuItem);
/*  52 */     archivoMenu.add(guardarMenuItem);
/*  53 */     archivoMenu.addSeparator();
/*  54 */     archivoMenu.add(salirMenuItem);
/*  55 */     menuBar.add(archivoMenu);
/*     */     
/*  57 */     this.frame.setJMenuBar(menuBar);
/*     */     
/*  59 */     JPanel buttonPanel = new JPanel(new FlowLayout(0));
/*     */     
/*  61 */     final JButton btnPlayPause = new JButton();
/*  62 */     JButton boton2 = new JButton();
/*     */     
/*  64 */     btnPlayPause.setIcon(new ImageIcon("assets/pause.png"));
/*  65 */     btnPlayPause.setFocusPainted(false);
/*     */     
/*  67 */     btnPlayPause.setPreferredSize(new Dimension(30, 30));
/*     */     
/*  69 */     String[] items = { "x0.5", "x1", "x2", "x4", "x8" };
/*  70 */     JComboBox<String> timeScaleCombo = new JComboBox<>(items);
/*  71 */     timeScaleCombo.setSelectedIndex(1);
/*  72 */     timeScaleCombo.setPreferredSize(new Dimension(50, 30));
/*     */     
/*  74 */     String[] nodeValues = new String[(int)g.getParam(Config.Parameter.MAP_NODE_VALUES) + 1];
/*  75 */     nodeValues[0] = "None";
/*  76 */     for (int i = 1; i < nodeValues.length; i++) {
/*  77 */       nodeValues[i] = "" + i - 1;
/*     */     }
/*  79 */     JComboBox<String> nodeValuesCombo = new JComboBox<>(nodeValues);
/*  80 */     nodeValuesCombo.setSelectedIndex(0);
/*  81 */     nodeValuesCombo.setPreferredSize(new Dimension(50, 30));
/*     */     
/*  83 */     JCheckBox showGridCheckBox = new JCheckBox("Grid");
/*  84 */     showGridCheckBox.setPreferredSize(new Dimension(60, 30));
/*     */     
/*  86 */     JCheckBox showRadiusCheckBox = new JCheckBox("Radius");
/*  87 */     showRadiusCheckBox.setPreferredSize(new Dimension(70, 30));
/*     */ 
/*     */     
/*  90 */     buttonPanel.add(btnPlayPause);
/*     */     
/*  92 */     buttonPanel.add(timeScaleCombo);
/*  93 */     buttonPanel.add(new JLabel("Time scale"));
/*  94 */     buttonPanel.add(showGridCheckBox);
/*  95 */     buttonPanel.add(showRadiusCheckBox);
/*  96 */     buttonPanel.add(nodeValuesCombo);
/*  97 */     buttonPanel.add(new JLabel("Node values"));
/*     */ 
/*     */     
/* 100 */     this.frame.add(buttonPanel, "North");
/*     */     
/* 102 */     final JLabel statusBar = new JLabel(" ");
/*     */     
/* 104 */     this.frame.add(statusBar, "South");
/*     */     
/* 106 */     this.mapPanel = new MapPanel();
/* 107 */     this.frame.add(this.mapPanel);
/*     */     
/* 109 */     abrirMenuItem.addActionListener(new ActionListener(this)
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {}
/*     */         });
/*     */ 
/*     */     
/* 115 */     guardarMenuItem.addActionListener(new ActionListener(this)
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {}
/*     */         });
/*     */ 
/*     */     
/* 121 */     salirMenuItem.addActionListener(new ActionListener(this) {
/*     */           public void actionPerformed(ActionEvent e) {
/* 123 */             System.exit(0);
/*     */           }
/*     */         });
/*     */     
/* 127 */     btnPlayPause.addActionListener(new ActionListener(this) {
/*     */           public void actionPerformed(ActionEvent e) {
/* 129 */             if (g.isPaused()) {
/* 130 */               g.resume();
/* 131 */               btnPlayPause.setIcon(new ImageIcon("assets/pause.png"));
/*     */             } else {
/* 133 */               g.pause();
/* 134 */               btnPlayPause.setIcon(new ImageIcon("assets/play.png"));
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 139 */     this.mapPanel.addMouseMotionListener(new MouseMotionListener()
/*     */         {
/*     */           public void mouseDragged(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void mouseMoved(MouseEvent e) {
/* 146 */             Game g = Game.getInstance();
/*     */             
/* 148 */             if (g.getMap() != null && g.getMap().getObstacles() != null && g.getMap().getTowers() != null && g
/* 149 */               .getCurrentRound() != null) {
/*     */               
/* 151 */               ArrayList<Entity> entities = new ArrayList<>(Game.getInstance().getMap().getObstacles());
/* 152 */               entities.addAll(Game.getInstance().getMap().getTowers());
/* 153 */               if (g.getCurrentRound().getEnemiesAlive() != null) {
/* 154 */                 entities.addAll(Game.getInstance().getCurrentRound().getEnemiesAlive());
/*     */               }
/*     */               
/* 157 */               Point2D mapPointPosition = new Point2D(e.getX() / GameWindow.this.mapPanel.getScale(), e.getY() / GameWindow.this.mapPanel.getScale());
/*     */               
/* 159 */               Entity closest = null;
/* 160 */               float closestDistance = Float.MAX_VALUE;
/* 161 */               for (Entity entity : entities) {
/* 162 */                 float distance = entity.getPosition().distance(mapPointPosition);
/* 163 */                 if (distance < closestDistance && distance < entity.getRadius()) {
/* 164 */                   closest = entity;
/* 165 */                   closestDistance = distance;
/*     */                 } 
/*     */               } 
/*     */               
/* 169 */               String entityDescription = "";
/* 170 */               if (closest != null) {
/* 171 */                 entityDescription = closest.toString();
/*     */               }
/*     */ 
/*     */               
/* 175 */               statusBar.setText("X: " + (int)(e.getX() / GameWindow.this.mapPanel.getScale()) + " Y: " + 
/* 176 */                   (int)(e.getY() / GameWindow.this.mapPanel.getScale()) + " " + entityDescription);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 181 */     this.mapPanel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent evt) {
/* 183 */             Game g = Game.getInstance();
/*     */             
/* 185 */             if (g.getMap() != null && g.getCurrentRound() != null && g.getCurrentRound().getEnemiesAlive() != null) {
/*     */               
/* 187 */               Point2D mapPointPosition = new Point2D(evt.getX() / GameWindow.this.mapPanel.getScale(), evt.getY() / GameWindow.this.mapPanel.getScale());
/*     */               
/* 189 */               Enemy closest = null;
/* 190 */               float closestDistance = Float.MAX_VALUE;
/* 191 */               for (Enemy enemy : Game.getInstance().getCurrentRound().getEnemiesAlive()) {
/* 192 */                 float distance = enemy.getPosition().distance(mapPointPosition);
/* 193 */                 if (distance < closestDistance && distance < enemy.getRadius()) {
/* 194 */                   closest = enemy;
/* 195 */                   closestDistance = distance;
/*     */                 } 
/*     */               } 
/*     */               
/* 199 */               if (closest != null) {
/* 200 */                 GameWindow.this.mapPanel.setSelectedEnemy(closest);
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 206 */     timeScaleCombo.addActionListener(new ActionListener(this) {
/*     */           public void actionPerformed(ActionEvent e) {
/* 208 */             JComboBox cb = (JComboBox)e.getSource();
/* 209 */             String timeScale = (String)cb.getSelectedItem();
/* 210 */             float timeScaleFactor = Float.parseFloat(timeScale.substring(1));
/* 211 */             Game.getInstance().setTimeScale(timeScaleFactor);
/*     */           }
/*     */         });
/*     */     
/* 215 */     nodeValuesCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 217 */             JComboBox cb = (JComboBox)e.getSource();
/* 218 */             String nodeValues = (String)cb.getSelectedItem();
/* 219 */             if (nodeValues.equals("None")) {
/* 220 */               GameWindow.this.mapPanel.setNodeValuesIndex(-1);
/*     */             } else {
/* 222 */               GameWindow.this.mapPanel.setNodeValuesIndex(Integer.parseInt(nodeValues));
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 227 */     showGridCheckBox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 229 */             JCheckBox cb = (JCheckBox)e.getSource();
/* 230 */             GameWindow.this.mapPanel.setShowGrid(cb.isSelected());
/*     */           }
/*     */         });
/*     */     
/* 234 */     showRadiusCheckBox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 236 */             JCheckBox cb = (JCheckBox)e.getSource();
/* 237 */             GameWindow.this.mapPanel.setShowRadius(cb.isSelected());
/*     */           }
/*     */         });
/*     */     
/* 241 */     this.frame.setVisible(true);
/*     */   }
/*     */   
/*     */   public void update() {
/* 245 */     this.mapPanel.repaint();
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 249 */     this.frame.dispose();
/*     */   }
/*     */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/gui/GameWindow.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */