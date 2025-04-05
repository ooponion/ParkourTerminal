package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.util.NumberWrapper;

public class LabelValueDouble implements LabelValue<Double> {
    Double value;
    @Override
    public void Update(Double data) {
        value=data;
    }

    @Override
    public String getValue() {
        if(value==null||value.isNaN()){
            return GlobalData.getValueColor() +"N/A";
        }
        return GlobalData.getValueColor() + NumberWrapper.toDecimalString(value);
    }
}
