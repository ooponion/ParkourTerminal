package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;

public class DFModifier implements SettingModifier<Integer> {

    @Override
    public Integer getDefaultValue() {
        return 5;
    }

    @Override
    public Integer getConfigValue() {
        return TerminalJsonConfig.getProperties().getPrecision();
    }

    @Override
    public void setConfigValue(String value) {
        if(ifSatisfied(value))
        {
            TerminalJsonConfig.getProperties().setPrecision(Integer.parseInt(value));
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        return ParseHelper.isNunNegativeInteger(value)&&Integer.parseInt(value)>=0&&Integer.parseInt(value)<=16;
    }

}
