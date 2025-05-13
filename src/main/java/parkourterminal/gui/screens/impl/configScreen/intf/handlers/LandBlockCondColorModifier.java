package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;

public class LandBlockCondColorModifier implements SettingModifier<Integer>{
    @Override
    public Integer getDefaultValue() {
        return 0xf1c008;
    }

    @Override
    public Integer getConfigValue() {
        return TerminalJsonConfig.getProperties().getCondColor();
    }

    @Override
    public void setConfigValue(String value) {
        Integer color=ParseHelper.ParseColor(value);
        if(ifSatisfied(value)&&color!=null){
            TerminalJsonConfig.getProperties().setCondColor(color);
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        Integer parseColor=ParseHelper.ParseColor(value);
        return parseColor!=null&&parseColor>=0&&parseColor<=0xFFFFFF;
    }
}
