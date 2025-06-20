package parkourterminal.mixins;

import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import parkourterminal.util.SystemOutHelper;

@Mixin(World.class)
public class MixinWorld {
    @Inject(
            method = "handleMaterialAcceleration",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn, CallbackInfoReturnable<Boolean> cir) {
        if(((Object) entityIn instanceof EntityPlayerSP)){
            Entity entity = (Entity)(Object)entityIn;
//            SystemOutHelper.printfCaller("beforeWaterMovement:%s:%s",5,entity.motionX,entity.motionZ);
        }
    }
    @Inject(
            method = "handleMaterialAcceleration",
            at = @At("TAIL"),
            cancellable = true
    )
    private void handleMaterialAcceleration2(AxisAlignedBB bb, Material materialIn, Entity entityIn, CallbackInfoReturnable<Boolean> cir) {
        if(((Object)entityIn instanceof EntityPlayerSP)){
            Entity entity = (Entity)(Object)entityIn;
//            SystemOutHelper.printfCaller("afterWaterMovement:%s:%s",5,entity.motionX,entity.motionZ);
        }
    }
}
