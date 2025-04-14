package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.data.GlobalData;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;

public class LandblockDisplayModifier implements SettingModifier<Boolean>{
    @Override
    public Boolean getDefaultValue() {
        return true;
    }

    @Override
    public Boolean getConfigValue() {
        return TerminalJsonConfig.getProperties().isBbVisible();
    }

    @Override
    public void setConfigValue(String value) {
        if(ifSatisfied(value)){
            TerminalJsonConfig.getProperties().setBbVisible(Boolean.parseBoolean(value));
            TerminalJsonConfig.getProperties().setCondVisible(Boolean.parseBoolean(value));
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        return ParseHelper.isBoolean(value);
    }
}
