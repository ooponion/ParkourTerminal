package parkourterminal.gui.screens.intf.instantiationScreen.intf;

import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

public interface InstantiationScreen {
    GuiScreen getScreenInstantiation();
    ScreenID getScreenID();
}
