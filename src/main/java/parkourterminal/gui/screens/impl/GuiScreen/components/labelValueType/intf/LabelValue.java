package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf;

public interface LabelValue<T> {
    void Update(T data);
    String getValue();

}
