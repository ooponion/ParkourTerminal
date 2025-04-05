package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.client.Minecraft;
import parkourterminal.data.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

public class LabelValuePing implements LabelValue<Integer> {
    String ms="SinglePlayer";

    @Override
    public void Update(Integer data) {
        boolean isSingleplayer = Minecraft.getMinecraft().isSingleplayer();
        if(isSingleplayer){
            ms="SinglePlayer";
        }else{
            ms=String.valueOf(data);
        }
    }

    @Override
    public String getValue() {
        return GlobalData.getValueColor() +ms;
    }
}
