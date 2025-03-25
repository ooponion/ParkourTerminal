package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.util.EnumChatFormatting;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LableValue;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;

public class LabelValue2DDegreeVector implements LableValue<Vector2f> {
    Vector2f vector2f;
    @Override
    public void Update(Vector2f data) {
        vector2f=new Vector2f(data);
    }

    @Override
    public String getValue() {
        if(vector2f==null){
            return GlobalConfig.getValueColor() +"N/A"+GlobalConfig.getLabelColor()+"/"+GlobalConfig.getValueColor()+"N/A";
        }
        EnumChatFormatting valueColor=GlobalConfig.getValueColor();
        EnumChatFormatting labelColor=GlobalConfig.getLabelColor();
        String slash=labelColor+"/";
        String x=valueColor+String.format("%." + GlobalConfig.precision + "f", vector2f.getX());
        String y=valueColor+String.format("%." + GlobalConfig.precision + "f", vector2f.getY());
        return x+slash+y+"\u00B0";
    }


}
