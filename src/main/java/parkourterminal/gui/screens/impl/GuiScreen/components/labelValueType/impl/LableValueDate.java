package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import net.minecraft.client.Minecraft;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LableValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LableValueDate implements LableValue<LocalDateTime> {
    String formattedDate;
    @Override
    public void Update(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
        formattedDate = data.format(formatter);
    }

    @Override
    public String getValue() {
        if(formattedDate==null){
            return GlobalConfig.getValueColor() +"N/A";
        }
        return GlobalConfig.getValueColor() +formattedDate;
    }
}
