package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

public class LabelValueInt implements LabelValue<Integer> {
    int data=0;

    @Override
    public void Update(Integer data) {
        this.data=data;
    }

    @Override
    public String getValue() {
        return GlobalData.getValueColor() +String.valueOf(data);
    }
}
