package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

public class LabelValueDouble implements LabelValue<Double> {
    Double value;
    @Override
    public void Update(Double data) {
        value=data;
    }

    @Override
    public String getValue() {
        if(value==null){
            return GlobalConfig.getValueColor() +"N/A";
        }
        return GlobalConfig.getValueColor() +String.format("%." + GlobalConfig.precision + "f", value);
    }
}
