package parkourterminal.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import parkourterminal.freecamera.CameraController;

import java.lang.reflect.Field;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {

    @Inject(
            method = "addToSendQueue",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onAddToSendQueue(net.minecraft.network.Packet packet, CallbackInfo ci) {
        if(!CameraController.FREECAM.shouldBlockInteraction()){
            return;
        }
        if (packet instanceof C08PacketPlayerBlockPlacement || // 方块放置
                packet instanceof C07PacketPlayerDigging ||       // 方块破坏
                packet instanceof C02PacketUseEntity ||            // 实体交互
                packet instanceof C09PacketHeldItemChange ||      // 手持物品切换
                packet instanceof C0APacketAnimation) {           // 动画（攻击）
            gthModTerminal$resetBlockPlacementState();
            ci.cancel();
        }

    }
    @Unique
    private void gthModTerminal$resetBlockPlacementState() {
        PlayerControllerMP controller = Minecraft.getMinecraft().playerController;
//        controller.resetBlockRemoving();
        try {
            Field curBlockDamageMP = ReflectionHelper.findField(
                    PlayerControllerMP.class,
                    "field_78778_j",
                    "curBlockDamageMP"
            );

            Field stepSoundTickCounter = ReflectionHelper.findField(
                    PlayerControllerMP.class,
                    "field_78779_i",
                    "stepSoundTickCounter"
            );

            // 设置字段可访问
            curBlockDamageMP.setAccessible(true);
            stepSoundTickCounter.setAccessible(true);

            // 重置字段值
            curBlockDamageMP.setFloat(controller, 0.0f);
            stepSoundTickCounter.setFloat(controller, 0.0f);
        } catch (Exception e) {
        }
    }
}
