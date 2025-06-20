package parkourterminal.mixins;

import ibxm.Player;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import parkourterminal.freecamera.CameraController;

@Mixin(Render.class)
public class MixinRender {
    @Inject(method = "renderShadow",at=@At("HEAD"),cancellable = true)
    public void renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks, CallbackInfo ci){
        if(entityIn instanceof EntityPlayerSP&& CameraController.FREECAM.isActive()){
            ci.cancel();
        }
    }
}
