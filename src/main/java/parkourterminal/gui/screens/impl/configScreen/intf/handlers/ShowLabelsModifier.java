package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;

public class ShowLabelsModifier implements SettingModifier<Boolean>{
    @Override
    public Boolean getDefaultValue() {
        return true;
    }

    @Override
    public Boolean getConfigValue() {
        return TerminalJsonConfig.isShowLabels();
    }

    @Override
    public void setConfigValue(String value) {
        if(ifSatisfied(value)){
            TerminalJsonConfig.setShowLabels(Boolean.parseBoolean(value));
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        return ParseHelper.isBoolean(value);
    }
}
