package parkourterminal.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SaveCoordinatePacket implements IMessage {
    private NBTTagCompound coordinateData;

    public SaveCoordinatePacket() {}

    public SaveCoordinatePacket(NBTTagCompound data) {
        this.coordinateData = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        coordinateData = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, coordinateData);
    }

    public static class Handler implements IMessageHandler<SaveCoordinatePacket, IMessage> {
        @Override
        public IMessage onMessage(final SaveCoordinatePacket message, final MessageContext ctx) {
            // 使用匿名内部类替代 Lambda 表达式
            ctx.getServerHandler().playerEntity.mcServer.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                    ItemStack heldItem = player.inventory.getCurrentItem();
                    if (heldItem != null && heldItem.getItem() != null) { // 替换为实际物品类
                        NBTTagCompound nbt = heldItem.getTagCompound();
                        if (nbt != null && nbt.hasKey("savedLocations")) {
                            NBTTagList locations = nbt.getTagList("savedLocations", 10);

                            // 更新逻辑（同客户端代码）
                            for (int i = 0; i < locations.tagCount(); i++) {
                                NBTTagCompound entry = locations.getCompoundTagAt(i);
                                if (entry.getString("name").equals(message.coordinateData.getString("name"))) {
                                    locations.set(i, message.coordinateData);
                                    break;
                                }
                            }
                            heldItem.setTagCompound(nbt);
                        }
                    }
                }
            });
            return null;
        }
    }
}