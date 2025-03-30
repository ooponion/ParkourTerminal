package parkourterminal.global;

import net.minecraftforge.common.MinecraftForge;
import parkourterminal.global.event.*;

public class EventLoader {
    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(new ItemRightClickHandler());
        MinecraftForge.EVENT_BUS.register(new ItemLeftClickHandler());
        MinecraftForge.EVENT_BUS.register(new MenuHandler());
        MinecraftForge.EVENT_BUS.register(new LandBlockRendererHandler());
        MinecraftForge.EVENT_BUS.register(new TickEventHandler());
    }
}