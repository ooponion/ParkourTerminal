package parkourterminal.global.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import parkourterminal.gui.ShiftRightClickGui;
import parkourterminal.network.NetworkLoader;
import parkourterminal.network.TeleportPacket;

public class ItemRightClickHandler {
    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            EntityPlayer player = event.entityPlayer;
            World world = player.worldObj;
            ItemStack heldItem = player.getHeldItem();

            if (heldItem != null && heldItem.hasTagCompound()) {
                NBTTagCompound nbt = heldItem.getTagCompound();
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                int selectedIndex = nbt.hasKey("selectedIndex") ? nbt.getInteger("selectedIndex") : 0;

                if (savedLocations.tagCount() > 0 && selectedIndex < savedLocations.tagCount()) {
                    if (world.isRemote && player.isSneaking()) {
                        Minecraft.getMinecraft().displayGuiScreen(new ShiftRightClickGui());
                        event.setCanceled(true);
                        return;
                    }

                    // 传送逻辑
                    NBTTagCompound location = savedLocations.getCompoundTagAt(selectedIndex);

                    double x = location.getDouble("posX");
                    double y = location.getDouble("posY");
                    double z = location.getDouble("posZ");
                    float yaw = location.getFloat("yaw");
                    float pitch = location.getFloat("pitch");

                    player.setPositionAndRotation(x, y, z, yaw, pitch);  // 传送玩家并更新视角

                    player.motionX = 0;
                    player.motionY = 0;
                    player.motionZ = 0;

                    resetAllKeyBindings();

                    TeleportPacket packet = new TeleportPacket(location);
                    NetworkLoader.NETWORK_WRAPPER.sendToServer(packet);

                    event.setCanceled(true);
                }
            }
        }
    }

    private void resetAllKeyBindings() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.gameSettings != null) {
            KeyBinding[] movementKeys = new KeyBinding[]{
                    mc.gameSettings.keyBindForward,  // W
                    mc.gameSettings.keyBindBack,     // S
                    mc.gameSettings.keyBindLeft,     // A
                    mc.gameSettings.keyBindRight,    // D
            };

            for (KeyBinding key : movementKeys) {
                KeyBinding.setKeyBindState(key.getKeyCode(), false);  // 立即释放按键
            }
        }
    }
}