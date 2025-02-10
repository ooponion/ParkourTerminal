package parkourterminal.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TeleportPacket implements IMessage {
    private NBTTagCompound location;

    public TeleportPacket() { }

    public TeleportPacket(NBTTagCompound location) {
        this.location = location;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, location);
    }

    public static class Handler implements IMessageHandler<TeleportPacket, IMessage> {
        @Override
        public IMessage onMessage(final TeleportPacket message, final MessageContext ctx) {
            ctx.getServerHandler().playerEntity.mcServer.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                    if (message.location != null) {
                        double x = message.location.getDouble("posX");
                        double y = message.location.getDouble("posY");
                        double z = message.location.getDouble("posZ");
                        float yaw = message.location.getFloat("yaw");
                        float pitch = message.location.getFloat("pitch");

                        player.setPositionAndRotation(x, y, z, yaw, pitch);
                        player.motionX = 0;
                        player.motionY = 0;
                        player.motionZ = 0;
                    }
                }
            });

            return null;
        }
    }
}
