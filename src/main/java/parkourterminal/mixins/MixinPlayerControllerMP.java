package parkourterminal.mixins;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import parkourterminal.freecamera.CameraController;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        if (CameraController.FREECAM.shouldBlockInteraction()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
    @Inject(method = "onPlayerDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void onPlayerDestroyBlock(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        if (CameraController.FREECAM.shouldBlockInteraction()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}