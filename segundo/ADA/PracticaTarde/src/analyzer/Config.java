package analyzer;

public class Config {
    // singleton pattern
    private static Config instance = null;

    // Enum for parameters
    public enum Parameter {
        MAX_EXECUTION_TIME, STRICT_TIMEOUT, VERBOSE
    }

    float[] values = new float[Parameter.values().length];

    // singleton pattern
    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public Config(){
        // Set default values
        values[Parameter.MAX_EXECUTION_TIME.ordinal()] = 10 * 1000; // in milliseconds
        values[Parameter.STRICT_TIMEOUT.ordinal()] = 0; // 0 = false, 1 = true
        values[Parameter.VERBOSE.ordinal()] = 1; // 0 = false, 1 = true
    }

    public float get(Parameter p) {
        return values[p.ordinal()];
    }

    public void set(Parameter p, float value) {
        values[p.ordinal()] = value;
    }

}