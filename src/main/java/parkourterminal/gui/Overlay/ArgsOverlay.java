package parkourterminal.gui.Overlay;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import parkourterminal.gui.screens.impl.GuiScreen.TerminalGuiScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

public class ArgsOverlay {
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        // 仅在游戏 HUD 渲染时执行
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;


        if(ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"))==null){
            return;
        }
        if(Minecraft.getMinecraft().currentScreen==ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"))){
            return;
        }
        TerminalGuiScreen screen=(TerminalGuiScreen)ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"));
        screen.drawOverlay();
    }
}
