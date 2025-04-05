package parkourterminal.gui.screens.impl.configScreen.intf.handlers;

public interface SettingModifier<T> {
    T getDefaultValue();
    T getConfigValue();
    void setConfigValue(String value);
    boolean ifSatisfied(String value);
}
