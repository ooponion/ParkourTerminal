package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LableValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LableValuePing implements LableValue<Integer> {
    String ms="SinglePlayer";

    @Override
    public void Update(Integer data) {
        ms=String.valueOf(data);
    }

    @Override
    public String getValue() {
        return GlobalConfig.getValueColor() +ms;
    }
}
