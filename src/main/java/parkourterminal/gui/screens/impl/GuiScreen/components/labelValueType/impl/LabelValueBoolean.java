package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LableValue;

public class LabelValueBoolean implements LableValue<Boolean> {
    String bool="not set";
    @Override
    public void Update(Boolean data) {
        bool=data?"Enable":"Disable";
    }

    @Override
    public String getValue() {
        return GlobalConfig.getValueColor() +bool;
    }
}
