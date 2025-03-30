package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

public class LabelValueBoolean implements LabelValue<Boolean> {
    String bool="not set";
    @Override
    public void Update(Boolean data) {
        bool=data?"Enable":"Disable";
    }

    @Override
    public String getValue() {
        return GlobalData.getValueColor() +bool;
    }
}
