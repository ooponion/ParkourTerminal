package parkourterminal.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import parkourterminal.freecamera.CameraController;
import parkourterminal.util.SendMessageHelper;

@Mixin(Entity.class)
public class MixinEntity{
//    @Inject(method = "moveEntity", at = @At("HEAD"))
//    public void moveEntity(double x, double y, double z,CallbackInfo ci) {
//        if(((Object) this instanceof EntityPlayerSP)){
//            SystemOutHelper.printf("\nmoveEntity");
//            SpeedTest.moveEntityTest((EntityPlayerSP)((Object)this),x,y,z);
//        }
//    }
//    @Inject(method = "moveEntity", at = @At("TAIL"))
//    public void moveEntity2(double x, double y, double z,CallbackInfo ci) {
//        if(((Object) this instanceof EntityPlayerSP)){
//            SpeedTest.moveEntityVerify((EntityPlayerSP)((Object)this));
//        }
//    }
//    @Redirect(
//            method = "onEntityUpdate",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/entity/Entity;handleWaterMovement()Z"
//            )
//    )
//    private boolean redirectDoSomething(Entity instance) {
//        // 空实现，相当于删除 SomeClass.doSomething() 调用
//        return false;
//    }
//    @Inject(
//            method = "handleWaterMovement",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void cancelHandleWaterMovement(CallbackInfoReturnable<Boolean> cir) {
//        if(((Object) this instanceof Entity)){
//            Entity entity = (Entity)(Object)this;
//            SystemOutHelper.printf("beforeWaterMovement:%s:%s",entity.motionX,entity.motionZ);
//        }
//    }
//    @Inject(
//            method = "handleWaterMovement",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void cancelHandleWaterMovement2(CallbackInfoReturnable<Boolean> cir) {
//        if(((Object) this instanceof Entity)){
//            Entity entity = (Entity)(Object)this;
//            SystemOutHelper.printf("afterWaterMovement:%s:%s",entity.motionX,entity.motionZ);
//        }
//    }
//    @Inject(
//            method = "isInsideOfMaterial",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;isEntityInsideMaterial(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/Entity;DLnet/minecraft/block/material/Material;Z)Ljava/lang/Boolean;"),
//            cancellable = true
//    )
//    private void isInsideOfMaterial(CallbackInfoReturnable<Boolean> cir) {
//        if(((Object) this instanceof Entity)){
//            Entity entity = (Entity)(Object)this;
//            System.out.printf("isEntityInsideMaterial:%s\n",cir.getReturnValue()==null);
//        }
//    }
    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    public void onSetAngles(float yawDelta, float pitchDelta, CallbackInfo ci) {
        if(((Object) this instanceof EntityPlayerSP)) {
            if (CameraController.FREECAM.isActive()) {
                ci.cancel();
                CameraController.FREECAM.setYawAndPitch(yawDelta,pitchDelta);
            }
        }

    }
    @Inject(method = "spawnRunningParticles", at = @At("HEAD"), cancellable = true)
    public void spawnRunningParticles(CallbackInfo ci)
    {
        if((Object)this instanceof EntityPlayerSP&&CameraController.FREECAM.isActive()) {
            ci.cancel();
        }
    }
}
