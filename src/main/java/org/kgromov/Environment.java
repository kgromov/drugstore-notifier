package org.kgromov;

public class Environment {
    private static final Environment instance = new Environment();

    private Environment() {
    }

    public static Environment getInstance() {
        return  instance;
    }

    public String getProperty(String key) {
        return System.getenv(key);
    }

    public String getProperty(String key, String defaultValue) {
        return  System.getenv().getOrDefault(key, defaultValue);
    }
}
