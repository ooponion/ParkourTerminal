package parkourterminal.util;

public class SystemOutHelper {
    public static void printf(String format, Object... args){
        System.out.printf(format+"\n", args);
    }
}
