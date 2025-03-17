package parkourterminal.global.event;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import parkourterminal.gui.screens.impl.CustomIngameMenu;

public class MenuHandler {
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiIngameMenu)
            event.gui = new CustomIngameMenu();
    }
}
