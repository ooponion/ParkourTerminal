package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

public class LabelValuePing implements LabelValue<Integer> {
    String ms="SinglePlayer";

    @Override
    public void Update(Integer data) {
        ms=String.valueOf(data);
    }

    @Override
    public String getValue() {
        return GlobalData.getValueColor() +ms;
    }
}
