package parkourterminal.util;


import parkourterminal.global.json.TerminalJsonConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberWrapper {
    public static double round(double number) {
        double scale = Math.pow(10, TerminalJsonConfig.getProperties().getPrecision());
        return Math.round(number * scale) / scale;
    }
    public static String toDecimalString(double number,int n,boolean trimZeros){
        if(trimZeros){
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
    public static String toDecimalString(double number){
        return toDecimalString(number,TerminalJsonConfig.getProperties().getPrecision(),TerminalJsonConfig.getProperties().isTrimZeros());
    }
    public static String toFormattedFloat(float value,String syntax){
        return String.format(syntax, value);
    }
    public static int LeadingZerosCounter(double number){
        BigDecimal num = new BigDecimal(number);
        String numStr = num.toPlainString();
        int decimalIndex = numStr.indexOf('.');
        if (decimalIndex == -1) {
            return -1;
        }
        int count = 0;
        for (int i = decimalIndex + 1; i < numStr.length(); i++) {
            if (numStr.charAt(i) == '0') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
