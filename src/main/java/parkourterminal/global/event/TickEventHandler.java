package parkourterminal.global.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import parkourterminal.data.GlobalData;

public class TickEventHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            EntityPlayerSP player= Minecraft.getMinecraft().thePlayer;
            if(player==null){
                return;
            }
            //GlobalData.getInputData().UpdateOperation(player);

        }
        if(event.phase == TickEvent.Phase.END){
            EntityPlayerSP player= Minecraft.getMinecraft().thePlayer;
            if(player==null){
                return;
            }
            GlobalData.getInputData().UpdateOperation(player);
            GlobalData.getLandingBlock().Update(player);
            GlobalData.getLandData().Update(player);
            GlobalData.getJumpData().Update(player);
            GlobalData.getSpeedData().Update(player);
        }
    }

}
