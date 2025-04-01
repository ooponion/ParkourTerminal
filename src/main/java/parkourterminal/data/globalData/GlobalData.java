package parkourterminal.data.globalData;

import net.minecraft.util.EnumChatFormatting;
import parkourterminal.data.EntityJumpData.JumpData;
import parkourterminal.data.EntitySpeedData.SpeedData;
import parkourterminal.data.inputdata.InputData;
import parkourterminal.data.EntitylandData.LandingData;
import parkourterminal.data.landingblock.LandingBlockData;

public class GlobalData {
    private static EnumChatFormatting labelColor=EnumChatFormatting.LIGHT_PURPLE;
    private static EnumChatFormatting ValueColor=EnumChatFormatting.AQUA;
    //    private static int LandBlockColor =0x10bafa;
    private static LandingBlockData landingBlock=new LandingBlockData();

    private static InputData inputData=new InputData();
    private static LandingData landingData =new LandingData();
    private static SpeedData speedData =new SpeedData();
    private static JumpData jumpData =new JumpData();
    public static EnumChatFormatting getLabelColor(){
        return labelColor;
    }

    public static EnumChatFormatting getValueColor() {
        return ValueColor;
    }
    public static LandingBlockData getLandingBlock(){return landingBlock;}
    public static InputData getInputData(){return inputData;}
    public static LandingData getLandData(){return landingData;}
    public static  SpeedData getSpeedData(){return speedData;}

    public static JumpData getJumpData() {
        return jumpData;
    }
}
