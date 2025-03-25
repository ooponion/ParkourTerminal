package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

public class LabelValueBlockPos implements LabelValue<BlockPos> {
    BlockPos blockPos;


    @Override
    public void Update(BlockPos data) {
        blockPos=new BlockPos(data);
    }

    @Override
    public String getValue() {
        if(blockPos==null){
            return GlobalConfig.getValueColor() +"N/A"+GlobalConfig.getLabelColor()+"/"+GlobalConfig.getValueColor()+"N/A"+GlobalConfig.getLabelColor()+"/"+GlobalConfig.getValueColor()+"N/A";
        }
        EnumChatFormatting valueColor=GlobalConfig.getValueColor();
        EnumChatFormatting labelColor=GlobalConfig.getLabelColor();
        String slash=labelColor+"/";
        String x=valueColor+String.valueOf(blockPos.getX());
        String y=valueColor+String.valueOf(blockPos.getY());
        String z=valueColor+String.valueOf(blockPos.getZ());
        return x+slash+y+slash+z;
    }


}
