package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LableValue;

public class LabelValueString implements LableValue<String> {
    String string="";

    @Override
    public void Update(String data) {
        string=data;
    }

    @Override
    public String getValue() {
        return GlobalConfig.getValueColor() +string;
    }
}
