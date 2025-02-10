package parkourterminal.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class TeleporterHelper {
    // 切换玩家手持物品 selectedIndex，并更新显示名称和 Lore
    public static boolean switchSelectedIndex(EntityPlayer player, int index) {
        ItemStack heldItem = player.getHeldItem();

        if (heldItem == null || !heldItem.hasTagCompound()) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No valid item!"));
            return false;
        }

        NBTTagCompound nbt = heldItem.getTagCompound();
        if (!nbt.hasKey("savedLocations")) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No saved locations!"));
            return false;
        }

        NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
        if (index < 0 || index >= savedLocations.tagCount()) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid index!"));
            return false;
        }

        // 更新 selectedIndex
        nbt.setInteger("selectedIndex", index);
        NBTTagCompound loc = savedLocations.getCompoundTagAt(index);
        String name = loc.getString("name");

        heldItem.setTagCompound(nbt);

        String teleporterAlias = nbt.getString("teleporterAlias");
        String displayName = EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + teleporterAlias + " " + name;
        heldItem.setStackDisplayName(displayName);

        // 更新 Lore
        NBTTagCompound displayTag = nbt.hasKey("display") ? nbt.getCompoundTag("display") : new NBTTagCompound();
        NBTTagList loreList = new NBTTagList();
        loreList.appendTag(new NBTTagString(EnumChatFormatting.YELLOW + "Stored Position:"));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "X: " + NumberWrapper.round(loc.getDouble("posX"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Y: " + NumberWrapper.round(loc.getDouble("posY"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Z: " + NumberWrapper.round(loc.getDouble("posZ"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Yaw: " + NumberWrapper.round(loc.getFloat("yaw"))));
        loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Pitch: " + NumberWrapper.round(loc.getFloat("pitch"))));
        displayTag.setTag("Lore", loreList);
        nbt.setTag("display", displayTag);

        player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Switched to Position #" + index));
        return true;
    }
}
