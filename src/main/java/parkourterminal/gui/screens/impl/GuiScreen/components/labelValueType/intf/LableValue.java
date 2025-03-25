package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf;

import net.minecraft.client.Minecraft;

public interface LableValue<T> {
    void Update(T data);
    String getValue();

}
