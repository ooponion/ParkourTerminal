package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl;

import parkourterminal.data.globalData.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.BlipLabel;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;

import javax.vecmath.Vector3d;

public class LabelValueBlip implements LabelValue<BlipLabel> {
    BlipLabel blipLabel=new BlipLabel(Double.NaN,0);
    @Override
    public void Update(BlipLabel data) {
        blipLabel=data;
    }

    @Override
    public String getValue() {
        return GlobalData.getValueColor() +String.valueOf(blipLabel.getBlipTimes())+
                GlobalData.getLabelColor()+" chained/Y: " +
                GlobalData.getValueColor() + blipLabel.getBlipY();
    }
}
