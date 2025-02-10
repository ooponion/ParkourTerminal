package parkourterminal.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.TeleporterHelper;

public class CommandSaveLocation extends CommandBase {
    @Override
    public String getCommandName() {
        return "sl";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sl";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 2) {
            sender.addChatMessage(new ChatComponentText("Usage: /sl | /sl <alias> | /sl switch <index>"));
            return;
        }

        if (args.length >= 1 && args[0].equals("switch")) {
            if (!(sender instanceof EntityPlayer))
                return;

            EntityPlayer player = (EntityPlayer) sender;

            if (args.length < 2) {
                sender.addChatMessage(new ChatComponentText("Usage: /sl switch <index>"));
                return;
            }

            try {
                int index = Integer.parseInt(args[1]);
                TeleporterHelper.switchSelectedIndex(player, index);
            } catch (NumberFormatException e) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Index must be a number!"));
            }
            return;
        }

        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack heldItem = player.getHeldItem();

            if (heldItem != null) {
                NBTTagCompound nbt = heldItem.hasTagCompound() ? heldItem.getTagCompound() : new NBTTagCompound();

                // Get or create savedLocation list
                NBTTagList savedLocations = nbt.hasKey("savedLocations") ? nbt.getTagList("savedLocations", 10) : new NBTTagList();

                // Create a new location entry
                NBTTagCompound location = new NBTTagCompound();
                String positionName = "Position #" + (savedLocations.tagCount());

                location.setString("name", positionName);
                location.setDouble("posX", player.posX);
                location.setDouble("posY", player.posY);
                location.setDouble("posZ", player.posZ);
                location.setFloat("yaw", player.rotationYaw);
                location.setFloat("pitch", player.rotationPitch);

                // Add to list
                savedLocations.appendTag(location);
                nbt.setTag("savedLocations", savedLocations);

                // Update selected index to the latest
                nbt.setInteger("selectedIndex", savedLocations.tagCount() - 1);

                heldItem.setTagCompound(nbt);

                String teleporterAlias = (args.length == 1) ? args[0] : "Teleporter";
                nbt.setString("teleporterAlias", teleporterAlias);

                String displayName = EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + teleporterAlias + " " + positionName;
                heldItem.setStackDisplayName(displayName);

                NBTTagCompound displayTag = nbt.hasKey("display") ? nbt.getCompoundTag("display") : new NBTTagCompound();
                NBTTagList loreList = new NBTTagList();

                loreList.appendTag(new NBTTagString(EnumChatFormatting.YELLOW + "Stored Position:"));
                loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "X: " + NumberWrapper.round(player.posX)));
                loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Y: " + NumberWrapper.round(player.posY)));
                loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Z: " + NumberWrapper.round(player.posZ)));
                loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Yaw: " + NumberWrapper.round(player.rotationYaw)));
                loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Pitch: " + NumberWrapper.round(player.rotationPitch)));

                displayTag.setTag("Lore", loreList);
                nbt.setTag("display", displayTag);

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Position has been saved!"));
            } else {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Please hold an item at first!"));
            }
        }
    }
}