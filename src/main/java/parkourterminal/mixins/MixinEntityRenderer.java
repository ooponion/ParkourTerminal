package parkourterminal.mixins;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import parkourterminal.util.SystemOutHelper;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @Inject(method = "orientCamera", at = @At("HEAD"), cancellable = true)
    private void onOrientCamera(float partialTicks, CallbackInfo ci) {
//        if (CameraController.isFreeCameraEnabled()) {
//            //CameraController.setupFreeCameraView(partialTicks);
//            Minecraft mc = Minecraft.getMinecraft();
//            Entity entity = mc.getRenderViewEntity();
//            float f = entity.getEyeHeight();
//            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
//            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
//            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
//            GlStateManager.translate(0.0F, 0.0F, -0.1F);
//
//            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
//            float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
//            float roll = 0.0F;
//            if (entity instanceof EntityAnimal)
//            {
//                EntityAnimal entityanimal = (EntityAnimal)entity;
//                yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
//            }
//            Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(mc.theWorld, entity, partialTicks);
//             GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
//            GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
//            GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
//            GlStateManager.translate(0.0F, -f,0);
//            ci.cancel();
//        }
    }
//    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE",
//            target = "Lnet/minecraft/client/renderer/culling/ICamera;setPosition(DDD)V",
//            shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILHARD)
//    private void getCameraRenderPostition(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci, RenderGlobal renderglobal, EffectRenderer effectrenderer, boolean flag, ICamera icamera, Entity entity, double d0, double d1, double d2){
//        SystemOutHelper.printfplain("getCameraRenderEnt:%s",entity);
//    }

}