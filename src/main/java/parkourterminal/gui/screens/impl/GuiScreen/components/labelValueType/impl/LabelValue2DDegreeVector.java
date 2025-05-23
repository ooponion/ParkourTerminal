package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.util.EnumChatFormatting;
import parkourterminal.data.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.util.NumberWrapper;

import javax.vecmath.Vector2f;

public class LabelValue2DDegreeVector implements LabelValue<Vector2f> {
    Vector2f vector2f;
    @Override
    public void Update(Vector2f data) {
        vector2f=new Vector2f(data);
    }

    @Override
    public String getValue() {
        if(vector2f==null){
            return GlobalData.getValueColor() +"N/A"+GlobalData.getLabelColor()+"/"+GlobalData.getValueColor()+"N/A";
        }
        EnumChatFormatting valueColor=GlobalData.getValueColor();
        EnumChatFormatting labelColor=GlobalData.getLabelColor();
        String slash=labelColor+"/";
        String x=valueColor+ NumberWrapper.toDecimalString(vector2f.getX());
        String y=valueColor+NumberWrapper.toDecimalString(vector2f.getY());
        return x+slash+y+"\u00B0";
    }


}
