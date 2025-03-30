package parkourterminal.data.globalData;

import net.minecraft.util.EnumChatFormatting;
import parkourterminal.data.landingblock.LandingBlockData;

public class GlobalData {
    private static EnumChatFormatting labelColor=EnumChatFormatting.LIGHT_PURPLE;
    private static EnumChatFormatting ValueColor=EnumChatFormatting.AQUA;
    //    private static int LandBlockColor =0x10bafa;
    private static LandingBlockData landingBlock=new LandingBlockData();
    public static EnumChatFormatting getLabelColor(){
        return labelColor;
    }

    public static EnumChatFormatting getValueColor() {
        return ValueColor;
    }
    public static LandingBlockData getLandingBlock(){return landingBlock;}
}
