package parkourterminal.tests;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import parkourterminal.simulation.sim.PlayerState;

public class SpeedTest {
    private static PlayerState playerState;
    public static void INITState(){
        playerState=new PlayerState(Minecraft.getMinecraft().thePlayer);
    }
    public static void EntityOnUpdate(EntityPlayerSP player){
        if(playerState==null){
            return;
        }
    }
    public static void EntityOnUpdateVerify(EntityPlayerSP player){
        if(playerState==null){
            return;
        }
//        if(Math.abs(playerState.posX-player.posX)>=1e-10){
//            SendMessageHelper.addChatMessage(player,"testfailsX: P "+playerState.posX+"::"+player.posX);
//        }
//        if(Math.abs(playerState.posY-player.posY)>=1e-10){
//            SendMessageHelper.addChatMessage(player,"testfailsY: P "+playerState.posY+"::"+player.posY);
//        }
//        if(Math.abs(playerState.posZ-player.posZ)>=1e-10){
//            SendMessageHelper.addChatMessage(player,"testfailsZ: P "+playerState.posZ+"::"+player.posZ);
//        }
//        if(Math.abs(playerState.motionX-player.motionX)>=1e-10){
//            SendMessageHelper.addChatMessage(player,"testfailsX: M "+playerState.motionX+"::"+player.motionX);
//        }
//        if(Math.abs(playerState.motionY-player.motionY)>=1e-10){
//            SendMessageHelper.addChatMessage(player,"testfailsY: M "+playerState.motionY+"::"+player.motionY);
//        }
//        if(Math.abs(playerState.motionZ-player.motionZ)>=1e-10){
//            SendMessageHelper.addChatMessage(player,"testfailsZ: M "+playerState.motionZ+"::"+player.motionZ);
//        }
//        if(Math.abs(playerState.rotationYaw-player.rotationYaw)>=1e-10){
//            SendMessageHelper.addChatMessage(player,"testfailsYaw: "+playerState.rotationYaw+"::"+player.rotationYaw);
//        }
    }
}

