package parkourterminal.global.event;

import javafx.scene.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import parkourterminal.data.GlobalData;
import parkourterminal.data.inputdata.TickInput;
import parkourterminal.data.macroData.controller.MacroRunner;
import parkourterminal.data.macroData.intf.Operation;
import parkourterminal.freecamera.CameraController;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.SendMessageHelper;
import parkourterminal.util.SystemOutHelper;

public class TickEventHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            EntityPlayerSP player= Minecraft.getMinecraft().thePlayer;
            if(player==null){
                return;
            }
        }
        if(event.phase == TickEvent.Phase.END){
            EntityPlayerSP player= Minecraft.getMinecraft().thePlayer;
            if(player==null){
                return;
            }
            boolean toggled=TerminalJsonConfig.getProperties().isToggleSprint();
            if (toggled) {
                player.setSprinting(true);
            }
            GlobalData.getInputData().UpdateOperation(player);
            GlobalData.getLandingBlock().Update(player);
            GlobalData.getLandData().Update(player);
            GlobalData.getJumpData().Update(player);
            GlobalData.getSpeedData().Update(player);
            MacroRunner.onTick(player);
            CameraController.FREECAM.update();
        }
    }
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {




    }

}
