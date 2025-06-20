package parkourterminal.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import parkourterminal.util.SystemOutHelper;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "getRenderViewEntity", at = @At("HEAD"), cancellable = true)
    private void onGetRenderViewEntity(CallbackInfoReturnable<Entity> cir) {
//        if (CameraController.isFreeCameraEnabled()) {
//            cir.setReturnValue(CameraController.getCamera());
//            cir.cancel();
//        }
    }
}