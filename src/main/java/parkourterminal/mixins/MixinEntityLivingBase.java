package parkourterminal.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import parkourterminal.data.GlobalData;
import parkourterminal.tests.SpeedTest;
import parkourterminal.util.SendMessageHelper;
import parkourterminal.util.SystemOutHelper;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {
    @Shadow public float moveStrafing;

    @Shadow public float moveForward;

    @Shadow protected boolean isJumping;

    @Shadow public abstract boolean isServerWorld();

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void EntityOnUpdate(CallbackInfo ci) {
        if(((Object) this instanceof EntityPlayerSP)){
//            SystemOutHelper.printf("\nonLivingUpdate");
            //SpeedTest.EntityOnUpdate((EntityPlayerSP)((Object)this));
        }
        if(((Object) this instanceof EntityPlayerSP)){
            EntityPlayerSP player= (EntityPlayerSP)((Object)this);
            if(player==null){
                return;
            }
//            if(GlobalData.getInputData().getOperation().isJump()!=player.movementInput.jump){
//                SendMessageHelper.addChatMessage(player,"testfails: J "+GlobalData.getInputData().getOperation().isJump()+"::"+player.movementInput.jump);
//            }
        }
    }
//    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
//    public void EntityOnLivingUpdate2(CallbackInfo ci) {
//        if(((Object) this instanceof EntityPlayerSP)){
//            SpeedTest.EntityOnLivingUpdateVerify((EntityPlayerSP)((Object)this));
//        }
//    }
//    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
//    public void EntityOnLivingUpdate(CallbackInfo ci) {
//        if(((Object) this instanceof EntityCamera)){
//            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onLivingUpdate1: %s:   %s,%s,%s",isJumping,((EntityCamera) (Object) this).motionX,((EntityCamera) (Object) this).motionY,((EntityCamera) (Object) this).motionZ));
//        }
//    }
//    @Inject(method = "onLivingUpdate", at = @At(target = "Lnet/minecraft/entity/EntityLivingBase;setPosition(DDD)V",value = "INVOKE"),locals = LocalCapture.CAPTURE_FAILHARD)
//    public void EntityOnLivingUpdate2(CallbackInfo ci, double d0, double d1, double d2, double d3) {
//        if(((Object) this instanceof EntityCamera)){
//            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onLivingUpdate2: %s:   %s,%s,%s",isJumping,d0,d1,d2));
//        }
//    }
//    @Inject(method = "onLivingUpdate", at = @At(target = "Lnet/minecraft/entity/EntityLivingBase;jump()V",value = "INVOKE",shift = At.Shift.AFTER))
//    public void EntityOnLivingUpdate3(CallbackInfo ci) {
//        if(((Object) this instanceof EntityCamera)){
//            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onLivingUpdate3: %s:   %s,%s,%s",isJumping,((EntityCamera) (Object) this).motionX,((EntityCamera) (Object) this).motionY,((EntityCamera) (Object) this).motionZ));
//        }
//    }
//    @Inject(method = "moveEntityWithHeading", at = @At("HEAD"))
//    public void moveEntityWithHeading(float strafe, float forward, CallbackInfo ci) {
//        if(((Object) this instanceof EntityCamera)){
//            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onLivingUpdate4: %s,%s,%s:   %s,%s,%s",strafe,forward,isServerWorld(),((EntityCamera) (Object) this).motionX,((EntityCamera) (Object) this).motionY,((EntityCamera) (Object) this).motionZ));
//        }
//    }
//    @Inject(method = "moveEntityWithHeading", at = @At(target = "Lnet/minecraft/entity/EntityLivingBase;moveFlying(FFF)V",value = "INVOKE",shift = At.Shift.AFTER))
//    public void moveEntityWithHeading2(float strafe, float forward, CallbackInfo ci) {
//        if(((Object) this instanceof EntityCamera)){
//            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onLivingUpdate5: %s,%s:   %s,%s,%s",strafe,forward,((EntityCamera) (Object) this).motionX,((EntityCamera) (Object) this).motionY,((EntityCamera) (Object) this).motionZ));
//        }
//    }
//    @Inject(method = "onLivingUpdate", at = @At(value = "TAIL"))
//    public void EntityOnLivingUpdate2(CallbackInfo ci) {
//        if(((Object) this instanceof EntityCamera)){
//            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, String.format("onLivingUpdate2: %s", ((EntityCamera)(Object) this).motionX));
//        }
//    }
}
