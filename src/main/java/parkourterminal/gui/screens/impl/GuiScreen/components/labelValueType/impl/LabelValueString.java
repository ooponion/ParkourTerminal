package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

public class LabelValueString implements LabelValue<String> {
    String string="";

    @Override
    public void Update(String data) {
        string=data;
    }

    @Override
    public String getValue() {
        return GlobalData.getValueColor() +string;
    }
}
