package de.jungblut.graph.bsp;

public class TestHelpers {

    private static final String TEMP_DIR;

    static {
        TEMP_DIR = System.getProperty("java.io.tmpdir");
    }

    public static String getTempDir() {
        return TEMP_DIR;
    }

}
