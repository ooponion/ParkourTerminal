package parkourterminal.global;

import net.minecraftforge.common.MinecraftForge;
import parkourterminal.global.event.ItemLeftClickHandler;
import parkourterminal.global.event.ItemRightClickHandler;
import parkourterminal.global.event.MenuHandler;

public class EventLoader {
    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(new ItemRightClickHandler());
        MinecraftForge.EVENT_BUS.register(new ItemLeftClickHandler());
        MinecraftForge.EVENT_BUS.register(new MenuHandler());
    }
}