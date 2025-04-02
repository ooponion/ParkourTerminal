package parkourterminal.data.ColorData;

import net.minecraft.util.EnumChatFormatting;

public class ColorData {
    private EnumChatFormatting labelColor=EnumChatFormatting.LIGHT_PURPLE;
    private EnumChatFormatting ValueColor=EnumChatFormatting.AQUA;
    public EnumChatFormatting getLabelColor(){
        return labelColor;
    }

    public EnumChatFormatting getValueColor() {
        return ValueColor;
    }

    public void reset(){
        labelColor=EnumChatFormatting.LIGHT_PURPLE;
        ValueColor=EnumChatFormatting.AQUA;
    }
    public void setLabelColor(EnumChatFormatting labelColor) {
        this.labelColor = labelColor;
    }

    public void setValueColor(EnumChatFormatting valueColor) {
        ValueColor = valueColor;
    }
}
