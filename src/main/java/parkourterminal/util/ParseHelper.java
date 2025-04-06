package parkourterminal.util;

import net.minecraft.util.EnumChatFormatting;

public class ParseHelper {
    public static boolean isNunNegativeInteger(String str) {
        return str != null && str.matches("^[0-9]\\d*$");
    }
    public static boolean isBoolean(String str) {
        return str != null && (str.equalsIgnoreCase("true")||str.equalsIgnoreCase("false"));
    }
    public static EnumChatFormatting parseEnumChatFormatting(String name) {
        if (name == null) {
            return null;
        }
        name = name.toUpperCase();

        if (name.equals("BLACK")) return EnumChatFormatting.BLACK;
        else if (name.equals("DARK_BLUE")) return EnumChatFormatting.DARK_BLUE;
        else if (name.equals("DARK_GREEN")) return EnumChatFormatting.DARK_GREEN;
        else if (name.equals("DARK_AQUA")) return EnumChatFormatting.DARK_AQUA;
        else if (name.equals("DARK_RED")) return EnumChatFormatting.DARK_RED;
        else if (name.equals("DARK_PURPLE")) return EnumChatFormatting.DARK_PURPLE;
        else if (name.equals("GOLD")) return EnumChatFormatting.GOLD;
        else if (name.equals("GRAY")) return EnumChatFormatting.GRAY;
        else if (name.equals("DARK_GRAY")) return EnumChatFormatting.DARK_GRAY;
        else if (name.equals("BLUE")) return EnumChatFormatting.BLUE;
        else if (name.equals("GREEN")) return EnumChatFormatting.GREEN;
        else if (name.equals("AQUA")) return EnumChatFormatting.AQUA;
        else if (name.equals("RED")) return EnumChatFormatting.RED;
        else if (name.equals("LIGHT_PURPLE")) return EnumChatFormatting.LIGHT_PURPLE;
        else if (name.equals("YELLOW")) return EnumChatFormatting.YELLOW;
        else if (name.equals("WHITE")) return EnumChatFormatting.WHITE;
        else return null; // 未找到
    }
    public static Integer ParseColor(String color){
        if(color.isEmpty()){
            return null;
        }
        Integer result=null;
        try{
            result=Integer.decode(color);
        }catch (NumberFormatException e) {
           e.printStackTrace();
        }
        return result;
    }
}
