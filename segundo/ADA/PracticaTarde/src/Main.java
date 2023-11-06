import analyzer.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Config config = Config.getInstance();

        processArgs(args, config);

        long maxExecutionTime = (long) config.get(Config.Parameter.MAX_EXECUTION_TIME);
        boolean strictTimeout = config.get(Config.Parameter.STRICT_TIMEOUT) == 1;
        boolean verbose = config.get(Config.Parameter.VERBOSE) == 1;

        if(verbose) {
            System.out.println("Working Directory: " + System.getProperty("user.dir"));
        }

        Chronometer chronometer = new Chronometer();
        int ok = 0;
        //for (String className : new String[]{"MinMax"}) { // uncomment this line and comment the next one to test a single algorithm
        for (String className : getAlgorithmsClasses()) {
            try {
                if(Algorithm.class.isAssignableFrom(Class.forName("algorithms." + className))) {
                    Thread t;
                    Algorithm algorithm = (Algorithm) Class.forName("algorithms." + className).newInstance();
                    Analyzer analyzer = new Analyzer(algorithm, maxExecutionTime);
                    t = new Thread(analyzer);
                    t.start();
                    if (strictTimeout) {
                        t.join(maxExecutionTime);
                        if (t.isAlive()) {
                            t.interrupt();
                            t.stop();  // @todo deprecated (use executor service instead?)
                            t = null;
                            System.out.println(className + ": Timeout! (" + algorithm.getComplexity() + ")");
                        }
                    } else {
                        t.join();
                    }

                    if (t != null) {
                        String complexity = (analyzer.getComplexity() != null) ? analyzer.getComplexity() : "ERROR";
                        String isOk = (complexity.equals(algorithm.getComplexity())) ? "OK" : "FAIL (" + algorithm.getComplexity() + ")";
                        if (isOk.equals("OK")) {
                            ok++;
                        }

                        if (verbose) {
                            System.out.println(className + "\t" + complexity + "\t" + isOk + "\t" + chronometer.getElapsedTime());
                        }
                    }
                    chronometer.start();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(ok);
    }

    private static void processArgs(String[] args, Config config) {
        for (String arg : args) {
            String[] split = arg.split("=");
            if (split.length == 2) {
                String key = split[0];
                String value = split[1];
                Config.Parameter parameter = Config.Parameter.valueOf(key.toUpperCase());
                config.set(parameter, Float.parseFloat(value));
            } else if(split.length == 1 && split[0].equals("--list")) {
                for (String className : getAlgorithmsClasses()) {
                    System.out.println("Algorithm\tComplexity");
                    try {
                        Algorithm algorithm = (Algorithm) Class.forName("algorithms." + className).newInstance();
                        System.out.println(className + "\t" + algorithm.getComplexity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            } else if(split.length == 1 && split[0].equals("--stats")) {
                java.util.HashMap<String, Integer> stats = new java.util.HashMap<>();
                for (String className : getAlgorithmsClasses()) {
                    try {
                        Algorithm algorithm = (Algorithm) Class.forName("algorithms." + className).newInstance();
                        String complexity = algorithm.getComplexity();
                        if(!stats.containsKey(complexity)) {
                            stats.put(complexity, 1);
                        } else {
                            stats.put(complexity, stats.get(complexity) + 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String[] complexities = {
                        "1",
                        "log(n)",
                        "n",
                        "n*log(n)",
                        "n^2",
                        "n^3",
                        "2^n"
                };
                System.out.println("Complexity\tCount");
                int total = 0;
                for(String complexity: complexities) {
                    System.out.println(complexity + "\t" + stats.get(complexity));
                    total += stats.get(complexity);
                }
                System.out.println("Total\t" + total);

                System.exit(0);
            } else {
                System.out.println("Invalid parameter: " + arg);
                System.exit(1);
            }
        }
    }

    private static ArrayList<String> getAlgorithmsClasses() {
        ArrayList<String> classes = new ArrayList<>();
        for (String fileName: Util.listFiles(new java.io.File("algorithms"), ".java")) {
            String className = fileName.substring(0, fileName.length() - 5);
            classes.add(className);
        }
        for (String fileName: Util.listFiles(new java.io.File("algorithms"), ".class")) {
            String className = fileName.substring(0, fileName.length() - 6);
            if(!classes.contains(className)) {
                classes.add(className);
            }
        }
        // IntelliJ default working directory is the project root, so we need to add the src directory
        for (String fileName: Util.listFiles(new java.io.File("src/algorithms"), ".java")) {
            String className = fileName.substring(0, fileName.length() - 5);
            classes.add(className);
        }

        return classes;
    }
}