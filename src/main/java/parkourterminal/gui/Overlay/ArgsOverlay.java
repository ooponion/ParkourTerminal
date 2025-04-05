package parkourterminal.gui.Overlay;

import ibxm.Player;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.TerminalGuiScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.BlockUtils;

import java.util.ArrayList;
import java.util.List;

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
        if(TerminalJsonConfig.getProperties().isShowLabels()&&screen!=null){
            screen.drawOverlay();
        }
    }
}
