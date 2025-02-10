package parkourterminal.util;

import static parkourterminal.global.GlobalConfig.precision;

public class NumberWrapper {
    public static double round(double number) {
        double scale = Math.pow(10, precision);
        return Math.round(number * scale) / scale;
    }
}
