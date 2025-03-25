package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.util.AngleUtils;

public class LabelValueDegree implements LabelValue<Float> {
    Float value;
    @Override
    public void Update(Float data) {
        value= AngleUtils.normalizeAngle(data);
    }

    @Override
    public String getValue() {
        if(value==null){
            return GlobalConfig.getValueColor() +"N/A";
        }
        return GlobalConfig.getValueColor() +String.format("%." + GlobalConfig.precision + "f", value)+"\u00B0";
    }
}
