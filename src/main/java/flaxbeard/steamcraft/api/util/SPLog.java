package flaxbeard.steamcraft.api.util;

public class SPLog {

    public static final int NONE = 0;
    public static final int ERROR = 1;
    public static final int INFO = 2;
    public static final int DEBUG = 3;
    private static SPLog INSTANCE = new SPLog();
    private int logLevel = 0;

    public static SPLog getInstance() {
        return INSTANCE;
    }

    public SPLog setLogLevel(int level) {
        this.logLevel = level;
        return this;
    }

    public void log(int level, String message) {
        if (level <= this.logLevel) {
            print(message);
        }
    }

    public void error(String message) {
        if (this.logLevel >= ERROR) {
            print("ERROR: " + message);
        }
    }

    public void info(String message) {
        if (this.logLevel >= INFO) {
            print("INFO: " + message);
        }
    }

    public void debug(String message) {
        if (this.logLevel >= DEBUG) {
            print("DEBUG: " + message);
        }
    }


    private void print(String msg) {
        System.out.println("[FSP] " + msg);
    }

}
