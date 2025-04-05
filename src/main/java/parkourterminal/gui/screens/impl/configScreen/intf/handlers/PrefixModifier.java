package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.global.json.TerminalJsonConfig;

public class PrefixModifier implements SettingModifier<String>{
    @Override
    public String getDefaultValue() {
        return "terminal";
    }

    @Override
    public String getConfigValue() {
        return TerminalJsonConfig.getProperties().getPrefix();
    }

    @Override
    public void setConfigValue(String value) {
        TerminalJsonConfig.getProperties().setPrefix(value);
    }

    @Override
    public boolean ifSatisfied(String value) {
        return true;
    }
}
