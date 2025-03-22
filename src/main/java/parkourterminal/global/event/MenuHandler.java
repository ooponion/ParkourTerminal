package parkourterminal.global.event;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import parkourterminal.gui.screens.impl.CustomIngameMenu;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

public class MenuHandler {
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiIngameMenu)
            event.gui = ScreenManager.getGuiScreen(new ScreenID("CustomInGameMenu"));
    }
}
