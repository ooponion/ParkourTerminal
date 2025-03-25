package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.util.EnumChatFormatting;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

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
            return GlobalConfig.getValueColor() +"N/A"+GlobalConfig.getLabelColor()+"/"+GlobalConfig.getValueColor()+"N/A"+GlobalConfig.getLabelColor()+"/"+GlobalConfig.getValueColor()+"N/A";
        }
        EnumChatFormatting valueColor=GlobalConfig.getValueColor();
        EnumChatFormatting labelColor=GlobalConfig.getLabelColor();
        String slash=labelColor+"/";
        String x=valueColor+String.format("%." + GlobalConfig.precision + "f", vector3d.getX());
        String y=valueColor+String.format("%." + GlobalConfig.precision + "f", vector3d.getY());
        String z=valueColor+String.format("%." + GlobalConfig.precision + "f", vector3d.getZ());
        return x+slash+y+slash+z;
    }


}
