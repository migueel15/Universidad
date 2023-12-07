package net.agsh.towerdefense;

public class Config {
  float[] values = new float[Config.Parameter.values().length];

  public Config() {
    this.values[Config.Parameter.GAME_STEP_SIZE.ordinal()] = 20.0F;
    this.values[Config.Parameter.OBSTACLE_RADIUS_MIN.ordinal()] = 20.0F;
    this.values[Config.Parameter.OBSTACLE_RADIUS_MAX.ordinal()] = 50.0F;
    this.values[Config.Parameter.OBSTACLE_DENSITY.ordinal()] = 0.01F;
    this.values[Config.Parameter.TOWER_RADIUS_MIN.ordinal()] = 20.0F;
    this.values[Config.Parameter.TOWER_RADIUS_MAX.ordinal()] = 50.0F;
    this.values[Config.Parameter.TOWER_RANGE_MAX.ordinal()] = 2.0F * this.values[Config.Parameter.TOWER_RADIUS_MAX.ordinal()];
    this.values[Config.Parameter.TOWER_DAMAGE_MIN.ordinal()] = 1.0F;
    this.values[Config.Parameter.TOWER_DAMAGE_MAX.ordinal()] = 50.0F;
    this.values[Config.Parameter.TOWER_COOLDOWN_MIN.ordinal()] = 100.0F;
    this.values[Config.Parameter.TOWER_COOLDOWN_MAX.ordinal()] = 2000.0F;
    this.values[Config.Parameter.TOWER_DISPERSION_MIN.ordinal()] = 0.01F;
    this.values[Config.Parameter.TOWER_DISPERSION_MAX.ordinal()] = 30.0F;
    this.values[Config.Parameter.TOWER_COST_MIN.ordinal()] = 5.0F;
    this.values[Config.Parameter.TOWER_COST_MAX.ordinal()] = 20.0F;
    this.values[Config.Parameter.ENEMY_RADIUS_MIN.ordinal()] = 10.0F;
    this.values[Config.Parameter.ENEMY_RADIUS_MAX.ordinal()] = 10.0F;
    this.values[Config.Parameter.ENEMY_SPEED_MIN.ordinal()] = 30.0F;
    this.values[Config.Parameter.ENEMY_SPEED_MAX.ordinal()] = 60.0F;
    this.values[Config.Parameter.MAP_SIZE_X.ordinal()] = 1200.0F;
    this.values[Config.Parameter.MAP_SIZE_Y.ordinal()] = 600.0F;
    this.values[Config.Parameter.MAP_GRID_SPACE.ordinal()] = 20.0F;
    this.values[Config.Parameter.TOWER_MIN_MONEY_FACTOR.ordinal()] = 0.4F;
    this.values[Config.Parameter.TOWER_MAX_MONEY_FACTOR.ordinal()] = 1.1F;
    this.values[Config.Parameter.TOWER_DENSITY.ordinal()] = 0.01F;
    this.values[Config.Parameter.ENEMY_HEALTH_MIN.ordinal()] = 100.0F;
    this.values[Config.Parameter.ENEMY_HEALTH_MAX.ordinal()] = 500.0F;
    this.values[Config.Parameter.ENEMY_SPAWN_INTERVAL.ordinal()] = 1000.0F;
    this.values[Config.Parameter.ENEMY_DENSITY.ordinal()] = 0.001F;
    this.values[Config.Parameter.ENEMY_DENSITY_ROUND_FACTOR.ordinal()] = 1.1F;
    this.values[Config.Parameter.GAME_MAX_ROUNDS.ordinal()] = 10.0F;
    this.values[Config.Parameter.DEBUG_PRINT_TOWER_DETAILS.ordinal()] = 0.0F;
    this.values[Config.Parameter.GUI.ordinal()] = 0.0F;
    this.values[Config.Parameter.GUI_REFRESH_RATE.ordinal()] = 20.0F;
    this.values[Config.Parameter.MAP_ENDING_POINTS.ordinal()] = 2.0F;
    this.values[Config.Parameter.MAP_STARTING_POINTS.ordinal()] = 2.0F;
    this.values[Config.Parameter.MAP_STARTING_POINTS_SEPARATION_FACTOR.ordinal()] = 0.5F;
    this.values[Config.Parameter.MAP_ENDING_POINTS_SEPARATION_FACTOR.ordinal()] = 0.1F;
    this.values[Config.Parameter.MAP_NODE_VALUES.ordinal()] = 5.0F;
    this.values[Config.Parameter.OBSTACLE_MAX_TRIES.ordinal()] = 20.0F;
    this.values[Config.Parameter.W_DAM.ordinal()] = 100.0F;
    this.values[Config.Parameter.W_CODW.ordinal()] = -0.36F;
    this.values[Config.Parameter.W_RAN.ordinal()] = 0.6F;
    this.values[Config.Parameter.W_DISP.ordinal()] = -38.8F;
  }

  public float get(Config.Parameter p) {
    return this.values[p.ordinal()];
  }

  void set(Config.Parameter p, float value) {
    this.values[p.ordinal()] = value;
  }

  public static enum Parameter {
    GAME_STEP_SIZE,
    OBSTACLE_RADIUS_MIN,
    OBSTACLE_RADIUS_MAX,
    OBSTACLE_DENSITY,
    TOWER_RADIUS_MIN,
    TOWER_RADIUS_MAX,
    TOWER_RANGE_MIN,
    TOWER_RANGE_MAX,
    TOWER_DAMAGE_MIN,
    TOWER_DAMAGE_MAX,
    TOWER_COOLDOWN_MIN,
    TOWER_COOLDOWN_MAX,
    TOWER_DISPERSION_MIN,
    TOWER_DISPERSION_MAX,
    TOWER_COST_MIN,
    TOWER_COST_MAX,
    ENEMY_RADIUS_MIN,
    ENEMY_RADIUS_MAX,
    ENEMY_SPEED_MIN,
    ENEMY_SPEED_MAX,
    MAP_SIZE_X,
    MAP_SIZE_Y,
    MAP_GRID_SPACE,
    TOWER_MIN_MONEY_FACTOR,
    TOWER_MAX_MONEY_FACTOR,
    TOWER_DENSITY,
    ENEMY_HEALTH_MIN,
    ENEMY_HEALTH_MAX,
    ENEMY_SPAWN_INTERVAL,
    ENEMY_DENSITY,
    ENEMY_DENSITY_ROUND_FACTOR,
    GAME_MAX_ROUNDS,
    DEBUG_PRINT_TOWER_DETAILS,
    GUI,
    GUI_REFRESH_RATE,
    MAP_ENDING_POINTS,
    MAP_STARTING_POINTS,
    MAP_STARTING_POINTS_SEPARATION_FACTOR,
    MAP_ENDING_POINTS_SEPARATION_FACTOR,
    MAP_NODE_VALUES,
    OBSTACLE_MAX_TRIES,
    W_DAM,
    W_RAN,
    W_CODW,
    W_DISP;

    private Parameter() {
    }
  }
}
