package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.client.Minecraft;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LableValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LableValueTime implements LableValue<LocalDateTime> {
    String formattedTime;
    @Override
    public void Update(LocalDateTime data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        formattedTime = data.format(formatter);
    }

    @Override
    public String getValue() {
        if(formattedTime==null){
            return GlobalConfig.getValueColor() +"N/A";
        }
        return GlobalConfig.getValueColor() +formattedTime;
    }
}
