package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.GlobalData;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.util.NumberWrapper;

public class LabelValuePb implements LabelValue<Double> {
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
        int n= TerminalJsonConfig.getProperties().getPrecision();
        int e=NumberWrapper.LeadingZerosCounter(value)+1;
        if(e>n){
            return GlobalData.getValueColor() + NumberWrapper.toDecimalString(value,e,true);
        }
        return GlobalData.getValueColor() + NumberWrapper.toDecimalString(value);
    }
}
