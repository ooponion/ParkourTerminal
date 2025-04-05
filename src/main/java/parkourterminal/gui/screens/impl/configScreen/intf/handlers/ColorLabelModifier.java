package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.util.ParseHelper;

public class ColorLabelModifier implements SettingModifier<String>{
    @Override
    public String getDefaultValue() {
        return "gold";
    }

    @Override
    public String getConfigValue() {
        return GlobalData.getColorData().getLabelColor().getFriendlyName();
    }

    @Override
    public void setConfigValue(String value) {
        if(ifSatisfied(value)){
            GlobalData.getColorData().setLabelColor(ParseHelper.parseEnumChatFormatting(value));
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        return ParseHelper.parseEnumChatFormatting(value)!=null;
    }
}
