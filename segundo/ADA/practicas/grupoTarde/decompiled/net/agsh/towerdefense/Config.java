/*    */ package net.agsh.towerdefense;
/*    */ 
/*    */ public class Config
/*    */ {
/*    */   public enum Parameter {
/*  6 */     GAME_STEP_SIZE, OBSTACLE_RADIUS_MIN, OBSTACLE_RADIUS_MAX, OBSTACLE_DENSITY, TOWER_RADIUS_MIN, TOWER_RADIUS_MAX,
/*  7 */     TOWER_RANGE_MIN, TOWER_RANGE_MAX, TOWER_DAMAGE_MIN, TOWER_DAMAGE_MAX, TOWER_COOLDOWN_MIN, TOWER_COOLDOWN_MAX,
/*  8 */     TOWER_DISPERSION_MIN, TOWER_DISPERSION_MAX, TOWER_COST_MIN, TOWER_COST_MAX, ENEMY_RADIUS_MIN, ENEMY_RADIUS_MAX,
/*  9 */     ENEMY_SPEED_MIN, ENEMY_SPEED_MAX, MAP_SIZE_X, MAP_SIZE_Y, MAP_GRID_SPACE, TOWER_MIN_MONEY_FACTOR,
/* 10 */     TOWER_MAX_MONEY_FACTOR, TOWER_DENSITY, ENEMY_HEALTH_MIN, ENEMY_HEALTH_MAX, ENEMY_SPAWN_INTERVAL, ENEMY_DENSITY,
/* 11 */     ENEMY_DENSITY_ROUND_FACTOR, GAME_MAX_ROUNDS, DEBUG_PRINT_TOWER_DETAILS, GUI, GUI_REFRESH_RATE,
/* 12 */     MAP_ENDING_POINTS, MAP_STARTING_POINTS, MAP_STARTING_POINTS_SEPARATION_FACTOR, MAP_ENDING_POINTS_SEPARATION_FACTOR, MAP_NODE_VALUES, OBSTACLE_MAX_TRIES;
/*    */   }
/*    */   
/* 15 */   float[] values = new float[(Parameter.values()).length];
/*    */ 
/*    */   
/*    */   public Config() {
/* 19 */     this.values[Parameter.GAME_STEP_SIZE.ordinal()] = 20.0F;
/* 20 */     this.values[Parameter.OBSTACLE_RADIUS_MIN.ordinal()] = 20.0F;
/* 21 */     this.values[Parameter.OBSTACLE_RADIUS_MAX.ordinal()] = 50.0F;
/* 22 */     this.values[Parameter.OBSTACLE_DENSITY.ordinal()] = 0.01F;
/* 23 */     this.values[Parameter.TOWER_RADIUS_MIN.ordinal()] = 20.0F;
/* 24 */     this.values[Parameter.TOWER_RADIUS_MAX.ordinal()] = 50.0F;
/*    */     
/* 26 */     this.values[Parameter.TOWER_RANGE_MAX.ordinal()] = 2.0F * this.values[Parameter.TOWER_RADIUS_MAX.ordinal()];
/* 27 */     this.values[Parameter.TOWER_DAMAGE_MIN.ordinal()] = 1.0F;
/* 28 */     this.values[Parameter.TOWER_DAMAGE_MAX.ordinal()] = 50.0F;
/* 29 */     this.values[Parameter.TOWER_COOLDOWN_MIN.ordinal()] = 100.0F;
/* 30 */     this.values[Parameter.TOWER_COOLDOWN_MAX.ordinal()] = 2000.0F;
/* 31 */     this.values[Parameter.TOWER_DISPERSION_MIN.ordinal()] = 0.01F;
/* 32 */     this.values[Parameter.TOWER_DISPERSION_MAX.ordinal()] = 30.0F;
/* 33 */     this.values[Parameter.TOWER_COST_MIN.ordinal()] = 5.0F;
/* 34 */     this.values[Parameter.TOWER_COST_MAX.ordinal()] = 20.0F;
/* 35 */     this.values[Parameter.ENEMY_RADIUS_MIN.ordinal()] = 10.0F;
/* 36 */     this.values[Parameter.ENEMY_RADIUS_MAX.ordinal()] = 10.0F;
/* 37 */     this.values[Parameter.ENEMY_SPEED_MIN.ordinal()] = 30.0F;
/* 38 */     this.values[Parameter.ENEMY_SPEED_MAX.ordinal()] = 60.0F;
/* 39 */     this.values[Parameter.MAP_SIZE_X.ordinal()] = 1200.0F;
/* 40 */     this.values[Parameter.MAP_SIZE_Y.ordinal()] = 600.0F;
/* 41 */     this.values[Parameter.MAP_GRID_SPACE.ordinal()] = 20.0F;
/* 42 */     this.values[Parameter.TOWER_MIN_MONEY_FACTOR.ordinal()] = 0.4F;
/* 43 */     this.values[Parameter.TOWER_MAX_MONEY_FACTOR.ordinal()] = 1.1F;
/* 44 */     this.values[Parameter.TOWER_DENSITY.ordinal()] = 0.01F;
/* 45 */     this.values[Parameter.ENEMY_HEALTH_MIN.ordinal()] = 100.0F;
/* 46 */     this.values[Parameter.ENEMY_HEALTH_MAX.ordinal()] = 500.0F;
/* 47 */     this.values[Parameter.ENEMY_SPAWN_INTERVAL.ordinal()] = 1000.0F;
/* 48 */     this.values[Parameter.ENEMY_DENSITY.ordinal()] = 0.001F;
/* 49 */     this.values[Parameter.ENEMY_DENSITY_ROUND_FACTOR.ordinal()] = 1.1F;
/* 50 */     this.values[Parameter.GAME_MAX_ROUNDS.ordinal()] = 10.0F;
/* 51 */     this.values[Parameter.DEBUG_PRINT_TOWER_DETAILS.ordinal()] = 0.0F;
/* 52 */     this.values[Parameter.GUI.ordinal()] = 0.0F;
/* 53 */     this.values[Parameter.GUI_REFRESH_RATE.ordinal()] = 20.0F;
/* 54 */     this.values[Parameter.MAP_ENDING_POINTS.ordinal()] = 2.0F;
/* 55 */     this.values[Parameter.MAP_STARTING_POINTS.ordinal()] = 2.0F;
/* 56 */     this.values[Parameter.MAP_STARTING_POINTS_SEPARATION_FACTOR.ordinal()] = 0.5F;
/* 57 */     this.values[Parameter.MAP_ENDING_POINTS_SEPARATION_FACTOR.ordinal()] = 0.1F;
/* 58 */     this.values[Parameter.MAP_NODE_VALUES.ordinal()] = 5.0F;
/* 59 */     this.values[Parameter.OBSTACLE_MAX_TRIES.ordinal()] = 20.0F;
/*    */   }
/*    */   
/*    */   public float get(Parameter p) {
/* 63 */     return this.values[p.ordinal()];
/*    */   }
/*    */   
/*    */   void set(Parameter p, float value) {
/* 67 */     this.values[p.ordinal()] = value;
/*    */   }
/*    */ }


/* Location:              /home/miguel/Universidad/segundo/ADA/practicas/grupoTarde/PracticaTarde3/towerdefense.jar!/net/agsh/towerdefense/Config.class
 * Java compiler version: 20 (64.0)
 * JD-Core Version:       1.1.3
 */