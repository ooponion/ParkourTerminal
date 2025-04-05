package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LabelValueDate implements LabelValue<LocalDateTime> {
    String formattedDate;
    @Override
    public void Update(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
        formattedDate = data.format(formatter);
    }

    @Override
    public String getValue() {
        if(formattedDate==null){
            return GlobalData.getValueColor() +"N/A";
        }
        return GlobalData.getValueColor() +formattedDate;
    }
}
