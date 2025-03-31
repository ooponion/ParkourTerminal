package parkourterminal.global.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.data.inputdata.InputData;

public class TickEventHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            EntityPlayerSP player= Minecraft.getMinecraft().thePlayer;
            if(player==null){
                return;
            }
            GlobalData.getLandingBlock().Update(player);
            GlobalData.getInputData().UpdateOperation(player);
        }
    }
}
