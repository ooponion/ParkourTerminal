package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.util.EnumChatFormatting;
import parkourterminal.data.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.util.NumberWrapper;

import javax.vecmath.Vector3d;

public class LabelValue3DVector implements LabelValue<Vector3d> {
    Vector3d vector3d;
    @Override
    public void Update(Vector3d data) {
        vector3d=new Vector3d(data);
    }

    @Override
    public String getValue() {
        if(vector3d==null){
            return GlobalData.getValueColor() +"N/A"+GlobalData.getLabelColor()+"/"+GlobalData.getValueColor()+"N/A"+GlobalData.getLabelColor()+"/"+GlobalData.getValueColor()+"N/A";
        }
        EnumChatFormatting valueColor=GlobalData.getValueColor();
        EnumChatFormatting labelColor=GlobalData.getLabelColor();
        String slash=labelColor+"/";
        String x=valueColor+NumberWrapper.toDecimalString(vector3d.getX());
        String y=valueColor+NumberWrapper.toDecimalString(vector3d.getY());
        String z=valueColor+NumberWrapper.toDecimalString(vector3d.getZ());
        return x+slash+y+slash+z;
    }


}
