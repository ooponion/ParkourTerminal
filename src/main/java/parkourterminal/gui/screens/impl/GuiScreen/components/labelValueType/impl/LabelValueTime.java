package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.globalData.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LabelValueTime implements LabelValue<LocalDateTime> {
    String formattedTime;
    @Override
    public void Update(LocalDateTime data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        formattedTime = data.format(formatter);
    }

    @Override
    public String getValue() {
        if(formattedTime==null){
            return GlobalData.getValueColor() +"N/A";
        }
        return GlobalData.getValueColor() +formattedTime;
    }
}
