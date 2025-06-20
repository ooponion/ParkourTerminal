package parkourterminal.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import parkourterminal.tests.SpeedTest;
import parkourterminal.util.SystemOutHelper;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {

//    @Shadow protected abstract boolean isCurrentViewEntity();
//
//    @Inject(method = "onUpdate", at = @At("HEAD"))
//    public void onUpdate(CallbackInfo ci) {
//        if(((Object) this instanceof EntityPlayerSP)){
//            //SystemOutHelper.printf("\nmoveEntityWithHeading:%s,%s",strafe,forward);
//            //SpeedTest. predictionTest((EntityPlayerSP)((Object)this));
//            SpeedTest.EntityOnUpdate((EntityPlayerSP)((Object)this));
//        }
//    }
//    @Inject(method = "onUpdate", at = @At("TAIL"))
//    public void onUpdate2(CallbackInfo ci) {
//        if(((Object) this instanceof EntityPlayerSP)){
//            //SystemOutHelper.printf("\nmoveEntityWithHeading:%s,%s",strafe,forward);
//            //SpeedTest. predictionTest((EntityPlayerSP)((Object)this));
//            SpeedTest.EntityOnUpdateVerify((EntityPlayerSP)((Object)this));
//        }
//    }
//    @Inject(method = "onLivingUpdate", at = @At("HEAD"), cancellable = true)
//    public void disablePlayerMotion(CallbackInfo ci) {
//        if (!CameraController.FREECAM.isActive()) return;
//        if (!this.isCurrentViewEntity()) {
//            //SystemOutHelper.printfplain("mixinEntityPlayer::"+ this.toString());
//            ci.cancel();
//        }
//    }

}
