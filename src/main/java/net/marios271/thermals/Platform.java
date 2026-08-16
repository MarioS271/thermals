package net.marios271.thermals;

public class Platform {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() { return OS.contains("win"); }
    public static boolean isLinux() { return OS.contains("nux"); }

    public static boolean isAdminWindows() {
        if (!isWindows()) { return false; }
        try {
            Process p = Runtime.getRuntime().exec("net session");
            p.waitFor();
            return p.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
