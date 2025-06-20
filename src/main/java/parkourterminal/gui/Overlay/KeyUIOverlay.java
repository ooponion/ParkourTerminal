package parkourterminal.gui.Overlay;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;

@SideOnly(Side.CLIENT)
public class KeyUIOverlay {
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type ==  RenderGameOverlayEvent.ElementType.ALL) {
            if(Minecraft.getMinecraft().currentScreen ==null){
                KeyUIManager.getInstance().getContainer().overlayDraw();
            }
        }

    }
}
