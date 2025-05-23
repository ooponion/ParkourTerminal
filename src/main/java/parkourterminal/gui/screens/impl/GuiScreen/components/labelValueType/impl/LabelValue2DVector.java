package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.util.EnumChatFormatting;
import parkourterminal.data.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.util.NumberWrapper;

import javax.vecmath.Vector2d;

public class LabelValue2DVector implements LabelValue<Vector2d> {
    Vector2d vector2d;
    @Override
    public void Update(Vector2d data) {
        vector2d=new Vector2d(data);
    }

    @Override
    public String getValue() {
        if(vector2d==null){
            return GlobalData.getValueColor() +"N/A"+GlobalData.getLabelColor()+"/"+GlobalData.getValueColor()+"N/A";
        }
        EnumChatFormatting valueColor=GlobalData.getValueColor();
        EnumChatFormatting labelColor=GlobalData.getLabelColor();
        String slash=labelColor+"/";
        String x=valueColor+ NumberWrapper.toDecimalString(vector2d.getX());
        String y=valueColor+NumberWrapper.toDecimalString(vector2d.getY());
        return x+slash+y;
    }


}
