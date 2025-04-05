package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;

public class LandblockOffsetModifier implements SettingModifier<Boolean>{
    @Override
    public Boolean getDefaultValue() {
        return false;
    }

    @Override
    public Boolean getConfigValue() {
        return TerminalJsonConfig.getLandBlockJson().isSendChatOffset();
    }

    @Override
    public void setConfigValue(String value) {
        if(ifSatisfied(value)){
            TerminalJsonConfig.getLandBlockJson().setSendChatOffset(Boolean.parseBoolean(value));
        }
    }

    @Override
    public boolean ifSatisfied(String value) {
        return ParseHelper.isBoolean(value);
    }
}
