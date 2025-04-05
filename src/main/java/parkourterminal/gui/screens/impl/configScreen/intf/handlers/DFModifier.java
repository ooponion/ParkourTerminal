package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.global.GlobalConfig;
import parkourterminal.util.ParseHelper;

public class DFModifier implements SettingModifier<Integer> {

    @Override
    public Integer getDefaultValue() {
        return 5;
    }

    @Override
    public Integer getConfigValue() {
        return GlobalConfig.precision;
    }

    @Override
    public void setConfigValue(String value) {
        if(ifSatisfied(value))
        {
            GlobalConfig.updateConfig("precision",value);
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        return ParseHelper.isPositiveInteger(value)&&Integer.parseInt(value)>=0&&Integer.parseInt(value)<=16;
    }

}
