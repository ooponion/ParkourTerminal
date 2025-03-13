package parkourterminal.gui.Overlay;

import net.minecraftforge.common.MinecraftForge;

public class OverlayHandler {
    public static void RegisterOverlayInit() {
        MinecraftForge.EVENT_BUS.register(new CoordinatesOverlay());
    }
}
