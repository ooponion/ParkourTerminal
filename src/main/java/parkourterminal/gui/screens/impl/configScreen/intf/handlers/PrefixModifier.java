package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;

public class PrefixModifier implements SettingModifier<String>{
    @Override
    public String getDefaultValue() {
        return "terminal";
    }

    @Override
    public String getConfigValue() {
        return TerminalJsonConfig.getPrefix();
    }

    @Override
    public void setConfigValue(String value) {
        TerminalJsonConfig.setPrefix(value);
    }

    @Override
    public boolean ifSatisfied(String value) {
        return true;
    }
}
