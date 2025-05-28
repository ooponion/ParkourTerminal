package parkourterminal.util;


import parkourterminal.global.json.TerminalJsonConfig;

import java.text.DecimalFormat;

public class NumberWrapper {
    public static double round(double number) {
        double scale = Math.pow(10, TerminalJsonConfig.getProperties().getPrecision());
        return Math.round(number * scale) / scale;
    }
    public static String toDecimalString(double number){
        int n=TerminalJsonConfig.getProperties().getPrecision();
        if(TerminalJsonConfig.getProperties().isTrimZeros()){
            StringBuilder pattern = new StringBuilder("#");
            if (n > 0) {
                pattern.append(".");
                for (int i = 0; i < n; i++) {
                    pattern.append("#");
                }
            }
            DecimalFormat df = new DecimalFormat(pattern.toString());
            return df.format(number);
        }
        return String.format("%." + n + "f", number);
    }
    public static String toFormattedFloat(float value,String syntax){
        return String.format(syntax, value);
    }
}
