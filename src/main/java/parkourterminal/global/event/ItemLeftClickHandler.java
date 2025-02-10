package parkourterminal.global.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import parkourterminal.util.NumberWrapper;

public class ItemLeftClickHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onLeftClick(MouseEvent event) {
        if (event.button != 0 || !event.buttonstate)
            return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack heldItem = player.getHeldItem();

        if (heldItem == null || !heldItem.hasTagCompound())
            return;

        NBTTagCompound nbt = heldItem.getTagCompound();
        if (!nbt.hasKey("savedLocations"))
            return;

        NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
        if (savedLocations.tagCount() < 2)
            return;

        // Calculate new index
        int currentIndex = nbt.getInteger("selectedIndex");
        int newIndex = (currentIndex + 1) % savedLocations.tagCount();
        nbt.setInteger("selectedIndex", newIndex);

        // Hint
        String positionName = savedLocations.getCompoundTagAt(newIndex).getString("name");
        String message = EnumChatFormatting.GREEN + "Switched to: " + positionName;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));

        // Update item display
        String alias = nbt.hasKey("teleporterAlias") ? nbt.getString("teleporterAlias") : "Teleporter";
        String displayName = EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + alias + " " + positionName;
        heldItem.setStackDisplayName(displayName);

        // Update lore
        NBTTagCompound loc = savedLocations.getCompoundTagAt(newIndex);
        NBTTagCompound displayTag = nbt.getCompoundTag("display");
        NBTTagList loreList = new NBTTagList();
        loreList.appendTag(new NBTTagString(EnumChatFormatting.YELLOW + "Stored Position:"));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "X: " + NumberWrapper.round(loc.getDouble("posX"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Y: " + NumberWrapper.round(loc.getDouble("posY"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Z: " + NumberWrapper.round(loc.getDouble("posZ"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Yaw: " + NumberWrapper.round(loc.getFloat("yaw"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Pitch: " + NumberWrapper.round(loc.getFloat("pitch"))));
        displayTag.setTag("Lore", loreList);
        nbt.setTag("display", displayTag);
        heldItem.setTagCompound(nbt);

        event.setCanceled(true);
    }
}