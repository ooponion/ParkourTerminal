package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LableValue;

public class LabelValueInt implements LableValue<Integer> {
    int data=0;

    @Override
    public void Update(Integer data) {
        this.data=data;
    }

    @Override
    public String getValue() {
        return GlobalConfig.getValueColor() +String.valueOf(data);
    }
}
