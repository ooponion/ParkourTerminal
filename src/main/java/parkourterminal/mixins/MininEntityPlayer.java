package parkourterminal.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import parkourterminal.util.SendMessageHelper;

@Mixin(EntityPlayer.class)
public class MininEntityPlayer {
//    @Inject(method = "moveEntityWithHeading", at = @At(target = "Lnet/minecraft/entity/EntityLivingBase;moveEntityWithHeading(FF)V" ,shift = At.Shift.BEFORE,value = "INVOKE"))
//    public void EntityOnLivingUpdate(float strafe, float forward, CallbackInfo ci) {
//        if(((Object) this instanceof EntityCamera)){
//            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onLivingUpdate1: %s   %s,%s",strafe,forward,((EntityCamera)(Object) this).motionX));
//        }
//    }
//    @Inject(method = "onUpdate", at = @At("HEAD"))
//    public void EntityOnUpdate(CallbackInfo ci) {
//        SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onUpdate: %s", ((EntityPlayer)(Object) this).worldObj.isRemote));
//
//    }
}
