package parkourterminal.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import parkourterminal.util.NumberWrapper;

public class DeleteCoordinatePacket implements IMessage {
    private String coordinateName;
    private boolean indexChanged;
    private int newIndex;
    private boolean isLastEntry;

    public DeleteCoordinatePacket() {}

    public DeleteCoordinatePacket(String name, boolean isChanged, int newIndex, boolean isLastEntry) {
        this.coordinateName = name;
        this.indexChanged = isChanged;
        this.newIndex = newIndex;
        this.isLastEntry = isLastEntry;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        coordinateName = ByteBufUtils.readUTF8String(buf);
        indexChanged = buf.readBoolean();
        newIndex = buf.readInt();
        isLastEntry = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, coordinateName);
        buf.writeBoolean(indexChanged);
        buf.writeInt(newIndex);
        buf.writeBoolean(isLastEntry);
    }

    public static class Handler implements IMessageHandler<DeleteCoordinatePacket, IMessage> {
        @Override
        public IMessage onMessage(final DeleteCoordinatePacket message, final MessageContext ctx) {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            player.mcServer.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    ItemStack heldItem = player.inventory.getCurrentItem();

                    // 有效性验证
                    if (heldItem == null ||
                            heldItem.getItem() == null || // 替换为你的物品类
                            !heldItem.hasTagCompound()) {
                        return;
                    }

                    NBTTagCompound nbt = heldItem.getTagCompound();
                    if (!nbt.hasKey("savedLocations")) return;

                    NBTTagList locations = nbt.getTagList("savedLocations", 10);
                    boolean found = false;

                    // 遍历查找目标坐标
                    for (int i = locations.tagCount() - 1; i >= 0; i--) {
                        NBTTagCompound entry = locations.getCompoundTagAt(i);
                        if (entry.getString("name").equals(message.coordinateName)) {
                            locations.removeTag(i);
                            found = true;
                            break;
                        }
                    }

                    if (!found) return;

                    // 重命名剩余条目
                    for (int i = 0; i < locations.tagCount(); i++) {
                        NBTTagCompound entry = locations.getCompoundTagAt(i);
                        entry.setString("name", "Position #" + i); // 重命名为 "Position #1", "Position #2", 等等
                    }

                    // 更新选中索引
                    if (message.indexChanged && locations.tagCount() > 0) {
                        nbt.setInteger("selectedIndex", message.newIndex);

                        // 同步更新物品显示
                        NBTTagCompound selectedLoc = locations.getCompoundTagAt(message.newIndex);
                        String teleporterAlias = nbt.getString("teleporterAlias");
                        String displayName = EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD
                                + teleporterAlias + " " + selectedLoc.getString("name");
                        heldItem.setStackDisplayName(displayName);

                        // 更新Lore
                        NBTTagCompound displayTag = nbt.hasKey("display") ?
                                nbt.getCompoundTag("display") : new NBTTagCompound();
                        NBTTagList loreList = new NBTTagList();

                        loreList.appendTag(new NBTTagString(EnumChatFormatting.YELLOW + "Stored Position:"));
                        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "X: "
                                + NumberWrapper.round(selectedLoc.getDouble("posX"))));
                        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Y: "
                                + NumberWrapper.round(selectedLoc.getDouble("posY"))));
                        loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Z: "
                                + NumberWrapper.round(selectedLoc.getDouble("posZ"))));
                        loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Yaw: "
                                + NumberWrapper.round(selectedLoc.getFloat("yaw"))));
                        loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Pitch: "
                                + NumberWrapper.round(selectedLoc.getFloat("pitch"))));

                        displayTag.setTag("Lore", loreList);
                        nbt.setTag("display", displayTag);
                    }

                    // 处理空列表情况
                    if (locations.tagCount() == 0) {
                        nbt.removeTag("savedLocations");
                        nbt.removeTag("selectedIndex");

                        // 还原显示名称和Lore
                        if (nbt.hasKey("display")) {
                            NBTTagCompound displayTag = nbt.getCompoundTag("display");
                            if (displayTag.hasKey("Name")) {
                                displayTag.removeTag("Name"); // 移除自定义名称
                            }
                            if (displayTag.hasKey("Lore")) {
                                displayTag.removeTag("Lore"); // 移除Lore
                            }
                            if (displayTag.hasNoTags()) {
                                nbt.removeTag("display"); // 如果display标签为空则移除
                            }
                        }

                        if (nbt.hasNoTags()) {
                            heldItem.setTagCompound(null);
                        } else {
                            heldItem.setTagCompound(nbt);
                        }
                    } else {
                        nbt.setTag("savedLocations", locations);
                        heldItem.setTagCompound(nbt);
                    }

                    // 强制同步更新
                    player.inventory.markDirty();
                    player.inventoryContainer.detectAndSendChanges();
                }
            });
            return null;
        }
    }
}