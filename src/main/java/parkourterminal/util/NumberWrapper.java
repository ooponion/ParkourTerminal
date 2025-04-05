package parkourterminal.util;


import parkourterminal.global.json.TerminalJsonConfig;

public class NumberWrapper {
    public static double round(double number) {
        double scale = Math.pow(10, TerminalJsonConfig.getProperties().getPrecision());
        return Math.round(number * scale) / scale;
    }
    public static String toDecimalString(double number){
        return String.format("%." + TerminalJsonConfig.getProperties().getPrecision() + "f", number);
    }
}
