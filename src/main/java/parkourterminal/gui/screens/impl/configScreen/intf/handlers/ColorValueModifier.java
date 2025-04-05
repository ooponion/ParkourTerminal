package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.data.GlobalData;
import parkourterminal.util.ParseHelper;

public class ColorValueModifier implements SettingModifier<String>{
    @Override
    public String getDefaultValue() {
        return "aqua";
    }

    @Override
    public String getConfigValue() {
        return GlobalData.getColorData().getValueColor().getFriendlyName();
    }

    @Override
    public void setConfigValue(String value) {
        if(ifSatisfied(value)){
            GlobalData.getColorData().setValueColor(ParseHelper.parseEnumChatFormatting(value));
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        return ParseHelper.parseEnumChatFormatting(value)!=null;
    }
}
